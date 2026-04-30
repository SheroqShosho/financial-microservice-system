import Link from "next/link";

export default function Home() {
  return (
      <div className="min-h-screen bg-white text-gray-900">

        {/* ── HERO ── */}
        <section className="grid md:grid-cols-2">
          {/* Left */}
          <div className="flex flex-col justify-center px-12 md:px-20 py-20">
            <h1 className="text-5xl md:text-6xl font-extrabold leading-tight tracking-tight mb-6">
              Välj ett<br />förhandlings-<br />fritt bolån
            </h1>
            <p className="text-gray-700 text-lg mb-8 max-w-sm leading-relaxed">
              Räkna på vilken ränta vi kan erbjuda dig – och se din bästa ränta direkt.
            </p>
            <Link
                href="/loantemplates"
                className="inline-block w-fit bg-red-600 hover:bg-red-700 text-white font-bold px-6 py-3 rounded transition-colors text-sm"
            >
              Räkna på vilken ränta du kan få
            </Link>
          </div>

          {/* Right – warm image placeholder */}
          <div className="relative bg-amber-50 min-h-[380px] overflow-hidden">
            <div className="absolute inset-0 bg-gradient-to-br from-amber-100 via-orange-50 to-yellow-100" />
            <svg
                viewBox="0 0 500 380"
                className="absolute inset-0 w-full h-full"
                xmlns="http://www.w3.org/2000/svg"
            >
              <rect x="80"  y="180" width="160" height="130" rx="4" fill="#d97706" opacity="0.3"/>
              <rect x="100" y="160" width="120" height="150" rx="4" fill="#b45309" opacity="0.25"/>
              <rect x="260" y="200" width="140" height="110" rx="4" fill="#d97706" opacity="0.3"/>
              <rect x="280" y="170" width="100" height="140" rx="4" fill="#92400e" opacity="0.2"/>
              <circle cx="330" cy="155" r="28" fill="#fde68a" opacity="0.7"/>
              <rect x="310" y="183" width="40" height="60" rx="8" fill="#fbbf24" opacity="0.5"/>
            </svg>
            <p className="absolute bottom-3 right-4 text-xs text-amber-400 opacity-40 italic">Illustrationsbild</p>
          </div>
        </section>

        {/* ── PRODUCT CARDS ── */}
        <section className="px-10 md:px-20 py-16">
          <div className="flex flex-wrap gap-4">

            {/* Bolån */}
            <Link
                href="/loantemplates/mortgage"
                className="flex flex-col items-center justify-center gap-3 border border-gray-200 hover:border-gray-400 hover:shadow-md transition rounded-lg p-8 w-44 cursor-pointer group"
            >
              <svg viewBox="0 0 24 24" fill="none" className="w-10 h-10" xmlns="http://www.w3.org/2000/svg">
                <path
                    d="M3 12L12 4L21 12V20C21 20.55 20.55 21 20 21H15V15H9V21H4C3.45 21 3 20.55 3 20V12Z"
                    stroke="#111" strokeWidth="1.8" strokeLinejoin="round"
                />
              </svg>
              <span className="font-semibold text-gray-900 text-sm group-hover:text-red-600 transition-colors">Bolån</span>
            </Link>

            {/* Privatlån */}
            <Link
                href="/loantemplates/private"
                className="flex flex-col items-center justify-center gap-3 border border-gray-200 hover:border-gray-400 hover:shadow-md transition rounded-lg p-8 w-44 cursor-pointer group"
            >
              <svg viewBox="0 0 24 24" fill="none" className="w-10 h-10" xmlns="http://www.w3.org/2000/svg">
                <rect x="2" y="5" width="20" height="14" rx="2" stroke="#111" strokeWidth="1.8"/>
                <circle cx="8" cy="12" r="2.5" stroke="#111" strokeWidth="1.5"/>
                <path d="M13 9H19M13 12H17M13 15H15" stroke="#111" strokeWidth="1.4" strokeLinecap="round"/>
              </svg>
              <span className="font-semibold text-gray-900 text-sm group-hover:text-red-600 transition-colors">Privatlån</span>
            </Link>

            {/* Kreditkort */}
            <Link
                href="/creditcardtemplates"
                className="flex flex-col items-center justify-center gap-3 border border-gray-200 hover:border-gray-400 hover:shadow-md transition rounded-lg p-8 w-44 cursor-pointer group"
            >
              <svg viewBox="0 0 24 24" fill="none" className="w-10 h-10" xmlns="http://www.w3.org/2000/svg">
                <rect x="2" y="5" width="20" height="14" rx="2" stroke="#111" strokeWidth="1.8"/>
                <path d="M2 10H22" stroke="#111" strokeWidth="1.8"/>
                <rect x="5" y="14" width="5" height="2" rx="0.5" fill="#111"/>
                <rect x="12" y="14" width="3" height="2" rx="0.5" fill="#111"/>
              </svg>
              <span className="font-semibold text-gray-900 text-sm group-hover:text-red-600 transition-colors">Kreditkort</span>
            </Link>


          </div>
        </section>

      </div>
  );
}