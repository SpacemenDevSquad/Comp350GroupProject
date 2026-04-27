import { useEffect, useState } from 'react';
import '../css/section.css'
import { createAlert } from '../js/createAlert.jsx';
import ProfessorRatings from './ProfessorRatings.jsx';
import StarRating from './StarRating.jsx';

const ProfessorModal = ({ name, qualityRating, numRatings, wouldTakeAgain, difficulty, rmpId, onClose }) => {
  return (
    // The backdrop catches clicks outside the modal to close it
    <div className="modal-backdrop" onClick={onClose} style={backdropStyle}>
      {/* Stop propagation so clicking inside the modal doesn't close it */}
      <div className="modal-content" onClick={(e) => e.stopPropagation()} style={modalStyle}>
        <h2>Professor {name}</h2>
        <div style={{"height": "unset"}}>
        {rmpId !== 0 && rmpId !== "Null" && (
          <>
            <h3>
              <a
                href={`https://www.ratemyprofessors.com/professor/${rmpId}`} target='_blank' rel='noopener noreferrer'
                style={{ color: '#82b8fe', textDecoration: 'none' }}>
                RateMyProfessors Entry 🛈
              </a>
            </h3>
            <ul>
              <li>Quality: {qualityRating}/5 ({numRatings} ratings)</li>
              <li>{wouldTakeAgain*100}% would take again</li>
              <li>Difficulty: {difficulty}/5</li>
            </ul>
          </>
        )}
        <h3>Student Ratings</h3>
        {/* here we will fetch the average professor rating from the /api/ratings/professor/{professorName} endpoint and compute an average */}
        <ProfessorRatings professorName={name} />
        </div>
        
        <button onClick={onClose} className='closeProfessorModal'>Close</button>
      </div>
    </div>
  );
};

const CourseRatingModal = ({ userId, deptCode, courseNum, profName, onClose }) => {
  const [difficulty, setDifficulty] = useState(0);
  const [quality, setQuality] = useState(0);
  const [review, setReview] = useState('');
  const [loadingExisting, setLoadingExisting] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    let isMounted = true;

    async function fetchExistingRating() {
      if (!userId) {
        setLoadingExisting(false);
        return;
      }

      try {
        const response = await fetch(
          `${import.meta.env.VITE_API_URL}/api/ratings/user/${encodeURIComponent(userId)}/course/${encodeURIComponent(deptCode)}/${encodeURIComponent(courseNum)}/professor/${encodeURIComponent(profName)}`
        );

        if (response.ok) {
          const existing = await response.json();
          if (isMounted) {
            setDifficulty(existing.difficulty || 0);
            setQuality(existing.quality || 0);
            setReview(existing.review || '');
          }
        } else if (response.status !== 404) {
          throw new Error(`Could not load existing rating (${response.status})`);
        }
      } catch (error) {
        createAlert(error.message, 'Rating Load Error', 'red');
      } finally {
        if (isMounted) {
          setLoadingExisting(false);
        }
      }
    }

    fetchExistingRating();

    return () => {
      isMounted = false;
    };
  }, [userId, deptCode, courseNum, profName]);

  async function submitRating() {
    if (!userId) {
      window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
        detail: {
          title: 'Login Required',
          desc: 'Please sign in to rate this course.',
          color: 'red'
        }
      }));
      window.dispatchEvent(new CustomEvent('showLogin'));
      return;
    }

    if (difficulty < 1 || quality < 1) {
      createAlert('Please select both difficulty and quality ratings.', 'Missing Rating', 'red');
      return;
    }

    setSaving(true);
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/api/rating`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          user: { id: userId },
          course: {
            department: { code: deptCode },
            number: Number(courseNum)
          },
          professor: { name: profName },
          difficulty,
          quality,
          review
        })
      });

      if (!response.ok) {
        const msg = await response.text();
        throw new Error(msg || `Failed to save rating (${response.status})`);
      }

      createAlert('Your rating was saved.', 'Rating Submitted', 'green');
      onClose();
    } catch (error) {
      createAlert(error.message, 'Rating Error', 'red');
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="modal-backdrop" onClick={onClose} style={backdropStyle}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()} style={modalStyle}>
        <h2>Rate {deptCode} {courseNum}</h2>
        <p className="ratingMetaText">Professor: {profName}</p>

        {loadingExisting ? (
          <p>Loading your existing rating...</p>
        ) : (
          <>
            <div className="ratingInputGroup">
              <p className="ratingLabel">Difficulty (1-3)</p>
              <StarRating rating={difficulty} maxStars={3} interactive={true} onRate={setDifficulty} size={28} />
            </div>

            <div className="ratingInputGroup">
              <p className="ratingLabel">Quality (1-3)</p>
              <StarRating rating={quality} maxStars={3} interactive={true} onRate={setQuality} size={28} />
            </div>

            <div className="ratingInputGroup">
              <p className="ratingLabel">Review</p>
              <textarea
                className="ratingReviewInput"
                value={review}
                onChange={(e) => setReview(e.target.value)}
                placeholder="Share your experience..."
                rows={4}
              />
            </div>
          </>
        )}

        <div className="ratingModalActions">
          <button onClick={submitRating} className="closeProfessorModal" disabled={loadingExisting || saving}>
            {saving ? 'Saving...' : 'Submit'}
          </button>
          <button onClick={onClose} className="closeProfessorModal">Close</button>
        </div>
      </div>
    </div>
  );
};

function Section({ data, year, term, userId, scheduleName, openProfessor, setOpenProfessor }) {
  const [showCourseRatingModal, setShowCourseRatingModal] = useState(false);

  // Creates a unique identifier for this professor to compare with openProfessor
  const professorId = `${data.faculty[0]?.id || 'null'}-${data.course?.id || 'null'}-${data.sectionLetter || 'null'}`;

  // Gets the title of the course and formats it with proper capitalization
  let rawTitle = data.course?.title || "Null";
  let splitTitle = rawTitle.split(" ");
  let joinedTitle = ""
  for (let i = 0; i < splitTitle.length; i++) {
    joinedTitle += splitTitle[i][0].toString().toUpperCase() + splitTitle[i].substring(1).toUpperCase()+" ";
  }

  // Timeslots
  let rawTimes = data.timeslots || "Null";
  let joinedTimes;
  if (rawTimes !== "Null") {
    joinedTimes = [];

    // Go through each timeslot for the class
    for (let i = 0; i < rawTimes.length; i++) {

      // Get the raw data
      let startTime = rawTimes[i].startTime;
      let endTime = rawTimes[i].endTime;
      let day = rawTimes[i].day;

      // Calculate if the class time is AM or PM
      let startTimeOfDay = "am";
      let endTimeOfDay = "am";
      if (Math.floor((startTime/60)/12) == 1) startTimeOfDay = "pm";
      if (Math.floor((endTime/60)/12) == 1) endTimeOfDay = "pm";

      // Get the starting time of the class as a string
      let startString = ((Math.floor(startTime/60))%12 == 0 ? 12 : (Math.floor(startTime/60))%12) + ":" + ((startTime%60) > 10 ? (startTime%60).toString() : "0"+(startTime%60).toString());

      // Get the ending time of the class as a string
      let endString = ((Math.floor(endTime/60))%12 == 0 ? 12 : (Math.floor(endTime/60))%12) + ":" + ((endTime%60) > 10 ? (endTime%60).toString() : "0"+(endTime%60).toString());

      // Detects if a class shares the same time between multiple days. For example, MWF at 9:00-9:50am all share the same time
      let existed = false;

      for (let j = 0; j < Math.floor(joinedTimes.length/2); j++) {
        if (joinedTimes[j*2+1] == startString+startTimeOfDay+" - "+endString+endTimeOfDay) {
          existed = true;
          joinedTimes[j*2] += day;
          break;
        }
      }

      // If the timeslot doesn't already 
      if (!existed) {
        joinedTimes.push(day);
        joinedTimes.push(startString+startTimeOfDay+" - "+endString+endTimeOfDay)
      }

    }
  } else {
    joinedTimes = "Null";
  }


  //ADD/DROP LOGIC
  async function addSection(force=false) {
    // check if user is logged in before doing anything
    if (!userId) {
        window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
            detail: {
                title: "Login Required",
                desc: "Please sign in to add courses to a schedule.",
                color: "red"
            }
        }));
        //open login modal automatically
        window.dispatchEvent(new CustomEvent('showLogin')); 
        return;
    }

    //make api call
    const response = await fetch(`${import.meta.env.VITE_API_URL}/api/schedule/add/${userId}/${year}/${term}/${scheduleName}?force=${force}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })

    //checks for conflict
    if(!response.ok){
      const msg = await response.text();
      
      //credit limit popup
      if(response.status==403 && msg === "CREDIT_LIMIT"){
        const confirmForce = window.confirm("Warning: This puts you over 18 credits. Force add anyway?");
        if (confirmForce) {
          addSection(true);
        }
        return;
      }
      
      createAlert(msg, "Error: "+response.status, "red");
      return;
    }

    createAlert("Added Course", "Check schedule for details", "green");
    window.dispatchEvent(new CustomEvent('scheduleRefresh'));
  }

  async function dropSection() {

    // check if user is logged in before doing anything
    if (!userId) {
        window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
            detail: {
                title: "Login Required",
                desc: "Please sign in to add courses to a schedule.",
                color: "red"
            }
        }));
        //open login modal automatically
        window.dispatchEvent(new CustomEvent('showLogin')); 
        return;
    }

    
    //make api call
    const response = await fetch(`${import.meta.env.VITE_API_URL}/api/schedule/drop/${userId}/${year}/${term}/${scheduleName}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    })

    //checks if not in schedule
    if(!response.ok){
      createAlert("Failed to drop", "Could not find in schedule", "red");
      return;
    }
    createAlert("Course Dropped", "Check schedule for details", "green");
    window.dispatchEvent(new CustomEvent('scheduleRefresh'));
  }

  // Safely grab the details from the Java Section object you passed in
  const deptCode = data.course?.department?.code || data.course?.department?.id || "N/A";
  const courseNum = data.course?.number || "000";
  const sectionLetter = data.sectionLetter || "?";
  const credits = data.course?.credits || "Null";
  const title = joinedTitle;
  const timeSlots = Array.isArray(joinedTimes) ? joinedTimes.join(" ") : "No Time Listed";
  const description = data.course?.description || "Null";
  const sectionTerm = data.semester?.term || "Null";
  const sectionYear = data.semester?.year || "Null";
  const profName = data.faculty[0]?.name || "Null";
  const profQualityRating = data.faculty[0]?.quality_rating || "Null";
  const profNumRatings = data.faculty[0]?.rating_count || "Null";
  const profWouldTakeAgain = data.faculty[0]?.would_take_again || "Null";
  const profDifficulty = data.faculty[0]?.difficulty || "Null";
  const profRMPId = data.faculty[0]?.id || "Null";

  function openCourseRatingModal() {
    if (!userId) {
      window.dispatchEvent(new CustomEvent('triggerCustomAlert', {
        detail: {
          title: 'Login Required',
          desc: 'Please sign in to rate this course/professor.',
          color: 'red'
        }
      }));
      window.dispatchEvent(new CustomEvent('showLogin'));
      return;
    }

    if (!profName || profName === 'Null') {
      createAlert('No professor is attached to this section yet.', 'Rating Unavailable', 'red');
      return;
    }

    setShowCourseRatingModal(true);
  }

  return (
    <div className="sectionCard">
      <p className="sectionTitle">{title}</p>
      <p className="sectionDeptInfo">
        <span
          className="courseName"
          onClick={openCourseRatingModal}
          onKeyDown={(e) => {
            if (e.key === 'Enter' || e.key === ' ') {
              e.preventDefault();
              openCourseRatingModal();
            }
          }}
          role="button"
          tabIndex={0}
        >
          {deptCode} {courseNum}
        </span> - Section {sectionLetter}
      </p>
      <p className="sectionProf">
        Professor:{' '}
        {/* Note: It's better for accessibility to use a button styled as text than a span */}
        <button
          onClick={() => setOpenProfessor({
            id: professorId,
            name: profName,
            qualityRating: profQualityRating,
            numRatings: profNumRatings,
            wouldTakeAgain: profWouldTakeAgain,
            difficulty: profDifficulty,
            rmpId: profRMPId
          })}
          style={{ background: 'none', border: 'none', color: 'inherit', textDecoration: 'none', cursor: 'pointer', padding: 0, fontFamily: 'inherit', fontSize: 'inherit', 'text-shadow': '2px 2px black'}}
        >
          {profName} 🛈
        </button>
      </p>
      <p className="sectionTerm">{"Semester: "+sectionYear+" "+sectionTerm}</p>
      <p className="sectionTime">{timeSlots}</p>
      <p className="sectionCreds">Credits: {credits}</p>
      <button onClick={addSection} className="addButton">Add</button>
      <button onClick={dropSection} className="dropButton">Drop</button>

      {openProfessor && openProfessor.id === professorId && (
        <ProfessorModal 
          name={openProfessor.name}
          qualityRating={openProfessor.qualityRating}
          numRatings={openProfessor.numRatings}
          wouldTakeAgain={openProfessor.wouldTakeAgain}
          difficulty={openProfessor.difficulty}
          rmpId={openProfessor.rmpId}
          onClose={() => setOpenProfessor(null)} 
        />
      )}

      {showCourseRatingModal && (
        <CourseRatingModal
          userId={userId}
          deptCode={deptCode}
          courseNum={courseNum}
          profName={profName}
          onClose={() => setShowCourseRatingModal(false)}
        />
      )}
    </div>
  );
}

export default Section;

// Quick inline styles just for the example to work visually
const backdropStyle = {
  position: 'fixed', top: 0, left: 0, width: '100%', height: '100%',
  display: 'flex', 
  justifyContent: 'center', alignItems: 'center', zIndex: 1000,
  backgroundColor: 'rgba(0, 0, 0, 0.5)',
  overflowY: 'auto'
};

const modalStyle = {
  backgroundColor: 'rgb(160,0,0)', padding: '20px', borderRadius: '8px', 
  minWidth: '300px', maxWidth: '50vw', boxShadow: '0 4px 6px rgba(0,0,0,0.1)', border: '4px solid rgb(255, 255, 255)',
  'text-shadow': '2px 2px black',
  height: 'fit-content', maxHeight: '90vh'
};
