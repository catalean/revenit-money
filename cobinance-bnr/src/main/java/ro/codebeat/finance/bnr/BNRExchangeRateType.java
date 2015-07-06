package ro.codebeat.finance.bnr;

import javax.money.convert.ExchangeRateProviderSupplier;

/**
 *
 */
public enum BNRExchangeRateType implements ExchangeRateProviderSupplier {

    BNR("BNR", "Exchange rate to the National Bank of Romania.");

    private final String type;
    private final String description;

    BNRExchangeRateType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    @Override
    public String get() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
