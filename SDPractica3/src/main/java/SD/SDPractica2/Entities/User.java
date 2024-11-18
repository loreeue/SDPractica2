package SD.SDPractica2.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //user identifier
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String birthdate;
    private String gender;
    private String selectedAvatar;
    private LocalDateTime createdAt; //user creation date
    private LocalDateTime updatedAt; //user last updated date

    public User(String username, String email, String password, String firstName, String lastName, String birthdate, String gender, String avatar) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.gender = gender;
        this.selectedAvatar = avatar;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    //one user is in one event
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;
}