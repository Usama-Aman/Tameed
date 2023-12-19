package tameed.com.tameed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tameed.com.tameed.R;
/**
 * Created by dev on 18-01-2018.
 */

public class Sub_category_Adapter extends RecyclerView.Adapter<Sub_category_Adapter.MyViewHolder>{
    ArrayList<String> sub_category_detail;
    Context context;

    public Sub_category_Adapter(Context context, ArrayList<String> sub_category_detail) {

        this.sub_category_detail=sub_category_detail;
        this.context=context;
        //Log.e("Sub_category_Adapter","****************************");

    }

    @Override
    public Sub_category_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
   View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_cat_recycle,parent,false);
   MyViewHolder vh=new MyViewHolder(view);
   return vh;
    }

    @Override
    public void onBindViewHolder(Sub_category_Adapter.MyViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView sub_check;
        public MyViewHolder(View itemView) {
            super(itemView);
//            sub_check=(ImageView)itemView.findViewById(R.id.sub_recy_check);
//            sub_check.setTag(1);
        }
    }
}
