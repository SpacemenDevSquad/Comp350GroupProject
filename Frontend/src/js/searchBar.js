export default async function OnHitEnter(e) {
  console.log("Searching for courses...")
  
  // Send search API call here to retrieve a list of courses
  const searchQuery = e.target.value;
  let courseResults = [];

  try{
    const response = await fetch("http://localhost:8096/api/search", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ searchText: searchQuery })
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

// Change 'e' to 'text'
export async function OnType(text) {
  
  // REMOVE the line that says: const query = e.target.value;

  // Use the text variable directly
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