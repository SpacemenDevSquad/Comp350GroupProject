import '../css/TopBar.css'
import logoImage from '../images/logo.png'
import calendarTransition, { statusSheetTransition } from '../js/screenTransitions.js'
import toggleFilter from '../js/toggleFilter.js'

function TopBar({ user, onLoginClick }) {
  return (
    <div id='background'>
      <img id="logo" src={logoImage} alt="App Logo" />
      
        <div id="topBarButtons" style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
        

        {/* Login/User button*/}
        {user ? (
          <button id="userProfileBtn" className="profileButton">
            Hi, {user.name}
          </button>
        ) : (
          <button id="loginBtn" className="profileButton" onClick={onLoginClick}>
            Login / Signup
          </button>
        )}
        
        {/* Status Sheet Button */}
        <button 
          id="statusSheetBtn" 
          onClick={() => { statusSheetTransition(); toggleFilter(true); }}
        >
          My Major
        </button>

        {/* Weekly Schedule Button */}
        <button 
          id="calendarBtn" 
          onClick={() => { calendarTransition(); toggleFilter(true); }}
        >
        </button>

      </div>
    </div>
  )
}

export default TopBar