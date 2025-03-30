package cz.tul.ppj.provisioning;

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

    public void createDb() {
        Resource rc = new ClassPathResource("create_tables.sql");
        try {
            ScriptUtils.executeSqlScript(dataSource.getConnection(), rc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
