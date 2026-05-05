import { CreditCardTemplateDTO } from "@/types/creditcard";
import Link from "next/link";

async function getCardData(type: string): Promise<CreditCardTemplateDTO | null> {
    const res = await fetch("http://localhost:8080/template/creditcards", { cache: "no-store" });
    if (!res.ok) return null;
    const cards: CreditCardTemplateDTO[] = await res.json();
    const decodedType = decodeURIComponent(type).toLowerCase();
    return cards.find((c) => c.creditCardType.toLowerCase() === decodedType) || null;
}

const cardAccents: Record<string, { bg: string; chip: string; gradientFrom: string; gradientTo: string }> = {
    standard: { bg: "from-gray-800 to-gray-950", chip: "bg-yellow-400", gradientFrom: "from-gray-50", gradientTo: "to-gray-100" },
    gold:     { bg: "from-yellow-600 to-yellow-900", chip: "bg-yellow-200", gradientFrom: "from-yellow-50", gradientTo: "to-amber-100" },
    platinum: { bg: "from-slate-500 to-slate-800",   chip: "bg-slate-200",  gradientFrom: "from-slate-50",  gradientTo: "to-slate-100" },
};

export default async function CardDetailPage({ params }: { params: Promise<{ type: string }> }) {
    const resolvedParams = await params;
    const card = await getCardData(resolvedParams.type);
    const accent = cardAccents[resolvedParams.type] ?? cardAccents["standard"];

    if (!card) {
        return (
            <main className="min-h-screen bg-white flex flex-col items-center justify-center text-gray-900 px-8">
                <h1 className="text-2xl font-bold mb-2">Kortet &quot;{resolvedParams.type}&quot; hittades inte.</h1>
                <p className="text-gray-500 mb-6">Kontrollera att namnet stämmer i URL:en.</p>
                <Link href="/creditcardtemplates" className="bg-red-600 text-white font-bold px-5 py-2.5 rounded hover:bg-red-700 transition text-sm">
                    ← Tillbaka till kreditkort
                </Link>
            </main>
        );
    }

    return (
        <main className="min-h-screen bg-white text-gray-900">

            {/* Hero */}
            <section className={`grid md:grid-cols-2 border-b border-gray-100`}>
                <div className="flex flex-col justify-center px-12 md:px-20 py-16">
                    <div className="flex items-center gap-2 text-sm text-gray-400 mb-6">
                        <Link href="/" className="hover:text-gray-700 transition">Hem</Link>
                        <span>/</span>
                        <Link href="/creditcardtemplates" className="hover:text-gray-700 transition">Kreditkort</Link>
                        <span>/</span>
                        <span className="text-gray-700 font-medium">{card.creditCardType}</span>
                    </div>

                    <h1 className="text-4xl md:text-5xl font-extrabold leading-tight tracking-tight mb-4">
                        {card.creditCardType}
                    </h1>
                    <p className="text-gray-600 text-lg max-w-md mb-8 leading-relaxed">{card.description}</p>

                    <Link
                        href="/mypages/creditcards/apply"
                        className="inline-block w-fit bg-red-600 hover:bg-red-700 text-white font-bold px-6 py-3 rounded transition-colors text-sm"
                    >
                        Ansök nu
                    </Link>
                </div>

                {/* Right – card visual */}
                <div className={`relative bg-gradient-to-br ${accent.gradientFrom} ${accent.gradientTo} min-h-[300px] flex items-center justify-center hidden md:flex`}>
                    <div className={`relative w-72 h-44 rounded-2xl bg-gradient-to-br ${accent.bg} p-6 shadow-2xl overflow-hidden`}>
                        <div className="absolute -right-8 -top-8 w-40 h-40 rounded-full bg-white opacity-5" />
                        <div className="absolute -right-4 bottom-4 w-24 h-24 rounded-full bg-white opacity-5" />
                        <div className="flex justify-between items-start">
                            <span className="text-white/60 text-xs font-semibold tracking-widest uppercase">Omega Bank</span>
                            <span className="text-white text-xs font-bold tracking-wider">{card.creditCardType}</span>
                        </div>
                        <div className={`mt-4 w-10 h-7 rounded-md ${accent.chip} opacity-90`} />
                        <div className="absolute bottom-5 left-6 right-6 flex justify-between items-end">
                            <span className="text-white/80 text-xs font-mono tracking-widest">•••• •••• •••• ••••</span>
                            <svg viewBox="0 0 38 24" className="w-10 opacity-70" fill="none">
                                <circle cx="15" cy="12" r="10" fill="#eb001b" fillOpacity="0.8" />
                                <circle cx="23" cy="12" r="10" fill="#f79e1b" fillOpacity="0.8" />
                            </svg>
                        </div>
                    </div>
                </div>
            </section>

            {/* Stats */}
            <section className="px-12 md:px-20 py-12 bg-white">
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-6 max-w-3xl">
                    <div className="border border-gray-200 rounded-xl p-6">
                        <p className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-1">Ränta</p>
                        <p className="text-4xl font-extrabold text-gray-900">{card.interestRate}<span className="text-xl font-bold text-gray-500">%</span></p>
                        <p className="text-xs text-gray-400 mt-1">nominell årsränta</p>
                    </div>
                    <div className="border border-gray-200 rounded-xl p-6">
                        <p className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-1">Årsavgift</p>
                        <p className="text-4xl font-extrabold text-gray-900">{card.fee}<span className="text-xl font-bold text-gray-500"> kr</span></p>
                        <p className="text-xs text-gray-400 mt-1">per år</p>
                    </div>
                    <div className="border border-gray-200 rounded-xl p-6">
                        <p className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-1">Kreditgräns</p>
                        <p className="text-4xl font-extrabold text-gray-900">{card.creditLimit.toLocaleString("sv-SE")}<span className="text-xl font-bold text-gray-500"> kr</span></p>
                        <p className="text-xs text-gray-400 mt-1">max kreditgräns</p>
                    </div>
                </div>
            </section>

            {/* Info */}
            <section className="px-12 md:px-20 py-10 bg-gray-50 border-t border-gray-100">
                <div className="max-w-2xl">
                    <h2 className="text-xl font-extrabold mb-4">Det här ingår</h2>
                    <ul className="space-y-3">
                        {[
                            `Ränta ${card.interestRate}% per år`,
                            `Årsavgift ${card.fee} kr`,
                            `Kreditgräns upp till ${card.creditLimit.toLocaleString("sv-SE")} kr`,
                            "Kontaktlös betalning ingår",
                            "Digital korthantering i appen",
                        ].map((item) => (
                            <li key={item} className="flex items-start gap-3 text-gray-700 text-sm">
                <span className="mt-0.5 flex-shrink-0 w-5 h-5 bg-red-600 rounded-full flex items-center justify-center">
                  <svg viewBox="0 0 12 10" fill="none" className="w-3 h-3">
                    <path d="M1 5L4.5 8.5L11 1.5" stroke="white" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
                  </svg>
                </span>
                                {item}
                            </li>
                        ))}
                    </ul>
                </div>
            </section>

            {/* Bottom CTA strip */}
            <section className="px-10 md:px-20 py-12 bg-gray-950 text-white">
                <div className="max-w-2xl flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
                    <div>
                        <h2 className="text-xl font-extrabold mb-1">Redo att ansöka?</h2>
                        <p className="text-gray-400 text-sm">Logga in med ditt Google-konto för att skicka in din kreditkortansökan på under fem minuter.</p>
                    </div>

                    {/* Knapp-grupp som matchar lån-sektionen */}
                    <div className="flex gap-3 flex-shrink-0">
                        <a
                            href="/mypages/creditcards/apply"
                            className="bg-red-600 hover:bg-red-700 text-white font-bold px-6 py-3 rounded transition-colors text-sm"
                        >
                            Ansök nu
                        </a>
                        <Link
                            href="/creditcardtemplates"
                            className="border border-white/20 text-white font-bold px-6 py-3 rounded hover:bg-white/10 transition-colors text-sm"
                        >
                            ← Tillbaka
                        </Link>
                    </div>
                </div>
            </section>
        </main>
    );
}