package cz.tul.ppj;

import cz.tul.ppj.dao.CityDAO;
import cz.tul.ppj.dao.StateDAO;
import cz.tul.ppj.dao.WeatherDAO;
import cz.tul.ppj.provisioning.DBProvisioner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class PpjApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(PpjApplication.class);
        ApplicationContext ctx = app.run(args);

        CityDAO cityDAO = ctx.getBean(CityDAO.class);
        StateDAO stateDAO = ctx.getBean(StateDAO.class);
        WeatherDAO weatherDAO = ctx.getBean(WeatherDAO.class);
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

}
