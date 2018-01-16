package com.fmak.mlacngupta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MCMRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcmr);
        RelativeLayout menu1 = (RelativeLayout)findViewById(R.id.leader_add);
        menu1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String page = getResources().getString(R.string.web_mcmr_add);
                Intent open = new Intent(MCMRActivity.this,SecondActivity.class);
                open.putExtra("page",page);
                startActivity(open);
            }
        });

        RelativeLayout menu2 = (RelativeLayout)findViewById(R.id.leader_view);
        menu2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String page = getResources().getString(R.string.web_mcmr_list);
                Intent open = new Intent(MCMRActivity.this,SecondActivity.class);
                open.putExtra("page",page);
                startActivity(open);
            }
        });
    }
}
