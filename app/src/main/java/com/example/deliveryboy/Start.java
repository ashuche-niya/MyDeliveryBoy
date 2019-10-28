package com.example.deliveryboy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Start extends AppCompatActivity {


    public Button btnPickup, btnCheckpoint, btnCustomer, btnCancel, btnConfirm;
    private ImageView imgPickup, imgCheckpoint, imgCustomer;
    private TextView noOrders, startText;
    private LinearLayout linearLayout;
    private Bundle bundle;
    private String isPicked = "", isPickedCheck = "";
    private String deliveryType = "Split";
    private String deliveryPos = "Primary";
    private String btnState = "enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bundle = getIntent().getExtras();
        if(bundle != null){
            isPicked = bundle.getString("pickedUp");
            if(bundle.getString("pickedUpCheck") != null){
                isPickedCheck = bundle.getString("pickedUpCheck");
            }
            if (bundle.getString("button") != null){
                btnState = bundle.getString("button");
            }

        }

        btnPickup = (Button) findViewById(R.id.btnPickUp);
        btnCheckpoint = (Button) findViewById(R.id.btnCheckpoint);
        btnCustomer = (Button) findViewById(R.id.btnCustomer);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        noOrders = (TextView) findViewById(R.id.noOrders);
        startText = (TextView) findViewById(R.id.singleText);

        imgPickup = (ImageView) findViewById(R.id.imgPickUp);
        imgCheckpoint = (ImageView) findViewById(R.id.imgCheckpoint);
        imgCustomer = (ImageView) findViewById(R.id.imgCustomer);

        imgPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri intentUri = Uri.parse("google.navigation:q=28.7041, 77.1025");
                Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        if(deliveryType.equals("Single")){
            startText.setText("You are assigned single order");
            btnCheckpoint.setVisibility(View.GONE);
            imgCheckpoint.setVisibility(View.GONE);
        }else if(deliveryType.equals("Split")){
//            if(deliveryPos.equals("Primary")){
//                startText.setText("You are primary delivery boy");
//            }else{
//                startText.setText("You are secondary delivery boy");
//                btnCustomer.setVisibility(View.GONE);
//                imgCustomer.setVisibility(View.GONE);
//            }
            startText.setText("You are assigned split order");
            btnCustomer.setVisibility(View.GONE);
            imgCustomer.setVisibility(View.GONE);
        }

        linearLayout = (LinearLayout) findViewById(R.id.liveTaskSingle);

        if(btnState.equals("disabled")){
            btnConfirm.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }

        btnPickup.setText("Address");
        btnPickup.setEnabled(false);
        btnPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this, Pickup.class);
                intent.putExtra("deliveryType", deliveryType);
                intent.putExtra("deliveryPos", deliveryPos);
                startActivity(intent);
            }
        });


        if(isPicked.equals("single")){
            btnCustomer.setEnabled(true);
            imgCustomer.setEnabled(true);
        }else if(isPicked.equals("primary")){
            btnCheckpoint.setEnabled(true);
            imgCheckpoint.setEnabled(true);
        }else if(isPicked.equals("secondary")){
            btnCheckpoint.setEnabled(true);
            imgCheckpoint.setEnabled(true);
        }else if(isPickedCheck.equals("single")){
            btnCheckpoint.setEnabled(false);
            btnCustomer.setEnabled(true);
            imgCheckpoint.setEnabled(false);
            imgCustomer.setEnabled(true);
        }else if(isPickedCheck.equals("primary")){
            btnCustomer.setEnabled(true);
        }else{
            btnCustomer.setEnabled(false);
            btnCheckpoint.setEnabled(false);
        }

        btnCheckpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this, Checkpoint.class);
                intent.putExtra("deliveryType", deliveryType);
                intent.putExtra("deliveryPos", deliveryPos);
                startActivity(intent);
            }
        });

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this, Customer.class);
                intent.putExtra("deliveryType", deliveryType);
                intent.putExtra("deliveryPos", deliveryPos);
                startActivity(intent);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Start.this, "Confirmed", Toast.LENGTH_SHORT).show();
                if(deliveryType.equals("Split")){
                    if(deliveryPos.equals("Primary")){
//                        btnCustomer.setEnabled(true);
//                        btnCustomer.setVisibility(View.VISIBLE);
//                        imgCustomer.setVisibility(View.VISIBLE);

                    }else{
//                        btnCustomer.setEnabled(false);
                        btnCustomer.setVisibility(View.VISIBLE);
                        imgCustomer.setVisibility(View.VISIBLE);
                    }
                }
                btnPickup.setEnabled(true);
                btnConfirm.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                noOrders.setVisibility(View.VISIBLE);
                btnConfirm.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            }
        });
    }
}
