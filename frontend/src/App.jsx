
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
import LoginModal from './components/LoginSystem.jsx';

function App() {

  //LOGIN STATES
  const [currentUser, setCurrentUser]= useState(null); // null means guest, otherwise it holds a User object
  const [showLogin, setShowLogin]= useState(false);

  const [scheduleName, setScheduleName] = useState("Main Schedule");
  const [existingSchedules, setExistingSchedules] = useState([]); // Shared dropdown menu State
  const [activeAlerts, setActiveAlerts] = useState([]);

  //SEMESTER STATES
  const [year, setYear] = useState(2026)
  const [term, setTerm] = useState('F')


  //FETCH LOGIC
  //fetch schedule dropdown
  const fetchSchedules = useCallback(async () => {
    if (!currentUser?.id || !year || !term) return;
    try {
      const url = `${import.meta.env.VITE_API_URL}/api/schedules/${currentUser.id}/${year}/${term}?t=${Date.now()}`;
      const response = await fetch(url);
      
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


  const handleLogout = () => signOut(auth);

  //helper method to trigger alerts
  const triggerAlert = (title, desc, color) => {
    const id = Date.now() + Math.random();
    setActiveAlerts((prev) => [...prev, { title, desc, color, id }]);

    setTimeout(() => {
        setActiveAlerts((prev) => prev.filter((alert) => alert.id !== id));
    }, 5500);
    
  };

  


  


  return (
    <div>
      {/* pass User and trigger function to TopBar */}
      <OfflineAlert />
    
        {/* Custom alerts stack */}
        <div className="alertStack">
          {activeAlerts.map((activeAlert) => (
            <Alert 
              key={activeAlert.id} 
              alertTitle={activeAlert.title} 
              alertDesc={activeAlert.desc} 
              alertColor={activeAlert.color} 
            />
          ))}
        </div>

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
        setYear={setYear}
        term={term}
        setTerm={setTerm}
        userId={currentUser?.id}
        scheduleName={scheduleName}
        setScheduleName={setScheduleName}
        existingSchedules={existingSchedules}
      />

      <StatusSheet />

      <LoginModal 
        isOpen={showLogin} 
        onClose={() => setShowLogin(false)} 
        triggerAlert={triggerAlert} 
      />

  
    </div>
  );
}

export default App;
