package tameed.com.tameed.payment.task;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import tameed.com.tameed.payment.common.Constants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;


/**
 * Represents an async task to request a checkout id from the server.
 */
public class CheckoutIdRequestAsyncTask extends AsyncTask<String, Void, String> {

    private CheckoutIdRequestListener listener;

    public CheckoutIdRequestAsyncTask(CheckoutIdRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length != 2) {
            return null;
        }

        String amount = params[0];
        String currency = params[1];

        String id = null;

        try {
            id = requestCheckoutId(amount, currency);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    protected void onPostExecute(String checkoutId) {
        if (listener != null) {
            listener.onCheckoutIdReceived(checkoutId);
        }
    }

    private String request(String amount,
                           String currency) {
        String urlString = Constants.BASE_URL + "/token?" +
                "amount=" + amount +
                "&currency=" + currency +
                "&paymentType=DB" +
                /* store notificationUrl on your server to change it any time without updating the app */
                "&notificationUrl=https://tameed.net/tameed_app/notification";
        URL url;
        HttpURLConnection connection = null;
        String checkoutId = null;

        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);

            JsonReader reader = new JsonReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));

            reader.beginObject();

            while (reader.hasNext()) {
                if (reader.nextName().equals("checkoutId")) {
                    checkoutId = reader.nextString();

                    break;
                }
            }

            reader.endObject();
            reader.close();

            //////Log.e(Constants.LOG_TAG, "Checkout ID: " + checkoutId);
        } catch (Exception e) {
            ////Log.e(Constants.LOG_TAG, "Error: ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return checkoutId;
    }


    private String requestCheckoutId(String amount,
                                     String currency) throws IOException {
        URL url = new URL("https://test.oppwa.com/v1/checkouts");

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer OGFjN2E0Yzg2YTI2YzczMTAxNmEyYTcxZDY4YTAxYWV8Z2c4MlpCRm1ZNQ==");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        String ts = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        ////Log.e("Value:", "" + String.format(Locale.US, "%.2f", Float.parseFloat(amount)));

        String data = ""
                + "entityId=8ac7a4c86a26c731016a2a72126d01b3"
                + "&amount=" + String.format(Locale.US, "%.2f", Float.parseFloat(amount))
                + "&currency=SAR"
                + "&paymentType=DB"
                + "&merchantTransactionId=" + ts
                + "&testMode=EXTERNAL" +
                /* store notificationUrl on your server to change it any time without updating the app */
                "&notificationUrl=https://tameed.net/tameed_app/apis/card_payment_notification";


       /* String data = ""
                + "entityId=8ac7a4c86a26c731016a2a72126d01b3"
                + "&amount=92.00"
                + "&currency=SAR"
                + "&paymentBrand=VISA"
                + "&paymentType=DB"
                + "&card.number=4200000000000000"
                + "&card.holder=Jane Jones"
                + "&card.expiryMonth=05"
                + "&card.expiryYear=2021"
                + "&card.cvv=123"
                *//* store notificationUrl on your server to change it any time without updating the app *//*
                + "&shopperResultUrl=http://52.59.56.185:80/notification";*/

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(data);
        wr.flush();
        wr.close();
        int responseCode = conn.getResponseCode();
        InputStream is;

        if (responseCode >= 400) is = conn.getErrorStream();
        else is = conn.getInputStream();
        String conversion = convertStreamToString(is);
        return conversion;
        // urlString = "https://test.oppwa.com/v1/checkouts/" + resourcePath + "/payment";

    }


    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}