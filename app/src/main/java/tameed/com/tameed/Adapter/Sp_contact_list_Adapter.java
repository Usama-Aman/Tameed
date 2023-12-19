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
import tameed.com.tameed.Entity.Direct_T_Entity;
import tameed.com.tameed.R;
import tameed.com.tameed.RegisterActivity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.SaveSharedPrefernces;
import tameed.com.tameed.Util.URLogs;
/**
 * Created by dev on 17-01-2018.
 */

public class Sp_contact_list_Adapter extends RecyclerView.Adapter<Sp_contact_list_Adapter.MyViewHOlder> {

    Context context;
    HashMap<String, JSONArray> serviesmap;
    ArrayList<Direct_T_Entity> direct_list = new ArrayList<>();
    ArrayList<String> contact_list;
    float rate;
    Dialog dialog;
    String favourite;
    Boolean bool = false;
    SaveSharedPrefernces ssp;
    JSONArray jsonArray;
    private ProgressDialog progress_dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req";
    String serviceid, servicename = "", msg, mobile_number, provider_id;

    public Sp_contact_list_Adapter(Context context, ArrayList<Direct_T_Entity> direct_list, HashMap<String, JSONArray> servicemap) {
        this.direct_list = direct_list;
        this.serviesmap = servicemap;
        this.context = context;

        ssp = new SaveSharedPrefernces();
        URLogs.m1("Sp_contact_list_Adapter"+"****************************");


    }

    @Override
    public Sp_contact_list_Adapter.MyViewHOlder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_contact_list_recycle, parent, false);
        MyViewHOlder vh = new MyViewHOlder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final Sp_contact_list_Adapter.MyViewHOlder holder, final int position) {

        String string_temp = new Double(direct_list.get(position).getDistance()).toString();
        String string_form = string_temp.substring(0, string_temp.indexOf('.'));
        double t = Double.valueOf(string_form);
        holder.d_distance.setText(t + " KM");
        holder.d_sp_name.setText(direct_list.get(position).getName());
        provider_id = direct_list.get(position).getUser_id();


        holder.layout_sp_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.service_list.clear();
                ;
                helper.category_list.clear();
                helper.category_idlist.clear();
                helper.service_idlist.clear();
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
                    Intent intent = new Intent(context, DirectOrderDetails.class);
                    intent.putExtra("review_rating", direct_list.get(position).getReview_rating());
                    ////Log.e("rating_home", direct_list.get(position).getReview_rating());
                    intent.putExtra("provider_id", direct_list.get(position).getUser_id());
                    intent.putExtra("profile_pic", direct_list.get(position).getProfile_pic_2xthumb_url());
                    intent.putExtra("name", direct_list.get(position).getName());

                    //Nardeep Edit
                    //        intent.putExtra("status","chat");
                    //Log.e("get Category List....", "....." + direct_list.get(position).getCategory_id_list().size());
                    helper.service_list.addAll(direct_list.get(position).getService_name_list());
                    helper.service_idlist.addAll(direct_list.get(position).getService_id_list());
                    helper.category_idlist.addAll(direct_list.get(position).getCategory_id_list());
                    helper.category_list.addAll(direct_list.get(position).getCategory_name_list());
                    context.startActivity(intent);
                }
            }
        });


        String image = direct_list.get(position).getProfile_pic_2xthumb_url();
        if (!image.equals("")) {
            Picasso.with(context)
                    .load(direct_list.get(position).getProfile_pic_2xthumb_url())
                    .noFade()
                    .error(R.mipmap.nouser_2x)
                    .into(holder.d_image);
        } else {
            holder.d_image.setImageResource(R.mipmap.nouser_2x);
        }
        jsonArray = serviesmap.get(direct_list.get(position).getUser_id());
        servicename = "";
        for (int j = 0; j < 3; j++) {
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
            holder.d_service.setText(servicename);

        }
        try {
            rate = Float.parseFloat(direct_list.get(position).getReview_rating());
        } catch (NumberFormatException ex) {
//                                    rate = 0.0; // default ??
        }

        if (direct_list.get(position).getOnline_offline_status().equals("0") || direct_list.get(position).getOnline_offline_status().equals("")) {
            holder.sp_online_img.setImageResource(R.mipmap.ic_offline);
            //Picasso.with(context).load(provider_list.get(i).getProfile_pic_2xthumb_url()).placeholder(R.mipmap.ic_online).into(itemViewHolder.online_ofline_img);
        } else {
            holder.sp_online_img.setImageResource(R.mipmap.ic_online);
        }
        if (direct_list.get(position).getIs_favourite().equals("no") || direct_list.get(position).getIs_favourite().equals("")) {
            favourite = "no";
            holder.sp_favourite.setImageResource(R.mipmap.ic_fav);
            bool = true;
        } else {
            favourite = "yes";
            holder.sp_favourite.setImageResource(R.mipmap.ic_fav);
            bool = false;
        }


        if (rate == 1.0) {
            holder.d_star1.setImageResource(R.drawable.star_active);
            holder.d_star2.setImageResource(R.drawable.star_inactive);
            holder.d_star3.setImageResource(R.drawable.star_inactive);
            holder.d_star4.setImageResource(R.drawable.star_inactive);
            holder.d_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate == 2.0) {
            holder.d_star1.setImageResource(R.drawable.star_active);
            holder.d_star2.setImageResource(R.drawable.star_active);
            holder.d_star3.setImageResource(R.drawable.star_inactive);
            holder.d_star4.setImageResource(R.drawable.star_inactive);
            holder.d_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate == 3.0) {
            holder.d_star1.setImageResource(R.drawable.star_active);
            holder.d_star2.setImageResource(R.drawable.star_active);
            holder.d_star3.setImageResource(R.drawable.star_active);
            holder.d_star4.setImageResource(R.drawable.star_inactive);
            holder.d_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate == 4.0) {
            holder.d_star1.setImageResource(R.drawable.star_active);
            holder.d_star2.setImageResource(R.drawable.star_active);
            holder.d_star3.setImageResource(R.drawable.star_active);
            holder.d_star4.setImageResource(R.drawable.star_active);
            holder.d_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate == 5.0) {
            holder.d_star1.setImageResource(R.drawable.star_active);
            holder.d_star2.setImageResource(R.drawable.star_active);
            holder.d_star3.setImageResource(R.drawable.star_active);
            holder.d_star4.setImageResource(R.drawable.star_active);
            holder.d_star5.setImageResource(R.drawable.star_active);
        } /*else if (rate > 1.0 && rate < 2.0) {
            holder.d_star1.setImageResource(R.drawable.star_active);
            holder.d_star2.setImageResource(R.drawable.star_half);
            holder.d_star3.setImageResource(R.drawable.star_inactive);
            holder.d_star4.setImageResource(R.drawable.star_inactive);
            holder.d_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate > 2.0 && rate < 3.0) {
            holder.d_star1.setImageResource(R.drawable.star_active);
            holder.d_star2.setImageResource(R.drawable.star_active);
            holder.d_star3.setImageResource(R.drawable.star_half);
            holder.d_star4.setImageResource(R.drawable.star_inactive);
            holder.d_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate > 3.0 && rate < 4.0) {
            holder.d_star1.setImageResource(R.drawable.star_active);
            holder.d_star2.setImageResource(R.drawable.star_active);
            holder.d_star3.setImageResource(R.drawable.star_active);
            holder.d_star4.setImageResource(R.drawable.star_active);
            holder.d_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate > 4.0 && rate < 5.0) {
            holder.d_star1.setImageResource(R.drawable.star_active);
            holder.d_star2.setImageResource(R.drawable.star_active);
            holder.d_star3.setImageResource(R.drawable.star_active);
            holder.d_star4.setImageResource(R.drawable.star_active);
            holder.d_star5.setImageResource(R.drawable.star_half);
        }*/ else {
            holder.d_star1.setImageResource(R.drawable.star_inactive);
            holder.d_star2.setImageResource(R.drawable.star_inactive);
            holder.d_star3.setImageResource(R.drawable.star_inactive);
            holder.d_star4.setImageResource(R.drawable.star_inactive);
            holder.d_star5.setImageResource(R.drawable.star_inactive);
        }
        /*itemViewHolder.cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, DirectOrderDetails.class);
                context.startActivity(intent);
            }
        });*/


        holder.sp_call.setOnClickListener(new View.OnClickListener() {
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
                } else if (direct_list.get(position).getMobile_visible().equals("Yes")) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    //intent.setData(Uri.parse("tel:" + text_phone1.getText().toString()));
                    intent.setData(Uri.parse("tel:" + direct_list.get(position).getCombine_mobile()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
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
                    alert.setMessage(direct_list.get(position).getName() + "  " + context.getResources().getString(R.string.msg_dont_want_to_share_mobile_number));
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









    /*    holder.sp_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:123456789"));
                context.startActivity(callIntent);
            }
        });*/

        holder.sp_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inen = new Intent(context, Chat_window.class);

                inen.putExtra("firstname", direct_list.get(position).getName());
                inen.putExtra("friend_id", direct_list.get(position).getUser_id());
                inen.putExtra("profile_pic", direct_list.get(position).getProfile_pic_2xthumb_url());
//                    i.putExtra("project_id",chat_list.get(position).getProject_id());
                inen.putExtra("status", "chat");

                context.startActivity(inen);

            }
        });

        holder.sp_favourite.setOnClickListener(new View.OnClickListener() {
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
                    if (bool) {
//                                    if(favourite.equals("yes")) {
                        holder.sp_favourite.setImageResource(R.drawable.ic_fav_active_2x);
                        favourite = "yes";
//                                    }
//                                    else
//                                    {
//                                        itemViewHolder.home_fav.setImageResource(R.mipmap.ic_fav);
//                                    }
                    } else {
//                                    if(favourite.equals("no")) {
                        holder.sp_favourite.setImageResource(R.mipmap.ic_fav);
                        favourite = "no";
//                                    }else
//                                    {
//                                        itemViewHolder.home_fav.setImageResource(R.drawable.ic_fav_active_2x);
//                                    }
                    }
                    bool = !bool;
                    makeStringReq();
                }


            }
        });


      /*  holder.sp_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(context);
                }

                alert.setMessage("Do you want to make as favourite");


                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();



                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

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
*/

    }

    @Override
    public int getItemCount() {
        return direct_list.size();
    }

    public class MyViewHOlder extends RecyclerView.ViewHolder {
        ImageView sp_call, sp_chat, sp_favourite, d_image, sp_online_img;
        TextView d_sp_name, d_distance, d_service;
        ImageView d_star1, d_star2, d_star3, d_star4, d_star5;
        ConstraintLayout layout_sp_adapter;

        public MyViewHOlder(View itemView) {
            super(itemView);
            sp_call = (ImageView) itemView.findViewById(R.id.sp_call);
            sp_chat = (ImageView) itemView.findViewById(R.id.sp_chat);
            sp_favourite = (ImageView) itemView.findViewById(R.id.sp_favourite);
            d_sp_name = itemView.findViewById(R.id.d_sp_name);
            d_star1 = itemView.findViewById(R.id.d_star1);
            d_star2 = itemView.findViewById(R.id.d_star2);
            d_star3 = itemView.findViewById(R.id.d_star3);
            d_star4 = itemView.findViewById(R.id.d_star4);
            d_star5 = itemView.findViewById(R.id.d_star5);
            d_distance = itemView.findViewById(R.id.d_distance);
            d_image = itemView.findViewById(R.id.d_image);
            d_service = itemView.findViewById(R.id.d_service);
            sp_online_img = itemView.findViewById(R.id.sp_online_img);
            layout_sp_adapter = itemView.findViewById(R.id.layout_sp_adapter);


        }


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


    private void makeStringReq() {
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

                            } else if (msg.equals("Provider has been removed from favourite successfully")) {

                            }

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
                params.put("provider_id", provider_id);
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


}
