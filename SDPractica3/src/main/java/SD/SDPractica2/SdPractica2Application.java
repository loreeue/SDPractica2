package SD.SDPractica2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"SD.SDPractica2.Entities"})
public class SdPractica2Application {

	public static void main(String[] args) { SpringApplication.run(SdPractica2Application.class, args);
	}

}
