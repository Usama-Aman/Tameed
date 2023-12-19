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

import tameed.com.tameed.Entity.Search_service_Entity;
import tameed.com.tameed.FilterActivity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * Created by seemtech2 on 08-02-2018.
 */

public class Search_Service_Adapter extends RecyclerView.Adapter<Search_Service_Adapter.ItemViewHolder> {
    public ArrayList mlist_store;
    public LayoutInflater inflater;
    Context context;
    Map<String, String> paramsCount;
    ArrayList<Search_service_Entity>search_service_list=new ArrayList<>();
    int productquanity = 0;

    SaveSharedPrefernces ssp;

    public Search_Service_Adapter(Context applicationContext, ArrayList<Search_service_Entity>search_service_list) {

        this.search_service_list = search_service_list;
        this.context = applicationContext;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ssp=new SaveSharedPrefernces();
        //Log.e("Search_Service_Adapter","****************************");

    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        //ImageView project_img,custom_star1,custom_star2,custom_star3,custom_star4,custom_star5,default_img;
        TextView text_country,service_to_cover;
        RelativeLayout city_to_reltive,country_name_relative,country_code_relative,service_name_relative;



        public ItemViewHolder(View itemView) {
            super(itemView);
            service_to_cover = (TextView) itemView.findViewById(R.id.service_to_cover);
            service_name_relative=itemView.findViewById(R.id.service_name_relative);

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
        itemViewHolder.service_to_cover.setText(search_service_list.get(position).getService_name());
        itemViewHolder.service_name_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.service_name_list="";
                //helper.service_id_list="";

                Intent i=new Intent(context, FilterActivity.class);
                helper.service_name_list=search_service_list.get(position).getService_name();
                helper.service_id_list=search_service_list.get(position).getService_id();
               // i.putExtra("service_id",search_service_list.get(position).getService_id());
                context.startActivity(i);

            }
        });


    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search_service, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return search_service_list.size();
    }
}
