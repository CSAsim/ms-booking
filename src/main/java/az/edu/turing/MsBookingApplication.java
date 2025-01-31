package az.edu.turing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "az.edu.turing")
public class MsBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsBookingApplication.class, args);
    }

}
