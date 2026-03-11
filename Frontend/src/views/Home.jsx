import React, {useState} from 'react'
import {goToSearch} from '../js/screenTransitions.js'
import '../css/Home.css'
import OnHitEnter from '../js/searchBar.js'
import Section from '../components/Section.jsx';

function Home() {
  const [sections, setSections] = useState([]);
  
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
          <input type='search' id="searchBar" placeholder='Search Courses...' onKeyDown={async (e) => {
            if (e.key !== "Enter") return;
            // Fetch the data, then save it to React's state memory
            const fetchedSections = await OnHitEnter(e);
            setSections(fetchedSections || []); 
            
            goToSearch();
            }}></input>
          <button id="filterButton">☰</button>
        </div>
      </div>

      {/* Courses */}
      <div id='courseContainer'>
        {sections.length > 0 ? (
          sections.map((section, index) => {
            return <Section key={index} data={section} />;
          })
        ) : null}
      </div>
      
    </div>
  );
}

export default Home;