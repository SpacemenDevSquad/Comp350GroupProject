import json
import re
from datetime import datetime

def format_time(time_str):
    """Converts '10:00 AM' to '10:00:00'."""
    try:
        t = datetime.strptime(time_str.strip(), "%I:%M %p")
        return t.strftime("%H:%M:%S")
    except ValueError:
        return time_str

def parse_location(raw_location):
    """Simplifies the raw location string into building codes."""
    if "Off Campus" in raw_location:
        return "Off Campus Course"
    
    # Extract the room number using regex (looks for digits after a comma/space)
    room_match = re.search(r",\s*([A-Za-z0-9]+)\s", raw_location + " ")
    room = room_match.group(1) if room_match else ""

    # Map long names to short codes 
    if "Staley Hall of Arts & Letters" in raw_location:
        return f"SHAL {room}".strip()
    elif "STEM" in raw_location:
        return f"STEM {room}".strip()
    elif "Hoyt" in raw_location:
        return f"HOYT {room}".strip()
    elif "Breen" in raw_location:
        return f"BREEN {room}".strip()
    
    return raw_location.strip()

def get_semester(start_date_str):
    """Infers the semester (e.g., '2026_Fall') from the start date."""
    try:
        month, day, year = map(int, start_date_str.split('/'))
        if month >= 8:
            season = "Fall"
        elif month <= 5:
            season = "Spring"
        else:
            season = "Summer"
        return f"{year}_{season}"
    except ValueError:
        return "Unknown_Semester"

def transform_course_data(raw_data):
    structured_classes = []

    for row in raw_data:
        # Skip spacer rows or empty arrays
        if len(row) < 11:
            continue

        try:
            # 1. Parse Course Code
            code_parts = row[2].split()
            subject = code_parts[0] if len(code_parts) > 0 else ""
            number = int(code_parts[1]) if len(code_parts) > 1 else 0
            section = code_parts[2] if len(code_parts) > 2 else ""

            # 2. Basic Info & Faculty Guarantee
            name = row[3]
            raw_faculty = row[4].strip()
            # Explicitly enforce an empty array if the string is empty
            if raw_faculty:
                faculty = [f.strip() for f in raw_faculty.split('\n') if f.strip()]
            else:
                faculty = []
            
            # 3. Seats
            seats_parts = row[5].split('/')
            open_seats = int(seats_parts[0]) if len(seats_parts) > 0 else 0
            total_seats = int(seats_parts[1]) if len(seats_parts) > 1 else 0
            
            is_open = (row[6].strip().casefold() == "open")
            credits_val = int(float(row[8]))
            
            # 4. Dates & Semester
            semester = get_semester(row[9])

            # 5. Lab inference
            is_lab = "L" in section or "LAB" in name.upper()

            # 6. Parse Schedule and Location Guarantee
            sched_loc_parts = row[7].split(';')
            
            # Initialize strictly as an empty array
            times = [] 
            location = parse_location(sched_loc_parts[-1])

            # Only attempt to populate 'times' if there's actual time data before the semicolon
            if len(sched_loc_parts) > 1 and sched_loc_parts[0].strip():
                time_part = sched_loc_parts[0].strip()
                match = re.match(r"([A-Z]+)\s+(.+?)-(.+)", time_part)
                if match:
                    days_str = match.group(1)
                    start_time = format_time(match.group(2))
                    end_time = format_time(match.group(3))
                    
                    for day in days_str:
                        times.append({
                            "day": day,
                            "start_time": start_time,
                            "end_time": end_time
                        })

            # Assemble the final object
            course_obj = {
                "credits": credits_val,
                "faculty": faculty,
                "is_lab": is_lab,
                "is_open": is_open,
                "location": location,
                "name": name,
                "number": number,
                "open_seats": open_seats,
                "section": section,
                "semester": semester,
                "subject": subject,
                "times": times,
                "total_seats": total_seats
            }
            
            structured_classes.append(course_obj)
            
        except Exception as e:
            print(f"Error parsing row {row[2] if len(row)>2 else 'Unknown'}: {e}")
            continue

    return {"classes": structured_classes}

# --- Execution ---
if __name__ == "__main__":
    with open("out/scraped_courses.json", "r") as f:
        raw_json = json.load(f)

    formatted_data = transform_course_data(raw_json)

    with open("out/formatted_courses.json", "w") as f:
        json.dump(formatted_data, f, indent=4)
        
    print(f"Successfully formatted {len(formatted_data['classes'])} courses.")