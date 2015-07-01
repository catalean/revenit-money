package ro.cb.finance.storage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;

/**
 * @author Catalin Kormos
 */
@Entity
@Table(name = Rate.TABLE_NAME,
        indexes = {
                @Index(name = Rate.TABLE_NAME_SHORT + "_" + Rate.Columns.BANK_UUID + Rate._IX, columnList = Rate.Columns.BANK_UUID),
                @Index(name = Rate.TABLE_NAME_SHORT + "_" + Rate.Columns.ORIGINAL_CURRENCY_UUID + Rate._IX, columnList = Rate.Columns.ORIGINAL_CURRENCY_UUID),
                @Index(name = Rate.TABLE_NAME_SHORT + "_" + Rate.Columns.CURRENCY_UUID + Rate._IX, columnList = Rate.Columns.CURRENCY_UUID),
                @Index(name = Rate.TABLE_NAME_SHORT + "_" + Rate.Columns.DATE_UUID + Rate._IX, columnList = Rate.Columns.DATE_UUID)
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_rates_per_date", columnNames = {
                        Rate.Columns.BANK_UUID, Rate.Columns.ORIGINAL_CURRENCY_UUID, Rate.Columns.CURRENCY_UUID, Rate.Columns.DATE_UUID
                })
        }
)
public class Rate extends AbstractEntity {

    /** The name of the DB table this entity is persisted to. */
    public static final String TABLE_NAME = "cb_dwh_currency_rate";
    /** The short name used in FK and Indices names. */
    public static final String TABLE_NAME_SHORT = "cb_dwh_cr";

    /**
     * Class declaring all column names used by this entity.
     */
    public static final class Columns extends AbstractEntity.Columns {

        public static final String BANK_UUID = "bank_uuid";
        public static final String ORIGINAL_CURRENCY_UUID = "orig_currency_uuid";
        public static final String CURRENCY_UUID = "currency_uuid";
        public static final String DATE_UUID = "date_uuid";
        public static final String VALUE = "value";

        /**
         * Constructor, private as this class only declares static fields (constants) and it should never be instantiated.
         */
        private Columns() {
            //do nothing
        }
    }

    @JoinColumn(name = Columns.BANK_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + Columns.BANK_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Bank bank;

    @JoinColumn(name = Columns.ORIGINAL_CURRENCY_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + Columns.ORIGINAL_CURRENCY_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Currency originalCurrency;

    @JoinColumn(name = Columns.CURRENCY_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + Columns.CURRENCY_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Currency currency;

    @Column(name = Columns.VALUE, precision = 4, scale = 4, nullable = false)
    private BigDecimal value;

    @JoinColumn(name = Columns.DATE_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + Columns.DATE_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Date date;

    /**
     * Constructor, empty, required by JPA.
     */
    protected Rate() {
        //do nothing
    }

    /**
     * Constructor.
     *
     * @param bank
     * @param originalCurrency
     * @param date
     * @param currency
     * @param value
     */
    public Rate(Bank bank, Currency originalCurrency, Date date, Currency currency, BigDecimal value) {
        this.bank = bank;
        this.originalCurrency = originalCurrency;
        this.date = date;
        this.currency = currency;
        this.value = value;
    }

    /**
     * @return
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * @return
     */
    public Currency getOriginalCurrency() {
        return originalCurrency;
    }

    /**
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * @return
     */
    public BigDecimal getValue() {
        return value;
    }
}
