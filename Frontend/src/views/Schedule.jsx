import '../css/schedule.css'
import calendarTransition from '../js/screenTransitions.js'
import ScheduleBox from "../components/ScheduleBox.jsx"

function Schedule(){
  return (
    <div id="scheduleBlock">
      <button id="backArrow" onClick={calendarTransition}></button>
      <h1 id="scheduleTitle">Current Course Schedule</h1>
      <ScheduleBox></ScheduleBox>

      
    </div>


  )
}

export default Schedule;