package com.example.deliveryboy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pickup extends AppCompatActivity {

    private Button quesBtnPick;
    private ImageView imgPick;
    private String deliveryType, deliveryPos;
    private Bundle bundle;
    private RecyclerView recyclerView;
    private VendorAdapter vendorAdapter;
    private List<Vendor> vendorList = new ArrayList<>();
    private ArrayList<Boolean> checkedList = new ArrayList<>();

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        apiInterface = ApiUtils.getAPIService();

        quesBtnPick = (Button) findViewById(R.id.quesBtnPick);
        imgPick = (ImageView) findViewById(R.id.imgPick);
        recyclerView = (RecyclerView) findViewById(R.id.listItem);

        bundle = getIntent().getExtras();
        if(bundle != null){
            deliveryType = bundle.getString("deliveryType");
            deliveryPos = bundle.getString("deliveryPos");
        }

        quesBtnPick.setEnabled(false);
        imgPick.setEnabled(false);

        vendorAdapter = new VendorAdapter(vendorList, Pickup.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vendorAdapter);

        prepareList();

        vendorAdapter.setOnStateChangeListener(new VendorAdapter.OnStateChangeListener() {
            @Override
            public void newStateChanged(int position) {
                vendorList.get(position).setChecked(!vendorList.get(position).isChecked());
                checkedList.set(position, Boolean.valueOf(vendorList.get(position).isChecked()));

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        vendorAdapter.notifyDataSetChanged();
                    }
                });
                if(checkedList.size() != 0){
                    boolean b1 = true;
                    for(int i=0;i<checkedList.size();i++){
                        Log.i("Hello", "Chal rahi hai");
                        if(!Boolean.TRUE.equals(checkedList.get(i))){
                            b1 = false;
                        }
                    }
                    if(b1){
                        imgPick.setEnabled(true);
                        quesBtnPick.setEnabled(true);
                    }
                }
            }
        });


        imgPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quesBtnPick.setText("Picked Up");
                quesBtnPick.setEnabled(false);

                Call<PickupResponse> calldash = apiInterface.getResponse("125", "pickedup");
                calldash.enqueue(new Callback<PickupResponse>() {
                    @Override
                    public void onResponse(Call<PickupResponse> call, Response<PickupResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(Pickup.this, response.body().getSuccess(), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Pickup.this, "Response failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PickupResponse> call, Throwable t) {
                        Toast.makeText(Pickup.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });


//                Intent intent = new Intent(Pickup.this, Start.class);
//                intent.putExtra("pickedUp", "true");
//                intent.putExtra("button", "disabled");
//                if(deliveryType.equals("Single")){
//                    intent.putExtra("pickedUp", "single");
//                }else{
//                    if(deliveryPos.equals("Primary")){
//                        intent.putExtra("pickedUp", "primary");
//                    }else{
//                        intent.putExtra("pickedUp", "secondary");
//                    }
//                }
//                startActivity(intent);
//                finish();
            }
        });




    }

    void runThread() {

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                boolean b1 = true;
//                for(int i=0;i<checkedList.size();i++){
//                    if(!Boolean.TRUE.equals(checkedList.get(i))){
//                        b1 = false;
//                    }
//                }
//                if(b1){
//                    imgPick.setEnabled(true);
//                    quesBtnPick.setEnabled(true);
//                }
//                run();
//            }
//        });



    }

    void prepareList() {

        Vendor vendor = new Vendor("Alpha", false);
        vendorList.add(vendor);
        checkedList.add(Boolean.FALSE);

        vendor = new Vendor("Alpha", false);
        vendorList.add(vendor);
        checkedList.add(Boolean.FALSE);

        vendor = new Vendor("Alpha", false);
        vendorList.add(vendor);
        checkedList.add(Boolean.FALSE);

        vendor = new Vendor("Alpha", false);
        vendorList.add(vendor);
        checkedList.add(Boolean.FALSE);


    }
}
