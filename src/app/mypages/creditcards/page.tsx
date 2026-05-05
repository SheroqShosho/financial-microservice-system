"use client";

import { MyPagesDTO } from "@/types/user";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function CreditCardsDetailPage() {
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

    if (isLoading) return <div className="p-20 text-center font-bold tracking-widest uppercase text-xs">Laddar...</div>;
    if (!data) return <div className="p-20 text-center text-sm">Kunde inte hämta data.</div>;

    const getCardStyle = (type: string) => {
        const t = type.toLowerCase();

        if (t.includes("standard")) {
            return "bg-zinc-900 text-white shadow-zinc-950/30";
        }

        if (t.includes("platinum")) {
            return "bg-gradient-to-br from-[#37475a] to-[#2c3e50] text-white shadow-slate-900/30";
        }

        if (t.includes("gold")) {
            return "bg-gradient-to-br from-amber-400 via-amber-600 to-amber-700 text-white shadow-amber-900/20";
        }

        return "bg-gray-200 text-gray-800";
    };

    return (
        <main className="min-h-screen bg-gray-50/30 pb-20 flex flex-col items-center">
            {/* Header */}
            <header className="w-full bg-white border-b border-gray-100 px-8 py-12 mb-12 flex justify-center">
                <div className="w-full max-w-4xl">
                    <Link href="/mypages" className="text-[10px] font-black uppercase tracking-[0.2em] text-red-600 flex items-center gap-2 mb-4 hover:opacity-70 transition-opacity">
                        <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="3"><path d="M15 19l-7-7 7-7" /></svg>
                        Tillbaka till Mina Sidor
                    </Link>
                    <h1 className="text-4xl font-black tracking-tight text-gray-900">Mina Kreditkort</h1>
                </div>
            </header>

            {/* Kortlista */}
            <div className="w-full max-w-4xl px-8 flex flex-col gap-10">
                {data.creditCards.map((card) => (
                    <div key={card.creditCardId} className="bg-white rounded-[40px] p-8 shadow-sm border border-gray-100 flex flex-col lg:flex-row items-center gap-12 transition-all hover:shadow-md">

                        {/* Visuellt Kort */}
                        <div className={`${getCardStyle(card.creditCardType)} w-full md:w-80 h-52 rounded-[28px] p-8 flex flex-col justify-between shrink-0 shadow-xl relative overflow-hidden`}>
                            <div className="flex justify-between items-start">
                                <span className="text-[10px] font-black tracking-[0.2em] uppercase opacity-60">Omega Bank</span>
                                <div className="w-10 h-7 bg-white/20 rounded-md border border-white/10" />
                            </div>
                            <div>
                                <p className="text-xl font-medium tracking-[0.2em] mb-2">•••• •••• •••• {card.creditCardId.slice(-4)}</p>
                                <p className="text-[10px] uppercase font-black tracking-widest opacity-60">{card.creditCardType}</p>
                            </div>
                        </div>

                        {/* Attribut-lista matchad mot bilden */}
                        <div className="flex-1 w-full space-y-4">
                            <h2 className="text-2xl font-black text-gray-900 uppercase tracking-tight mb-6">{card.creditCardType}</h2>

                            <div className="space-y-3">
                                <div className="flex flex-col sm:flex-row sm:justify-between border-b border-gray-50 pb-2">
                                    <span className="text-[11px] font-black uppercase tracking-widest text-gray-400">Kort-ID</span>
                                    <span className="text-sm font-mono font-medium text-gray-700 break-all">{card.creditCardId}</span>
                                </div>

                                <div className="flex justify-between border-b border-gray-50 pb-2">
                                    <span className="text-[11px] font-black uppercase tracking-widest text-gray-400">Status</span>
                                    <span className="text-[11px] font-black uppercase text-green-600 bg-green-50 px-2 py-0.5 rounded">{card.status}</span>
                                </div>

                                <div className="flex justify-between border-b border-gray-50 pb-2">
                                    <span className="text-[11px] font-black uppercase tracking-widest text-gray-400">Spenderat</span>
                                    <span className="text-sm font-bold text-red-600">{card.spentAmount.toLocaleString('sv-SE')} kr</span>
                                </div>

                                <div className="flex justify-between border-b border-gray-50 pb-2">
                                    <span className="text-[11px] font-black uppercase tracking-widest text-gray-400">Tillgängligt</span>
                                    <span className="text-sm font-black text-gray-900">{card.availableAmount.toLocaleString('sv-SE')} kr</span>
                                </div>

                                <div className="flex justify-between border-b border-gray-50 pb-2">
                                    <span className="text-[11px] font-black uppercase tracking-widest text-gray-400">Kreditgräns</span>
                                    <span className="text-sm font-bold text-gray-700">{card.creditLimit.toLocaleString('sv-SE')} kr</span>
                                </div>

                                <div className="flex justify-between border-b border-gray-50 pb-2">
                                    <span className="text-[11px] font-black uppercase tracking-widest text-gray-400">Ränta</span>
                                    <span className="text-sm font-bold text-gray-700">{card.interestRate}%</span>
                                </div>

                                <div className="flex justify-between">
                                    <span className="text-[11px] font-black uppercase tracking-widest text-gray-400">Årsavgift</span>
                                    <span className="text-sm font-bold text-gray-700">{card.fee} kr</span>
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </main>
    );
}