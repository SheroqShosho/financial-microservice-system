"use client";

import { useRouter } from "next/navigation";
import { ReactNode } from "react";

interface ApplyButtonProps {
    href: string;
    children: ReactNode;
    className?: string;
}

export default function ApplyButton({ href, children, className = "" }: ApplyButtonProps) {
    const router = useRouter();

    const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        const token = localStorage.getItem("accessToken");

        if (!token) {
            alert("Du måste vara inloggad för att ansöka. Vänligen logga in först.");
            return;
        }

        router.push(href);
    };

    return (
        <button
            onClick={handleClick}
            className={className}
        >
            {children}
        </button>
    );
}

