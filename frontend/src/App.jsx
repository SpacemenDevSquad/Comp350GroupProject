import Home from './views/Home';
import Schedule from './views/Schedule';
import StatusSheet from './views/StatusSheet';
import TopBar from './components/TopBar';
import {useState} from 'react'
import './css/App.css'

function App() {

  //LOGIN STATES
  const [currentUser, setCurrentUser]= useState(null); // null means guest, otherwise it holds a User object
  const [showLogin, setShowLogin]= useState(false); //controls visability of login popup
  const [isSignup, setIsSignup] = useState(false);

  //SEMESTER STATES
  const [year, setYear] = useState(2025)
  const [term, setTerm] = useState('F')

  return (
    <div>
      {/* pass User and trigger function to TopBar */}
      <TopBar
        user={currentUser}
        onLoginClick={() => setShowLogin(true)}
      />
      <Home
        year={year}
        setYear={setYear}
        term={term}
        setTerm={setTerm}
      />
      <Schedule
        year={year}
        term={term}
      />

      <StatusSheet />

      {showLogin && (
        <div className="loginModal">
          <div className="loginContent">
            <h2 className="loginTitle">{isSignup ? "Create Account" : "Sign In"}</h2>

            {/* Only show "name" during signup*/}
            {isSignup && (
              <input type="text" placeholder="Full Name" className="loginInput" />
            )}
            
            <input type="email" placeholder="Email" className="loginInput" />
            <input type="password" placeholder="Password" className="loginInput" />
          
            <button className="loginBtn" onClick={() => { 
                setCurrentUser({id: 1, name: "Ryan Merrick"}); 
                setShowLogin(false); 
            }}>
              {isSignup ? "Register" : "Login"}
            </button>

            {/* Toggle Link: Switches the view without closing the modal */}
            <p className="toggleText">
              {isSignup ? "Already have an account? " : "Need an account? "}
              <span className="toggleLink" onClick={() => setIsSignup(!isSignup)}>
                {isSignup ? "Login" : "Sign up"}
              </span>
            </p>

            <button className="cancelBtn" onClick={() => setShowLogin(false)}>
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;