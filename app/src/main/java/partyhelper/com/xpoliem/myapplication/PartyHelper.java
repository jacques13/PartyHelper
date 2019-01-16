package partyhelper.com.xpoliem.myapplication;

import android.app.Application;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacques on 6/22/2015.
 */
public class PartyHelper extends Application {

    public String ID = "3";

    public boolean loggedIn = false;

    public String EVENT_ID="1";
    private List<User> users = new ArrayList<User>();


    public boolean isLoggedIn() {
        return loggedIn;
    }



    private List<Event> invites = new ArrayList<Event>();



    private List<Event> events = new ArrayList<Event>();



    public List<User> getUsers() {
        return users;
    }
    public List<Event> getInvites() {
        return invites;
    }
    public List<Event> getEvents() {
        return events;
    }
    public void populateUserList(){
        new Thread(){
            public void run(){
            //    if(hasActiveInternetConnection(getBaseContext())){
                Users m = new Users();
                m.getData();
                // events.add(new Event(who,id,name,time,date,latitude,longitude,going));

                for(int i=0; i<m.usernames.length;i++){
                    users.add(new User(m.ids[i],m.usernames[i],m.images[i],m.genders[i]));
                    //Log.i("Tag", m.usernames[i]);
                }

              //   }else{
                //   users.add(new User("0","No internet connection ","no_picture.png","---"));
                }
           // }
        }.start();



    }
    public void populateInviteList(){
        new Thread(){
            public void run(){
        //if(isNetworkAvailable()){
        Invites m = new Invites();
        m.getData(ID);
        for(int i=0; i<m.names.length;i++){
            invites.add(new Event(m.whos[i],m.ids[i],m.names[i],m.times[i],m.dates[i],m.latitudes[i],m.longitudes[i],m.goings[i],m.images[i]));
        }
        // events.add(new Event(who,id,name,time,date,latitude,longitude,going));
        // if(m.names.length == 0){
        //  events.add(new Event("0","0","no Invites","-","-","46","26","0","no_picture.png"));
        // }else{

        // }



        //}else{
        //    events.add(new Event("0","0","no Invites","-","-","46","26","0","no_picture.png"));
        // }

            }
        }.start();
    }
    public void populateEventList(){
        new Thread(){
            public void run() {
                MyEvents m = new MyEvents();
                m.getData(ID);
                for (int i = 0; i < m.names.length; i++) {
                    events.add(new Event("", m.ids[i], m.names[i], m.times[i], m.dates[i], m.latitudes[i], m.longitudes[i], m.goings[i], m.images[i]));
                }
            }
        }.start();
    }


    public void setEVENT_ID(String EVENT_ID) {
        this.EVENT_ID = EVENT_ID;
    }



    public String getEVENT_ID() {
        return EVENT_ID;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public String GetUsername(){
        GetUser getUser = new GetUser();
        getUser.getData(ID);
        Toast.makeText(this, getUser.usernames
                , Toast.LENGTH_LONG).show();
        return getUser.usernames;
    }
}
