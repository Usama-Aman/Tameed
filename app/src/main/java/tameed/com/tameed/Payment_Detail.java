package tameed.com.tameed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class Payment_Detail extends AppCompatActivity {

    ImageView header_back;

    Intent intent;
    String payment_type_date, dollor_price, paid_unpaid, order_number;

    TextView txt_payment_date, txt_dollar_price, txt_order_number, txt_paid_unpaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__detail);

        txt_order_number = (TextView) findViewById(R.id.txt_order_number);
        txt_payment_date = (TextView) findViewById(R.id.txt_payment_type_date);
        txt_dollar_price = (TextView) findViewById(R.id.txt_dollor_price);
        txt_paid_unpaid = (TextView) findViewById(R.id.txt_paid_unpaid);

        header_back = (ImageView) findViewById(R.id.header_back);
        header_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent =new Intent(Payment_Detail.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("flag",21);
                startActivity(intent);*/
                finish();
            }
        });

        intent = getIntent();
        payment_type_date = intent.getStringExtra("payment_type_date");
        dollor_price = intent.getStringExtra("dollor_price");
        paid_unpaid = intent.getStringExtra("paid_unpaid");
        order_number = intent.getStringExtra("order_number");

        txt_order_number.setText(order_number);
        txt_payment_date.setText(payment_type_date);
        txt_dollar_price.setText(dollor_price);
        if (paid_unpaid.equals("PAID")) {
            txt_paid_unpaid.setText("مدفوع");
        } else if (paid_unpaid.equals("PARTIALLY PAID")) {
            txt_paid_unpaid.setText("مدفوع جزئيا");
        } else {
            txt_paid_unpaid.setText("غير مدفوع");
        }
    }
}
