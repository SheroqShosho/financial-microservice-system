"use client";

import { MyPagesDTO } from "@/types/user";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function ApplyCreditCardPage() {
    const [data, setData] = useState<MyPagesDTO | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const [formData, setFormData] = useState({
        creditCardType: "STANDARD",
        firstName: "",
        lastName: "",
        socialSecurityNumber: "",
        country: "",
        city: "",
        address: "",
        zipCode: "",
        phoneNumber: "",
        yearlyIncome: ""
    });

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
                const userData: MyPagesDTO = {
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
                };

                setData(userData);
                setFormData(prev => ({
                    ...prev,
                    firstName: userData.firstName,
                    lastName: userData.lastName,
                    socialSecurityNumber: userData.socialSecurityNumber,
                    country: userData.country,
                    city: userData.city,
                    address: userData.address,
                    zipCode: userData.zipCode
                }));
            } catch (e) {
                console.error("Error fetching user data:", e);
            } finally {
                setIsLoading(false);
            }
        };
        fetchUserData();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsSubmitting(true);

        try {
            const token = localStorage.getItem("accessToken");
            if (!token) {
                alert("Du är inte inloggad");
                return;
            }

            const requestBody = {
                profile: null,
                creditcard: { creditCardType: formData.creditCardType }
            };

            const res = await fetch("http://localhost:8080/creditcard", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestBody)
            });

            if (res.ok) {
                const responseData = await res.json();
                alert("Kreditkortsansökan skickad!");
                window.location.href = "/mypages/creditcards";
            } else {
                const errorText = await res.text();
                alert("Fel vid ansökan: " + errorText);
            }
        } catch (error) {
            alert("Nätverksfel");
        } finally {
            setIsSubmitting(false);
        }
    };

    if (isLoading) return <div className="p-20 text-center font-bold tracking-widest uppercase text-xs text-slate-400">Laddar formulär...</div>;

    return (
        <main className="min-h-screen bg-gray-50/30 pb-20 flex flex-col items-center">
            {/* Header - Samma stil som de andra sidorna */}
            <header className="w-full bg-[#003349] px-8 pt-32 pb-16 mb-12 flex justify-center border-b border-white/5 shadow-lg">
                <div className="w-full max-w-4xl">
                    <h1 className="text-4xl md:text-5xl font-black tracking-tight text-white mb-4 uppercase">Ansök om Kreditkort</h1>
                    <Link href="/mypages/creditcards" className="text-[10px] font-black uppercase tracking-[0.2em] text-red-500 flex items-center gap-2 hover:opacity-70 transition-opacity">
                        <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="3"><path d="M15 19l-7-7 7-7" /></svg>
                        Avbryt och gå tillbaka
                    </Link>
                </div>
            </header>

            <div className="w-full max-w-4xl px-8">
                <form onSubmit={handleSubmit} className="bg-white rounded-[40px] p-10 shadow-sm border border-gray-100 space-y-12">

                    {/* Kortval Sektion */}
                    <section>
                        <h2 className="text-2xl font-black text-gray-900 uppercase tracking-tight mb-6 border-l-4 border-[#003349] pl-4">Välj Korttyp</h2>
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="flex flex-col">
                                <label className="text-[11px] font-black uppercase tracking-widest text-gray-400 mb-2">Kortnivå</label>
                                <select
                                    name="creditCardType"
                                    value={formData.creditCardType}
                                    onChange={handleChange}
                                    className="w-full px-6 py-4 bg-gray-50 border border-gray-100 rounded-2xl focus:outline-none focus:ring-2 focus:ring-[#003349] font-bold text-gray-700"
                                >
                                    <option value="STANDARD">Standard</option>
                                    <option value="GOLD">Gold</option>
                                    <option value="PLATINUM">Platinum</option>
                                </select>
                            </div>
                        </div>
                    </section>

                    {/* Profilinformation Sektion */}
                    <section>
                        <h2 className="text-2xl font-black text-gray-900 uppercase tracking-tight mb-2 border-l-4 border-[#003349] pl-4">Din Profilinformation</h2>
                        <p className="text-[11px] font-bold text-gray-400 uppercase tracking-wider mb-8 pl-4">Kontrollera att dina uppgifter stämmer</p>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-x-10 gap-y-6">
                            {[
                                { label: "Förnamn", name: "firstName", type: "text" },
                                { label: "Efternamn", name: "lastName", type: "text" },
                                { label: "Personnummer", name: "socialSecurityNumber", type: "text" },
                                { label: "Adress", name: "address", type: "text" },
                                { label: "Stad", name: "city", type: "text" },
                                { label: "Postnummer", name: "zipCode", type: "text" },
                                { label: "Land", name: "country", type: "text" },
                                { label: "Telefonnummer", name: "phoneNumber", type: "tel" },
                            ].map((field) => (
                                <div key={field.name} className="flex flex-col">
                                    <label className="text-[11px] font-black uppercase tracking-widest text-gray-400 mb-2">{field.label}</label>
                                    <input
                                        type={field.type}
                                        name={field.name}
                                        value={(formData as any)[field.name]}
                                        onChange={handleChange}
                                        className="px-6 py-4 bg-gray-50 border border-gray-100 rounded-2xl focus:outline-none focus:ring-2 focus:ring-gray-200 font-medium text-gray-700 transition-all"
                                        required={field.name !== "phoneNumber"}
                                    />
                                </div>
                            ))}

                            <div className="flex flex-col">
                                <label className="text-[11px] font-black uppercase tracking-widest text-gray-400 mb-2">Årlig Inkomst (kr)</label>
                                <input
                                    type="number"
                                    name="yearlyIncome"
                                    placeholder="Ex: 450000"
                                    value={formData.yearlyIncome}
                                    onChange={handleChange}
                                    className="px-6 py-4 bg-gray-50 border border-gray-100 rounded-2xl focus:outline-none focus:ring-2 focus:ring-gray-200 font-medium text-gray-700 transition-all"
                                />
                            </div>
                        </div>
                    </section>

                    {/* Submit Sektion */}
                    <div className="pt-10 border-t border-gray-50 flex flex-col sm:flex-row gap-4">
                        <button
                            type="submit"
                            disabled={isSubmitting}
                            className="flex-1 bg-[#003349] hover:opacity-90 text-white text-[11px] font-black uppercase tracking-[0.2em] px-10 py-5 rounded-2xl transition-all disabled:opacity-50"
                        >
                            {isSubmitting ? "Behandlar ansökan..." : "Skicka Ansökan"}
                        </button>
                        <Link href="/mypages/creditcards" className="flex-1">
                            <button
                                type="button"
                                className="w-full border border-gray-200 text-gray-400 text-[11px] font-black uppercase tracking-[0.2em] px-10 py-5 rounded-2xl hover:bg-gray-50 transition-all"
                            >
                                Avbryt
                            </button>
                        </Link>
                    </div>
                </form>
            </div>
        </main>
    );
}