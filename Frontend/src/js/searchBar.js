export default async function OnHitEnter(e, availability = []) {
  console.log("Searching for courses...")
  
  const searchQuery = e.target.value;
  let courseResults = [];

  // Transform availability to backend JSON format
  const availMap = {'M': [], 'T': [], 'W': [], 'R': [], 'F': []};
  availability.forEach(block => {
    const days = block.days;
    const startMin = parseTime(block.startTime);
    const endMin = parseTime(block.endTime);
    for (let day of days) {
      if (availMap[day]) {
        availMap[day].push([startMin, endMin]);
      }
    }
  });
  const availabilityJson = JSON.stringify(availMap);
  console.log("Sending search JSON:", JSON.stringify({ 
    searchText: searchQuery, 
    availabilityJson: availabilityJson 
  }));

  try{
    const response = await fetch("http://localhost:8096/api/search", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ 
        searchText: searchQuery, 
        availabilityJson: availabilityJson 
      })
    });
    if (!response.ok) {
      throw new Error("Search API call failed");
    }
    courseResults = await response.json();
    console.log(`Successfully retrieved ${courseResults.length} courses!`);
    return courseResults;
  } catch (error) {
    console.error("Error fetching course results:", error);
    return [];
  }

}

// Helper function
function parseTime(timeStr) {
  const [hours, minutes] = timeStr.split(':').map(Number);
  return hours * 60 + minutes;
}

// Change 'e' to 'text'
export async function OnType(text, availability) {
  if (!text || text.trim().length < 2) {
    return []; 
  }

  try {
    const response = await fetch(`http://localhost:8096/api/autocomplete?q=${encodeURIComponent(text)}`);
    if (!response.ok) {
      throw new Error("Autocomplete API call failed");
    }
    const suggestions = await response.json();
    console.log("Suggestions retrieved:", suggestions);
    return suggestions; 
  } catch (error) {
    console.error("Error fetching suggestions:", error);
    return [];
  }
}

const delay = ms => new Promise(res => setTimeout(res, ms));