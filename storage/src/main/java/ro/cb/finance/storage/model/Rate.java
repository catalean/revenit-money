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

import static ro.cb.finance.storage.model.Rate.*;
import static ro.cb.finance.storage.model.Rate.Columns.*;

/**
 *
 */
@Entity
@Table(name = TABLE_NAME,
        indexes = {
                @Index(name = TABLE_NAME_SHORT + "_" + BANK_UUID + _IX, columnList = BANK_UUID),
                @Index(name = TABLE_NAME_SHORT + "_" + BASE_CURRENCY_UUID + _IX, columnList = BASE_CURRENCY_UUID),
                @Index(name = TABLE_NAME_SHORT + "_" + CURRENCY_UUID + _IX, columnList = CURRENCY_UUID),
                @Index(name = TABLE_NAME_SHORT + "_" + DATE_UUID + _IX, columnList = DATE_UUID)
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_rates_per_date", columnNames = {
                        BANK_UUID, BASE_CURRENCY_UUID, CURRENCY_UUID, DATE_UUID
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

        public static final String BANK_UUID            = "bank_uuid";
        public static final String BASE_CURRENCY_UUID   = "base_currency_uuid";
        public static final String CURRENCY_UUID        = "currency_uuid";
        public static final String DATE_UUID            = "date_uuid";
        public static final String VALUE                = "value";

        /**
         * Constructor, private as this class only declares static fields (constants) and it should never be instantiated.
         */
        private Columns() {
            //do nothing
        }
    }

    @JoinColumn(name = BANK_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + BANK_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Bank bank;

    @JoinColumn(name = BASE_CURRENCY_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + BASE_CURRENCY_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Currency baseCurrency;

    @JoinColumn(name = CURRENCY_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + CURRENCY_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Currency currency;

    @Column(name = VALUE, precision = 4, scale = 4, nullable = false)
    private BigDecimal value;

    @JoinColumn(name = DATE_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + DATE_UUID  + _FK), nullable = false)
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
     * @param baseCurrency
     * @param date
     * @param currency
     * @param value
     */
    public Rate(Bank bank, Currency baseCurrency, Date date, Currency currency, BigDecimal value) {
        this.bank = bank;
        this.baseCurrency = baseCurrency;
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
    public Currency getBaseCurrency() {
        return baseCurrency;
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
