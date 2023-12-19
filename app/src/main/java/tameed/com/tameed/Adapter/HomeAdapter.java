package tameed.com.tameed.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tameed.com.tameed.Chat_window;
import tameed.com.tameed.DirectOrderDetails;
import tameed.com.tameed.Entity.Provider_List_Entity;
import tameed.com.tameed.R;
import tameed.com.tameed.RegisterActivity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * Created by USER on 1/12/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ContactViewHolder> {
    Context context;
    float rate;
    Boolean flag = false;
    SaveSharedPrefernces ssp;
    Boolean bool = false;
    ArrayList<String> service_id_list = new ArrayList<>();
    ArrayList<String> service_name_list = new ArrayList<>();
    ArrayList<Provider_List_Entity> provider_list = new ArrayList<>();
    HashMap<String, JSONArray> serviesmap;
    JSONArray jsonArray;
    String favourite;
    String serviceid, servicename = "", msg, mobile_number, provider_id;
    private ProgressDialog progress_dialog;
    Dialog dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String TAG = "Home Adapter";

    public HomeAdapter(Context context, ArrayList<Provider_List_Entity> provider_list, HashMap<String, JSONArray> servicemap) {
        this.context = context;
        this.provider_list = provider_list;
        this.serviesmap = servicemap;
        ssp = new SaveSharedPrefernces();
        //Log.e("HomeAdapter", "****************************");

    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cartView, call;
        ImageView home_chat, online_ofline_img, provider_img, home_fav, home_frwd, provider_star2, provider_star1, provider_star3, provider_star4, provider_star5;
        TextView provider_name, review_txt, distance_txt, service1_txt, service2_txt;

        public ContactViewHolder(View v) {
            super(v);
            cartView = (ConstraintLayout) itemView.findViewById(R.id.layout_home_adapter);
            call = itemView.findViewById(R.id.constraintLayout_call);
            home_chat = (ImageView) itemView.findViewById(R.id.home_chat);
            home_fav = (ImageView) itemView.findViewById(R.id.home_fav);
            home_frwd = (ImageView) itemView.findViewById(R.id.home_frwd);
            provider_name = itemView.findViewById(R.id.provider_name);
            review_txt = itemView.findViewById(R.id.review_txt);
            provider_star2 = itemView.findViewById(R.id.provider_star2);
            provider_star1 = itemView.findViewById(R.id.provider_star1);
            provider_star3 = itemView.findViewById(R.id.provider_star3);
            provider_star4 = itemView.findViewById(R.id.provider_star4);
            provider_star5 = itemView.findViewById(R.id.provider_star5);
            provider_img = itemView.findViewById(R.id.provider_img);
            online_ofline_img = itemView.findViewById(R.id.online_ofline_img);
            // service1_txt=itemView.findViewById(R.id.service1_txt);
            service2_txt = itemView.findViewById(R.id.service2_txt);
            distance_txt = itemView.findViewById(R.id.distance_txt);


        }

    }

    @Override
    public void onBindViewHolder(final ContactViewHolder itemViewHolder, @SuppressLint("RecyclerView") final int i) {

        String string_temp = new Double(provider_list.get(i).getDistance()).toString();
        String string_form = string_temp.substring(0, string_temp.indexOf('.'));
        double t = Double.valueOf(string_temp);
        String one = String.valueOf(Math.round(t));
        String two = "كيلومتر";
        String finalText = replaceArabicNumbers(one + two);
        itemViewHolder.distance_txt.setText(finalText);
        itemViewHolder.provider_name.setText(provider_list.get(i).getName());

        provider_id = provider_list.get(i).getUser_id();


        String image = provider_list.get(i).getProfile_pic_2xthumb_url();
        if (!image.equals("")) {
            Picasso.with(context)
                    .load(provider_list.get(i).getProfile_pic_2xthumb_url())
                    .noFade()
                    .error(R.mipmap.nouser_2x)
                    .into(itemViewHolder.provider_img);


        } else {
            itemViewHolder.provider_img.setImageResource(R.mipmap.nouser_2x);
        }

        jsonArray = serviesmap.get(provider_list.get(i).getUser_id());
        servicename = "";
        int size = 0;
        if (jsonArray != null) {
            if (jsonArray.length() < 3) {
                size = jsonArray.length();
            } else {
                size = 3;
            }
            for (int j = 0; j < size; j++) {
                try {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                    serviceid = jsonObject1.getString("user_service_id");
                    String service = jsonObject1.getString("service_name");
                    if (servicename.equals("")) {
                        servicename = service;
                    } else {
                        servicename = servicename + "," + service;
                    }

                } catch (JSONException e) {

                }
            }
            itemViewHolder.service2_txt.setText(servicename);

        }





        /*if (!my_count[0].equals("")) {
            itemViewHolder.service2_txt.setText(my_count[0]);
        } else if (!my_count[1].equals("")) {
            itemViewHolder.service2_txt.setText(my_count[1]);
        } else if (!my_count[2].equals("")) {
            itemViewHolder.service2_txt.setText(my_count[2]);
        } else if (!my_count[0].equals("") || !my_count[1].equals("")) {
            itemViewHolder.service2_txt.setText(service1+service2+service3);
           // itemViewHolder.service2_txt.setText(my_count[1]);
        }*/


        helper.services = String.valueOf(provider_list.get(i).getService_name_list());


        //itemViewHolder.service2_txt.setText(String.valueOf(service_name_list).toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));

        try {
            rate = Float.parseFloat(provider_list.get(i).getReview_rating());
        } catch (NumberFormatException ex) {
//                                    rate = 0.0; // default ??
        }

        if (provider_list.get(i).getOnline_offline_status().equals("0") || provider_list.get(i).getOnline_offline_status().equals("")) {
            itemViewHolder.online_ofline_img.setImageResource(R.mipmap.ic_offline);
            //Picasso.with(context).load(provider_list.get(i).getProfile_pic_2xthumb_url()).placeholder(R.mipmap.ic_online).into(itemViewHolder.online_ofline_img);
        } else {
            itemViewHolder.online_ofline_img.setImageResource(R.mipmap.ic_online);
        }


        if (provider_list.get(i).getIs_favourite().equals("no") || provider_list.get(i).getIs_favourite().equals("")) {
            favourite = "no";
            itemViewHolder.home_fav.setImageResource(R.mipmap.ic_fav);
            bool = true;
        } else {
            favourite = "yes";
            itemViewHolder.home_fav.setImageResource(R.drawable.ic_fav_active_2x);
            bool = false;
        }





      /*  if (rate == 1.0) {
            itemViewHolder.provider_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star2.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star3.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate == 2.0) {
            itemViewHolder.provider_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star2.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star3.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate == 3.0) {
            itemViewHolder.provider_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star2.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star3.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate == 4.0) {
            itemViewHolder.provider_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star2.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star3.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star4.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate == 5.0) {
            itemViewHolder.provider_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star2.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star3.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star4.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star5.setImageResource(R.drawable.star_active);
        }*/
        if (rate > 1.0 && rate < 2.0) {
            itemViewHolder.provider_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star2.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star3.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate > 2.0 && rate < 3.0) {
            itemViewHolder.provider_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star2.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star3.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate > 3.0 && rate < 4.0) {
            itemViewHolder.provider_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star2.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star3.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate > 4.0 && rate < 5.0) {
            itemViewHolder.provider_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star2.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star3.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star4.setImageResource(R.drawable.star_active);
            itemViewHolder.provider_star5.setImageResource(R.drawable.star_inactive);
        } else {
            itemViewHolder.provider_star1.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star2.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star3.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.provider_star5.setImageResource(R.drawable.star_inactive);
        }
        itemViewHolder.cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(ssp.getUserId(context))) {
                    helper.login = false;
                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(context);
                    }

                    alert.setMessage(context.getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(context, RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {

                    helper.login = true;
                    helper.service_list.clear();
                    helper.category_list.clear();
                    helper.category_idlist.clear();
                    helper.service_idlist.clear();
                    Intent intent = new Intent(context, DirectOrderDetails.class);
                    intent.putExtra("review_rating", provider_list.get(i).getReview_rating());
                    intent.putExtra("provider_id", provider_list.get(i).getUser_id());
                    intent.putExtra("profile_pic", provider_list.get(i).getProfile_pic_2xthumb_url());
                    intent.putExtra("name", provider_list.get(i).getName());

                    helper.service_list.addAll(provider_list.get(i).getService_name_list());
                    helper.service_idlist.addAll(provider_list.get(i).getService_id_list());
                    helper.category_idlist.addAll(provider_list.get(i).getCategory_id_list());
                    helper.category_list.addAll(provider_list.get(i).getCategory_name_list());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intent);

                }
            }
        });


        itemViewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ssp.getUserId(context).equals("")) {
                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(context);
                    }

                    alert.setMessage(context.getResources().getString(R.string.please_first_login));
                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(context, RegisterActivity.class));
                        }
                    });
                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else if (provider_list.get(i).getMobile_visible().equals("Yes")) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    //intent.setData(Uri.parse("tel:" + text_phone1.getText().toString()));
                    intent.setData(Uri.parse("tel:" + provider_list.get(i).getCombine_mobile()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{
                                Manifest.permission.CALL_PHONE
                        }, 1000);

                        return;
                    }
                    context.startActivity(intent);

                } else {

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(context);
                    }
                    alert.setMessage(provider_list.get(i).getName() + "  لاترغب بمشاركة رقمك");
                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();


                        }
                    });
                    dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();

                }
            }
        });


        itemViewHolder.home_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ssp.getUserId(context).equals("")) {

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(context);
                    }

                    alert.setMessage(context.getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(context, RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
//                    if (bool) {
////                                    if(favourite.equals("yes")) {
//                        itemViewHolder.home_fav.setImageResource(R.drawable.ic_fav_active_2x);
//                        favourite = "yes";
////                                    }
////                                    else
////                                    {
////                                        itemViewHolder.home_fav.setImageResource(R.mipmap.ic_fav);
////                                    }
//                    } else {
////                                    if(favourite.equals("no")) {
//                        itemViewHolder.home_fav.setImageResource(R.mipmap.ic_fav);
//                        favourite = "no";
////                                    }else
////                                    {
////                                        itemViewHolder.home_fav.setImageResource(R.drawable.ic_fav_active_2x);
////                                    }
//                    }
//                    bool = !bool;
                    makeStringReq(i,provider_list.get(i).getUser_id());
                }


            }
        });

        itemViewHolder.home_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inen = new Intent(context, Chat_window.class);

                //Log.e(TAG, "<------  Home Adapter------>");
                inen.putExtra("firstname", provider_list.get(i).getName());
                inen.putExtra("friend_id", provider_list.get(i).getUser_id());
                inen.putExtra("profile_pic", provider_list.get(i).getProfile_pic_2xthumb_url());
//                    i.putExtra("project_id",chat_list.get(position).getProject_id());
//                inen.putExtra("status","chat");
                context.startActivity(inen);
            }
        });
     /*   itemViewHolder.home_frwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/


    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //ImageView provider_img,imageCall,home_chat,home_fav,home_frwd;
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_home, viewGroup, false);
        return new ContactViewHolder(itemView);
    }


    @Override
    public int getItemCount() {
        return provider_list.size();
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


    private void makeStringReq(final int favPosition, String providerId) {
        mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);
        progress_dialog_msg = context.getResources().getString(R.string.loading);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            msg = obj.getString("msg");
                            if (msg.equals("Provider has been marked as favourite successfully")) {

                                provider_list.get(favPosition).setIs_favourite("yes");

                            } else if (msg.equals("Provider has been removed from favourite successfully")) {
                                provider_list.get(favPosition).setIs_favourite("no");
                            }

                            notifyItemChanged(favPosition);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
                        mHandler.sendEmptyMessage(LOAD_QUESTION_SUCCESS);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(context, context.getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(context, context.getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(context, context.getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                            //TODO
                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Makeproviderfavouriteunfavourite");
                params.put("user_id", ssp.getUserId(context));
                params.put("provider_id", providerId);
                // params.put("page", String.valueOf(page));
                ////Log.e("params", params.toString());
                return params;
            }

        };


        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(context);
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

    public String replaceArabicNumbers(String original) {
        return original.replaceAll("0", "٠")
                .replaceAll("1", "١")
                .replaceAll("2", "٢")
                .replaceAll("3", "٣")
                .replaceAll("4", "٤")
                .replaceAll("5", "٥")
                .replaceAll("6", "٦")
                .replaceAll("7", "٧")
                .replaceAll("8", "٨")
                .replaceAll("9", "٩");
    }
}

