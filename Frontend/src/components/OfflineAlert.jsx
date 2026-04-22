import { useEffect, useState } from "react";

async function checkConnection() {
    console.log("Checking...")
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
        const status = await checkConnection();
        console.log("Connection:", status);
        setIsOnline(status);
        }
        runCheck();
    }, []);
    return <div>TEST</div>;
}

export default OfflineAlert;