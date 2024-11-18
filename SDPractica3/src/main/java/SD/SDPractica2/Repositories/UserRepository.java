package SD.SDPractica2.Repositories;

import SD.SDPractica2.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}