"use client";

import { MyPagesDTO } from "@/types/user";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function LoansDetailPage() {
    const [data, setData] = useState<MyPagesDTO | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const token = localStorage.getItem("accessToken");

                if (!token) {
                    console.log("No access token found");
                    setIsLoading(false);
                    return;
                }

                const res = await fetch("http://localhost:8080/mypages", {
                    method: "GET",
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                });

                if (!res.ok) {
                    console.error("Failed to fetch user data:", res.status);
                    setIsLoading(false);
                    return;
                }

                const backendData = await res.json();

                setData({
                    username: backendData.profile.userId,
                    firstName: backendData.profile.firstName,
                    lastName: backendData.profile.lastName,
                    socialSecurityNumber: backendData.profile.socialSecurityNumber,
                    address: backendData.profile.address,
                    city: backendData.profile.city,
                    zipCode: backendData.profile.zipCode,
                    country: backendData.profile.country,
                    creditCards: backendData.creditCards ?? [],
                    loans: backendData.loans ?? []
                });
            } catch (e) {
                console.error("Error fetching user data:", e);
            } finally {
                setIsLoading(false);
            }
        };

        fetchUserData();
    }, []);

    if (isLoading) {
        return <div className="p-10 text-center">Laddar...</div>;
    }

    if (!data) {
        return <div className="p-10">Kunde inte hämta data. Logga in igen.</div>;
    }

    return (
        <main className="p-4 md:p-8 bg-gray-50 min-h-screen text-gray-800">
            <div className="w-full max-w-6xl mx-auto">
                <header className="mb-6">
                    <h1 className="text-2xl font-bold">Mina Lån - Detaljer</h1>
                    <div className="flex gap-4 mt-3">
                        <Link href="/mypages" className="text-blue-600 hover:underline">Tillbaka till Mina Sidor</Link>
                        <Link href="/mypages/loans/apply" className="text-green-600 hover:underline font-bold">+ Ansök om Lån</Link>
                    </div>
                </header>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {data.loans.map((loan) => (
                        <div key={loan.loanId} className="bg-white p-5 rounded-xl border-2 border-black shadow-sm">
                            <h3 className="text-lg font-bold mb-3">{loan.loanType}</h3>
                            <div className="space-y-2 text-sm">
                                <p><strong>Lån-ID:</strong> {loan.loanId}</p>
                                <p><strong>Status:</strong> {loan.loanStatus}</p>
                                <p><strong>Lånesumma:</strong> {loan.amount} kr</p>
                                <p><strong>Ränta:</strong> {loan.interestRate}%</p>
                                <p><strong>Löptid:</strong> {loan.durationMonths} månader</p>
                                <p><strong>Månadlig betalning:</strong> {((loan.amount * (loan.interestRate / 100))/ 12).toLocaleString('sv-SE', {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2
                                })} kr</p>
                            </div>
                        </div>
                    ))}
                </div>

                {data.loans.length === 0 && (
                    <div className="bg-white p-8 rounded-lg border text-center shadow-sm">
                        <p className="text-gray-500 italic">Inga lån att visa.</p>
                    </div>
                )}
            </div>
        </main>
    );
}
