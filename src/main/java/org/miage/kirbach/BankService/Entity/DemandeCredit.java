package org.miage.kirbach.BankService.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandeCredit implements java.io.Serializable{

    @Id
    private String id;

    private String etat; //faire enum avec les 5 états possibles

    private String nom;
    private String prenom;
    private String adresse;
    private String dateNaissance;
    private String emploiActuel;
    private Long revenuTroisDernieresAnnees;
    private Long montantCredit;
    private Integer dureeCredit;
    
}
