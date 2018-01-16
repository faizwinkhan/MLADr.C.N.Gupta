package com.fmak.mlacngupta;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadTextTask extends AsyncTask<String, Integer, String>
{
    String dataurl;
    int reqCode;
    ServiceExcecuted se;
    StringBuilder sb = new StringBuilder();

    //0 means Only Text and 1 means With Image
    Context context;

    public LoadTextTask(Context con, String dataurl, ServiceExcecuted se)
    {
        this.dataurl = dataurl;
        this.se = se;
        this.context = con;
     }

    @Override
    protected String doInBackground(String... params)
    {

        try {
            HttpURLConnection conn;
            URL url = new URL(dataurl);
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
           }
        }catch (Exception e){
            //Toast.makeText(context,"Unable to load data",Toast.LENGTH_LONG).show();
        }
        return sb.toString();
    }

    protected void onPostExecute(String Result)
    {
        se.onResult(sb.toString(), reqCode);
    }
}