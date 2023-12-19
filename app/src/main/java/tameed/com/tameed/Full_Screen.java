package tameed.com.tameed;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import tameed.com.tameed.Adapter.helper;

/**
 * Created by dev on 24-01-2018.
 */

public class Full_Screen extends AppCompatActivity {

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.full_screen);

        Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");
        Bitmap bmp2 = (Bitmap) extras.getParcelable("imagebitmap2");
        Bitmap bmp3 = (Bitmap) extras.getParcelable("imagebitmap3");


        Bitmap p_bmp = (Bitmap) extras.getParcelable("p_imagebitmap");
        Bitmap p_bmp2 = (Bitmap) extras.getParcelable("p_imagebitmap2");
        Bitmap p_bmp3 = (Bitmap) extras.getParcelable("p_imagebitmap3");


        Bitmap accept_bmp = (Bitmap) extras.getParcelable("accept_imagebitmap1");
        Bitmap accept_bmp2 = (Bitmap) extras.getParcelable("accept_imagebitmap2");
        Bitmap accept_bmp3 = (Bitmap) extras.getParcelable("accept_imagebitmap3");

        ImageView imgDisplay, btnClose;



        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        btnClose = (ImageView) findViewById(R.id.btnClose);


        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Full_Screen.this.finish();
            }
        });


        if(helper.img==0) {

            imgDisplay.setImageBitmap(bmp);
        }else if(helper.img==1) {
            imgDisplay.setImageBitmap(bmp2);
        }
        else if(helper.img==2){
            imgDisplay.setImageBitmap(bmp3);
        }


       else if(helper.img==3){
            imgDisplay.setImageBitmap(p_bmp);

        }
        else if(helper.img==4){

            imgDisplay.setImageBitmap(p_bmp2);
        }
        else if(helper.img==5){
            imgDisplay.setImageBitmap(p_bmp3);
        }


        else if(helper.img==6){
            imgDisplay.setImageBitmap(accept_bmp);

        }
        else if(helper.img==7){
            imgDisplay.setImageBitmap(accept_bmp2);
        }
        else if(helper.img==8){
            imgDisplay.setImageBitmap(accept_bmp3);
        }



    }


}

