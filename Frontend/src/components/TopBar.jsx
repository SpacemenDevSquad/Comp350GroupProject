import '../css/TopBar.css'
import logoImage from '../images/logo.png'

function Base() {
  return (
    <div id='background'>
      <img id="logo" src={logoImage}></img>
    </div>
  )
}

export default Base