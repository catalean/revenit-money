package ro.cb.finance.storage.util;

import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public final class JpaSchemaExport {

    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            execute();
            return;
        }

        execute(args[0], args[1]);
        System.exit(0);
    }

    private static void execute() {
        execute("cb-finance-pu", System.getProperty("user.dir"));
    }

    public static void execute(String persistenceUnitName, String destination) {
        System.out.println("Generating DDL create script to : " + destination);

        final Properties props = new Properties();

        props.setProperty("javax.persistence.schema-generation.database.action", "none");
        props.setProperty("javax.persistence.schema-generation.create-database-schemas", "false");
        props.setProperty("javax.persistence.schema-generation.scripts.action","drop-and-create");
        props.setProperty("javax.persistence.schema-generation.scripts.create-target","sampleCreate.ddl");
        props.setProperty("javax.persistence.schema-generation.scripts.drop-target","sampleDrop.ddl");

        Persistence.generateSchema(persistenceUnitName, props);
    }
}
