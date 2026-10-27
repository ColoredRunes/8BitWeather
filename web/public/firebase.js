import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.2/firebase-app.js";
import { getFirestore } from "https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js";

const firebaseConfig = {
  apiKey: "AIzaSyC279sUQnEBuUZto3CxV1_7rXwiUgtE2i8",
  authDomain: "bit-weather-b7bb3.firebaseapp.com",
  projectId: "bit-weather-b7bb3",
  storageBucket: "bit-weather-b7bb3.firebasestorage.app",
  messagingSenderId: "267855985339",
  appId: "1:267855985339:web:bfd39cafacaacab0d8c79e"
};

export const app = initializeApp(firebaseConfig);
export const db = getFirestore(app);