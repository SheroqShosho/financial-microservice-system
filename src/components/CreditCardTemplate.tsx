import { CreditCardTemplateDTO } from "@/types/creditcard";
import Link from "next/link";

interface Props {
  card: CreditCardTemplateDTO;
}

export default function CreditCardBox({ card }: Props) {
  const cardUrl = card.creditCardType.toLowerCase(); 
  return (

    <Link href = {`/creditcardtemplates/${cardUrl}`}>

    <div className="border p-4 rounded-lg shadow-sm bg-white">
      {/* Tydligt och bra: card.någonting */}
      <h2 className="text-xl font-bold text-gray-800">{card.creditCardType}</h2>
      
      <div className="flex flex-col mt-4">
        <span className="text-gray-800">Ränta: {card.interestRate}%</span>
        <span className="text-gray-800">Avgift: {card.fee} kr</span>
        <span className="text-gray-800">Limit: {card.creditLimit} kr</span>
      </div>

      <div className="mt-4 pt-2 border-t">
        <span className={card.productStatus === "ACTIVE" ? "text-green-600" : "text-red-600"}>
          ● {card.productStatus}
        </span>
      </div>
    </div>
    </Link>
  )
}