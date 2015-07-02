package ro.cb.finance.storage.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = Currency.TABLE_NAME)
public class Currency extends AbstractEntity {

    public static final String TABLE_NAME = "cb_dwh_currency";
}
