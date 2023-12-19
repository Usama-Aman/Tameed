package tameed.com.tameed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tameed.com.tameed.R;
/**
 * Created by dev on 16-01-2018.
 */

public class Payment_type_Adapter extends RecyclerView.Adapter<Payment_type_Adapter.MyViewHolder> {


    ArrayList<String> payment_detail;
    Context context;

    public Payment_type_Adapter(Context context, ArrayList<String> payment_detail) {

        this.payment_detail=payment_detail;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_type_recycle,parent,false);
      MyViewHolder vh=new MyViewHolder(view);
      return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
          if(helper.pay==1) {
              if (payment_detail.get(position).equals("PAID")) {
                  holder.paid_unpaid.setText("مدفوع");
                  holder.check_uncheck.setImageResource(R.mipmap.ic_check);
              } else {
                  holder.paid_unpaid.setText("غير مدفوع");
                  holder.check_uncheck.setImageResource(R.mipmap.ic_delete);
              }
          }
          else if(helper.pay==2){
              holder.paid_unpaid.setText("مدفوع");
              holder.check_uncheck.setImageResource(R.mipmap.ic_check);

          }
          else if(helper.pay==3){
              holder.paid_unpaid.setText("غير مدفوع");
              holder.check_uncheck.setImageResource(R.mipmap.ic_delete);

          }



    }





    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView check_uncheck;
        TextView paid_unpaid;
        public MyViewHolder(View itemView) {
            super(itemView);

            check_uncheck=(ImageView)itemView.findViewById(R.id.paid_check_img);
            paid_unpaid=(TextView) itemView.findViewById(R.id.txt_paid_unpaid);
        }
    }
}
