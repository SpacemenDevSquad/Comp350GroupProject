import { useLayoutEffect } from 'react';
import '../css/alert.css'
import popOut from '../js/alertPop.js'

function Alert({ alertTitle="DefaultTitle", alertDesc="DefaultDesc", alertColor="green"}) {

    const alertID = "id_" + Math.random().toString(36).substring(7);
    const loadBarID = "bar_" + Math.random().toString(36).substring(7);

    useLayoutEffect(() => {
        popOut(alertID, loadBarID);
    }, [alertID, loadBarID]);

    return (
        <div id="alertBox" className={alertColor+" "+alertID}>
            <h1 id="alertTitle" className={alertColor}>{alertTitle}</h1>
            <p id="alertDesc" className={alertColor}>{alertDesc}</p>
            <div id="loadingBar" className={alertColor+" "+loadBarID}></div>
        </div>
    )
}

export default Alert