package se.omegapoint.productdirectory.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.omegapoint.productdirectory.dtos.LoanRequestDTO;
import se.omegapoint.productdirectory.dtos.LoanResponseDTO;
import se.omegapoint.productdirectory.services.LoanService;

import java.util.List;

// RestController för lån
@RestController
@RequestMapping("/api/loan")
public class LoanController {

    private final LoanService loanService;

    // Konstruktor för injection
    public LoanController(final LoanService loanService) {
        this.loanService = loanService;
    }

    // Hanterar GET-request och hämtar alla lån. Returnerar dto-lista samt statuskod
    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAll() {

        List<LoanResponseDTO> allLoans = loanService.getAllLoans(); // Skapar lista via service

        return ResponseEntity.status(HttpStatus.OK).body(allLoans); // Returnerar response med listan och statuskod
    }

    // Hanterar GET-request för specifikt id. Returnerar dto samt statuskod
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getLoanById(@PathVariable Integer id) {

        LoanResponseDTO response = loanService.getLoanById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    // Hanterar POST-request och skapar nytt lån i databas. Returnerar sedan dto-response med body och statuskod
    @PostMapping
    public ResponseEntity<LoanResponseDTO> create(@RequestBody @Valid LoanRequestDTO request) {

        LoanResponseDTO response = loanService.addLoan(request); // Skickar request till service och får tillbaka response

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Returnerar response till client med body och statuskod
    }

    // Hanterar PUT-request och uppdaterar entityn för id't i databasen. Returnerar body och statuskod
    @PutMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid LoanRequestDTO request) {

        LoanResponseDTO response = loanService.updateLoan(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Hanterar DELETE-request. Tar bort entity i databas för id't och returnerar statuskod.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        loanService.deleteLoan(id);

        return ResponseEntity.noContent().build();
    }

}
