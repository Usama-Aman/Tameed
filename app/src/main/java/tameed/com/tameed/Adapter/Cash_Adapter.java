package tameed.com.tameed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tameed.com.tameed.Entity.All_Payment_Entity;
import tameed.com.tameed.Payment_Detail;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.LoadApi;

/**
 * Created by dev on 23-01-2018.
 */

public class Cash_Adapter extends RecyclerView.Adapter<Cash_Adapter.MyViewHolder> {

    Context context;
    ArrayList<All_Payment_Entity> cash_detail;

    public Cash_Adapter(Context context, ArrayList<All_Payment_Entity> cash_detail) {
        this.context = context;
        this.cash_detail = cash_detail;
        //Log.e("Cash_Adapter", "****************************");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_type_recycle, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.payment_type_date.setText("التاريخ" + LoadApi.parseDateToddMMyyyy(cash_detail.get(position).getLast_modified_date()));
        holder.dollor_price.setText("ريال " + cash_detail.get(position).getOrder_cost());
        if (cash_detail.get(position).getPayment_status().equals("PAID")) {
            holder.paid_unpaid.setText("مدفوع");
            holder.check_uncheck.setImageResource(R.mipmap.ic_check);
        } else if (cash_detail.get(position).getPayment_status().equals("PARTIALLY PAID")) {
            holder.paid_unpaid.setText("مدفوع جزئيا");
            holder.check_uncheck.setImageResource(R.mipmap.ic_check);
        } else {
            holder.paid_unpaid.setText("غير مدفوع");
            holder.check_uncheck.setImageResource(R.mipmap.ic_delete);
        }

        // TODO Nardeep Edit
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Payment_Detail.class);
                intent.putExtra("order_number",cash_detail.get(position).getOrder_id());
                intent.putExtra("payment_type_date", "التاريخ" + LoadApi.parseDateToddMMyyyy(cash_detail.get(position).getLast_modified_date()));
                intent.putExtra("dollor_price", "ريال " + cash_detail.get(position).getOrder_cost());
                intent.putExtra("paid_unpaid", cash_detail.get(position).getPayment_status());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cash_detail.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView check_uncheck;
        TextView paid_unpaid, payment_type_date, dollor_price;

        // Nardeep Edit
        ConstraintLayout constraintLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            // Nardeep Edit
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_payment);

            check_uncheck = (ImageView) itemView.findViewById(R.id.paid_check_img);
            paid_unpaid = (TextView) itemView.findViewById(R.id.txt_paid_unpaid);
            payment_type_date = (TextView) itemView.findViewById(R.id.payment_type_date);
            dollor_price = (TextView) itemView.findViewById(R.id.dollor_price);
        }
    }
}
