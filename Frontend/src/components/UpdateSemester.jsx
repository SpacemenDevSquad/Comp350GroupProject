import { useState } from 'react'

function UpdateSemester({ year, setYear, term, setTerm }){
  return (
    <div>
      Update Semester
      <select name="semester_year_selector" id="semester_year_selector" onChange={(e) => setYear(parseInt(e.target.value))} defaultValue={"2023"}>
        <option value="2023">2023</option>
        <option value="2024">2024</option>
        <option value="2025">2025</option>
        <option value="2026">2026</option>
        <option value="2027">2027</option>
      </select>
      <select name="semester_term_selector" id="semester_term_selector" onChange={(e) => setTerm(e.target.value)} defaultValue={"F"}>
        <option value="F">Fall</option>
        <option value="S">Spring</option>
      </select>
      {year + term}
    </div>
  )
}

export default UpdateSemester