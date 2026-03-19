import '../css/weeklySchedule.css'
import React, { useState, useEffect } from 'react';
import { createAlert } from '../js/createAlert.jsx';

const year = 2023;
const term = 'F';

function WeeklySchedule(){
    //state that holds the schedule from backend
    const [schedule, setSchedule] = useState(null);

    const [loading, setLoading] = useState(null);

    const [totalCreds, setCreds] = useState(null);

    //FETCH SCHEDULE
    async function fetchSchedule(){
        try {
            //fetch for manual user 1
            const response = await fetch(`http://localhost:8096/api/schedule/1/${year}/${term}`);
            const creds = await (await fetch(`http://localhost:8096/api/schedule/credits/1/${year}/${term}`)).json()
            const data = await response.json();
            setSchedule(data);
            setCreds(creds);
            setLoading(false);

        } catch (error) {
            console.error("Error fetching schedule:", error);
            setLoading(false);
        }
    };
    
    //runs automatically when page opens
    useEffect(() => {
        fetchSchedule();
        //refreshes every 3000 ms
        const interval = setInterval(fetchSchedule, 3000); 
        return () => clearInterval(interval);
    }, []);

    if (!schedule) {
        return <div className="schedule-box">Loading your schedule...</div>;
    }

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

    async function dropSection(courseData) {
        const response = await fetch(`http://localhost:8096/api/schedule/drop/1/${year}/${term}`, {
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

    //variables
    const dayEntries = Object.entries(scheduleMap); //converts map into array of [key,value]
    const currSemester= schedule.currSemester;
    const hours = [ "", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM"];
    
    return (
    <div className="schedule-container">
        <h2 className="schedule-header">Weekly View - Fall 2023</h2>
        <p id="totalCredLabel">Total Credits: {totalCreds}</p>
        
        <div className="weekly-grid">
            {/* Time Gutter */}
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
            
            {/* Day columns*/}

            {dayEntries.map(([day, courses]) => (
                
                <div key={day} className="day-column">
                    <div className="day-label">{getDayName(day)}</div>
                    <div className="courses-container">
                        {courses.length === 0 && <p className="no-classes">No Classes</p>}
                        
                        {courses.map((course, index) => {
                            const startOffset = 420; // 7 AM
                            const pixelsPerMinute = 80 / 60; // 1.333 pixels per minute
                            const labelHeight = 40; // The height of your .day-label in CSS

                        
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