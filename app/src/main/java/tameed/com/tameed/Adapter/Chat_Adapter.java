package tameed.com.tameed.Adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tameed.com.tameed.Entity.ChatHistory_Entity;
import tameed.com.tameed.R;
import tameed.com.tameed.Util.URLogs;

/**
 * Created by lenovo on 3/21/2017.
 */
public class Chat_Adapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ChatHistory_Entity> messages;
    private LayoutInflater mlayLayoutInflater;
    ViewHolder holder;


    class ViewHolder {
        TextView chat_send_time;
        TextView chat_send_text, date_txt;
        TextView chat_recieve_text;
        TextView chat_recieve_time;

        RelativeLayout send_relative, recieve_relative, relative_response;
        //ADD JAY
        RelativeLayout relativeLayout_Unread_massage;
        RelativeLayout recieve_relative1;
    }

    public Chat_Adapter(Activity activity, ArrayList<ChatHistory_Entity> chat_list) {
        // TODO Auto-generated constructor stub

        this.activity = activity;
        this.messages = chat_list;
        this.mlayLayoutInflater = LayoutInflater.from(this.activity);

        //Log.e("Chat_Adapter", "****************************");

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {

            convertView = mlayLayoutInflater.inflate(R.layout.custom_chat_list, null);
            holder = new ViewHolder();
            URLogs.m1("model  : " + (new Gson()).toJson(messages.get(position)));
            holder.chat_send_time = (TextView) convertView.findViewById(R.id.chat_send_time);
            holder.chat_recieve_time = (TextView) convertView.findViewById(R.id.chat_receive_time);
            holder.chat_send_text = (TextView) convertView.findViewById(R.id.chat_send_text);
            holder.date_txt = (TextView) convertView.findViewById(R.id.date_txt);
            holder.chat_recieve_text = (TextView) convertView.findViewById(R.id.chat_reciev_text);
            holder.send_relative = (RelativeLayout) convertView.findViewById(R.id.send_relative);
            holder.recieve_relative = (RelativeLayout) convertView.findViewById(R.id.recieve_relative);
            holder.relative_response = (RelativeLayout) convertView.findViewById(R.id.relative_response);

            holder.relativeLayout_Unread_massage = (RelativeLayout) convertView.findViewById(R.id.relativeLayout_Unread_massage);

            holder.recieve_relative1 = (RelativeLayout) convertView.findViewById(R.id.recieve_relative1);

            holder.date_txt.setTag(1);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        try {
            if (messages.get(position).getNo().equals("2")) {
                holder.date_txt.setVisibility(View.GONE);
                holder.relative_response.setVisibility(View.VISIBLE);

                holder.date_txt.setText(messages.get(position).getDatee());


            } else {
                holder.relative_response.setVisibility(View.GONE);
                holder.date_txt.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        String status = messages.get(position).getStatus();

        if (status.equals("sent")) {


            holder.send_relative.setVisibility(View.VISIBLE);
            holder.recieve_relative.setVisibility(View.GONE);
            holder.chat_send_text.setVisibility(View.VISIBLE);

            holder.chat_send_time.setText(convertTimeToAMPM(messages.get(position).getTime()));
            holder.chat_send_text.setText(messages.get(position).getMessage());
        } else {

            holder.send_relative.setVisibility(View.GONE);
            holder.recieve_relative.setVisibility(View.VISIBLE);

            holder.chat_recieve_text.setVisibility(View.VISIBLE);

            //TODO ADD JAY CHECK NEW MASSAGE
            String status_new = messages.get(position).getStatus_new();
            if (!status_new.equalsIgnoreCase("")) {
                if (status_new.equalsIgnoreCase("1")) {
                    holder.chat_recieve_text.setTypeface(null, Typeface.BOLD);
                } else {
                    holder.chat_recieve_text.setTypeface(null, Typeface.NORMAL);
                }
            } else {
                holder.chat_recieve_text.setTypeface(null, Typeface.NORMAL);
            }

            holder.chat_recieve_time.setText(convertTimeToAMPM(messages.get(position).getTime()));
            holder.chat_recieve_text.setText(messages.get(position).getMessage());


        }
        return convertView;


    }

    public String convertTimeToAMPM(String dateTime) {
        try {

            // Get date from string
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
            Date date = dateFormatter.parse(dateTime);

            // Get time from date
            SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.US);
            dateTime = timeFormatter.format(date);
            return dateTime;
        } catch (ParseException e) {
            return dateTime;
        }
    }


}

