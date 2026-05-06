import { LoanTemplateDTO } from "@/types/loan";
import Link from "next/link";

async function getLoanData(type: string): Promise<LoanTemplateDTO | null> {
    const res = await fetch("http://localhost:8080/template/loans", { cache: "no-store" });
    if (!res.ok) return null;

    const loans: LoanTemplateDTO[] = await res.json();
    const decodedType = decodeURIComponent(type).toLowerCase();

    return loans.find((l) => l.loanType.toLowerCase() === decodedType) || null;
}

function LoanIcon({ type }: { type: string }) {
    const isMortgage = type.toLowerCase().includes("mortgage") || type.toLowerCase().includes("bostad");
    return (
        <div className="text-red-600">
            {isMortgage ? (
                <svg viewBox="0 0 24 24" fill="none" className="w-8 h-8" stroke="currentColor" strokeWidth="1.2">
                    <path d="M3 21h18M3 10l9-7 9 7v11H3V10z" strokeLinecap="round" strokeLinejoin="round" />
                    <path d="M9 21v-6h6v6" strokeLinecap="round" strokeLinejoin="round" />
                </svg>
            ) : (
                <svg viewBox="0 0 24 24" fill="none" className="w-8 h-8" stroke="currentColor" strokeWidth="1.2">
                    <rect x="3" y="6" width="18" height="12" rx="1" />
                    <path d="M7 12h10M7 9h4m-4 6h7" strokeLinecap="round" />
                </svg>
            )}
        </div>
    );
}

export default async function LoanDetailPage({
                                                 params,
                                             }: {
    params: Promise<{ type: string }>;
}) {
    const resolvedParams = await params;
    const loan = await getLoanData(resolvedParams.type);

    if (!loan) {
        return (
            <main className="min-h-screen bg-[#002a3a] flex flex-col items-center justify-center text-white px-8">
                <h1 className="text-xl font-light mb-4 uppercase tracking-widest">Lånet hittades inte</h1>
                <Link href="/" className="text-red-500 font-bold uppercase text-[10px] tracking-[0.2em] border-b border-red-500 pb-1">
                    ← Tillbaka till start
                </Link>
            </main>
        );
    }

    const isMortgage = resolvedParams.type.toLowerCase() === "mortgage";

    return (
        <main className="min-h-screen bg-white text-slate-900">

            {/* ── HERO BANNER ── */}
            <section className="bg-[#003349] pt-24 pb-40 px-8 md:px-20 relative overflow-hidden">
                <div className="absolute inset-0 opacity-10 pointer-events-none">
                    <div className="absolute top-[-10%] right-[-10%] w-[500px] h-[500px] rounded-full bg-gradient-to-br from-blue-400 to-transparent blur-[120px]" />
                </div>

                <div className="max-w-6xl mx-auto relative z-10">
                    {/* Breadcrumb */}
                    <div className="flex items-center gap-3 text-[10px] font-bold uppercase tracking-[0.3em] text-red-500 mb-12">
                        <Link href="/" className="text-slate-400 hover:text-white transition-colors">Hem</Link>
                        <span className="text-slate-600">/</span>
                        <span className="text-white">{loan.loanType}</span>
                    </div>

                    <div className="flex flex-col md:flex-row md:items-end justify-between gap-12">
                        <div className="max-w-2xl">
                            <div className="mb-6 opacity-90">
                                <LoanIcon type={resolvedParams.type} />
                            </div>
                            <h1 className="text-4xl md:text-6xl font-light tracking-tight text-white mb-6 uppercase">
                                {loan.loanType}
                            </h1>
                            <p className="text-slate-300 text-lg md:text-xl font-light leading-relaxed">
                                {loan.description}
                            </p>
                        </div>
                        <Link
                            href="/mypages/loans/apply"
                            className="bg-red-600 hover:bg-red-700 text-white text-[11px] font-bold uppercase tracking-widest px-12 py-5 rounded-sm transition-all shadow-xl shadow-black/20"
                        >
                            Starta ansökan
                        </Link>
                    </div>
                </div>
            </section>

            {/* ── STATS CARDS ── */}
            <section className="px-8 md:px-20 -mt-20 relative z-20">
                <div className="max-w-6xl mx-auto">
                    <div className="grid grid-cols-1 md:grid-cols-3 bg-white border border-slate-100 rounded-sm shadow-2xl shadow-slate-900/5">
                        {[
                            { label: "Ränta från", value: `${loan.interestRate}%`, sub: "Rörlig årsränta" },
                            { label: "Låneintervall", value: `${loan.minAmount.toLocaleString("sv-SE")} - ${loan.maxAmount.toLocaleString("sv-SE")}`, unit: " kr" },
                            { label: "Handläggning", value: "Digital", sub: "Svar inom 24h" }
                        ].map((stat, i) => (
                            <div key={i} className="p-12 flex flex-col items-center text-center border-r last:border-r-0 border-slate-50">
                                <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-4">{stat.label}</p>
                                <p className="text-3xl font-light text-slate-900 mb-1">{stat.value}<span className="text-lg">{stat.unit}</span></p>
                                <p className="text-[11px] text-slate-400 font-light italic">{stat.sub}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </section>

            {/* ── INFO SECTION ── */}
            <section className="px-8 md:px-20 py-32">
                <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-24">
                    <div>
                        <h2 className="text-xl font-medium text-slate-900 mb-8 uppercase tracking-wider">Produktinformation</h2>
                        <p className="text-slate-600 leading-relaxed font-light mb-8">
                            Vi erbjuder en trygg finansiering med villkor anpassade efter din ekonomiska profil.
                            För att beviljas lån krävs en sedvanlig kreditprövning där vi ser till din framtida betalningsförmåga.
                        </p>

                        <ul className="space-y-4">
                            {[
                                `Individuell ränta från ${loan.interestRate}%`,
                                `Flexibel återbetalning upp till ${loan.maxAmount.toLocaleString("sv-SE")} kr`,
                                "Möjlighet till betalningsfria månader",
                                "Inga dolda avgifter eller uppläggningskostnader",
                            ].map((item) => (
                                <li key={item} className="flex items-center gap-4 text-slate-700 text-sm font-light">
                                    <div className="w-1 h-1 bg-red-600 rounded-full" />
                                    {item}
                                </li>
                            ))}
                        </ul>
                    </div>

                    <div className="bg-slate-50 p-12 rounded-sm border border-slate-100 flex flex-col justify-center">
                        <h3 className="text-sm font-bold uppercase tracking-widest text-slate-900 mb-4">Behöver du vägledning?</h3>
                        <p className="text-sm text-slate-500 font-light mb-8 leading-relaxed">
                            Är du osäker på vilket lån som passar dina behov bäst? Våra rådgivare hjälper dig att hitta rätt lösning för din unika situation.
                        </p>
                        <Link href="/kundservice" className="block text-center border border-slate-200 bg-white py-4 text-[10px] font-bold uppercase tracking-widest hover:bg-slate-50 transition-colors">
                            Kontakta rådgivare
                        </Link>
                    </div>
                </div>
            </section>

            {/* ── FINAL CTA / FOOTER ── */}
            <section className="bg-slate-50 border-t border-slate-100 py-16 px-8 md:px-20 text-center">
                <div className="max-w-2xl mx-auto">
                    <h2 className="text-xl font-medium mb-2 uppercase tracking-widest">Redo att ansöka?</h2>
                    <p className="text-slate-500 font-light text-sm mb-10">
                        Processen är helt digital, fyll i formuläret och ansök.
                    </p>
                    <div className="flex flex-col sm:flex-row justify-center gap-4">
                        <Link
                            href="/mypages/loans/apply"
                            className="bg-red-600 hover:bg-red-700 text-white font-bold px-10 py-4 rounded-sm transition-all text-[11px] uppercase tracking-widest"
                        >
                            Ansök nu
                        </Link>
                        <Link
                            href="/loantemplates"
                            className="border border-slate-200 text-slate-600 font-bold px-10 py-4 rounded-sm hover:bg-white transition-all text-[11px] uppercase tracking-widest"
                        >
                            ← Se alla lån
                        </Link>
                    </div>
                    <p className="mt-12 text-[10px] text-slate-400 uppercase tracking-tight leading-relaxed max-w-lg mx-auto">
                        Informationen på denna sida utgör inte ett bindande erbjudande. Slutlig ränta fastställs efter kreditprövning.
                        Omega Bank står under Finansinspektionens tillsyn.
                    </p>
                </div>
            </section>
        </main>
    );
}