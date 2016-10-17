package com.solarfloss.moneyproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    RelativeLayout lay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //img = (ImageView)findViewById(R.id.imageView);
        lay = (RelativeLayout)findViewById(R.id.activity_main);

        lay.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                Log.i("Coords",(Float.toString(event.getX()) + "," + Float.toString(event.getY())));

                return true;
            }
        });
        startService(new Intent(this, MyService.class));
    }




    public void stopService(){

    }
}
