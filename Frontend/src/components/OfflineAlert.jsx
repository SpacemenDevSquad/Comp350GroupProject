import { useEffect, useState } from "react";
import "../css/offlineAlert.css";

async function checkConnection() {
    try {
    const res = await fetch("/ping");
    return true;
    } catch {
    return false;
    }
}

function OfflineAlert() {
    useEffect(() => {
        async function runCheck() {
        let status = false;
        status = await checkConnection();
        console.log("Connection:", status);
        if (status) document.getElementById("offlineAlertDiv").remove();
        }
        runCheck();
    }, []);
    return (
        <div id="offlineAlertDiv">
            <p>You are offline. Functionality may be limited.</p>
        </div>
    );
}

export default OfflineAlert;