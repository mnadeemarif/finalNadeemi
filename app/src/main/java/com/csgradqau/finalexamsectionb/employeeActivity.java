package com.csgradqau.finalexamsectionb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.csgradqau.finalexamsectionb.data.MyDividerItemDecoration;
import com.csgradqau.finalexamsectionb.data.RecyclerTouchListener;
import com.csgradqau.finalexamsectionb.data.adapter;
import com.csgradqau.finalexamsectionb.data.product;
import com.csgradqau.finalexamsectionb.data.user;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class employeeActivity extends AppCompatActivity {


    private static final String TAG = "employeeActivity";
    private com.csgradqau.finalexamsectionb.data.adapter adapter;
    private List<product> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    ImageView s;
    FloatingActionButton fab;
    Uri imageUri;
    Bundle data;
    private RequestQueue mRequestQue;
    private  String URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        mRequestQue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recycler_view);
         fab = (FloatingActionButton) findViewById(R.id.fab);
        //userList = db.getAllUsers();
        adapter = new adapter(employeeActivity.this, userList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Toast.makeText(getBaseContext(),"Pressed", Toast.LENGTH_LONG).show();
                product p = userList.get(position);
                //final MaterialAlertDialogBuilder b = new MaterialAlertDialogBuilder(MainActivity.this);
                //b.setTitle("Task Details");
                //b.setView(R.layout.dialog);
                final Dialog dialog = new Dialog(employeeActivity.this); // Context, this, etc.
                dialog.setContentView(R.layout.detail_dialog);
                dialog.setTitle("Product Details ");
                TextView type = (TextView)dialog.findViewById(R.id.type);
                TextView disc = (TextView)dialog.findViewById(R.id.disc);
                TextView quantity = (TextView)dialog.findViewById(R.id.quantity);
                TextView price = (TextView)dialog.findViewById(R.id.price);
                ImageView selfie = (ImageView) dialog.findViewById(R.id.profile);
                //MapView coords = (MapView) dialog.findViewById(R.id.coords);
                type.setText(p.getType().toString());
                disc.setText(p.getDisc().toString());
                quantity.setText(p.getQuantity().toString());
                price.setText(p.getPrice().toString());
                selfie.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
                dialog.show();
                //dialog.show();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        FirebaseDatabase fd = FirebaseDatabase.getInstance();
        DatabaseReference ref = fd.getReference("products");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                product p = new product();
                for (DataSnapshot dt : dataSnapshot.getChildren()){
                    Toast.makeText(employeeActivity.this, "in Loop", Toast.LENGTH_LONG).show();
                    p = (product) dt.getValue(product.class);
                    userList.add(p);
                    //Toast.makeText(getActivity(), "user : " +u.getEmail(), Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProductDialog(false, null, -1);
            }
        });

    }

    private void showProductDialog(final boolean shouldUpdate, final product t, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.add_product, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(employeeActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText type = view.findViewById(R.id.type);
        final EditText quantity = view.findViewById(R.id.quantity);
        final EditText disc = view.findViewById(R.id.discount);
        final EditText  price = view.findViewById(R.id.price);
        final EditText coords = view.findViewById(R.id.coords);
        final ImageView selfie = view.findViewById(R.id.selfie);
        s = selfie;
        selfie.setImageResource(R.drawable.ic_add_a_photo_black_24dp);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
//                        product t = new product("", disc.getText().toString(),type.getText().toString(),quantity.getText().toString(),coords.getText().toString(),price.getText().toString());
//                        //db.addTask(t);
//                        FirebaseDatabase fd = FirebaseDatabase.getInstance();
//                        DatabaseReference dr = fd.getReference("products");
//                        t.setId(dr.push().getKey());
//                       Toast.makeText(employeeActivity.this,"in dialog",Toast.LENGTH_LONG);
//                        dr.child(t.getId()).setValue(t);
                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                product t = new product("", disc.getText().toString(),type.getText().toString(),quantity.getText().toString(),coords.getText().toString(),price.getText().toString());
                //db.addTask(t);
                FirebaseDatabase fd = FirebaseDatabase.getInstance();
                DatabaseReference dr = fd.getReference("products");
                t.setId(dr.push().getKey());
                Toast.makeText(employeeActivity.this,"in dialog",Toast.LENGTH_LONG);
                dr.child(t.getId()).setValue(t);
                FirebaseStorage fs = FirebaseStorage.getInstance();
                StorageReference sr = fs.getReference("customer_self");
                Bitmap bitmap = ((BitmapDrawable) selfie.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = sr.child(t.getId()).putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                    }
                });
                alertDialog.dismiss();


                // Show toast message when no text is entered
//                if (TextUtils.isEmpty(type.getText().toString())||TextUtils.isEmpty(quantity.getText().toString())||TextUtils.isEmpty((coords.getText().toString()))||TextUtils.isEmpty((price.getText().toString()))||TextUtils.isEmpty((disc.getText().toString())) {
//                    Toast.makeText(MainActivity.this, "You left something blank", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    alertDialog.dismiss();
//                }

                // check if user updating note
                //task a = new task(1, inputTitle.getText().toString(),inputDetails.getText().toString(),inputDeadline.getText().toString());
                //addTask(1, inputTitle.getText().toString(),inputDetails.getText().toString(),inputDeadline.getText().toString());
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
                        s.setImageBitmap(selectedImage);
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
                        s.setImageURI(imageUri);
                    }
                    break;
            }
        }
    }

    private void sendNotfication() throws JSONException {
        JSONObject sendObject = new JSONObject();
        sendObject.put("to","/topic/noti");

    }

}