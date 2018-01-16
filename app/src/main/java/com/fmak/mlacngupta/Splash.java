package com.fmak.mlacngupta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Splash extends AppCompatActivity  implements ServiceExcecuted{

    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final String url_mobilestatus = getResources().getString(R.string.mobilestatus);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        final String pm = sp.getString("mobile","");

        ImageView image = (ImageView)findViewById(R.id.imageView4);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade);
        image.startAnimation(animation1);

        Thread t = new Thread(){
            public void run()
            {
                try {
                    Thread.sleep(2800);
                    if(pm.equals("")) {
                        Intent i = new Intent(Splash.this, RegisterActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        String url = url_mobilestatus + "?m=" + pm;
                        LoadTextTask hitweb = new LoadTextTask(Splash.this, url, Splash.this);
                        hitweb.execute();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }

    @Override
    public void onResult(String result, int reqCode) {
        int response = -1;
        try {
            response = new Integer(result);
            if(response==0){
                Intent i = new Intent(Splash.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
            else if(response==1){
                Intent i = new Intent(Splash.this, OTPActivity.class);
                startActivity(i);
                finish();
            }
            else if(response==2){
                Intent i = new Intent(Splash.this, HomeActivity.class);
                startActivity(i);
                finish();
            }

        }catch (Exception e){
            response = -1;
            Toast.makeText(this, "Please connect with INTERNET \n TRY AGAIN!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
