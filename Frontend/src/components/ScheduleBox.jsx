import '../css/scheduleView.css'
import React, { useState, useEffect } from 'react';

const ScheduleBox = () => {
    //state that holds the schedule from backend
    const [schedule, setSchedule] = useState(null);

    //state that tracks if you are waiting for response
    const [loading, setLoading] = useState(true);

    //FETCH SCHEDULE
    const fetchSchedule = async () => {
        try {
            //fetch for manual user 1
            const response = await fetch('http://localhost:8096/api/schedule/1');
            const data = await response.json();
            setSchedule(data);
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

    if (loading) return <div>Loading Schedule...</div>;

    return (
        <div className="schedule-box">
            <pre class="jsonSchedule">{JSON.stringify(schedule, null, 2)}</pre>
          
        </div>
    );
};

export default ScheduleBox;