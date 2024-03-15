package org.miage.kirbach.BankService.Loan.boundary;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.miage.kirbach.BankService.Loan.Entity.Loan;
import org.miage.kirbach.BankService.Loan.Entity.LoanInput;
import org.miage.kirbach.BankService.Loan.Entity.LoanValidator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oracle.svm.core.annotate.Delete;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Controller
@ResponseBody
@RequestMapping(value = "/credit", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoanRepresentation {

    private final LoanResource loanResource;
    private LoanValidator loanValidator;
    
    public LoanRepresentation(LoanResource loanResource, LoanValidator loanValidator) {
        this.loanValidator = loanValidator;
        this.loanResource = loanResource;
    }

    @GetMapping // GET :8084/credit
    public ResponseEntity<?> getAllloan() {
        return ResponseEntity.ok(loanResource.findAll());
    }

    @GetMapping(value = "/{id}") // GET :8084/credit/numDemande
    public ResponseEntity<?> getOneloan(String id) {
        return Optional.of(loanResource.findById(id))
                .filter(Optional::isPresent)
                .map(i -> ResponseEntity.ok(i.get()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // POST :8084/credit
    public ResponseEntity<?> postloan(@RequestBody @Valid LoanInput loan) {
        Loan loanEntity = new Loan(
            UUID.randomUUID().toString(), 
            "[Début]", 
            loan.getFirstName(), 
            loan.getLastName(), 
            loan.getAdress(), 
            loan.getBirthDate(), 
            loan.getCurrentJob(), 
            loan.getIncomeLastThreeYears(), 
            loan.getLoanAmount(), 
            loan.getLoanDuration());

        Loan loanSaved = loanResource.save(loanEntity);
        URI uri = linkTo(LoanRepresentation.class).slash(loanSaved.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/{id}") // DELETE :8084/credit/numDemande
    @Transactional
    public ResponseEntity<?> deleteloan(String id) {
        Optional<Loan> loan = loanResource.findById(id);
        loan.ifPresent(loanResource::delete);
        return ResponseEntity.noContent().build();
    }
}
