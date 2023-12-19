package tameed.com.tameed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tameed.com.tameed.Entity.New_Order_Entity;
import tameed.com.tameed.Oder_detail_provider;
import tameed.com.tameed.Order_detail;
import tameed.com.tameed.R;
import tameed.com.tameed.RatingActivity;
/**
 * Created by dev on 22-01-2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    ArrayList<New_Order_Entity> order_detail;
    Context context;
    double ratting;

    public OrderAdapter(Context context, ArrayList<New_Order_Entity> order_detail) {
        this.context = context;
        this.order_detail = order_detail;
        //Log.e("OrderAdapter", "****************************");

        //ADD ME CHECK
        /*if (helper.pblc == 0)
        {
            //Log.e("OrderAdapter", "*************Customer***************");
        }
        else
        {
            //Log.e("OrderAdapter", "***************Provider*************");
        }*/

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {

        if (order_detail.get(position).getOrder_type().equals("public")) {
            holder.btn_pub_pri.setBackgroundResource(R.drawable.btn_public);
            holder.btn_pub_pri.setText("عام");
        } else {
            holder.btn_pub_pri.setBackgroundResource(R.drawable.btn_private_red);
            holder.btn_pub_pri.setText("خاص");

        }


        if (helper.list_type.equals("Pending")) {
            //REMOVE JIGS
           /* if (order_detail.get(position).getOrder_status().equals("New"))
            {
                holder.txt_status.setText("مقبول");
                holder.txt_status.setTextColor(context.getResources().getColor(R.color.Accept));
            } else {
                holder.txt_status.setTextColor(context.getResources().getColor(R.color.Accept));
                holder.txt_status.setText("تمت الموافقة");
            }*/


            //ADD ME CHECK  //ADD JIGS
            if (helper.pblc == 0) {
                ////Log.e("OrderAdapter", "*************Customer***************");
                if (order_detail.get(position).getOrder_status().equals("New")) {
                    //holder.txt_status.setText("بانتظار الموافقة"+"-"+"Awaiting approval");
                    holder.txt_status.setText("بانتظار الموافقة");
                    holder.txt_status.setTextColor(context.getResources().getColor(R.color.Accept));
                } else {
                    holder.txt_status.setTextColor(context.getResources().getColor(R.color.Accept));
                   // holder.txt_status.setText("تمت الموافقة"+"-"+"Has been approved");
                    holder.txt_status.setText("تمت الموافقة");
                }
            } else {
                ////Log.e("OrderAdapter", "***************Provider*************");
                if (order_detail.get(position).getOrder_status().equals("New")) {
                   // holder.txt_status.setText("تم ارسال المعاملة"+"-"+"request sent");
                    holder.txt_status.setText("تم ارسال المعاملة");
                    holder.txt_status.setTextColor(context.getResources().getColor(R.color.Accept));
                } else {
                    holder.txt_status.setTextColor(context.getResources().getColor(R.color.Accept));
                    //holder.txt_status.setText("تمت الموافقة"+"-"+"Has been approved");
                    holder.txt_status.setText("تمت الموافقة");
                }
            }


        } else if (helper.list_type.equals("Completed")) {
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.Accept));
            holder.txt_status.setText("مكتمل");
            if (order_detail.get(position).getIs_review_given().equals("yes")) {
                holder.ratting_layout.setVisibility(View.VISIBLE);
                ratting = Double.parseDouble(order_detail.get(position).getOrder_rating());
                if (ratting >= 0.0 && ratting < 1.0) {
                    holder.star1.setImageResource(R.mipmap.star_inactive);
                    holder.star2.setImageResource(R.mipmap.star_inactive);
                    holder.star3.setImageResource(R.mipmap.star_inactive);
                    holder.star4.setImageResource(R.mipmap.star_inactive);
                    holder.star5.setImageResource(R.mipmap.star_inactive);
                } else if (ratting >= 1.0 && ratting < 2.0) {
                    holder.star1.setImageResource(R.mipmap.star_active);
                    holder.star2.setImageResource(R.mipmap.star_inactive);
                    holder.star3.setImageResource(R.mipmap.star_inactive);
                    holder.star4.setImageResource(R.mipmap.star_inactive);
                    holder.star5.setImageResource(R.mipmap.star_inactive);
                } else if (ratting >= 2.0 && ratting < 3.0) {
                    holder.star1.setImageResource(R.mipmap.star_active);
                    holder.star2.setImageResource(R.mipmap.star_active);
                    holder.star3.setImageResource(R.mipmap.star_inactive);
                    holder.star4.setImageResource(R.mipmap.star_inactive);
                    holder.star5.setImageResource(R.mipmap.star_inactive);
                } else if (ratting >= 3.0 && ratting < 4.0) {
                    holder.star1.setImageResource(R.mipmap.star_active);
                    holder.star2.setImageResource(R.mipmap.star_active);
                    holder.star3.setImageResource(R.mipmap.star_active);
                    holder.star4.setImageResource(R.mipmap.star_inactive);
                    holder.star5.setImageResource(R.mipmap.star_inactive);
                } else if (ratting >= 4.0 && ratting < 5.0) {
                    holder.star1.setImageResource(R.mipmap.star_active);
                    holder.star2.setImageResource(R.mipmap.star_active);
                    holder.star3.setImageResource(R.mipmap.star_active);
                    holder.star4.setImageResource(R.mipmap.star_active);
                    holder.star5.setImageResource(R.mipmap.star_inactive);
                } else if (ratting >= 5.0) {
                    holder.star1.setImageResource(R.mipmap.star_active);
                    holder.star2.setImageResource(R.mipmap.star_active);
                    holder.star3.setImageResource(R.mipmap.star_active);
                    holder.star4.setImageResource(R.mipmap.star_active);
                    holder.star5.setImageResource(R.mipmap.star_active);
                } else {
                    holder.star1.setImageResource(R.mipmap.star_inactive);
                    holder.star2.setImageResource(R.mipmap.star_inactive);
                    holder.star3.setImageResource(R.mipmap.star_inactive);
                    holder.star4.setImageResource(R.mipmap.star_inactive);
                    holder.star5.setImageResource(R.mipmap.star_inactive);
                }
                holder.ratting_txt.setText("قيم الخدمة");

            } else {
                if (helper.pblc == 0) {
                    holder.ratting_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.ratting_layout.setVisibility(View.GONE);
                }
                holder.ratting_txt.setText("قيم الخدمة");
                holder.star_layout.setVisibility(View.GONE);
                holder.ratting_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, RatingActivity.class);
                        intent.putExtra("order_id", order_detail.get(position).getOrder_id());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                    }
                });

            }

        } else if (helper.list_type.equals("New")) {
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.Awaiting));
            holder.txt_status.setText("تحت الإنتظار");
        } else if (helper.list_type.equals("Rejected")) {
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.Rejct));
            holder.txt_status.setText("رفض");
        } else {
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.Rejct));
            //holder.txt_status.setText(order_detail.get(position).getOrder_status());
            //ADD JIGS
            holder.txt_status.setText(context.getString(R.string.Status_Cancelled));
        }
        if (helper.pblc == 0) {
            holder.txt_service_provider_name.setText("معاملتي");
        } else {
            holder.txt_service_provider_name.setText(order_detail.get(position).getName());
        }
        holder.txt_category.setText("معاملة#" + order_detail.get(position).getOrder_reference_number());
        holder.order_number_txt.setText("الخدمة:" + order_detail.get(position).getService_name());
        holder.txt_service_name.setText("الاسم الفرعي:" + order_detail.get(position).getService_name());


        //   holder.txt_category.setText(order_detail.get(position).getCategory_name());
        holder.layout_order_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helper.pblc == 0) {
                    //Log.e("open1111","....");
                    Intent intent = new Intent(context, Order_detail.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("order_id", order_detail.get(position).getOrder_id());
                    context.startActivity(intent);
                } else {
                    //Log.e("open2222","....");
                    Intent intent = new Intent(context, Oder_detail_provider.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("order_id", order_detail.get(position).getOrder_id());
                    context.startActivity(intent);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return order_detail.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder

    {

        Button btn_pub_pri;
        TextView order_number_txt, txt_category, txt_service_name, txt_status;
        ConstraintLayout ratting_layout, star_layout;
        ImageView star1, star2, star3, star4, star5;
        TextView ratting_txt, txt_service_provider_name;
        ConstraintLayout layout_order_adapter;

        public MyViewHolder(View itemView) {
            super(itemView);

            btn_pub_pri = (Button) itemView.findViewById(R.id.btn_pub_private);
            txt_category = (TextView) itemView.findViewById(R.id.textView29);
            order_number_txt = (TextView) itemView.findViewById(R.id.textView34);
            txt_service_name = (TextView) itemView.findViewById(R.id.textView30);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            ratting_txt = (TextView) itemView.findViewById(R.id.ratting_txt);
            txt_service_provider_name = (TextView) itemView.findViewById(R.id.txt_service_provider_name);
            star1 = (ImageView) itemView.findViewById(R.id.star1);
            star2 = (ImageView) itemView.findViewById(R.id.star2);
            star3 = (ImageView) itemView.findViewById(R.id.star3);
            star4 = (ImageView) itemView.findViewById(R.id.star4);
            star5 = (ImageView) itemView.findViewById(R.id.star5);
            ratting_layout = (ConstraintLayout) itemView.findViewById(R.id.ratting_layout);
            star_layout = (ConstraintLayout) itemView.findViewById(R.id.star_layout);

            layout_order_adapter = itemView.findViewById(R.id.layout_order_adapter);


        }
    }
}
