import pandas as pd
import json
import ast
from pathlib import Path

def parse_genres(genres_str):
    """Parse genres from JSON string to list of genre names."""
    try:
        if pd.isna(genres_str):
            return []
        genres_list = ast.literal_eval(genres_str)
        return [genre['name'] for genre in genres_list]
    except:
        return []

def preprocess_movies(input_path, output_path, limit=None):
    """
    Preprocess movies dataset:
    - Extract needed columns: id, title, genres, vote_average
    - Clean data
    - Parse genres from JSON to list
    - Save as JSON
    
    Args:
        input_path: Path to movies_metadata.csv
        output_path: Path to save processed JSON
        limit: Optional limit on number of movies (for testing)
    """
    print(f"Reading data from: {input_path}")
    
    # Read CSV with error handling for bad lines
    df = pd.read_csv(input_path, low_memory=False, on_bad_lines='skip')
    
    print(f"Loaded {len(df)} movies")
    
    # Select only needed columns
    columns_needed = ['id', 'title', 'genres', 'vote_average', 'vote_count', 'popularity']
    df = df[columns_needed]
    
    # Clean data
    print("Cleaning data...")
    
    # Remove rows with missing essential data
    df = df.dropna(subset=['id', 'title', 'genres'])
    
    # Convert id to int (some might be strings)
    df['id'] = pd.to_numeric(df['id'], errors='coerce')
    df = df.dropna(subset=['id'])
    df['id'] = df['id'].astype(int)
    
    # Fill missing ratings with 0
    df['vote_average'] = df['vote_average'].fillna(0)
    df['vote_count'] = df['vote_count'].fillna(0)
    df['popularity'] = df['popularity'].fillna(0)
    
    # Filter movies with at least some votes (quality control)
    df = df[df['vote_count'] >= 10]
    
    # Parse genres
    print("Parsing genres...")
    df['genres'] = df['genres'].apply(parse_genres)
    
    # Remove movies without genres
    df = df[df['genres'].apply(len) > 0]
    
    # Sort by popularity (keep most popular movies)
    df = df.sort_values('popularity', ascending=False)
    
    # Limit if specified
    if limit:
        df = df.head(limit)
        print(f"Limited to {limit} movies")
    
    print(f"Processed {len(df)} movies")
    
    # Convert to list of dictionaries
    movies_list = df.to_dict('records')
    
    # Save as JSON
    print(f"Saving to: {output_path}")
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(movies_list, f, indent=2, ensure_ascii=False)
    
    print("Done!")
    
    # Print some statistics
    print("\n=== Dataset Statistics ===")
    print(f"Total movies: {len(movies_list)}")
    print(f"Average rating: {df['vote_average'].mean():.2f}")
    
    # Count genres
    all_genres = []
    for movie in movies_list:
        all_genres.extend(movie['genres'])
    
    genre_counts = pd.Series(all_genres).value_counts()
    print(f"\nTop 10 genres:")
    print(genre_counts.head(10))
    
    return movies_list

if __name__ == "__main__":
    # Paths
    data_dir = Path(__file__).parent.parent / "data"
    input_path = data_dir / "movies_metadata.csv"
    output_path = data_dir / "processed_movies.json"
    
    # Process all movies (or set limit for testing, e.g., limit=1000)
    movies = preprocess_movies(input_path, output_path, limit=None)
    
    print(f"\n✅ Successfully processed {len(movies)} movies!")
    print(f"Output saved to: {output_path}")