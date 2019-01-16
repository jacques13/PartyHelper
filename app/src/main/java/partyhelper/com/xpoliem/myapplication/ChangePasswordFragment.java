package partyhelper.com.xpoliem.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ChangePasswordFragment extends Fragment {
    String oldpass,newpass1,newpass2;
    TextView txtError;
    EditText oldPass,edtPass1,edtPass2;
    Button btnchange;
    boolean Result1,Result2,Result3;

    public final static String TAG = ChangePasswordFragment.class.getSimpleName();
    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }


    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).internetFragTest();





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        txtError = (TextView)v.findViewById(R.id.textError);
        oldPass = (EditText)v.findViewById(R.id.edtTxtOldPass);

         edtPass1 = (EditText)v.findViewById(R.id.edtTxtNewPass1);
         edtPass2 = (EditText)v.findViewById(R.id.edtTxtNewPass2);
            btnchange = (Button)v.findViewById(R.id.btnChange);
            btnchange.setEnabled(false);
      /*  if (!(){
            txtError.setText("no internet");
            btnchange.setEnabled(false);
        }*/
        ((MainActivity) getActivity()).internetFragTest();

        CheckPassword(edtPass1,edtPass2,txtError);

        oldPass.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Result1 = Check(oldPass,txtError);
                if (Result1) {
                    if (Result2) {
                            if(Result3){
                                btnchange.setEnabled(true);
                            }
                    }
                }


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });
        edtPass1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Result2 = Check(edtPass1,txtError);
                if (Result1) {
                    if (Result2) {
                        if(Result3){
                            btnchange.setEnabled(true);
                        }
                    }
                }


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });
        edtPass2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Result3 =  CheckPassword(edtPass2, edtPass2, txtError);
                if (Result1) {
                    if (Result2) {
                        if(Result3){
                            btnchange.setEnabled(true);
                        }
                    }
                }


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }

        });



        btnchange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                oldpass = oldPass.getText().toString();
                newpass1 = edtPass1.getText().toString();
                newpass2 = edtPass2.getText().toString();

                ChangePassword u = new ChangePassword();
                u.getData(((PartyHelper) getActivity().getApplication()).getID(),oldpass,newpass1);

                if(u.results){
                    ((MainActivity)getActivity()).GoToMain();
                }else{
                    txtError.setText("Could not change password try again and check your internet connection");
                }

            }
        });


        return  v ;
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




}
