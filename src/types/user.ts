export interface CreditCardDTO {
    userId: string;
    creditCardId: string;
    creditCardType: string;
    creditLimit: number;
    fee: number;
    interestRate: number;
    spentAmount: number;
    availableAmount: number;
    status: string;
}

export interface LoanDTO {
    userId: string;
    loanId: string;
    loanStatus: string;
    loanType: string;
    interestRate: number;
    durationMonths: number;
    amount: number;
}

export interface MyPagesDTO {
    username: string;
    firstName: string;
    lastName: string;
    socialSecurityNumber: string;
    address: string;
    city: string;
    zipCode: string;
    country: string;
    creditCards: CreditCardDTO[];
    loans: LoanDTO[];
}
