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

const delay = ms => new Promise(res => setTimeout(res, ms));