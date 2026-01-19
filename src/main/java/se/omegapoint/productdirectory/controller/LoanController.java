package se.omegapoint.productdirectory.controller;

import org.springframework.web.bind.annotation.*;
import se.omegapoint.productdirectory.service.LoanService;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(final LoanService loanService) {
        this.loanService = loanService;

    }

}
