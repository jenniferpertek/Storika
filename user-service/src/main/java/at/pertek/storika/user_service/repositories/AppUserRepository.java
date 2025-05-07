package at.pertek.storika.user_service.repositories;

import at.pertek.storika.user_service.entities.AppUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
  Optional<AppUser> findByUsername(String username);
}
