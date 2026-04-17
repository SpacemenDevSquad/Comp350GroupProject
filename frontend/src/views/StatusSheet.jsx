import { useEffect, useState } from 'react';
import '../css/StatusSheet.css'
import { statusSheetTransition } from '../js/screenTransitions.js'
import RequirementGroup from '../components/RequirementGroup.jsx';

const MAJORS = [
    { id: "COMP_BS", name: "Computer Science (B.S.)" },
    { id: "MATH_BS", name: "Mathematics (B.S.)" },
    { id: "STAT_BS", name: "Applied Statistics (B.S.)" },
]

function StatusSheet(){
    const [selectedMajor, setSelectedMajor] = useState(() => {
        return localStorage.getItem("selectedMajor") || null;
    });
    const [pendingMajor, setPendingMajor] = useState('');
    const [degreeData, setDegreeData] = useState(null); 
    const [isLoading, setIsLoading] = useState(false);

    const [completedCourses, setCompletedCourses] = useState([]);
    const handleCourseToggle = (courseCode, isChecking) => {
        if (isChecking) {
            setCompletedCourses((prev) => [...prev, courseCode]);
        } else {
            setCompletedCourses((prev) => prev.filter((code) => code !== courseCode));
        }
    };

    // ==========================================
    // 2. GLOBAL MATH
    // ==========================================
    const totalCreditsEarned = degreeData?.requirementGroups?.reduce((acc, group) => {
        const groupSum = group.courses?.filter(c => completedCourses.includes(c.code))
                         .reduce((sum, c) => sum + c.credits, 0) || 0;
        return acc + groupSum;
    }, 0) || 0;

    useEffect(() => {
        if (!selectedMajor) return; 
        setDegreeData(null); // Clear previous data when switching majors
        const fetchStatusSheetData = async () => {
            setIsLoading(true);
            try{
                const response = await fetch(`http://localhost:8096/api/status-sheet/${selectedMajor}`);
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                const json = await response.json();
                setDegreeData(json);
            } catch (error) {
                console.error("Error fetching status sheet data:", error);
                setDegreeData(null); // Clear data on error to show error message in UI
            } finally {
                setIsLoading(false);
            }
        };

        fetchStatusSheetData();
    }, [selectedMajor]);

    const handleSelectMajor = () => {
        if (pendingMajor) {
            setSelectedMajor(pendingMajor);
            localStorage.setItem('selectedMajor', pendingMajor);
        }
    };
    return (
        <div id="statusSheetBlock">
            <button id="statusBackArrow" onClick={statusSheetTransition}></button>
            {/* THE TERNARY FLIP: If no major is selected, show the Empty State */}
            {!selectedMajor ? (
                <div id="majorSelectionOverlay">
                    <h2>Select Your Major</h2>
                    <select 
                        value={pendingMajor} 
                        onChange={(e) => setPendingMajor(e.target.value)}
                    >
                        <option value="" disabled>Choose a major...</option>
                        {MAJORS.map(m => (
                            <option key={m.id} value={m.id}>{m.name}</option>
                        ))}
                    </select>
                    
                    <button 
                        id="letsGoBtn" 
                        onClick={handleSelectMajor} 
                        disabled={!pendingMajor}
                    >
                        Let's Go
                    </button>
                </div>

            ) : (
                <div id="statusSheetContent">
                    <div id="statusSheetHeader">
                        <h1 id="scheduleTitle">Degree Requirements</h1>
                        
                        <select 
                            id="cornerDropdown"
                            value={selectedMajor} 
                            onChange={(e) => {
                                setDegreeData(null);
                                setSelectedMajor(e.target.value);
                                localStorage.setItem('selectedMajor', e.target.value);
                            }}
                        >
                            {MAJORS.map(m => (
                                <option key={m.id} value={m.id}>{m.name}</option>
                            ))}
                        </select>
                    </div>

                    {/* 3. GLOBAL PROGRESS BAR AREA */}
                    {degreeData && (
                        <div className="globalProgressContainer">
                            <div className="progressLabel">
                                <span>Overall Progress</span>
                                <span>{totalCreditsEarned} / {degreeData.totalCreditsRequired || 128} Credits</span>
                            </div>
                            <progress 
                                className="globalProgressBar"
                                value={totalCreditsEarned} 
                                max={degreeData.totalCreditsRequired || 128}
                            ></progress>
                        </div>
                    )}

                    {/* THIS IS THE UPDATED RENDER LOGIC */}
                    <div id="requirementsGrid">
                        {isLoading ? (
                            <p style={{color: 'white', marginTop: '20px'}}>
                                Contacting server for {selectedMajor} requirements...
                            </p>
                        ) : degreeData ? (
                            <div style={{ width: '100%', textAlign: 'left', marginTop: '20px' }}>
                                {degreeData.requirementGroups.map((group) => (
                                    <RequirementGroup 
                                        key={group.groupId} 
                                        groupData={group} 
                                        completedCourses={completedCourses}
                                        onToggle={handleCourseToggle}
                                    />
                                ))}
                            </div>
                        ) : (
                            <p style={{color: '#ffaaaa', marginTop: '20px'}}>
                                Failed to load data. Check console (F12) for errors.
                            </p>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}

export default StatusSheet;