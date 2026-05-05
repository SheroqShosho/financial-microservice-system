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
        // Profile fields (pre-filled)
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

                // Pre-fill form with user data
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
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
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
                profile: null, // Backend använder existing profile om den finns
                creditcard: {
                    creditCardType: formData.creditCardType
                }
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
                console.log("Credit card application successful:", responseData);
                alert("Kreditkortsansökan skickad! Kort-ID: " + responseData.creditCardId);
                window.location.href = "/mypages/creditcards";
            } else {
                const errorText = await res.text();
                console.error("Credit card application error:", res.status, errorText);
                alert("Fel vid kreditkortsansökan: " + (errorText || res.statusText));
            }
        } catch (error) {
            console.error("Network error:", error);
            alert("Nätverksfel: " + (error instanceof Error ? error.message : "Okänt fel"));
        } finally {
            setIsSubmitting(false);
        }
    };

    if (isLoading) {
        return <div className="p-10 text-center">Laddar...</div>;
    }

    if (!data) {
        return <div className="p-10">Kunde inte hämta data. Logga in igen.</div>;
    }

    return (
        <main className="p-4 md:p-8 bg-gray-50 min-h-screen text-gray-800">
            <div className="w-full max-w-4xl mx-auto">
                <header className="mb-6">
                    <h1 className="text-2xl font-bold mb-2">Ansök om Kreditkort</h1>
                    <Link href="/creditcardtemplates" className="text-blue-600 hover:underline">Tillbaka till kreditkort</Link>
                </header>

                <form onSubmit={handleSubmit} className="bg-white p-8 rounded-xl border-2 border-black shadow-sm space-y-6">

                    {/* Credit Card Information Section */}
                    <div className="border-b pb-6">
                        <h2 className="text-lg font-bold mb-4">Kortdetaljer</h2>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-bold text-gray-700 mb-2">Korttyp</label>
                                <select
                                    name="creditCardType"
                                    value={formData.creditCardType}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                                >
                                    <option value="STANDARD">Standard</option>
                                    <option value="GOLD">Gold</option>
                                    <option value="PLATINUM">Platinum</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    {/* Profile Information Section */}
                    <div className="border-b pb-6">
                        <h2 className="text-lg font-bold mb-4">Din Profilinformation</h2>
                        <p className="text-sm text-gray-600 mb-4">Dessa fält är pre-ifyllda från din profil. Uppdatera dem om det behövs.</p>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-bold text-gray-700 mb-2">Förnamn</label>
                                <input
                                    type="text"
                                    name="firstName"
                                    value={formData.firstName}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-bold text-gray-700 mb-2">Efternamn</label>
                                <input
                                    type="text"
                                    name="lastName"
                                    value={formData.lastName}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-bold text-gray-700 mb-2">Personnummer</label>
                                <input
                                    type="text"
                                    name="socialSecurityNumber"
                                    value={formData.socialSecurityNumber}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-bold text-gray-700 mb-2">Adress</label>
                                <input
                                    type="text"
                                    name="address"
                                    value={formData.address}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-bold text-gray-700 mb-2">Stad</label>
                                <input
                                    type="text"
                                    name="city"
                                    value={formData.city}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-bold text-gray-700 mb-2">Postnummer</label>
                                <input
                                    type="text"
                                    name="zipCode"
                                    value={formData.zipCode}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-bold text-gray-700 mb-2">Land</label>
                                <input
                                    type="text"
                                    name="country"
                                    value={formData.country}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-bold text-gray-700 mb-2">Telefonnummer</label>
                                <input
                                    type="tel"
                                    name="phoneNumber"
                                    value={formData.phoneNumber}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-bold text-gray-700 mb-2">Årlig Inkomst (kr)</label>
                                <input
                                    type="number"
                                    name="yearlyIncome"
                                    value={formData.yearlyIncome}
                                    onChange={handleChange}
                                    className="w-full px-4 py-2 border-2 border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
                                />
                            </div>
                        </div>
                    </div>

                    {/* Submit Button */}
                    <div className="flex gap-4">
                        <button
                            type="submit"
                            disabled={isSubmitting}
                            className="flex-1 py-3 px-6 bg-green-600 text-white rounded-full font-bold transition-all hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            {isSubmitting ? "Skickar..." : "Ansök om Kreditkort"}
                        </button>
                        <Link href="/mypages/creditcards" className="flex-1">
                            <button
                                type="button"
                                className="w-full py-3 px-6 bg-gray-400 text-white rounded-full font-bold transition-all hover:bg-gray-500"
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