package tameed.com.tameed.Util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class Concreate_global extends Abstract_global {

    //protected Abstract_common concreateobj_common;
    private Typeface typeface;

    //TODO Calendar
    protected Calendar calendar = Calendar.getInstance();
    protected int mYear = calendar.get(Calendar.YEAR);
    protected int mMonth = calendar.get(Calendar.MONTH);
    protected int mDay = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    public void abstract_toast(Activity act, String message) {
        Toast.makeText(act.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void abstract_log(final String classname, final String message) {
        ////Log.e(classname, message);
    }

    @Override
    public void abstract_logsystem(String classname, String message) {
        System.out.println(classname + " = " + message);
    }

    @Override
    public boolean abstract_checkinternetconnection_1(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.w("internet", "Internet Connection Not Present");
            return false;
        }
    }

    @Override
    public boolean abstract_checkinternetconnection_2(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public boolean abstract_checkinternetconnection_3(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    @Override
    public void abstract_customfontbutton(Context ctx, Button btn) {
        typeface = Typeface.createFromAsset(ctx.getAssets(),
                "Font/HelveticaNeueW23foSKY.ttf");
        btn.setTypeface(typeface);
    }

    @Override
    public void abstract_hidekeyboard(Activity act) {
        View view = act.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void abstract_shownotification(String eventtext, Activity act) {

    }

    @Override
    public void abstract_fontbutton(Button button, String fonttype) {
        typeface = Typeface.createFromAsset(button.getContext().getAssets(), fonttype);
        button.setTypeface(typeface);
    }

    @Override
    public void abstract_fonttextview(TextView textview, String fonttype) {
        typeface = Typeface.createFromAsset(textview.getContext().getAssets(), fonttype);
        textview.setTypeface(typeface);
    }

    @Override
    public void abstract_fontedittext(EditText edittext, String fonttype) {
        typeface = Typeface.createFromAsset(edittext.getContext().getAssets(), fonttype);
        edittext.setTypeface(typeface);
    }

    @Override
    public void abstract_datepicker(final Activity act, final TextView tv) {
      //  concreateobj_common = new Concreate_common();

        final int zeroaddmonth, zeroadddate;

        DatePickerDialog dpd = new DatePickerDialog(act,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        if (String.valueOf(dayOfMonth).length() != 2 || String.valueOf(monthOfYear).length() != 2) {
                         //   tv.setText(concreateobj_common.abstract_datezeroadd(dayOfMonth) + "/" + concreateobj_common.abstract_datezeroadd(monthOfYear + 1) + "/" + year);
                            tv.setTextColor(Color.parseColor("#000000"));
                        } else {
                            tv.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            tv.setTextColor(Color.parseColor("#000000"));
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    @Override
    public String abstract_datepicker_format(String dateformat) {
       // concreateobj_common = new Concreate_common();

        switch (dateformat) {
            case "dd/mm/yyyy/zeroadd": {
         //       String dd_mm_yyyy_zeroadd = concreateobj_common.abstract_datezeroadd(mDay) + "/" + concreateobj_common.abstract_datezeroadd(mMonth + 1) + "/" + mYear;
           //     return dd_mm_yyyy_zeroadd;
            }
        }

        return null;
    }
}