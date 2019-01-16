package partyhelper.com.xpoliem.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginFragment extends Fragment {
    EditText edtUser,edtPass;
    TextView txtError;
    Button btnlogin;
    String  username,pass;
    boolean Result1,Result2;
    public final static String TAG = LoginFragment.class.getSimpleName();
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        txtError = (TextView)v.findViewById(R.id.textError);
         edtUser = (EditText)v.findViewById(R.id.edtTxtUsername);
         edtPass = (EditText)v.findViewById(R.id.edtTxtPassword);
        btnlogin = (Button)v.findViewById(R.id.btnLogin);
        btnlogin.setEnabled(false);

        ((MainActivity) getActivity()).internetFragTest();
        edtUser.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Result1 = Check(edtUser,txtError);
                if (Result1) {
                    if (Result2) {
                        btnlogin.setEnabled(true);
                    }
                }


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });

        edtPass.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Result2 = Check(edtPass,txtError);
                if (Result1) {
                    if (Result2) {
                        btnlogin.setEnabled(true);
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = edtUser.getText().toString();
                pass = edtPass.getText().toString();

                Login u = new Login();
                u.getData(username,pass);
                if(u.results){
                    ((PartyHelper) getActivity().getApplication()).setID(u.ID);
                    ((PartyHelper) getActivity().getApplication()).setLoggedIn(true);
                    ((MainActivity)getActivity()).loginThings();
                    ((MainActivity)getActivity()).GoToMain();


                }else{
                    txtError.setText("Could not log you in try again and check your internet connection");
                }

            }
        });



        Log.i("TAG",""+Result1+Result2);



        return v;
    }



    public boolean Check(final EditText edt, final TextView err){

        if(edt.getText().toString().length() <6){
            err.setText("Too short");
            return false;

        }else{
            if(edt.getText().toString().length() >20){
                err.setText("Too Long");
                return false;

            }else{
                err.setText("Perfect");
                return true;

            }
        }

       // Log.i("TAG",""+result[0]);


    }





}
