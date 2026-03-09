import {goToSearch} from '../js/screenTransitions.js'
import '../css/Home.css'
import OnHitEnter from '../js/searchBar.js'

function Home() {
  return (
    <div>

      {/* Background Objects */}
      <div id='bgImage'>
        <div id='bgImageTint'></div>
      </div>

      {/* Searchbar, titletext, other general content */}
      <div id='content'>
        <h1 id='title'>GCC Course Search</h1>
        <div id="searchContainer">
          <input type='search' id="searchBar" placeholder='Search Courses...' onKeyDown={(e) => {
            if (e.key !== "Enter") return;
            OnHitEnter(); 
            goToSearch();
            }}></input>
          <button id="filterButton">☰</button>
        </div>
      </div>

      {/* Courses */}
      <div id='courseContainer'></div>
    </div>
  );
}

export default Home;