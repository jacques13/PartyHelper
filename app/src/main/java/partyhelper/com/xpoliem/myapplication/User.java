package partyhelper.com.xpoliem.myapplication;

/**
 * Created by Jacques on 4/2/2015.
 */
public class User {
    private String id;
    private String name;
    private String iconID;

    public String getGender() {
        return gender;
    }

    private String gender;


    public User(String id, String name, String iconID,String gender) {
        this.id = id;
        this.name = name;
        this.iconID = iconID;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconID() {
        return iconID;
    }
}
