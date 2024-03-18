package org.miage.kirbach.bankservice.boundary;
import org.miage.kirbach.bankservice.Entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LoanResource extends JpaRepository<Loan, String> {
    Boolean existsByFirstnameAndLastname(String firstname, String lastname);
    Loan findByFirstnameAndLastname(String firstname, String lastname);
}
