package crossai.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import crossai.model.Genre;
import crossai.model.Item;
import crossai.model.User;

/**
 * Hybrid recommendation service that combines rule-based filtering with ML.
 * Communicates with C++ engine via JSON files for cross-language integration.
 * 
 * This service writes user preferences to input.json and reads recommendations
 * from output.json, enabling communication with the C++ and Python layers.
 */

public class HybridRecommendationService extends BaseRecommendationService {
    private Gson gson;
    private String inputFilePath;
    private String outputFilePath;
    private BufferedWriter currentWriter;
    private BufferedReader currentReader;

    public HybridRecommendationService(String dataDirectory) {
        super(dataDirectory);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.inputFilePath = dataDirectory + "/input.json";
        this.outputFilePath = dataDirectory + "/output.json";
        this.currentWriter = null;
        this.currentReader = null;
    }

    @Override
    public List<Item> getRecommendations(User user) {
    // validate user 
    validateUser(user);
    
    // check cache first
    String cacheKey = "user_" + user.getName() + "_" + user.getAge();
    Optional<List<Item>> cachedResult = cache.get(cacheKey);
    
    if (cachedResult.isPresent()) {
        logRecommendation(user, cachedResult.get().size());
        if (loggingEnabled) {
            System.out.println("[CACHE] Returning cached recommendations");
        }
        return cachedResult.get();
    }
    
    try {
        // Step 1: Write user data to input.json
        writeUserToJson(user);
        
        // Step 2: Call C++ engine
        callCppEngine();
        
        // Step 3: Read recommendations from output.json
        List<Item> recommendations = readRecommendationsFromJson();
        
        // Step 4: Cache the results
        cache.put(cacheKey, recommendations);
        
        // Step 5: Log the recommendation event
        logRecommendation(user, recommendations.size());
        
        return recommendations;
        
    } catch (IOException e) {
        return handleError(e, user);
    } catch (InterruptedException e) {
        System.err.println("[ERROR] C++ engine was interrupted: " + e.getMessage());
        return handleError(new IOException("C++ engine interrupted"), user);
    } finally {
        try {
            close();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to close resources: " + e.getMessage());
        }
    }
    }

    private void writeUserToJson(User user) throws IOException {
        // JSON structure
        JsonObject root = new JsonObject();
        JsonObject userObj = new JsonObject();

        userObj.addProperty("name", user.getName());
        userObj.addProperty("age", user.getAge());

        // convert Genre enum list to String array for json
        JsonArray genresArray = new JsonArray();
        for (Genre genre : user.getPreferredGenres()) {
            genresArray.add(genre.getDisplayName());
        }
        userObj.add("preferredGenres", genresArray);

        root.add("user", userObj);
        root.addProperty("action", "GET_RECOMMENDATIONS");

        // write to file
        currentWriter = new BufferedWriter(new FileWriter(inputFilePath));
        currentWriter.write(gson.toJson(root));
        currentWriter.flush();

        if (loggingEnabled) {
            System.out.println("[FILE I/O] Wrote user data to: " + inputFilePath);
        }

    }

    /**
     * Call the C++ recommendation engine.
     * Executes the C++ executable and waits for it to complete.
     * 
     * @throws IOException if engine execution fails
     * @throws InterruptedException if engine is interrupted
     */

    private void callCppEngine() throws IOException, InterruptedException {
        if (loggingEnabled) {
            System.out.println("[C++ ENGINE] Calling C++ recommendation engine...");
        }
        
        // Path to C++ executable (relative to java/ directory)
        String cppEnginePath = "../cpp/build/Debug/crossai-engine.exe";
        
        // Check if file exists
        File engineFile = new File(cppEnginePath);
        if (!engineFile.exists()) {
            // Try Release build
            cppEnginePath = "../cpp/build/Release/crossai-engine.exe";
            engineFile = new File(cppEnginePath);
            
            if (!engineFile.exists()) {
                // Try Linux/Mac path (no Debug/Release folder)
                cppEnginePath = "../cpp/build/crossai-engine";
                engineFile = new File(cppEnginePath);
                
                if (!engineFile.exists()) {
                    throw new IOException("C++ engine not found. Build it first with: cd cpp/build && cmake --build .");
                }
            }
        }
        
        if (loggingEnabled) {
            System.out.println("[C++ ENGINE] Using engine: " + engineFile.getAbsolutePath());
        }
        
        // Execute the C++ engine
        ProcessBuilder processBuilder = new ProcessBuilder(engineFile.getAbsolutePath());
        processBuilder.redirectErrorStream(true); // Combine stdout and stderr
        
        // CRITICAL: Set working directory to cpp/build/ so relative paths work
        File cppBuildDir = new File("../cpp/build");
        processBuilder.directory(cppBuildDir.getAbsoluteFile());
        
        if (loggingEnabled) {
            System.out.println("[C++ ENGINE] Working directory: " + cppBuildDir.getAbsolutePath());
        }
        
        Process process = processBuilder.start();
        
        // Capture output for logging
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (loggingEnabled) {
                    System.out.println("[C++ ENGINE] " + line);
                }
            }
        }
        
        // Wait for process to complete
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            throw new IOException("C++ engine failed with exit code: " + exitCode);
        }
        
        if (loggingEnabled) {
            System.out.println("[C++ ENGINE] Engine completed successfully");
        }
}



    private List<Item> readRecommendationsFromJson() throws IOException {
    // Check if output file exists
    if (!Files.exists(Paths.get(outputFilePath))) {
        System.err.println("[WARNING] Output file not found: " + outputFilePath);
        System.err.println("[INFO] Creating sample output file for testing...");
        createSampleOutputFile();
    }
    
    // Read the file
    currentReader = new BufferedReader(new FileReader(outputFilePath));
    StringBuilder jsonContent = new StringBuilder();
    String line;
    
    while ((line = currentReader.readLine()) != null) {
        jsonContent.append(line);
    }
    
    if (loggingEnabled) {
        System.out.println("[FILE I/O] Read recommendations from: " + outputFilePath);
    }
    
    // Parse JSON
    JsonObject root = gson.fromJson(jsonContent.toString(), JsonObject.class);
    JsonArray recommendationsArray = root.getAsJsonArray("recommendations");
    
    List<Item> items = new ArrayList<>();
    
    if (recommendationsArray != null) {
        for (JsonElement element : recommendationsArray) {
            JsonObject itemObj = element.getAsJsonObject();
            
            int id = itemObj.get("id").getAsInt();
            String title = itemObj.get("title").getAsString();
            String description = itemObj.has("description") ? 
                                itemObj.get("description").getAsString() : "";
            
            // Parse genres array
            List<String> genres = new ArrayList<>();
            if (itemObj.has("genres")) {
                JsonArray genresArray = itemObj.getAsJsonArray("genres");
                for (JsonElement genreElement : genresArray) {
                    genres.add(genreElement.getAsString());
                }
            }
            
            // Parse rating
            double rating = 0.0;
            if (itemObj.has("rating")) {
                rating = itemObj.get("rating").getAsDouble();
            } else if (itemObj.has("vote_average")) {
                // Python ML returns "vote_average"
                rating = itemObj.get("vote_average").getAsDouble();
            }
            
            items.add(new Item(id, title, description, genres, rating));
        }
    }
    
    return items;
}

    /**
     * Create a sample output file for testing before C++ engine is ready.
     * This is a temporary helper method.
     * 
     * @throws IOException if file creation fails
     */

    private void createSampleOutputFile() throws IOException {
        JsonObject root = new JsonObject();
        JsonArray recommendations = new JsonArray();
        
        // Sample movies
        JsonObject movie1 = new JsonObject();
        movie1.addProperty("id", 1);
        movie1.addProperty("title", "Inception");
        movie1.addProperty("description", "A mind-bending thriller about dreams within dreams");
        recommendations.add(movie1);
        
        JsonObject movie2 = new JsonObject();
        movie2.addProperty("id", 2);
        movie2.addProperty("title", "The Matrix");
        movie2.addProperty("description", "A hacker discovers the reality is a simulation");
        recommendations.add(movie2);
        
        JsonObject movie3 = new JsonObject();
        movie3.addProperty("id", 3);
        movie3.addProperty("title", "Interstellar");
        movie3.addProperty("description", "A team of explorers travel through a wormhole in space");
        recommendations.add(movie3);
        
        root.add("recommendations", recommendations);
        root.addProperty("status", "success");
        root.addProperty("message", "Sample recommendations generated");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(gson.toJson(root));
            writer.flush();
        }
        
        System.out.println("[INFO] Sample output file created at: " + outputFilePath);
    }

    // close any open file resources - destructor pattern
    public void close() throws IOException {
        if (currentWriter != null) {
            currentWriter.close();
            currentWriter = null;
        }
        if (currentReader != null) {
            currentReader.close();
            currentReader = null;
        }
    }

    public String getInputFilePath() { return inputFilePath; }
    public String getOutputFilePath() { return outputFilePath; }
}
