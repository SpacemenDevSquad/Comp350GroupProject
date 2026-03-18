import '../css/schedule.css'
import calendarTransition from '../js/screenTransitions.js'
import WeeklySchedule from "../components/WeeklySchedule.jsx"

function Schedule(){
  return (
    <div id="scheduleBlock">
      <button id="backArrow" onClick={calendarTransition}></button>
      <h1 id="scheduleTitle">Current Course Schedule</h1>
      <WeeklySchedule></WeeklySchedule>

      
    </div>


  )
}

export default Schedule;