package org.miage.kirbach.bankservice.boundary;

import java.net.URI;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.swing.text.html.parser.Entity;

import org.miage.kirbach.bankservice.Entity.ELoanStates;
import org.miage.kirbach.bankservice.Entity.Loan;
import org.miage.kirbach.bankservice.Entity.LoanInput;
import org.miage.kirbach.bankservice.Entity.LoanValidator;
import org.miage.kirbach.bankservice.assembler.LoanModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Controller
@ResponseBody
@RequestMapping(value = "/credits", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoanRepresentation {

    private final LoanResource loanResource;
    private LoanValidator loanValidator;

    private final static String BASEURLFINANCECHECK = "http://localhost:8083/finances/check";

    private final LoanModelAssembler assembler;

    private final RestTemplate restTemplate;

    //TODO : Service appel vers cet URL + inclure CB avec annotation
    
    public LoanRepresentation(LoanResource loanResource, LoanValidator loanValidator, LoanModelAssembler assembler, RestTemplate restTemplate) {
        this.loanValidator = loanValidator;
        this.loanResource = loanResource;
        this.assembler = assembler;
        this.restTemplate = restTemplate;
    }

    @GetMapping // GET :8084/credits
    public ResponseEntity<?> getAllLoans() {
        return ResponseEntity.ok(loanResource.findAll());
    }

    @GetMapping(value = "/{id}") // GET :8084/credits/numDemande
    public ResponseEntity<?> getOneLoan(@PathVariable String id) {
        // return Optional.of(loanResource.findById(id))
        //         .filter(Optional::isPresent)
        //         .map(i -> ResponseEntity.ok(i.get()))
        //         .orElse(ResponseEntity.notFound().build());
        Optional<Loan> loan = loanResource.findById(id);
        if (loan.isPresent()) {
            EntityModel<Loan> loanModel = assembler.toModel(loan.get());
            return ResponseEntity.ok(loanModel);
        }
        return ResponseEntity.notFound().build();
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
        return ResponseEntity.badRequest().body("Aucune demande actuelle pour ce client");
    }

    @GetMapping(value = "/checkFinances")
    public ResponseEntity<String> checkFinances(@RequestParam String firstname, @RequestParam String lastname, @RequestParam int incomeLastThreeYears) {
        ResponseEntity<String> response = restTemplate.getForEntity(BASEURLFINANCECHECK + "?firstname=" + firstname + "&lastname=" + lastname + "&incomeLastThreeYears=" + incomeLastThreeYears, String.class);
        return response;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE) // POST :8084/credits
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

        EntityModel<Loan> loanModel = assembler.toModel(loanSaved);
        //URI uri = linkTo(LoanRepresentation.class).slash(loanSaved.getId()).toUri();

        return ResponseEntity.created(linkTo(methodOn(LoanRepresentation.class).getOneLoan(loanModel.getContent().getId())).toUri()).body(loanModel);
    }

    //MaJ de la demande Loan avec ID
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE) // PUT :8084/credits/numDemande
    public ResponseEntity<?> putLoan(@PathVariable String id, @RequestBody @Valid LoanInput loan) {
        Optional<Loan> loanEntity = loanResource.findById(id);
        if (loanEntity.isPresent()) {
            Loan loanToUpdate = loanEntity.get();
            loanToUpdate.setFirstname(loan.getFirstName());
            loanToUpdate.setLastname(loan.getLastName());
            loanToUpdate.setAdress(loan.getAdress());
            loanToUpdate.setBirthdate(loan.getBirthDate());
            loanToUpdate.setCurrentjob(loan.getCurrentJob());
            loanToUpdate.setIncomelastthreeyears(loan.getIncomeLastThreeYears());
            loanToUpdate.setLoanamount(loan.getLoanAmount());
            loanToUpdate.setLoanduration(loan.getLoanDuration());
            loanToUpdate.setLastupdate(new Date().toString());

            Loan loanSaved = loanResource.save(loanToUpdate);

            EntityModel<Loan> loanModel = assembler.toModel(loanSaved);
            return ResponseEntity.ok(loanModel);
            //return ResponseEntity.created(linkTo(methodOn(LoanRepresentation.class).getOneLoan(loanModel.getContent().getId())).toUri()).body(loanModel);

            //return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
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

    @PutMapping(value = "/{id}/study", produces = MediaType.APPLICATION_JSON_VALUE) // PUT :8084/credits/numDemande/state
    public ResponseEntity<?> studyLoan(@PathVariable String id) {
        Optional<Loan> loanEntity = loanResource.findById(id);
        if (loanEntity.isPresent()) {
            if (loanEntity.get().getState() != ELoanStates.Début) {
                return ResponseEntity.badRequest().build();
            }
            Loan loanToUpdate = loanEntity.get();
            loanToUpdate.setState(ELoanStates.Étude);
            loanToUpdate.setLastupdate(new Date().toString());
            loanResource.save(loanToUpdate);

            EntityModel<Loan> loanModel = assembler.toModel(loanToUpdate);
            return ResponseEntity.ok(loanModel);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/{id}/validate", produces = MediaType.APPLICATION_JSON_VALUE) // PUT :8084/credits/numDemande/state
    @CircuitBreaker(name = "finance", fallbackMethod = "fallbackFinanceCall")
	@Retry(name = "finance")
    public ResponseEntity<?> validateLoan(@PathVariable String id) {
        try {
            Optional<Loan> loanEntity = loanResource.findById(id);
        if (loanEntity.isPresent()) {
            if (loanEntity.get().getState() != ELoanStates.Étude) {
                return ResponseEntity.badRequest().body("La demande de crédit n'est pas en état d'étude.");
            }

            String firstname = loanEntity.get().getFirstname();
            String lastname = loanEntity.get().getLastname();
            Long incomeLastThreeYears = loanEntity.get().getIncomelastthreeyears();
            String result = restTemplate.getForObject(BASEURLFINANCECHECK + "?firstname=" + firstname + "&lastname=" + lastname + "&incomeLastThreeYears=" + incomeLastThreeYears, String.class);

            if (result.contains("KO")) {
                if (!result.contains("n'existe")) {
                    return ResponseEntity.badRequest().body("Le service de finance publique a indiqué un mauvais rensignement du salaire pour le client.");
                }
                return ResponseEntity.badRequest().body("Le service de finance publique n'a pas trouvé le client.");
            }

            Loan loanToUpdate = loanEntity.get();
            loanToUpdate.setState(ELoanStates.Validation);
            loanToUpdate.setLastupdate(new Date().toString());
            loanResource.save(loanToUpdate);

            EntityModel<Loan> loanModel = assembler.toModel(loanToUpdate);
            
            return ResponseEntity.ok(loanModel);
        }
        return ResponseEntity.badRequest().body("Aucune demande de crédit trouvé pour cet ID");
        } catch (Exception e) {
        return ResponseEntity.badRequest().body("Le service de finance publique est hors ligne");
        }
    }

    @PutMapping(value = "/{id}/accept", produces = MediaType.APPLICATION_JSON_VALUE) // PUT :8084/credits/numDemande/state
    public ResponseEntity<?> acceptLoan(@PathVariable String id) {
        Optional<Loan> loanEntity = loanResource.findById(id);
        if (loanEntity.isPresent()) {
            if (loanEntity.get().getState() != ELoanStates.Validation) {
                return ResponseEntity.badRequest().body("La demande de crédit n'est pas en état de validation.");
            }
            Loan loanToUpdate = loanEntity.get();
            loanToUpdate.setState(ELoanStates.Acceptation);
            loanToUpdate.setLastupdate(new Date().toString());
            loanResource.save(loanToUpdate);

            EntityModel<Loan> loanModel = assembler.toModel(loanToUpdate);
            return ResponseEntity.ok(loanModel);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/{id}/reject") // PUT :8084/credits/numDemande/state
    public ResponseEntity<?> rejectLoan(@PathVariable String id) {
        Optional<Loan> loanEntity = loanResource.findById(id);
        if (loanEntity.isPresent()) {
            if (loanEntity.get().getState() != ELoanStates.Validation) {
                return ResponseEntity.badRequest().body("La demande de crédit n'est pas en état de validation.");
            }
            Loan loanToUpdate = loanEntity.get();
            loanToUpdate.setState(ELoanStates.Rejet);
            loanToUpdate.setLastupdate(new Date().toString());
            loanResource.save(loanToUpdate);

            EntityModel<Loan> loanModel = assembler.toModel(loanToUpdate);
            return ResponseEntity.ok(loanModel);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> fallbackFinanceCall(String id, Exception e) { 
        return ResponseEntity.status(503).body("Impossible d'acceder au service de finance publiques, veuillez réessayer plus tard.");
    }

}
