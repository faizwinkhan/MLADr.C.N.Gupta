package com.fmak.mlacngupta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Method;

public class HomeActivity extends AppCompatActivity {

    GridView gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RelativeLayout menu1 = (RelativeLayout)findViewById(R.id.menu_complaint);
        menu1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ComplaintActivity.class));
                //Snackbar.make(view,"UNDER CONSTRUCTION",Snackbar.LENGTH_LONG).show();
            }
        });

        RelativeLayout menu2 = (RelativeLayout)findViewById(R.id.menu_suggestion);
        menu2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String page = getResources().getString(R.string.web_user_suggestion);
                Intent open = new Intent(HomeActivity.this,SecondActivity.class);
                open.putExtra("page",page);
                startActivity(open);
            }
        });

        RelativeLayout menu3 = (RelativeLayout)findViewById(R.id.menu_showcase);
        menu3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String page = getResources().getString(R.string.web_work_showcase);
                Intent open = new Intent(HomeActivity.this,SecondActivity.class);
                open.putExtra("page",page);
                startActivity(open);
            }
        });

        RelativeLayout menu4 = (RelativeLayout)findViewById(R.id.menu_mcmr);
        menu4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,MCMRActivity.class));
            }
        });

        RelativeLayout menu5 = (RelativeLayout)findViewById(R.id.menu_volenteer);
        menu5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,VolenteerActivity.class));
            }
        });

        RelativeLayout menu6 = (RelativeLayout)findViewById(R.id.menu_profile);
        menu6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String page = getResources().getString(R.string.web_profile);
                Intent open = new Intent(HomeActivity.this,SecondActivity.class);
                open.putExtra("page",page);
                startActivity(open);
            }
        });

        RelativeLayout menu7 = (RelativeLayout)findViewById(R.id.menu_news);
        menu7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String page = getResources().getString(R.string.web_news);
                Intent open = new Intent(HomeActivity.this,SecondActivity.class);
                open.putExtra("page",page);
                startActivity(open);
            }
        });


        RelativeLayout menu8 = (RelativeLayout)findViewById(R.id.menu_live);
        menu8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent open = new Intent(HomeActivity.this,LiveActivity.class);
                startActivity(open);
            }
        });

        RelativeLayout menu9 = (RelativeLayout)findViewById(R.id.menu_connect);
        menu9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent open = new Intent(HomeActivity.this,GetConnectActivity.class);
                startActivity(open);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.shareapp) {
            //Toast.makeText(this,"Kya Share kroge yr",Toast.LENGTH_LONG).show();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "RCPL APP");
            share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.fmak.mlacngupta");

            startActivity(Intent.createChooser(share, "Share RCPL App!!"));
        }else if(id == R.id.rateapp) {
            // Toast.makeText(this,"Kya Rate kroge yr",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            //Copy App URL from Google Play Store.
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.fmak.mlacngupta"));
            startActivity(intent);
        }
        else if(id == R.id.about)
        {

            startActivity(new Intent(this,AboutUs.class));
        }
        else if(id == R.id.aboutus)
        {
            String page = getResources().getString(R.string.web_aboutus);
            Intent open = new Intent(HomeActivity.this,SecondActivity.class);
            open.putExtra("page",page);
            startActivity(open);
        }
        else if(id == R.id.exit)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId,Menu menu)
    {
        if(featureId == Window.FEATURE_ACTION_BAR && menu !=null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);

                    m.setAccessible(true);
                    m.invoke(menu,true);
                }
                catch(Exception e){throw new RuntimeException(e);}
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    //code to show icons on option menu
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public void onBackPressed() {
       final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Complaint Portal");
        builder.setMessage("Are You want to Exit ??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

