// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyBvqkuKmDMJEE3rCQc_G5y-5JpRutBnYho",
  authDomain: "prijproject.firebaseapp.com",
  projectId: "prijproject",
  storageBucket: "prijproject.firebasestorage.app",
  messagingSenderId: "320155719997",
  appId: "1:320155719997:web:7bc43c7212fba092c24d54",
  measurementId: "G-JT1RS8V4LL"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

export { app, auth };