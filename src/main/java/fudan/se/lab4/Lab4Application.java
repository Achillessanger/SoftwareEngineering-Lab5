package fudan.se.lab4;

import fudan.se.lab4.context.EnvironmentContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Lab4Application {

    public static void main(String[] args) {

        SpringApplication.run(Lab4Application.class, args);
//        EnvironmentContext e = EnvironmentContext.getEnvironmentContext();
//        int a = 0;
    }
}
