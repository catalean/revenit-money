package ro.cobinance.money.bnr;

import org.javamoney.moneta.FastMoney;
import org.junit.Test;
import ro.cobinance.money.ExchangeRateProviderType;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.ConversionQuery;
import javax.money.convert.ConversionQueryBuilder;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collection;

/**
 * @author Catalin Kormos
 */
public class ConversionTest {

    @Test
    public void convert() {
        CurrencyConversion conv = MonetaryConversions.getConversion("EUR", ExchangeRateProviderType.BNR.get());
        Collection<String> provNames = MonetaryConversions.getConversionProviderNames();

        System.out.println(conv + ", " + provNames);

        CurrencyUnit ron = Monetary.getCurrency("RON");
        CurrencyUnit eur = Monetary.getCurrency("EUR");

        MonetaryAmount money = FastMoney.of(10, ron);
        MonetaryAmount money2 = FastMoney.of(10, eur);

        LocalDate localDate = Year.of(2009).atMonth(Month.JANUARY).atDay(9);

        ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider(ExchangeRateProviderType.BNR);
        ConversionQuery query = ConversionQueryBuilder.of().setBaseCurrency(ron).setTermCurrency(eur).set(localDate).build();

        CurrencyConversion currencyConversion = provider.getCurrencyConversion(query);

        MonetaryAmount result = currencyConversion.apply(money);
        System.out.println(result);
    }
}
