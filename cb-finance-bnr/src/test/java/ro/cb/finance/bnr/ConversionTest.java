package ro.cb.finance.bnr;

import org.javamoney.moneta.FastMoney;
import org.junit.Test;

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

/**
 * @author Catalin Kormos
 */
public class ConversionTest {

    @Test
    public void convert() {
        CurrencyUnit ron = Monetary.getCurrency("RON");
        CurrencyUnit eur = Monetary.getCurrency("EUR");

        MonetaryAmount money = FastMoney.of(10, ron);
        MonetaryAmount money2 = FastMoney.of(10, eur);

        LocalDate localDate = Year.of(2009).atMonth(Month.JANUARY).atDay(9);

        ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider(BNRExchangeRateType.BNR);
        ConversionQuery query = ConversionQueryBuilder.of().setBaseCurrency(ron).setTermCurrency(eur).set(localDate).build();

        CurrencyConversion currencyConversion = provider.getCurrencyConversion(query);

        MonetaryAmount result = currencyConversion.apply(money);
        System.out.println(result);
    }
}
