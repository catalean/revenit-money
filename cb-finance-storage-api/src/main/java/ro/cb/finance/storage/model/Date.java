package ro.cb.finance.storage.model;

import org.hibernate.annotations.Check;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.time.Month;

import static ro.cb.finance.storage.model.Date.*;
import static ro.cb.finance.storage.model.Date.Columns.*;

/**
 *
 */
@Entity
@Table(name = TABLE_NAME,
        indexes = {
                @Index(name = TABLE_NAME_SHORT + "_dmy" + _IX, columnList = DAY_OF_MONTH + "," + MONTH_OF_YEAR + "," + YEAR)
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_days", columnNames = {
                        DAY_OF_MONTH, MONTH_OF_YEAR, YEAR
                })
        }
)
public class Date extends AbstractEntity {

    /** The name of the DB table this entity is persisted to. */
    public static final String TABLE_NAME = "cb_dwh_date";
    /** The short name used in FK and Indices names. */
    public static final String TABLE_NAME_SHORT = "cb_dwh_dt";

    /**
     * Class declaring all column names used by this entity.
     */
    public static final class Columns extends AbstractEntity.Columns {

        public static final String DAY_OF_MONTH     = "day_of_month";
        public static final String MONTH_OF_YEAR    = "month_of_year";
        public static final String YEAR             = "year";

        /**
         * Constructor, private as this class only declares static fields (constants) and it should never be instantiated.
         */
        private Columns() {
            //do nothing
        }
    }

    @Column(name = DAY_OF_MONTH, nullable = false) @Check(constraints = DAY_OF_MONTH + " >= 1 and " + DAY_OF_MONTH + " <= 31")
    private int dayOfMonth;

    @Column(name = MONTH_OF_YEAR, nullable = false) @Check(constraints = MONTH_OF_YEAR + " >= 1 and " + MONTH_OF_YEAR + " <= 12")
    private int monthOfYear;

    @Column(name = YEAR, nullable = false) @Check(constraints = YEAR + " > 0")
    private int year;

    /**
     * Constructor, empty, required by JPA.
     */
    protected Date() {
        //do nothing
    }

    /**
     * Constructor.
     *
     * @param dayOfMonth
     * @param monthOfYear
     * @param year
     */
    public Date(int dayOfMonth, int monthOfYear, int year) {
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear;
        this.year = year;
    }

    /**
     * @return
     */
    public static Date today() {
        LocalDate today = LocalDate.now();
        return new Date(today.getDayOfMonth(), today.getMonthValue(), today.getYear());
    }

    /**
     * @return
     */
    public Date tomorrow() {
        LocalDate thisAsLocalDate = LocalDate.of(year, Month.of(monthOfYear), dayOfMonth);
        LocalDate nextDay = thisAsLocalDate.plusDays(1);

        return new Date(nextDay.getDayOfMonth(), nextDay.getMonthValue(), nextDay.getYear());
    }

    /**
     * @return
     */
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * @return
     */
    public int getMonthOfYear() {
        return monthOfYear;
    }

    /**
     * @return
     */
    public int getYear() {
        return year;
    }
}
