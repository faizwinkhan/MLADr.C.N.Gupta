package com.fmak.mlacngupta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutUs extends AppCompatActivity {

    int click;
    String info = "Powered By: Prowess Apps \nhttp://www.prowessapps.in\n\nDevelopers : \n-------------------\nMr. Faiz Mohd Arif Khan\nMr. Mohd Daneyal Hasan Lari\nMr. Brajraj Singh";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        TextView magic = (TextView)findViewById(R.id.magic);
        magic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                click++;
                if(click==4){
                    Toast.makeText(AboutUs.this,"CLICK 2 more times",Toast.LENGTH_LONG).show();
                }
                if(click==6){
                    Toast.makeText(AboutUs.this,info,Toast.LENGTH_LONG).show();
                    click=0;
                }
            }
        });
    }
}
