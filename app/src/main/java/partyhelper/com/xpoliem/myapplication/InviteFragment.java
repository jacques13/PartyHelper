package partyhelper.com.xpoliem.myapplication;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class InviteFragment extends Fragment {

    View v;
    private List<Event> invites = new ArrayList<Event>();
    private AdView adView;
    private static final String AD_UNIT_ID = "ca-app-pub-4510811185421782/2633630150";
    private String DEVICE_ID  = "";



    public final static String TAG = InviteFragment.class.getSimpleName();
    public static InviteFragment newInstance() {
        return new InviteFragment();
    }

    public InviteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_invite, container, false);
        //invites.clear();
        ((MainActivity) getActivity()).internetFragTest();
        DEVICE_ID = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        adView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(DEVICE_ID).build();
        adView.loadAd(adRequest);


        if(((PartyHelper) getActivity().getApplication()).isLoggedIn()){
            new Thread(){
                public void run(){
                    invites = ((PartyHelper) getActivity().getApplication()).getInvites();
                    if(invites.size() == 0){
                        invites.add(new Event("","","No Invites Yet", "","","37.2350","115.8111","","")) ;
                        populateListView();

                    }else{
                        populateListView();
                        registerClickCallback();
                    }

                }
            }.start();

        }else{
            new Thread(){
                public void run(){
                    invites.add(new Event("","","Log In to Get Invites", "","","37.2350","115.8111","","")) ;
                    populateListView();
                }
            }.start();
        }







        // Inflate the layout for this fragment
        return v;
    }
    public void populateListView() {
        ListAdapter myEventsAdapter = new MyListAdapter();
        ListView listView = (ListView)v.findViewById(R.id.listViewInvites);
        listView.setLongClickable(true);
        listView.setClickable(true);
        listView.setAdapter(myEventsAdapter);
    }


    public void registerClickCallback() {
        ListView listView = (ListView)v.findViewById(R.id.listViewInvites);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event clickedCar = invites.get(i);
                Double lat = Double.valueOf(clickedCar.getLatitude());
                Double lon = Double.valueOf(clickedCar.getLongitude());
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f",lat,lon);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
                return true;
            }
        });


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    private class MyListAdapter extends ArrayAdapter<Event> {
        public MyListAdapter() {
            super(getActivity(), R.layout.invite_row, invites);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            if (itemView == null) {
                itemView = vi.inflate(R.layout.invite_row, parent, false);
            }

            // Find the car to work with.
            final Event currentEvent = invites.get(position);






            final ViewHolder holder = new ViewHolder();

            holder.position = getPosition(getItem(position));
            holder.icon = (ImageView)itemView.findViewById(R.id.imgViewInvite);
            holder.name = (TextView)itemView.findViewById(R.id.InvitenameText);
            holder.date = (TextView)itemView.findViewById(R.id.InvitedateText);
            holder.time = (TextView)itemView.findViewById(R.id.InvitetimeText);
            holder.going = (TextView)itemView.findViewById(R.id.goingText);
            holder.numberGoing = (TextView)itemView.findViewById(R.id.numberGoingText);
            holder.whoInvitedMe = (TextView)itemView.findViewById(R.id.whoInvitedMeText);
            holder.address = (TextView)itemView.findViewById(R.id.addressText);
            itemView.setTag(holder);
            holder.name.setText(currentEvent.getName());
            holder.time.setText(currentEvent.getTime());
            holder.date.setText(currentEvent.getDate());
            holder.address.setText("Address");
            holder.numberGoing.setText("Number Going "+currentEvent.getGoing());
            holder.whoInvitedMe.setText(currentEvent.getWho()+" Invited You");
            holder.address.setText("");
            String u = "http://xpoliem.com/ePics/"+currentEvent.getIconID();
            Picasso.with(getActivity()).load(u).transform(new CircleTransform()).error(R.drawable.ic_launcher).into(holder.icon);

            holder.going.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Event clickedCar = invites.get(position);
                    Going u = new Going();
                    u.getData(((PartyHelper) getActivity().getApplication()).getID(),clickedCar.getId());
                    holder.going.setText(u.going);
                }
            });


            new AsyncTask<ViewHolder, Void, String>() {
                private ViewHolder v;

                @Override
                protected String doInBackground(ViewHolder... params) {
                    v = params[0];
                    if(!currentEvent.getId().contains("")){
                        CheckGoing Check = new CheckGoing();
                        Check.getData(((PartyHelper) getActivity().getApplication()).getID(),currentEvent.getId());
                        return Check.going;
                    }
                    return "----";

                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    if (v.position == position) {
                        v.going.setVisibility(View.VISIBLE);
                        v.going.setText(result);
                        v.address.setText(getAddress(currentEvent.getLatitude(),currentEvent.getLongitude()));
                    }
                }
            }.execute(holder);

            return itemView;
        }
    }

    class ViewHolder {
        TextView name,time,date,going,numberGoing,address,whoInvitedMe;
        ImageView icon;
        int position;
    }

    public String  getAddress(String lat,String lon){
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        return address;

    }

}
