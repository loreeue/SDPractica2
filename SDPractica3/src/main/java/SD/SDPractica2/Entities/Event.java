package SD.SDPractica2.Entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "EVENTS")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //event identifier
    private String name;
    private String description;
    private String dateTime;
    private String image;
    private LocalDateTime createdAt; //event creation date
    private LocalDateTime updatedAt; //event last updated date

    public Event(String name, String description, String dateTime, String image) {
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.image = image;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    //in one event there are several users
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<User> users = new LinkedList<>();

    //in one event there are several locations
    @JsonIgnore
    @ManyToMany(mappedBy = "events", fetch = FetchType.EAGER)
    private Set<Location> locations = new HashSet<>();

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addLocation(Location location) { this.locations.add(location); }
}