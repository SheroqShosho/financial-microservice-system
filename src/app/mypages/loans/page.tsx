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

    if (isLoading) return <div className="p-20 text-center font-bold tracking-widest uppercase text-xs">Laddar lån...</div>;
    if (!data || data.loans.length === 0) return (
        <div className="p-20 text-center">
            <p className="mb-4">Du har inga aktiva lån.</p>
            <Link href="/mypages" className="text-red-600 font-bold uppercase text-xs tracking-widest">Tillbaka</Link>
        </div>
    );

    return (
        <main className="min-h-screen bg-gray-50/30 pb-20 flex flex-col items-center">
            {/* Header */}
            <header className="w-full bg-white border-b border-gray-100 px-8 py-12 mb-12 flex justify-center">
                <div className="w-full max-w-4xl">
                    <Link href="/mypages" className="text-[10px] font-black uppercase tracking-[0.2em] text-red-600 flex items-center gap-2 mb-4 hover:opacity-70 transition-opacity">
                        <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="3"><path d="M15 19l-7-7 7-7" /></svg>
                        Tillbaka till Mina Sidor
                    </Link>
                    <h1 className="text-4xl font-black tracking-tight text-gray-900">Mina Lån</h1>
                </div>
            </header>

            {/* Lånelista */}
            <div className="w-full max-w-4xl px-8 flex flex-col gap-8">
                {data.loans.map((loan) => (
                    <div key={loan.loanId} className="bg-white rounded-[40px] p-8 shadow-sm border border-gray-100 transition-all hover:shadow-md">
                        <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
                            <div>
                                <h2 className="text-2xl font-black text-gray-900 uppercase tracking-tight">{loan.loanType}</h2>
                                <p className="text-[10px] font-mono text-gray-400 mt-1">ID: {loan.loanId}</p>
                            </div>
                            <span className="text-[10px] font-black uppercase px-4 py-1.5 rounded-full bg-blue-50 text-blue-600 border border-blue-100">
                                Aktiv skuld
                            </span>
                        </div>

                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-y-8 gap-x-12">
                            {/* Huvudbelopp */}
                            <div className="flex flex-col">
                                <span className="text-[11px] font-black uppercase tracking-[0.15em] text-gray-400 mb-2">Nuvarande skuld</span>
                                <span className="text-3xl font-black text-gray-900">{loan.amount.toLocaleString('sv-SE')} kr</span>
                            </div>

                            {/* Ränta */}
                            <div className="flex flex-col">
                                <span className="text-[11px] font-black uppercase tracking-[0.15em] text-gray-400 mb-2">Ränta</span>
                                <span className="text-xl font-bold text-gray-800">{loan.interestRate}%</span>
                            </div>

                            {/* Status eller annat fält (använder befintliga attribut) */}
                            <div className="flex flex-col">
                                <span className="text-[11px] font-black uppercase tracking-[0.15em] text-gray-400 mb-2">Typ</span>
                                <span className="text-sm font-bold text-gray-700 uppercase">{loan.loanType}</span>
                            </div>
                        </div>


                    </div>
                ))}
            </div>
        </main>
    );
}