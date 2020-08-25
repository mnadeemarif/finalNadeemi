package com.csgradqau.finalexamsectionb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {

    private Button login,register;
    private TextInputLayout email,password;
    private View view;
    //private DatabaseHelper db;
    String f_pass;
    Boolean flag;
    Bundle usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.loginf);
        register = (Button) findViewById(R.id.registerf);
        email = (TextInputLayout) findViewById(R.id.emailL);
        password = (TextInputLayout) findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Clicked !", Toast.LENGTH_LONG).show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dt : dataSnapshot.getChildren()){

                            if(dt.child("username").getValue().toString().equals(email.getEditText().getText().toString()))
                            {
                                f_pass = dt.child("password").getValue().toString().trim();
                                //Toast.makeText(getActivity(), "help : "+f_pass, Toast.LENGTH_LONG).show();
                                if (f_pass.equals(password.getEditText().getText().toString().trim())) {
                                    Toast.makeText(loginActivity.this, "Logged In!!!", Toast.LENGTH_LONG).show();
                                    if(dt.child("type").getValue().toString().equals("employee"))
                                    {
                                        Intent i = new Intent(loginActivity.this, employeeActivity.class);
                                        startActivity(i);
                                    }
                                     else{
                                        Intent i = new Intent(loginActivity.this, adminActivity.class);
                                        startActivity(i);
                                    }
                                }
                                else {
                                    Toast.makeText(loginActivity.this, "Wrong Password !!!", Toast.LENGTH_LONG).show();
                                }

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Query checkuser = myRef.orderByChild("email").equalTo(email.getEditText().getText().toString());

               /* if (db.checkUserExist(email.getEditText().getText().toString()))
                {
                    l = db.getUser(email.getEditText().getText().toString());
                    //Toast.makeText(getActivity(), email.getEditText().getText().toString(), Toast.LENGTH_LONG).show();
                    if (l.getPassword().toString().equals(password.getEditText().getText().toString()))
                    {
                        Toast.makeText(getActivity(), "Logged In!!!", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new homeFragment()).commit();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "User Doesn't Exist Please Register !", Toast.LENGTH_LONG).show();
                }*/
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(getApplicationContext(),registerActivity.class);
                startActivity(reg);
            }
        });
    }
}