import { useEffect } from 'react';
import '../css/alert.css'
import popOut from '../js/alertPop.js'

function Alert({ alertTitle="DefaultTitle", alertDesc="DefaultDesc", alertColor="green"}) {

    const alertID = Math.random()
    const loadBarID = Math.random()

    useEffect(() => {
        popOut(alertID, loadBarID);
    }, []);

    return (
        <div id="alertBox" className={alertColor+" "+alertID}>
            <h1 id="alertTitle" className={alertColor}>{alertTitle}</h1>
            <p id="alertDesc" className={alertColor}>{alertDesc}</p>
            <div id="loadingBar" className={alertColor+" "+loadBarID}></div>
        </div>
    )
}

export default Alert