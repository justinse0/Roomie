package cs121.ucsc.roomie;

/**
 * Created by Adam on 12/6/2017.
 */

public class OpenChannelStore {
    public boolean channelExists;
    public String houseName;
    public OpenChannelStore(){

    }

    public OpenChannelStore(String houseName){
        this.channelExists = false;
        this.houseName = houseName;
    }


}
