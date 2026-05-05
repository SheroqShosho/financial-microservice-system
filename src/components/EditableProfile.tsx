"use client";

import { useState } from "react";
import { MyPagesDTO } from "@/types/user";

export default function EditableProfile({ initialData }: { initialData: MyPagesDTO }) {
    const [isEditing, setIsEditing] = useState(false);
    const [profile, setProfile] = useState(initialData);
    const [isSaving, setIsSaving] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setProfile({ ...profile, [e.target.name]: e.target.value });
    };

    const handleSave = async () => {
        setIsSaving(true);
        try {
            const token = localStorage.getItem("accessToken");

            const res = await fetch(`http://localhost:8080/profile/${profile.username}/${profile.socialSecurityNumber}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify({
                    firstName: profile.firstName,
                    lastName: profile.lastName,
                    country: profile.country,
                    city: profile.city,
                    address: profile.address,
                    zipCode: profile.zipCode,
                    phoneNumber: "", // Behålls tomma enligt din existerande kod
                    yearlyIncome: 0,
                    status: "ACTIVE"
                }),
            });

            if (res.ok) {
                setIsEditing(false);
                alert('Profil uppdaterad!');
            } else {
                const errorText = await res.text();
                console.error('Update error:', res.status, errorText);
                alert('Fel vid uppdatering: ' + (errorText || res.statusText));
            }
        } catch (error) {
            console.error('Network error:', error);
            alert('Nätverksfel: ' + (error instanceof Error ? error.message : 'Okänt fel'));
        } finally {
            setIsSaving(false);
        }
    };

    return (
        <div className="bg-white rounded-[32px] p-8 shadow-sm border border-gray-100 h-full flex flex-col transition-all hover:shadow-md">
            {/* Header */}
            <div className="flex justify-between items-center mb-10 border-b border-gray-50 pb-4">
                <div>
                    <h2 className="text-[10px] font-black uppercase tracking-[0.2em] text-gray-400">Din Profil</h2>
                    <p className="text-[9px] font-mono text-gray-300 mt-1 uppercase tracking-tight">{profile.username}</p>
                </div>
                {!isSaving && (
                    <button
                        onClick={isEditing ? () => setIsEditing(false) : () => setIsEditing(true)}
                        className="text-[10px] font-black uppercase tracking-widest text-red-600 hover:opacity-70 transition-opacity"
                    >
                        {isEditing ? "Avbryt" : "Redigera"}
                    </button>
                )}
            </div>

            <div className="space-y-8 flex-1">
                {/* Namn-sektion */}
                <div className="grid grid-cols-2 gap-6">
                    <div className="flex flex-col">
                        <label className="text-[9px] font-black uppercase tracking-widest text-gray-400 mb-1">Förnamn</label>
                        {isEditing ? (
                            <input
                                name="firstName"
                                value={profile.firstName}
                                onChange={handleChange}
                                className="border-b-2 border-black py-1 focus:outline-none text-sm font-bold bg-transparent"
                            />
                        ) : (
                            <span className="text-sm font-bold text-gray-900">{profile.firstName}</span>
                        )}
                    </div>
                    <div className="flex flex-col">
                        <label className="text-[9px] font-black uppercase tracking-widest text-gray-400 mb-1">Efternamn</label>
                        {isEditing ? (
                            <input
                                name="lastName"
                                value={profile.lastName}
                                onChange={handleChange}
                                className="border-b-2 border-black py-1 focus:outline-none text-sm font-bold bg-transparent"
                            />
                        ) : (
                            <span className="text-sm font-bold text-gray-900">{profile.lastName}</span>
                        )}
                    </div>
                </div>

                {/* Personnummer (Skrivskyddat) */}
                <div className="flex flex-col">
                    <label className="text-[9px] font-black uppercase tracking-widest text-gray-400 mb-1">Personnummer</label>
                    <span className="text-sm font-medium text-gray-500">{profile.socialSecurityNumber}</span>
                </div>

                {/* Adress-sektion */}
                <div className="space-y-6 pt-4 border-t border-gray-50">
                    <div className="flex flex-col">
                        <label className="text-[9px] font-black uppercase tracking-widest text-gray-400 mb-1">Gatuadress</label>
                        {isEditing ? (
                            <input
                                name="address"
                                value={profile.address}
                                onChange={handleChange}
                                className="border-b-2 border-black py-1 focus:outline-none text-sm font-bold bg-transparent"
                            />
                        ) : (
                            <span className="text-sm font-bold text-gray-900">{profile.address}</span>
                        )}
                    </div>

                    <div className="grid grid-cols-2 gap-6">
                        <div className="flex flex-col">
                            <label className="text-[9px] font-black uppercase tracking-widest text-gray-400 mb-1">Postnummer</label>
                            {isEditing ? (
                                <input
                                    name="zipCode"
                                    value={profile.zipCode}
                                    onChange={handleChange}
                                    className="border-b-2 border-black py-1 focus:outline-none text-sm font-bold bg-transparent"
                                />
                            ) : (
                                <span className="text-sm font-bold text-gray-900">{profile.zipCode}</span>
                            )}
                        </div>
                        <div className="flex flex-col">
                            <label className="text-[9px] font-black uppercase tracking-widest text-gray-400 mb-1">Stad</label>
                            {isEditing ? (
                                <input
                                    name="city"
                                    value={profile.city}
                                    onChange={handleChange}
                                    className="border-b-2 border-black py-1 focus:outline-none text-sm font-bold bg-transparent"
                                />
                            ) : (
                                <span className="text-sm font-bold text-gray-900">{profile.city}</span>
                            )}
                        </div>
                    </div>

                    <div className="flex flex-col">
                        <label className="text-[9px] font-black uppercase tracking-widest text-gray-400 mb-1">Land</label>
                        {isEditing ? (
                            <input
                                name="country"
                                value={profile.country}
                                onChange={handleChange}
                                className="border-b-2 border-black py-1 focus:outline-none text-sm font-bold bg-transparent"
                            />
                        ) : (
                            <span className="text-sm font-bold text-gray-900">{profile.country}</span>
                        )}
                    </div>
                </div>
            </div>

            {/* Spara-knapp */}
            {isEditing && (
                <button
                    onClick={handleSave}
                    disabled={isSaving}
                    className={`w-full mt-10 py-4 bg-black text-white text-[10px] font-black uppercase tracking-[0.2em] rounded-2xl transition-all shadow-lg shadow-gray-200 active:scale-95 ${
                        isSaving ? "opacity-50 cursor-not-allowed" : "hover:bg-gray-800"
                    }`}
                >
                    {isSaving ? "Sparar..." : "Spara ändringar"}
                </button>
            )}
        </div>
    );
}