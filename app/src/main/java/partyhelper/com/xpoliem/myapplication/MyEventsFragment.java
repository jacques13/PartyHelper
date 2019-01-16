package partyhelper.com.xpoliem.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyEventsFragment extends Fragment {
    View v;
    private List<Event> events = new ArrayList<Event>();

    public final static String TAG = MyEventsFragment.class.getSimpleName();
    public static MyEventsFragment newInstance() {
        return new MyEventsFragment();
    }

    public MyEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_events, container, false);
        ((MainActivity) getActivity()).internetFragTest();
       // events.clear();
        if(((PartyHelper) getActivity().getApplication()).isLoggedIn()){

            new Thread(){
                public void run(){
                    events = ((PartyHelper) getActivity().getApplication()).getEvents();

                    if(events.size() == 0){
                        events.add(new Event("","","Create an Event", "","","37.2350","115.8111","","")) ;
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
                    events.add(new Event("","","Log In to See Your Events", "","","37.2350","115.8111","","")) ;
                    populateListView();
                }
            }.start();
        }




        // Inflate the layout for this fragment
        return v;
    }

    public void populateListView() {
        ListAdapter myEventsAdapter = new MyListAdapter();
        ListView listView = (ListView)v.findViewById(R.id.listViewMyEvents);
        listView.setAdapter(myEventsAdapter);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public void registerClickCallback() {
        ListView listView = (ListView)v.findViewById(R.id.listViewMyEvents);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Event clickedCar = events.get(position);


                ((PartyHelper) getActivity().getApplication()).setEVENT_ID(clickedCar.getId());
                ((MainActivity) getActivity()).GoToUsers();






            }
        });


    }

    private class MyListAdapter extends ArrayAdapter<Event> {
        public MyListAdapter() {
            super(getActivity(), R.layout.myevents_row, events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            if (itemView == null) {
                itemView = vi.inflate(R.layout.myevents_row, parent, false);
            }

            // Find the car to work with.
            Event currentEvent = events.get(position);





            ViewHolder holder = new ViewHolder();
            holder.position = getPosition(getItem(position));
            holder.icon = (ImageView)itemView.findViewById(R.id.myEventImg);
            holder.name = (TextView)itemView.findViewById(R.id.myEventnameText);
            holder.time = (TextView)itemView.findViewById(R.id.myEventtimeText);
            holder.date = (TextView)itemView.findViewById(R.id.myEventdateText);
            holder.numberGoing = (TextView)itemView.findViewById(R.id.GoingText);
            holder.address = (TextView)itemView.findViewById(R.id.addressText);
            holder.name.setText(currentEvent.getName());
            holder.time.setText(currentEvent.getTime());
            holder.date.setText(currentEvent.getDate());
            holder.numberGoing.setText("Number Going "+currentEvent.getGoing());
            holder.address.setText(getAddress(currentEvent.getLatitude(),currentEvent.getLongitude()));
            String u = "http://xpoliem.com/ePics/"+currentEvent.getIconID();
            Picasso.with(getActivity()).load(u).transform(new CircleTransform()).error(R.drawable.ic_launcher).into(holder.icon);

            return itemView;
        }
    }



    class ViewHolder {
        TextView name,time,date,numberGoing,address;
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
