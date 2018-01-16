package com.fmak.mlacngupta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class VolenteerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volenteer);
        RelativeLayout menu1 = (RelativeLayout)findViewById(R.id.volenteer_details);
        menu1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent open = new Intent(VolenteerActivity.this,AboutVolunteer.class);
                startActivity(open);
            }
        });

        RelativeLayout menu2 = (RelativeLayout)findViewById(R.id.volenteer_apply_form);
        menu2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String page = getResources().getString(R.string.web_volunteer_form);
                Intent open = new Intent(VolenteerActivity.this,SecondActivity.class);
                open.putExtra("page",page);
                startActivity(open);
            }
        });
    }
}
