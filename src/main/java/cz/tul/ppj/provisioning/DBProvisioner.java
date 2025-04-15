package cz.tul.ppj.provisioning;

import cz.tul.ppj.model.City;
import cz.tul.ppj.model.State;
import cz.tul.ppj.service.jpa.CityService;
import cz.tul.ppj.service.jpa.StateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.util.List;

public class DBProvisioner {

    private static final Logger log = LoggerFactory.getLogger(DBProvisioner.class);

    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private StateService stateService;

    @Autowired
    private CityService cityService;

    public void doProvision() {
        List<String> allTables = namedParameterJdbcOperations.getJdbcOperations().queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES", String.class);
        if (!allTables.contains("WEATHER")) {
            log.warn("DBProvisioner :: Weather table not found. Creating database.");
            createDb();
            allTables = namedParameterJdbcOperations.getJdbcOperations().queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES", String.class);
            log.info("DBProvisioner :: Created tables: {}", allTables);
        } else {
            log.info("DBProvisioner :: Weather table EXISTS, all tables are: {}", allTables);
        }
    }

    private void createDb() {
        execSql("create_tables.sql");
    }

    public void insertTestDataIntoDb() {
        execSql("insert_test_data.sql");
    }

    private void execSql(String classPathResourcePath) {
        Resource rc = new ClassPathResource(classPathResourcePath);
        try {
            ScriptUtils.executeSqlScript(dataSource.getConnection(), rc);
            log.info("DBProvisioner execSql {} OK", classPathResourcePath);
        } catch (Exception e) {
            log.error("[!] DBProvisioner execSql Exception", e);
        }
    }

    public void insertTestDataIntoDbOrm() {
        var stateGb = new State();
        stateGb.setStateId("GB");
        stateGb.setName("United Kingdom of Great Britain and Northern Ireland");
        stateService.create(stateGb);

        var cityLondon = new City();
        cityLondon.setCityId(2643743);
        cityLondon.setState(stateGb);
        cityLondon.setName("London");
        cityService.create(cityLondon);
    }

}
