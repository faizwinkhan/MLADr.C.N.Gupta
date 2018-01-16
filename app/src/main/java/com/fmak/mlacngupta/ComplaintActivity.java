package com.fmak.mlacngupta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ComplaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        RelativeLayout menu1 = (RelativeLayout)findViewById(R.id.complaint_register);
        menu1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String page = getResources().getString(R.string.web_complaint_reg);
                Intent open = new Intent(ComplaintActivity.this,SecondActivity.class);
                open.putExtra("page",page);
                startActivity(open);
            }
        });

        RelativeLayout menu2 = (RelativeLayout)findViewById(R.id.complaint_track);
        menu2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String page = getResources().getString(R.string.web_complaint_track);
                Intent open = new Intent(ComplaintActivity.this,SecondActivity.class);
                open.putExtra("page",page);
                startActivity(open);
            }
        });


        RelativeLayout menu3 = (RelativeLayout)findViewById(R.id.complaint_document);
        menu3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ComplaintActivity.this);
                String pe = sp.getString("email","");
                String page = "http://prowessapps.in/complaint_app/upload_complaint_document_status.php?e="+pe;
                Uri url = Uri.parse(page);
                Intent open = new Intent(Intent.ACTION_VIEW,url);
               // startActivity(open);
                Snackbar.make(view,"UNDER CONSTRUCTION",Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
