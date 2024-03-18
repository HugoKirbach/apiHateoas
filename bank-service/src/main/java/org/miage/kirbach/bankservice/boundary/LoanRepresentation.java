package org.miage.kirbach.bankservice.boundary;

import java.net.URI;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.miage.kirbach.bankservice.Entity.ELoanStates;
import org.miage.kirbach.bankservice.Entity.Loan;
import org.miage.kirbach.bankservice.Entity.LoanInput;
import org.miage.kirbach.bankservice.Entity.LoanValidator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Controller
@ResponseBody
@RequestMapping(value = "/credits", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoanRepresentation {

    private final LoanResource loanResource;
    private LoanValidator loanValidator;
    
    public LoanRepresentation(LoanResource loanResource, LoanValidator loanValidator) {
        this.loanValidator = loanValidator;
        this.loanResource = loanResource;
    }

    @GetMapping // GET :8084/credits
    public ResponseEntity<?> getAllLoans() {
        return ResponseEntity.ok(loanResource.findAll());
    }

    @GetMapping(value = "/{id}") // GET :8084/credits/numDemande
    public ResponseEntity<?> getOneLoan(@PathVariable String id) {
        return Optional.of(loanResource.findById(id))
                .filter(Optional::isPresent)
                .map(i -> ResponseEntity.ok(i.get()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/check") // GET :8084/credits/check?firstname=nom&lastname=prenom pour récup état de la demande
    public ResponseEntity<String> checkLoan(@RequestParam String firstname, @RequestParam String lastname) {
        boolean loanExists = loanResource.existsByFirstnameAndLastname(firstname, lastname);
        if (loanExists) {
            Loan loanEntity = loanResource.findByFirstnameAndLastname(firstname, lastname);
            if (true) {
                return ResponseEntity.ok("Etat de la demande : " + loanEntity.getState().toString());
            }
        }
        return ResponseEntity.ok("Aucune demande actuelle pour ce client");
    }

    @PostMapping // POST :8084/credits
    public ResponseEntity<?> postLoan(@RequestBody @Valid LoanInput loan) {
        Loan loanEntity = new Loan(
            Integer.toString(getMaxId()), 
            ELoanStates.Début, 
            loan.getFirstName(), 
            loan.getLastName(), 
            loan.getAdress(), 
            loan.getBirthDate(), 
            loan.getCurrentJob(), 
            loan.getIncomeLastThreeYears(), 
            loan.getLoanAmount(), 
            loan.getLoanDuration(),
            new Date().toString());
            

        Loan loanSaved = loanResource.save(loanEntity);
        URI uri = linkTo(LoanRepresentation.class).slash(loanSaved.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/{id}") // DELETE :8084/credits/numDemande
    @Transactional
    public ResponseEntity<?> deleteLoan(@PathVariable String id) {
        Optional<Loan> loan = loanResource.findById(id);
        loan.ifPresent(loanResource::delete);
        return ResponseEntity.noContent().build();
    }

    public int getMaxId() {
        return loanResource.findAll().size()+1;
    }


}
