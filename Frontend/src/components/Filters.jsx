import '../css/Filters.css';
import { useState } from 'react';

const DAYS_OF_WEEK = ['M', 'T', 'W', 'R', 'F'];

function Filters({ availability, setAvailability, triggerSearch }) {
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

    const currentSearchText = document.getElementById("searchBar").value;
    if (triggerSearch) triggerSearch(currentSearchText, updatedAvailability);
  };

  const handleRemove = (indexToRemove) => {
    const updatedAvailability = availability.filter((_, index) => index !== indexToRemove);
    setAvailability(updatedAvailability);

    const currentSearchText = document.getElementById("searchBar").value;
    if (triggerSearch) triggerSearch(currentSearchText, updatedAvailability);
  };

  return (
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
  );
}

export default Filters;