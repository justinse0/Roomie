package cs121.ucsc.roomie;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by justinseo on 12/5/17.
 */

public class ToDoCustomAdapter extends BaseAdapter {
    Context context;
    static ArrayList<ToDoModel> itemModelList;
    FirebaseAuth mAuth;
    DatabaseReference database;
    public ToDoCustomAdapter(Context context, ArrayList<ToDoModel> modelList) {
        this.context = context;
        this.itemModelList = modelList;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getCount() {
        return itemModelList.size();
    }
    @Override
    public Object getItem(int position) {
        return itemModelList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.todoitem, null);
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            ImageView imgRemove = (ImageView) convertView.findViewById(R.id.imgRemove);
            ToDoModel m = (ToDoModel) itemModelList.get(position);
            tvName.setText(m.getName());
            // click listener for remove button
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemModelList.remove(position);
                    database.child("ToDoData").child(MainActivity.currUser.houseName).setValue(itemModelList);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }
}
