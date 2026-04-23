export interface LoanTemplateDTO {
  loanType: string;
  interestRate: number;
  minAmount: number;
  maxAmount: number;
  productStatus: "ACTIVE"|"INACTIVE";
  description: string;
}

