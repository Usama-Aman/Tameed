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
 * Created by dev on 17-01-2018.
 */

public class Review_Adapter extends RecyclerView.Adapter<Review_Adapter.MyViewHolder>{
    Boolean bool=true;

    Context context;
    ArrayList<String> review_detail;

    public Review_Adapter(Context context, ArrayList<String> review_detail) {

        this.context=context;
        this.review_detail=review_detail;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recycle,parent,false);

        MyViewHolder vh=new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool){
                    holder.star1.setImageResource(R.drawable.empty_rating);

                }
                else{
                    holder.star1.setImageResource(R.drawable.filled_rating);
                }
                bool=!bool;
            }
        });

        holder.star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool){
                    holder.star2.setImageResource(R.drawable.empty_rating);

                }
                else{
                    holder.star2.setImageResource(R.drawable.filled_rating);
                }
                bool=!bool;
            }
        });

        holder.star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool){
                    holder.star3.setImageResource(R.drawable.empty_rating);

                }
                else{
                    holder.star3.setImageResource(R.drawable.filled_rating);
                }
                bool=!bool;
            }
        });

        holder.star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool){

                    holder.star4.setImageResource(R.drawable.filled_rating);

                }
                else{
                    holder.star4.setImageResource(R.drawable.empty_rating);

                }
                bool=!bool;
            }
        });

        holder.star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool){

                    holder.star5.setImageResource(R.drawable.filled_rating);

                }
                else{
                    holder.star5.setImageResource(R.drawable.empty_rating);

                }
                bool=!bool;
            }
        });


    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView star1,star2,star3,star4,star5;
        public MyViewHolder(View itemView) {
            super(itemView);

            star1=(ImageView)itemView.findViewById(R.id.filled_star1);
            star2=(ImageView)itemView.findViewById(R.id.filled_star2);
            star3=(ImageView)itemView.findViewById(R.id.filled_star3);
            star4=(ImageView)itemView.findViewById(R.id.review_star4);
            star5=(ImageView)itemView.findViewById(R.id.review_star5);


        }
    }
}
