import '../css/weeklySchedule.css'
import React, { useState, useEffect, useCallback, useRef } from 'react';
import { createAlert } from '../js/createAlert.jsx';
import trashCanIcon from '../images/trashCan.svg'
import exportIcon from '../images/exportIcon.svg'
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import UpdateSemester from './UpdateSemester';

function WeeklySchedule({ userId, year, setYear, term, setTerm, scheduleName, setScheduleName, existingSchedules}){
    // ----STATE MANAGEMENT----
    // Tracks the schedule object, loading status, and total credits from the backend
    const [schedule, setSchedule] = useState(null);

    const [loading, setLoading] = useState(null);

    const [totalCreds, setCreds] = useState(null);
    const [newScheduleInput, setNewScheduleInput] = useState("");

    const printRef = useRef();

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


    if (!userId) {
        return (
            
            <div className="login-prompt-content">
                <h2 className="login-prompt-header">Access Restricted</h2>
                <p className="login-prompt-text">Please sign in to view and manage your custom schedules.</p>
            </div>
        
        );
    }

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
    const noTimeSections= [];

    if (schedule && schedule.sections) {
        for (const section of schedule.sections) {
            if (!section.timeslots || section.timeslots.length === 0) {
            noTimeSections.push(section);
        } else {
            // Only iterate if timeslots actually exist
            for (const timeslot of section.timeslots) {
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
    }};

    const deleteSchedule = async () => {
        //main schedule cannot be deleted
        if (scheduleName === "Main Schedule") {
            window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
                detail: { title: "Action Denied", desc: "You cannot delete the Main Schedule.", color: "red" }
            }));
            return;
        }

        if (!window.confirm(`Are you sure you want to delete "${scheduleName}"?`)) return;

        try {
            
            const encodedName = encodeURIComponent(scheduleName);
            const url = `${import.meta.env.VITE_API_URL}/api/schedule/delete/${userId}/${year}/${term}/${encodedName}`;
            
            const response = await fetch(url, {
                method: 'DELETE',
            });

            if (response.ok) {
                window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
                    detail: { title: "Schedule Deleted", desc: `"${scheduleName}" has been removed.`, color: "green" }
                }));
                
                setScheduleName("Main Schedule");
                setTimeout(() => {
                    window.dispatchEvent(new CustomEvent('scheduleRefresh'));
                }, 0);
            } else {
                // DIAGNOSIS: Capture the actual error from the backend
                const errorText = await response.text();
                console.error("Delete Failed. Status:", response.status, "Error:", errorText);
                
                window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
                    detail: { title: "Error", desc: `Server returned ${response.status}: ${errorText || "Could not delete"}`, color: "red" }
                }));
            }
        } catch (error) {
            console.error("Network/Delete error:", error);
        }
    };

    /* WeeklySchedule.jsx - Update the exportPDF function */

    /* WeeklySchedule.jsx */

    const exportPDF = async () => {
        const element = printRef.current;
        if (!element) return;

        // 1. Temporarily force the element to its full height to capture everything
        const originalHeight = element.style.height;
        const originalOverflow = element.style.overflow;
        
        element.style.height = 'auto';
        element.style.overflow = 'visible';

        // 2. Capture the entire content (including the 1200px day columns)
        const canvas = await html2canvas(element, {
            scale: 2, 
            useCORS: true, 
            backgroundColor: "#a00000",
            scrollY: -window.scrollY, // Fixes offset if page is scrolled
        });

        // Restore original styles immediately
        element.style.height = originalHeight;
        element.style.overflow = originalOverflow;

        const imgData = canvas.toDataURL('image/png');
        
        // 3. Initialize PDF (Landscape A4)
        const pdf = new jsPDF('l', 'mm', 'a4');
        const pdfWidth = pdf.internal.pageSize.getWidth();
        const pdfHeight = pdf.internal.pageSize.getHeight();

        // 4. Calculate dimensions
        const imgProps = pdf.getImageProperties(imgData);
        const ratio = imgProps.height / imgProps.width;
        const finalWidth = pdfWidth;
        const finalHeight = pdfWidth * ratio;

        // 5. Multi-page Logic
        let heightLeft = finalHeight;
        let position = 0;

        // Add the first page
        pdf.addImage(imgData, 'PNG', 0, position, finalWidth, finalHeight);
        heightLeft -= pdfHeight;

        // If there is more content, add new pages and offset the image
        while (heightLeft > 0) {
            position = heightLeft - finalHeight;
            pdf.addPage();
            pdf.addImage(imgData, 'PNG', 0, position, finalWidth, finalHeight);
            heightLeft -= pdfHeight;
        }

        pdf.save(`${scheduleName}_Schedule.pdf`);
        
        window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
            detail: { title: "Export Success", desc: "Full schedule saved.", color: "green" }
        }));
    };

    // Calls java delete endpoint to remove section
    async function dropSection(courseData) {
        const response = await fetch(`${import.meta.env.VITE_API_URL}/api/schedule/drop/${userId}/${year}/${term}/${scheduleName}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(courseData),
        });

        if (!response.ok) {
            window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
                detail: { title: "Error", desc: "Could not drop course", color: "red" }
            }));
            
            return;
        }
        window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
            detail: { title: "Course Dropped", desc: "Updated Schedule", color: "green" }
        }));
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
    

    return (
    <div className="schedule-container" ref={printRef}>
        {/* top section controls and text above calendar view */}
        <div className="schedule-top-section">

            {/* Navigate/Create New Schedule controls */}
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
                <div className="control-row" data-html2canvas-ignore>
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

            {/* Header Text */}
            <div className="header-section">
                <h2 className="schedule-header">Weekly View - {termMap[term]} {year}</h2>
    
            </div>
            
            
            {/* Schedule Status, including delete button, export, and total credits */}
            <div className="schedule-status-group">
                <div className="delete-export-buttons" data-html2canvas-ignore>
                    <button className="export-btn" onClick={exportPDF} title="Export schedule as PDF">
                        <img className="export-icon" src={exportIcon} alt="Export" />
                    </button>
        </div>

        <div className="schedule-semester-selector">
            <UpdateSemester
                year={year}
                setYear={setYear}
                term={term}
                setTerm={setTerm}
            />
        </div>

                    <button className="schedule-delete-btn" onClick={deleteSchedule} title="Delete current schedule">
                        <img className="trash-icon" src={trashCanIcon} alt="Delete" />
                    </button>
                </div>
            
                <p id="totalCredLabel">Total Credits: {totalCreds}</p>
            </div>
            
            

        </div>
        <div className="main-scroll-area">
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
        
        
            {/* courses without time slots */}
            {noTimeSections.length > 0 && (
                <div className="no-time-container">
                    <h3 className="no-time-header">Online / No Time Slot Courses</h3>
                    <div className="no-time-grid">
                        {noTimeSections.map((section, index) => (
                            <div key={index} className="no-time-card">
                                <div className="no-time-info">
                                    <span className="card-dept">{section.course.department.code} {section.course.number}</span>
                                    <p className="card-title">{section.course.title}</p>
                                </div>
                                <button className="action-button no-time-drop" onClick={() => dropSection(section)}>Drop</button>
                            </div>
                        ))}
                    </div>
                </div>
            )}

        </div>
    </div>
    );
}

export default WeeklySchedule;

