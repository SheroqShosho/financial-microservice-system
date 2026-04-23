// src/app/creditcardtemplates/[type]/page.tsx

import { CreditCardTemplateDTO } from "@/types/creditcard";

async function getCardData(type: string): Promise<CreditCardTemplateDTO | null> {
  const res = await fetch('http://localhost:8080/template/creditcards', { cache: 'no-store' });
  if (!res.ok) return null;
  
  const cards: CreditCardTemplateDTO[] = await res.json();
  // Vi använder decodeURIComponent ifall det finns specialtecken i URL:en
  const decodedType = decodeURIComponent(type).toLowerCase();
  
  return cards.find(c => c.creditCardType.toLowerCase() === decodedType) || null;
}

// Lägg till async här för params
export default async function CardDetailPage({ params }: { params: Promise<{ type: string }> }) {
  // Här är fixen: Vänta på att params ska läsas in
  const resolvedParams = await params; 
  const card = await getCardData(resolvedParams.type);

  if (!card) {
    return (
      <main className="p-10 bg-white min-h-screen text-gray-900">
        <h1 className="text-2xl font-bold">Kortet "{resolvedParams.type}" hittades inte.</h1>
        <p className="mt-2 text-gray-600">Kontrollera att namnet stämmer i URL:en.</p>
      </main>
    );
  }

  return (
    <main className="p-10 min-h-screen bg-white text-gray-900">
      <h1 className="text-4xl font-bold text-blue-900 mb-4">{card.creditCardType}</h1>
      {/* Resten av din UI-kod... */}
    </main>
  );
}