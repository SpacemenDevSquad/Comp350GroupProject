export function createAlert(alertTitle = "DefaultTitle", alertDesc = "DefaultDesc", color = "green") {
  window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
    detail: {
      title: alertTitle,
      desc: alertDesc,
      color
    }
  }));
}
