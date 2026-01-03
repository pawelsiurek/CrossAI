# CrossAI - Development Handoff Document

## 🚀 Current Project Status

**Only the Java layer is currently working.** C++ and Python components have not been started yet.
# ogolnie claude to giga ogarnia wiec na luzie
---

## ✅ What We've Completed So Far

### Java Components:

1. **Project Structure**
   - Maven build system configured (`pom.xml`)
   - Package structure: `crossai.model`, `crossai.service`, `crossai.controller`
   - Dependencies: Gson (JSON), JUnit (testing)

2. **Model Layer** (`java/src/crossai/model/`)
   - ✅ `Genre.java` - Enum with all 19 TMDB movie genres (matches Kaggle dataset)
   - ✅ `User.java` - User class with name, age, and preferred genres (List<Genre>)
   - ✅ `Item.java` - Movie/Item class with id, title, description

3. **Service Layer** (`java/src/crossai/service/`)
   - ✅ `RecommendationService.java` - Interface defining the recommendation contract
   - ❌ `HybridRecommendationService.java` - **NOT IMPLEMENTED YET** (this is the next step!)

4. **Unit Tests** (`java/test/crossai/model/`)
   - ✅ `UserTest.java` - 10 comprehensive tests for User class (all passing)
   - Coverage: constructor validation, genre management, encapsulation

5. **Git Repository**
   - ✅ Initialized with proper `.gitignore`
   - ✅ Multiple commits showing clean development history
   - ✅ Professional README.md with project documentation

---

## 🛠️ Required Setup

### Prerequisites:

1. **Java Development Kit (JDK) 17 or higher**
   - Check: `java -version`

2. **Maven** (CRITICAL!)
   - Download: https://maven.apache.org/download.cgi
   - **IMPORTANT:** Add Maven's `bin` folder to your Windows PATH environment variables
   - Example path: `C:\Program Files\Apache\maven\bin`
   - Verify installation: `mvn --version`

3. **IDE** (Recommended: VSCode)
   - Install "Java Extension Pack" from VSCode marketplace
   - Includes Maven support automatically

### First-Time Setup:

```bash
# Navigate to Java directory
cd D:\Projects\CrossAI\java

# Download all dependencies and compile
mvn clean install

# Run tests to verify everything works
mvn test
```

If `mvn` command is not recognized, you need to add Maven to your PATH (see Prerequisites above).

---

## 📂 Project Structure

```
CrossAI/
├── java/                    # ✅ WORKING
│   ├── src/crossai/
│   │   ├── model/          # Data models (User, Item, Genre)
│   │   ├── service/        # Business logic (RecommendationService)
│   │   └── controller/     # (Not started yet)
│   ├── test/crossai/       # Unit tests
│   └── pom.xml             # Maven configuration
│
├── cpp/                     # ❌ NOT STARTED
│   ├── include/
│   ├── src/
│   └── CMakeLists.txt
│
├── python/                  # ❌ NOT STARTED
│   ├── recommender/
│   └── requirements.txt
│
├── shared/                  # JSON communication folder
│   ├── input.json
│   └── output.json
│
└── docs/
    └── UML_Diagram.png
```

---

## 🎯 Next Steps 

### Immediate Next Task: **HybridRecommendationService**

This class needs to:
1. **Implement** the `RecommendationService` interface
2. **Write user data** to `shared/input.json` (using Gson library)
3. **Read recommendations** from `shared/output.json`
4. **Return** `List<Item>` of recommended movies

**File location:** `java/src/crossai/service/HybridRecommendationService.java`

**What it should do:**
```java
public class HybridRecommendationService implements RecommendationService {
    
    @Override
    public List<Item> getRecommendations(User user) {
        // 1. Validate user
        // 2. Write user preferences to shared/input.json
        // 3. (Future: Call C++ engine)
        // 4. Read results from shared/output.json
        // 5. Parse JSON and return List<Item>
    }
}
```

**This will give us:**
- ✅ File I/O (read/write JSON files)
- ✅ Polymorphism (implements interface)
- ✅ Error handling (try-catch for file operations)
- ✅ More points toward project requirements!

---

## 📋 Java Project Requirements Tracker

### ✅ Already Completed:
- [x] Constructor (non-empty) - User, Item, Genre
- [x] Encapsulation (private/protected) - all classes
- [x] Interface - RecommendationService
- [x] Overridden method (@Override) - toString(), equals()
- [x] Collections (ArrayList) - User's preferredGenres
- [x] Error handling - IllegalArgumentException with validation
- [x] Enumeration (enum) - Genre enum
- [x] Unit tests (3 pt) - UserTest with 10 tests

### ❌ Still Need:
- [ ] Destructor (cleanup method, close files)
- [ ] Inheritance (class extends another class)
- [ ] Polymorphism (7 pt!) - need more demonstrations
- [ ] Read/write file (FileReader/Scanner) - **Next: HybridRecommendationService**
- [ ] Generic class/method `<T>` (3 pt)
- [ ] Parallel programming (threads, 3 pt)

---

## 🔧 Useful Maven Commands

```bash
# Compile source code only
mvn clean compile

# Run all tests
mvn test

# Compile + package into JAR
mvn clean package

# Run the main application (when MainApp is ready)
mvn exec:java

# Skip tests during build (use sparingly!)
mvn clean install -DskipTests
```

---

## 📚 Key Resources

### Dataset:
- **Kaggle Movies Dataset:** https://www.kaggle.com/datasets/rounakbanik/the-movies-dataset
- Contains 45,000 movies with metadata (genres, ratings, etc.)
- Genre format: `[{'id': 28, 'name': 'Action'}, {'id': 12, 'name': 'Adventure'}]`

### Libraries We're Using:
- **Gson:** JSON parsing/writing (already in pom.xml)
- **JUnit 5:** Unit testing framework (already in pom.xml)

### Documentation:
- Maven: https://maven.apache.org/guides/
- JUnit 5: https://junit.org/junit5/docs/current/user-guide/
- Gson: https://github.com/google/gson

---

## 🐛 Troubleshooting

### "mvn: command not found"
- Maven is not installed OR not in your PATH
- Add Maven's `bin` folder to environment variables
- Restart terminal/VSCode after adding to PATH

### Tests failing after pulling latest code
- Run `mvn clean test` to recompile everything
- Make sure you're using Java 17+

### "Package does not exist" errors
- Run `mvn clean install` to download dependencies
- Check that `pom.xml` is present in `/java` directory

---
