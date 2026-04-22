export interface CreditCardTemplateDTO {
  creditCardType: string;
  interestRate: number;
  fee: number;
  creditLimit: number;
  productStatus: "ACTIVE"|"INACTIVE";
}