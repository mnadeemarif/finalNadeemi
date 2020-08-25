package com.csgradqau.finalexamsectionb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.csgradqau.finalexamsectionb.data.IResult;
import com.csgradqau.finalexamsectionb.data.Utils;
import com.csgradqau.finalexamsectionb.data.VolleyService;
import com.csgradqau.finalexamsectionb.data.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class registerActivity extends AppCompatActivity {
    private VolleyService mVolleyService;
    private IResult mResultCallback = null;

    Button register;
    EditText name,username,password,dob;
    TextView btmLogin;
    RadioGroup gender;
    RadioGroup type;
    ImageView profile;
    AutoCompleteTextView sect;
    DatePickerDialog picker;
    private user a;
    private static final int PICK_IMAGE = 100;
   // private DatabaseHelper db;
    Uri imageUri;
    FirebaseDatabase f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profile = (ImageView) findViewById(R.id.profilePicture);
        register = (Button) findViewById(R.id.register);
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.usernameT);
        password = (EditText) findViewById(R.id.password);
        dob = (EditText) findViewById(R.id.dob);
        gender = (RadioGroup) findViewById(R.id.genderGroup);
        sect  = findViewById(R.id.sect);
        type = (RadioGroup) findViewById(R.id.userType);
        btmLogin = findViewById(R.id.btmLogin);

        final String[] sectors = new String[] {"I8", "I10", "BlueArea", "F10", "F7", "I9", "H8", "H10", "H12", "Bahria", "Barakaho"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, sectors);
        sect.setAdapter(adapter);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(registerActivity.this);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = new user();
                f = FirebaseDatabase.getInstance();
                DatabaseReference myRef = f.getReference("users");
                FirebaseStorage fbStorage = FirebaseStorage.getInstance();
                StorageReference myStoreRef = fbStorage.getReference("selfie");
                String u_id = myRef.push().getKey();
                String _email = username.getText().toString().trim();
                a.setId(u_id);
                a.setUsername(username.getText().toString());
                a.setPassword(password.getText().toString());
                a.setName(name.getText().toString());
                a.setDoj(dob.getText().toString());
                int s = gender.getCheckedRadioButtonId();
                if (s == R.id.male)
                    a.setGender("Male");
                else
                    a.setGender("Female");

                int sm = type.getCheckedRadioButtonId();
                if (sm == R.id.admin)
                    a.setType("admin");
                else
                    a.setType("employee");


                a.setMarketingSector(sect.getText().toString());
                try {
                    //a.setProfile(imageViewToByte(profile));
                    //profile.setDrawingCacheEnabled(true);
                    //profile.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    assert u_id != null;
                    UploadTask uploadTask = myStoreRef.child(u_id).putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            Toast.makeText(registerActivity.this,"Picture uploaded",Toast.LENGTH_LONG);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

                assert u_id != null;
                myRef.child(u_id).setValue(a);

                if (!u_id.equals("") )
                {
                    Toast.makeText(registerActivity.this, "User Registered !", Toast.LENGTH_LONG).show();
                    addNewNote(a.getId(),a.getId(),a.getName(),a.getUsername(),a.getPassword(),a.getMarketingSector(),a.getGender(),a.getType(),a.getDoj());
                }

                if(a.getType().equals("admin"))

                {
                    FirebaseMessaging.getInstance().subscribeToTopic("admin")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            private static final String TAG = "";

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "successful";
                                if (!task.isSuccessful()) {
                                    msg = "unsuccessful";
                                }
                                Log.d(TAG, msg);
                                Toast.makeText(registerActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                }

                //long id = db.registerUser(a.getEmail(),a.getPassword(),a.getName(),a.getDob(),a.getGender(),a.getHobbies(),a.getProfile());

            }
        });

        btmLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(registerActivity.this,loginActivity.class);
               startActivity(i);
            }
        });

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("SELECT A DATE");
        final MaterialDatePicker mdp = builder.build();
        dob.setKeyListener(null);
        mdp.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dob.setText(mdp.getHeaderText());
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdp.show(getSupportFragmentManager(),"DOB_PICKER");
            }
        });



    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profile.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    /*if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }*/
                    if (resultCode == RESULT_OK) {
                        imageUri = data.getData();
                        profile.setImageURI(imageUri);
                    }
                    break;
            }
        }
    }

    private byte [] imageViewToByte(ImageView img) throws IOException {
        //Bitmap bmp = ((BitmapDrawable)img.getDrawable()).getBitmap();
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
        ByteArrayOutputStream op =  new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,op);
        byte [] byteArray = op.toByteArray();
        return byteArray;

    }

    private void addNewNote(String id,String img,String name,String username,String password,String sector, String gender, String type, String dob) {
//        Utils.isOnline(registerActivity.this)
        Toast.makeText(registerActivity.this, "in new note ",Toast.LENGTH_LONG).show();
        if (true) {
            //Log.d(TAG,"AddNewNote()");
            mResultCallback = new IResult() {
                private static final String TAG = "";

                @Override
                public void notifySuccess(String requestType, String response) {
                    Log.d(TAG, "Volley JSON post" + response);
                    try {
                        if(requestType.equalsIgnoreCase("add_note")){

                            JSONArray responseArray=new JSONArray(response);
                            //Log.d(TAG,responseArray.toString());
                            if (responseArray.length() > 0) {
                                for (int i = 0; i < responseArray.length(); i++) {
                                    JSONObject responseObject=responseArray.getJSONObject(i);
                                    user u = new user();
                                    u.setId(responseObject.get("id").toString());
                                    u.setImage(responseObject.get("image").toString());
                                    u.setUsername(responseObject.get("username").toString());
                                    u.setName(responseObject.get("username").toString());
                                    u.setPassword(responseObject.get("password").toString());
                                    u.setType(responseObject.get("userType").toString());
                                    u.setGender(responseObject.get("marketsector").toString());
                                    u.setDoj(responseObject.get("doj").toString());
                                }

                            } else {
                                Toast.makeText(registerActivity.this, "No Users Found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (Exception je) {
                        //Log.e(TAG, je.toString());
                    }
                }

                @Override
                public void notifyError(String requestType, VolleyError error) {
//                    Log.d(TAG, "Volley requester " + requestType);
//                    Log.d(TAG, "Volley JSON post " + " That didn't work!");
//                    Log.e(TAG, error.toString());
                }
            };
            //mResultCallback = new MainActivity();
            mVolleyService = new VolleyService(mResultCallback, registerActivity.this);

            JSONObject sendObj = new JSONObject();
            try {
                sendObj.put("ACTION", "add_note");
                sendObj.put("name",name);
                sendObj.put("username", username);
                sendObj.put("password", password);
                //sendObj.put("id",id);
                sendObj.put("image",img+" "+name);
                sendObj.put("usertype", type);
                sendObj.put("marketsector", sector);
                sendObj.put("doj", dob);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mVolleyService.postDataVolley("add_note", Utils.SERVER_HOME_URL, sendObj);

        } else {
            Toast.makeText(registerActivity.this," Network Error" , Toast.LENGTH_SHORT).show();
        }
    }

}