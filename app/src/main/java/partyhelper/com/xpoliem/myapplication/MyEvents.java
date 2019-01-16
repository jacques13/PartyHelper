package partyhelper.com.xpoliem.myapplication;

import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jacques on 4/2/2015.
 */
public class MyEvents {

    public String[] ids,names,times,dates,latitudes,longitudes,goings,images;

    public void getData(String u_id){
        String result = "";
        InputStream isr = null;
        StrictMode.enableDefaults();
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://xpoliem.com/android/getMyEvents.php?u_id="+u_id); //onthou om reg te maak
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();

        }
        catch(Exception e){
            Log.e("log_tag", "Error in http connection " + e.toString());
            //textView.setText("Couldnt connect to database");
        }
        //convert response to string
        try{

            result = inputStreamToString(isr);
            result.replaceAll("\\s+","");

            isr.close();



            Log.e("log_tag", result);
        }
        catch(Exception e){
            Log.e("log_tag", "Error  converting result " + e.toString());
        }

        //parse json data
        try {
            String s = "";
            JSONArray jArray = new JSONArray(result);
            names = new String[jArray.length()];
            ids = new String[jArray.length()];
            times = new String[jArray.length()];
            dates = new String[jArray.length()];
            latitudes = new String[jArray.length()];
            longitudes = new String[jArray.length()];
            goings = new String[jArray.length()];
            images = new String[jArray.length()];;


            for(int i=0; i<jArray.length();i++){
                JSONObject json = jArray.getJSONObject(i);
                ids[i] = json.getString("id");
                names[i] = json.getString("name");
                times[i] = json.getString("time");
                dates[i] = json.getString("date");
                latitudes[i] = json.getString("latitude");
                longitudes[i] = json.getString("longitude");
                goings[i] = json.getString("going");
                images[i] = json.getString("image");

            }



        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "Error Parsing Data "+e.toString());
            //textView.setText("error");
        }

    }

    private String inputStreamToString(InputStream is)
    {

        String line = "";
        StringBuilder total = new StringBuilder();
        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is), 1024 * 4);
        // Read response until the end
        try
        {

            while ((line = rd.readLine()) != null)
            {
                total.append(line);
            }
        } catch (IOException e)
        {
            Log.e("log_tag", "error build string" + e.getMessage());
        }
        // Return full string
        return total.toString();
    }
}
