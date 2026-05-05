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
                const profile = backendData.profile || {};

                console.log("Backend response:", backendData);

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

    if (isLoading) return <div className="p-20 text-center font-bold tracking-widest uppercase text-xs">Laddar din profil...</div>;
    if (!data) return <div className="p-20 text-center">Sessionen har gått ut. Logga in igen.</div>;

    return (
        <main className="min-h-screen bg-gray-50/50 text-gray-900 pb-20">
            {/* Hero Header */}
            <section className="bg-white border-b border-gray-100 px-8 py-14 mb-12">
                <div className="max-w-6xl mx-auto">
                    <p className="text-red-600 font-black tracking-[0.2em] uppercase text-[10px] mb-3">Dashboard</p>
                    <h1 className="text-4xl md:text-5xl font-black tracking-tight text-gray-900">
                        Välkommen, <span className="text-red-600">{data.firstName}</span>.
                    </h1>
                    <p className="text-gray-500 mt-3 text-lg font-medium">Här är en överblick av din ekonomi hos Omega Bank.</p>
                </div>
            </section>

            <div className="max-w-6xl mx-auto px-8">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-8">

                    {/* Kreditkort-sektion */}
                    <div className="bg-white rounded-[32px] p-8 shadow-sm border border-gray-100 flex flex-col justify-between transition-all hover:shadow-xl group">
                        <div>
                            <div className="flex justify-between items-start mb-8">
                                <h2 className="text-xs font-black uppercase tracking-widest text-gray-400">Kreditkort</h2>
                                <div className="p-2.5 bg-red-50 rounded-xl text-red-600">
                                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="2">
                                        <path d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
                                    </svg>
                                </div>
                            </div>
                            <div className="space-y-8">
                                {data.creditCards.length > 0 ? data.creditCards.map((card) => (
                                    <div key={card.creditCardId} className="group/item">
                                        <div className="flex justify-between items-center mb-2">
                                            <h3 className="font-bold text-gray-900">{card.creditCardType}</h3>
                                            <span className="text-[10px] font-bold text-gray-400">AKTIVT</span>
                                        </div>
                                        <div className="flex justify-between items-end">
                                            <p className="text-[10px] text-gray-400 uppercase font-black tracking-tighter">Tillgängligt</p>
                                            <p className="font-black text-xl text-gray-900">{card.availableAmount.toLocaleString('sv-SE')} kr</p>
                                        </div>
                                        <div className="w-full bg-gray-100 h-2 rounded-full mt-3 overflow-hidden">
                                            <div
                                                className="bg-red-600 h-full rounded-full transition-all duration-1000"
                                                style={{ width: `${Math.min((card.spentAmount / card.creditLimit) * 100, 100)}%` }}
                                            />
                                        </div>
                                    </div>
                                )) : <p className="text-gray-400 italic text-sm">Inga aktiva kort</p>}
                            </div>
                        </div>
                        <Link href="/mypages/creditcards" className="mt-10">
                            <button className="w-full py-4 bg-black text-white text-[10px] font-black uppercase tracking-[0.2em] rounded-2xl hover:bg-zinc-800 transition-all active:scale-[0.98] shadow-lg shadow-black/5">
                                Hantera Kreditkort
                            </button>
                        </Link>
                    </div>

                    {/* Lån-sektion */}
                    <div className="bg-white rounded-[32px] p-8 shadow-sm border border-gray-100 flex flex-col justify-between transition-all hover:shadow-xl">
                        <div>
                            <div className="flex justify-between items-start mb-8">
                                <h2 className="text-xs font-black uppercase tracking-widest text-gray-400">Dina Lån</h2>
                                <div className="p-2.5 bg-blue-50 rounded-xl text-blue-600">
                                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="2">
                                        <path d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                                    </svg>
                                </div>
                            </div>
                            <div className="space-y-8">
                                {data.loans.length > 0 ? data.loans.map((loan) => (
                                    <div key={loan.loanId} className="group/item">
                                        <h3 className="font-bold text-gray-900 mb-2">{loan.loanType}</h3>
                                        <div className="flex justify-between items-baseline mb-1">
                                            <p className="text-[10px] text-gray-400 uppercase font-black tracking-tighter">Aktuell skuld</p>
                                            <p className="font-black text-xl text-gray-900">{loan.amount.toLocaleString('sv-SE')} kr</p>
                                        </div>
                                        <div className="flex justify-between items-center text-[11px]">
                                            <span className="text-gray-400">Ränta: {loan.interestRate}%</span>
                                            <span className="text-blue-600 font-bold">Månadsbet: {((loan.amount * (loan.interestRate / 100))/ 12).toFixed(0)} kr</span>
                                        </div>
                                    </div>
                                )) : <p className="text-gray-400 italic text-sm">Inga aktiva lån</p>}
                            </div>
                        </div>
                        <Link href="/mypages/loans" className="mt-10">
                            <button className="w-full py-4 bg-white border border-gray-200 text-gray-900 text-[10px] font-black uppercase tracking-[0.2em] rounded-2xl hover:bg-gray-50 transition-all active:scale-[0.98]">
                                Se Lånedetaljer
                            </button>
                        </Link>
                    </div>

                    {/* Profil - EditableProfile */}
                    <EditableProfile initialData={data} />

                </div>
            </div>
        </main>
    );
}