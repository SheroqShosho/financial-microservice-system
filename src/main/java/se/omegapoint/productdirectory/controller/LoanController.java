package se.omegapoint.productdirectory.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.omegapoint.productdirectory.dtos.LoanRequestDTO;
import se.omegapoint.productdirectory.dtos.LoanResponseDTO;
import se.omegapoint.productdirectory.service.LoanService;

import java.util.List;

// RestController för lån
@RestController
@RequestMapping("/api/loan")
public class LoanController {

    private static final Logger log = LoggerFactory.getLogger(LoanController.class);

    private final LoanService loanService;

    // Konstruktor för injection
    public LoanController(final LoanService loanService) {
        this.loanService = loanService;
    }

    // Hanterar GET-request och hämtar alla lån. Returnerar dto-lista samt statuskod
    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAll() {
        log.info("Get all loans");
        List<LoanResponseDTO> allLoans = loanService.getAllLoans(); // Skapar lista via service

        return ResponseEntity.status(HttpStatus.OK).body(allLoans); // Returnerar response med listan och statuskod
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getLoanById(@PathVariable Integer id) {
        log.info("Get loan by ID {}", id);

        LoanResponseDTO response = loanService.getLoanById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    // Hanterar POST-request och skapar nytt lån i databas. Returnerar sedan dto-response med body och statuskod
    @PostMapping
    public ResponseEntity<LoanResponseDTO> create(@RequestBody LoanRequestDTO request) {
        log.info("Create loan request: {}", request);
        LoanResponseDTO response = loanService.addLoan(request); // Skickar request till service och får tillbaka response

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // Returnerar response till client med body och statuskod
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> update(@PathVariable Integer id, @RequestBody LoanRequestDTO request) {
        log.info("Update loan: {} with data {}", id, request);
        LoanResponseDTO response = loanService.updateLoan(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Delete credit card: {}", id);

        loanService.deleteLoan(id);

        return ResponseEntity.noContent().build();
    }

}
