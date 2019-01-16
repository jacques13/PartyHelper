package partyhelper.com.xpoliem.myapplication;

/**
 * Created by Jacques on 4/2/2015.
 */
public class Event {
    private String who,id,name,time,date,latitude,longitude,going,iconID;



    public Event(String who, String id, String name, String time, String date, String latitude, String longitude, String going, String iconID){
        super();
        this.who = who;
        this.id = id;
        this.name = name;
        this.time = time;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.going = going;
        this.iconID = iconID;



    }
    public String getIconID() {
        return iconID;
    }
    public String getWho() {
        return who;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getGoing() {
        return going;
    }


}
