package org.miage.kirbach.BankService.boundary;
import org.miage.kirbach.BankService.Entity.DemandeCredit;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DemandeCreditResource extends JpaRepository<DemandeCredit, String> {
    
}
