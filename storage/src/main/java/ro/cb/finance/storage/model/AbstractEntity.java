package ro.cb.finance.storage.model;

import ro.cb.finance.storage.util.UUIDGenerator;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Base class defining common fields and their mappings for all entities.
 *
 *
 */
@MappedSuperclass
public abstract class AbstractEntity {

    /** Predefined suffix value added to all FK column names */
    protected static final String _FK = "_FK";
    /** Predefined suffix value added to all index names */
    protected static final String _IX = "_IX";

    /**
     * Class declaring all column names used by this entity.
     */
    public static class Columns {

        public static final String UUID = "uuid";
        public static final String VERSION = "version";

        /**
         * Constructor, protected as this class only declares static fields (constants) and it should never be instantiated.
         */
        protected Columns() {
            //do nothing
        }
    }

    /**
     * Technical unique key.
     */
    @Id
    @Column(name = Columns.UUID, length = 32)
    private String uuid;

    @Version
    @Column(name = Columns.VERSION, nullable = false)
    private Long version;

    /**
     * Constructor, empty, required by JPA.
     */
    protected AbstractEntity() {
        //do nothing
    }

    /**
     * @return
     */
    public String getUuid() {
        if (uuid == null) {
            uuid = UUIDGenerator.next();
        }

        return uuid;
    }

    /**
     * @return
     */
    public Long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        return uuid.equals(that.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
