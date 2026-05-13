// @/components/CreditCardTemplate.tsx
import { CreditCardTemplateDTO } from "@/types/creditcard";
import Link from "next/link";

interface CardAccent {
  bg: string;
  chip: string;
  glow: string;
}

const cardAccents: Record<string, CardAccent> = {
  standard: {
    bg: "from-[#1a1c20] to-[#0f1012]",
    chip: "bg-slate-400",
    glow: "from-slate-500/20",
  },
  gold: {
    bg: "from-[#a16d03] to-[#7a5202]",
    chip: "bg-[#f3e5ab]",
    glow: "from-amber-500/20",
  },
  platinum: {
    bg: "from-[#384c62] to-[#273544]",
    chip: "bg-[#e5e7eb]",
    glow: "from-blue-400/20",
  },
};

// Palette för okända korttyper — varje ny typ får en deterministisk färg
const dynamicPalette: CardAccent[] = [
  { bg: "from-[#1a3a5c] to-[#0f2240]", chip: "bg-sky-200",     glow: "from-sky-500/20"    }, // marinblå
  { bg: "from-[#2d1b69] to-[#1a0f3d]", chip: "bg-purple-200",  glow: "from-purple-500/20" }, // lila
  { bg: "from-[#1b4332] to-[#0f2b1e]", chip: "bg-emerald-200", glow: "from-emerald-500/20"}, // skogsgrönt
  { bg: "from-[#7f1d1d] to-[#4c0f0f]", chip: "bg-red-200",     glow: "from-red-500/20"    }, // djupröd
  { bg: "from-[#78350f] to-[#451f08]", chip: "bg-orange-200",  glow: "from-orange-500/20" }, // brons
  { bg: "from-[#134e4a] to-[#0b3330]", chip: "bg-teal-200",    glow: "from-teal-500/20"   }, // teal
  { bg: "from-[#3b0764] to-[#220444]", chip: "bg-fuchsia-200", glow: "from-fuchsia-500/20"}, // magenta
  { bg: "from-[#1e3a5f] to-[#0e2040]", chip: "bg-indigo-200",  glow: "from-indigo-500/20" }, // indigo
];

// Enkel hash → index i paletten, deterministisk per korttypsnamn
function hashString(str: string): number {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = (hash * 31 + str.charCodeAt(i)) >>> 0;
  }
  return hash;
}

function getAccent(type: string): CardAccent {
  const known = cardAccents[type];
  if (known) return known;
  return dynamicPalette[hashString(type) % dynamicPalette.length];
}

interface Props {
  card: CreditCardTemplateDTO;
}

export default function CreditCardBox({ card }: Props) {
  if (!card) return null;
  const type = card.creditCardType.toLowerCase();
  const accent = getAccent(type);

  return (
      <div className="group flex flex-col gap-6 w-full max-w-[360px] mx-auto">
        {/* KORTET */}
        <div
            className={`
          relative aspect-[1.58/1] w-full rounded-[24px] p-9 overflow-hidden
          transition-all duration-500 ease-out group-hover:-translate-y-2
          bg-gradient-to-br ${accent.bg} text-white shadow-2xl
        `}
        >
          {/* Ljusinsläpp */}
          <div className="absolute inset-0 bg-gradient-to-tr from-white/[0.04] to-transparent pointer-events-none" />
          <div className="relative h-full flex flex-col justify-between">
            {/* TOPP */}
            <div className="flex justify-between items-start">
            <span className="text-[11px] font-medium tracking-[0.4em] opacity-40 uppercase">
              OMEGA BANK
            </span>
              <div className={`w-11 h-8 ${accent.chip} rounded-md opacity-80 shadow-inner`} />
            </div>
            {/* MITTEN: prickar */}
            <div className="flex gap-4 text-[16px] tracking-[0.25em] font-light opacity-70">
              <span>•••• </span> <span>•••• </span> <span>•••• </span> <span>••••</span>
            </div>
            {/* BOTTEN */}
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
              <span className="text-sm font-semibold text-slate-800">{card.creditLimit?.toLocaleString("sv-SE")} kr</span>
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