let shown = false;

export default function toggleFilter() {
    const filter = document.getElementById("availability_filter");
    if (shown) {
        shown = false;
        filter.style.display = 'none';
        return;
    }
    shown = true;
    filter.style.display = 'flex';
}