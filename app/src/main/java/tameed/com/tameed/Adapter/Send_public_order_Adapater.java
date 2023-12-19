package tameed.com.tameed.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tameed.com.tameed.Entity.Public_Room_Entity;
import tameed.com.tameed.My_services;
import tameed.com.tameed.Oder_detail_provider;
import tameed.com.tameed.Order_detail;
import tameed.com.tameed.R;
import tameed.com.tameed.RegisterActivity;
import tameed.com.tameed.Util.SaveSharedPrefernces;
import tameed.com.tameed.Util.URLogs;
/**
 * Created by dev on 23-01-2018.
 */
public class Send_public_order_Adapater extends RecyclerView.Adapter<Send_public_order_Adapater.MyViewHolder> {

    ArrayList<String> public_room;
    Context context;
    SaveSharedPrefernces ssp;
    ArrayList<Public_Room_Entity> public_room_list = new ArrayList<>();

    public Send_public_order_Adapater(Context context, ArrayList<Public_Room_Entity> public_room_list) {
        this.public_room_list = public_room_list;
        this.context = context;
        ssp = new SaveSharedPrefernces();
        ////Log.e("Send_public_Adapater", "****************************");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_public_room, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txt_public_room_name.setVisibility(View.VISIBLE);
        if (public_room_list.get(position).getIs_yours().equals("no")) {
            URLogs.m1("other: " + public_room_list.get(position).getOrder_reference_number());
            holder.txt_public_room_name.setText(public_room_list.get(position).getName());

        }

        if (public_room_list.get(position).getIs_yours().equals("yes")) {
            //////Log.e("2222222", "6666666");
//            holder.your_order.setVisibility(View.VISIBLE);
            holder.order_no.setText("معاملة:" + "#" + public_room_list.get(position).getOrder_reference_number());

        } else {
            holder.your_order.setVisibility(View.GONE);
            holder.order_no.setText("معاملة" + "#" + public_room_list.get(position).getOrder_reference_number());
        }
        holder.order_date.setText("التاريخ" + public_room_list.get(position).getAdded_date());
        holder.main_category.setText(public_room_list.get(position).getCategory_name() + "," + public_room_list.get(position).getService_name());

        if (public_room_list.get(position).getOrder_status().equals("NEW")) {
            holder.order_type.setText("جديد");
        }
        String str = public_room_list.get(position).getAdded_date();
        String[] splitStr = str.split("\\s+");
        String date1 = splitStr[0];
        String date2 = splitStr[1];
//        splitStr[0] = "";
//        splitStr[1] ="Hello";

//        String s=order_list.get(position).getServing_date();
        String[] parts = date1.split("\\-"); // escape .
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];

        holder.order_date.setText("التاريخ" + part3 + "/" + part2 + "/" + part1);

        holder.send_to_porder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    if (public_room_list.get(position).getIs_yours().equals("yes")) {
                        Intent i = new Intent(context, Order_detail.class);
                        helper.pblc = 0;
                        i.putExtra("order_id", public_room_list.get(position).getOrder_id());
                        context.startActivity(i);

                    } else {
                        //////Log.e("PROVIDER", "======" + ssp.getUser_type(context));
                        if (ssp.getUser_type(context).equals("Provider")) {
                            Intent i = new Intent(context, Oder_detail_provider.class);
                            helper.pblc = 1;
                            i.putExtra("order_id", public_room_list.get(position).getOrder_id());
                            context.startActivity(i);
                        } else if (ssp.getUser_type(context).equals("Customer")) {
                            final AlertDialog.Builder alert;
                            if (Build.VERSION.SDK_INT >= 11) {
                                alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                            } else {
                                alert = new AlertDialog.Builder(context);
                            }

                            alert.setMessage("سجل كمقدم خدمة لقبول المعاملة");


                            alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            alert.setNegativeButton("تسجيل", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //helper.tab = 0;

                                    Intent i = new Intent(context, My_services.class);
                                    i.putExtra("p_room", "1");
                                    context.startActivity(i);
                                    dialog.dismiss();

                                }
                            });
                            Dialog dialog = alert.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.show();
                        }
                    }
                }

            }
        });
    /*    holder.send_to_porder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.send_private=0;
                helper.pblc=1;
                context.startActivity(new Intent(context,MainActivity.class));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return public_room_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout send_to_porder;
        TextView order_no, order_date, main_category, order_type, your_order, txt_public_room_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            send_to_porder = (ConstraintLayout) itemView.findViewById(R.id.send_to_porder);
            order_type = itemView.findViewById(R.id.order_type);
            order_no = itemView.findViewById(R.id.order_no);
            order_date = itemView.findViewById(R.id.order_date);
            main_category = itemView.findViewById(R.id.main_category);
            your_order = itemView.findViewById(R.id.your_order);
            txt_public_room_name = itemView.findViewById(R.id.txt_public_room_name);

        }
    }
}
