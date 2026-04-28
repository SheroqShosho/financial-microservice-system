import LoanBox from "@/components/LoanTemplate";
import { LoanTemplateDTO } from "@/types/loan";

async function getTemplates(): Promise<LoanTemplateDTO[]> {
  const res = await fetch(`http://localhost:8080/template/loans`, {
    cache: 'no-store'
  });

  if (!res.ok) {
    throw new Error('Could not get templates from ProductAPI');
  }

  return res.json();
}

export default async function LoansPage() {
  const templates = await getTemplates();
  
  return (
    <main className="p-10 min-h-screen bg-gray-50">
      <header className="mb-10">
        <h1 className="text-4xl font-bold text-blue-900">Lånmallar</h1>
        <p className="text-lg text-gray-600 mt-2">
          Här listas alla tillgängliga låntyper.
        </p>
      </header>

      {/* 3. Grid-system för att visa lånen snyggt bredvid varandra */}
      <div className="flex flex-wrap gap-6">
        {templates
          .filter((t) => t.productStatus === "ACTIVE") // Visar endast de som är ACTIVE
          .map((template, index) => (
            // Vi skickar in hela 'template'-objektet till din komponent
            // under namnet 'loan' (Props)
            <LoanBox key={index} loan={template} />
          ))
        }
      </div>

      {/* 4. Om inga lån hittas efter filtrering */}
      {templates.filter(t => t.productStatus === "ACTIVE").length === 0 && (
        <div className="bg-white p-8 rounded-lg border text-center shadow-sm">
          <p className="text-gray-500 italic">Just nu finns inga aktiva lånmallar att visa.</p>
        </div>
      )}
    </main>
  );
}

