package com.example.deliveryboy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Customer extends AppCompatActivity {

    private TextView shopAddr, custPhone;
    private LinearLayout llpickedUpBtn;
    private Button quesBtnPick, goThereBtn;
    private ImageView imgPick;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        shopAddr = (TextView) findViewById(R.id.shopAddr);
        custPhone = (TextView) findViewById(R.id.custPhone);
        llpickedUpBtn = (LinearLayout) findViewById(R.id.llpickedUpBtn);
        quesBtnPick = (Button) findViewById(R.id.quesBtnPick);
        goThereBtn = (Button) findViewById(R.id.goThereBtn);
        imgPick = (ImageView) findViewById(R.id.imgPick);

        imgPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer.this, Start.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
