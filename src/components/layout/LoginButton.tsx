"use client";
import { useEffect, useCallback, useState } from "react";

declare global {
    var google: any;
}

interface GoogleResponse {
    credential: string;
}

export default function LoginButton() {
    const [isMounted, setIsMounted] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    // 1. Definiera din funktion först
    const handleGoogleResponse = useCallback(async (response: GoogleResponse) => {
        const realToken = response.credential;

        try {
            const backendRes = await fetch("http://localhost:8082/api/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ idToken: realToken }),
            });

            if (backendRes.ok) {
                const data = await backendRes.json();
                localStorage.setItem("accessToken", data.accessToken);
                localStorage.setItem("refreshToken", data.refreshToken);
                setIsLoggedIn(true);
                window.location.reload();
            } else {
                console.error("Backenden nekade inloggningen. Status:", backendRes.status);
            }
        } catch (e) {
            console.error("Fetch error:", e);
        }
    }, []);

    // 2. useEffect kommer efter definitionen
    useEffect(() => {
        setIsMounted(true);
        setIsLoggedIn(!!localStorage.getItem("accessToken"));

        const script = document.createElement("script");
        script.src = "https://accounts.google.com/gsi/client";
        script.async = true;
        script.defer = true;
        document.body.appendChild(script);

        script.onload = () => {
            if (globalThis.google) {
                globalThis.google.accounts.id.initialize({
                    client_id: "210602676176-p68dp0mq87n7ts9gdup32k63nq3a5vuq.apps.googleusercontent.com",
                    callback: handleGoogleResponse
                });

                const btnElement = document.getElementById("googleBtn");
                if (btnElement) {
                    globalThis.google.accounts.id.renderButton(btnElement, {
                        theme: "outline",
                        size: "large"
                    });
                }
            }
        };

        return () => {
            if (document.body.contains(script)) {
                document.body.removeChild(script);
            }
        };
    }, [handleGoogleResponse]);

    const handleLogout = async () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        setIsLoggedIn(false);
        window.location.reload();
    };

    if (!isMounted) return null;

    if (isLoggedIn) {
        return (
            <button onClick={handleLogout} className="py-2 px-4 bg-red-600 text-white rounded-full">
                Logga ut
            </button>
        );
    }

    return <div id="googleBtn"></div>;
}