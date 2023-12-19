package tameed.com.tameed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tameed.com.tameed.Entity.Service_Category_Entity;
import tameed.com.tameed.R;
/**
 * Created by lenovo on 3/28/2018.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> implements FilterSubAdapter.senddata {
    private final ArrayList<Service_Category_Entity> listdata;
    private Context context;
    private String baseUrl;
    private FilterSubAdapter adpter2;
    LinearLayoutManager layoutManager;
    int mCurrentPlayingPosition = -1;
    int value =0;

    //ADD JAY
    boolean isShowHide = true;



    public FilterAdapter(Context context,ArrayList<Service_Category_Entity> listData) {
        this.listdata = listData;
        this.context = context;
        this.baseUrl = baseUrl;
        //Log.e("FilterAdapter","****************************");

    }

    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_services, parent, false);
        FilterAdapter.ViewHolder viewHolder = new FilterAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FilterAdapter.ViewHolder holder, final int position) {

//        holder.mTV.setText(listdata.get(position).getCategory_name());
//        adpter2 = new Adapter2(listdata.get(position).data, context);
//        holder.mRecyclerViewSub.setAdapter(adpter2);
        holder.mTV.setText(listdata.get(position).getCategory_name());

        ////Log.e("List","===="+listdata.get(position).getList());
        layoutManager=new LinearLayoutManager(this.context);
        holder.mRecyclerViewSub.setLayoutManager(layoutManager);
        ArrayList<Service_Category_Entity.Services_Entity> list=listdata.get(position).getList();

//        adpter2 = new FilterSubAdapter(listdata.get(getAdapterPosition()).get,context);
//        mRecyclerViewSub.setAdapter(adpter2);

        adpter2 =new FilterSubAdapter(list,context);
        holder.mRecyclerViewSub.setAdapter(adpter2);

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    @Override
    public void changeData(int position) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        private final RecyclerView mRecyclerViewSub;
        private final LinearLayoutManager linearLayoutManager;
        private final ImageView cat;
        private ImageView forward_img5,forward_img5_sown;
        private TextView mTV;
        //ADD JAY
        ConstraintLayout constraintLayout_category;
        ConstraintLayout constraintLayout_recycler_item_services;
        View View_line_one;

        public ViewHolder(View itemView) {
            super(itemView);
            mTV = (TextView) itemView.findViewById(R.id.textView2);


            forward_img5= (ImageView) itemView.findViewById(R.id.forward_img5);
            forward_img5_sown= (ImageView) itemView.findViewById(R.id.forward_img5_sown);
            constraintLayout_category= (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_category);
            constraintLayout_recycler_item_services= (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_recycler_item_services);
            View_line_one= (View) itemView.findViewById(R.id.View_line_one);


            mRecyclerViewSub = (RecyclerView) itemView.findViewById(R.id.service_view);
            linearLayoutManager = new LinearLayoutManager(context);
            mRecyclerViewSub.setLayoutManager(linearLayoutManager);
            mRecyclerViewSub.setNestedScrollingEnabled(false);

            //ADD JAY
            constraintLayout_category.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(isShowHide)
                    {
                        isShowHide = false;
                        constraintLayout_recycler_item_services.setVisibility(View.GONE);

                       // forward_img5.setBackgroundResource(R.mipmap.ic_forward_w_2x);
                        forward_img5_sown.setVisibility(View.INVISIBLE);
                        forward_img5.setVisibility(View.VISIBLE);
                        //View_line_one.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        isShowHide = true;
                        constraintLayout_recycler_item_services.setVisibility(View.VISIBLE);

                        //forward_img5.setBackgroundResource(R.mipmap.ic_forward_w_2x_down);
                        forward_img5_sown.setVisibility(View.VISIBLE);
                        forward_img5.setVisibility(View.INVISIBLE);
                        //View_line_one.setVisibility(View.INVISIBLE);

                    }

                }
            });





            cat = (ImageView) itemView.findViewById(R.id.check_spareall);
//            cat.setOnClickListener(this);

            cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ArrayList<Service_Category_Entity.Services_Entity> list1= listdata.get(getAdapterPosition()).getList();


                    if(listdata.get(getAdapterPosition()).isSelected){
                        listdata.get(getAdapterPosition()).isSelected = false;
                        cat.setSelected(false);

                        //Log.e("LISTSIXQQ",""+listdata.get(getAdapterPosition()).getList().size());
                        for (int i = 0; i < listdata.get(getAdapterPosition()).getList().size(); i++) {
                            ////Log.e("ANJALII","ANJALI");
                            listdata.get(getAdapterPosition()).getList().get(i).isSubSelected = false;

                            helper.service_id.remove(listdata.get(getAdapterPosition()).getList().get(i).getService_id());
                            helper.service_list.remove(listdata.get(getAdapterPosition()).getList().get(i).getService_name());
                            ////Log.e("helper.list1", "" + helper.service_id);
                            ////Log.e("helper.list2", "" + helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                            //  ////Log.e("helper.price", "" + helper.cat_price.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                            ////Log.e("helper.subcat", "" + helper.service_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
//                    helper.value = "1";

                        }

                    }
                    else {
                        listdata.get(getAdapterPosition()).isSelected = true;
                        cat.setSelected(true);

                        //Log.e("LISTZZZZZZ",""+listdata.get(getAdapterPosition()).getList().size());
                        for (int i = 0; i < listdata.get(getAdapterPosition()).getList().size(); i++) {

                            listdata.get(getAdapterPosition()).getList().get(i).isSubSelected = true;
                            helper.service_id.add(listdata.get(getAdapterPosition()).getList().get(i).getService_id());
                            helper.service_list.add(listdata.get(getAdapterPosition()).getList().get(i).getService_name());

                            ////Log.e("helper.list1", "" + helper.service_id);
                            ////Log.e("helper.list2", "" + helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                            //  ////Log.e("helper.price", "" + helper.cat_price.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                            ////Log.e("helper.subcat", "" + helper.service_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
//                    helper.value = "1";

                        }
                    }
                    adpter2 = new FilterSubAdapter(listdata.get(getAdapterPosition()).getList(),context);
                    mRecyclerViewSub.setAdapter(adpter2);

                }
            });



        }

    }
}

