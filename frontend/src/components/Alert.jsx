import '../css/alert.css'

function Alert({ alertTitle="DefaultTitle", alertDesc="DefaultDesc", alertColor="green"}) {
    return (
        <div className={`alertBox ${alertColor}`}>
            <h1 className={`alertTitle ${alertColor}`}>{alertTitle}</h1>
            <p className={`alertDesc ${alertColor}`}>{alertDesc}</p>
            <div className={`loadingBar ${alertColor}`}></div>
        </div>
    )
}

export default Alert
