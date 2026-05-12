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
        <main className="min-h-screen bg-white text-gray-900 flex flex-col items-center">

            {/* Hero */}
            <section className="w-full px-10 py-32 bg-[#003349] flex flex-col items-center text-center relative overflow-hidden">
                <div className="absolute inset-0 opacity-10 pointer-events-none bg-[radial-gradient(circle_at_top_right,_var(--tw-gradient-stops))] from-blue-300 via-transparent to-transparent" />

                <div className="max-w-3xl relative z-10 mt-10">
                    <p className="text-[10px] font-black tracking-[0.3em] text-red-500 uppercase mb-4">Finansiering</p>
                    <h1 className="text-4xl md:text-6xl font-black leading-[1.1] tracking-tighter mb-6 text-white">
                        Enkla lån med transparenta villkor
                    </h1>
                    <div className="w-16 h-1 bg-red-600 mx-auto mb-8 rounded-full" />
                    <p className="text-blue-100/70 text-lg leading-relaxed font-medium">
                        Bolån eller privatlån – vi erbjuder konkurrenskraftiga räntor och snabb handläggning utan krångel.
                    </p>
                </div>
            </section>

            {/* Loans grid */}
            <section className="w-full flex flex-col items-center bg-white py-24">
                <div className="mb-16 text-center">
                    <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-2">Våra lånealternativ</h2>
                </div>

                <div className="w-full max-w-7xl px-10">
                    {active.length > 0 ? (
                        <div className="flex flex-wrap justify-center gap-12 w-full">
                            {active.map((template, index) => (
                                <div key={index} className="transition-all duration-500 hover:-translate-y-3 flex justify-center">
                                    <LoanBox loan={template} />
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="mx-auto bg-gray-50 p-16 rounded-[40px] border border-gray-100 text-center max-w-lg shadow-xl shadow-gray-200/50">
                            <p className="text-gray-400 italic font-medium text-sm tracking-wide">Just nu finns inga aktiva lånmallar att visa.</p>
                        </div>
                    )}
                </div>
            </section>

            {/* Bottom CTA strip */}
            <section className="w-full bg-slate-50 py-24 flex justify-center border-t border-slate-100">
            <div className="w-full max-w-5xl px-10">
                    <div className="bg-[#003349] rounded-[40px] p-12 md:p-16 shadow-2xl flex flex-col md:flex-row items-center text-center md:text-left justify-between gap-10">
                        <div className="max-w-md">
                            <h2 className="text-2xl md:text-3xl font-black tracking-tight mb-3 uppercase text-white">
                                Inte säker på vilket lån?
                            </h2>
                            <p className="text-blue-100/60 font-medium text-lg">
                                Kontakta vår rådgivning – vi hjälper dig hitta rätt lösning för din ekonomi.
                            </p>
                        </div>
                        <a
                            href="/kundservice"
                            className="w-full md:w-auto flex-shrink-0 bg-red-600 hover:bg-red-700 text-white font-black uppercase tracking-widest px-12 py-5 rounded-2xl transition-all shadow-xl shadow-red-600/30 active:scale-95 text-xs text-center"
                        >
                            Kontakta oss
                        </a>
                    </div>
                </div>
            </section>
        </main>
    );
}