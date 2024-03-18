package org.miage.kirbach.bankservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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

    @Enumerated(EnumType.STRING)
    private ELoanStates state; //faire enum avec les 5 états possibles

    private String firstname;
    private String lastname;
    private String adress;
    private String birthdate;
    private String currentjob;
    private Long incomelastthreeyears;
    private Long loanamount;
    private Integer loanduration;
    private String lastupdate;
    
    
}
