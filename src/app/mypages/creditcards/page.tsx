import { MyPagesDTO } from "@/types/user";
import Link from "next/link";

async function getUserData(username: string): Promise<MyPagesDTO | null> {
    try {
        const res = await fetch(`http://localhost:8080/mypages/${username}`, { cache: 'no-store' });
        if (!res.ok) return null;
        const backendData = await res.json();

        return {
            username: backendData.profile.userId,
            firstName: backendData.profile.firstName,
            lastName: backendData.profile.lastName,
            socialSecurityNumber: backendData.profile.socialSecurityNumber,
            address: backendData.profile.address,
            city: backendData.profile.city,
            zipCode: backendData.profile.zipCode,
            country: backendData.profile.country,
            creditCards: backendData.creditCards,
            loans: backendData.loans
        };
    } catch (e) { return null; }
}

export default async function CreditCardsDetailPage() {
    const data = await getUserData("test123");
    if (!data) return <div className="p-10">Kunde inte hämta data.</div>;

    return (
        <main className="p-4 md:p-8 bg-gray-50 min-h-screen text-gray-800">
            <div className="w-full max-w-6xl mx-auto">
                <header className="mb-6">
                    <h1 className="text-2xl font-bold">Mina Kreditkort - Detaljer</h1>
                    <Link href="/mypages" className="text-blue-600 hover:underline">Tillbaka till Mina Sidor</Link>
                </header>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {data.creditCards.map((card) => (
                        <div key={card.creditCardId} className="bg-white p-5 rounded-xl border-2 border-black shadow-sm">
                            <h3 className="text-lg font-bold mb-3">{card.creditCardType}</h3>
                            <div className="space-y-2 text-sm">
                                <p><strong>Kort-ID:</strong> {card.creditCardId}</p>
                                <p><strong>Status:</strong> {card.status}</p>
                                <p><strong>Spenderat:</strong> {card.spentAmount} kr</p>
                                <p><strong>Tillgängligt:</strong> {card.availableAmount} kr</p>
                                <p><strong>Kreditgräns:</strong> {card.creditLimit} kr</p>
                                <p><strong>Ränta:</strong> {card.interestRate}%</p>
                                <p><strong>Årsavgift:</strong> {card.fee} kr</p>
                            </div>
                        </div>
                    ))}
                </div>

                {data.creditCards.length === 0 && (
                    <div className="bg-white p-8 rounded-lg border text-center shadow-sm">
                        <p className="text-gray-500 italic">Inga kreditkort att visa.</p>
                    </div>
                )}
            </div>
        </main>
    );
}
