addEventListener("DOMContentLoaded", () => { 

    content = document.getElementById("content");
    backArrow = document.getElementById("backArrow");
    scheduleBlock = document.getElementById("scheduleBlock");
    titleText = document.getElementById("title");
    courseContainer = document.getElementById("courseContainer")
    searchBar = document.getElementById("searchBar");
    statusSheetBlock = document.getElementById("statusSheetBlock");

    window.onpopstate = renderPage;
    renderPage()
})

let content;
let backArrow;
let scheduleBlock;
let titleText;
let courseContainer;
let searchBar;
let statusSheetBlock;
let statusBackArrow;

export default async function calendarTransition() {
    catchMissingElements(); 

    if (location.pathname === '/schedule') {
        if (searchBar && searchBar.value !== '') {
            goToSearch();
            return;
        }
        gotToHome();
        return;
    }
    gotToCalendar();
}

export async function statusSheetTransition() {
    catchMissingElements();

    if (location.pathname === '/status') {
        if (searchBar && searchBar.value !== '') {
            goToSearch();
            return;
        }
        gotToHome();
        return;
    }
    goToStatusSheet();
}

export function gotToHome() {
    history.pushState({}, "", "/");
    renderPage();
}

function gotToCalendar() {
    history.pushState({}, "", "/schedule");
    renderPage();
}

export function goToSearch() {
    catchMissingElements();
    history.pushState({}, "", "/search?"+searchBar.value);
    renderPage();
}

export function goToStatusSheet() {
    catchMissingElements();
    history.pushState({}, "", "/status");
    renderPage();
}


// Page rendering code
async function renderPage() {
    catchMissingElements();
    if (location.pathname === '/schedule') Calendar();
    if (location.pathname === '/search') Search();
    if (location.pathname === '/') Home();

    if (location.pathname === '/status') StatusSheetView();
}

// Transition to calendar view
async function Calendar() {
    if (content) {
        if (content.style.transform !== '') {
            content.style.transform = 'translateX(-100vw) translateY(calc(-50vh + 55px))';
        } else {
            content.style.transform = 'translateX(-100vw)';
        }
    }

    if (backArrow) backArrow.style.opacity = '100%';
    if (scheduleBlock) scheduleBlock.style.transform = 'translateX(-100vw)';
    if (courseContainer) courseContainer.style.transform = 'translateX(-100vw)';
    
    if (statusSheetBlock) statusSheetBlock.style.transform = '';
}


// Transition back to home
async function Home() {
    catchMissingElements();
    if(titleText){
        titleText.style.opacity = '100%';
        titleText.style.fontSize = '5vw';
        titleText.style.height = '';
    }

    if(content){
        content.style.transform = '';
    }

    if(backArrow){
        backArrow.style.opacity = '0%';
    }
    
    if(scheduleBlock){
        scheduleBlock.style.transform = '';
    }   
    
    if(courseContainer){
        courseContainer.style.transform = '';
        courseContainer.style.top = '0px';
        courseContainer.style.height = '0px';       
    }

    if (statusSheetBlock){
        statusSheetBlock.style.transform = '';
    }
    if (statusBackArrow){
        statusBackArrow.style.opacity = '0%';
    }
}

// Transition to search view
async function Search() {
    catchMissingElements();
    searchBar.value = decodeURIComponent(location.search.substring(1));

    titleText.style.opacity = '0%';
    titleText.style.fontSize = '0vw';
    titleText.style.height = '50px';

    content.style.transform = 'translateY(calc(-50vh + 55px))';

    courseContainer.style.transform = '';
    courseContainer.style.top = '120px';
    courseContainer.style.height = 'calc(100vh - 120px)';

    backArrow.style.opacity = '0%';

    scheduleBlock.style.transform = '';

    if (statusSheetBlock){
        statusSheetBlock.style.transform = '';
    }
    if (statusBackArrow){
        statusBackArrow.style.opacity = '0%';
    }
}

async function StatusSheetView() {
    catchMissingElements();

    // Push Home/Search out to the left
    if (content.style.transform !== '') {
        content.style.transform = 'translateX(-100vw) translateY(calc(-50vh + 55px))';
    } else {
        content.style.transform = 'translateX(-100vw)';
    }
    
    if (courseContainer) courseContainer.style.transform = 'translateX(-100vw)';
    if (scheduleBlock) scheduleBlock.style.transform = '';

    if (statusSheetBlock) statusSheetBlock.style.transform = 'translateX(-100vw)';
    if (statusBackArrow) statusBackArrow.style.opacity = '100%';
}

// A tiny safety net to catch elements if React rendered them late
function catchMissingElements() {
    if (!content) content = document.getElementById("content");
    if (!backArrow) backArrow = document.getElementById("backArrow");
    if (!scheduleBlock) scheduleBlock = document.getElementById("scheduleBlock");
    if (!titleText) titleText = document.getElementById("title");
    if (!courseContainer) courseContainer = document.getElementById("courseContainer");
    if (!searchBar) searchBar = document.getElementById("searchBar");
    if (!statusSheetBlock) statusSheetBlock = document.getElementById("statusSheetBlock");
    if (!statusBackArrow) statusBackArrow = document.getElementById("statusBackArrow");
}