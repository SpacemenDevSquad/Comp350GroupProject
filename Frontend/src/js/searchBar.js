export default async function OnHitEnter(e) {
  if (e.key !== "Enter") return;
  console.log("Searching for courses...")
  
  // TODO:
  // Send search API call here to retrieve a list of courses
  const courseResults = [];

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

  // TODO:
  // Wait for course results, then create course HTML objects

}

const delay = ms => new Promise(res => setTimeout(res, ms));