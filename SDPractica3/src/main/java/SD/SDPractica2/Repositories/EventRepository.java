package SD.SDPractica2.Repositories;

import SD.SDPractica2.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}