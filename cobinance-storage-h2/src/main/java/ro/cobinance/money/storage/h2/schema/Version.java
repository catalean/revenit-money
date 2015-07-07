package ro.cobinance.money.storage.h2.schema;

import ro.cobinance.money.storage.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static ro.cobinance.money.storage.h2.schema.Version.*;
import static ro.cobinance.money.storage.h2.schema.Version.Columns.*;

/**
 *
 */
@Entity
@Table(name = TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_schema_version", columnNames = {VALUE})
        }
)
public class Version extends AbstractEntity {

    /** The name of the DB table this entity is persisted to. */
    public static final String TABLE_NAME = "cb_dwh_schema_version";

    /**
     * Class declaring all column names used by this entity.
     */
    public static class Columns {

        public static final String VALUE = "sch_version";

        /**
         * Constructor, protected as this class only declares static fields (constants) and it should never be instantiated.
         */
        protected Columns() {
            //do nothing
        }
    }

    @Column(name = VALUE, length = 6, nullable = false)
    private String value;

    /**
     * Constructor, empty, required by JPA
     */
    protected Version() {
        //do nothing
    }

    /**
     * Constructor.
     *
     * @param value
     */
    public Version(String value) {
        this.value = value;
    }

    /**
     * @return
     */
    public String getValue() {
        return value;
    }
}
