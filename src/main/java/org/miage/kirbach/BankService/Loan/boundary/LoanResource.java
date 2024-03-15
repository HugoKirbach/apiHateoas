package org.miage.kirbach.BankService.Loan.boundary;
import org.miage.kirbach.BankService.Loan.Entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LoanResource extends JpaRepository<Loan, String> {
    
}
