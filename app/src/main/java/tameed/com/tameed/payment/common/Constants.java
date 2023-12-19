package tameed.com.tameed.payment.common;

import java.util.LinkedHashSet;
import java.util.Set;


public class Constants {

    /* The configuration values to change across the app */
    public static class Config {

        /* The payment brands for Ready-to-Use UI and Payment Button */
        public static final Set<String> PAYMENT_BRANDS;

        static {
            PAYMENT_BRANDS = new LinkedHashSet<>();
            PAYMENT_BRANDS.add("VISA");
            PAYMENT_BRANDS.add("MASTER");
            PAYMENT_BRANDS.add("PAYPAL");
        }

        /* The card info for SDK & Your Own UI*/
        public static final String CARD_BRAND = "VISA";

    }

    public static final int CONNECTION_TIMEOUT = 5000;

    public static final String BASE_URL = "http://52.59.56.185";

    public static final String LOG_TAG = "msdk.demo";
}
