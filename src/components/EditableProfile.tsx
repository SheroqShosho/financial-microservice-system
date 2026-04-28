"use client"; // Viktigt! Detta gör att vi kan använda knappar och inputs

import { useState } from "react";
import { MyPagesDTO } from "@/types/user";

export default function EditableProfile({ initialData }: { initialData: MyPagesDTO }) {
    const [isEditing, setIsEditing] = useState(false);
    const [profile, setProfile] = useState(initialData);
    const [isSaving, setIsSaving] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setProfile({ ...profile, [e.target.name]: e.target.value });
    };

    //TODO : LÄGG IN Authorization token flöde under content type application json
    const handleSave = async () => {
        setIsSaving(true);
        try {
            const res = await fetch(`http://localhost:8080/profile/${profile.username}/${profile.socialSecurityNumber}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    firstName: profile.firstName,
                    lastName: profile.lastName,
                    country: profile.country,
                    city: profile.city,
                    address: profile.address,
                    zipCode: profile.zipCode,
                    phoneNumber: "",
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
        <section className="bg-white p-5 rounded-xl border-2 border-black shadow-sm h-full flex flex-col justify-between">
            <div className="space-y-3 text-sm">
                <h2 className="text-lg font-bold mb-3 border-b pb-1 uppercase tracking-tight text-gray-800">Profil</h2>

                {[
                    { label: "Namn", name: "firstName", val: profile.firstName },
                    { label: "Efternamn", name: "lastName", val: profile.lastName },
                    { label: "Adress", name: "address", val: profile.address },
                    { label: "Stad", name: "city", val: profile.city },
                    { label: "Postnr", name: "zipCode", val: profile.zipCode },
                    { label: "Land", name: "country", val: profile.country },
                ].map((field) => (
                    <div key={field.name}>
                        <label className="block text-[10px] uppercase font-bold text-gray-500">{field.label}</label>
                        {isEditing ? (
                            <input
                                name={field.name}
                                value={field.val}
                                onChange={handleChange}
                                className="w-full border-b border-blue-500 bg-blue-50 outline-none p-1 text-gray-900"
                            />
                        ) : (
                            <p className="text-gray-900 font-medium">{field.val}</p>
                        )}
                    </div>
                ))}
            </div>

            <button
                onClick={isEditing ? handleSave : () => setIsEditing(true)}
                disabled={isSaving}
                className={`mt-6 py-2 px-6 border-2 border-black rounded-full font-bold transition-all ${
                    isEditing ? "bg-black text-white" : "hover:bg-gray-100"
                } ${isSaving ? "opacity-50 cursor-not-allowed" : ""}`}
            >
                {isSaving ? "Sparar..." : isEditing ? "Spara" : "Ändra"}
            </button>
        </section>
    );
}