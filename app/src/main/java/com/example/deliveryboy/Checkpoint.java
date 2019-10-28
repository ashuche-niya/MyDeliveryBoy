package com.example.deliveryboy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Checkpoint extends AppCompatActivity {

    private ListView listView;
    private String[] deliListItem;
    private ImageView imgPick;
    private String deliveryType, deliveryPos;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint);

        bundle = getIntent().getExtras();
        if(bundle != null){
            deliveryType = bundle.getString("deliveryType");
            deliveryPos = bundle.getString("deliveryPos");
        }

        listView = (ListView) findViewById(R.id.otherDeli);
        deliListItem = getResources().getStringArray(R.array.array_deli);
        imgPick = (ImageView) findViewById(R.id.imgPick);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, deliListItem);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Checkpoint.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        imgPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Checkpoint.this, Start.class);
//                intent.putExtra("pickedUpCheck", "true");
//                if(deliveryType.equals("Single")){
//                    intent.putExtra("pickedUpCheck", "single");
//                }else{
//                    if(deliveryPos.equals("Primary")){
//                        intent.putExtra("pickedUpCheck", "primary");
//                    }else{
//                        intent.putExtra("pickedUpCheck", "secondary");
//                    }
//                }
                startActivity(intent);
                finish();
            }
        });
    }
}
