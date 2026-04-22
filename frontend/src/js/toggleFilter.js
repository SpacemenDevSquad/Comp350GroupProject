let shown = false;

export default function toggleFilter(forceOff = false) {
    const filter = document.getElementById("filters");
    if (shown || forceOff) {
        shown = false;
        let height = filter.getBoundingClientRect().height;
        filter.style.height = (height-20).toString()+'px';
        setTimeout(() => {filter.style.height = '0px';}, 1);
        setTimeout(() => {filter.style.display = 'none';}, 350);
        return;
    }
    shown = true;
    filter.style.height = 'auto';
    filter.style.display = 'flex';
    let height = filter.getBoundingClientRect().height;
    filter.style.height = '0px';
    setTimeout(() => {filter.style.height = (height-20).toString()+'px';}, 1);
    setTimeout(() => {filter.style.height = 'auto';}, 300);
}

// Handle click outside filter menu to close it
function handleClickOutsideFilter(event) {
    const filter = document.getElementById("filters");
    const filterButton = document.getElementById("filterButton");
    
    if (filter && filterButton && shown) {
        // Check if click is outside both the filter menu and the filter button
        if (!filter.contains(event.target) && !filterButton.contains(event.target)) {
            toggleFilter(true); // Force close
        }
    }
}

function schedulePlacement() {
    const filter = document.getElementById("filters");
    const searchBar = document.getElementById("searchContainer");
    if (filter !== null && searchBar !== null) {
        const location = searchBar.getBoundingClientRect()
        let bottomBar = location.bottom;
        bottomBar += 15;
        filter.style.top = bottomBar.toString()+'px';
    }
    setTimeout(schedulePlacement, 50);
}

// Add click-outside listener for filter menu
document.addEventListener('mousedown', handleClickOutsideFilter);

schedulePlacement();