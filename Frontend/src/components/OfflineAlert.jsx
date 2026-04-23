/* OfflineAlert.jsx */
import { useEffect, useState } from "react";
import "../css/offlineAlert.css";

function OfflineAlert() {
  const [isOnline, setIsOnline] = useState(navigator.onLine);

  useEffect(() => {
    const handleStatus = () => setIsOnline(navigator.onLine);

    // Listen for actual browser online/offline events
    window.addEventListener('online',  handleStatus);
    window.addEventListener('offline', handleStatus);

    return () => {
      window.removeEventListener('online',  handleStatus);
      window.removeEventListener('offline', handleStatus);
    };
  }, []);

  // ONLY show if offline. 
  // Returning null allows React to safely unmount the element without a crash.
  if (isOnline) return null;

  return (
    <div id="offlineAlertDiv">
      <p>You are offline. Functionality may be limited.</p>
    </div>
  );
}

export default OfflineAlert;