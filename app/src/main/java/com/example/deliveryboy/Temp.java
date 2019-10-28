package com.example.deliveryboy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.util.HttpAuthorizer;

import org.json.JSONObject;


public class Temp extends AppCompatActivity {

    private final String TAG = "Temp";

    private TextView textView;
    private PrivateChannel privateChannel;
    private Pusher pusher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        textView = (TextView) findViewById(R.id.text);

        HttpAuthorizer authorizer = new HttpAuthorizer("https://fathomless-journey-91846.herokuapp.com/vendor/pusher_auth");
        PusherOptions options = new PusherOptions().setAuthorizer(authorizer);
        pusher = new Pusher("7c495f369f4053064877", options);


//        privateChannel = pusher.subscribePrivate("", new PrivateChannelEventListener() {
//            @Override
//            public void onAuthenticationFailure(String message, Exception e) {
//                bindToEvents();
//            }
//
//            @Override
//            public void onSubscriptionSucceeded(String channelName) {
//
//            }
//
//            @Override
//            public void onEvent(PusherEvent event) {
//
//            }
//        });
//
//        privateChannel.bind("", new PrivateChannelEventListener() {
//            @Override
//            public void onAuthenticationFailure(String message, Exception e) {
//
//            }
//
//            @Override
//            public void onSubscriptionSucceeded(String channelName) {
//
//            }
//
//            @Override
//            public void onEvent(PusherEvent event) {
//
//            }
//        });




//        Channel channel = pusher.subscribe("my-channel");


//        if (channel.isSubscribed()) {
//            Toast.makeText(this, "kjdnkjd", Toast.LENGTH_SHORT).show();
//        }

//        channel.bind("my-event", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(final PusherEvent event) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        try{
//                            JSONObject object = new JSONObject(event.getData());
//                            textView.setText(object.getString("jdl"));
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });




        pusher.connect();
    }

    void subscribeToPrivate(String channelName) {
        privateChannel = pusher.subscribePrivate(channelName, new PrivateChannelEventListener() {
            @Override
            public void onAuthenticationFailure(String message, Exception e) {
                bindToEvents();
            }

            @Override
            public void onSubscriptionSucceeded(String channelName) {

            }

            @Override
            public void onEvent(PusherEvent event) {

            }
        });

    }

    void bindToEvents() {
        privateChannel.bind("message-sent", new PrivateChannelEventListener() {
            @Override
            public void onAuthenticationFailure(String message, Exception e) {

            }

            @Override
            public void onSubscriptionSucceeded(String channelName) {

            }

            @Override
            public void onEvent(PusherEvent event) {

            }
        });
    }
}
