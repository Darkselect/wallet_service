package uz.wallet.wallet_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.wallet.wallet_service.entity.Admin;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Optional<Admin> findByEmail(String email);
}
