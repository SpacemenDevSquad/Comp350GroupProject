export default function popOut(alertID, loadBarID) {
    let loadBar = document.getElementsByClassName(loadBarID)[0];
    let alertBox = document.getElementsByClassName(alertID)[0];
    if (loadBar === undefined || alertBox === undefined) {
        console.log("Waiting");
        setTimeout(() => {popOut(alertID, loadBarID)}, 50);
    } else {
        console.log("Go");
        console.log(alertBox);
        alertBox.style.transform = "translate(-215px, 0px)";
        setTimeout(() => {shrinkBar(100, loadBar, alertBox)}, 15);
    }
}

function shrinkBar(width, loadBar, alertBox) {
    width -= 5;
    loadBar.style.width = width.toString()+"%";
    if (width > 0) {
        setTimeout(() => {shrinkBar(width, loadBar, alertBox)}, 100);
        return;
    }
    alertBox.style.transform = "translate(215px, 0px)";
    setTimeout( () => {deleteAlert(alertBox)}, 500);
}

function deleteAlert(alertBox) {
    alertBox.remove()
}