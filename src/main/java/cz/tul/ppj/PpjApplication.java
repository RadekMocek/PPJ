package cz.tul.ppj;

import cz.tul.ppj.service.WeatherFetcher;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
import cz.tul.ppj.service.jpa.WeatherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class PpjApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PpjApplication.class);
        ApplicationContext ctx = app.run(args);
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
    }

    @Bean
    public WeatherFetcher weatherFetcher() {
        return new WeatherFetcher();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public StateService stateService() {
        return new StateService();
    }

    @Bean
    public CityService cityService() {
        return new CityService();
    }

    @Bean
    public WeatherService weatherService() {
        return new WeatherService();
    }
}
