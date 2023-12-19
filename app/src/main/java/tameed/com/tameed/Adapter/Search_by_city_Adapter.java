package tameed.com.tameed.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import tameed.com.tameed.Entity.City_to_cover_Entity;
import tameed.com.tameed.FilterActivity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.SaveSharedPrefernces;
/**
 * Created by seemtech2 on 08-02-2018.
 */

public class Search_by_city_Adapter extends RecyclerView.Adapter<Search_by_city_Adapter.ItemViewHolder> {
    public ArrayList mlist_store;
    public LayoutInflater inflater;
    Context context;
    ArrayList<City_to_cover_Entity>search_list=new ArrayList<>();
    Map<String, String> paramsCount;
    int productquanity = 0;

    SaveSharedPrefernces ssp;

    public Search_by_city_Adapter(Context applicationContext,  ArrayList<City_to_cover_Entity>search_list) {

        this.search_list = search_list;
        this.context = applicationContext;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ssp=new SaveSharedPrefernces();
        //Log.e("Search_by_city_Adapter","****************************");

    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        //ImageView project_img,custom_star1,custom_star2,custom_star3,custom_star4,custom_star5,default_img;
        TextView text_country,city_to_cover;
        RelativeLayout city_to_reltive,country_name_relative,country_code_relative;



        public ItemViewHolder(View itemView) {
            super(itemView);
            city_to_cover = (TextView) itemView.findViewById(R.id.city_to_cover);
            country_name_relative=itemView.findViewById(R.id.country_name2_relative);


            //city_to_reltive= (RelativeLayout) itemView.findViewById(R.id.country_reltive);
           /* country_name_relative= (RelativeLayout) itemView.findViewById(R.id.country_name_relative);
            country_code_relative= (RelativeLayout) itemView.findViewById(R.id.country_code_relative);*/


        }
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {


//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) itemViewHolder.city_to_reltive.getLayoutParams();
//        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) itemViewHolder.text_country.getLayoutParams();



        ////Log.e("333","333");
        //lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //itemViewHolder.text_country.setLayoutParams(lp1);
        itemViewHolder.city_to_cover.setText(search_list.get(position).getCity_name());

        itemViewHolder.country_name_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                helper.city_name_list="";
                helper.city_to_id_list="";

                Intent i=new Intent(context, FilterActivity.class);
                helper.city_name_list=search_list.get(position).getCity_name();
                helper.city_to_id_list=search_list.get(position).getCity_id();
                //////Log.e("city_to_id_list",helper.city_to_id_list);
                context.startActivity(i);

            }
        });


    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search_city, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return search_list.size();
    }
}
