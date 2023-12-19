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

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.MyViewHolder> {
    boolean bool=true;

    ArrayList<String> category_detail;
    Context context;

    public Category_Adapter(Context context, ArrayList<String> category_detail) {
        this.context=context;
        this.category_detail=category_detail;
        //Log.e("Category_Adapter","****************************");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.service_type_recycle,parent,false);
        Category_Adapter.MyViewHolder vh=new Category_Adapter.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.cat_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(bool) {
                    holder.cat_check.setImageResource(R.mipmap.ic_checked);
                }
                else{
                    holder.cat_check.setImageResource(R.mipmap.ic_checked_un);
                }
                bool=!bool;
            }
        });


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cat_check;
        public MyViewHolder(View itemView) {
            super(itemView);
            cat_check=(ImageView)itemView.findViewById(R.id.service_recy_check);
            cat_check.setTag(1);

        }
    }
}
