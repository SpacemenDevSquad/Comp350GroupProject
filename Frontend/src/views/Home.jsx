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
          <input type='search' id="searchBar" placeholder='Search Courses...'></input>
          <button id="filterButton">☰</button>
        </div>
      </div>
    </div>
  );
}

export default Home;