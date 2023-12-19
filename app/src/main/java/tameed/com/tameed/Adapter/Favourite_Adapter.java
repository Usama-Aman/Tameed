package tameed.com.tameed.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tameed.com.tameed.Chat_window;
import tameed.com.tameed.DirectOrderDetails;
import tameed.com.tameed.Entity.Favourite_Entity;
import tameed.com.tameed.MainActivity;
import tameed.com.tameed.R;
import tameed.com.tameed.RegisterActivity;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * Created by dev on 18-01-2018.
 */

public class Favourite_Adapter extends RecyclerView.Adapter<Favourite_Adapter.MyViewHolder> {

    ArrayList<String> favourite_detail;
    HashMap<String, JSONArray> serviesmap;
    Context context;
    float rate;

    SaveSharedPrefernces ssp;
    private ProgressDialog progress_dialog;
    Dialog dialog;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    private String progress_dialog_msg = "", tag_string_req = "string_req", msg, provider_id;

    ArrayList<Favourite_Entity> favorite_list = new ArrayList<>();

    public Favourite_Adapter(Context context, ArrayList<Favourite_Entity> favorite_list, HashMap<String, JSONArray> servicemap) {
        this.context = context;
        this.serviesmap = servicemap;
        this.favorite_list = favorite_list;
        ssp = new SaveSharedPrefernces();
        //Log.e("Favourite_Adapter", "****************************");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_recycle, parent, false);
        Favourite_Adapter.MyViewHolder vh = new Favourite_Adapter.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder itemViewHolder, final int position) {

        itemViewHolder.favourite_name.setText(favorite_list.get(position).getName());
        provider_id = favorite_list.get(position).getUser_id();

        itemViewHolder.cart_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ////Log.e("22222", "Value");
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
                    intent.putExtra("review_rating", favorite_list.get(position).getReview_rating());
                    ////Log.e("rating_home", favorite_list.get(position).getReview_rating());
                    intent.putExtra("provider_id", favorite_list.get(position).getUser_id());
                    intent.putExtra("profile_pic", favorite_list.get(position).getProfile_pic_2xthumb_url());
                    intent.putExtra("name", favorite_list.get(position).getName());
                    helper.service_list.addAll(favorite_list.get(position).getService_name_list());
                    helper.service_idlist.addAll(favorite_list.get(position).getService_id_list());
                    helper.category_idlist.addAll(favorite_list.get(position).getCategory_id_list());
                    helper.category_list.addAll(favorite_list.get(position).getCategory_name_list());
                    context.startActivity(intent);
                }

            }
        });


        final String favourite_review_count = favorite_list.get(position).getReview_count();

        if (Integer.parseInt(favourite_review_count) > 0) {
            itemViewHolder.review_count.setVisibility(View.VISIBLE);
            itemViewHolder.review_count.setText("(" + favorite_list.get(position).getReview_count() + ")");
        } else {
            itemViewHolder.review_count.setVisibility(View.GONE);
        }

        String image = favorite_list.get(position).getProfile_pic_2xthumb_url();
        if (!image.equals("")) {
            Picasso.with(context)
                    .load(favorite_list.get(position).getProfile_pic_2xthumb_url())
                    .noFade()
                    .error(R.mipmap.nouser_2x)
                    .into(itemViewHolder.favourite_img);
        } else {
            itemViewHolder.favourite_img.setImageResource(R.mipmap.nouser_2x);
        }

        try {
            rate = Float.parseFloat(favorite_list.get(position).getReview_rating());
        } catch (NumberFormatException ex) {
//                                    rate = 0.0; // default ??
        }


        if (rate > 1.0 && rate < 2.0) {
            itemViewHolder.review_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.review_star2.setImageResource(R.drawable.star_inactive);
            itemViewHolder.review_star3.setImageResource(R.drawable.star_inactive);
            itemViewHolder.review_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.review_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate > 2.0 && rate < 3.0) {
            itemViewHolder.review_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.review_star2.setImageResource(R.drawable.star_active);
            itemViewHolder.review_star3.setImageResource(R.drawable.star_inactive);
            itemViewHolder.review_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.review_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate > 3.0 && rate < 4.0) {
            itemViewHolder.review_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.review_star2.setImageResource(R.drawable.star_active);
            itemViewHolder.review_star3.setImageResource(R.drawable.star_active);
            itemViewHolder.review_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.review_star5.setImageResource(R.drawable.star_inactive);
        } else if (rate > 4.0 && rate < 5.0) {
            itemViewHolder.review_star1.setImageResource(R.drawable.star_active);
            itemViewHolder.review_star2.setImageResource(R.drawable.star_active);
            itemViewHolder.review_star3.setImageResource(R.drawable.star_active);
            itemViewHolder.review_star4.setImageResource(R.drawable.star_active);
            itemViewHolder.review_star5.setImageResource(R.drawable.star_inactive);
        } else {
            itemViewHolder.review_star1.setImageResource(R.drawable.star_inactive);
            itemViewHolder.review_star2.setImageResource(R.drawable.star_inactive);
            itemViewHolder.review_star3.setImageResource(R.drawable.star_inactive);
            itemViewHolder.review_star4.setImageResource(R.drawable.star_inactive);
            itemViewHolder.review_star5.setImageResource(R.drawable.star_inactive);
        }


        itemViewHolder.review_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(context);
                }

                alert.setMessage(context.getResources().getString(R.string.msg_do_you_want_to_remove) + " " +
                        favorite_list.get(position).getName() + context.getResources().getString(R.string.msg_from) + " " + context.getResources().getString(R.string.msg_favourite_list));


                alert.setPositiveButton("إزالة", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (AppController.isOnline(context)) {
                            favorite_list.remove(position);
                            notifyItemRemoved(position);
                            makeStringReq();
                        } else {
                            AppController.showAlert((Activity) context, context.getString(R.string.networkError_Message));
                        }


                        notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });

                alert.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                Dialog dialog = alert.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

               /* final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(context);
                }

                alert.setMessage("Do you want to remove this from favourite list?.");


                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        makeStringReq();

                       // notifyDataSetChanged();



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
                dialog.show();*/

            }
        });


        itemViewHolder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inen = new Intent(context, Chat_window.class);

                inen.putExtra("firstname", favorite_list.get(position).getName());
                inen.putExtra("friend_id", favorite_list.get(position).getUser_id());
                inen.putExtra("profile_pic", favorite_list.get(position).getProfile_pic_2xthumb_url());
//                    i.putExtra("project_id",chat_list.get(position).getProject_id());
                inen.putExtra("status", "chat");
                context.startActivity(inen);
            }
        });
    }


    @Override
    public int getItemCount() {
        return favorite_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView chat, review_delete, forward, favourite_img, review_star1, review_star2, review_star3, review_star4, review_star5;

        TextView review_count, favourite_name;
        ConstraintLayout cart_favourite;

        public MyViewHolder(View itemView) {
            super(itemView);

            review_count = itemView.findViewById(R.id.review_count);

            favourite_img = itemView.findViewById(R.id.favourite_img);
            cart_favourite = itemView.findViewById(R.id.cart_favourite);
            chat = (ImageView) itemView.findViewById(R.id.review_chat);
            review_delete = (ImageView) itemView.findViewById(R.id.review_delete);
            forward = (ImageView) itemView.findViewById(R.id.review_forward);
            review_star1 = itemView.findViewById(R.id.review_star1);
            review_star2 = itemView.findViewById(R.id.review_star2);
            review_star3 = itemView.findViewById(R.id.review_star3);
            review_star4 = itemView.findViewById(R.id.favourite_star4);
            review_star5 = itemView.findViewById(R.id.favourite_star5);
            favourite_name = itemView.findViewById(R.id.favourite_name);

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
                        ////Log.e("Message5555", response);

                        try {
                            msg = obj.getString("msg");
                            if (msg.equals("Provider has been removed from favourite successfully")) {
                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(context);
                                }
                                alert.setMessage(context.getResources().getString(R.string.msg_provider_has_been_removed_from_favourite_successfully));
                                alert.setPositiveButton(" نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //context.startActivity(new Intent(context, MainActivity.class));
                                        Intent i = new Intent(context, MainActivity.class);
                                        helper.favourite_list = 7;
                                        context.startActivity(i);


                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();

                            }


//                            else if(msg.equals("Provider has been removed from favourite successfully"))
//                            {
//                                context.startActivity(new Intent(context, MainActivity.class));
//
//
//                            }
//
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//
//


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
