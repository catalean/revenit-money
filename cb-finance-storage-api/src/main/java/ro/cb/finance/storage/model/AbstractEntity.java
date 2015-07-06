package ro.cb.finance.storage.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import static ro.cb.finance.storage.model.AbstractEntity.Columns.*;

/**
 * Base class defining common fields and their mappings for all entities.
 */
@MappedSuperclass
public abstract class AbstractEntity {

    /** Predefined suffix value added to all FK column names */
    protected static final String _FK = "_fk";
    /** Predefined suffix value added to all index names */
    protected static final String _IX = "_ix";

    /**
     * Class declaring all column names used by this entity.
     */
    public static class Columns {

        public static final String UUID     = "uuid";
        public static final String VERSION  = "version";

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
    @Column(name = UUID, length = 32)
    private String uuid;

    @Version
    @Column(name = VERSION, nullable = false)
    private Long version;

    /**
     * Constructor, empty, required by JPA.
     */
    protected AbstractEntity() {
        //do nothing
    }

    @PrePersist
    public void prePersist() {
        if (uuid == null) {
            createUUid();
        }
    }

    /**
     * @return
     */
    public String getUuid() {
        if (uuid == null) {
            createUUid();
        }

        return uuid;
    }

    /**
     *
     */
    private void createUUid() {
        //a new {@link UUID} as string of length 32 and all upper case characters.
        this.uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase(); // replace "-" 36 -> 32 char
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
