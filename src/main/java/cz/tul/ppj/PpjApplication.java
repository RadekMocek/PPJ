package cz.tul.ppj;

import cz.tul.ppj.dao.CityDAO;
import cz.tul.ppj.dao.StateDAO;
import cz.tul.ppj.dao.WeatherDAO;
import cz.tul.ppj.provisioning.DBProvisioner;
import cz.tul.ppj.service.WeatherFetcher;
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

        CityDAO cityDAO = ctx.getBean(CityDAO.class);
        StateDAO stateDAO = ctx.getBean(StateDAO.class);
        WeatherDAO weatherDAO = ctx.getBean(WeatherDAO.class);

        WeatherFetcher weatherFetcher = ctx.getBean(WeatherFetcher.class);
        weatherFetcher.fetchWeatherDataAndStoreToDatabase();
    }

    @Bean
    public CityDAO cityDAO() {
        return new CityDAO();
    }

    @Bean
    public StateDAO stateDAO() {
        return new StateDAO();
    }

    @Bean
    public WeatherDAO weatherDAO() {
        return new WeatherDAO();
    }

    @Profile({"devel"})
    @Bean(initMethod = "doProvision")
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
}
