package tameed.com.tameed.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tameed.com.tameed.Entity.Notification_entity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * Created by dev on 16-01-2018.
 */
public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.MyViewHolder> {


    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    SaveSharedPrefernces ssp;
    ArrayList<Notification_entity> Notification_detail;
    Context context;
    private ProgressDialog progress_dialog;
    private String progress_dialog_msg = "", tag_string_req = "string_req", message;

    public Notification_Adapter(Context context, ArrayList<Notification_entity> Notification_detail) {

        this.context = context;
        this.Notification_detail = Notification_detail;
        //Log.e("Notification_Adapter", "****************************");

    }

    @Override
    public Notification_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycle, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(Notification_Adapter.MyViewHolder holder, final int position) {

        ssp = new SaveSharedPrefernces();

        holder.notification_detail.setText(Notification_detail.get(position).getNotification_message());
        holder.notification_date.setText(Notification_detail.get(position).getAdded_date());

        holder.delete_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(context);
                }

                alert.setMessage("هل ترغب بمسح هذا الإشعار");


                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Notification_detail.remove(position);
                        delete_notification();
                        notifyDataSetChanged();


                    }
                });

                alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                Dialog dialog = alert.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return Notification_detail.size();
    }

    public void delete_notification() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            message = obj.getString("msg");
                            ////Log.e("response", "" + obj);


                            if (message.equals("Notification has been cleared successfully")) {


                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(context);
                                }

                                alert.setMessage("تم حذف جميع الإشعارات");


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        dialog.dismiss();


                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();


                            }


//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(context, context.getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(context, context.getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(context, context.getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "Clearnotifications");
                params.put("user_id", ssp.getUserId(context));
                params.put("delete_by", "single");
                params.put("notification_id", ssp.getNotification_id(context));

                ////Log.e("admin call", params.toString());

                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notification_detail, notification_date;
        ImageView delete_notification;

        public MyViewHolder(View itemView) {
            super(itemView);

            delete_notification = (ImageView) itemView.findViewById(R.id.delete_notification);
            notification_date = (TextView) itemView.findViewById(R.id.notification_date);
            notification_detail = (TextView) itemView.findViewById(R.id.tvNotification);

        }
    }

}




