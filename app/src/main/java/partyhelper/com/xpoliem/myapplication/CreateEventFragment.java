package partyhelper.com.xpoliem.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;


public class CreateEventFragment extends Fragment {
    String name,lat,lon;
    String formatedDate,formattedTime;
    DatePicker dp ;
    TimePicker tp;
    EditText edtName;
    ImageView imgView;
    TextView errTxt;
    Button btnCreateEvent,btnImageSet,btnPlace;
    boolean btnShow;
    private static int RESULT_LOAD_IMG = 1;
    int PLACE_PICKER_REQUEST = 1;
    String picturePath;

    public final static String TAG = CreateEventFragment.class.getSimpleName();
    public static CreateEventFragment newInstance() {
        return new CreateEventFragment();
    }


    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_event, container, false);
        btnShow = false;
        edtName = (EditText)v.findViewById(R.id.edtName);
        dp = (DatePicker)v.findViewById(R.id.datePicker);
        tp = (TimePicker)v.findViewById(R.id.timePicker);
        imgView = (ImageView)v.findViewById(R.id.imgViewEvent);
        errTxt = (TextView)v.findViewById(R.id.textErr);
        btnCreateEvent = (Button)v.findViewById(R.id.btnCreate);
        btnImageSet = (Button)v.findViewById(R.id.GetImage);
        btnPlace = (Button)v.findViewById(R.id.btnPlace);
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEvent();
            }
        });
        btnImageSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageDisplay();
            }
        });
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickPlace();
            }
        });
        btnCreateEvent.setVisibility(View.INVISIBLE);

        new ListAsync().execute();


        return  v ;
    }
    public void CreateEvent(){
        int   day  = dp.getDayOfMonth();
        int   month= dp.getMonth();
        int   year = dp.getYear();

        int hour = tp.getCurrentHour();
        int min = tp.getCurrentMinute();

        formattedTime = String.valueOf(hour)+":"+String.valueOf(min);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

        formatedDate = sdf.format(new Date(2015, month, day));


        name = edtName.getText().toString();
        CreateEvent cE  = new CreateEvent();
        //Toast.makeText(getActivity(), formatedDate+formattedTime, Toast.LENGTH_LONG).show();
        cE.getData(((PartyHelper) getActivity().getApplication()).getID(),name,formatedDate,formattedTime,lat,lon);
        uploadFile(picturePath);

    }
    public void ImageDisplay(){
        loadImageFromGallery();
    }
    public void PickPlace(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Context context = getActivity().getApplicationContext();
        btnCreateEvent.setVisibility(View.VISIBLE);
        try {
            startActivityForResult(builder.build(context), 0);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    public void loadImageFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){

            case 0:
                if (resultCode == getActivity().RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                final LatLng location = place.getLatLng();
                lat = String.valueOf(location.latitude);
                lon = String.valueOf(location.longitude);
                btnShow = true;
            }
                break;
            case 1 :

                if (data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    Toast.makeText(getActivity(), picturePath,
                            Toast.LENGTH_LONG).show();
                    //imgView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    Picasso.with(getActivity()).load(new File(picturePath)).transform(new CircleTransform()).error(R.drawable.ic_launcher).into(imgView);

                    cursor.close();
                } else {
                    Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
        }
    }
    public boolean CheckEvent(final EditText edt, final TextView err) {

        final CheckEventName n = new CheckEventName();
        if (edt.getText().toString().length() < 6) {
            err.setText("Too Short");
            return false;
        } else {
            if (edt.getText().toString().length() > 20) {
                err.setText("Too Long");
                return false;
            } else {
                if (n.checkEventName(edt.getText().toString())) {
                    err.setText("Perfect");
                    return true;
                } else {
                    err.setText("Event Name taken");
                    return false;
                }


            }
        }
    }

    public int uploadFile(String sourceFileUri) {


        String fileName =  name+".jpg";
        int serverResponseCode  = 0;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {




            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("http://xpoliem.com/android/EventImage.php");
                StrictMode.enableDefaults();
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);


                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename="+ fileName + lineEnd);


                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {

                         /*   String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://www.androidexample.com/media/uploads/"
                                    +uploadFileName;*/

                            //messageText.setText(msg);
                            Toast.makeText(getActivity(), "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {


                ex.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        // messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(getActivity(), "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {


                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(getActivity(), "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("server Exception", "Exception : "
                        + e.getMessage(), e);
            }


            return serverResponseCode;

        } // End else block
    }

    class ListAsync extends AsyncTask<Void, Integer, String> {


        protected String doInBackground(Void...arg0) {
            edtName.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    CheckEvent(edtName, errTxt);



                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                }

            });

            return "You are at PostExecute";
        }



        protected void onPostExecute(String result) {

        }
    }


}


