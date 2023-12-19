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
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;


/**
 * Represents an async task to request a payment status from the server.
 */
public class PaymentStatusRequestAsyncTask extends AsyncTask<String, Void, String> {

    private PaymentStatusRequestListener listener;

    public PaymentStatusRequestAsyncTask(PaymentStatusRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length != 1) {
            return null;
        }

        String resourcePath = params[0];

        if (resourcePath != null) {
            String id = null;

            try {
                id = requestPaymentStatus(resourcePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return id;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String paymentStatus) {
        if (listener != null) {
            if (paymentStatus == null) {
                listener.onErrorOccurred();

                return;
            }

            listener.onPaymentStatusReceived(paymentStatus);
        }
    }


    private String request(String resourcePath) throws IOException {
        //Log.e("Payment status request:", "" + resourcePath);
        URL url = new URL("https://test.oppwa.com/" + resourcePath);
        String checkoutId = null;
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        // conn.setRequestProperty("Authorization", "Bearer OGFjN2E0Yzg2YTI2YzczMTAxNmEyYTcxZDY4YTAxYWV8Z2c4MlpCRm1ZNQ==");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        int responseCode = conn.getResponseCode();
        InputStream is;

        if (responseCode >= 400) is = conn.getErrorStream();
        else is = conn.getInputStream();
        String conversion = convertStreamToString(is);
        //Log.e("Output:", "...." + conversion);
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

    private String requestPaymentStatus(String resourcePath) {
        if (resourcePath == null) {
            return null;
        }

        URL url;
        String urlString;
        HttpURLConnection connection = null;
        String paymentStatus = null;
        String conversion = null;
        try {
            urlString = Constants.BASE_URL + "/status?resourcePath=" +
                    URLEncoder.encode(resourcePath, "UTF-8");

            ////Log.e(Constants.LOG_TAG, "Status request url: " + urlString);

            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);

            JsonReader jsonReader = new JsonReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));

            int responseCode = connection.getResponseCode();
            InputStream is;
            if (responseCode >= 400) is = connection.getErrorStream();
            else is = connection.getInputStream();
            conversion = convertStreamToString(is);
            //Log.e("Response:", "" + conversion);

            /*{"paymentResult":"OK","reason":"000","id":"8ac7a4a06a304583016a4529574f49f6","card":{"bin":"411111","last4Digits":"1111","holder":"Jhon","expiryMonth":"05","expiryYear":"2021"},"paymentBrand":"VISA"}*/

            ////Log.e(Constants.LOG_TAG, "Status: " + paymentStatus);
        } catch (Exception e) {
            //Log.e(Constants.LOG_TAG, "Error: ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return conversion;
    }
}
