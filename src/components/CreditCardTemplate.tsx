// @/components/CreditCardTemplate.tsx
import { CreditCardTemplateDTO } from "@/types/creditcard";
import Link from "next/link";

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

interface Props {
  card: CreditCardTemplateDTO;
}

export default function CreditCardBox({ card }: Props) {
  if (!card) return null;

  const type = card.creditCardType.toLowerCase();
  const accent = cardAccents[type] || cardAccents.standard;

  return (
      <div className="group flex flex-col gap-6 w-full max-w-[360px] mx-auto">
        {/* KORTET - Återställd till full storlek och rymligare padding */}
        <div
            className={`
          relative aspect-[1.58/1] w-full rounded-[24px] p-9 overflow-hidden
          transition-all duration-500 ease-out group-hover:-translate-y-2
          bg-gradient-to-br ${accent.bg} text-white shadow-2xl
        `}
        >
          {/* Ljusinsläpp för exklusiv känsla */}
          <div className="absolute inset-0 bg-gradient-to-tr from-white/[0.04] to-transparent pointer-events-none" />

          <div className="relative h-full flex flex-col justify-between">
            {/* TOPP: Layout som i din mål-bild */}
            <div className="flex justify-between items-start">
            <span className="text-[11px] font-medium tracking-[0.4em] opacity-40 uppercase">
              OMEGA BANK
            </span>
              <div className={`w-11 h-8 ${accent.chip} rounded-md opacity-80 shadow-inner`} />
            </div>

            {/* MITTEN: Tunna, eleganta prickar */}
            <div className="flex gap-4 text-[16px] tracking-[0.25em] font-light opacity-70">
              <span>••••</span> <span>••••</span> <span>••••</span> <span>••••</span>
            </div>

            {/* BOTTEN: Namn till vänster, Mastercard till höger */}
            <div className="flex justify-between items-end">
              <div className="flex flex-col">
                <span className="text-[7px] uppercase opacity-30 font-bold tracking-[0.25em] mb-1">Card Holder</span>
                <span className="text-[14px] font-light tracking-[0.15em] uppercase opacity-90">
                VALUED MEMBER
              </span>
              </div>

              {/* Mastercard Logo */}
              <div className="flex -space-x-3 mb-1 opacity-90">
                <div className="w-8 h-8 rounded-full bg-[#eb001b]" />
                <div className="w-8 h-8 rounded-full bg-[#f79e1b] opacity-80" />
              </div>
            </div>
          </div>
        </div>

        {/* INFO UNDER KORTET */}
        <div className="px-1 flex flex-col gap-5">
          <div className="grid grid-cols-2 gap-y-4 border-b border-slate-100 pb-5">
            <div className="flex flex-col">
              <span className="text-[10px] uppercase text-slate-400 font-bold tracking-widest">Limit</span>
              <span className="text-sm font-semibold text-slate-800">{card.creditLimit?.toLocaleString('sv-SE')} kr</span>
            </div>
            <div className="flex flex-col items-end">
              <span className="text-[10px] uppercase text-slate-400 font-bold tracking-widest">Ränta</span>
              <span className="text-sm font-semibold text-slate-800">{card.interestRate}%</span>
            </div>
            <div className="flex flex-col">
              <span className="text-[10px] uppercase text-slate-400 font-bold tracking-widest">Avgift</span>
              <span className="text-sm font-semibold text-slate-800">{card.fee} kr</span>
            </div>
            <div className="flex flex-col items-end">
              <span className="text-[10px] uppercase text-slate-400 font-bold tracking-widest">Nivå</span>
              <span className="text-sm font-semibold text-slate-800 capitalize">{card.creditCardType}</span>
            </div>
          </div>

          <Link
              href={`/creditcardtemplates/${type}`}
              className="w-full py-4 bg-[#0f172a] text-white font-bold text-[11px] uppercase tracking-[0.3em] transition-all hover:bg-slate-800 active:scale-[0.97] rounded-xl shadow-lg flex items-center justify-center"
          >
            VÄLJ
          </Link>
        </div>
      </div>
  );
}