package SD.SDPractica2.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "LOCATIONS")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //location identifier
    private String name;
    private String address;
    private int capacity;
    private String type;
    private String accessibility;
    private String city;
    private String country;
    private String image;
    private LocalDateTime createdAt; //location creation date
    private LocalDateTime updatedAt; //location last updated date

    public Location(String name, String address, int capacity, String type, String accessibility, String city, String country, String image) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.type = type;
        this.accessibility = accessibility;
        this.city = city;
        this.country = country;
        this.image = image;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    //in one location there are several events
    @JsonIgnore
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.MERGE })
    @JoinTable(
            name = "event_location",
            inverseJoinColumns = @JoinColumn(name = "event_id"),
            joinColumns = @JoinColumn(name = "location_id")
    )
    @JsonManagedReference
    private Set<Event> events = new HashSet<>();

    public void addEvent(Event event) { this.events.add(event); }
}