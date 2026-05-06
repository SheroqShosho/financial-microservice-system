import { CreditCardTemplateDTO } from "@/types/creditcard";
import Link from "next/link";

async function getCardData(type: string): Promise<CreditCardTemplateDTO | null> {
    const res = await fetch("http://localhost:8080/template/creditcards", { cache: "no-store" });
    if (!res.ok) return null;
    const cards: CreditCardTemplateDTO[] = await res.json();
    const decodedType = decodeURIComponent(type).toLowerCase();
    return cards.find((c) => c.creditCardType.toLowerCase() === decodedType) || null;
}

const cardAccents: Record<string, { bg: string; chip: string; glow: string }> = {
    standard: {
        bg: "from-[#1a1c20] to-[#0f1012]",
        chip: "bg-slate-400",
        glow: "from-slate-500/20"
    },
    gold: {
        bg: "from-[#a16d03] to-[#7a5202]",
        chip: "bg-[#f3e5ab]",
        glow: "from-amber-500/20"
    },
    platinum: {
        bg: "from-[#384c62] to-[#273544]",
        chip: "bg-[#e5e7eb]",
        glow: "from-blue-400/20"
    },
};

export default async function CardDetailPage({ params }: { params: Promise<{ type: string }> }) {
    const resolvedParams = await params;
    const card = await getCardData(resolvedParams.type);
    const accent = cardAccents[resolvedParams.type.toLowerCase()] ?? cardAccents["standard"];

    if (!card) {
        return (
            <main className="min-h-screen bg-[#002a3a] flex flex-col items-center justify-center text-white px-8 text-center">
                <h1 className="text-xl font-light mb-4 uppercase tracking-[0.2em]">Kortet hittades inte</h1>
                <Link href="/creditcardtemplates" className="text-red-500 font-bold uppercase text-[10px] tracking-[0.2em] border-b border-red-500 pb-1">
                    ← Se alla kreditkort
                </Link>
            </main>
        );
    }

    return (
        <main className="min-h-screen bg-white text-slate-900">

            {/* ── HERO SECTION ── */}
            <section className="bg-[#003349] pt-24 pb-48 px-8 md:px-20 relative overflow-hidden">
                <div className={`absolute inset-0 opacity-20 pointer-events-none bg-gradient-to-tr ${accent.glow} to-transparent`} />

                <div className="max-w-6xl mx-auto relative z-10">
                    <div className="flex items-center gap-3 text-[10px] font-bold uppercase tracking-[0.3em] text-red-500 mb-12">
                        <Link href="/" className="text-slate-400 hover:text-white transition-colors">Hem</Link>
                        <span className="text-slate-600">/</span>
                        <Link href="/creditcardtemplates" className="text-slate-400 hover:text-white transition-colors">Kreditkort</Link>
                        <span className="text-slate-600">/</span>
                        <span className="text-white uppercase">{card.creditCardType}</span>
                    </div>

                    <div className="flex flex-col md:flex-row items-center md:items-end justify-between gap-16">
                        <div className="max-w-2xl text-center md:text-left">
                            <h1 className="text-4xl md:text-6xl font-light tracking-tight text-white mb-6 uppercase">
                                {card.creditCardType}
                            </h1>
                            <p className="text-slate-300 text-lg md:text-xl font-light leading-relaxed mb-10">
                                {card.description} ett kort utformat för dig som värdesätter trygghet, exklusiva förmåner och global räckvidd.
                            </p>
                            <Link
                                href="/mypages/creditcards/apply"
                                className="inline-block bg-red-600 hover:bg-red-700 text-white text-[11px] font-bold uppercase tracking-widest px-12 py-5 rounded-sm transition-all shadow-xl shadow-black/20"
                            >
                                Ansök nu
                            </Link>
                        </div>

                        <div className="relative group perspective hidden md:block">
                            <div className={`relative w-[400px] h-[250px] rounded-[20px] bg-gradient-to-br ${accent.bg} p-8 shadow-[0_50px_100px_-20px_rgba(0,0,0,0.5)] border border-white/10 overflow-hidden`}>
                                <div className="absolute top-0 right-0 w-full h-full bg-[radial-gradient(circle_at_50%_0%,rgba(255,255,255,0.05),transparent)]" />
                                <div className="flex justify-between items-start mb-12">
                                    <span className="text-white/40 text-[10px] font-bold tracking-[0.3em] uppercase">Omega Bank</span>
                                    <div className={`w-12 h-9 rounded-md ${accent.chip} opacity-80 shadow-inner`} />
                                </div>
                                <div className="space-y-6">
                                    <div className="text-white/90 text-xl font-mono tracking-[0.2em]">•••• •••• •••• ••••</div>
                                    <div className="flex justify-between items-end">
                                        <div>
                                            <p className="text-white/30 text-[8px] uppercase tracking-widest mb-1">Card Holder</p>
                                            <p className="text-white/70 text-xs font-light tracking-widest uppercase">Valued Member</p>
                                        </div>
                                        <svg viewBox="0 0 38 24" className="w-12 opacity-80" fill="none">
                                            <circle cx="15" cy="12" r="10" fill="#eb001b" />
                                            <circle cx="23" cy="12" r="10" fill="#f79e1b" fillOpacity="0.8" />
                                        </svg>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* ── STATS SECTION ── */}
            <section className="px-8 md:px-20 -mt-24 relative z-20">
                <div className="max-w-6xl mx-auto">
                    <div className="grid grid-cols-1 md:grid-cols-3 bg-white border border-slate-100 rounded-sm shadow-2xl shadow-slate-900/5">
                        {[
                            { label: "Ränta", value: `${card.interestRate}%`, sub: "Årsränta på nyttjad kredit" },
                            { label: "Årsavgift", value: `${card.fee} kr`, sub: "Debiteras årligen" },
                            { label: "Kreditgräns", value: `${card.creditLimit.toLocaleString("sv-SE")} kr`, sub: "Maximal kreditvärdighet" }
                        ].map((stat, i) => (
                            <div key={i} className="p-12 flex flex-col items-center text-center border-r last:border-r-0 border-slate-50">
                                <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-4">{stat.label}</p>
                                <p className="text-3xl font-light text-slate-900 mb-1">{stat.value}</p>
                                <p className="text-[11px] text-slate-400 font-light italic">{stat.sub}</p>
                            </div>
                        ))}
                    </div>
                </div>
            </section>

            {/* ── INFO & HELP ── */}
            <section className="px-8 md:px-20 py-32 mx-auto max-w-6xl">
                <div className="grid md:grid-cols-2 gap-24">
                    <div>
                        <h2 className="text-xl font-medium text-slate-900 mb-8 uppercase tracking-wider">Förmåner & Detaljer</h2>
                        <ul className="space-y-6">
                            {[
                                `Ränta ${card.interestRate}% per år på utnyttjad kredit`,
                                `Årsavgift om endast ${card.fee} kr per år`,
                                `Kreditutrymme upp till ${card.creditLimit.toLocaleString("sv-SE")} kr`,
                                "Global acceptans via Mastercard-nätverket",
                                "Full kontroll och korthantering i Omega Banks mobilapp"
                            ].map((item, i) => (
                                <li key={i} className="flex items-start gap-4 text-slate-600 text-sm font-light leading-relaxed">
                                    <div className="w-1 h-1 bg-red-600 rounded-full mt-2 shrink-0" />
                                    {item}
                                </li>
                            ))}
                        </ul>
                    </div>

                    <div className="bg-slate-50 p-12 rounded-sm border border-slate-100 flex flex-col justify-center">
                        <h3 className="text-sm font-bold uppercase tracking-widest text-slate-900 mb-4">Behöver du vägledning?</h3>
                        <p className="text-sm text-slate-500 font-light mb-8 leading-relaxed">
                            Är du osäker på vilket kort som bäst passar din livsstil? Våra rådgivare finns tillgängliga för att hjälpa dig välja rätt nivå av förmåner och kreditutrymme.
                        </p>
                        <div className="space-y-4">
                            <Link href="/kundservice" className="block text-center border border-slate-200 bg-white py-4 text-[10px] font-bold uppercase tracking-widest hover:bg-slate-50 transition-colors">
                                Kontakta rådgivare
                            </Link>
                            <Link href="/creditcardtemplates" className="block text-center py-4 text-[10px] font-bold uppercase tracking-widest text-slate-400 hover:text-slate-900 transition-colors">
                                Jämför våra kort
                            </Link>
                        </div>
                    </div>
                </div>
            </section>

            {/* ── FINAL CTA STRIP ── */}
            <section className="bg-slate-50 border-t border-slate-100 py-20 px-8 md:px-20 text-center">
                <div className="max-w-2xl mx-auto">
                    <h2 className="text-2xl font-light mb-4 uppercase tracking-widest text-slate-900">Redo att ansöka?</h2>
                    <p className="text-slate-500 font-light text-sm mb-10 leading-relaxed">
                        Logga in för att påbörja din ansökan.
                    </p>
                    <div className="flex flex-col sm:flex-row justify-center gap-4">
                        <Link
                            href="/mypages/creditcards/apply"
                            className="bg-red-600 hover:bg-red-700 text-white font-bold px-12 py-4 rounded-sm transition-all text-[11px] uppercase tracking-widest shadow-lg shadow-red-600/10"
                        >
                            Ansök nu
                        </Link>
                        <Link
                            href="/creditcardtemplates"
                            className="border border-slate-200 text-slate-600 font-bold px-12 py-4 rounded-sm hover:bg-white transition-all text-[11px] uppercase tracking-widest"
                        >
                            ← Gå tillbaka
                        </Link>
                    </div>

                    <div className="mt-16 pt-8 border-t border-slate-200">
                        <p className="text-[10px] text-slate-400 uppercase tracking-tight leading-relaxed font-light">
                            Effektiv ränta vid utnyttjad kredit om 20 000 kr är 14.5% (exempel). Kreditgivare är Omega Bank AB.
                            Vi tillämpar ansvarsfull kreditgivning under Finansinspektionens tillsyn.
                        </p>
                    </div>
                </div>
            </section>
        </main>
    );
}