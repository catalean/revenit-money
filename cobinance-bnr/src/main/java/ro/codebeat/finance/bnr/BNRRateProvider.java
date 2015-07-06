package ro.codebeat.finance.bnr;

import org.javamoney.moneta.ExchangeRateBuilder;
import org.javamoney.moneta.spi.DefaultNumberValue;
import org.javamoney.moneta.spi.LazyBoundCurrencyConversion;

import javax.money.convert.ConversionContext;
import javax.money.convert.ConversionQuery;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.ProviderContext;
import javax.money.convert.ProviderContextBuilder;
import javax.money.convert.RateType;
import java.math.BigDecimal;

/**
 *
 */
public final class BNRRateProvider implements ExchangeRateProvider {

    /**
     * The {@link javax.money.convert.ConversionContext} of this provider.
     */
    private static final ProviderContext CONTEXT = ProviderContextBuilder.of(BNRExchangeRateType.BNR.get(), RateType.DEFERRED)
            .set("providerDescription", BNRExchangeRateType.BNR.getDescription()).build();

    @Override
    public ProviderContext getContext() {
        return CONTEXT;
    }

    @Override
    public ExchangeRate getExchangeRate(ConversionQuery conversionQuery) {
        return new ExchangeRateBuilder(BNRExchangeRateType.BNR.get(), RateType.ANY)
                .setBase(conversionQuery.getBaseCurrency())
                .setTerm(conversionQuery.getCurrency())
                .setFactor(new DefaultNumberValue(BigDecimal.TEN))
                .build();
    }

    @Override
    public CurrencyConversion getCurrencyConversion(ConversionQuery conversionQuery) {
        if (getContext().getRateTypes().size() == 1) {
            return new LazyBoundCurrencyConversion(conversionQuery, this, ConversionContext
                    .of(getContext().getProviderName(), getContext().getRateTypes().iterator().next()));
        }
        return new LazyBoundCurrencyConversion(conversionQuery, this,
                ConversionContext.of(getContext().getProviderName(), RateType.ANY));
    }
}
