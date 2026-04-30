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

            {/* Why us */}
            <section className="px-10 md:px-20 py-12 bg-gray-50 border-t border-gray-100">
                <div className="max-w-3xl">
                    <h2 className="text-xl font-extrabold mb-6">Varför Omega Bank?</h2>
                    <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
                        {[
                            { title: "Snabbt svar", desc: "Vi handlägger din ansökan inom 24 timmar." },
                            { title: "Inga dolda avgifter", desc: "Du ser alltid hela kostnaden innan du skriver under." },
                            { title: "Personlig rådgivning", desc: "En rådgivare hjälper dig hela vägen." },
                        ].map((item) => (
                            <div key={item.title} className="bg-white border border-gray-200 rounded-xl p-5">
                                <div className="w-6 h-6 bg-red-600 rounded-full mb-3 flex items-center justify-center">
                                    <svg viewBox="0 0 12 10" fill="none" className="w-3 h-3">
                                        <path d="M1 5L4.5 8.5L11 1.5" stroke="white" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
                                    </svg>
                                </div>
                                <h3 className="font-bold text-sm mb-1">{item.title}</h3>
                                <p className="text-gray-500 text-xs leading-relaxed">{item.desc}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </section>

            {/* Bottom CTA */}
            <section className="px-10 md:px-20 py-12 bg-gray-950 text-white">
                <div className="max-w-2xl flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
                    <div>
                        <h2 className="text-xl font-extrabold mb-1">Redo att ansöka?</h2>
                        <p className="text-gray-400 text-sm">Logga in med Google och skicka in din ansökan på under fem minuter.</p>
                    </div>
                    <a
                        href="/mypages/loans/apply"
                        className="flex-shrink-0 bg-red-600 hover:bg-red-700 text-white font-bold px-6 py-3 rounded transition-colors text-sm"
                    >
                        Ansök nu
                    </a>
                </div>
            </section>
        </main>
    );
}