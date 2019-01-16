package partyhelper.com.xpoliem.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class ProfilePicFragment extends Fragment {
    ImageView imgView;
    String picturePath;
    Button buttonLoadImage,buttonUpload;
    private static int RESULT_LOAD_IMG = 1;
    public Boolean selectedPhoto = false;
    public final static String TAG = ProfilePicFragment.class.getSimpleName();
    public static ProfilePicFragment newInstance() {
        return new ProfilePicFragment();
    }
    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = "http://website.com/android/upload.php";


    public ProfilePicFragment() {
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
        View v = inflater.inflate(R.layout.fragment_profile_pic, container, false);
        ((MainActivity) getActivity()).internetFragTest();
        buttonLoadImage = (Button) v.findViewById(R.id.btnSelect);
        buttonUpload = (Button) v.findViewById(R.id.btnUpload);
        imgView  = (ImageView)v.findViewById(R.id.imgView);
        buttonUpload.setEnabled(true);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                loadImageFromGallery(arg0);
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                uploadFile(picturePath);
            }
        });

        return v;
    }

    public void loadImageFromGallery(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMG);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       ((PartyHelper) getActivity().getApplication()).GetUsername();

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
            Log.i("TAG", picturePath);
            buttonUpload.setEnabled(true);
            cursor.close();
        } else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public int uploadFile(String sourceFileUri) {


        String fileName =  ((PartyHelper) getActivity().getApplication()).GetUsername();
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
                URL url = new URL("http://xpoliem.com/android/upload.php");
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





}
