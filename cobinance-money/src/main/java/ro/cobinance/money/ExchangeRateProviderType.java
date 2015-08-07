package ro.cobinance.money;

import javax.money.convert.ExchangeRateProviderSupplier;

/**
 * <p>
 * This enum contains identifiers of the implementations provided by Cobinance. Usage:
 * </p>
 * <code>ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider(ExchangeRateProviderType.BNR);<code>
 */
public enum ExchangeRateProviderType implements ExchangeRateProviderSupplier {

    BNR("BNR", "Exchange rate to the National Bank of Romania.");

    private final String type;
    private final String description;

    ExchangeRateProviderType(String type, String description) {
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
