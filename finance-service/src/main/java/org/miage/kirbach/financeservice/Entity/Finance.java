package org.miage.kirbach.financeservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Finance {

    @Id
    private String id;

    private String firstname;
    private String lastname;
    private Long incomelastthreeyears;
    
}
