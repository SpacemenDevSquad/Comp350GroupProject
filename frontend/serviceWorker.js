const cacheName = "gccCourseSchedule - File Caching"
const cachedFiles = [
  "/index.html",
  "/src/App.jsx"
]


self.addEventListener('install', (event) => {
  console.log('Service worker installed');
  event.waitUntil(
    caches.open(cacheName).then((cache) => {
      console.log('Caching app files');
      return cache.addAll(cachedFiles);
    })
  );
});

self.addEventListener('fetch', (event) => {
  event.respondWith(
    fetch(event.request).catch(() => {
      return caches.match(event.request);
    })
  );
});