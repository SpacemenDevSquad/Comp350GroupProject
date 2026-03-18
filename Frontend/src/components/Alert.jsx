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
        <div id="alertBox" class={alertColor+" "+alertID}>
            <h1 id="alertTitle" class={alertColor}>{alertTitle}</h1>
            <p id="alertDesc" class={alertColor}>{alertDesc}</p>
            <div id="loadingBar" class={alertColor+" "+loadBarID}></div>
        </div>
    )
}

export default Alert