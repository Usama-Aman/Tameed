package tameed.com.tameed;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.Entity.All_Payment_Entity;
import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.Fragment.Frag_Month_Picker;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * Created by dev on 18-01-2018.
 */

public class Cash_Report extends AppCompatActivity {

    TextView header_txt, select_pay_type, select_month, from_date, to_date;
    ImageView header_back, radio_current_month, radio_from_to, radio_select_month;
    int count = 0;

    TextView filter_txt;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";

    SaveSharedPrefernces ssp;
    Activity act;

    String filter_type;
    PieChart pie_chart;
    float paid = 0, unpaid = 0;
    Calendar cal, cal2;

    String tab_name = "All";

    String customer;
    TextView user_type_all;

    private String TAG = "Cash_Report";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());

        setContentView(R.layout.cash_report);
        //Log.e(TAG, "****************************");

        ssp = new SaveSharedPrefernces();
        act = Cash_Report.this;

        filter_txt = (TextView) findViewById(R.id.filter_txt);
        header_txt = (TextView) findViewById(R.id.txt_header);
        header_txt.setText("التقارير");
        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        radio_current_month = (ImageView) findViewById(R.id.radio_current_month);
        radio_from_to = (ImageView) findViewById(R.id.radio_from_to);
        radio_select_month = (ImageView) findViewById(R.id.radio_select_month);

        from_date = (TextView) findViewById(R.id.from_date);
        to_date = (TextView) findViewById(R.id.to_date);
        pie_chart = (PieChart) findViewById(R.id.pie_chart);
        Description description = new Description();
        description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        description.setText("");
        pie_chart.setDescription(description);
        pie_chart.setRotationEnabled(true);
        pie_chart.setHoleRadius(25);
        pie_chart.setTransparentCircleAlpha(0);

        pie_chart.setCenterText("التقارير");

        pie_chart.setCenterTextSize(10);
        pie_chart.setDrawEntryLabels(true);

        pie_chart.animateXY(1400, 1400);

        pie_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                ////Log.e("Chart_Selector", "====");
                ////Log.e("Entity", "====" + e.toString());
                ////Log.e("Height", "=======" + h.toString());

            }

            @Override
            public void onNothingSelected() {

            }
        });


        user_type_all = findViewById(R.id.user_type_all);
        if (ssp.getUser_type(act).equals("Provider")) {
            user_type_all.setText("مقدم الخدمة");
            user_type_all.setVisibility(View.VISIBLE);
        } else {
            user_type_all.setText("العميل");
            user_type_all.setVisibility(View.VISIBLE);
        }

  /*      user_type_all.setText(ssp.getUser_type(act));
        user_type_all.setVisibility(View.VISIBLE);*/


        customer = ssp.getUser_type(act);

        utiltyRequest();
        user_type_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog1 = new Dialog(act, R.style.DialogSlideAnim);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.custom_cash_report);

                dialog1.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();
                final RelativeLayout customer_type = (RelativeLayout) dialog1.findViewById(R.id.customer);
                final RelativeLayout provider_type = (RelativeLayout) dialog1.findViewById(R.id.provider);
                final TextView customer_txt = dialog1.findViewById(R.id.customer_txt);
                final TextView provider_txt = dialog1.findViewById(R.id.provider_txt);


                customer_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ////Log.e("Customer", "44444444");

                        customer = "Customer";
                        user_type_all.setText("العميل");
                        utiltyRequest();
                        dialog1.dismiss();
                    }
                });


                provider_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ////Log.e("Provider", "555555");
                        customer = "Provider";
                        user_type_all.setText("مقدم الخدمة");
                        utiltyRequest();
                        dialog1.dismiss();
                    }
                });
//                if(customer.equals("Customer"))
//                {
//                    ////Log.e("Customer","222222");
//                    user_type_all.setText(customer);
//                }
//                else
//                {
//                    user_type_all.setText(customer);
//                }


               /* if (bool) {

                    user_type="Customer";
                    user_type_all.setText("Customer");
                    user_type_all.setVisibility(View.VISIBLE);


                } else {
//
                    user_type="Provider";
                    user_type_all.setText("Provider");
                    user_type_all.setVisibility(View.VISIBLE);

                            *//*else
                            {
                                notification_clear_all.setText("Customer");
                                notification_clear_all.setVisibility(View.VISIBLE);
                            }//


                }
                bool = !bool;*/

                // dialog.show();
            }
        });


        select_pay_type = (TextView) findViewById(R.id.select_payment_txt);
        select_month = (TextView) findViewById(R.id.select_month_txt);


        final String[] pay_type = {"All", "Paid", "Unpaid"};
        final String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


        select_pay_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radio_current_month.setImageResource(R.mipmap.radio);
                radio_from_to.setImageResource(R.mipmap.radio);
                radio_select_month.setImageResource(R.mipmap.radio);
                count = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(Cash_Report.this);
                builder.setTitle("نوع الدفع");
                builder.setItems(pay_type, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        //here you can use like this... str[position]
                        from_date.setText("");
                        to_date.setText("");
                        select_month.setText("");
                        select_pay_type.setText(pay_type[position].toString());
                        if (count == 0) {
                            filter_type = "";
                            tab_name = pay_type[position];
                            utiltyRequest();
                        }
                        count = count + 1;

                    }

                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });


        radio_current_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_type = "Current Month";
                utiltyRequest();
                radio_current_month.setImageResource(R.mipmap.radio_checked);
                radio_from_to.setImageResource(R.mipmap.radio);
                radio_select_month.setImageResource(R.mipmap.radio);
                from_date.setText("");
                to_date.setText("");
                select_month.setText("");
            }
        });

        radio_from_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_type = "date";
                from_date.setText("");
                to_date.setText("");
                select_month.setText("");
                radio_current_month.setImageResource(R.mipmap.radio);
                radio_from_to.setImageResource(R.mipmap.radio_checked);
                radio_select_month.setImageResource(R.mipmap.radio);


                from_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (filter_type.equals("date")) {
                            cal2 = Calendar.getInstance();
                            to_date.setText("");
                            count = 0;
                            final Calendar calendar = Calendar.getInstance();
                            int yy = calendar.get(Calendar.YEAR);
                            int mm = calendar.get(Calendar.MONTH);
                            int dd = calendar.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog datePicker = new DatePickerDialog(Cash_Report.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                              String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
//                                      + "-" + String.valueOf(year);
                                    cal = Calendar.getInstance();
                                    cal.set(year, monthOfYear, dayOfMonth);

                                    ////Log.e("MOnthofYear", "====" + monthOfYear + 1);
                                    if (monthOfYear + 1 < 10) {
                                        String date = String.valueOf(year) + "-0" + String.valueOf(monthOfYear + 1)
                                                + "-" + String.valueOf(dayOfMonth);
                                        from_date.setText(date);
                                    } else {

                                        String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                                                + "-" + String.valueOf(dayOfMonth);
                                        from_date.setText(date);
                                        if (!TextUtils.isEmpty(to_date.getText().toString()))
                                            if (count == 0) {
                                                utiltyRequest();

                                            }
                                        count = count + 1;
                                    }


                                }
                            }, yy, mm, dd);
                            datePicker.getDatePicker().setMaxDate(cal2.getTimeInMillis() + 20000);
                            datePicker.show();
                        }
                    }
                });


                to_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (filter_type.equals("date")) {
                            cal2 = Calendar.getInstance();
                            if (!TextUtils.isEmpty(from_date.getText().toString())) {
                                count = 0;
                                final Calendar calendar = Calendar.getInstance();
                                int yy = calendar.get(Calendar.YEAR);
                                int mm = calendar.get(Calendar.MONTH);
                                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog datePicker = new DatePickerDialog(Cash_Report.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                              String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
//                                      + "-" + String.valueOf(year);


                                        ////Log.e("MOnthofYear", "====" + monthOfYear + 1);
//            if (monthOfYear+1<10){
//                String date = String.valueOf(year) + "-0" + String.valueOf(monthOfYear + 1)
//                        + "-" + String.valueOf(dayOfMonth);
//                to_date.setText(date);
//               // utiltyRequest();
//            }
//            else {

                                        String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                                                + "-" + String.valueOf(dayOfMonth);

                                        to_date.setText(date);
                                        if (count == 0) {
                                            utiltyRequest();

                                        }
                                        count = count + 1;
                                        //utiltyRequest();

                                        ////Log.e("TODATE", "=====222222222222");


                                    }
                                }, yy, mm, dd);
                                datePicker.getDatePicker().setMinDate(cal.getTimeInMillis() - 1000);
                                datePicker.getDatePicker().setMaxDate(cal2.getTimeInMillis() + 20000);
                                datePicker.show();


                            } else {
                                Toast.makeText(act, "يرجى الاختيار من تاريخ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });


            }
        });
        radio_select_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_type = "month";
                radio_current_month.setImageResource(R.mipmap.radio);
                radio_from_to.setImageResource(R.mipmap.radio);
                radio_select_month.setImageResource(R.mipmap.radio_checked);
                from_date.setText("");
                to_date.setText("");
                select_month.setText("");


                //ADD JIGS
                select_month.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (filter_type.equals("month")) {
                            Frag_Month_Picker pd = new Frag_Month_Picker();
                            pd.setListener(new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                                    //Log.e("onDateSet", monthOfYear + " " + year);

                                    count = 0;

                                    Calendar c1 = Calendar.getInstance();
                                    c1.set(Calendar.DAY_OF_WEEK, 1);
                                    int year1 = c1.get(Calendar.YEAR);
                                    int month1 = c1.get(Calendar.MONTH) + 1;
                                    int day1 = c1.get(Calendar.DAY_OF_MONTH);

                                    if (year > year1) {
                                        select_month.setText("");
                                        Toast.makeText(Cash_Report.this, R.string.enter_valid_month, Toast.LENGTH_SHORT).show();
                                        ////Log.e(TAG,"**if*****monthOfYear********"+monthOfYear +"year*****"+year);
                                    } else if (year == year1) {
                                        if (monthOfYear > month1) {
                                            select_month.setText("");
                                            Toast.makeText(Cash_Report.this, R.string.enter_valid_month, Toast.LENGTH_SHORT).show();
                                            ////Log.e(TAG,"**else if*****monthOfYear********"+monthOfYear +"year*****"+year);
                                        } else if (monthOfYear < month1) {
                                            SetupMonth(String.valueOf(year), String.valueOf(monthOfYear));
                                        } else {
                                            SetupMonth(String.valueOf(year), String.valueOf(monthOfYear));
                                        }
                                    } else if (year < year1) {
                                        ////Log.e(TAG,"**else*****monthOfYear********"+monthOfYear +"year*****"+year);
                                        SetupMonth(String.valueOf(year), String.valueOf(monthOfYear));
                                    }

                                   /* if (year > year1)
                                    {
                                        SetupMonth(String.valueOf(year), String.valueOf(monthOfYear));
                                    }
                                    else if (year == year1)
                                    {
                                        if (monthOfYear > month1)
                                        {
                                            SetupMonth(String.valueOf(year), String.valueOf(monthOfYear));
                                        } else if (monthOfYear < month1) {
                                            select_month.setText("");
                                            Toast.makeText(Cash_Report.this,R.string.enter_valid_month,Toast.LENGTH_SHORT).show();
                                        } else {
                                            SetupMonth(String.valueOf(year), String.valueOf(monthOfYear));
                                        }
                                    }*/


                                }
                            });
                            pd.show(getFragmentManager(), "MonthYearPickerDialog");
                        }


                    }
                });


                //REMOVE JIGS
              /*  select_month.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (filter_type.equals("month"))
                        {
                            //Log.e(TAG,"**************select_month**************");
                            cal2 = Calendar.getInstance();
                            count = 0;
                            final Calendar calendar = Calendar.getInstance();
                            int yy = calendar.get(Calendar.YEAR);
                            int mm = calendar.get(Calendar.MONTH);
                            int dd = calendar.get(Calendar.DAY_OF_MONTH);
                            DatePickerDialog datePicker = new DatePickerDialog(Cash_Report.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                              String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
//                                      + "-" + String.valueOf(year);
                                    ////Log.e("MOnthofYear", "====" + monthOfYear + 1);
                                    //Log.e(TAG,"**************MOnthofYear**************"+ monthOfYear + 1);

                                    // if (monthOfYear+1<10){
//                                  String date = String.valueOf(year) + "-0" + String.valueOf(monthOfYear + 1);
//                                  select_month.setText(date);
//
//                              }
//                              else {

                                    String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1);
                                    select_month.setText(date);
                                    //Log.e(TAG,"**************SELCT_MONTH**************"+ date);

                                    if (count == 0) {
                                        utiltyRequest();
                                    }
                                    count = count + 1;
                                    //utiltyRequest();

                                    ////Log.e("SELCT_MONTH", "=====1111111111111");
                                }
                            }, yy, mm, dd);
                            datePicker.getDatePicker().setMaxDate(cal2.getTimeInMillis() + 20000);
                            datePicker.show();

                        }
                    }
                });*/


            }
        });


    }

    private void SetupMonth(String year, String monthOfYear) {
        String date = year + "-" + monthOfYear;
        select_month.setText(date);

        if (count == 0) {
            utiltyRequest();
        }
        count = count + 1;
    }

    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case SHOW_PROG_DIALOG:
                    showProgDialog();
                    break;

                case HIDE_PROG_DIALOG:
                    hideProgDialog();
                    break;

                case LOAD_QUESTION_SUCCESS:

                    break;

                default:
                    break;
            }

            return false;
        }

    });


    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(act, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(act);
            }
            progress_dialog.setMessage(progress_dialog_msg);
            progress_dialog.setCancelable(false);

            progress_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hide progress
    private void hideProgDialog() {
        try {
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void utiltyRequest() {
//        if (page==0) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = getResources().getString(R.string.loading);
        //  }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        final ArrayList<New_Order_Entity> order_list = new ArrayList<>();

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                ////Log.e("Json Data", response.toString().substring(start1, end));
                            }

                            JSONArray jsonArray = obj.getJSONArray("all_payments");
                            ArrayList<All_Payment_Entity> list = new ArrayList<>();
                            paid = 0;
                            unpaid = 0;

                            ArrayList<Integer> color_list = new ArrayList<>();
                            ArrayList<PieEntry> y_list = new ArrayList<>();
                            ArrayList<String> x_list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                All_Payment_Entity payment_entity = new All_Payment_Entity();
                                payment_entity.setOrder_id(object.getString("order_id"));
                                payment_entity.setLast_modified_date(object.getString("last_modified_date"));
                                payment_entity.setOrder_cost(object.getString("order_cost"));

                                if (object.getString("payment_status").equals("UNPAID")) {
                                    unpaid = unpaid + Float.parseFloat(object.getString("order_cost"));
                                } else {
                                    paid = paid + Float.parseFloat(object.getString("order_cost"));
                                }
                                payment_entity.setPayment_status(object.getString("payment_status"));
                                list.add(payment_entity);

                            }
                            if (list.size() > 0) {
                                filter_txt.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(filter_type)) {
                                    if (filter_type.equals("date")) {
                                        filter_txt.setText("فلتر" + from_date.getText().toString() + "  " + to_date.getText().toString());
                                    } else if (filter_type.equals("month")) {

                                        filter_txt.setText("فلتر" + select_month.getText().toString());
                                    } else if (filter_type.equals("Current Month")) {
                                        filter_txt.setText("فلتر : " + "هذا الشهر");
                                    }
                                } else {
                                    filter_txt.setText("فلتر " + select_pay_type.getText().toString());
                                }


                                pie_chart.setVisibility(View.VISIBLE);

                                if (paid > 0) {
                                    y_list.add(new PieEntry(paid, "Paid"));
                                    x_list.add("PAID");
                                    color_list.add(Color.GREEN);
                                    x_list.add("UNPAID");
                                    color_list.add(Color.RED);
                                }
                                if (unpaid > 0) {
                                    y_list.add(new PieEntry(unpaid, "Unpaid"));
                                    x_list.add("UNPAID");
                                    color_list.add(Color.RED);
                                    x_list.add("PAID");
                                    color_list.add(Color.GREEN);
                                }


                                PieDataSet dataSet = new PieDataSet(y_list, "Cash Report");

                                dataSet.setSliceSpace(2);
                                dataSet.setValueTextSize(12);
                                dataSet.setColor(Color.WHITE);
                                dataSet.setValueLineColor(Color.WHITE);


                                dataSet.setColors(color_list);


                                Legend legend = pie_chart.getLegend();
                                legend.setForm(Legend.LegendForm.CIRCLE);
                                legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);


                                PieData pieData = new PieData(dataSet);
                                pie_chart.setData(pieData);
                                pie_chart.invalidate();
                            } else {
                                filter_txt.setText("لاتوجد بيانات");
                                pie_chart.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);

                        //}
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(Cash_Report.this, getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(Cash_Report.this, getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(Cash_Report.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(Cash_Report.this, getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Cashreportfilter");
                params.put("user_id", ssp.getUserId(act));


                params.put("user_type", customer);

                if (!TextUtils.isEmpty(filter_type)) {
                    if (filter_type.equals("date")) {
                        params.put("from_date", from_date.getText().toString());
                        params.put("to_date", to_date.getText().toString());
                        params.put("filter_type", "date");
                    } else if (filter_type.equals("month")) {
                        params.put("month", select_month.getText().toString());
                        params.put("filter_type", "month");
                    } else if (filter_type.equals("Current Month")) {
                        params.put("filter_type", "Current Month");
                    }
                } else {
                    params.put("filter_type", "");
                }
                params.put("tab_type", tab_name);


                ////Log.e("params", params.toString());
                return params;
            }

        };


//
// add(stringRequest);
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


}