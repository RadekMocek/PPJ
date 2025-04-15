package cz.tul.ppj;

import cz.tul.ppj.provisioning.DBProvisioner;
import cz.tul.ppj.service.WeatherFetcher;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
import cz.tul.ppj.service.jpa.WeatherService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class PpjApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(PpjApplication.class);
        ApplicationContext ctx = app.run(args);

        DBProvisioner dbProvisioner = ctx.getBean(DBProvisioner.class);
        WeatherFetcher weatherFetcher = ctx.getBean(WeatherFetcher.class);

        StateService stateService = ctx.getBean(StateService.class);
        CityService cityService = ctx.getBean(CityService.class);
        WeatherService weatherService = ctx.getBean(WeatherService.class);

        // Deletes
        //stateService.deleteAll(); // Delete everything (cascade)
        //weatherService.deleteAll();

        // Download and store weather reports from API
        //dbProvisioner.insertTestDataIntoDbOrm();
        //weatherFetcher.fetchWeatherDataAndStoreToDatabase();

        // Print all stored weathers
        System.out.println(weatherService.getAll());
    }

    @Profile({"devel", "test"})
    @Bean
    public DBProvisioner dbProvisioner() {
        return new DBProvisioner();
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
