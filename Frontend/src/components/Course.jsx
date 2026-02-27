import { useState, useEffect } from 'react';

function Course({department, number}) {
  const [data, setData] = useState(null);

  useEffect(() => {
    fetch(`http://localhost:8096/api/course/${department}/${number}/`)
      .then(res => res.json())
      .then(fetchedData => {
        // Log it to see the structure, then save it to state
        console.log(fetchedData); 
        setData(fetchedData); 
      })
      .catch(error => console.error("Error fetching data:", error)); // Added error handling
  }, [department, number]);

  return (
    <div>
      {/* If data is null, show "Loading...". 
        If data exists, stringify it so React can render the object safely.
      */}
      {data ? (
        <pre>{JSON.stringify(data, null, 2)}</pre> 
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
}

export default Course;