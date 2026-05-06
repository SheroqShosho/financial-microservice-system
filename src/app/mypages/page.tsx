"use client";

import { MyPagesDTO } from "@/types/user";
import Link from "next/link";
import EditableProfile from "@/components/EditableProfile";
import { useEffect, useState } from "react";

export default function MyPages() {
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
                const profile = backendData.profile || {};

                setData({
                    username: profile.userId ?? "Användarnamn saknas",
                    firstName: profile.firstName ?? "",
                    lastName: profile.lastName ?? "",
                    socialSecurityNumber: profile.socialSecurityNumber ?? "",
                    address: profile.address ?? "",
                    city: profile.city ?? "",
                    zipCode: profile.zipCode ?? "",
                    country: profile.country ?? "",
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

    if (isLoading) return <div className="p-20 text-center font-black tracking-[0.3em] uppercase text-[10px] text-white bg-[#003349] min-h-screen flex items-center justify-center">Laddar Dashboard...</div>;
    if (!data) return <div className="p-20 text-center text-white bg-[#003349] min-h-screen flex items-center justify-center">Sessionen har gått ut. Logga in igen.</div>;

    return (
        <main className="min-h-screen bg-[#f8fafc] text-gray-900 flex flex-col items-center">

            {/* Hero Header */}
            <header className="w-full bg-[#003349] px-8 py-24 flex flex-col items-center text-center relative overflow-hidden">
                <div className="absolute inset-0 opacity-10 pointer-events-none bg-[radial-gradient(circle_at_top_right,_var(--tw-gradient-stops))] from-blue-300 via-transparent to-transparent" />

                <div className="w-full max-w-6xl relative z-10 mt-10">
                    <p className="text-red-500 font-black tracking-[0.4em] uppercase text-[10px] mb-4">Mina Sidor</p>
                    <h1 className="text-4xl md:text-6xl font-black tracking-tighter text-white">
                        Välkommen, <span className="text-white/80 italic font-medium">{data.firstName}</span>.
                    </h1>
                    <p className="text-blue-100/60 mt-4 text-lg font-medium max-w-xl mx-auto">
                        Här är en överblick av din ekonomi hos Omega Bank.
                    </p>
                </div>
            </header>

            {/* Dashboard Grid */}
            <div className="w-full max-w-6xl mx-auto px-8 py-16">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-8">

                    {/* Kreditkort-sektion */}
                    <div className="bg-white rounded-[40px] p-8 shadow-2xl shadow-gray-200/60 border border-gray-100 flex flex-col justify-between transition-all hover:-translate-y-2 duration-500">
                        <div>
                            <div className="flex justify-between items-start mb-10">
                                <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400">Kreditkort</h2>
                                <div className="p-3 bg-red-50 rounded-2xl text-red-600">
                                    <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="2.5">
                                        <path d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                                    </svg>
                                </div>
                            </div>
                            <div className="space-y-8">
                                {data.creditCards.length > 0 ? data.creditCards.map((card) => (
                                    <div key={card.creditCardId} className="group/item">
                                        <div className="flex justify-between items-center mb-2">
                                            <h3 className="font-black text-gray-900 uppercase text-xs tracking-tight">{card.creditCardType}</h3>
                                            <span className="text-[9px] font-black text-green-600 bg-green-50 px-2 py-0.5 rounded-full">AKTIVT</span>
                                        </div>
                                        <div className="flex justify-between items-end">
                                            <p className="text-[10px] text-gray-400 uppercase font-black tracking-widest">Tillgängligt</p>
                                            <p className="font-black text-2xl text-gray-900">{card.availableAmount.toLocaleString('sv-SE')} kr</p>
                                        </div>
                                        <div className="w-full bg-gray-100 h-1.5 rounded-full mt-4 overflow-hidden">
                                            <div
                                                className="bg-red-600 h-full rounded-full transition-all duration-1000"
                                                style={{ width: `${Math.min((card.spentAmount / card.creditLimit) * 100, 100)}%` }}
                                            />
                                        </div>
                                    </div>
                                )) : <p className="text-gray-400 italic text-sm">Inga aktiva kort</p>}
                            </div>
                        </div>
                        <Link href="/mypages/creditcards" className="mt-12">
                            <button className="w-full py-5 bg-[#003349] text-white text-[10px] font-black uppercase tracking-[0.2em] rounded-2xl hover:bg-red-600 transition-all active:scale-[0.98] shadow-xl shadow-[#003349]/10">
                                Hantera Kreditkort
                            </button>
                        </Link>
                    </div>

                    {/* Lån-sektion */}
                    <div className="bg-white rounded-[40px] p-8 shadow-2xl shadow-gray-200/60 border border-gray-100 flex flex-col justify-between transition-all hover:-translate-y-2 duration-500">
                        <div>
                            <div className="flex justify-between items-start mb-10">
                                <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400">Dina Lån</h2>
                                <div className="p-3 bg-blue-50 rounded-2xl text-[#003349]">
                                    <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="2.5">
                                        <path d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                                    </svg>
                                </div>
                            </div>
                            <div className="space-y-8">
                                {data.loans.length > 0 ? data.loans.map((loan) => (
                                    <div key={loan.loanId} className="group/item">
                                        <h3 className="font-black text-gray-900 uppercase text-xs tracking-tight mb-2">{loan.loanType}</h3>
                                        <div className="flex justify-between items-baseline mb-3">
                                            <p className="text-[10px] text-gray-400 uppercase font-black tracking-widest">Skuld</p>
                                            <p className="font-black text-2xl text-gray-900">{loan.amount.toLocaleString('sv-SE')} kr</p>
                                        </div>
                                        <div className="flex justify-between items-center text-[10px] font-bold uppercase tracking-wide">
                                            <span className="text-gray-400">Ränta: {loan.interestRate}%</span>
                                            <span className="text-red-600">Bet: {((loan.amount * (loan.interestRate / 100))/ 12).toFixed(0)} kr/mån</span>
                                        </div>
                                    </div>
                                )) : <p className="text-gray-400 italic text-sm">Inga aktiva lån</p>}
                            </div>
                        </div>
                        <Link href="/mypages/loans" className="mt-12">
                            <button className="w-full py-5 bg-white border-2 border-gray-100 text-gray-900 text-[10px] font-black uppercase tracking-[0.2em] rounded-2xl hover:bg-gray-50 transition-all active:scale-[0.98]">
                                Se Lånedetaljer
                            </button>
                        </Link>
                    </div>

                    {/* Profil - EditableProfile */}
                    <div className="md:col-span-1">
                        <EditableProfile initialData={data} />
                    </div>

                </div>
            </div>
        </main>
    );
}