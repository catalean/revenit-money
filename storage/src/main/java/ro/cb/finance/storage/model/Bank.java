package ro.cb.finance.storage.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = Bank.TABLE_NAME)
public class Bank extends AbstractEntity {

    public static final String TABLE_NAME = "cb_dwh_bank";

}
