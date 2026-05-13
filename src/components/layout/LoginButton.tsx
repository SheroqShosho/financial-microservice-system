"use client";
import { useEffect, useCallback, useState } from "react";

declare global {
    interface Window {
        google?: {
            accounts: {
                id: {
                    initialize: (config: Record<string, unknown>) => void;
                    renderButton: (element: HTMLElement, options: Record<string, unknown>) => void;
                };
            };
        };
    }
}

interface GoogleResponse {
    credential: string;
}

export default function LoginButton() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [googleReady, setGoogleReady] = useState(false);

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

    // Sätt inloggningsstatus direkt utan att vänta på mount
    useEffect(() => {
        setIsLoggedIn(!!localStorage.getItem("accessToken"));
    }, []);

    // Ladda Google-scriptet separat, bara när vi inte är inloggade
    useEffect(() => {
        if (isLoggedIn) return;

        const initGoogle = () => {
            if (!window.google) return;
            window.google.accounts.id.initialize({
                client_id: "210602676176-p68dp0mq87n7ts9gdup32k63nq3a5vuq.apps.googleusercontent.com",
                callback: handleGoogleResponse,
            });
            const btnElement = document.getElementById("googleBtn");
            if (btnElement) {
                window.google.accounts.id.renderButton(btnElement, {
                    theme: "outline",
                    size: "large",
                });
            }
            setGoogleReady(true);
        };

        if (window.google) {
            initGoogle();
            return;
        }

        const existing = document.querySelector('script[src*="accounts.google.com/gsi/client"]');
        if (existing) {
            const interval = setInterval(() => {
                if (window.google) {
                    initGoogle();
                    clearInterval(interval);
                }
            }, 100);
            return () => clearInterval(interval);
        }

        const script = document.createElement("script");
        script.src = "https://accounts.google.com/gsi/client";
        script.async = true;
        script.defer = true;
        script.onload = initGoogle;
        document.body.appendChild(script);

        return () => {
            if (document.body.contains(script)) {
                document.body.removeChild(script);
            }
        };
    }, [isLoggedIn, handleGoogleResponse]);

    const handleLogout = async () => {
        const refreshToken = localStorage.getItem("refreshToken");
        try {
            await fetch("http://localhost:8082/api/auth/logout", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ refreshToken }),
            });
        } catch (e) {
            console.error("Logout error:", e);
        } finally {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            setIsLoggedIn(false);
            window.location.reload();
        }
    };

    if (isLoggedIn) {
        return (
            <button
                onClick={handleLogout}
                className="py-2 px-4 bg-white text-[#003349] rounded-full font-semibold hover:bg-gray-100 transition-all"
            >
                Logga ut
            </button>
        );
    }

    // Visa alltid en placeholder-div så knappen har plats att renderas in
    return <div id="googleBtn" style={{ minWidth: "120px", minHeight: "40px" }} />;
}