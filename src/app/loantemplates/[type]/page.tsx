import { LoanTemplateDTO } from "@/types/loan";
import Link from "next/link";

async function getLoanData(type: string): Promise<LoanTemplateDTO | null> {
    const res = await fetch("http://localhost:8080/template/loans", { cache: "no-store" });
    if (!res.ok) return null;

    const loans: LoanTemplateDTO[] = await res.json();
    const decodedType = decodeURIComponent(type).toLowerCase();

    return loans.find((l) => l.loanType.toLowerCase() === decodedType) || null;
}

// Icons per loan type
function LoanIcon({ type }: { type: string }) {
    if (type === "mortgage") {
        return (
            <svg viewBox="0 0 24 24" fill="none" className="w-10 h-10" xmlns="http://www.w3.org/2000/svg">
                <path
                    d="M3 12L12 4L21 12V20C21 20.55 20.55 21 20 21H15V15H9V21H4C3.45 21 3 20.55 3 20V12Z"
                    stroke="currentColor" strokeWidth="1.8" strokeLinejoin="round"
                />
            </svg>
        );
    }
    return (
        <svg viewBox="0 0 24 24" fill="none" className="w-10 h-10" xmlns="http://www.w3.org/2000/svg">
            <rect x="2" y="5" width="20" height="14" rx="2" stroke="currentColor" strokeWidth="1.8"/>
            <circle cx="8" cy="12" r="2.5" stroke="currentColor" strokeWidth="1.5"/>
            <path d="M13 9H19M13 12H17M13 15H15" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round"/>
        </svg>
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
            <main className="min-h-screen bg-white flex flex-col items-center justify-center text-gray-900 px-8">
                <h1 className="text-2xl font-bold mb-2">
                    Lånet &quot;{resolvedParams.type}&quot; hittades inte.
                </h1>
                <p className="text-gray-500 mb-6">Kontrollera att namnet stämmer i URL:en.</p>
                <Link
                    href="/"
                    className="bg-red-600 text-white font-bold px-5 py-2.5 rounded hover:bg-red-700 transition text-sm"
                >
                    ← Tillbaka till startsidan
                </Link>
            </main>
        );
    }

    const isMortgage = resolvedParams.type === "mortgage";

    return (
        <main className="min-h-screen bg-white text-gray-900">

            {/* ── HERO BANNER ── */}
            <section className="grid md:grid-cols-2 border-b border-gray-100">
                <div className="flex flex-col justify-center px-12 md:px-20 py-16">
                    {/* Breadcrumb */}
                    <div className="flex items-center gap-2 text-sm text-gray-400 mb-6">
                        <Link href="/" className="hover:text-gray-700 transition">Hem</Link>
                        <span>/</span>
                        <span className="text-gray-700 font-medium">{loan.loanType}</span>
                    </div>

                    {/* Icon + title */}
                    <div className="flex items-center gap-4 mb-4 text-gray-900">
                        <LoanIcon type={resolvedParams.type} />
                        <h1 className="text-4xl md:text-5xl font-extrabold leading-tight tracking-tight">
                            {loan.loanType}
                        </h1>
                    </div>

                    <p className="text-gray-600 text-lg max-w-md mb-8 leading-relaxed">
                        {loan.description}
                    </p>

                    <Link
                        href="/mypages/loans/apply"
                        className="inline-block w-fit bg-red-600 hover:bg-red-700 text-white font-bold px-6 py-3 rounded transition-colors text-sm"
                    >
                        Ansök nu
                    </Link>
                </div>

                {/* Right – decorative */}
                <div className="relative bg-gray-50 min-h-[300px] overflow-hidden hidden md:block">
                    <div
                        className={`absolute inset-0 ${
                            isMortgage
                                ? "bg-gradient-to-br from-amber-50 via-orange-50 to-yellow-50"
                                : "bg-gradient-to-br from-blue-50 via-indigo-50 to-sky-50"
                        }`}
                    />
                    {isMortgage ? (
                        <svg viewBox="0 0 500 300" className="absolute inset-0 w-full h-full" xmlns="http://www.w3.org/2000/svg">
                            <rect x="150" y="130" width="200" height="140" rx="4" fill="#d97706" opacity="0.15"/>
                            <polygon points="250,60 360,130 140,130" fill="#b45309" opacity="0.12"/>
                            <rect x="215" y="190" width="70" height="80" rx="3" fill="#fde68a" opacity="0.6"/>
                            <rect x="230" y="165" width="12" height="25" rx="2" fill="#f59e0b" opacity="0.5"/>
                        </svg>
                    ) : (
                        <svg viewBox="0 0 500 300" className="absolute inset-0 w-full h-full" xmlns="http://www.w3.org/2000/svg">
                            <rect x="120" y="90" width="260" height="160" rx="12" fill="#3b82f6" opacity="0.1"/>
                            <rect x="120" y="90" width="260" height="45" rx="12" fill="#3b82f6" opacity="0.12"/>
                            <rect x="145" y="175" width="50" height="18" rx="4" fill="#6366f1" opacity="0.25"/>
                            <circle cx="350" cy="110" r="18" fill="#818cf8" opacity="0.3"/>
                        </svg>
                    )}
                </div>
            </section>

            {/* ── STATS CARDS ── */}
            <section className="px-12 md:px-20 py-12 bg-white">
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-6 max-w-3xl">

                    <div className="border border-gray-200 rounded-xl p-6">
                        <p className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-1">Ränta</p>
                        <p className="text-4xl font-extrabold text-gray-900">
                            {loan.interestRate}
                            <span className="text-xl font-bold text-gray-500">%</span>
                        </p>
                        <p className="text-xs text-gray-400 mt-1">nominell årsränta</p>
                    </div>

                    <div className="border border-gray-200 rounded-xl p-6">
                        <p className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-1">Lägsta belopp</p>
                        <p className="text-4xl font-extrabold text-gray-900">
                            {loan.minAmount.toLocaleString("sv-SE")}
                            <span className="text-xl font-bold text-gray-500"> kr</span>
                        </p>
                        <p className="text-xs text-gray-400 mt-1">minsta lånebelopp</p>
                    </div>

                    <div className="border border-gray-200 rounded-xl p-6">
                        <p className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-1">Högsta belopp</p>
                        <p className="text-4xl font-extrabold text-gray-900">
                            {loan.maxAmount.toLocaleString("sv-SE")}
                            <span className="text-xl font-bold text-gray-500"> kr</span>
                        </p>
                        <p className="text-xs text-gray-400 mt-1">högsta lånebelopp</p>
                    </div>

                </div>
            </section>

            {/* ── INFO SECTION ── */}
            <section className="px-12 md:px-20 py-10 bg-gray-50 border-t border-gray-100">
                <div className="max-w-2xl">
                    <h2 className="text-xl font-extrabold mb-4">Så fungerar {loan.loanType.toLowerCase()}</h2>
                    <p className="text-gray-600 leading-relaxed mb-6">
                        {loan.description} Vi erbjuder konkurrenskraftiga räntor och flexibla villkor
                        som passar din ekonomiska situation. Kontakta oss om du har frågor eller vill
                        veta mer om vad som gäller just för dig.
                    </p>

                    <ul className="space-y-3">
                        {[
                            `Ränta från ${loan.interestRate}% per år`,
                            `Låna mellan ${loan.minAmount.toLocaleString("sv-SE")} – ${loan.maxAmount.toLocaleString("sv-SE")} kr`,
                            "Snabb handläggning och svar",
                            "Personlig rådgivning utan extra kostnad",
                        ].map((item) => (
                            <li key={item} className="flex items-start gap-3 text-gray-700 text-sm">
                <span className="mt-0.5 flex-shrink-0 w-5 h-5 bg-red-600 rounded-full flex items-center justify-center">
                  <svg viewBox="0 0 12 10" fill="none" className="w-3 h-3">
                    <path d="M1 5L4.5 8.5L11 1.5" stroke="white" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>
                  </svg>
                </span>
                                {item}
                            </li>
                        ))}
                    </ul>
                </div>
            </section>

            {/* ── CTA ── */}
            <section id="ansok" className="px-12 md:px-20 py-14 bg-red-600 text-white">
                <div className="max-w-xl">
                    <h2 className="text-2xl font-extrabold mb-2">Redo att ansöka?</h2>
                    <p className="text-red-100 text-sm mb-6">
                        Logga in med ditt Google-konto för att skicka in din låneansökan.
                        Det tar bara några minuter.
                    </p>
                    <div className="flex gap-3 flex-wrap">
                        <Link
                            href="/mypages/loans/apply"
                            className="bg-white text-red-600 font-bold px-5 py-2.5 rounded hover:bg-red-50 transition text-sm"
                        >
                            Ansök
                        </Link>
                        <Link
                            href="/"
                            className="border border-white text-white font-bold px-5 py-2.5 rounded hover:bg-red-700 transition text-sm"
                        >
                            ← Tillbaka
                        </Link>
                    </div>
                </div>
            </section>

        </main>
    );
}