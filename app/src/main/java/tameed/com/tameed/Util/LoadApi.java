package tameed.com.tameed.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@SuppressLint("NewApi")
public class LoadApi {

    Activity activity;

    Bitmap bitmap = null;

    static JSONObject json = null;
    String result;
    String image;
    String show;
    JSONObject result1;


    String company_name;
    String num;

    public String getReceiver_status_online_offline() {
        return receiver_status_online_offline;
    }

    public void setReceiver_status_online_offline(String receiver_status_online_offline) {
        this.receiver_status_online_offline = receiver_status_online_offline;
    }

    public JSONObject getCompany_details() {
        return company_details;
    }

    public void setCompany_details(JSONObject company_details) {
        this.company_details = company_details;

    }

    JSONObject company_details;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String user_id;
    String auto_reply_message_doctor;
    String receiver_status_online_offline, support_online_status;

    public String getSupport_online_status() {
        return support_online_status;
    }

    public void setSupport_online_status(String support_online_status) {
        this.support_online_status = support_online_status;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAuto_reply_message_doctor() {
        return auto_reply_message_doctor;
    }

    public void setAuto_reply_message_doctor(String auto_reply_message_doctor) {
        this.auto_reply_message_doctor = auto_reply_message_doctor;
    }

    public String getChat_seeting_doctor() {
        return chat_seeting_doctor;
    }

    public void setChat_seeting_doctor(String chat_seeting_doctor) {
        this.chat_seeting_doctor = chat_seeting_doctor;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getResult() {
        return result;
    }

    public void setResult1(JSONObject result1) {
        this.result1 = result1;
    }

    public JSONObject getResult1() {
        return result1;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image_id, chat_seeting_doctor;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public JSONObject Action_PrivateOrder_Image(String action, String user_id,
                                                ArrayList<byte[]> list, String provider_id, String category_id, String service_id, String order_description,
                                                String service_time, String service_date, String warranty_days, String service_fee, String percentage,
                                                String commission, String total_fee, String payment_id) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";

        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {

                    entity.addPart("picture" + i, new ByteArrayBody(list.get(i), "image/jpeg", "test" + i + ".jpg"));
                    //////Log.e("55555", "" + list.get(i));
                }

            }
//            else {
//                entity.addPart("picture", new ByteArrayBody("","image/jpeg", "test1.jpg")
//            }


            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("provider_id", new StringBody(provider_id, "text/plain", Charset.forName("UTF-8")));


            entity.addPart("category_id", new StringBody(category_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("service_id", new StringBody(service_id, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("order_description", new StringBody(order_description, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("service_time", new StringBody(service_time, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("service_date", new StringBody(service_date, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("warranty_days", new StringBody(warranty_days, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("service_fee", new StringBody(service_fee, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("percentage", new StringBody(percentage, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("commission", new StringBody(commission, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("total_fee", new StringBody(total_fee, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("payment_id", new StringBody(payment_id, "text/plain", Charset.forName("UTF-8")));
            //  service_fee , percentage , commission , total_fee (All required) , picture0 (these will be multiple as picture1,picture2 and so on...) [optional]
            //////Log.e("user_id111", "" + user_id);
            //////Log.e("Provider_id", "====" + provider_id);
            //////Log.e("category_id", "====" + category_id);
            //////Log.e("Service_id", "====" + service_id);
            //////Log.e("Other_discription", order_description);
            //////Log.e("service_time", service_time);
            //////Log.e("service_date", service_date);
            //////Log.e("Warranty", warranty_days);


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    //////Log.e("jObj//////", " " + jObj);

                    setResult1(jObj);
     /*JSONObject jobj1=jObj.getJSONObject("company_details");
     setResult(jObj.getString("msg"));
     setCompany_details(jObj.getJSONObject("company_details"));
     //////Log.e("msg", " " + jObj.getString("msg"));
     //////Log.e("company_details", " " + jObj.getJSONObject("company_details"));*/
                    //////Log.e("msg", " " + jObj.getString("msg"));
//     //////Log.e("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }


    public JSONObject Action_PublicOrder_Image(String action, String user_id,
                                               ArrayList<byte[]> list, String location, String category_id, String service_id, String order_description,
                                               String service_time, String service_date, String warranty_days, String lattitude, String longitude
    ) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";

        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    entity.addPart("picture" + i, new ByteArrayBody(list.get(i), "image/jpeg", "test" + i + ".jpg"));
                    ////Log.e("55555", "******" + list.get(i));
                    //////Log.e("ByteArrayBody****", "*****" + new ByteArrayBody(list.get(i), "image/jpeg", "test" + i + ".jpg"));

                    //ADD JIG try to compress After Uppload Image
                    /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    BitmapFactory.decodeFile(imageFilePath).compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    byte[] data = bos.toByteArray();
                    ByteArrayBody bab = new ByteArrayBody(data, imageFilePath); */
                }
            }


//            else {
//                entity.addPart("picture", new ByteArrayBody("","image/jpeg", "test1.jpg")
//            }

            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("location", new StringBody(location, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("category_id", new StringBody(category_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("service_id", new StringBody(service_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("order_description", new StringBody(order_description, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("service_time", new StringBody(service_time, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("service_date", new StringBody(service_date, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("warranty_days", new StringBody(warranty_days, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("latitude", new StringBody(lattitude, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("longitude", new StringBody(longitude, "text/plain", Charset.forName("UTF-8")));

            ////Log.e("action", "***********PUT***********" + action);
            ////Log.e("user_id", "***********PUT**********" + user_id);
            ////Log.e("location", "***********PUT*********" + location);
            ////Log.e("category_id", "***********PUT******" + category_id);
            ////Log.e("Service_id", "***********PUT*******" + service_id);
            ////Log.e("Lattitude", "***********PUT********" + lattitude);
            ////Log.e("Longitude", "***********PUT********" + longitude);
            ////Log.e("Other_discription", "******PUT******" + order_description);
            ////Log.e("service_time", "********PUT********" + service_time);
            ////Log.e("service_date", "*********PUT********" + service_date);
            ////Log.e("Warranty", "***********PUT**********" + warranty_days);

            int statusCode = -1;
            try {
                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    //////Log.e("jObj//////", " " + jObj);
                    setResult1(jObj);
     /*JSONObject jobj1=jObj.getJSONObject("company_details");
     setResult(jObj.getString("msg"));
     setCompany_details(jObj.getJSONObject("company_details"));
     //////Log.e("msg", " " + jObj.getString("msg"));
     //////Log.e("company_details", " " + jObj.getJSONObject("company_details"));*/
                    //////Log.e("msg", " " + jObj.getString("msg"));
//     //////Log.e("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }


    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMMM-yyyy @ hh:mm a";

        //  Date time2 = Calendar.getInstance().getTime();

        //  outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public JSONObject Action_profileSetting_Image(String action, String user_id,
                                                  byte[] picture) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (picture.length > 0) {


                entity.addPart("picture", new ByteArrayBody(picture, "image/jpeg", "test1.jpg"));
                //////Log.e("55555", "" + picture);

            }

            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));


            //////Log.e("action 111", "" + action);
            //////Log.e("user_id111", "" + user_id);
            ////////Log.e("555551111", "" + picture);
            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    //////Log.e("jObj//////", " " + jObj);

                    setResult1(jObj);
     /*JSONObject jobj1=jObj.getJSONObject("company_details");
     setResult(jObj.getString("msg"));
     setCompany_details(jObj.getJSONObject("company_details"));
     //////Log.e("msg", " " + jObj.getString("msg"));
     //////Log.e("company_details", " " + jObj.getJSONObject("company_details"));*/
                    //////Log.e("msg", " " + jObj.getString("msg"));
//     //////Log.e("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject Action_profileSetting_noImage(String action, String user_id, byte[] picture, String pic_type) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";
        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("pic_type", new StringBody(pic_type, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("last_name", new StringBody(last_name, "text/plain", Charset.forName("UTF-8")));
            //entity.addPart("token", new StringBody(token, "text/plain", Charset.forName("UTF-8")));

            //entity.addPart("email_address", new StringBody(email_address, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("company_name", new StringBody(company_name, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("mobile_number", new StringBody(mobile_number, "text/plain", Charset.forName("UTF-8")));
            //entity.addPart("phone_number", new StringBody(phone_number, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("Street_number", new StringBody(street_number, "text/plain", Charset.forName("UTF-8")));
            //entity.addPart("address", new StringBody(address, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("state", new StringBody(state, "text/plain", Charset.forName("UTF-8")));
            // entity.addPart("city", new StringBody(city, "text/plain", Charset.forName("UTF-8")));
            //entity.addPart("date_of_birth", new StringBody(date_of_birth, "text/plain", Charset.forName("UTF-8")));


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    //////Log.e("jObj//////", " " + jObj);
//     setUser_id(jObj.getString("user_id"));

                    setResult1(jObj);
     /*JSONObject jobj1=jObj.getJSONObject("company_details");
     setResult(jObj.getString("msg"));
     setCompany_details(jObj.getJSONObject("company_details"));
     //////Log.e("msg", " " + jObj.getString("msg"));
     //////Log.e("company_details", " " + jObj.getJSONObject("company_details"));*/
                    //////Log.e("msg", " " + jObj.getString("msg"));
//     //////Log.e("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject Action_ResubmittOrder_Image(String action, String user_id,
                                                  ArrayList<byte[]> list, String provider_id, String category_id, String service_id, String order_description,
                                                  String service_time, String service_date, String warranty_days, String service_fee, String percentage,
                                                  String commission, String total_fee, String order_id) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";

        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {

                    entity.addPart("picture" + i, new ByteArrayBody(list.get(i), "image/jpeg", "test" + i + ".jpg"));
                    //////Log.e("55555", "" + list.get(i));
                }

            }
//            else {
//                entity.addPart("picture", new ByteArrayBody("","image/jpeg", "test1.jpg")
//            }


            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("provider_id", new StringBody(provider_id, "text/plain", Charset.forName("UTF-8")));


            entity.addPart("category_id", new StringBody(category_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("service_id", new StringBody(service_id, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("order_description", new StringBody(order_description, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("service_time", new StringBody(service_time, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("service_date", new StringBody(service_date, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("warranty_days", new StringBody(warranty_days, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("service_fee", new StringBody(service_fee, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("percentage", new StringBody(percentage, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("commission", new StringBody(commission, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("total_fee", new StringBody(total_fee, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("order_id", new StringBody(order_id, "text/plain", Charset.forName("UTF-8")));
            //  service_fee , percentage , commission , total_fee (All required) , picture0 (these will be multiple as picture1,picture2 and so on...) [optional]
            //////Log.e("user_id111", "" + user_id);
            //////Log.e("Provider_id", "====" + provider_id);
            //////Log.e("category_id", "====" + category_id);
            //////Log.e("Service_id", "====" + service_id);
            //////Log.e("Other_discription", order_description);
            //////Log.e("service_time", service_time);
            //////Log.e("service_date", service_date);
            //////Log.e("Warranty", warranty_days);


            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    //////Log.e("jObj//////", " " + jObj);

                    setResult1(jObj);
     /*JSONObject jobj1=jObj.getJSONObject("company_details");
     setResult(jObj.getString("msg"));
     setCompany_details(jObj.getJSONObject("company_details"));
     //////Log.e("msg", " " + jObj.getString("msg"));
     //////Log.e("company_details", " " + jObj.getJSONObject("company_details"));*/
                    //////Log.e("msg", " " + jObj.getString("msg"));
//     //////Log.e("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }

    public JSONObject Action_CashPaymentImage(String action, String user_id,
                                              ArrayList<byte[]> list, String provider_id, String order_id, String payment_type, String bank_account, String payment_amount, String str_order_new_data, String payment_id

    ) {
        JSONParser jparser = new JSONParser();
        JSONObject jObj = null;
        String msg = "";

        final int GOOD_RETURN_CODE = 200;
        try {
            String url = "";

            url = Apis.api_url;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            MultipartEntity entity = new MultipartEntity();


            if (list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {

                    entity.addPart("picture" + i, new ByteArrayBody(list.get(i), "image/jpeg", "test" + i + ".jpg"));
                    //////Log.e("55555", "" + list.get(i));
                }

            }
//            else {
//                entity.addPart("picture", new ByteArrayBody("","image/jpeg", "test1.jpg")
//            }


            entity.addPart("user_id", new StringBody(user_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("action", new StringBody(action, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("provider_id", new StringBody(provider_id, "text/plain", Charset.forName("UTF-8")));


//user_id , provider_id , order_id , payment_amount , payment_type (Cash/Card) (All Required)
            //bank_account (Required if payment_type : 'Cash') , card_number , name_on_card , expiry_date (like 10/2020)
            //, cvv (All required if payment_type : 'Card' ) , picture0 (these will be multiple as picture1,picture2 and so on...) [optional have to se
            entity.addPart("order_id", new StringBody(order_id, "text/plain", Charset.forName("UTF-8")));
            entity.addPart("payment_amount", new StringBody(payment_amount, "text/plain", Charset.forName("UTF-8")));

            entity.addPart("payment_type", new StringBody(payment_type, "text/plain", Charset.forName("UTF-8")));

//            entity.addPart("bank_account", new StringBody(bank_account, "text/plain", Charset.forName("UTF-8")));
            if (payment_type.equals("Card"))
                entity.addPart("payment_id", new StringBody(payment_id, "text/plain", Charset.forName("UTF-8")));


            //TODO ADD JAY NEW FILD HERE
            if (payment_type.equals("Cash"))
                entity.addPart("from_bank_account", new StringBody(str_order_new_data, "text/plain", Charset.forName("UTF-8")));


            //  service_fee , percentage , commission , total_fee (All required) , picture0 (these will be multiple as picture1,picture2 and so on...) [optional]
            //////Log.e("user_id111", "==========" + user_id);
            //////Log.e("Provider_id", "=========" + provider_id);
            //////Log.e("order_id", "============" + order_id);
            //////Log.e("payment_type", "========" + payment_type);
            //////Log.e("payment_amount", "======" + payment_amount);
            //////Log.e("bank_account", "========" + bank_account);
            //////Log.e("str_order_new_data", "==" + str_order_new_data);
            //////Log.e("payment_id", "==" + payment_id);

            int statusCode = -1;


            try {

                httppost.setEntity(entity);
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity resEntity = resp.getEntity();
                String string = EntityUtils.toString(resEntity);
                try {
                    jObj = new JSONObject(string);

                    //////Log.e("jObj//////", " " + jObj);

                    setResult1(jObj);
     /*JSONObject jobj1=jObj.getJSONObject("company_details");
     setResult(jObj.getString("msg"));
     setCompany_details(jObj.getJSONObject("company_details"));
     //////Log.e("msg", " " + jObj.getString("msg"));
     //////Log.e("company_details", " " + jObj.getJSONObject("company_details"));*/
                    ////Log.e("msg", " " + jObj.getString("msg"));
//     ////Log.e("show", " " + jObj.getString("user_id"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return json;
    }


}
