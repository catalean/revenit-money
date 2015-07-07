package ro.cobinance.money.storage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static ro.cobinance.money.storage.model.Bank.*;
import static ro.cobinance.money.storage.model.Bank.Columns.*;

/**
 *
 */
@Entity
@Table(name = TABLE_NAME,
        indexes = {
                @Index(name = TABLE_NAME_SHORT + "_" + NAME + _IX, columnList = NAME)
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_bank_name", columnNames = {NAME})
        }
)
public class Bank extends AbstractEntity {

    /** The name of the DB table this entity is persisted to. */
    public static final String TABLE_NAME = "cb_dwh_bank";
    /** The short name used in FK and Indices names. */
    public static final String TABLE_NAME_SHORT = "cb_dwh_bk";

    /**
     * Class declaring all column names used by this entity.
     */
    public static final class Columns extends AbstractEntity.Columns {

        public static final String NAME         = "name";
        public static final String DESCRIPTION  = "description";

        /**
         * Constructor, private as this class only declares static fields (constants) and it should never be instantiated.
         */
        private Columns() {
            //do nothing
        }
    }

    @Column(name = NAME, length = 200, nullable = false)
    private String name;

    @Column(name = DESCRIPTION, length = 512, nullable = true)
    private String description;

    /**
     * Constructor, empty, required by JPA.
     */
    protected Bank() {
        //do nothing
    }

    /**
     * Constructor.
     *
     * @param name
     */
    public Bank(String name) {
        this(name, null);
    }

    /**
     * Constructor.
     *
     * @param name
     * @param description
     */
    public Bank(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
