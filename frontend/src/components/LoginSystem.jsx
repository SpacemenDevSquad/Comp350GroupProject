import { useState } from "react";
import { auth } from "../js/firebase.js";
import { signInWithEmailAndPassword, createUserWithEmailAndPassword, updateProfile } from "firebase/auth";
import '../css/login.css';

function LoginModal({ isOpen, onClose, triggerAlert }) {
  const [isSignup, setIsSignup] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleAuth = async () => {
    if (isLoading) return;
    setIsLoading(true);
    try {
        let firebaseUser;
        if (isSignup) {
            const userCredential = await createUserWithEmailAndPassword(auth, email, password);
            await updateProfile(userCredential.user, { displayName: name });
            firebaseUser = userCredential.user;
        } else {
            const creds = await signInWithEmailAndPassword(auth, email, password);
            firebaseUser = creds.user;
        }

        // Sync with Java Backend
        await fetch(`${import.meta.env.VITE_API_URL}/api/user/sync`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            id: firebaseUser.uid,
            email: firebaseUser.email,
            name: firebaseUser.displayName || name
        })
        });

        triggerAlert(
            isSignup ? "Account Created" : "Welcome Back", 
            isSignup ? `Welcome, ${name}!` : "Successfully signed in.", 
            "green"
        );
        
        //cleanup
        onClose(); 
        setEmail("");
        setPassword("");
        setName("");
    } catch (error) {
        let errorMessage = error.message;
        if (error.code === 'auth/wrong-password') errorMessage = "Incorrect password. Please try again.";
        if (error.code === 'auth/user-not-found') errorMessage = "No account found with this email.";
        if (error.code === 'auth/invalid-email') errorMessage = "Please input a valid email.";
        if (error.code === 'auth/invalid-credential') errorMessage = "The password you entered is incorrect.";
        if (error.code === 'auth/weak-password') errorMessage = "Password must be at least 6 characters.";
        if (error.code === 'auth/email-already-in-use') errorMessage = "This email is already associated with an account.";
        if (error.code === 'auth/missing-password') errorMessage = "Must input a password that is at least 6 characters.";

        
        triggerAlert("Auth Error", errorMessage, "red");
    } finally {
        setIsLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="loginModel" onClick={(e) => e.target.className === 'loginModel' && onClose()}>
      <div
        className="loginContent"
        onClick={(e) => e.stopPropagation()}
        onKeyDown={(e) => {
          if (e.key === "Enter") {
            e.preventDefault();
            handleAuth();
          }
        }}
      >
        <h2 className="loginTitle">{isSignup ? "Create Account" : "Sign In"}</h2>
        
        {isSignup && (
          <input className="loginInput" type="text" placeholder="Full Name" value={name} onChange={(e) => setName(e.target.value)} disabled={isLoading} />
        )}
        <input className="loginInput" type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} disabled={isLoading} />
        <input className="loginInput" type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} disabled={isLoading} />

        <button className="loginBtn" onClick={handleAuth} disabled={isLoading}>
          {isLoading ? (
            <span className="loginLoadingWrap">
              <span className="loginSpinner" aria-hidden="true"></span>
              <span>{isSignup ? "Creating Account..." : "Signing In..."}</span>
            </span>
          ) : (
            isSignup ? "Register" : "Login"
          )}
        </button>

        <p className="toggleText">
          {isSignup ? "Already have an account? " : "Need an account? "}
          <span className="toggleLink" onClick={() => !isLoading && setIsSignup(!isSignup)}>{isSignup ? "Login" : "Sign up"}</span>
        </p>
        <button className="cancelBtn" onClick={onClose} disabled={isLoading}>Cancel</button>
      </div>
    </div>
  );
}

export default LoginModal; 
