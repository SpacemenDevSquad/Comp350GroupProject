
import { useState, useEffect, useCallback } from "react"; 
import Home from './views/Home';
import Schedule from './views/Schedule';
import StatusSheet from './views/StatusSheet';
import TopBar from './components/TopBar';
import './css/login.css'
import './css/App.css'
import { auth } from "./js/firebase.js"
import { signInWithEmailAndPassword, createUserWithEmailAndPassword, onAuthStateChanged, signOut, updateProfile } from "firebase/auth";
import OfflineAlert from './components/OfflineAlert.jsx'
import Alert from './components/Alert.jsx';




function App() {

  //LOGIN STATES
  const [currentUser, setCurrentUser]= useState(null); // null means guest, otherwise it holds a User object
  const [showLogin, setShowLogin]= useState(false);
  const [isSignup, setIsSignup]= useState(false);
  const [email, setEmail]= useState("");
  const [password, setPassword]= useState("");
  const [name, setName] = useState("");
  const [scheduleName, setScheduleName] = useState("Main Schedule");
  const [existingSchedules, setExistingSchedules] = useState([]); // Shared dropdown menu State
  const [activeAlert, setActiveAlert] = useState(null);

  //SEMESTER STATES
  const [year, setYear] = useState(2026)
  const [term, setTerm] = useState('F')


  //FETCH LOGIC
  //fetch schedule dropdown
  const fetchSchedules = useCallback(async () => {
    if (!currentUser?.id || !year || !term) return;
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/api/schedules/${currentUser.id}/${year}/${term}`);
      if (response.ok) {
        const names = await response.json();
        setExistingSchedules(names);
      }
    } catch (error) { console.error("Error fetching schedules:", error); }
  }, [currentUser?.id, year, term]);

  //refreshes schedule dropdown when there is a change in user year term etc.
  useEffect(() => {
    if (currentUser?.id) {
        fetchSchedules();
    }
}, [currentUser?.id, year, term, fetchSchedules]);

  //Refetches schedule when there is a new schedule
  useEffect(() => {
      const handleRefresh = () => fetchSchedules();
      window.addEventListener('scheduleRefresh', handleRefresh);
      return () => window.removeEventListener('scheduleRefresh', handleRefresh);
  }, [fetchSchedules]);

  // Listen for showLogin event
  useEffect(() => {
    const handleShowLogin = () => {
      setShowLogin(true);
    };
    window.addEventListener('showLogin', handleShowLogin);
    return () => window.removeEventListener('showLogin', handleShowLogin);
  }, []);

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

  useEffect(() => {
    const handleGlobalAlert = (e) => {
        const { title, desc, color } = e.detail;
        triggerAlert(title, desc, color);
    };
    
    window.addEventListener('triggerCustomAlert', handleGlobalAlert);
    return () => window.removeEventListener('triggerCustomAlert', handleGlobalAlert);
  }, []);

  //helper method to trigger alerts
  const triggerAlert = (title, desc, color) => {
    setActiveAlert({ title, desc, color, id: Date.now() });

    setTimeout(() => {
        setActiveAlert(null);
    }, 5500);
    
   
  };

  //AUTHENTICATION HANDLER
  const handleAuth = async () => {
  try {
    let firebaseUser;
    if (isSignup) {
      // Create Account
      const userCredential = await createUserWithEmailAndPassword(auth, email, password);
      // Attach the name to the Firebase profile
      await updateProfile(userCredential.user, {
        displayName: name
      });
      firebaseUser = userCredential.user;

    } else {
      const creds = await signInWithEmailAndPassword(auth, email, password);
      firebaseUser = creds.user;

    }

    //syncs the firebase user to the java backend
    await fetch(`${import.meta.env.VITE_API_URL}/api/user/sync`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        id: firebaseUser.uid,
        email: firebaseUser.email,
        name: firebaseUser.displayName || name
      })
    });

    setShowLogin(false);
    
    //Success alerts
    triggerAlert(
      isSignup ? "Account Created" : "Welcome Back", 
      isSignup ? `Welcome, ${name}!` : "Successfully signed in.", 
      "green"
    );
    

    setEmail("");
    setPassword("");
    setName(""); // Clear name state
  } catch (error) {
    let errorMessage = error.message;
    if (error.code === 'auth/wrong-password') errorMessage = "Incorrect password. Please try again.";
    if (error.code === 'auth/user-not-found') errorMessage = "No account found with this email.";
    if (error.code === 'auth/invalid-email') errorMessage = "Please input a valid email.";
    if (error.code === 'auth/invalid-credential') errorMessage = "The password you entered is incorrect.";
    if (error.code === 'auth/weak-password') errorMessage = "Password must be at least 6 characters.";

    
    triggerAlert("Auth Error", errorMessage, "red");
  }
  };

  const handleLogout = () => signOut(auth);


  


  return (
    <div>
      {/* pass User and trigger function to TopBar */}
      <OfflineAlert />
    
        {/* Custom alert if state is present */}
        {activeAlert && (
          <Alert 
            key={activeAlert.id} 
            alertTitle={activeAlert.title} 
            alertDesc={activeAlert.desc} 
            alertColor={activeAlert.color} 
          />
        )}

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
        userId={currentUser?.id}
        scheduleName={scheduleName}
        setScheduleName={setScheduleName}
        existingSchedules={existingSchedules}
      />
      <Schedule
        year={year}
        term={term}
        userId={currentUser?.id}
        scheduleName={scheduleName}
        setScheduleName={setScheduleName}
        existingSchedules={existingSchedules}
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