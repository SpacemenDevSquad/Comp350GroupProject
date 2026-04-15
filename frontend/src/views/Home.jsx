import {useState, useEffect, use} from 'react'
import {goToSearch} from '../js/screenTransitions.js'
import '../css/Home.css'
import OnHitEnter, { OnType } from '../js/searchBar.js'
import Section from '../components/Section.jsx';
import Filters from '../components/Filters.jsx';
import toggleFilter from '../js/toggleFilter.js';
import UpdateSemester from '../components/UpdateSemester.jsx';

let addedListener = false;

function Home({year, setYear, term, setTerm}) {
  const [sections, setSections] = useState([]);
  const [suggestions, setSuggestions] = useState([]);
  const [availability, setAvailability] = useState([]);
  const [credits, setCredits] = useState(0);

  // Reusable seaerch function to avoid duplicated logic
  const executeSearch = async (searchText, year, term, currentAvailability, credits) => {
    // Hide the autocomplete dropdown
    setSuggestions([]); 
    
    // Pass the text and filters straight through to searchController.java and get the results
    const fetchedSections = await OnHitEnter(searchText, year, term, currentAvailability, credits);
    
    // Update the UI
    setSections(fetchedSections || []); 
    goToSearch();
  };

  useEffect(() => {
    const searchInput = document.getElementById("searchBar");
    
    const handleInput = async (e) => {
      const text = e.target.value;
      console.log("Typing detected:", text);
      
      // Get autocomplete suggestions based on the current input
      const results = await OnType(text, year, term, availability, credits);
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
  }, [year, term]);

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
          <div style={{ position: 'relative', width: 'fit-content', height: 'fit-content' }}>
          
          <input type='search' id="searchBar" placeholder='Search Courses...' 
          
          autoComplete='off'
          onKeyDown={async (e) => {
            if (e.key !== "Enter") return;
            // Call the frontend executeSearch function (which is not the same as the backend executeSearch)
            executeSearch(e.target.value, year, term, availability, credits);
          }}></input>

          {suggestions.length > 0 && (
            <ul id="autocompleteDropdown">
              {suggestions.map((suggestion, index) => (
                <li 
                  key={index} 
                  onClick={async () => {
                      // Fill the search bar
                      document.getElementById("searchBar").value = suggestion;
                      // Execute the search with the current filters
                      executeSearch(suggestion, year, term, availability, credits);
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
        credits={credits}
        setCredits={setCredits}
        triggerSearch={executeSearch}
        year={year}
        setYear={setYear}
        term={term}
        setTerm={setTerm}
      />

      {/* Courses */}
      <div id='courseContainer'>
        {sections.length > 0 ? (
          sections.map((section, index) => {
            return <Section key={index} data={section} year={year} term={term} />;
          })
        ) : null}
      </div>
      
    </div>
  );
}

export default Home;