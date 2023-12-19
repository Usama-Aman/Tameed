package tameed.com.tameed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tameed.com.tameed.Entity.Service_Category_Entity;
import tameed.com.tameed.R;
/**
 * Created by lenovo on 3/28/2018.
 */

public class FilterSubAdapter extends RecyclerView.Adapter<FilterSubAdapter.ViewHolder> {
    private final ArrayList<Service_Category_Entity.Services_Entity> listdata;
    private Context context;
    FilterSubAdapter.senddata senddata;
    private String baseUrl;
    int value=0;

    public FilterSubAdapter(ArrayList<Service_Category_Entity.Services_Entity> listData, Context context) {
        this.listdata = listData;
        this.context = context;
        this.baseUrl = baseUrl;
        //Log.e("FilterSubAdapter","****************************");

    }

    @Override
    public FilterSubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_service_type, parent, false);
        FilterSubAdapter.ViewHolder viewHolder = new FilterSubAdapter.ViewHolder(itemLayoutView);


        if (helper.driver_value.equals("0")) {
            for (int j = 0; j <  listdata.size(); j++) {
                if (! listdata.get(viewType).getService_name().equals("")) {
                    if (!listdata.get(viewType).getCategory_id().equals("")) {
                        viewHolder.sub.setSelected(true);
                        viewHolder.sub.setTag(2);
                        helper.update_value= helper.update_value+1;
                        listdata.get(viewType).setNo("2");

                    } else {
                        viewHolder.sub.setSelected(false);
                        viewHolder.sub.setTag(1);
                        listdata.get(viewType).setNo("1");
                    }
                    viewType++;
                }
            }
        }
        else
        {

            viewHolder.sub.setTag(1);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FilterSubAdapter.ViewHolder holder, final int position) {

        holder.mTV.setText(listdata.get(position).getService_name());

        ////Log.e("SUBSELECT111",""+listdata.get(position).isSubSelected);

        boolean select =listdata.get(position).isSubSelected;
        ////Log.e("SUBSELECT",""+select);

        if(listdata.get(position).isSubSelected){
            holder.sub.setSelected(true);
        }
        else{
            holder.sub.setSelected(false);
        }
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listdata.get(position).isSubSelected){
                    listdata.get(position).isSubSelected = false;
                    holder.sub.setSelected(false);
                    holder.sub.setSelected(false);

                    value = 4;
                    holder.sub.setTag(1);
                    listdata.get(position).setNo("1");
                    helper.service_id.remove(listdata.get(position).getService_id());
                    helper.service_list.remove(listdata.get(position).getService_name());

                    ////Log.e("helper.list1", "" + helper.service_id);
                    ////Log.e("helper.list2", "" + helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    //  ////Log.e("helper.price", "" + helper.cat_price.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    ////Log.e("helper.subcat", "" + helper.service_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
//                    helper.value = "1";
                }
                else{
                    listdata.get(position).isSubSelected = true;
                    holder.sub.setSelected(true);

                    holder.sub.setTag(2);
                    listdata.get(position).setNo("2");
                    helper.value = "1";
                    helper.service_id.add(listdata.get(position).getService_id());
                    helper.service_list.add(listdata.get(position).getService_name());

                    ////Log.e("helper.list1", "" + helper.service_id);
                    ////Log.e("helper.list2", "" + helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    //  ////Log.e("helper.price", "" + helper.cat_price.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                    ////Log.e("helper.subcat", "" + helper.service_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
//                    helper.va
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTV;
        ImageView sub;

        public ViewHolder(View itemView) {
            super(itemView);
            mTV = (TextView) itemView.findViewById(R.id.textView3);
            sub = (ImageView)itemView.findViewById(R.id.check_commercial);


        }


    }

    public interface senddata{
        public void changeData(int position);
    }
}

