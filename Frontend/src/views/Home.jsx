import { useState, useEffect } from 'react';
import Course from '../components/Course'

function Home() {
  return (
    <div>
      <Course department="HUMA" number="202" />
    </div>
  );
}

export default Home;