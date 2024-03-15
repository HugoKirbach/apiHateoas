package org.miage.kirbach.BankService.Loan.Entity;

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
public class Loan implements java.io.Serializable{

    @Id
    private String id;

    private String etat; //faire enum avec les 5 états possibles

    private String firstName;
    private String lastName;
    private String adress;
    private String birthDate;
    private String currentJob;
    private Long incomeLastThreeYears;
    private Long loanAmount;
    private Integer loanDuration;
    
}
