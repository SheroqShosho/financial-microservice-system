package se.omegapoint.productdirectory.controller;

import org.springframework.web.bind.annotation.*;
import se.omegapoint.productdirectory.service.CreditCardService;


@RestController
@RequestMapping("/api/creditcard")
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }



}
