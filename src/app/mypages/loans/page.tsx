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

export default async function LoansDetailPage() {
    const data = await getUserData("test123");
    if (!data) return <div className="p-10">Kunde inte hämta data.</div>;

    return (
        <main className="p-4 md:p-8 bg-gray-50 min-h-screen text-gray-800">
            <div className="w-full max-w-6xl mx-auto">
                <header className="mb-6">
                    <h1 className="text-2xl font-bold">Mina Lån - Detaljer</h1>
                    <Link href="/mypages" className="text-blue-600 hover:underline">Tillbaka till Mina Sidor</Link>
                </header>

                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {data.loans.map((loan) => (
                        <div key={loan.loanId} className="bg-white p-5 rounded-xl border-2 border-black shadow-sm">
                            <h3 className="text-lg font-bold mb-3">{loan.loanType}</h3>
                            <div className="space-y-2 text-sm">
                                <p><strong>Lån-ID:</strong> {loan.loanId}</p>
                                <p><strong>Status:</strong> {loan.loanStatus}</p>
                                <p><strong>Lånesumma:</strong> {loan.amount} kr</p>
                                <p><strong>Ränta:</strong> {loan.interestRate}%</p>
                                <p><strong>Löptid:</strong> {loan.durationMonths} månader</p>
                                <p><strong>Månadlig betalning:</strong> {((loan.amount * (loan.interestRate / 100))/ 12).toLocaleString('sv-SE', {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2
                                })} kr</p>
                            </div>
                        </div>
                    ))}
                </div>

                {data.loans.length === 0 && (
                    <div className="bg-white p-8 rounded-lg border text-center shadow-sm">
                        <p className="text-gray-500 italic">Inga lån att visa.</p>
                    </div>
                )}
            </div>
        </main>
    );
}
