package org.miage.kirbach.financeservice.Entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinanceInput {

    @NotBlank
    @NotNull
    private String firstname;

    @NotBlank
    @NotNull
    private String lastname;

    @NotNull
    @NotBlank
    @Min(0)
    private Long incomelastthreeyears;
    
}
