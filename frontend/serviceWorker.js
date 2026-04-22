const cacheName = "gccCourseSchedule - File Caching v1"
const apiCacheName = "gccCourseSchedule - API Caching v1"
const cachedFiles = [
  "/",
  "/schedule",
  "/status",
  "/manifest.json",
  "/src/main.jsx",
  "/src/App.jsx",
  "/src/views/Home.jsx",
  "/src/views/Schedule.jsx",
  "/src/views/StatusSheet.jsx",
  "/src/js/alertPop.js",
  "/src/js/createAlert.jsx",
  "/src/js/firebase.js",
  "/src/js/screenTransitions.js",
  "/src/js/searchBar.js",
  "/src/js/toggleFilter.js",
  "/src/images/AppleIcon.png",
  "/src/images/backArrow.svg",
  "/src/images/bg1.png",
  "/src/images/bg2.png",
  "/src/images/calendar.svg",
  "/src/images/favicon.png",
  "/src/images/logo.png",
  "/src/images/PRIJ_horizontal_white.svg",
  "/src/images/profile.svg",
  "/src/images/search.svg",
  "/src/images/searchIcon.svg",
  "/src/fonts/robotoslab-variablefont_wght-webfont.woff",
  "/src/fonts/robotoslab-variablefont_wght-webfont.woff2",
  "/src/css/alert.css",
  "/src/css/App.css",
  "/src/css/Filters.css",
  "/src/css/fonts.css",
  "/src/css/Home.css",
  "/src/css/index.css",
  "/src/css/login.css",
  "/src/css/requirements.css",
  "/src/css/schedule.css",
  "/src/css/section.css",
  "/src/css/StatusSheet.css",
  "/src/css/TopBar.css",
  "/src/css/weeklySchedule.css",
  "/src/components/Alert.jsx",
  "/src/components/Course.jsx",
  "/src/components/Filters.jsx",
  "/src/components/RequirementGroup.jsx",
  "/src/components/Section.jsx",
  "/src/components/TopBar.jsx",
  "/src/components/UpdateSemester.jsx",
  "/src/components/WeeklySchedule.jsx",
  "/@vite/client",
  "/node_modules/vite/dist/client/env.mjs",
  "/serviceWorker.js",
  "/src/js/registerWorkers.js",
  "/@react-refresh",
  "/src/images/PRIJ_horizontal_white.svg?import"
]

async function cacheFirst(request) {
  const cached = await caches.match(request);
  return cached || fetch(request);
}

async function networkFirst(request) {
  const cache = await caches.open(apiCacheName);

  try {
    const response = await fetch(request);

    // Save a copy to cache
    cache.put(request, response.clone());

    return response;
  } catch (err) {
    // Offline → try cache
    const cached = await cache.match(request);
    return cached || null;
  }
}


self.addEventListener("activate", (event) => {
  event.waitUntil(self.clients.claim());
});

self.addEventListener('install', (event) => {
  console.log('Service worker installed');
  self.skipWaiting();
  self.clients.claim();
  event.waitUntil(
    caches.open(cacheName).then((cache) => {
      console.log('Caching app files');
      return cache.addAll(cachedFiles);
    })
  );
});

self.addEventListener('fetch', (event) => {
  const {request} = event;

  // Handle API calls differently
  if (isApiRequest(request)) {
    event.respondWith(networkFirst(request));
  } else {
    event.respondWith(cacheFirst(request));
  }
});


const isApiRequest = (request) => {return request.url.includes("/api/")}