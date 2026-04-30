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
        <main className="p-4 md:p-8 bg-gray-50 min-h-screen text-gray-800 flex flex-col items-center">
            <div className="w-full max-w-6xl">
                <h1 className="text-2xl font-bold mb-6 p-10">Välkommen till mina sidor, {data.firstName}</h1>

                <div className="grid grid-cols-1 md:grid-cols-3 gap-10">

                    {/* Kreditkort - Hela rutan är nu en länk */}
                    <div className="group">
                        <section
                            className="bg-white p-5 rounded-xl border-2 border-black shadow-sm h-full flex flex-col justify-between">
                            <div>
                                <h2 className="text-lg font-bold mb-3 border-b pb-1 uppercase tracking-tight">Kreditkort</h2>
                                <div className="space-y-4">
                                    {data.creditCards.map((card) => (
                                        <div key={card.creditCardId} className="text-sm">
                                            <h3 className="font-bold">{card.creditCardType}</h3>
                                            <p>Spenderat: <span
                                                className="font-semibold text-gray-900">{card.spentAmount} kr</span></p>
                                            <p>Tillgängligt: <span
                                                className="font-semibold text-gray-900">{card.availableAmount} kr</span></p>
                                            <p>Kreditgräns: <span
                                                className="font-semibold text-gray-900">{card.creditLimit} kr</span></p>
                                            <p>Ränta: <span
                                                className="font-semibold text-gray-900">{card.interestRate}%</span></p>
                                        </div>
                                    ))}
                                </div>
                            </div>
                            <Link href="/mypages/creditcards" className="inline-block w-full">
                                <button className="mt-8 w-full py-2 px-6 border-2 border-black rounded-full font-bold transition-all hover:bg-black hover:text-white">
                                    Se mer
                                </button>
                            </Link>
                        </section>
                    </div>

                    {/* Lån - Hela rutan är nu en länk */}
                    <div className="group">
                        <section
                            className="bg-white p-5 rounded-xl border-2 border-black shadow-sm h-full flex flex-col justify-between">
                        <div>
                                <h2 className="text-lg font-bold mb-3 border-b pb-1 uppercase tracking-tight">Lån</h2>
                                <div className="space-y-4">
                                    {data.loans.map((loan) => (
                                        <div key={loan.loanId} className="text-sm">
                                            <h3 className="font-bold">{loan.loanType}</h3>
                                            <p>Lånesumma: <span className="font-semibold text-gray-900">{loan.amount} kr</span></p>
                                            <p>Nästa betalning: <span className="font-semibold text-gray-900">
                                                {((loan.amount * (loan.interestRate / 100))/ 12).toLocaleString('sv-SE', {
                                                    minimumFractionDigits: 2,
                                                    maximumFractionDigits: 2
                                                })} kr</span></p>
                                            <p>Ränta: <span className="font-semibold text-gray-900">{loan.interestRate}%</span></p>
                                        </div>
                                    ))}
                                </div>
                            </div>
                            <div className="flex gap-2 mt-8">
                                <Link href="/mypages/loans" className="flex-1">
                                    <button className="w-full py-2 px-6 border-2 border-black rounded-full font-bold transition-all hover:bg-black hover:text-white">
                                        Se mer
                                    </button>
                                </Link>
                            </div>
                        </section>
                    </div>

                    {/* Profil - Använder vår nya interaktiva komponent */}
                    <EditableProfile initialData={data} />

                </div>
            </div>
        </main>
    );
}