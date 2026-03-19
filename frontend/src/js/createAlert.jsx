import React from 'react';
import ReactDOM from 'react-dom/client';
import Alert from '../components/Alert.jsx';

export function createAlert(alertTitle = "DefaultTitle", alertDesc = "DefaultDesc", color = "green") {
  // Create a new container div
  const container = document.createElement('div');
  document.body.appendChild(container);
  setTimeout(()=>{killParentDiv(container)}, 3500);

  // Create a React root in that div (React 18+)
  const root = ReactDOM.createRoot(container);

  // Render the Alert component
  root.render(
    <Alert
      alertTitle={alertTitle}
      alertDesc={alertDesc}
      alertColor={color}
      onExpire={() => {
        // Clean up the DOM after the alert ends
        root.unmount();
        container.remove();
      }}
    />
  );
}

function killParentDiv(div) {
  div.remove();
}