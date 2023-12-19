package tameed.com.tameed.Util;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public abstract class Abstract_global {
    public abstract boolean abstract_checkinternetconnection_1(Context context);

    public abstract boolean abstract_checkinternetconnection_2(Context context);

    public abstract boolean abstract_checkinternetconnection_3(Context context);

    public abstract void abstract_toast(Activity act, final String message);

    public abstract void abstract_log(final String classname, final String message);

    public abstract void abstract_logsystem(final String classname, final String message);

    public abstract void abstract_datepicker(final Activity act, final TextView tv);

    public abstract String abstract_datepicker_format(final String dateformat);

    public abstract void abstract_customfontbutton(Context ctx, Button btn);

    public abstract void abstract_hidekeyboard(Activity act);

    public abstract void abstract_shownotification(String eventtext, Activity act);

    public abstract void abstract_fontbutton(Button button, String fonttype);

    public abstract void abstract_fonttextview(TextView textview, String fonttype);

    public abstract void abstract_fontedittext(EditText edittext, String fonttype);
}
