import { useState, useEffect } from 'react';
import Course from '../components/Course'
import '../css/Home.css'

function Home() {
  return (
    <div>
      <div id='bgImage'>
        <div id='bgImageTint'></div>
      </div>
      <div id='content'>
        <h1 id='title'>GCC Course Search</h1>
        <div id="searchContainer">
          <input type='search' id="searchBar" placeholder='Search Courses...' onKeyDown={(e) => {OnHitEnter(e)}}></input>
          <button id="filterButton">☰</button>
        </div>
      </div>
      <div id='courseContainer'></div>
    </div>
  );
}

async function OnHitEnter(e) {
  if (e.key !== "Enter") return;
  console.log("Searching for courses...")
  
  // TODO:
  // Send search API call here to retrieve a list of courses
  const courseResults = [];

  // TODO:
  // Change CSS to fit in new course elements
  const titleText = document.getElementById("title");
  const content = document.getElementById("content");
  const courseContainer = document.getElementById("courseContainer");

  titleText.style.opacity = '0%';
  titleText.style.fontSize = '0vw';
  titleText.style.height = '50px';
  content.style.transform = 'translateY(calc(-50vh + 55px))';
  courseContainer.style.top = '120px';
  courseContainer.style.height = '1000px';
  await delay(500);

  

  // TODO:
  // Wait for course results, then create course HTML objects

}

const delay = ms => new Promise(res => setTimeout(res, ms));

export default Home;