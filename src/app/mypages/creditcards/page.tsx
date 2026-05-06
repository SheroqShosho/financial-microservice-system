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

    if (isLoading) return <div className="p-20 text-center font-bold tracking-widest uppercase text-xs text-slate-400">Laddar...</div>;
    if (!data) return <div className="p-20 text-center text-sm">Kunde inte hämta data.</div>;

    const getCardAccent = (type: string) => {
        const t = type.toLowerCase();
        if (t.includes("platinum")) return { bg: "bg-gradient-to-br from-[#38495a] to-[#2b3947]", chip: "bg-slate-300" };
        if (t.includes("gold")) return { bg: "bg-gradient-to-br from-[#926a2d] to-[#4a3411]", chip: "bg-[#f3cf8a]" };
        return { bg: "bg-[#111111]", chip: "bg-[#926a2d]" };
    };

    return (
        <main className="min-h-screen bg-gray-50/30 pb-20 flex flex-col items-center">
            {/* Header */}
            <header className="w-full bg-[#003349] px-8 pt-32 pb-16 mb-12 flex justify-center border-b border-white/5 shadow-lg">
                <div className="w-full max-w-4xl">
                    <h1 className="text-4xl md:text-5xl font-black tracking-tight text-white mb-4 uppercase">Mina Kreditkort</h1>
                    <Link href="/mypages" className="text-[10px] font-black uppercase tracking-[0.2em] text-red-500 flex items-center gap-2 hover:opacity-70 transition-opacity">
                        <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="3"><path d="M15 19l-7-7 7-7" /></svg>
                        Tillbaka till Mina Sidor
                    </Link>
                </div>
            </header>

            {/* Kortlista */}
            <div className="w-full max-w-4xl px-8 flex flex-col gap-10">
                {data.creditCards.map((card) => {
                    const accent = getCardAccent(card.creditCardType);
                    return (
                        <div key={card.creditCardId} className="bg-white rounded-[40px] p-8 shadow-sm border border-gray-100 flex flex-col lg:flex-row items-center gap-12 transition-all hover:shadow-md">

                            {/* Visuellt Kort */}
                            <div className={`${accent.bg} w-full md:w-80 h-52 rounded-[24px] p-8 flex flex-col justify-between shrink-0 shadow-2xl relative overflow-hidden text-white`}>
                                <div className="flex justify-between items-start relative z-10">
                                    <span className="text-[11px] font-bold tracking-[0.15em] uppercase opacity-90">Omega Bank</span>
                                    <span className="text-[11px] font-bold tracking-[0.15em] uppercase opacity-90">{card.creditCardType}</span>
                                </div>

                                <div className={`w-12 h-9 rounded-md ${accent.chip} opacity-80 shadow-inner mb-2 relative z-10`} />

                                <div className="flex justify-between items-center relative z-10">
                                    <div className="flex items-baseline gap-2 text-lg font-medium tracking-[0.25em] opacity-80 whitespace-nowrap">
                                        <span>•••• •••• ••••</span>
                                        <span className="tracking-normal font-mono">{card.creditCardId.slice(-4)}</span>
                                    </div>

                                    {/* Mastercard Logo */}
                                    <div className="flex -space-x-3 opacity-95 shrink-0 ml-4">
                                        <div className="w-8 h-8 rounded-full bg-[#eb001b]" />
                                        <div className="w-8 h-8 rounded-full bg-[#f79e1b] opacity-80" />
                                    </div>
                                </div>

                                <div className="absolute top-0 right-0 w-full h-full bg-[radial-gradient(circle_at_50%_0%,rgba(255,255,255,0.08),transparent)] pointer-events-none" />
                            </div>

                            {/* Attribut-lista */}
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
                    );
                })}
            </div>
        </main>
    );
}