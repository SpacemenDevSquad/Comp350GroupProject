export default function popOut(alertID, loadBarID, maxRetries = 100) {
    let retries = 0;
    
    function attemptPop() {
        const loadBar = document.getElementsByClassName(loadBarID)[0];
        const alertBox = document.getElementsByClassName(alertID)[0];
        
        if (loadBar === undefined || alertBox === undefined) {
            if (retries < maxRetries) {
                retries++;
                requestAnimationFrame(attemptPop); // Safer than setTimeout recursion
                return;
            } else {
                console.warn(`Alert elements not found after ${maxRetries} attempts: ${alertID}, ${loadBarID}`);
                return; // Fail silently
            }
        }
        
        // Safe animation
        try {
            alertBox.style.transform = "translate(-215px, 0px)";
            shrinkBar(100, loadBar, alertBox);
        } catch (e) {
            console.error('Alert animation error:', e);
        }
    }
    
    attemptPop();
}

function shrinkBar(width, loadBar, alertBox) {
    if (!loadBar || !alertBox) return;
    
    width -= 5;
    loadBar.style.width = width.toString() + "%";
    
    if (width > 0) {
        setTimeout(() => shrinkBar(width, loadBar, alertBox), 100);
        return;
    }
    
    alertBox.style.transform = "translate(215px, 0px)";
    setTimeout(() => deleteAlert(alertBox), 500);
}

function deleteAlert(alertBox) {
    // if (alertBox && alertBox.parentNode) {
    //     alertBox.remove();
    // }
}
