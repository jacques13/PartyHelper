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
 * Created by Jacques on 4/3/2015.
 */
public class CheckInvite {

    public String invited  ;

    public void getData(String ID ,String u_id,String event_id){
        String result = "";
        InputStream isr = null;
        StrictMode.enableDefaults();
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost =
            new HttpPost("http://xpoliem.com/android/checkInvite.php?my_id="+ID+"&u_id="+u_id+"&event_id="+event_id); //onthou om reg te maak
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




        }
        catch(Exception e){
            Log.e("log_tag", "Error  converting result " + e.toString());
        }

        //parse json data
        try {
            String s = "";
            JSONArray jArray = new JSONArray(result);

            JSONObject json = jArray.getJSONObject(0);
            invited = json.getString("result");

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
