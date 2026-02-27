package se.omegapoint.productdirectory.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.omegapoint.productdirectory.dtos.CreditCardRequestDTO;
import se.omegapoint.productdirectory.dtos.CreditCardResponseDTO;
import se.omegapoint.productdirectory.dtos.CreditCardUpdateDTO;
import se.omegapoint.productdirectory.services.CreditCardService;

import java.util.List;

// RestController för kreditkort
@RestController
@RequestMapping("/api/creditcard")
public class CreditCardController {

    private final CreditCardService creditCardService;

    // Konstruktor för injection
    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    // Hanterar GET-request och hämtar alla kreditkort. Returnerar dto-lista samt statuskod
    @GetMapping
    public ResponseEntity<List<CreditCardResponseDTO>> getAll() {

        List<CreditCardResponseDTO> allCards = creditCardService.getAllCreditCards(); // Skapar lista via service

        return ResponseEntity.status(HttpStatus.OK).body(allCards); // Returnerar response med listan och statuskod
    }

    // Hanterar GET-request för specifikt id. Returnerar dto samt statuskod
    @GetMapping("/{id}")
    public ResponseEntity<CreditCardResponseDTO> getCreditCardById(@PathVariable Integer id) {

        CreditCardResponseDTO response = creditCardService.getCreditCardById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Hanterar POST-request och skapar nytt kreditkort i databas. Returnerar sedan dto-response med body och statuskod
    @PostMapping
    public ResponseEntity<CreditCardResponseDTO> create(@RequestBody @Valid CreditCardRequestDTO request) {

        CreditCardResponseDTO response = creditCardService.addCreditCard(request); // Skickar request till service och får tillbaka response

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Returnerar response till client med body och statuskod
    }

    // Hanterar PUT-request och uppdaterar entityn för id't i databasen. Returnerar body och statuskod
    @PutMapping("/{id}")
    public ResponseEntity<CreditCardResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid CreditCardUpdateDTO request) {

        CreditCardResponseDTO response = creditCardService.updateCreditCard(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Hanterar DELETE-request. Tar bort entity i databas för id't och returnerar statuskod.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        creditCardService.deleteCreditCard(id);

        return ResponseEntity.noContent().build();
    }

}
