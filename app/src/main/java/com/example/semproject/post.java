package com.example.semproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
public class post extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String Storage_Path = "images";
    String Database_Path ="allimages";
    Button ChooseButton, UploadButton;
    EditText ImageName,price,menu,grams,desc;
    TextView ingredients,choseningredients,category;
    ImageView SelectImage;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference Reference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    boolean[] selectedingredients;
    ArrayList<Integer> list_ingredients=new ArrayList<>();
    String[] ingredients_array={"eggs","milk","tomatoes","basil","lime","chicken","carrots","potatoes","beef"};
    String[] type={"Select Category","breakfast","brunch","lunch","supper","dinner"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        category=findViewById(R.id.categ);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();
        Reference = FirebaseDatabase.getInstance().getReference(Database_Path);
        ChooseButton = (Button)findViewById(R.id.ButtonChooseImage);
        UploadButton = (Button)findViewById(R.id.ButtonUploadImage);
        ImageName = (EditText)findViewById(R.id.ImageNameEditText);
        Spinner spin = (Spinner) findViewById(R.id.simpleSpinner);
        spin.setOnItemSelectedListener(this);
        grams=findViewById(R.id.quantity);
        desc=findViewById(R.id.description);
        price=findViewById(R.id.prices);
        menu=findViewById(R.id.menus);
        ingredients=findViewById(R.id.ingredients);
        SelectImage = (ImageView)findViewById(R.id.ShowImageView);
        progressDialog = new ProgressDialog(post.this);
        selectedingredients= new boolean[ingredients_array.length];
        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,type);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);

        ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(post.this);
                builder.setTitle("Select ingredients");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(ingredients_array, selectedingredients, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked){
                            list_ingredients.add(which);
                            Collections.sort(list_ingredients);
                        }
                        else{
                            list_ingredients.remove(which);
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder=new StringBuilder();
                        for (int j=0;j<list_ingredients.size();j++){
                            stringBuilder.append(ingredients_array[list_ingredients.get(j)]);
                            if (j !=list_ingredients.size()-1){
                                stringBuilder.append(",");
                            }
                        }
                        ingredients.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int j=0;j<selectedingredients.length;j++){
                            selectedingredients[j]=false;
                            list_ingredients.clear();
                            ingredients.setText("");
                        }
                    }
                });
                builder.show();
            }
        });


        // Adding click listener to Choose image button.
        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

            }
        });


        // Adding click listener to Upload image button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path +
                    System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            // Getting image name from EditText and store into string variable.
                            String savecurrenttime,savecurrentdate;
                            Calendar calfordate=Calendar.getInstance();
                            SimpleDateFormat dateFormat=new SimpleDateFormat("MMM dd, yyyy");
                            savecurrentdate=dateFormat.format(calfordate.getTime());
                            SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
                            savecurrenttime=currenttime.format(calfordate.getTime());
                            String productrandomkey=savecurrentdate+savecurrenttime;
                            String TempImageName = ImageName.getText().toString().trim();
                            String Tempmenu = menu.getText().toString().trim();
                            String Tempprice = price.getText().toString().trim();
                            String Tempgrams = grams.getText().toString().trim();
                            String Tempdesc = desc.getText().toString().trim();
                            String Tempingr = ingredients.getText().toString().trim();
                            String Tempcate = category.getText().toString().trim();
                            String uid=FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid();
                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            Intent inte=new Intent(post.this,MainActivity.class);
                            startActivity(inte);
                            finish();

                            @SuppressWarnings("VisibleForTests")
                            User imageUploadInfo = new User(uid,TempImageName,downloadUrl.toString(),Tempmenu,Tempprice,Tempgrams,Tempdesc,Tempingr,Tempcate);

                            // Getting image upload ID.


                            // Adding image upload id s child element into databaseReference.
                            Reference.child(productrandomkey).setValue(imageUploadInfo);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(post.this,
                                    exception.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(post.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).equals("breakfast")){
            category.setText(parent.getSelectedItem().toString());
        }
        else if (parent.getItemAtPosition(position).equals("brunch")){
            category.setText(parent.getSelectedItem().toString());
        }
        else if (parent.getItemAtPosition(position).equals("lunch")){
            category.setText(parent.getSelectedItem().toString());
        }
        else if (parent.getItemAtPosition(position).equals("supper")){
            category.setText(parent.getSelectedItem().toString());
        }else if(parent.getItemAtPosition(position).equals("dinner")){
            category.setText(parent.getSelectedItem().toString());
        }
        else {
            category.setText(null);
        }
        Toast.makeText(getApplicationContext(), type[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}