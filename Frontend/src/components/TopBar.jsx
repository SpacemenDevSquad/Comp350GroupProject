import '../css/TopBar.css'
import logoImage from '../images/logo.png'

function Base() {
  return (
    <div id='background'>
      <img id="logo" src={logoImage}></img>
      <button id="calendarBtn" onClick={() => console.log("Hello World")}></button>
    </div>
  )
}

export default Base