package tameed.com.tameed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tameed.com.tameed.Entity.Service_List_Entity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.SaveSharedPrefernces;
/**
 * Created by seemtech2 on 09-02-2018.
 */

public class Service_Adapter extends RecyclerView.Adapter<Service_Adapter.MyViewHolder>{
    ArrayList<String> public_room;
    Context context;
    SaveSharedPrefernces ssp;
    ArrayList<Service_List_Entity>service_list=new ArrayList<>();
    public Service_Adapter(Context context, ArrayList<Service_List_Entity>service_list) {
        this.service_list=service_list;
        this.context=context;
        ssp=new SaveSharedPrefernces();
        //Log.e("Service_Adapter","****************************");

    }
    @Override
    public Service_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_services,parent,false);
        MyViewHolder vh=new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(Service_Adapter.MyViewHolder holder, final int position) {
       // holder.order_no.setText("Order No:"+public_room_list.get(position).getOrder_id());



    }

    @Override
    public int getItemCount() {
        return service_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout send_to_porder;
        TextView order_no,order_date,main_category,order_type;

        public MyViewHolder(View itemView) {
            super(itemView);
            send_to_porder=(ConstraintLayout)itemView.findViewById(R.id.send_to_porder);
            order_type=itemView.findViewById(R.id.order_type);
            order_no=itemView.findViewById(R.id.order_no);
            order_date=itemView.findViewById(R.id.order_date);
            main_category=itemView.findViewById(R.id.main_category);

        }
    }
}
