addEventListener("DOMContentLoaded", () => { 

    content = document.getElementById("content");
    backArrow = document.getElementById("backArrow");
    scheduleBlock = document.getElementById("scheduleBlock");
    titleText = document.getElementById("title");
    courseContainer = document.getElementById("courseContainer")
    searchBar = document.getElementById("searchBar");

    window.onpopstate = renderPage;
    renderPage()
})

let content;
let backArrow;
let scheduleBlock;
let titleText;
let courseContainer;
let searchBar;

export default async function calendarTransition() {

    if (location.pathname === '/schedule') {
        if (searchBar.value !== '') {
            goToSearch();
            return;
        }
        gotToHome();
        return;
    }
    gotToCalendar();

}

function gotToHome() {
    history.pushState({}, "", "/");
    renderPage();
}

function gotToCalendar() {
    history.pushState({}, "", "/schedule");
    renderPage();
}

export function goToSearch() {
    history.pushState({}, "", "/search#"+searchBar.value);
    renderPage();
}



// Page rendering code

async function renderPage() {
    if (location.pathname === '/schedule') Calendar();
    if (location.pathname === '/search') Search();
    if (location.pathname === '/') Home();
}

// Transition to calendar view
async function Calendar() {
    if (content.style.transform !== '') {
        content.style.transform = 'translateX(-100vw) translateY(calc(-50vh + 55px))';
    } else {
        content.style.transform = 'translateX(-100vw)';
    }

    backArrow.style.opacity = '100%';
    scheduleBlock.style.transform = 'translateX(-100vw)';

    courseContainer.style.transform = 'translateX(-100vw)';
}


// Transition back to home
async function Home() {
    titleText.style.opacity = '100%';
    titleText.style.fontSize = '5vw';
    titleText.style.height = '';

    content.style.transform = '';

    backArrow.style.opacity = '0%';

    scheduleBlock.style.transform = '';

    courseContainer.style.transform = '';
    courseContainer.style.top = '0px';
    courseContainer.style.height = '0px';
}

// Transition to search view
async function Search() {
    searchBar.value = decodeURIComponent(location.hash.substring(1));

    titleText.style.opacity = '0%';
    titleText.style.fontSize = '0vw';
    titleText.style.height = '50px';

    content.style.transform = 'translateY(calc(-50vh + 55px))';

    courseContainer.style.transform = '';
    courseContainer.style.top = '120px';
    courseContainer.style.height = '1000px';

    backArrow.style.opacity = '0%';

    scheduleBlock.style.transform = '';
}