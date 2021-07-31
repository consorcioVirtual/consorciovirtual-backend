package ar.edu.unsam.consorciovirtual;

import ar.edu.unsam.consorciovirtual.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class ConsorcioVirtualApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsorcioVirtualApplication.class, args);
	}

}
