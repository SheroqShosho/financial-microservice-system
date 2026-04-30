"use client";
import { useEffect, useCallback, useState } from "react";
import { useRouter } from "next/navigation";


interface GoogleResponse {
    credential: string;
}


declare global {
    interface Window {
        google: {
            accounts: {
                id: {
                    initialize: (config: { client_id: string; callback: (response: GoogleResponse) => void }) => void;
                    renderButton: (element: HTMLElement | null, options: { theme: string; size: string }) => void;
                };
            };
        };
    }
}


export default function LoginButton() {
    const [isLoggedIn, setIsLoggedIn] = useState(() => {
        if (typeof window !== "undefined") {
            return !!localStorage.getItem("accessToken");
        }
        return false;
    });


    const router = useRouter();


    const handleGoogleResponse = useCallback(async (response: GoogleResponse) => {
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
            // Save tokens to localStorage
            localStorage.setItem("accessToken", data.accessToken);
            localStorage.setItem("refreshToken", data.refreshToken);
            setIsLoggedIn(true);
        } else {
            console.error("Backenden nekade inloggningen");
        }
    }, []);


    useEffect(() => {
        const initializeGoogle = () => {
            if (globalThis.window.google) {
                globalThis.window.google.accounts.id.initialize({
                    client_id: "210602676176-p68dp0mq87n7ts9gdup32k63nq3a5vuq.apps.googleusercontent.com",
                    callback: handleGoogleResponse
                });


                globalThis.window.google.accounts.id.renderButton(
                    document.getElementById("googleBtn"),
                    { theme: "outline", size: "large" }
                );
            }
        };


        // Om scriptet redan är laddat, kör direkt, annars vänta lite
        if (globalThis.window.google) {
            initializeGoogle();
        } else {
            // En enkel fallback om scriptet tar tid
            const interval = setInterval(() => {
                if (globalThis.window.google) {
                    initializeGoogle();
                    clearInterval(interval);
                }
            }, 100);
        }
    }, [handleGoogleResponse, isLoggedIn]);


    const handleLogout = async () => {
        const refreshToken = localStorage.getItem("refreshToken");


        try {
            // Call backend logout endpoint
            const response = await fetch("http://localhost:8082/api/auth/logout", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ refreshToken }),
            });


            if (response.ok) {
                console.log("Sessionen avslutad!");
            } else {
                console.error("Logout misslyckades på backend");
            }
        } catch (error) {
            console.error("Logout error:", error);
        } finally {
            // Clear local state - this will trigger re-render and show login button
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            setIsLoggedIn(false);
        }
    };


    if (isLoggedIn) {
        return (
            <button
                onClick={handleLogout}
                className="py-2 px-4 bg-red-600 text-white rounded-full font-semibold hover:bg-red-700 transition-all"
            >
                Logga ut
            </button>
        );
    }


    return <div id="googleBtn"></div>;
}