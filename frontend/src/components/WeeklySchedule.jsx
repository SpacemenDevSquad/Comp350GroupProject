import '../css/weeklySchedule.css'
import React, { useState, useEffect, useCallback } from 'react';
import { createAlert } from '../js/createAlert.jsx';

function WeeklySchedule({ userId, year, term, scheduleName, setScheduleName, existingSchedules}){
    // ----STATE MANAGEMENT----
    // Tracks the schedule object, loading status, and total credits from the backend
    const [schedule, setSchedule] = useState(null);

    const [loading, setLoading] = useState(null);

    const [totalCreds, setCreds] = useState(null);
    const [newScheduleInput, setNewScheduleInput] = useState("");

    // ----DATA FETCHING----
    
    //pull the latest schedule and credit totals for the current user/semester
    async function fetchSchedule(){
        console.log(year, term)
        if (!userId || !scheduleName) return;
        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/schedule/${userId}/${year}/${term}/${scheduleName}`);
            const credsResponse = await fetch(`${import.meta.env.VITE_API_URL}/api/schedule/credits/${userId}/${year}/${term}/${scheduleName}`);
            const creds = await credsResponse.json();
            const data = await response.json();
            setSchedule(data);
            setCreds(creds);
            setLoading(false);

        } catch (error) {
            console.error("Error fetching schedule:", error);
            setLoading(false);
        }
    };
    
    //runs automatically when props/semester change or add/drop event
    useEffect(() => {
        fetchSchedule();
    }, [year, term, scheduleName, userId]);

    // Listen for add/drop refresh to do another fetch
    useEffect(() => {
        const handler = () => fetchSchedule();
        window.addEventListener('scheduleRefresh', handler);
        return () => window.removeEventListener('scheduleRefresh', handler);
    }, [year, term, scheduleName, userId]);

    if (!schedule) {
        return <div className="schedule-box">Loading your schedule...</div>;
    }

    // ----DATA TRANSFORMATION----
    // maps the list of sections by day char
    const scheduleMap= {
        'M': [],
        'T': [],
        'W': [],
        'R': [],
        'F': [],
    };

    for (const section of schedule.sections) {
        for (const timeslot of section.timeslots) {
            //adds time slot to the day array in the map
            scheduleMap[timeslot.day].push({
                title: section.course.title,
                start: timeslot.startTime,
                end: timeslot.endTime,
                dept: section.course.department.code,
                num: section.course.number,
                originalData: section
            });
        }
    }

    // ----HELPER FUNCTIONS----
    // Converts char's to full Day names
    function getDayName(day) {
        const names = { 
            'M': 'Monday', 
            'T': 'Tuesday', 
            'W': 'Wednesday', 
            'R': 'Thursday', 
            'F': 'Friday' 
        };
        return names[day];
    };

    //converts minutes from Midnight to standard Time (X:XX AM/PM) using same function as Timeslot.java
    function formatMinutesToTime(totalMinutes) {
        let hours = Math.floor(totalMinutes / 60);
        const minutes = totalMinutes % 60;
        const amPm = (hours >= 12) ? "PM" : "AM";

        // Convert military time to standard 12-hour format
        if (hours > 12) {
            hours -= 12;
        } else if (hours == 0) {
            hours = 12;
        }

        return `${hours}:${minutes.toString().padStart(2, '0')} ${amPm}`;
    }

    // ----ACTION HANDLERS----

    //Creates a new blank schedule
    const createNewSchedule = async () => {
    if (newScheduleInput.trim() !== "") {
        const newName = newScheduleInput.trim();
       
        await fetch(`${import.meta.env.VITE_API_URL}/api/schedule/${userId}/${year}/${term}/${newName}`);
    
        setScheduleName(newName);
        setNewScheduleInput("");
        window.dispatchEvent(new CustomEvent('scheduleRefresh'));
    }
};
    // Calls java delete endpoint to remove section
    async function dropSection(courseData) {
        const response = await fetch(`${import.meta.env.VITE_API_URL}/api/schedule/drop/${userId}/${year}/${term}/${scheduleName}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(courseData),
        });

        if (!response.ok) {
            createAlert("Could not drop course", "Not found in schedule", "red");
            return;
        }
        createAlert("Course Dropped", "Updated Schedule", "green");
        fetchSchedule();
    }

    // ----COMPONENT VARIABLES FOR JSX----
    const dayEntries = Object.entries(scheduleMap); //converts map into array of [key,value]
    const currSemester= schedule.currSemester;
    const hours = [ "", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM"];
    const termMap = {
        "F": "Fall",
        "S": "Spring"
    }
    
   if (!userId) {
        return (
            
            <div className="login-prompt-content">
                <h2 className="login-prompt-header">Access Restricted</h2>
                <p className="login-prompt-text">Please sign in to view and manage your custom schedules.</p>
            </div>
        
        );
    }

      
    

    return (
    <div className="schedule-container">
        <div className="schedule-controls">
            <div className="control-row">
                <label>Select Schedule:</label>
                <select
                    value={scheduleName}
                    onChange={(e) => setScheduleName(e.target.value)}
                    className="schedule-dropdown"
                    disabled={!userId}
                >
                    <option value="Main Schedule">Main Schedule</option>
                
                    {(existingSchedules || [])
                        .filter(name => name !== "Main Schedule")
                        .map((name, index) => (
                            <option key={`${name}-${index}`} value={name}>{name}</option>
                        ))}
                </select>
            </div>

            <div className="control-row">
                <input 
                    type="text" 
                    placeholder="New schedule name..."
                    value={newScheduleInput}
                    onChange={(e) => setNewScheduleInput(e.target.value)}
                    className="schedule-input"
                />
                <button onClick={createNewSchedule} className="schedule-plus-btn">+</button>
            </div>
        </div>
        



        <h2 className="schedule-header">Weekly View - {termMap[term]} {year}</h2>
        <p id="totalCredLabel">Total Credits: {totalCreds}</p>

        
        <div className="weekly-grid">
            {/* TIME COLUMN */}
            <div className="time-gutter">
                <div className="day-label" style={{border: "0px", margin: "1px"}}>Time</div>
                <div className="hours-container">
                    {hours.map((hour) => (
                        <div key={hour} className="hour-marker">
                            {hour}
                        </div>
                    ))}
                </div>
            </div>
            
            {/* DAY COLUMNS*/}
            {dayEntries.map(([day, courses]) => (
                
                <div key={day} className="day-column">
                    <div className="day-label">{getDayName(day)}</div>
                    <div className="courses-container">
                        {courses.length === 0 && <p className="no-classes">No Classes</p>}
                        
                        {courses.map((course, index) => {
                            const startOffset = 420; // 7 AM
                            const pixelsPerMinute = 80 / 60; // 1.333 pixels per minute
                            const labelHeight = 40; // height of .day-label in CSS

                            const topPosition = Math.round(((course.start - startOffset) * pixelsPerMinute) + labelHeight);
                            const blockHeight = Math.round((course.end - course.start) * pixelsPerMinute);

                            return (
                                <div 
                                    key={index} 
                                    className="calendar-card"
                                    style={{
                                        position: 'absolute',
                                        top: `${topPosition +2}px`,
                                        height: `${blockHeight-10}px`
                                    }}>

                                    <span className="card-dept">{course.dept} {course.num}</span>
                                    <p className="card-title">{course.title}</p>
                                    
                                    <p className="card-time">
                                        {formatMinutesToTime(course.start)} - {formatMinutesToTime(course.end)}
                                    </p>
                                    <div className="action-group">
                                        <button className="action-button" onClick={() => dropSection(course.originalData)}>Drop</button>
                                    </div>
                                   
                                </div>

                            );
                        })}
                    </div>
                </div>
            ))}
        </div>
    </div>
    );
}

export default WeeklySchedule;