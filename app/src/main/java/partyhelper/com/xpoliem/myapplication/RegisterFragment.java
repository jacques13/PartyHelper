package partyhelper.com.xpoliem.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterFragment extends Fragment {
    String username,email,newpass1,newpass2,gender;
    boolean Result1,Result2,Result3,Result4,Result5;
    Button btnregister;
    EditText edtUsername;
    TextView txtError;
    public final static String TAG = RegisterFragment.class.getSimpleName();
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }



    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ListAsync().execute();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ((MainActivity) getActivity()).internetFragTest();
        txtError = (TextView) v.findViewById(R.id.textError);

        final EditText edtEmail = (EditText) v.findViewById(R.id.edtEmail);
        edtUsername = (EditText) v.findViewById(R.id.edtTxtUsername);
        final EditText edtPass1 = (EditText) v.findViewById(R.id.edtTxtPass1);
        final EditText edtPass2 = (EditText) v.findViewById(R.id.edtTxtPass2);

        final RadioGroup radioSexGroup = (RadioGroup) v.findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        final RadioButton radioSexButton = (RadioButton) v.findViewById(selectedId);
        btnregister = (Button)v.findViewById(R.id.btnRegister);
        btnregister.setEnabled(false);

        //radioSexGroup.setEnabled(false);

        edtPass1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Result2 = Check(edtPass1, txtError);

                                    btnregister.setEnabled(true);



            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });
        edtPass2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Result3 = CheckPassword(edtPass2, edtPass1, txtError);

                                    btnregister.setEnabled(true);



            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });
        edtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Result4 = isValidEmail(edtEmail, txtError);

                                    btnregister.setEnabled(true);



            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });
        Result5 = CheckGender(radioSexGroup,txtError);

        btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = edtUsername.getText().toString();
                email = edtEmail.getText().toString();
                newpass1 = edtPass1.getText().toString();
                newpass1 = edtPass2.getText().toString();
                if(radioSexGroup.getCheckedRadioButtonId()!=-1){
                    int id= radioSexGroup.getCheckedRadioButtonId();
                    View radioButton = radioSexGroup.findViewById(id);
                    int radioId = radioSexGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) radioSexGroup.getChildAt(radioId);
                    gender = (String) btn.getText();
                }

                Register u = new Register();
                u.getData(username, newpass1, email, gender);

                if(u.results){
                    ((MainActivity)getActivity()).GoToLogin();
                    Toast.makeText(getActivity(), "You can now login with your details",
                            Toast.LENGTH_LONG).show();
                }else{
                    txtError.setText("Could not log you in try again and check your internet connection");
                }

            }
        });


        return v;
    }

    private boolean CheckGender(RadioGroup radioSexGroup, TextView txtError) {
        if (radioSexGroup.getCheckedRadioButtonId() == -1){
            txtError.setText("Select a gender");
            return false;

        }else{
            txtError.setText("Perfect");
            return true;
        }


    }

    public final static boolean isValidEmail(final EditText edt, final TextView err) {
         if (TextUtils.isEmpty(edt.getText().toString())) {
                    err.setText("Not A Valid Email");
                   return false;

                } else {
                    err.setText("Perfect");
                    return  android.util.Patterns.EMAIL_ADDRESS.matcher(edt.getText().toString()).matches();
                }
            }
     public boolean CheckUser(final EditText edt, final TextView err) {

        final CheckName n = new CheckName();
        if (edt.getText().toString().length() < 6) {
            err.setText("Too short");
            return false;
        } else {
            if (edt.getText().toString().length() > 20) {
                err.setText("Too Long");
                return false;
            } else {
                if (n.checkUsername(edt.getText().toString())) {
                    err.setText("Perfect");
                    return true;
                } else {
                    err.setText("Username taken");
                    return false;
                }


            }
        }
    }
     public boolean CheckPassword(final EditText edt1,final EditText edt2, final TextView err){
        if(edt2.getText().toString().length() <6){
            err.setText("Too short");
            return false;
        }else{
            if(edt2.getText().toString().length() >20){
                err.setText("Too Long");
                return false;
            }else{
                if(edt2.getText().toString().equals(edt1.getText().toString())){
                    err.setText("Perfect");
                    return true;
                }else {
                    err.setText("Passwords Do Not Match");
                    return false;
                }
            }
        }

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

    class ListAsync extends AsyncTask<Void, Integer, String> {


        protected String doInBackground(Void...arg0) {
            edtUsername.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    Result1 = CheckUser(edtUsername, txtError);

                                        btnregister.setEnabled(true);



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





