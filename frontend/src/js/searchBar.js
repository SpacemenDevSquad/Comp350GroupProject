export default async function OnHitEnter(searchText, year, term, availability = [], credits = 0, noTimeSections = false) {
  const requestId = `search-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`;
  console.log(`[${requestId}] Searching for courses`, {
    searchText,
    year,
    term,
    availability,
    credits,
    noTimeSections
  });
  
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
  console.log(`[${requestId}] Sending search payload`, {
    searchText: searchText,
    availabilityJson: availabilityJson,
    credits: credits,
    noTimeSections: noTimeSections
  });

  // Get the search results from the backend
  try{
    const url = `${import.meta.env.VITE_API_URL}/api/search/${year}/${term}`
    console.log(`[${requestId}] POST ${url}`)
    const response = await fetch(url, {
      method: "POST",
      cache: "no-store",
      headers: {
        "Content-Type": "application/json",
        "X-Search-Request-Id": requestId
      },
      body: JSON.stringify({ 
        searchText: searchText, 
        availabilityJson: availabilityJson,
        credits: credits,
        noTimeSections: noTimeSections
      })
    });
    if (!response.ok) {
      throw new Error("Search API call failed");
    }
    courseResults = await response.json();
    const sample = (courseResults || []).slice(0, 5).map((section) => {
      const dept = section?.course?.department?.code || "N/A";
      const num = section?.course?.number || "N/A";
      const title = section?.course?.title || "N/A";
      return `${dept} ${num} - ${title}`;
    });
    console.log(`[${requestId}] Search response`, {
      status: response.status,
      count: courseResults.length,
      firstFive: sample
    });
    return courseResults;
  } catch (error) {
    console.error(`[${requestId}] Error fetching course results:`, error);
    return [];
  }

}

// Helper function
function parseTime(timeStr) {
  const [hours, minutes] = timeStr.split(':').map(Number);
  return hours * 60 + minutes;
}

// Get the autocomplete suggestions from the backend as the user types
export async function OnType(text, year, term, availability, credits=0) {
  if (!text || text.trim().length < 2) {
    return []; 
  }

  // No body needed since all info is in the query params
  try {
    const url = `${import.meta.env.VITE_API_URL}/api/autocomplete/${year}/${term}?q=${encodeURIComponent(text)}`
    console.log(url)
    const response = await fetch(url);
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
