package com.fmak.mlacngupta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements ServiceExcecuted {

    EditText name, fname, email, mobile;
    Button save;
    SharedPreferences sp;
    String n, f, m,e;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_profile);



        sp = PreferenceManager.getDefaultSharedPreferences(this);

        name = (EditText)findViewById(R.id.name);
        fname = (EditText)findViewById(R.id.fname);
        email = (EditText)findViewById(R.id.email);
        mobile = (EditText)findViewById(R.id.mobile);
        save = (Button)findViewById(R.id.bsave);

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(validate()){

                    n = name.getText().toString();
                    e = email.getText().toString();
                    m = mobile.getText().toString();
                    f = fname.getText().toString();


                    String url_reg = getResources().getString(R.string.url_reg);


                    String rn = n.replace(" ","%20");
                    String re = e.replace(" ","%20");
                    String rm = m.replace(" ","%20");
                    String rf = f.replace(" ","%20");

                    url_reg += "?n="+rn+"&f="+rf+"&m="+rm+"&e="+re;
                    Toast.makeText(RegisterActivity.this, url_reg, Toast.LENGTH_SHORT).show();
                    LoadTextTask hitweb = new LoadTextTask(RegisterActivity.this, url_reg, RegisterActivity.this);
                    hitweb.execute();
                    pd = new ProgressDialog(RegisterActivity.this);
                    pd.setMessage("saving...");
                    pd.setCancelable(false);
                    pd.show();
                }
            }
        });

    }

    @Override
    public void onResult(String result, int reqCode) {
        int response = -1;
        try {
            response = new Integer(result);
            if(response==1){
                Toast.makeText(this,"Profile Saved!!\nPlease Verify Your Mobile Number",Toast.LENGTH_LONG).show();
                pd.dismiss();

                SharedPreferences.Editor edit = sp.edit();

                edit.putString("name",n);
                edit.putString("email",e);
                edit.putString("mobile",m);
                edit.putString("fname",f);

                edit.putInt("mobilevalidate",0);
                edit.commit();
                startActivity(new Intent(this,OTPActivity.class));
                finish();
            }
            else if(response==0){
                Toast.makeText(this,"Profile Not Saved.\nTry Again!!",Toast.LENGTH_LONG).show();
            }
            else if(response==2){
                Toast.makeText(this,"Your Profile Already Exist.\nPlease Verify Your Mobile Number",Toast.LENGTH_LONG).show();
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("name",n);
                edit.putString("email",e);
                edit.putString("mobile",m);
                edit.putString("fname",f);
                edit.putInt("mobilevalidate",0);
                edit.commit();
                startActivity(new Intent(this,OTPActivity.class));
                finish();
            }

        }catch (Exception e){
            response = -1;
            Toast.makeText(this,"NO INTERNET\nPlease connect with INTERNET\n"+e.getMessage(), Toast.LENGTH_LONG).show();
            pd.dismiss();
            finishAffinity();
        }
    }


    boolean validate(){
       if(name.getText().toString().equals("")){
            name.setError("fill your name");
            return false;
        }
        else if(fname.getText().toString().equals("")){
           fname.setError("fill father name");
           return false;
       }
        else  if(email.getText().toString().equals("")){
            name.setError("fill email id");
            return false;
        }
        else  if(mobile.getText().toString().equals("")){
            mobile.setError("fill mobile number");
            return false;
        }
        else  if(mobile.getText().toString().length()!=10){
            mobile.setError("invalid mobile number\nProvide 10 digit number");
            return false;
        }
        else  if("987".indexOf(mobile.getText().toString().charAt(0))==-1){
            mobile.setError("invalid mobile number\nMust start with 9,8, or 7");
            return false;
        }
        else{
            return true;
        }
    }


}
