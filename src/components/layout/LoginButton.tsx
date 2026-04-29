"use client";
import { useEffect } from "react";


export default function LoginButton() {

    useEffect(() => {
        const initializeGoogle = () => {
            if (window.google) {
                window.google.accounts.id.initialize({
                    client_id: "210602676176-p68dp0mq87n7ts9gdup32k63nq3a5vuq.apps.googleusercontent.com",
                    callback: (res) => handleGoogleResponse(res)
                });

                window.google.accounts.id.renderButton(
                    document.getElementById("googleBtn"),
                    { theme: "outline", size: "large" }
                );
            }
        };

        // Om scriptet redan är laddat, kör direkt, annars vänta lite
        if (window.google) {
            initializeGoogle();
        } else {
            // En enkel fallback om scriptet tar tid
            const interval = setInterval(() => {
                if (window.google) {
                    initializeGoogle();
                    clearInterval(interval);
                }
            }, 100);
        }
    }, []);

    const handleGoogleResponse = async (response) => {
        const realToken = response.credential;

        // Nu gör du det riktiga anropet till ditt authservice-repo!
        const backendRes = await fetch("http://localhost:8082/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ idToken: realToken }),
        });

        if (backendRes.ok) {
            const data = await backendRes.json();
            console.log("Snyggt! Er backend skapade en session:", data);
        } else {
            console.error("Backenden nekade inloggningen");
        }
    };

    return <div id="googleBtn"></div>;
}