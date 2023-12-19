package tameed.com.tameed;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

import tameed.com.tameed.Adapter.ChatArrayAdapter;
import tameed.com.tameed.Util.ChatMessage;


public class ChatActivity extends Activity {


    private TextView txtHeader, txttyping;
    private ImageView imgBackHeader,ChatprofileImg,header_back;

    private ChatArrayAdapter arrayAdapter;
    private ListView list;
    private EditText chatText;
    private ImageView Send;
    private boolean side = false;
    ImageView comments_back;
    private String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,
                getResources().getDisplayMetrics());

        setContentView(R.layout.activity_chat);
        //Log.e(TAG,"****************************");
        txtHeader=(TextView) findViewById(R.id.txt_header);
        txtHeader.setText("المحادثة الفورية ");

        header_back=(ImageView)findViewById(R.id.header_back);

        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ChatprofileImg= (ImageView) findViewById(R.id.imageChatProfile);
        ChatprofileImg.setVisibility(View.VISIBLE);
        txttyping=(TextView) findViewById(R.id.textTyping);
        txttyping.setVisibility(View.VISIBLE);

//        Send = (ImageView) findViewById(R.id.chat_send);
//
//        list = (ListView) findViewById(R.id.chatwindow_lst);

        arrayAdapter = new ChatArrayAdapter(ChatActivity.this, R.layout.left);
        list.setAdapter(arrayAdapter);
//        comments_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        //chatText = (EditText) findViewById(R.id.chat_edtext);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();

            }
        });

        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(arrayAdapter);

        //to scroll the list view to bottom on data change
        arrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                list.setSelection(arrayAdapter.getCount() - 1);
            }
        });
    }

    private boolean sendChatMessage() {
        arrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }


}

