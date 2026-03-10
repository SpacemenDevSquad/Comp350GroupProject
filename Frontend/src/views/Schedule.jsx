import '../css/schedule.css'
import calendarTransition from '../js/screenTransitions.js'

function Schedule(){
  return (
    <div id="scheduleBlock">
      <button id="backArrow" onClick={calendarTransition}></button>
      <h1 id="scheduleTitle">Current Course Schedule</h1>
    </div>
  )
}

export default Schedule;