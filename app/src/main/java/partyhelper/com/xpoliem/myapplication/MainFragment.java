package partyhelper.com.xpoliem.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MainFragment extends Fragment {
    View v;
    GetUser getUser = new GetUser();

    public final static String TAG = MainFragment.class.getSimpleName();
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public MainFragment() {
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
        new ListAsync().execute();
       v = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).internetFragTest();
        return v;
    }
    public void setup(){

        TextView usernameTxt = (TextView)v.findViewById(R.id.txtViewUsername);
        TextView invitesTxt = (TextView)v.findViewById(R.id.txtViewInvites);
        TextView myEventsTxt = (TextView)v.findViewById(R.id.txtViewMyEvents);
        TextView goingTxt = (TextView)v.findViewById(R.id.txtViewGoing);
        ImageView imgView = (ImageView)v.findViewById(R.id.imgViewUser);

        if(((PartyHelper) getActivity().getApplication()).isLoggedIn()){

            usernameTxt.setText(getUser.usernames);
            invitesTxt.setText("Invites : "+getUser.invites);
            myEventsTxt.setText("My Events : "+getUser.myEvents);
            goingTxt.setText("People Going To My Events : "+getUser.goings);
            //  imgView.setImageResource(currentEvent.getIconID());
            String u = "http://xpoliem.com/pPics/"+getUser.images;
            Picasso.with(getActivity()).load(u).transform(new CircleTransform()).error(R.drawable.ic_launcher).into(imgView);

        }else{
            usernameTxt.setText("Log in or Register");
            invitesTxt.setText("Get Invites From Parties");
            myEventsTxt.setText("Create Your Own  Events");
            goingTxt.setText("");
            //  imgView.setImageResource(currentEvent.getIconID());
            String u = "http://xpoliem.com/pPics/";
            Picasso.with(getActivity()).load(u).transform(new CircleTransform()).error(R.drawable.ic_launcher).into(imgView);

        }


    }


    class ListAsync extends AsyncTask<Void, Integer, String> {


        protected String doInBackground(Void...arg0) {



            return "You are at PostExecute";
        }



        protected void onPostExecute(String result) {
            getUser.getData(((PartyHelper) getActivity().getApplication()).getID());
            setup();
        }
    }
}
