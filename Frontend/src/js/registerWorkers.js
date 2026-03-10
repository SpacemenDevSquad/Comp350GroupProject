addEventListener("DOMContentLoaded", () => {
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register('/serviceWorker.js')
        .then(reg => {
        console.log('SW registered', reg);
        })
        .catch(err => {
        console.error('SW registration failed:', err);
        });
    }
})