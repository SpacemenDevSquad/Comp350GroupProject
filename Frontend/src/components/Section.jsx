import '../css/section.css'

function Section({ data }) {

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


  // Safely grab the details from the Java Section object you passed in
  const deptCode = data.course?.department?.code || data.course?.department?.id || "N/A";
  const courseNum = data.course?.number || "000";
  const sectionLetter = data.sectionLetter || "?";
  const credits = data.course?.credits || "Null";
  const title = joinedTitle;
  const timeSlots = joinedTimes.join(" ");
  const description = data.course?.description || "Null";
  const term = data.semester?.term || "Null";
  const year = data.semester?.year || "Null";
  const profName = data.faculty[0]?.name || "Null";
  
  

  return (
    <div class="sectionCard">
      <p class="sectionTitle">{title}</p>
      <p class="sectionDeptInfo">{deptCode} {courseNum} - Section {sectionLetter}</p>
      <p class="sectionProf">{"Professor: "+profName}</p>
      <p class="sectionTerm">{"Semester: "+year+" "+term}</p>
      <p class="sectionTime">{timeSlots}</p>
      <p class="sectionCreds">Credits: {credits}</p>
    </div>
  );
}

//<pre style={{ fontSize: '10px' }}>
// {JSON.stringify(data, null, 2)}
// </pre>

export default Section;