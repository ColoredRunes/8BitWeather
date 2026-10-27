import { db } from "./firebase.js";
import {
  doc,
  updateDoc,
  increment,
  arrayUnion
} from "https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js";

(function () {
  let leaving = false;

  function show() {
    if (!leaving) {
      document.getElementById("exitOverlay").classList.add("show");
    }
  }

  document.addEventListener("mouseleave", (e) => {
    if (e.clientY <= 0) show();
  });

  window.submitAndLeave = async function () {
    const reason =
      document.querySelector('input[name="reason"]:checked')?.value || "none";

    const comment =
      document.getElementById("extraComment").value.trim();

    console.log("Reason:", reason, "| Comment:", comment);

    try {
      await updateDoc(doc(db, "user-feedback", reason), {
        count: increment(1),
        usercomment: arrayUnion(`${reason}: ${comment}`)
      });

      justLeave();
    } catch (error) {
      console.error("Error saving feedback:", error);
    }
  };

  window.justLeave = function () {
    leaving = true;
    document.getElementById("exitOverlay").classList.remove("show");
  };
// git remote add origin https://github.com/ColoredRunes/8BitWeather

  document.body.insertAdjacentHTML(
    "beforeend",
    `
    <div class="overlay" id="exitOverlay">
      <div class="modal">
        <h2>Before you go...</h2>
        <p>I am a one person, small time indie dev... do you have any feedback? It would help me out a lot and is super easy!</p>
        <p>Why did you decide not to download?</p>

        <div class="options">
          <label><input type="radio" name="reason" value="unwanted"> Don't need it</label>
          <label><input type="radio" name="reason" value="dislike"> Don't like it</label>
          <label><input type="radio" name="reason" value="sketchy"> Looks sketchy</label>
          <label><input type="radio" name="reason" value="justlooking"> Just wanted to look</label>
          <label><input type="radio" name="reason" value="other"> Other</label>
        </div>

        <textarea
          id="extraComment"
          rows="3"
          placeholder="Any other thoughts? (optional)"
        ></textarea>

        <div class="btn-row">
          <button class="btn-submit" onclick="submitAndLeave()">
            Submit & Leave
          </button>
          <button class="btn-leave" onclick="justLeave()">
            Just Leave
          </button>
        </div>
      </div>
    </div>

    <style>
      .overlay {
        margin-left: 18%;
        text-align: left;
        display: none;
        position: fixed;
        inset: 0;
        background: rgba(0,0,0,0.6);
        z-index: 1000;
        align-items: center;
        justify-content: center;
      }

      .overlay.show {
        display: flex;
      }

      .modal {
        background: #000;
        border-radius: 12px;
        padding: 32px;
        max-width: 400px;
        width: 90%;
        font-family: sans-serif;
      }

      h2 {
        margin: 0 0 8px;
        font-size: 20px;
      }

      p {
        color: #555;
        margin: 0 0 20px;
        font-size: 14px;
      }

      .options label {
        display: block;
        margin-bottom: 10px;
        font-size: 14px;
        cursor: pointer;
      }

      textarea {
        width: 100%;
        margin-top: 12px;
        padding: 8px;
        border: 1px solid #015078;
        border-radius: 6px;
        font-size: 13px;
        resize: vertical;
        box-sizing: border-box;
      }

      .btn-row {
        display: flex;
        gap: 10px;
        margin-top: 20px;
      }

      .btn-submit {
        flex: 1;
        padding: 10px;
        background: #4f8ef7;
        color: #fff;
        border: none;
        border-radius: 8px;
        font-size: 14px;
        font-weight: 600;
        cursor: pointer;
      }

      .btn-submit:hover {
        background: #3a7be0;
      }

      .btn-leave {
        padding: 10px 16px;
        background: transparent;
        border: 1px solid #070505;
        border-radius: 8px;
        font-size: 14px;
        cursor: pointer;
        color: #888;
      }
    </style>
  `
  );
})();