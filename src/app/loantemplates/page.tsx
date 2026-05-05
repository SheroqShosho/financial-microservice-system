import LoanBox from "@/components/LoanTemplate";
import { LoanTemplateDTO } from "@/types/loan";

async function getTemplates(): Promise<LoanTemplateDTO[]> {
    const res = await fetch("http://localhost:8080/template/loans", {
        cache: "no-store",
    });
    if (!res.ok) throw new Error("Could not get templates from ProductAPI");
    return res.json();
}

export default async function LoansPage() {
    const templates = await getTemplates();
    const active = templates.filter((t) => t.productStatus === "ACTIVE");

    return (
        <main className="min-h-screen bg-white text-gray-900">

            {/* Hero */}
            <section className="border-b border-gray-100 px-10 md:px-20 py-16 bg-gray-50">
                <div className="max-w-4xl">
                    <p className="text-xs font-bold tracking-widest text-red-600 uppercase mb-3">Våra lån</p>
                    <h1 className="text-4xl md:text-5xl font-extrabold leading-tight tracking-tight mb-4">
                        Enkla lån med<br />transparenta villkor
                    </h1>
                    <p className="text-gray-500 text-lg max-w-lg leading-relaxed">
                        Bolån eller privatlån – vi erbjuder konkurrenskraftiga räntor och snabb handläggning utan krångel.
                    </p>
                </div>
            </section>

            {/* Loans grid */}
            <section className="px-10 md:px-20 py-16">
                {active.length > 0 ? (
                    <div className="flex flex-wrap gap-8">
                        {active.map((template, index) => (
                            <LoanBox key={index} loan={template} />
                        ))}
                    </div>
                ) : (
                    <div className="bg-gray-50 p-12 rounded-2xl border text-center">
                        <p className="text-gray-400 italic">Just nu finns inga aktiva lånmallar att visa.</p>
                    </div>
                )}
            </section>


            {/* Bottom CTA strip */}
            <section className="px-10 md:px-20 py-12 bg-gray-950 text-white">
                <div className="max-w-2xl flex flex-col md:flex-row items-start md:items-center justify-start gap-15">
                    <div>
                        <h2 className="text-xl font-extrabold mb-1">Inte säker på vilket lån?</h2>
                        <p className="text-gray-400 text-sm">Kontakta vår rådgivning – vi hjälper dig välja rätt.</p>
                    </div>
                    <a
                        href="/kundservice"
                        className="flex-shrink-0 bg-red-600 hover:bg-red-700 text-white font-bold px-6 py-3 rounded transition-colors text-sm"
                    >
                        Kontakta oss
                    </a>
                </div>
            </section>
        </main>
    );
}