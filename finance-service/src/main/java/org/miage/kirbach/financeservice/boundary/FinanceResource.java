package org.miage.kirbach.financeservice.boundary;


import org.miage.kirbach.financeservice.Entity.Finance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceResource extends JpaRepository<Finance, String>{
    boolean existsByFirstnameAndLastname(String firstname, String lastname);
    Finance findByFirstnameAndLastname(String firstname, String lastname);
}
