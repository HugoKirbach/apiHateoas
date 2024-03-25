package org.miage.kirbach.financeservice.boundary;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.logging.Logger;

import org.miage.kirbach.financeservice.Entity.EGouvStates;
import org.miage.kirbach.financeservice.Entity.Finance;
import org.miage.kirbach.financeservice.Entity.FinanceInput;
import org.miage.kirbach.financeservice.Entity.FinanceValidator;
import org.springframework.http.ResponseEntity;

@Controller
@ResponseBody
@RequestMapping(value = "/finances", produces = MediaType.APPLICATION_JSON_VALUE)
public class FinanceRepresentation {

    final Logger LOGGER = Logger.getLogger(FinanceRepresentation.class.getName());

    private final FinanceResource financeResource;
    private FinanceValidator financeValidator;

    public FinanceRepresentation(FinanceResource financeResource, FinanceValidator financeValidator) {
        this.financeValidator = financeValidator;
        this.financeResource = financeResource;
    }

    @GetMapping
    public ResponseEntity<?> getAllFinances() {
        return ResponseEntity.ok(financeResource.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOneFinance(String id) {
        return financeResource.findById(id)
                .map(finance -> ResponseEntity.ok(finance))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/check")
    public ResponseEntity<String> checkFinance(@RequestParam("firstname") String nom,
                                               @RequestParam("lastname") String prenom,
                                               @RequestParam("incomeLastThreeYears") Long salary) {
        boolean financeExists = financeResource.existsByFirstnameAndLastname(nom, prenom);
        if (financeExists) {
            LOGGER.info("finance existe");
            Finance finance = financeResource.findByFirstnameAndLastname(nom, prenom);
            //deux manières de comparer les salaires, une fois en Long et une fois en String car cela ne fonctionnait pas tout le temps avec un Long, va savoir pourquoi
            if (finance.getIncomelastthreeyears() == salary) {
                return ResponseEntity.ok(EGouvStates.OK.toString());
            }
            if (finance.getIncomelastthreeyears().toString().equals(salary.toString())) {
                return ResponseEntity.ok(EGouvStates.OK.toString());
            }
        }
        return ResponseEntity.badRequest().body(EGouvStates.KO.toString());
    }

    @PostMapping
    public ResponseEntity<?> createFinance(@RequestBody @Valid FinanceInput financeInput) {
        Finance finance = new Finance(
                String.valueOf(getMaxId()),
                financeInput.getFirstname(),
                financeInput.getLastname(),
                financeInput.getIncomelastthreeyears()
        );
        Finance financeSaved = financeResource.save(finance); 
        URI uri = linkTo(FinanceRepresentation.class).slash(financeSaved.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }


    public int getMaxId() {
        return financeResource.findAll().size()+1;
    }
    
}
