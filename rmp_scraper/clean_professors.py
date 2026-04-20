import json
import pandas as pd
import numpy as np

def clean_professor_data(raw_json):
    df = pd.DataFrame(raw_json)

    # Convert ID to integer
    df['id'] = df['id'].astype(int)

    # Extract integer count from "X ratings"
    df['rating_count'] = df['rating_count'].str.replace(' ratings', '').astype(int)

    # Convert ratings to float, replacing empty 0.0 states with NaN
    df['quality_rating'] = df['quality_rating'].astype(float).replace(0.0, np.nan)
    df['difficulty'] = df['difficulty'].astype(float).replace(0.0, np.nan)

    # Convert percentages to float (e.g., '39%' -> 0.39), mapping 'N/A' to NaN
    df['would_take_again'] = (
        df['would_take_again']
        .replace('N/A', np.nan)
        .str.rstrip('%')
        .astype(float) / 100
    )

    # --- KEY CHANGE START ---
    # Replace all NaN values with None. 
    # Python's json.dump() converts None to null automatically.
    df = df.replace({np.nan: None})
    # --- KEY CHANGE END ---

    return df.to_dict(orient='records')

# Load the raw data
with open("professors.json", "r") as f:
    raw_data = json.load(f)

# Clean the data
cleaned_data = clean_professor_data(raw_data)

# Save the cleaned data
with open("professors_clean.json", "w") as f:
    json.dump(cleaned_data, f, indent=4)