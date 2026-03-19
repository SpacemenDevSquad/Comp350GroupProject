import '../css/TopBar.css'
import logoImage from '../images/logo.png'
import calendarTransition from '../js/screenTransitions.js'
import toggleFilter from '../js/toggleFilter.js'

function Base() {
  return (
    <div id='background'>
      <img id="logo" src={logoImage}></img>
      <button id="calendarBtn" onClick={() => {calendarTransition(); toggleFilter(true)}}></button>
    </div>
  )
}

export default Base