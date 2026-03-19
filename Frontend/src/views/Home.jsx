import {useState, useEffect, use} from 'react'
import {goToSearch} from '../js/screenTransitions.js'
import '../css/Home.css'
import OnHitEnter, { OnType } from '../js/searchBar.js'
import Section from '../components/Section.jsx';
import Filters from '../components/Filters.jsx';
import toggleFilter from '../js/toggleFilter.js';

let addedListener = false;

function Home() {
  const [sections, setSections] = useState([]);
  const [suggestions, setSuggestions] = useState([]);
  const [availability, setAvailability] = useState([]);

  // A reusable search function!
  const executeSearch = async (searchText, currentAvailability) => {
    // 1. Hide the dropdown
    setSuggestions([]); 
    
    // 2. NO MORE FAKE EVENT! Just pass the text and filters straight through
    const fetchedSections = await OnHitEnter(searchText, currentAvailability);
    
    // 3. Update the UI
    setSections(fetchedSections || []); 
    goToSearch();
  };
  
  // useEffect(() => {
  //   if (addedListener === false) {
  //     addedListener = true;
  //     window.addEventListener("popstate", () => {
  //       console.log("Hey");
  //       const fetchedSections = OnHitEnter(document.getElementById("searchBar"), availability);
  //       setSections(fetchedSections || []); 
  //     });
  //   }
  // })

  useEffect(() => {
    const searchInput = document.getElementById("searchBar");
    
    const handleInput = async (e) => {
      const text = e.target.value;
      console.log("Typing detected:", text);
      
      const results = await OnType(text, availability);
      setSuggestions(results || []);
    };

    if (searchInput) {
      searchInput.addEventListener('input', handleInput);
    }

    // Cleanup the listener when the component unmounts
    return () => {
      if (searchInput) {
        searchInput.removeEventListener('input', handleInput);
      }
    };
  }, []); // Empty array means this only wires up once when the page loads

  return (
    <div>

      {/* Background Objects */}
      <div id='bgImage'>
        <div id='bgImageTint'></div>
      </div>

      {/* Searchbar, titletext, other general content */}
      <div id='content'>
        <h1 id='title'>GCC Course Search</h1>
        
        {/* THE FIX: Put position: relative back on the main container, no extra wrappers! */}
        <div id="searchContainer">
          <div style={{ position: 'relative', width: 'fit-content', height: 'fit-content' }}>
          
          <input type='search' id="searchBar" placeholder='Search Courses...' 
          
          autoComplete='off'
          onKeyDown={async (e) => {
            if (e.key !== "Enter") return;
            executeSearch(e.target.value, availability);
          }}></input>

          {/* The Autocomplete Dropdown UI sits neatly inside the container */}
          {suggestions.length > 0 && (
            <ul id="autocompleteDropdown">
              {suggestions.map((suggestion, index) => (
                <li 
                  key={index} 
                  onClick={async () => {
                      // 1. Fill the search bar
                      document.getElementById("searchBar").value = suggestion;
                      // 2. Execute the search with the current filters
                      executeSearch(suggestion, availability);
                  }}
                >
                  {suggestion}
                </li>
              ))}
            </ul>
          )}

          </div>

          <button id="filterButton" onClick={()=>{toggleFilter(false)}}>☰</button>

        </div>
      </div>

      {/* Filters */}
      <Filters
        availability={availability} 
        setAvailability={setAvailability}
        triggerSearch={executeSearch}
      />

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