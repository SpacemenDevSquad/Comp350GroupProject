addEventListener("DOMContentLoaded", () => {
    // Avoid SW + dev-server collisions (e.g., stale Vite dev assets in production).
    if (import.meta.env.PROD && 'serviceWorker' in navigator) {
        navigator.serviceWorker.register('/serviceWorker.js')
        .then(reg => {
        console.log('SW registered', reg);
        })
        .catch(err => {
        console.error('SW registration failed:', err);
        });
    }
})
