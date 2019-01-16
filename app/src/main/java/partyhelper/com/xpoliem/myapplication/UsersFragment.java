package partyhelper.com.xpoliem.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment{
    String my_id ;
    String event_id ;
    View v;
    ListView listView;

    private List<User> users = new ArrayList<User>();

    public final static String TAG = UsersFragment.class.getSimpleName();

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // new UserAsync().execute();



    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_users, container, false);
        ((MainActivity) getActivity()).internetFragTest();
        my_id =  ((PartyHelper) getActivity().getApplication()).getID();
        event_id = ((PartyHelper) getActivity().getApplication()).getEVENT_ID();


                users = ((PartyHelper) getActivity().getApplication()).getUsers();
                populateListView(users);
                registerClickCallback(users);
                Search();







          return v ;
    }
    public void Search(){
        EditText searchValue = (EditText)v.findViewById(R.id.edtSearch);
        searchValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ArrayList<User> temp = new ArrayList<>();
                EditText editSearch = (EditText)v.findViewById(R.id.edtSearch);
                int textlength = editSearch.getText().length();
                temp.clear();
                for (int x = 0; x < users.size()/2; x++){

                    if(users.get(x).getName().toUpperCase().contains(editSearch.getText().toString().toUpperCase())){
                        //  Log.i("users",users.get(x).getName());
                        temp.add(users.get(x));

                    }


                }
                for (int x = 0; x < temp.size()/2; x++){


                    Log.i("users", temp.get(x).getName());

                }


                populateListView(temp);
                registerClickCallback(temp);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    public void populateListView(List<User> ListUsers) {
        ListAdapter userAdapter = new MyListAdapter(ListUsers);
        listView = (ListView)v.findViewById(R.id.listViewUsers);
        listView.setAdapter(userAdapter);


    }


    public void registerClickCallback(final List<User> LISTS) {


        final ListView list = (ListView) v.findViewById(R.id.listViewUsers);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                User clickedUser = LISTS.get(position);

                TextView textViewInvite = ((TextView) viewClicked
                        .findViewById(R.id.txtViewInvite));

                SendInvite u = new SendInvite();
                u.getData(my_id,clickedUser.getId(),event_id);
                textViewInvite.setText(u.invited);



            }
        });


    }


    private class MyListAdapter extends ArrayAdapter<User> {
        List<User> ListUsersAdapter;
        public MyListAdapter(List<User> ListUsers) {
            super(getActivity(), R.layout.user_row, ListUsers);
            ListUsersAdapter = ListUsers;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)

            View itemView = convertView;
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            itemView = vi.inflate(R.layout.user_row, null);
            if (itemView == null) {
                itemView = vi.inflate(R.layout.user_row, parent, false);
            }



            final User currentUser = ListUsersAdapter.get(position);




           /* */

            final ViewHolder holder = new ViewHolder();
            holder.position = getPosition(getItem(position));
            holder.icon = (ImageView)itemView.findViewById(R.id.imgViewUser);
            holder.username = (TextView)itemView.findViewById(R.id.txtUser);
            holder.invited = (TextView)itemView.findViewById(R.id.txtViewInvite);
            holder.gender = (TextView)itemView.findViewById(R.id.txtViewGender);
            itemView.setTag(holder);
            holder.user_id = currentUser.getId();
            holder.username.setText(currentUser.getName());
            holder.gender.setText(currentUser.getGender());
            String u = "http://xpoliem.com/pPics/"+currentUser.getIconID();
            Picasso.with(getActivity()).load(u).transform(new CircleTransform()).error(R.drawable.ic_launcher).into(holder.icon);
            holder.invited.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SendInvite u = new SendInvite();
                    u.getData(my_id,holder.user_id,event_id);
                    holder.invited.setText(u.invited);
                }
            });


            new AsyncTask<ViewHolder, Void, String>() {
                private ViewHolder v;

                @Override
                protected String doInBackground(ViewHolder... params) {
                    v = params[0];

                    CheckInvite Check = new CheckInvite();
                    Check.getData(my_id,currentUser.getId(),event_id);

                    return Check.invited;
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    if (v.position == position) {
                        v.invited.setVisibility(View.VISIBLE);
                        v.invited.setText(result);
                    }
                }
            }.execute(holder);



            return itemView;
        }

        class ViewHolder {
            TextView username;
            TextView gender;
            TextView invited;
            ImageView icon;
            int position;
            String user_id;
        }
    }










}
