package tameed.com.tameed.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tameed.com.tameed.R;

/**
 * Created by dev on 24-01-2018.
 */

public class Spo_Adapter extends RecyclerView.Adapter<Spo_Adapter.MyViewHolder> {

    Context context;
    ArrayList<Bitmap> add_img_detail;

    public Spo_Adapter(Context context, ArrayList<Bitmap> add_img_detail) {

        this.add_img_detail=add_img_detail;
        this.context=context;

        //Log.e("Spo_Adapter","****************************");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.direct_order_addimg_recy,parent,false);
     MyViewHolder vh=new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.upload_pic.setImageBitmap(add_img_detail.get(position));


        holder.delete_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              add_img_detail.remove(position);
              notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return add_img_detail.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView upload_pic,delete_pic;
        public MyViewHolder(View itemView) {
            super(itemView);

            upload_pic=(ImageView)itemView.findViewById(R.id.upic);
            delete_pic=(ImageView)itemView.findViewById(R.id.dod_delete_img);

        }
    }
}
