import json
import sys
import numpy as np
from pathlib import Path
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

class MovieRecommender:
    """Content-based movie recommender using TF-IDF on genres."""
    
    def __init__(self, movies_data):
        self.movies = movies_data
        self.prepare_features()
    
    def prepare_features(self):
        """Prepare TF-IDF features from movie genres."""
        print("Preparing TF-IDF features...")
        
        # Create genre strings for each movie
        self.genre_strings = []
        for movie in self.movies:
            genres = movie.get('genres', [])
            # Join genres with spaces for TF-IDF
            genre_str = ' '.join(genres)
            self.genre_strings.append(genre_str)
        
        # Create TF-IDF matrix
        self.tfidf = TfidfVectorizer()
        self.tfidf_matrix = self.tfidf.fit_transform(self.genre_strings)
        
        print(f"Feature matrix shape: {self.tfidf_matrix.shape}")
    
    def recommend(self, preferred_genres, n=10, min_rating=6.0, min_votes=100):
        """
        Recommend movies based on preferred genres with quality filtering.
        
        Args:
            preferred_genres: List of genre names user prefers
            n: Number of recommendations to return
            min_rating: Minimum vote_average threshold (default: 6.0)
            min_votes: Minimum vote_count threshold (default: 100)
        
        Returns:
            List of recommended movies with scores
        """
        print(f"Generating recommendations for genres: {preferred_genres}")
        print(f"Quality filters: rating >= {min_rating}, votes >= {min_votes}")
        
        # Create query vector from preferred genres
        query_str = ' '.join(preferred_genres)
        query_vector = self.tfidf.transform([query_str])
        
        # Calculate cosine similarity
        similarities = cosine_similarity(query_vector, self.tfidf_matrix).flatten()
        
        # Get indices of top movies (get more than needed for filtering)
        top_indices = similarities.argsort()[-n*5:][::-1]  # Increased multiplier for filtering
        
        # Get movies with scores and apply quality filters
        recommendations = []
        filtered_count = 0
        
        for idx in top_indices:
            if similarities[idx] > 0:  # Only include movies with positive similarity
                movie = self.movies[idx].copy()
                
                # Quality filters
                rating = movie.get('vote_average', 0)
                votes = movie.get('vote_count', 0)
                
                if rating >= min_rating and votes >= min_votes:
                    # Base similarity score
                    movie['ml_score'] = float(similarities[idx])
                    
                    # Optional: Boost popular movies slightly
                    popularity = movie.get('popularity', 0)
                    if popularity > 50:
                        movie['ml_score'] *= 1.05  # 5% boost for popular movies
                    
                    recommendations.append(movie)
                    
                    if len(recommendations) >= n:
                        break
                else:
                    filtered_count += 1
        
        print(f"Generated {len(recommendations)} recommendations (filtered out {filtered_count} low-quality movies)")
        return recommendations

def load_movies(movies_path):
    """Load processed movies from JSON."""
    print(f"Loading movies from: {movies_path}")
    with open(movies_path, 'r', encoding='utf-8') as f:
        movies = json.load(f)
    print(f"Loaded {len(movies)} movies")
    return movies

def load_input(input_path):
    """Load user preferences from input JSON."""
    print(f"Loading user preferences from: {input_path}")
    with open(input_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    return data

def save_output(recommendations, output_path):
    """Save recommendations to output JSON."""
    print(f"Saving recommendations to: {output_path}")
    
    # Format output
    output = {
        'ml_recommendations': recommendations,
        'count': len(recommendations)
    }
    
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(output, f, indent=2, ensure_ascii=False)
    
    print("Output saved successfully!")

def main():
    """Main function for ML recommendation."""
    try:
        # Paths
        script_dir = Path(__file__).parent
        data_dir = script_dir.parent / "data"
        shared_dir = script_dir.parent.parent / "shared"
        
        movies_path = data_dir / "processed_movies.json"
        input_path = shared_dir / "ml_input.json"
        output_path = shared_dir / "ml_output.json"
        
        # Check if input exists (called by C++)
        if not input_path.exists():
            print(f"❌ Input file not found: {input_path}")
            print("Using default preferences for testing...")
            # Use default for testing
            user_data = {
                "preferredGenres": ["Action", "Sci-Fi", "Drama"]
            }
        else:
            # Load user preferences from C++
            user_data = load_input(input_path)
        
        preferred_genres = user_data.get('preferredGenres', [])
        print(f"User preferred genres: {preferred_genres}")
        
        # Load movies
        movies = load_movies(movies_path)
        
        # Create recommender
        recommender = MovieRecommender(movies)
        
        # Get recommendations
        recommendations = recommender.recommend(preferred_genres, n=10)
        
        # Save output
        save_output(recommendations, output_path)
        
        print("\n✅ ML Recommendations generated successfully!")
        print(f"Top 3 recommendations:")
        for i, movie in enumerate(recommendations[:3], 1):
            print(f"  {i}. {movie['title']} (Score: {movie['ml_score']:.3f})")
        
        return 0
        
    except Exception as e:
        print(f"❌ Error in ML model: {e}")
        import traceback
        traceback.print_exc()
        return 1

if __name__ == "__main__":
    sys.exit(main())