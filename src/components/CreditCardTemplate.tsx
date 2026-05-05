import { CreditCardTemplateDTO } from "@/types/creditcard";
import Link from "next/link";

const cardAccents: Record<string, { bg: string; chip: string; circle: string }> = {
  standard: {
    bg: "from-[#1a1c20] to-[#0f1012]",
    chip: "bg-[#d4af37]",
    circle: "bg-white/5"
  },
  gold: {
    bg: "from-[#a16d03] to-[#7a5202]",
    chip: "bg-[#f3e5ab]",
    circle: "bg-white/10"
  },
  platinum: {
    bg: "from-[#384c62] to-[#273544]",
    chip: "bg-[#e5e7eb]",
    circle: "bg-white/10"
  },
};

interface Props {
  card: CreditCardTemplateDTO;
}

export default function CreditCardBox({ card }: Props) {
  if (!card) return null;

  // Identifiera typen för färg och URL
  const type = card.creditCardType.toLowerCase();
  const accent = cardAccents[type] || cardAccents.standard;

  return (
      <div className="group flex flex-col gap-6 w-full max-w-[360px] mx-auto">
        {/* Kort-visualisering */}
        <div
            className={`
          relative aspect-[1.58/1] w-full rounded-[20px] p-8 shadow-2xl overflow-hidden
          transition-all duration-500 ease-out group-hover:-translate-y-2
          bg-gradient-to-br ${accent.bg} text-white
        `}
        >
          {/* Design-element (cirklarna) */}
          <div className={`absolute top-[-20%] right-[-10%] w-[70%] h-[120%] ${accent.circle} rounded-full blur-2xl`} />
          <div className="absolute bottom-[-20%] right-[10%] w-[50%] h-[80%] bg-black/20 rounded-full blur-xl" />

          <div className="relative h-full flex flex-col justify-between">
            <div className="flex justify-between items-start">
            <span className="text-[13px] font-bold tracking-[0.15em] uppercase opacity-90">
              OMEGA BANK
            </span>
              <span className="text-[15px] font-bold tracking-tight opacity-90 uppercase">
              {card.creditCardType}
            </span>
            </div>

            <div className={`w-12 h-9 ${accent.chip} rounded-lg shadow-inner mt-2 opacity-90`} />

            <div className="flex justify-between items-end">
              <div className="flex gap-2 text-[18px] tracking-[0.25em] font-bold opacity-70">
                <span>....</span> <span>....</span> <span>....</span> <span>....</span>
              </div>

              <div className="flex -space-x-3 mb-1">
                <div className="w-8 h-8 rounded-full bg-[#eb001b] opacity-90" />
                <div className="w-8 h-8 rounded-full bg-[#f79e1b] opacity-90" />
              </div>
            </div>
          </div>
        </div>

        {/* Detaljerad Info under kortet */}
        <div className="px-1 flex flex-col gap-5">
          <div className="grid grid-cols-2 gap-y-4 border-b border-gray-100 pb-5">
            <div className="flex flex-col">
              <span className="text-[10px] uppercase text-gray-400 font-bold tracking-wider">Limit</span>
              <span className="text-sm font-semibold text-gray-800">{card.creditLimit?.toLocaleString('sv-SE')} kr</span>
            </div>
            <div className="flex flex-col items-end">
              <span className="text-[10px] uppercase text-gray-400 font-bold tracking-wider">Ränta</span>
              <span className="text-sm font-semibold text-gray-800">{card.interestRate}%</span>
            </div>
            <div className="flex flex-col">
              <span className="text-[10px] uppercase text-gray-400 font-bold tracking-wider">Avgift</span>
              <span className="text-sm font-semibold text-gray-800">{card.fee} kr</span>
            </div>
            <div className="flex flex-col items-end">
              <span className="text-[10px] uppercase text-gray-400 font-bold tracking-wider">Typ</span>
              <span className="text-sm font-semibold text-gray-800 capitalize">{card.creditCardType}</span>
            </div>
          </div>

          {/* Navigeringsknapp som länkar till /[type] */}
          <Link
              href={`/creditcardtemplates/${type}`}
              className="w-full py-4 bg-black text-white font-bold text-[11px] uppercase tracking-[0.3em] transition-all hover:bg-zinc-800 active:scale-[0.97] rounded-xl shadow-lg shadow-black/10 flex items-center justify-center"
          >
            VÄLJ
          </Link>
        </div>
      </div>
  );
}