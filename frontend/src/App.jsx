import Home from './views/Home';
import Schedule from './views/Schedule';
import StatusSheet from './views/StatusSheet';
import TopBar from './components/TopBar';
import './css/login.css'
import {useState} from 'react'
import './css/App.css'
import { auth } from "./js/firebase.js"
import { signInWithEmailAndPassword, createUserWithEmailAndPassword, onAuthStateChanged, signOut, updateProfile } from "firebase/auth";
import { useEffect } from "react";




function App() {

  //LOGIN STATES
  const [currentUser, setCurrentUser]= useState(null); // null means guest, otherwise it holds a User object
  const [showLogin, setShowLogin]= useState(false);
  const [isSignup, setIsSignup]= useState(false);
  const [email, setEmail]= useState("");
  const [password, setPassword]= useState("");
  const [name, setName] = useState("");

  //SEMESTER STATES
  const [year, setYear] = useState(2025)
  const [term, setTerm] = useState('F')


  // Automatic check if the user is logged in (checking the firebase database)
  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (firebaseUser) => {
      if (firebaseUser) {
        setCurrentUser({
          id: firebaseUser.uid,
          email: firebaseUser.email,
          name: firebaseUser.displayName
        });
      } else {
        setCurrentUser(null);
      }
    });
    return () => unsubscribe(); // listener cleanup
  }, []);

  //Authentication handler
  const handleAuth = async () => {
  try {
    if (isSignup) {
      // 1. Create the account
      const userCredential = await createUserWithEmailAndPassword(auth, email, password);
      
      // 2. Attach the name to the Firebase profile
      await updateProfile(userCredential.user, {
        displayName: name
      });
    } else {
      await signInWithEmailAndPassword(auth, email, password);
    }
    
    setShowLogin(false);
    setEmail("");
    setPassword("");
    setName(""); // Clear name state
  } catch (error) {
    alert("Error: " + error.message);
  }
};

  const handleLogout = () => signOut(auth);


  return (
    <div>
      {/* pass User and trigger function to TopBar */}
      <TopBar
        user={currentUser}
        onLoginClick={() => setShowLogin(true)}
        onLogout={handleLogout}

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
        <div className="loginModel">
          <div className="loginContent">
            <h2 className="loginTitle">{isSignup ? "Create Account" : "Sign In"}</h2>

            {/* Name, Email, and Password inputs */}
            {/* Name field ONLY shows if isSignup is true */}
            {isSignup && (
              <input 
                className="loginInput"
                type="text" 
                placeholder="Full Name" 
                value={name}
                onChange={(e) => setName(e.target.value)} 
              />
            )}
            <input 
                className="loginInput"
                type="email" 
                placeholder="Email" 
                value={email}
                onChange={(e) => setEmail(e.target.value)} 
            />
            <input 
              className="loginInput"
              type="password" 
              placeholder="Password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)} 
            />

            <button className="loginBtn" onClick={handleAuth}>
              {isSignup ? "Register" : "Login"}
            </button>

            {/* The switch between login and signup */}
            <p className="toggleText">
              {isSignup ? "Already have an account? " : "Need an account? "}
              <span 
                className="toggleLink" 
                onClick={() => setIsSignup(!isSignup)}
              >
                {isSignup ? "Login" : "Sign up"}
              </span>
            </p>
            {/* Cancel button */}
            <button className="cancelBtn" onClick={() => setShowLogin(false)}>Cancel</button>
          
          </div>
        </div>
          
      )}
    </div>
  );
}

export default App;