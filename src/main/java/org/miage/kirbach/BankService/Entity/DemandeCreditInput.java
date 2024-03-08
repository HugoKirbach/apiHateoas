package org.miage.kirbach.BankService.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeCreditInput {

    @NotNull
    @NotBlank
    private String nom;

    @NotNull
    @Size(min = 2)
    private String prenom;

    @NotNull
    @NotBlank
    @Size(min = 4)
    private String adresse;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[0-3][0-9]/[0-1][0-9]/[1-2][0-9][0-9][0-9]", message = "La date de naissance doit être au format JJ/MM/AAAA")
    private String dateNaissance;

    @NotNull
    @NotBlank
    @Size(min = 1, message = "Si sans emploi, saisir \"/\"") // Laisser 1 caractère pour les personnes sans emplois qui saisissent "/" ?
    private String emploiActuel;

    @NotNull
    @NotBlank
    @Min(0)
    private Long revenuTroisDernieresAnnees;

    @NotNull
    @NotBlank
    @Min(0)
    private Long montantCredit;

    @NotNull
    @NotBlank
    @Min(12) // 1 an ?
    @Max(240) // 20 ans
    private Integer dureeCredit; // en nb mois ?

}
