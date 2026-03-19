import { useEffect } from 'react';
import '../css/section.css'
import { createAlert } from '../js/createAlert.jsx';

function Section({ data, year, term }) {

  // Gets the title of the course and formats it with proper capitalization
  let rawTitle = data.course?.title || "Null";
  let splitTitle = rawTitle.split(" ");
  let joinedTitle = ""
  for (let i = 0; i < splitTitle.length; i++) {
    joinedTitle += splitTitle[i][0].toString().toUpperCase() + splitTitle[i].substring(1).toUpperCase()+" ";
  }

  // Timeslots
  let rawTimes = data.timeslots || "Null";
  let joinedTimes;
  if (rawTimes !== "Null") {
    joinedTimes = [];

    // Go through each timeslot for the class
    for (let i = 0; i < rawTimes.length; i++) {

      // Get the raw data
      let startTime = rawTimes[i].startTime;
      let endTime = rawTimes[i].endTime;
      let day = rawTimes[i].day;

      // Calculate if the class time is AM or PM
      let startTimeOfDay = "am";
      let endTimeOfDay = "am";
      if (Math.floor((startTime/60)/12) == 1) startTimeOfDay = "pm";
      if (Math.floor((endTime/60)/12) == 1) endTimeOfDay = "pm";

      // Get the starting time of the class as a string
      let startString = ((Math.floor(startTime/60))%12 == 0 ? 12 : (Math.floor(startTime/60))%12) + ":" + ((startTime%60) > 10 ? (startTime%60).toString() : "0"+(startTime%60).toString());

      // Get the ending time of the class as a string
      let endString = ((Math.floor(endTime/60))%12 == 0 ? 12 : (Math.floor(endTime/60))%12) + ":" + ((endTime%60) > 10 ? (endTime%60).toString() : "0"+(endTime%60).toString());

      // Detects if a class shares the same time between multiple days. For example, MWF at 9:00-9:50am all share the same time
      let existed = false;

      for (let j = 0; j < Math.floor(joinedTimes.length/2); j++) {
        if (joinedTimes[j*2+1] == startString+startTimeOfDay+" - "+endString+endTimeOfDay) {
          existed = true;
          joinedTimes[j*2] += day;
          break;
        }
      }

      // If the timeslot doesn't already 
      if (!existed) {
        joinedTimes.push(day);
        joinedTimes.push(startString+startTimeOfDay+" - "+endString+endTimeOfDay)
      }

    }
  } else {
    joinedTimes = "Null";
  }


  //ADD/DROP LOGIC
  async function addSection(force=false) {
    //make api call
    const response= await fetch(`http://localhost:8096/api/schedule/add/1/${year}/${term}?force=${force}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })

    //checks for conflict
    if(!response.ok){
      const msg = await response.text();
      
      //credit limit popup
      if(response.status==403 && msg === "CREDIT_LIMIT"){
        const confirmForce = window.confirm("Warning: This puts you over 18 credits. Force add anyway?");
        if (confirmForce) {
          addSection(true);
        }
        return;
      }
      
      createAlert(msg, "Error: "+response.status, "red");
      return;
    }

    createAlert("Added Course", "Check schedule for details", "green");
  }

  async function dropSection() {
    //make api call
    const response= await fetch(`http://localhost:8096/api/schedule/drop/1/${year}/${term}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })

    //checks if not in schedule
    if(!response.ok){
      createAlert("Failed to drop", "Could not find in schedule", "red");
      return;
    }
    createAlert("Course Dropped", "Check schedule for details", "green");
  }



  // Safely grab the details from the Java Section object you passed in
  const deptCode = data.course?.department?.code || data.course?.department?.id || "N/A";
  const courseNum = data.course?.number || "000";
  const sectionLetter = data.sectionLetter || "?";
  const credits = data.course?.credits || "Null";
  const title = joinedTitle;
  const timeSlots = joinedTimes.join(" ");
  const description = data.course?.description || "Null";
  const sectionTerm = data.semester?.term || "Null";
  const sectionYear = data.semester?.year || "Null";
  const profName = data.faculty[0]?.name || "Null";
  
  

  return (
    <div className="sectionCard">
      <p className="sectionTitle">{title}</p>
      <p className="sectionDeptInfo">{deptCode} {courseNum} - Section {sectionLetter}</p>
      <p className="sectionProf">{"Professor: "+profName}</p>
      <p className="sectionTerm">{"Semester: "+sectionYear+" "+sectionTerm}</p>
      <p className="sectionTime">{timeSlots}</p>
      <p className="sectionCreds">Credits: {credits}</p>
      <button onClick={addSection} className="addButton">Add</button>
      <button onClick={dropSection} className="dropButton">Drop</button>
    </div>
  );
}

export default Section;