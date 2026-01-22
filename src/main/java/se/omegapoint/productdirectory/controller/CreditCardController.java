package se.omegapoint.productdirectory.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.omegapoint.productdirectory.dtos.CreditCardRequestDTO;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
import se.omegapoint.productdirectory.service.CreditCardService;

import java.util.List;

// RestController för kreditkort
@RestController
@RequestMapping("/api/creditcard")
public class CreditCardController {

    private static final Logger log = LoggerFactory.getLogger(CreditCardController.class);

    private final CreditCardService creditCardService;

    // Konstruktor för injection
    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    // Hanterar GET-request och hämtar alla kreditkort. Returnerar dto-lista samt statuskod
    @GetMapping
    public ResponseEntity<List<CreditCardResponseDTO>> getAll() {
        log.info("Get all CreditCards");
        List<CreditCardResponseDTO> allCards = creditCardService.getAllCreditCards(); // Skapar lista via service

        return ResponseEntity.status(HttpStatus.OK).body(allCards); // Returnerar response med listan och statuskod
    }

    // Hanterar POST-request och skapar nytt kreditkort i databas. Returnerar sedan dto-response med body och statuskod
    @PostMapping
    public ResponseEntity<CreditCardResponseDTO> create(@RequestBody CreditCardRequestDTO request) {
        log.info("Create credit card: {}", request);
        CreditCardResponseDTO response = creditCardService.addCreditCard(request); // Skickar request till service och får tillbaka response

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Returnerar response till client med body och statuskod
    }

}
