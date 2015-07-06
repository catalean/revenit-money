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
                @Index(name = TABLE_NAME_SHORT + "_" + REFERENCE_CURRENCY_UUID + _IX, columnList = REFERENCE_CURRENCY_UUID),
                @Index(name = TABLE_NAME_SHORT + "_" + CURRENCY_UUID + _IX, columnList = CURRENCY_UUID),
                @Index(name = TABLE_NAME_SHORT + "_" + DATE_UUID + _IX, columnList = DATE_UUID)
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_rates_per_date", columnNames = {
                        BANK_UUID, REFERENCE_CURRENCY_UUID, CURRENCY_UUID, DATE_UUID
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

        public static final String BANK_UUID                 = "bank_uuid";
        public static final String REFERENCE_CURRENCY_UUID   = "reference_currency_uuid";
        public static final String CURRENCY_UUID             = "currency_uuid";
        public static final String DATE_UUID                 = "date_uuid";
        public static final String EFFECTIVE_DATE_UUID       = "effective_date_uuid";
        public static final String VALUE                     = "value";
        public static final String DECIMAL_DIGITS_COUNT      = "decimal_digits_count";
        public static final String MULTIPLIER                = "multiplier";

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

    @JoinColumn(name = REFERENCE_CURRENCY_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + REFERENCE_CURRENCY_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Currency referenceCurrency;

    @JoinColumn(name = CURRENCY_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + CURRENCY_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Currency currency;

    @Column(name = VALUE, nullable = false)
    private int value;

    @Column(name = DECIMAL_DIGITS_COUNT, nullable = false)
    private int decimalDigitsCount;

    @Column(name = MULTIPLIER, nullable = false)
    private int multiplier;

    @JoinColumn(name = DATE_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + DATE_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Date date;

    @JoinColumn(name = EFFECTIVE_DATE_UUID, foreignKey = @ForeignKey(name = TABLE_NAME_SHORT + "_" + EFFECTIVE_DATE_UUID  + _FK), nullable = false)
    @ManyToOne(optional = false)
    private Date effectiveDate;

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
     * @param referenceCurrency
     * @param date
     * @param effectiveDate
     * @param currency
     * @param value
     */
    public Rate(Bank bank, Currency referenceCurrency, Date date, Date effectiveDate, Currency currency, int value) {
        this(bank, referenceCurrency, date, effectiveDate, currency, value, 4, 1);
    }

    /**
     * @param bank
     * @param referenceCurrency
     * @param date
     * @param effectiveDate
     * @param currency
     * @param value
     * @param multiplier
     */
    public Rate(Bank bank, Currency referenceCurrency, Date date, Date effectiveDate, Currency currency, int value, int decimalDigitsCount, int multiplier) {
        this.bank = bank;
        this.referenceCurrency = referenceCurrency;
        this.date = date;
        this.effectiveDate = effectiveDate;
        this.currency = currency;
        this.value = value;
        this.decimalDigitsCount = decimalDigitsCount;
        this.multiplier = multiplier;
    }

    /**
     * @param bank
     * @return
     */
    public Rate withBank(Bank bank) {
        return new Rate(bank, getReferenceCurrency(), getDate(), getEffectiveDate(), getCurrency(), getValue(), getDecimalDigitsCount(), getMultiplier());
    }

    /**
     * @param referenceCurrency
     * @return
     */
    public Rate withReferenceCurrency(Currency referenceCurrency) {
        return new Rate(getBank(), referenceCurrency, getDate(), getEffectiveDate(), getCurrency(), getValue(), getDecimalDigitsCount(), getMultiplier());
    }

    /**
     * @param effectiveDate
     * @return
     */
    public Rate withDate(Date date) {
        return new Rate(getBank(), getReferenceCurrency(), date, getEffectiveDate(), getCurrency(), getValue(), getDecimalDigitsCount(), getMultiplier());
    }

    /**
     * @param effectiveDate
     * @return
     */
    public Rate withEffectiveDate(Date effectiveDate) {
        return new Rate(getBank(), getReferenceCurrency(), getDate(), effectiveDate, getCurrency(), getValue(), getDecimalDigitsCount(), getMultiplier());
    }

    /**
     * @param referenceCurrency
     * @return
     */
    public Rate withCurrency(Currency currency) {
        return new Rate(getBank(), getReferenceCurrency(), getDate(), getEffectiveDate(), currency, getValue(), getDecimalDigitsCount(), getMultiplier());
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
    public Currency getReferenceCurrency() {
        return referenceCurrency;
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
    public Date getEffectiveDate() {
        return effectiveDate;
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
    public int getValue() {
        return value;
    }

    /**
     * @return
     */
    public int getDecimalDigitsCount() {
        return decimalDigitsCount;
    }

    /**
     * @return
     */
    public int getMultiplier() {
        return multiplier;
    }
}
