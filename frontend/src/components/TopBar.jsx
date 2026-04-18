import '../css/TopBar.css'
import logoImage from '../images/logo.png'
import calendarTransition, { statusSheetTransition } from '../js/screenTransitions.js'
import toggleFilter from '../js/toggleFilter.js'

function TopBar({ user, onLoginClick, onLogout }) {
  return (
    <div id='background'>
      <img id="logo" src={logoImage}/>
      
        <div id="topBarButtons" style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
        

        {/* Login/User section */}
        {user ? (
          
          <button 
            id="logoutBtn" 
            onClick={onLogout} className="logoutBtn"
            >
              Logout
            </button>
          
        ) : (
          <button id="loginBtn" onClick={onLoginClick}>
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