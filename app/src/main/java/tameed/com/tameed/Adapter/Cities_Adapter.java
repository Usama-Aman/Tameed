package tameed.com.tameed.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tameed.com.tameed.Entity.Cities_list_entity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.SaveSharedPrefernces;
import tameed.com.tameed.profile;

/**
 * Created by dev on 03-02-2018.
 */

public class Cities_Adapter extends RecyclerView.Adapter<Cities_Adapter.MyViewHolder> {

    ArrayList<Cities_list_entity> cities_detail;
    Context context;
    SaveSharedPrefernces ssp;

    public Cities_Adapter(Context context, ArrayList<Cities_list_entity> cities_detail) {
        this.context=context;
        this.cities_detail=cities_detail;
        ssp=new SaveSharedPrefernces();

        //Log.e("Cities_Adapter","****************************");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cities_list, parent, false);

        MyViewHolder vh=new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

  holder.city_name.setText(cities_detail.get(position).getCity_name());
  holder.city_relative.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

          Intent i=new Intent(context,profile.class);
          helper.city_name=cities_detail.get(position).getCity_name();
          helper.city_id=cities_detail.get(position).getCity_id();
          ssp.setCity_id(context,helper.city_id);
          ////Log.e("helper ca",helper.city_name);
          i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
           context.startActivity(i);
          ((Activity)context).finish();
      }
  });

    }

    @Override
    public int getItemCount() {
        return cities_detail.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView city_name;
        RelativeLayout city_relative;
        public MyViewHolder(View itemView) {
            super(itemView);

            city_name=(TextView)itemView.findViewById(R.id.text_city);
               city_relative=(RelativeLayout)itemView.findViewById(R.id.city_reltive);

        }
    }
}
