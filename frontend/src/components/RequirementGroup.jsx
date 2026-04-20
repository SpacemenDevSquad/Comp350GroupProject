import React, { useState } from 'react';
import Course from './Course';
import '../css/requirements.css';

function RequirementGroup({ groupData, completedCourses, onToggle }) {


    
    const getRuleText = () => {
        switch(groupData.ruleType) {
            case 'ALL_REQUIRED':
                return "All courses below are required.";
            case 'CREDIT_MIN_FROM_LIST':
                return `Choose at least ${groupData.minCredits} credits from this list.`;
            case 'CHOOSE_ONE_GROUP':
                return "Choose one of the required paths.";
            case 'CHOOSE_EXACTLY':
                return `Choose exactly ${groupData.amount} course(s) from this list.`;
            default:
                return "Complete the following requirements.";
        }
    };

    return (
        <div className="requirementGroupBlock">
            <div className="groupHeader">
                <h2 className="groupTitle">{groupData.title}</h2>
                <p className="groupRule">{getRuleText()}</p>
                {groupData.description && <p className="groupDesc">{groupData.description}</p>}
            </div>

            <div className="courseGrid">
                {/* Standard lists use groupData.courses. 
                  CHOOSE_ONE_GROUP uses groupData.options (an array of arrays).
                */}
                {groupData.courses ? (
                    groupData.courses.map((courseObj, index) => (
                        <Course 
                            key={index} 
                            code={courseObj.code} 
                            name={courseObj.name} 
                            credits={courseObj.credits}

                            isChecked={completedCourses.includes(courseObj.code)}
                            onToggle={(isChecked) => onToggle(courseObj.code, isChecked)}
                        />
                    ))
                ) : groupData.options ? (
                    <div className="optionsGrid">
                        {groupData.options.map((optionGroup, index) => (
                            <div key={index} className="optionPath">
                                <span className="pathLabel">Option {index + 1}:</span>
                                {optionGroup.map((courseObj, cIndex) => (
                                    <Course 
                                        key={cIndex} 
                                        code={courseObj.code} 
                                        name={courseObj.name} 
                                        credits={courseObj.credits} 

                                        isChecked={completedCourses.includes(courseObj.code)}
                                        onToggle={(isChecked) => onToggle(courseObj.code, isChecked)}
                                    />
                                ))}
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="groupDesc">Check catalog for complex requirements.</p>
                )}
            </div>
        </div>
    );
}

export default RequirementGroup;