package org.miage.kirbach.bankservice.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.miage.kirbach.bankservice.Entity.Loan;
import org.miage.kirbach.bankservice.Entity.Loan;
import org.miage.kirbach.bankservice.boundary.LoanRepresentation;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpMethod;


/**
 * Création d'une classe d'assembleur pour la réponse HATEOAS sous recommandation de mes camarades pour simplifier la gestion des liens
 */
@Configuration
public class LoanModelAssembler implements RepresentationModelAssembler<Loan, EntityModel<Loan>>{

    @Override
    public EntityModel<Loan> toModel(Loan loanEntity) {
        EntityModel<Loan> loanModel = EntityModel.of(loanEntity);
        loanModel.add(linkTo(methodOn(LoanRepresentation.class).getOneLoan(loanEntity.getId())).withSelfRel().withType(HttpMethod.GET.toString()).withName("Info sur cette demande"));
        //loanModel.add(linkTo(methodOn(LoanRepresentation.class).putLoan(loanEntity.getId(), loanEntity)).withRel("updateLoan").withType(HttpMethod.PUT.toString()).withName("Mettre à jour la demande"));
        loanModel.add(linkTo(methodOn(LoanRepresentation.class).getAllLoans()).withRel("allLoans").withType(HttpMethod.GET.toString()).withName("Toutes les demandes"));

        loanModel.add(linkTo(methodOn(LoanRepresentation.class).checkLoan(loanEntity.getFirstname(), loanEntity.getLastname())).withRel("checkLoan").withType(HttpMethod.GET.toString()).withName("Vérifier l'état de la demande"));

        switch (loanModel.getContent().getState()) {
            case Début:
                loanModel.add(linkTo(methodOn(LoanRepresentation.class).studyLoan(loanEntity.getId())).withRel("studyLoan").withType(HttpMethod.PUT.toString()).withName("Etudier la demande"));
                break;
            case Étude:
                loanModel.add(linkTo(methodOn(LoanRepresentation.class).validateLoan(loanEntity.getId())).withRel("validateLoan").withType(HttpMethod.PUT.toString()).withName("Valider la demande"));

            case Validation:
                loanModel.add(linkTo(methodOn(LoanRepresentation.class).acceptLoan(loanEntity.getId())).withRel("acceptLoan").withType(HttpMethod.PUT.toString()).withName("Accepter la demande"));
                loanModel.add(linkTo(methodOn(LoanRepresentation.class).rejectLoan(loanEntity.getId())).withRel("rejectLoan").withType(HttpMethod.PUT.toString()).withName("Rejeter la demande"));
                break;
            default:
                break;
        }

        return loanModel;
    }

    
}
