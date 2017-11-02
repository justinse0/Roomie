package cs121.ucsc.roomie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class MessageActivity extends AppCompatActivity {


        public static String text_1 = "";
    private ArrayList<String> messageItems;
    ListView listView;
    public String yearString = new String("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] listString = new String[0];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //create the button, listview, and textfield assignments
        Button sendButton = (Button) findViewById(R.id.sendButton);
        final EditText message = (EditText) findViewById(R.id.messageText);
        messageItems = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.ListView);
        int x = 0;
        this.PopulateList( x, listString);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //open output file
                Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                try {
                    File file = getFileStreamPath("messageFile.txt");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    FileOutputStream messageFile = openFileOutput(file.getName(), Context.MODE_APPEND);
                    BufferedOutputStream writer = new BufferedOutputStream(messageFile);

                    //write data string to file
                    //THIS IS WHERE I NEED THE USER'S NAME (CAPS FOR VISIBILITY)
                    text_1 = "User Name: " + message.getText().toString();
                    String string = text_1;
                    writer.write(string.getBytes());


                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);

                    //open new file
                    writer.flush();
                    writer.close();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

    //populate  the list. I need to be able to refresh it every time somebody sends a message
    private void PopulateList( int counter, String[] strArray ){
        try {
            FileInputStream messageFile = openFileInput("messageFile.txt");
            DataInputStream in = new DataInputStream(messageFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String inputString = "";
            while ((inputString = br.readLine()) != null) {
                messageItems.add(inputString);
                counter += 1;
            }
            strArray = new String[counter];
            for(int i = 0; i<counter; i++){
                strArray[i] = messageItems.get(i);
            }
            messageFile.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
