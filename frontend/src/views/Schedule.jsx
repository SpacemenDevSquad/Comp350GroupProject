import '../css/schedule.css'
import calendarTransition from '../js/screenTransitions.js'
import WeeklySchedule from "../components/WeeklySchedule.jsx"

function Schedule({ year, term }){
  return (
    <div id="scheduleBlock">
      <button id="backArrow" onClick={() => window.history.back()}></button>
      <h1 id="scheduleTitle">Current Course Schedule</h1>
      <WeeklySchedule
        key={`${year}-${term}`}
        year={year}
        term={term}
      />
    </div>
  )
}

export default Schedule;

