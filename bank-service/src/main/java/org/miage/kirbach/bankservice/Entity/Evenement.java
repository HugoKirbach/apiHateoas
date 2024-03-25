package org.miage.kirbach.bankservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evenement implements java.io.Serializable {

    @Id
    private String id;

    @NonNull
    public LocalDate date;

    @NonNull
    public String comment;


    
}
