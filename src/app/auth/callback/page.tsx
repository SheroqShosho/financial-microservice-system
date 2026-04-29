"use client";
import { useEffect, Suspense } from "react";
import { useRouter, useSearchParams } from "next/navigation";

function AuthCallbackContent() {
    const searchParams = useSearchParams();
    const router = useRouter();

    useEffect(() => {
        // 1. Hämta token som din Java-backend skickade med i URL:en
        const token = searchParams.get("token");

        if (token) {
            // 2. Spara token i en cookie.
            // Detta gör att både frontend och backend kan läsa den framöver.
            document.cookie = `token=${token}; path=/; max-age=3600; SameSite=Lax`;

            // 3. Skicka användaren till startsidan
            router.push("/");
        } else {
            console.error("Ingen token hittades i URL:en");
            router.push("/?error=auth_failed");
        }
    }, [searchParams, router]);

    return (
        <div className="flex h-screen items-center justify-center bg-gray-50">
            <div className="text-center">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
                <h2 className="mt-4 text-xl font-semibold">Loggar in dig...</h2>
                <p className="text-gray-500">Vänligen vänta medan vi verifierar dina uppgifter.</p>
            </div>
        </div>
    );
}

export default function AuthCallback() {
    return (
        <Suspense fallback={<div>Laddar...</div>}>
            <AuthCallbackContent />
        </Suspense>
    );
}