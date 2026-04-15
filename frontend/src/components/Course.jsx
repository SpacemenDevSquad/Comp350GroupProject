import React from "react";
import '../css/Requirements.css'

function Course({ code, name, credits, isChecked, onToggle }) {
    return (
        <div className="courseCard">
            <p className="courseCode">{code}</p>
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