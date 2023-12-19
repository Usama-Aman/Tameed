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

/**import androidx.recyclerview.widget.RecyclerView;

 * Created by dev on 24-01-2018.
 */

public class Direct_order_Adapter extends RecyclerView.Adapter<Direct_order_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Bitmap> dod_img_upload;
    ArrayList<byte[]> image_list=new ArrayList<>();
    public Direct_order_Adapter(Context context, ArrayList<Bitmap> dod_img_upload, ArrayList<byte[]> image_list) {
        this.context=context;
        this.dod_img_upload=dod_img_upload;
        this.image_list=image_list;
        //Log.e("Direct_order_Adapter","****************************");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.direct_order_addimg_recy,parent,false);

       MyViewHolder vh=new MyViewHolder(view);

       return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.upload_img.setImageBitmap(dod_img_upload.get(position));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dod_img_upload.remove(position);
                image_list.remove(position);
                helper.image_list.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dod_img_upload.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView upload_img,delete;
        public MyViewHolder(View itemView) {
            super(itemView);

            upload_img=(ImageView)itemView.findViewById(R.id.upic);
            delete=(ImageView)itemView.findViewById(R.id.dod_delete_img);
        }
    }
}
