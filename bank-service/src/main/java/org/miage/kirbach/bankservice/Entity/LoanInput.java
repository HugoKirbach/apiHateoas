package org.miage.kirbach.bankservice.Entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanInput {

    
    private String id;
    private ELoanStates status;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @Size(min = 2)
    private String lastName;

    @NotNull
    @NotBlank
    @Size(min = 4)
    private String adress;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[0-3][0-9]/[0-1][0-9]/[1-2][0-9][0-9][0-9]", message = "La date de naissance doit être au format JJ/MM/AAAA")
    private String birthDate;

    @NotNull
    @NotBlank
    @Size(min = 1, message = "Si sans emploi, saisir \"/\"") // Laisser 1 caractère pour les personnes sans emplois qui saisissent "/" ?
    private String currentJob;

    @NotNull
    @Min(0)
    private Long incomeLastThreeYears;

    @NotNull
    @Min(0)
    private Long loanAmount;

    @NotNull
    @Min(12) // 1 an ?
    @Max(240) // 20 ans
    private Integer loanDuration; // en nb mois ?

}
