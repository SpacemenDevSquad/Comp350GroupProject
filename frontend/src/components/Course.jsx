import React from "react";
import '../css/requirements.css'

function Course({ code, name, credits, isChecked, onToggle, onCourseClick }) {
    return (
        <div className={`courseCard ${isChecked ? 'completed' : ''}`}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <p className="courseCode" style={{ margin: 0 }}>{code}</p>
                <button 
                    className="courseSearchIcon" 
                    onClick={() => onCourseClick(code)}
                    title={`Search for ${code}`}
                ></button>
            </div>

            <p className="courseTitle">{name}</p>
            <div className="courseFooter">
                <p className="courseCredits">{credits} Credits</p>
                <label className="checkboxLabel">
                    Completed
                    <input 
                        type="checkbox"
                        checked={isChecked || false}
                        onChange={(e) => onToggle(e.target.checked)}
                    />
                </label>
            </div>
        </div>
    );
}

export default Course;