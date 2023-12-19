package tameed.com.tameed.Adapter;

import android.annotation.SuppressLint;
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
import java.util.Map;

import tameed.com.tameed.Entity.Country_list_Entity;
import tameed.com.tameed.R;
import tameed.com.tameed.RegisterActivity;
import tameed.com.tameed.Util.SaveSharedPrefernces;

/**
 * Created by seemtech2 on 08-12-2017.
 */

public class Country_List_Adapter extends RecyclerView.Adapter<Country_List_Adapter.ItemViewHolder> {
    public ArrayList mlist_store;
    public LayoutInflater inflater;
    Context context;
    ArrayList<Country_list_Entity> country_list = new ArrayList<Country_list_Entity>();
    Map<String, String> paramsCount;
    int productquanity = 0;

    SaveSharedPrefernces ssp;

    public Country_List_Adapter(Context applicationContext, ArrayList<Country_list_Entity> country_list ) {

        this.country_list = country_list;
        this.context = applicationContext;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ssp=new SaveSharedPrefernces();

        //Log.e("Country_List_Adapter","****************************");

    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        //ImageView project_img,custom_star1,custom_star2,custom_star3,custom_star4,custom_star5,default_img;
        TextView text_country,text_country_code;
        RelativeLayout country_reltive,country_name_relative,country_code_relative;



        public ItemViewHolder(View itemView) {
            super(itemView);
            text_country = (TextView) itemView.findViewById(R.id.text_country);
            text_country_code= (TextView) itemView.findViewById(R.id.text_country_code);
            country_reltive= (RelativeLayout) itemView.findViewById(R.id.country_reltive);
            country_name_relative= (RelativeLayout) itemView.findViewById(R.id.country_name_relative);
            country_code_relative= (RelativeLayout) itemView.findViewById(R.id.country_code_relative);


        }
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {


        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) itemViewHolder.text_country_code.getLayoutParams();

        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) itemViewHolder.text_country.getLayoutParams();



            ////Log.e("333","333");
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            itemViewHolder.text_country_code.setLayoutParams(lp);

            lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            itemViewHolder.text_country.setLayoutParams(lp1);





        itemViewHolder.text_country.setText(country_list.get(position).getCountry_name());
        itemViewHolder.text_country_code.setText(country_list.get(position).getCalling_code());

        itemViewHolder.country_reltive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, RegisterActivity.class);
               // i.putExtra("country_code",country_list.get(position).getCalling_code());
                helper.calling_code=country_list.get(position).getCalling_code();
               // helper.category_id=work_shop_list.get(position).getCategory_id();
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_country_list, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return country_list.size();
    }
}

