package tameed.com.tameed.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import tameed.com.tameed.Entity.Order_Pic_Entity;
import tameed.com.tameed.PreviewIamge;
import tameed.com.tameed.R;
/**
 * Created by dell on 2/7/2018.
 */

public class Pic_Adapter extends RecyclerView.Adapter<Pic_Adapter.ItemViewHolder> {

    Context context;
    String text;
    int count = 0;
    int flag = 0;
    Activity activity;
    int value;
    ArrayList<Order_Pic_Entity> storelist = new ArrayList<Order_Pic_Entity>();
    String opentime;
    Typeface typeface;
Bitmap bitmap3,bitmap;
byte[] test2;
    public Pic_Adapter(Context context, ArrayList<Order_Pic_Entity> storelist) {
        this.context = context;
        this.storelist = storelist;
        //Log.e("Pic_Adapter","****************************");


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView custom_card_mage;


        public ItemViewHolder(View itemView) {
            super(itemView);

            custom_card_mage= (ImageView) itemView.findViewById(R.id.custom_card_mage);
        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {

        Picasso.with(context).load(storelist.get(position).getPic_2xthumb_url()).placeholder(R.mipmap.no_thumb).into(itemViewHolder.custom_card_mage);

        Picasso.with(context)
                .load(storelist.get(position).getPic_2xthumb_url())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // loaded bitmap is here (bitmap)
                        //photo_image2_edit.setImageBitmap(bitmap);
                        ////Log.e("Bitma3","<><>"+bitmap);
                        bitmap3=bitmap;
                        ////Log.e("BITMAP1","b"+bitmap3);


                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap3.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        ////Log.e("Byte_ARR3","<><>"+byteArray);
                        test2=byteArray;
                        ////Log.e("Test3","<>"+test2);
helper.pic_list.add(bitmap3);
                        helper.image_list.add(test2);

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }



                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        itemViewHolder.custom_card_mage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PreviewIamge.class);
                intent.putExtra("Stringimage",storelist.get(position).getPic_url());
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pic_view, parent, false);
       ItemViewHolder viewHolder = new ItemViewHolder(v);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return storelist != null ? storelist.size() : 0;

    }


}

