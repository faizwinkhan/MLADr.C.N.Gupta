package com.fmak.mlacngupta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OTPActivity extends AppCompatActivity implements ServiceExcecuted{

    TextView resend, otpverify, mobileno;
    EditText otp;
    Button send;
    SharedPreferences sp;
    ProgressDialog pd;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        send = (Button)findViewById(R.id.sendotp);
        otp = (EditText)findViewById(R.id.otp);

        resend = (TextView)findViewById(R.id.resend);
        otpverify = (TextView)findViewById(R.id.otpverify);
        mobileno = (TextView)findViewById(R.id.mobileno);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String pn = sp.getString("name","");
        final String pe = sp.getString("email","");
        final String pm = sp.getString("mobile","");
        final int pev = sp.getInt("mobilevalidate",-1);

        final String votp = getResources().getString(R.string.votp);
        final String resendotp = getResources().getString(R.string.rsotp);

        mobileno.setText("+91-"+pm);
        //FOR VERIFICATION
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(validate()) {
                    url = votp + "?m=" + pm + "&c=" + otp.getText().toString();
                    LoadTextTask hitweb = new LoadTextTask(OTPActivity.this, url, OTPActivity.this);
                    hitweb.execute();
                    pd = new ProgressDialog(OTPActivity.this);
                    pd.setMessage("verifying...");
                    pd.setCancelable(false);
                    pd.show();
                }
            }
        });

        //TO GET NEW OTP
        resend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                url = resendotp + "?m="+pm;
                LoadTextTask hitweb = new LoadTextTask(OTPActivity.this, url, OTPActivity.this);
                hitweb.execute();
                pd = new ProgressDialog(OTPActivity.this);
                pd.setMessage("sending...");
                pd.setCancelable(false);
                pd.show();

            }
        });
    }

    @Override
    public void onResult(String result, int reqCode) {
        int response = -1;
        try {
            response = new Integer(result);
            if(response==1){

                pd.dismiss();
                Thread t = new Thread(){
                    @Override
                    public void run() {
                        try{
                            sleep(2000);
                            startActivity(new Intent(OTPActivity.this,HomeActivity.class));
                            finish();
                        }catch (Exception e){}
                    }
                };

                success();
                SharedPreferences.Editor edit = sp.edit();
                edit.putInt("mobilevalidate",1);
                edit.commit();
                //startActivity(new Intent(OTPActivity.this,HomeActivity.class));
                t.start();


            }
            else if(response==0){
                pd.dismiss();
                fail();
            }
            else if(response==3){
                pd.dismiss();
                Toast.makeText(OTPActivity.this, "OTP RE-SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            }


        }catch (Exception e){
            response = -1;
            pd.dismiss();
            Toast.makeText(this, "Please connect with INTERNET \n TRY AGAIN!!\n"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void success(){
        otpverify.setVisibility(View.VISIBLE);
        otpverify.setText("OTP Verified !!");
        otpverify.setTextColor(Color.GREEN);
    }
    void fail(){
        otpverify.setVisibility(View.VISIBLE);
        otpverify.setText("Invalid PIN !!");
        otpverify.setTextColor(Color.RED);
    }
    boolean validate(){
        String data = otp.getText().toString();
        if(data.equals("")){
            otp.setError("Please Enter OTP");
            return false;
        }
        else if(data.length()!=4){
            otp.setError("Please Enter Valid 4 digit PIN");
            return false;
        }
        else{
            return true;
        }
    }
}
