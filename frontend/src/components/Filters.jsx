import '../css/Filters.css';
import { useState, useEffect } from 'react';
import UpdateSemester from './UpdateSemester';

const DAYS_OF_WEEK = ['M', 'T', 'W', 'R', 'F'];

function Filters({ availability, setAvailability, credits, setCredits, triggerSearch, year, setYear, term, setTerm, noTimeSections, setNoTimeSections }) {
  const [daysToggled, setDaysToggled] = useState([]);
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  const toggleDayAvailability = (day) => {
    setDaysToggled((prevDays) => {
      if (prevDays.includes(day)) {
        return prevDays.filter((d) => d !== day);
      } else {
        return [...prevDays, day];
      }
    });
  };

  const handleAdd = () => {
    if (daysToggled.length === 0 || !startTime || !endTime) {
      alert("Please select days and times.");
      return;
    }

    const newAvailabilityBlock = {
      days: DAYS_OF_WEEK.filter(day => daysToggled.includes(day)).join(''), 
      startTime: startTime,
      endTime: endTime,
    };

    const updatedAvailability = [...availability, newAvailabilityBlock];
    setAvailability(updatedAvailability);

    setDaysToggled([]);
    setStartTime("");
    setEndTime("");

    // After adding a new availability block, trigger a search with the updated filters
    const currentSearchText = document.getElementById("searchBar").value;
    if (triggerSearch) triggerSearch(currentSearchText, updatedAvailability, credits, noTimeSections);
  };

  const handleRemove = (indexToRemove) => {
    const updatedAvailability = availability.filter((_, index) => index !== indexToRemove);
    setAvailability(updatedAvailability);

    // After removing an availability block, trigger a search with the updated filters
    const currentSearchText = document.getElementById("searchBar").value;
    if (triggerSearch) triggerSearch(currentSearchText, updatedAvailability, credits, noTimeSections);
  };



  return (
    <div id='filters'>
      <div id='availability_filter'>
        <h1 id="filterTitle">Available:</h1>

        <button id="addButton" onClick={handleAdd}>Add</button>
        
        <div id='availability_container'>
          <div id='availability_time_selectors'>
            <div id='filter_availability_times'>
              <input 
                type="time" 
                value={startTime}
                onChange={(e) => setStartTime(e.target.value)} 
                className='time_availability_input'
              />
              <p>to</p>
              <input 
                type="time" 
                value={endTime}
                onChange={(e) => setEndTime(e.target.value)} 
                className='time_availability_input'
              />
            </div>

            <div id='filter_availability_days'>
              {DAYS_OF_WEEK.map((day) => {
                const isToggled = daysToggled.includes(day);
                return (
                  <div 
                    key={day}
                    className={`filter_availability_day ${isToggled ? "toggled" : ""}`} 
                    onClick={() => toggleDayAvailability(day)}
                  >
                    {day}
                  </div>
                );
              })}
            </div>
          </div>

        </div>
        

        <div>
          <ul>
            {availability.map((block, index) => (
              <li key={index} class="filterItem">
                {block.days} {block.startTime} - {block.endTime}
                {/* NEW: Remove button added next to the text */}
                <button 
                  class = "filterRemoveButton"
                  onClick={() => handleRemove(index)}
                  style={{ marginLeft: "10px" }} // Added a little margin for spacing
                >
                  Remove
                </button>
              </li>
            ))}
          </ul>
        </div>
      </div>
      <div>
        <div id="creditFilter">
          <span>Credits:</span>
        <select name="credit_filter" id="credit_filter" value={credits} onChange={(e) => {
            const newCredits = parseInt(e.target.value);
            setCredits(newCredits);
            const currentSearchText = document.getElementById("searchBar").value;
            if (triggerSearch && currentSearchText) {
              triggerSearch(currentSearchText, availability, credits, noTimeSections);
            }
          }}>
            <option value="0">Any</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
          </select>
        </div>
      </div>

      <div style={{ marginTop: '20px', marginBottom: '20px', display: 'flex', alignItems: 'center', position: 'relative', zIndex: 10, minHeight: '24px' }}>
        <label htmlFor="noTimeCheckbox" style={{ 
            cursor: 'pointer', 
            fontSize: '18px', 
            color: '#000000',
            fontWeight: '500' 
          }}>
          Display courses with no time slots:
        </label>
        <input 
          type="checkbox" 
          id="noTimeCheckbox"
          checked={noTimeSections} 
          onChange={(e) => {
            const isChecked = e.target.checked;
            setNoTimeSections(isChecked);
            
            const currentSearchText = document.getElementById("searchBar").value;
            if (triggerSearch) {
               triggerSearch(currentSearchText, year, term, availability, credits, isChecked);
            }
          }} 
          style={{ marginRight: '8px', cursor: 'pointer' }}
        />
      </div>

      <UpdateSemester
        year={year}
        setYear={setYear}
        term={term}
        setTerm={setTerm}
      />
    </div>
  );
}

export default Filters;