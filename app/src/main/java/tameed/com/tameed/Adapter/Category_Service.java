package tameed.com.tameed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import tameed.com.tameed.Entity.Service_Category_Entity;
import tameed.com.tameed.R;

/**
 * Created by dell on 1/4/2018.
 */

public class Category_Service extends RecyclerView.Adapter<Category_Service.ItemViewHolder> {

    public LayoutInflater inflater;
    Context context;
    float rate;
    ArrayList<Service_Category_Entity> multiple_image_list = new ArrayList<Service_Category_Entity>();
    Map<String, String> paramsCount;
    int productquanity = 0;
    String review_rating;
    LinearLayoutManager layoutManager;
    int mCurrentPlayingPosition = -1;
    int value=0;


    public Category_Service(Context applicationContext,   ArrayList<Service_Category_Entity> multiple_image_list) {

        this.multiple_image_list = multiple_image_list;
        this.context = applicationContext;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView check_spareall;
        RelativeLayout cancelled_type_relative;
        TextView category_name;
        ConstraintLayout constraintLayout_category;
        RecyclerView service_view;
        public ItemViewHolder(View itemView) {
            super(itemView);
            category_name= (TextView) itemView.findViewById(R.id.textView2);
            check_spareall= (ImageView) itemView.findViewById(R.id.check_spareall);
            constraintLayout_category= (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_category);
            service_view= (RecyclerView) itemView.findViewById(R.id.service_view);
            //  custom_order1_img= (ImageView) itemView.findViewById(R.id.toyata);
            check_spareall.setTag(1);
            constraintLayout_category.setTag(1);

            // message_txt = (TextView) itemView.findViewById(R.id.message_txt);*/

        }
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {

        //  Picasso.with(context).load(multiple_image_list.get(position).getBrand_logo_thumb_url()).placeholder(R.drawable.upic).into(itemViewHolder.custom_order1_img);
        itemViewHolder.category_name.setText(multiple_image_list.get(position).getCategory_name());

        ////Log.e("List","===="+multiple_image_list.get(position).getList());
        layoutManager=new LinearLayoutManager(this.context);
        itemViewHolder.service_view.setLayoutManager(layoutManager);
        ArrayList<Service_Category_Entity.Services_Entity> list=multiple_image_list.get(position).getList();
        Service_Type_Adapter adapter =new Service_Type_Adapter(context,list);
        itemViewHolder.service_view.setAdapter(adapter);

        if (mCurrentPlayingPosition == position) {
            //itemViewHolder.check_spareall.setChecked(true);
            itemViewHolder.check_spareall.setImageResource(R.mipmap.ic_checked);
//
        }
        else
        {

            if (multiple_image_list.get(position).getNo().equals("2")) {
                ;                ////Log.e("ARROWW","<><>"+multiple_image_list.get(position).getNo());
                //  holder.check_box.setChecked(true);
                itemViewHolder.check_spareall.setImageResource(R.mipmap.ic_checked);
//                helper.c//at_id.add(my_interest_list.get(position).getCategory_id());
//                helper.cat_price.add(my_interest_list.get(position).getprice());
            } else {
                // holder.check_box.setChecked(false);
                itemViewHolder.check_spareall.setImageResource(R.mipmap.ic_checked_un);
            }
//
        }
        itemViewHolder.constraintLayout_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int PlayStopButtonState = (int) itemViewHolder.check_spareall.getTag();
                int previousPosition = mCurrentPlayingPosition;
                helper.checkvalue = "1";
                if (multiple_image_list.get(position).getNo().equals("2")) {
                    PlayStopButtonState = 2;
                } else {
                    PlayStopButtonState = 1;
                }
                if (PlayStopButtonState == 1) {
                    mCurrentPlayingPosition = position;
                    itemViewHolder.check_spareall.setImageResource(R.mipmap.ic_checked);
                    itemViewHolder.check_spareall.setTag(2);
                    itemViewHolder.constraintLayout_category.setTag(2);

                    multiple_image_list.get(position).setNo("2");

                    helper.value = "1";
//                    helper.service_id.add(multiple_image_list.get(position).getService_id());
//                    // my_interest_list.get(position).setPrice(edt_pr);
//                    // helper.cat_price.add(itemViewHolder.edt_price.getText().toString());
////                        my_interest_list.get(position).setprice(itemViewHolder.edt_price.getText().toString());
//                    helper.service_list.add(multiple_image_list.get(position).getService_name());
                    //helper.LogEntity3.get(position).setother_services_price(itemViewHolder.edt_price.getText().toString());
                    // helper.LogEntity3.get(position).setstylist_type(my_interest_list.get(position).getName());
                    String name1 = "", price1 = "", loop1 = "1";
//                        for(int k=0;k<=helper.LogEntity2.size();k++)
//                        {
//                            try {
//                                if (helper.LogEntity2.get(k).getstylist_type().equals(my_interest_list.get(position).getCategory_name())) {
//                                    helper.LogEntity2.get(k).setother_services_price(itemViewHolder.edt_price.getText().toString());
//                                }
//                                else
//                                {
//                                    name1=my_interest_list.get(position).getCategory_name();
//                                    price1=itemViewHolder.edt_price.getText().toString();
//                                    loop1="2";
//                                }
//                            }catch (IndexOutOfBoundsException e)
//                            {
//                                e.printStackTrace();
//                            }
//                        }
//                        if(loop1.equals("2"))
//                        {
//                            Search_Entity1 frind_snap1 = new Search_Entity1();
//                            frind_snap1.setother_services_price(price1);
//                            frind_snap1.setstylist_type(name1);
//                            helper.LogEntity2.add(frind_snap1);
//                            loop1="1";
//                        }
                } else {
                    value = 4;
                    mCurrentPlayingPosition = -1; // nothing wil be played after hitting stop
                    itemViewHolder.check_spareall.setImageResource(R.mipmap.ic_checked_un);
                    itemViewHolder.check_spareall.setTag(1);
                    itemViewHolder.constraintLayout_category.setTag(1);
                    //itemViewHolder.mfriends_contacts.setTag(1);
                    multiple_image_list.get(position).setNo("1");
//                        my_interest_list.get(position).setprice("");
//                    helper.value = "0";
//                    helper.service_id.remove(multiple_image_list.get(position).getService_id());
//                    //helper.cat_price.remove(itemViewHolder.edt_price.getText().toString());
//                    helper.service_list.remove(multiple_image_list.get(position).getService_name());
//                        for(int k=0;k<=helper.LogEntity2.size();k++)
//                        {
//                            try {
//                                if (helper.LogEntity2.get(k).getstylist_type().equals(my_interest_list.get(position).getCategory_name())) {
//                                    helper.LogEntity3.get(position).setother_services_price("");
                    //helper.LogEntity3.get(position).setother_services_price("");
                    //helper.LogEntity3.get(position).setstylist_type("");
//                                }
//
//                            }catch (IndexOutOfBoundsException e)
//                            {
//                                e.printStackTrace();
//                            }
//                        }
                }
                ////Log.e("helper.list1", "" + helper.service_id);
                ////Log.e("helper.list2", "" + helper.service_id.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                //  ////Log.e("helper.price", "" + helper.cat_price.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                ////Log.e("helper.subcat", "" + helper.service_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
                helper.value = "1";

                if (previousPosition != -1)
                    notifyItemChanged(previousPosition);
            }




        });
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_services, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);
        if (helper.driver_value.equals("0")) {
            for (int j = 0; j <  multiple_image_list.size(); j++) {
                if (! multiple_image_list.get(position).getCategory_name().equals("")) {
                    if (!multiple_image_list.get(position).getCategory_id().equals("")) {
                        viewHolder.check_spareall.setImageResource(R.mipmap.ic_checked);
                        viewHolder.check_spareall.setTag(2);
                        helper.update_value= helper.update_value+1;
                        multiple_image_list.get(position).setNo("2");

                    } else {
                        viewHolder.check_spareall.setImageResource(R.mipmap.ic_checked_un);
                        viewHolder.check_spareall.setTag(1);
                        multiple_image_list.get(position).setNo("1");
                    }
                    position++;
                }
            }
        }
        else
        {

            viewHolder.check_spareall.setTag(1);
        }

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return multiple_image_list.size();
    }
}
