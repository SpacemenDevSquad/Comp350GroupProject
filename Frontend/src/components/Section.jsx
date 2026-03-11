import React from 'react';

function Section({ data }) {
  // Safely grab the details from the Java Section object you passed in
  const deptCode = data.course?.department?.name || data.course?.department?.id || "N/A";
  const courseNum = data.course?.number || "000";
  const sectionLetter = data.sectionLetter || "?";
  
  return (
    <div className="section-card" style={{ border: '1px solid gray', margin: '10px', padding: '10px', color: 'white' }}>
      <h2>{deptCode} {courseNum} - Section {sectionLetter}</h2>
      
      <p>Credits: {data.course?.credits || "N/A"}</p>
      
      <pre style={{ fontSize: '10px' }}>
        {/* Temporarily printing the raw data here */}
        {JSON.stringify(data, null, 2)}
      </pre>
    </div>
  );
}

export default Section;