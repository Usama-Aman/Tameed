package tameed.com.tameed.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import tameed.com.tameed.Entity.Provider_List_Entity;
import tameed.com.tameed.R;
import tameed.com.tameed.ResentChat;
import tameed.com.tameed.Resubmitt_DirectOrder;
/**
 * Created by USER on 1/12/2018.
 */

public class SearchProviderAdapter extends RecyclerView.Adapter<SearchProviderAdapter.ContactViewHolder> {

    String name_service = "";
    Context context;
    ArrayList<Provider_List_Entity> provider_list = new ArrayList<>();

    DecimalFormat df = new DecimalFormat("00.00");
    Double ratting;
    ArrayList<String> service_name_list = new ArrayList<>();

    public SearchProviderAdapter(Context context, ArrayList<Provider_List_Entity> provider_list) {
        this.context = context;
        this.provider_list = provider_list;
        //Log.e("SearchProviderAdapter", "****************************");
    }

    @Override
    public int getItemCount() {
        return provider_list.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, final int i) {
        contactViewHolder.provider_name.setText(provider_list.get(i).getName());
        if (provider_list.get(i).getService_name_list().size() > 2) {
            service_name_list.clear();
            service_name_list.addAll(provider_list.get(i).getService_name_list().subList(0, 2));
        } else {
            service_name_list.clear();
            service_name_list.addAll(provider_list.get(i).getService_name_list());
        }
        //Log.e("Distance val:", "...." + provider_list.get(i).getDistance());
        String string_temp = new Double(provider_list.get(i).getDistance()).toString();
        String string_form = string_temp.substring(0, string_temp.indexOf('.'));
        double t = Double.valueOf(string_form);
        contactViewHolder.distance_txt.setText(t + "كيلو");
        // itemViewHolder.provider_name.setText(provider_list.get(i).getName());

//contactViewHolder.distance_txt.setText(""+df.format(Double.parseDouble(provider_list.get(i).getDistance()))+" KM");
//for (int j=0;j<provider_list.get(i).getService_name_list().size();j++){
//    if (j<=3){
//        name_service=
//    }
//}
        contactViewHolder.service_name.setText(service_name_list.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ","));
        ratting = Double.parseDouble(provider_list.get(i).getReview_rating());
        if (ratting >= 0.0 && ratting < 1.0) {
            contactViewHolder.star1.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star2.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star3.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star4.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star5.setImageResource(R.mipmap.star_inactive);
        } else if (ratting >= 1.0 && ratting < 2.0) {
            contactViewHolder.star1.setImageResource(R.mipmap.star_active);
            contactViewHolder.star2.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star3.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star4.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star5.setImageResource(R.mipmap.star_inactive);
        } else if (ratting >= 2.0 && ratting < 3.0) {
            contactViewHolder.star1.setImageResource(R.mipmap.star_active);
            contactViewHolder.star2.setImageResource(R.mipmap.star_active);
            contactViewHolder.star3.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star4.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star5.setImageResource(R.mipmap.star_inactive);
        } else if (ratting >= 3.0 && ratting < 4.0) {
            contactViewHolder.star1.setImageResource(R.mipmap.star_active);
            contactViewHolder.star2.setImageResource(R.mipmap.star_active);
            contactViewHolder.star3.setImageResource(R.mipmap.star_active);
            contactViewHolder.star4.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star5.setImageResource(R.mipmap.star_inactive);
        } else if (ratting >= 4.0 && ratting < 5.0) {
            contactViewHolder.star1.setImageResource(R.mipmap.star_active);
            contactViewHolder.star2.setImageResource(R.mipmap.star_active);
            contactViewHolder.star3.setImageResource(R.mipmap.star_active);
            contactViewHolder.star4.setImageResource(R.mipmap.star_active);
            contactViewHolder.star5.setImageResource(R.mipmap.star_inactive);
        } else if (ratting >= 5.0) {
            contactViewHolder.star1.setImageResource(R.mipmap.star_active);
            contactViewHolder.star2.setImageResource(R.mipmap.star_active);
            contactViewHolder.star3.setImageResource(R.mipmap.star_active);
            contactViewHolder.star4.setImageResource(R.mipmap.star_active);
            contactViewHolder.star5.setImageResource(R.mipmap.star_active);
        } else {
            contactViewHolder.star1.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star2.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star3.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star4.setImageResource(R.mipmap.star_inactive);
            contactViewHolder.star5.setImageResource(R.mipmap.star_inactive);
        }
        contactViewHolder.cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.service_list.clear();
                ;
                helper.category_list.clear();
                helper.category_idlist.clear();
                helper.service_idlist.clear();

                Intent intent = new Intent(context, Resubmitt_DirectOrder.class);
                intent.putExtra("review_rating", provider_list.get(i).getReview_rating());
                intent.putExtra("provider_id", provider_list.get(i).getUser_id());
                intent.putExtra("profile_pic", provider_list.get(i).getProfile_pic_url());
                intent.putExtra("name", provider_list.get(i).getName());
                helper.service_list.addAll(provider_list.get(i).getService_name_list());
                helper.service_idlist.addAll(provider_list.get(i).getService_id_list());
                helper.category_idlist.addAll(provider_list.get(i).getCategory_id_list());
                helper.category_list.addAll(provider_list.get(i).getCategory_name_list());

                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
            }
        });

        if (provider_list.get(i).getOnline_offline_status().equals("0") || provider_list.get(i).getOnline_offline_status().equals("")) {
            contactViewHolder.online_ofline_img.setImageResource(R.mipmap.ic_offline);
            //Picasso.with(context).load(provider_list.get(i).getProfile_pic_2xthumb_url()).placeholder(R.mipmap.ic_online).into(itemViewHolder.online_ofline_img);
        } else {
            contactViewHolder.online_ofline_img.setImageResource(R.mipmap.ic_online);
        }


        if (!TextUtils.isEmpty(provider_list.get(i).getProfile_pic_thumb_url())) {
            Picasso.with(context).load(provider_list.get(i).getProfile_pic_thumb_url())
                    .placeholder(R.mipmap.no_thumb)
                    .error(R.mipmap.no_thumb)
                    .into(contactViewHolder.provide_pic);
        } else {
            contactViewHolder.provide_pic.setImageResource(R.mipmap.no_thumb);
        }

        contactViewHolder.call.setVisibility(View.GONE);

        contactViewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + provider_list.get(i).getCombine_mobile()));
                context.startActivity(callIntent);
            }
        });
        contactViewHolder.home_fav.setVisibility(View.GONE);

        contactViewHolder.home_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(context);
                }

                alert.setMessage(context.getResources().getString(R.string.msg_do_you_want_provider_favourite));


                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();


                    }
                });

                alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                Dialog dialog = alert.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });
        contactViewHolder.home_chat.setVisibility(View.GONE);
        contactViewHolder.home_frwd.setVisibility(View.GONE);


        contactViewHolder.home_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ResentChat.class));
            }
        });
        contactViewHolder.home_frwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_home, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cartView;
        ImageView call, home_chat, home_fav, home_frwd, provide_pic, online_ofline_img;
        TextView provider_name, service_name, distance_txt;
        ImageView star1, star2, star3, star4, star5;

        public ContactViewHolder(View v) {
            super(v);
            cartView = (ConstraintLayout) itemView.findViewById(R.id.layout_home_adapter);
            call = (ImageView) itemView.findViewById(R.id.imageCall);
            home_chat = (ImageView) itemView.findViewById(R.id.home_chat);
            home_fav = (ImageView) itemView.findViewById(R.id.home_fav);
            home_frwd = (ImageView) itemView.findViewById(R.id.home_frwd);
            provide_pic = (ImageView) itemView.findViewById(R.id.provider_img);
            provider_name = (TextView) itemView.findViewById(R.id.provider_name);
            service_name = (TextView) itemView.findViewById(R.id.service2_txt);
            distance_txt = (TextView) itemView.findViewById(R.id.textView8);
            star1 = (ImageView) itemView.findViewById(R.id.provider_star1);
            star2 = (ImageView) itemView.findViewById(R.id.provider_star2);
            star3 = (ImageView) itemView.findViewById(R.id.provider_star3);
            star4 = (ImageView) itemView.findViewById(R.id.provider_star4);
            star5 = (ImageView) itemView.findViewById(R.id.provider_star5);
            online_ofline_img = itemView.findViewById(R.id.online_ofline_img);
            distance_txt = itemView.findViewById(R.id.distance_txt);


        }

    }


}

