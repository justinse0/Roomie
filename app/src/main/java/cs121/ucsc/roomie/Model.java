package cs121.ucsc.roomie;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
/**
 * Created by kevin on 10/31/2017.
 */

public class Model extends Observable{
    private List<String> accounts;
    private List<String> passwords;
    private List<String> names;

    public Model(){
        accounts = new ArrayList<String>();
        passwords = new ArrayList<String>();
    }
    //search through the list of accounts, see if there's a match, return 1 if match, 0 no match.
    public int SearchForAccount(String acc){
        return 0;
    }
    //search through the list of passwords, see if there's a match, return 1 if match, 0 no match.
    public int SearchForPassword(String pass){
        return 0;
    }
    //add the new account that user entered to the list of accounts
    //add the new password that user entered to the list of passwords
    public void AddNewAccount(String acc, String pass){

    }

    //add the new legal name that user entered to the list of names
    public void AddNewName(String nam){

    }

    //.... follow this pattern when getting new user.


    // if login succeed
    public void ToMainScreen(){

    }
    // if user choose to create new
    public void ToNewUser(){

    }

}
