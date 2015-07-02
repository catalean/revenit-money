package ro.cb.finance.storage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static ro.cb.finance.storage.model.Currency.*;
import static ro.cb.finance.storage.model.Currency.Columns.*;

/**
 *
 */
@Entity
@Table(name = TABLE_NAME,
        indexes = {
                @Index(name = TABLE_NAME_SHORT + "_" + CODE + _IX, columnList = CODE)
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_curr_code", columnNames = {CODE})
        }
)
public class Currency extends AbstractEntity {

    /** The name of the DB table this entity is persisted to. */
    public static final String TABLE_NAME = "cb_dwh_currency";
    /** The short name used in FK and Indices names. */
    public static final String TABLE_NAME_SHORT = "cb_dwh_cr";

    /**
     * Class declaring all column names used by this entity.
     */
    public static final class Columns extends AbstractEntity.Columns {

        public static final String CODE         = "code";
        public static final String DESCRIPTION  = "description";

        /**
         * Constructor, private as this class only declares static fields (constants) and it should never be instantiated.
         */
        private Columns() {
            //do nothing
        }
    }

    @Column(name = CODE, length = 8, nullable = false)
    private String code;

    @Column(name = DESCRIPTION, length = 512, nullable = true)
    private String description;

    /**
     * Constructor, empty, required by JPA.
     */
    protected Currency() {
        //do nothing
    }

    /**
     * Constructor.
     *
     * @param code
     */
    public Currency(String code) {
        this(code, null);
    }

    /**
     * Constructor.
     *
     * @param code
     * @param description
     */
    public Currency(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * @return
     */
    public String getCode() {
        return code;
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
