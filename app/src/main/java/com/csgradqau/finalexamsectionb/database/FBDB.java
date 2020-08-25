package com.csgradqau.finalexamsectionb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.csgradqau.finalexamsectionb.data.user;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBDB {
    private Context context;
    public FBDB(Context context) {
        this.context = context;
    }
    public void addData(user user, String rootName) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference(rootName);
        String id = reference.push().getKey();
        user.setId(id);
        reference.child(id).setValue(user);
        Toast.makeText(context,"MPD Added Successfully",Toast.LENGTH_LONG).show();
        user.setName("");
        user.setPassword("");
        user.setMarketingSector("");
        user.setType("");
        user.setDoj("");
    }
}
