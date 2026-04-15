import React from "react";
import '../css/Requirements.css'

function Course({ code, name, credits }){
    return (
        <div className="courseCard">
            <p className="courseCode">{code}</p>
            <p className="courseTitle">{name}</p>
            <p className="courseCredits">{credits} </p>
        </div>
    );
}

export default Course;