package cs121.ucsc.roomie;

/**
 * Created by kevin on 11/9/2017.
 */

public class User {
    public String name;
    public String houseName;
    public String password;
    public String houseAddress;
    public int busy;
    public String userEmail;
    public String msgURL;
    public  String msgID;
    public User(){

    }
    public User(String name, String houseName, String password, String houseAddress, int busy, String userEmail, String msgURL, String msgID){
        this.name = name;
        this.houseName = houseName;
        this.password = password;
        this.houseAddress = houseAddress;
        this.busy = busy;
        this.userEmail = userEmail;
        this.msgURL = msgURL;
        this.msgID = msgID;
    }





}
