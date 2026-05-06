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

    if (isLoading) return <div className="p-20 text-center font-bold tracking-widest uppercase text-xs text-slate-400">Laddar lån...</div>;

    if (!data || data.loans.length === 0) return (
        <div className="min-h-screen flex flex-col items-center justify-center p-20 text-center">
            <p className="mb-6 font-light text-slate-500">Du har inga aktiva lån för tillfället.</p>
            <Link href="/mypages" className="text-red-600 font-black uppercase text-[10px] tracking-widest border-b-2 border-red-600 pb-1 hover:opacity-70 transition-opacity">
                Tillbaka till Mina Sidor
            </Link>
        </div>
    );

    return (
        <main className="min-h-screen bg-gray-50/30 pb-20 flex flex-col items-center">
            {/* Header */}
            <header className="w-full bg-[#003349] px-8 pt-32 pb-16 mb-12 flex justify-center border-b border-white/5 shadow-lg">
                <div className="w-full max-w-4xl">
                    <h1 className="text-4xl md:text-5xl font-black tracking-tight text-white mb-4 uppercase">Mina Lån</h1>
                    <Link href="/mypages" className="text-[10px] font-black uppercase tracking-[0.2em] text-red-500 flex items-center gap-2 hover:opacity-70 transition-opacity">
                        <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="3"><path d="M15 19l-7-7 7-7" /></svg>
                        Tillbaka till Mina Sidor
                    </Link>
                </div>
            </header>

            {/* Lånelista */}
            <div className="w-full max-w-4xl px-8 flex flex-col gap-10">
                {data.loans.map((loan) => (
                    <div key={loan.loanId} className="bg-white rounded-[40px] p-10 shadow-sm border border-gray-100 flex flex-col gap-8 transition-all hover:shadow-md">

                        {/* Övre del: Typ och Status */}
                        <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
                            <div>
                                <h2 className="text-3xl font-black text-gray-900 uppercase tracking-tight">{loan.loanType}</h2>
                                <p className="text-[11px] font-mono text-gray-400 mt-1 tracking-wider uppercase">Låne-ID: {loan.loanId}</p>
                            </div>
                            <span className="text-[10px] font-black uppercase px-4 py-2 rounded-xl bg-green-50 text-green-600 border border-green-100 tracking-widest">
                                Aktiv skuld
                            </span>
                        </div>

                        {/* Rutnät med detaljer */}
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-10 pt-4">
                            {/* Nuvarande skuld */}
                            <div className="flex flex-col border-l-4 border-[#003349] pl-6">
                                <span className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-1">Nuvarande skuld</span>
                                <span className="text-3xl font-black text-gray-900">{loan.amount.toLocaleString('sv-SE')} kr</span>
                            </div>

                            {/* Ränta */}
                            <div className="flex flex-col border-l-4 border-gray-100 pl-6">
                                <span className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-1">Årsränta</span>
                                <span className="text-2xl font-black text-gray-800">{loan.interestRate}%</span>
                            </div>

                            {/* Lånetyp/Kategori */}
                            <div className="flex flex-col border-l-4 border-gray-100 pl-6">
                                <span className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-1">Kategori</span>
                                <span className="text-lg font-bold text-gray-700 uppercase tracking-tight">{loan.loanType}</span>
                            </div>
                        </div>

                    </div>
                ))}
            </div>
        </main>
    );
}