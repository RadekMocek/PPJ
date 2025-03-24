package cz.tul.ppj;

import cz.tul.ppj.dao.CityDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PpjApplication {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(PpjApplication.class);
        ApplicationContext ctx = app.run(args);

        CityDAO cityDAO = ctx.getBean(CityDAO.class);
    }

    @Bean
    public CityDAO cityDAO() {
        return new CityDAO();
    }

}
