package cs121.ucsc.roomie;

/**
 * Created by justinseo on 12/5/17.
 */

public class RequestModel {
    public String name;
    public RequestModel(){

    }
    public RequestModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}