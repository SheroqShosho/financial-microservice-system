// src/app/loans/[type]/page.tsx

import { LoanTemplateDTO } from "@/types/loan";

async function getLoanData(type: string): Promise<LoanTemplateDTO | null> {
  const res = await fetch('http://localhost:8080/template/loans', { cache: 'no-store' });
  if (!res.ok) return null;

  const loans: LoanTemplateDTO[] = await res.json();
  // Vi använder decodeURIComponent ifall det finns specialtecken i URL:en
  const decodedType = decodeURIComponent(type).toLowerCase();

  return loans.find(l => l.loanType.toLowerCase() === decodedType) || null;
}

// Lägg till async här för params
export default async function LoanDetailPage({ params }: { params: Promise<{ type: string }> }) {
  // Här är fixen: Vänta på att params ska läsas in
  const resolvedParams = await params;
  const loan = await getLoanData(resolvedParams.type);

  if (!loan) {
    return (
      <main className="p-10 bg-white min-h-screen text-gray-900">
        <h1 className="text-2xl font-bold">Lånet &quot;{resolvedParams.type}&quot; hittades inte.</h1>
        <p className="mt-2 text-gray-600">Kontrollera att namnet stämmer i URL:en.</p>
      </main>
    );
  }

  return (
    <main className="p-10 min-h-screen bg-white text-gray-900">
      <h1 className="text-4xl font-bold text-blue-900 mb-4">{loan.loanType}</h1>
      <h1 className="text-1xl font-bold text-gray-800 mb-4">Ränta: {loan.interestRate}%</h1>
      <h1 className="text-1xl font-bold text-gray-800 mb-4">Min belopp: {loan.minAmount} kr</h1>
      <h1 className="text-1xl font-bold text-gray-800 mb-4">Max belopp: {loan.maxAmount} kr</h1>
      <p className="text-1xl font-bold text-gray-800 mb-4">Beskrivning: {loan.description}</p>

      {/* Resten av din UI-kod... */}
    </main>
  );
}

