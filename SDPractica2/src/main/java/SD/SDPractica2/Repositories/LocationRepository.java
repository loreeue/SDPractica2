package SD.SDPractica2.Repositories;

import SD.SDPractica2.Entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}