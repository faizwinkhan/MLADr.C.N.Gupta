package com.fmak.mlacngupta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    WebView wb;
    String pn,  pe, pm , pf;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT >= 21){
            Uri[] results = null;
            //Check if response is positive
            if(resultCode== Activity.RESULT_OK){
                if(requestCode == FCR){
                    if(null == mUMA){
                        return;
                    }
                    if(intent == null){
                        //Capture Photo if no image available
                        if(mCM != null){
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    }else{
                        String dataString = intent.getDataString();
                        if(dataString != null){
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        }else{
            if(requestCode == FCR){
                if(null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        wb = (WebView) findViewById(R.id.webview);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        pn = sp.getString("name","");
        pe = sp.getString("email","");
        pm = sp.getString("mobile","");
        pf = sp.getString("fname","");

        Intent ri = getIntent();
        String page = ri.getExtras().getString("page");
        //http://prowessapps.in/complaint_app/login.php
        loadWebPage(page+"?n="+pn+"&m="+pm+"&e="+pe+"&f="+pf);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private ValueCallback<Uri> mUploadMessage;
    void loadWebPage(String url)
    {
        wb.getSettings().setJavaScriptEnabled(true);
        wb.setWebChromeClient(new WebChromeClient(){
            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg){
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                SecondActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FCR);
            }
            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType){
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                SecondActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }
            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                SecondActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), SecondActivity.FCR);
            }
            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams){
                if(mUMA != null){
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(SecondActivity.this.getPackageManager()) != null){
                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    }catch(IOException ex){
                        Log.e("TAG", "Image file creation failed", ex);
                    }
                    if(photoFile != null){
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    }else{
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;
                if(takePictureIntent != null){
                    intentArray = new Intent[]{takePictureIntent};
                }else{
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });
        wb.getSettings().setLoadsImagesAutomatically(true);
        wb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // ActionBar bar = getSupportActionBar();
        // bar.setDisplayHomeAsUpEnabled(true);

        wb.setWebViewClient(new WebViewClient() {
            ProgressDialog prDialog;


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                prDialog = ProgressDialog.show(SecondActivity.this, null, "loading, please wait...");
                //"loading, please wait..."
                if(url.equals("http://prowessapps.in/complaint_app/complaint_registration_blogic.php")){
                    prDialog.dismiss();
                }
                //onclidk="window.location=nav://close"
                if(url.equals("http://exitme/")){
                    Toast.makeText(SecondActivity.this,"Saved Successfully!!",Toast.LENGTH_SHORT).show();
                    finish();
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                prDialog.dismiss();
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }




            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Do something
               // Toast.makeText(SecondActivity.this,"ERROR : "+errorCode,Toast.LENGTH_LONG).show();
                if(!failingUrl.equals("http://exitme/")) {
                    startActivity(new Intent(SecondActivity.this, ErrorActivity.class));
                    finish();
                }

            }
        });


        wb.loadUrl(url);

    }

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Online Portal");
        builder.setMessage("Are You want to Exit ??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.dmenu_home){
            finish();
        }
        else if(id==R.id.dmenu_complaint){
            finish();
            startActivity(new Intent(this,ComplaintActivity.class));
        }
        else if(id==R.id.dmenu_suggestion){
            String page = getResources().getString(R.string.web_user_suggestion);;
            loadWebPage(page+"?n="+pn+"&m="+pm+"&e="+pe);
        }
        else if(id==R.id.dmenu_showcase){
            String page = getResources().getString(R.string.web_work_showcase);;
            loadWebPage(page+"?n="+pn+"&m="+pm+"&e="+pe);
        }
        else if(id==R.id.dmenu_mission){
            finish();
            startActivity(new Intent(this,MCMRActivity.class));
        }
        else if(id==R.id.dmenu_volenteer){
            finish();
            startActivity(new Intent(this,VolenteerActivity.class));
        }
        else if(id==R.id.dmenu_profile){
            String page = getResources().getString(R.string.web_profile);;
            loadWebPage(page+"?n="+pn+"&m="+pm+"&e="+pe);
        }
        else if(id==R.id.dmenu_news){
            String page = getResources().getString(R.string.web_news);;
            loadWebPage(page+"?n="+pn+"&m="+pm+"&e="+pe);
        }
        else if(id==R.id.dmenu_connect){
            finish();
            startActivity(new Intent(this,GetConnectActivity.class));
        }
        else if(id==R.id.dmenu_live){
            finish();
            startActivity(new Intent(this,LiveActivity.class));
        }
        else if(id==R.id.dmenu_exit){
            finishAffinity();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.home){
            finishAffinity();
            startActivity(new Intent(this,HomeActivity.class));
        }
        else if(id==R.id.swach){
            //String page = getResources().getString(R.string.web_user_suggestion);
            //loadWebPage(page+"?n="+pn+"&m="+pm+"&e="+pe);
        }
        else if(id==R.id.meet){
            //String page = getResources().getString(R.string.web_work_showcase);;
            //loadWebPage(page+"?n="+pn+"&m="+pm+"&e="+pe);
        }
        else if(id==R.id.shareapp){
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_SUBJECT, "RCPL APP");
            share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.fmak.mlacngupta");
            startActivity(Intent.createChooser(share, "Share RCPL App!!"));
        }
        else if(id==R.id.rateapp){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.fmak.mlacngupta"));
            startActivity(intent);
        }
        else if(id==R.id.about){
            startActivity(new Intent(this,AboutUs.class));
        }
        else if(id==R.id.contact){
            String page = getResources().getString(R.string.web_contact);;
            loadWebPage(page+"?n="+pn+"&m="+pm+"&e="+pe);
        }
        else if(id==R.id.aboutus){
            String page = getResources().getString(R.string.web_aboutus);;
            loadWebPage(page+"?n="+pn+"&m="+pm+"&e="+pe);
        }
        else if(id==R.id.exit){
            finish();
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


}