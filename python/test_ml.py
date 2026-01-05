#!/usr/bin/env python3
"""
Test script for the Python ML module.
Run this to verify the ML engine works before C++ integration.
"""

import json
import sys
from pathlib import Path

def test_ml_module():
    """Test the ML recommendation system."""
    
    print("=" * 60)
    print("CrossAI Python ML Module - Standalone Test")
    print("=" * 60)
    
    # Setup paths
    script_dir = Path(__file__).parent
    data_dir = script_dir / "data"
    shared_dir = script_dir.parent / "shared"
    
    movies_path = data_dir / "processed_movies.json"
    input_path = shared_dir / "ml_input.json"
    output_path = shared_dir / "ml_output.json"
    
    # Check prerequisites
    print("\n1. Checking prerequisites...")
    
    if not movies_path.exists():
        print(f"❌ FAILED: {movies_path} not found")
        print("   → Run preprocess.py first!")
        return False
    print(f"✅ Movies dataset found: {movies_path}")
    
    # Create shared directory if needed
    shared_dir.mkdir(parents=True, exist_ok=True)
    
    # Create test input
    print("\n2. Creating test input...")
    test_genres = ["Action", "Sci-Fi", "Thriller"]
    test_input = {
        "preferredGenres": test_genres
    }
    
    with open(input_path, 'w') as f:
        json.dump(test_input, f, indent=2)
    print(f"✅ Created test input: {test_genres}")
    
    # Import and run model
    print("\n3. Running ML model...")
    print("-" * 60)
    
    try:
        from recommender.model import main as run_model
        result = run_model()
        
        print("-" * 60)
        
        if result != 0:
            print(f"❌ FAILED: Model returned error code {result}")
            return False
            
    except Exception as e:
        print(f"❌ FAILED: {e}")
        import traceback
        traceback.print_exc()
        return False
    
    # Verify output
    print("\n4. Verifying output...")
    
    if not output_path.exists():
        print(f"❌ FAILED: {output_path} not created")
        return False
    
    with open(output_path, 'r') as f:
        output = json.load(f)
    
    if 'ml_recommendations' not in output:
        print("❌ FAILED: No recommendations in output")
        return False
    
    recs = output['ml_recommendations']
    print(f"✅ Generated {len(recs)} recommendations")
    
    # Display results
    print("\n5. Top recommendations:")
    print("-" * 60)
    for i, movie in enumerate(recs[:5], 1):
        title = movie['title']
        score = movie['ml_score']
        genres = ', '.join(movie.get('genres', []))
        rating = movie.get('vote_average', 0)
        
        print(f"{i}. {title}")
        print(f"   Score: {score:.3f} | Rating: {rating}/10 | Genres: {genres}")
    print("-" * 60)
    
    # Success!
    print("\n" + "=" * 60)
    print("✅ ALL TESTS PASSED!")
    print("=" * 60)
    print("\nThe ML module is working correctly.")
    print("You can now integrate it with the C++ engine.")
    print("\nNext step: Follow INTEGRATION_GUIDE.md")
    
    return True

if __name__ == "__main__":
    success = test_ml_module()
    sys.exit(0 if success else 1)