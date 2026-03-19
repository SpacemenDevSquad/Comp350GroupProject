let shown = false;

export default function toggleFilter(forceOff = false) {
    const filter = document.getElementById("availability_filter");
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

function schedulePlacement() {
    const filter = document.getElementById("availability_filter");
    const searchBar = document.getElementById("searchContainer");
    if (filter !== null && searchBar !== null) {
        const location = searchBar.getBoundingClientRect()
        let bottomBar = location.bottom;
        bottomBar += 15;
        filter.style.top = bottomBar.toString()+'px';
    }
    setTimeout(schedulePlacement, 50);
}

schedulePlacement();