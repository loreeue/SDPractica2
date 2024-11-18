package SD.SDPractica2.Entities;

import SD.SDPractica2.Repositories.EventRepository;
import SD.SDPractica2.Repositories.LocationRepository;
import SD.SDPractica2.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public void run(String... args) throws Exception {

        //create events
        String url1 = "https://i.blogs.es/25d3e1/rammstein/1366_2000.jpg";
        Event event1 = new Event("RAMMSTEIN", "Feur frei! con tus alemanes favoritos!", "2024-10-03 20:00", url1);
        event1 = eventRepository.save(event1);

        String url2 = "https://cloudfront-eu-central-1.images.arcpublishing.com/prisa/SYY4JP477GNNYVCBWQIXXDYRAY.jpg";
        Event event2 = new Event("METALLICA", "Master, master, where's the dreams that I've been after? Ven a dejarte la garganta en un concierto único.", "2024-07-12 19:30", url2);
        event2 = eventRepository.save(event2);

        String url3 = "https://images.lifestyleasia.com/wp-content/uploads/sites/5/2023/06/12142541/coldplay-singapore-concert-2024-music-of-the-spheres-world-tour-asian-leg-ticket-prices-details-sale-dates-venue-1.jpeg";
        Event event3 = new Event("COLDPLAY", "Nobody said it was easy, no one ever said it would be this hard.", "2025-07-03 20:30", url3);
        event3 = eventRepository.save(event3);

        String url4 = "https://uh.gsstatic.es/sfAttachPlugin/1307299.jpg";
        Event event4 = new Event("AITANA", "¡Ven y disfruta de Aitana en una noche inolvidable!", "2024-05-19 21:00", url4);
        event4 = eventRepository.save(event4);

        String url5 = "https://s2.ppllstatics.com/elcomercio/www/multimedia/2023/12/19/estopa-gijon-life-kRGF-U2101033974079SXG-1200x840@El%20Comercio.jpg";
        Event event5 = new Event("ESTOPA", "Esto... es... ESTOPA!", "2024-06-22 20:00", url5);
        event5 = eventRepository.save(event5);

        //create users
        User usuario1 = new User("elena123", "elena@gmail.com", "elena123", "Elena", "Gonzalez", "1980-03-12", "Femenino", "/avatar-mujer.png");
        usuario1 = userRepository.save(usuario1);

        User usuario2 = new User("pablo123", "pablo@gmail.com", "pablo123", "Pablo", "Gutiérrez", "1965-01-24", "Masculino", "/avatar-hombre.png");
        usuario2 = userRepository.save(usuario2);

        User usuario3 = new User("miguel123", "miguel@gmail.com", "miguel123", "Miguel", "Fernández", "1967-02-19", "Masculino", "/avatar-normal.png");
        usuario3 = userRepository.save(usuario3);

        User usuario4 = new User("silvia123", "svx@gmail.com", "silvia123", "Silvia", "Coldplay", "1994-07-06", "Femenino", "/avatar-mujer.png");
        usuario4 = userRepository.save(usuario4);

        User usuario5 = new User("lore123", "lore@gmail.com", "lore123", "Loreto", "Uzquiano", "2003-10-01", "Femenino", "/avatar-mujer.png");
        usuario5 = userRepository.save(usuario5);

        User usuario6 = new User("pepe123", "pepe@gmail.com", "pepe123", "Pepe", "Esteban", "2000-11-11", "Masculino", "/avatar-hombre.png");
        usuario6 = userRepository.save(usuario6);

        User usuario7 = new User("maria123", "maria@gmail.com", "maria123", "Maria", "Martin", "1999-04-23", "Femenino", "/avatar-mujer.png");
        usuario7 = userRepository.save(usuario7);

        User usuario8 = new User("mario123", "mario@gmail.com", "mario123", "Mario", "Uceta", "2007-12-31", "Masculino", "/avatar-normal.png");
        usuario8 = userRepository.save(usuario8);

        User usuario9 = new User("ana123", "ana@gmail.com", "ana123", "Ana", "Lorenzo", "2010-07-15", "Femenino", "/avatar-mujer.png");
        usuario9 = userRepository.save(usuario9);

        User usuario10 = new User("juan123", "juan@gmail.com", "juan123", "Juan", "Arevalo", "2001-11-01", "Masculino", "/avatar-hombre.png");
        usuario10 = userRepository.save(usuario10);

        //create locations
        url1 = "https://img.huffingtonpost.es/files/image_720_480/uploads/2023/11/21/fachada-del-wizink-center-de-madrid.jpeg";
        Location location1 = new Location("Wizink Center", "Avenida de Felipe II, s/n", 17000, "Sala grande", "Sí", "Madrid","España", url1);
        location1 = locationRepository.save(location1);

        url2 = "https://cadenaser.com/resizer/Akl0gq_zhNmopGEDwcsaHGrL0po=/736x552/filters:format(jpg):quality(70)/cloudfront-eu-central-1.images.arcpublishing.com/prisaradio/YD2DQTOWMZC4LNR3UN4SK6WQJY.jpg";
        Location location2 = new Location("Estadio Santiago Bernabéu", "Avenida Concha Espina, 1", 80000, "Estadio", "Sí","Madrid", "España", url2);
        location2 = locationRepository.save(location2);

        url3 = "https://upload.wikimedia.org/wikipedia/commons/d/d8/Estadi_Ol%C3%ADmpic_Llu%C3%ADs_Companys.JPG";
        Location location3 = new Location("Estadio Olímpico Lluís Companys", "Passeig Olimpic, 17", 56000, "Estadio", "Sí","Barcelona", "España", url3);
        location3 = locationRepository.save(location3);

        url4 = "https://indiehoy.com/wp-content/uploads/2018/09/sala-la-rivera-madrid.jpg";
        Location location4 = new Location("Sala La Riviera", "Paseo Bajo de la Virgen del Puerto, s/n", 2500, "Sala mediana", "Sí", "Madrid", "España", url4);
        location4 = locationRepository.save(location4);

        url5 = "https://madridsecreto.co/wp-content/uploads/2023/04/parque-tierno.jpg";
        Location location5 = new Location("Parque Enrique Tierno Galván", "Calle Meneses, 4", 10000, "Parque", "Sí", "Madrid", "España", url5);
        location5 = locationRepository.save(location5);

        //updates the user in the database with the event he is attending
        usuario1 = userRepository.save(usuario1);
        usuario2 = userRepository.save(usuario2);
        usuario3 = userRepository.save(usuario3);
        usuario4 = userRepository.save(usuario4);
        usuario5 = userRepository.save(usuario5);
        usuario6 = userRepository.save(usuario6);
        usuario7 = userRepository.save(usuario7);
        usuario8 = userRepository.save(usuario8);
        usuario9 = userRepository.save(usuario9);
        usuario10 = userRepository.save(usuario10);

        //add attributes to events
        event1.addLocation(location1);
        event1.addLocation(location2);
        event1 = eventRepository.save(event1);

        event2.addLocation(location2);
        event2.addLocation(location3);
        event2 = eventRepository.save(event2);

        event3.addLocation(location3);
        event3.addLocation(location4);
        event3 = eventRepository.save(event3);

        event4.addLocation(location4);
        event4.addLocation(location5);
        event4 = eventRepository.save(event4);

        event5.addLocation(location5);
        event5.addLocation(location1);
        event5 = eventRepository.save(event5);

        //add attributes to locations
        location1.addEvent(event1);
        location1.addEvent(event5);

        location2.addEvent(event1);
        location2.getEvents().add(event2);

        location3.addEvent(event2);
        location3.addEvent(event3);

        location4.addEvent(event3);
        location4.addEvent(event4);

        location5.addEvent(event4);
        location5.addEvent(event5);

        //updates the locations
        location1 = locationRepository.save(location1);
        location2 = locationRepository.save(location2);
        location3 = locationRepository.save(location3);
        location4 = locationRepository.save(location4);
        location5 = locationRepository.save(location5);

        //updates the events
        event1 = eventRepository.save(event1);
        event2 = eventRepository.save(event2);
        event3 = eventRepository.save(event3);
        event4 = eventRepository.save(event4);
        event5 = eventRepository.save(event5);
    }
}