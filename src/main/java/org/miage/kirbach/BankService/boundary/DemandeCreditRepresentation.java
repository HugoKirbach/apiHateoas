package org.miage.kirbach.BankService.boundary;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.miage.kirbach.BankService.Entity.DemandeCredit;
import org.miage.kirbach.BankService.Entity.DemandeCreditInput;
import org.miage.kirbach.BankService.Entity.DemandeCreditValidator;
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
@RequestMapping(value = "/demande-credit", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemandeCreditRepresentation {

    private final DemandeCreditResource demandeCreditResource;
    private DemandeCreditValidator demandeCreditValidator;
    
    public DemandeCreditRepresentation(DemandeCreditResource demandeCreditResource, DemandeCreditValidator demandeCreditValidator) {
        this.demandeCreditValidator = demandeCreditValidator;
        this.demandeCreditResource = demandeCreditResource;
    }

    @GetMapping // GET :8084/demande-credit
    public ResponseEntity<?> getAllDemandeCredit() {
        return ResponseEntity.ok(demandeCreditResource.findAll());
    }

    @GetMapping(value = "/{id}") // GET :8084/demande-credit/num
    public ResponseEntity<?> getOneDemandeCredit(String id) {
        return Optional.of(demandeCreditResource.findById(id))
                .filter(Optional::isPresent)
                .map(i -> ResponseEntity.ok(i.get()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping // POST :8084/demande-credit
    public ResponseEntity<?> postDemandeCredit(@RequestBody @Valid DemandeCreditInput demandeCredit) {
        DemandeCredit demandeCreditEntity = new DemandeCredit(
        UUID.randomUUID().toString(),
         "[Début]", 
         demandeCredit.getNom(), 
         demandeCredit.getPrenom(), 
         demandeCredit.getAdresse(), 
         demandeCredit.getDateNaissance(), 
         demandeCredit.getEmploiActuel(), 
         demandeCredit.getRevenuTroisDernieresAnnees(), 
         demandeCredit.getMontantCredit(), 
         demandeCredit.getDureeCredit());

        DemandeCredit demandeCreditSaved = demandeCreditResource.save(demandeCreditEntity);
        URI uri = linkTo(DemandeCreditRepresentation.class).slash(demandeCreditSaved.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/{id}") // DELETE :8084/demande-credit/num
    @Transactional
    public ResponseEntity<?> deleteDemandeCredit(String id) {
        Optional<DemandeCredit> demandeCredit = demandeCreditResource.findById(id);
        demandeCredit.ifPresent(demandeCreditResource::delete);
        return ResponseEntity.noContent().build();
    }
}
