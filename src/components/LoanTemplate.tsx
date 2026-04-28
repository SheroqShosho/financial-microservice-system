import { LoanTemplateDTO } from "@/types/loan";
import Link from "next/link";

interface Props {
  loan: LoanTemplateDTO;
}

export default function LoanBox({ loan }: Props) {
  const loanUrl = loan.loanType.toLowerCase();
  return (
    <Link href={`/loantemplates/${loanUrl}`}>
      <div className="border p-4 rounded-lg shadow-sm bg-white">
        {/* Tydligt och bra: loan.någonting */}
        <h2 className="text-xl font-bold text-gray-800">{loan.loanType}</h2>
        <p className="text-gray-600 text-sm mt-1">{loan.description}</p>

        <div className="flex flex-col mt-4">
          <span className="text-gray-800">Ränta: {loan.interestRate}%</span>
          <span className="text-gray-800">Min: {loan.minAmount} kr</span>
          <span className="text-gray-800">Max: {loan.maxAmount} kr</span>
        </div>

        <div className="mt-4 pt-2 border-t">
          <span className={loan.productStatus === "ACTIVE" ? "text-green-600" : "text-red-600"}>
            ● {loan.productStatus}
          </span>
        </div>
      </div>
    </Link>
  );
}

