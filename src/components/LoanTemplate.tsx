import { LoanTemplateDTO } from "@/types/loan";
import Link from "next/link";

function LoanIcon({ type, className }: { type: string; className?: string }) {
  if (type.toLowerCase().includes("mortgage") || type.toLowerCase().includes("bolån")) {
    return (
        <svg viewBox="0 0 24 24" fill="none" className={className} xmlns="http://www.w3.org/2000/svg">
          <path
              d="M3 12L12 4L21 12V20C21 20.55 20.55 21 20 21H15V15H9V21H4C3.45 21 3 20.55 3 20V12Z"
              stroke="currentColor" strokeWidth="1.8" strokeLinejoin="round"
          />
        </svg>
    );
  }
  return (
      <svg viewBox="0 0 24 24" fill="none" className={className} xmlns="http://www.w3.org/2000/svg">
        <rect x="2" y="5" width="20" height="14" rx="2" stroke="currentColor" strokeWidth="1.8"/>
        <circle cx="8" cy="12" r="2.5" stroke="currentColor" strokeWidth="1.5"/>
        <path d="M13 9H19M13 12H17M13 15H15" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round"/>
      </svg>
  );
}

const IconChart = () => (
    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="w-4 h-4">
      <path strokeLinecap="round" strokeLinejoin="round" d="M3 13.125C3 12.504 3.504 12 4.125 12h2.25c.621 0 1.125.504 1.125 1.125v6.75C7.5 20.496 6.996 21 6.375 21h-2.25A1.125 1.125 0 013 19.875v-6.75zM9.75 8.625c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125v11.25c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V8.625zM16.5 4.125c0-.621.504-1.125 1.125-1.125h2.25C20.496 3 21 3.504 21 4.125v15.75c0 .621-.504 1.125-1.125 1.125h-2.25a1.125 1.125 0 01-1.125-1.125V4.125z" />
    </svg>
);

interface Props {
  loan: LoanTemplateDTO;
}

export default function LoanBox({ loan }: Props) {
  if (!loan) return null;

  const loanUrl = loan.loanType.toLowerCase();
  const isMortgage = loanUrl.includes("mortgage") || loanUrl.includes("bolån");

  return (
      <div className="group flex flex-col gap-5 w-full max-w-[350px] mx-auto">
        {/* Produktkortet */}
        <div className="relative overflow-hidden bg-white border border-gray-100 rounded-[28px] p-8 shadow-sm transition-all duration-500 hover:shadow-2xl hover:-translate-y-2">

          {/* Dekorativ glow-effekt */}
          <div className={`absolute -right-4 -top-4 w-28 h-28 rounded-full blur-3xl opacity-20 ${isMortgage ? 'bg-amber-500' : 'bg-blue-500'}`} />

          <div className="relative flex flex-col gap-6">
            {/* Header: Ikon från detaljvyn & Status */}
            <div className="flex justify-between items-center">
              <div className={`p-3 rounded-2xl ${isMortgage ? 'bg-amber-50 text-amber-600' : 'bg-blue-50 text-blue-600'}`}>
                <LoanIcon type={loanUrl} className="w-8 h-8" />
              </div>
            </div>

            {/* Titel & Beskrivning */}
            <div>
              <h2 className="text-2xl font-black text-gray-900 tracking-tight leading-none">{loan.loanType}</h2>
              <p className="text-gray-400 text-[13px] mt-2 leading-relaxed line-clamp-2 italic">
                {loan.description}
              </p>
            </div>

            {/* Belopps-box */}
            <div className="bg-gray-50/50 rounded-2xl p-4 border border-gray-50">
              <span className="text-[10px] uppercase text-gray-400 font-bold tracking-widest block mb-1">Högsta belopp</span>
              <div className="flex items-baseline gap-1">
                <span className="text-3xl font-black text-gray-900">{loan.maxAmount.toLocaleString('sv-SE')}</span>
                <span className="text-sm font-bold text-gray-800">kr</span>
              </div>
            </div>

            {/* Specifikationer */}
            <div className="grid grid-cols-2 gap-4">
              <div className="flex items-center gap-3">
                <div className="text-gray-300">
                  <IconChart />
                </div>
                <div className="flex flex-col">
                  <span className="text-[9px] text-gray-400 font-bold uppercase tracking-tighter">Ränta</span>
                  <span className="text-[15px] font-bold text-gray-800">{loan.interestRate}%</span>
                </div>
              </div>
              <div className="flex flex-col border-l border-gray-100 pl-4">
                <span className="text-[9px] text-gray-400 font-bold uppercase tracking-tighter">Minsta belopp</span>
                <span className="text-[15px] font-bold text-gray-800">{loan.minAmount.toLocaleString('sv-SE')} kr</span>
              </div>
            </div>
          </div>
        </div>

        {/* Knapp */}
        <Link
            href={`/loantemplates/${loanUrl}`}
            className="w-full py-4 bg-black text-white font-bold text-[11px] uppercase tracking-[0.3em] transition-all hover:bg-zinc-800 active:scale-[0.98] rounded-2xl shadow-lg shadow-black/10 flex items-center justify-center"
        >
          Se detaljer
        </Link>
      </div>
  );
}