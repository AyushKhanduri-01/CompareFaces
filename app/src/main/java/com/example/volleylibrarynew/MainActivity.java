package com.example.volleylibrarynew;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class MainActivity extends AppCompatActivity {
    private TextView t1;

    private ImageView image;
    private Uri urii;
    FirebaseStorage storage;
    StorageReference storageReference;
    Button check;
    private String image1url;
    public  String image2url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.result);
        image = findViewById(R.id.image);
        check = findViewById(R.id.check);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byte bb[] = bytes.toByteArray();
                image.setImageBitmap(photo);
                addtostore(bb);
            }
        }
    }







    public void addtostore(byte bb[]) {
        String path = "123456789";
        StorageReference ref = storageReference.child(path);
        ref.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              //  Toast.makeText(MainActivity.this, "Success saved to firestore", Toast.LENGTH_SHORT).show();


                //----------------------------------------------------------------------------------------------------------------

                // StorageReference ref1 = storageReference.child(path);
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        image1url = uri.toString();
                       // Toast.makeText(MainActivity.this, "get the url", Toast.LENGTH_SHORT).show();
                        postdata();


                        //========================================================================================================
                        //   StorageReference ref2 = storageReference.child(path);

//                ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // File deleted successfully
//
//                        Log.d(TAG, "onSuccess: deleted file");
//                        Toast.makeText(MainActivity.this, "url deleted.......", Toast.LENGTH_SHORT).show();
//                       // t1.setText(image1url);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Uh-oh, an error occurred!
//                        Log.d(TAG, "onFailure: did not delete file");
//                        Toast.makeText(MainActivity.this, "url not deleted......", Toast.LENGTH_SHORT).show();
//                    }
//                });


                //==============================================================================================================================
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                      //  Toast.makeText(MainActivity.this, "not get url", Toast.LENGTH_SHORT).show();
                    }


                });


                //-----------------------------------------------------------------------------------------------------------------

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              //  Toast.makeText(MainActivity.this, "not saved to firebase", Toast.LENGTH_SHORT).show();
            }
        });




    }

    public void postdata() {
        String url = "https://api-us.faceplusplus.com/facepp/v3/compare";
        String apikey = "AbFP2l7o8-_vC-9a2nqhhASIMMiK3Lks";
        String apiSecret = "1g4zEvczboQRhySWplg-dN5Ac4Rf7odX";
      t1.setText(image1url);

        StorageReference ref = storageReference.child("21011013");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                image2url = uri.toString();

               // Toast.makeText(MainActivity.this, "get url2 ", Toast.LENGTH_SHORT).show();





                //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    Double confidence = respObj.getDouble("confidence");
                    if (confidence > 70.1) {
                       // Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        t1.setText("FACES MATCHED");




                        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        StorageReference ref1= storageReference.child("123456789");
                        ref1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully

                        Log.d(TAG, "onSuccess: deleted file");
                     //   Toast.makeText(MainActivity.this, "url deleted.......", Toast.LENGTH_SHORT).show();
                       // t1.setText(image1url);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                      //  Toast.makeText(MainActivity.this, "url not deleted......", Toast.LENGTH_SHORT).show();
                    }
                });

                        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


                    } else {
                        t1.setText("FACES NOT MATCHED");
                     //   Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                    t1.setText(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                t1.setText(error.toString());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", apikey);
                params.put("api_secret", apiSecret);
                params.put("image_url1", image1url);
                params.put("image_url2", image2url);
                return params;
            }
        };
        queue.add(request);


                //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
             //   Toast.makeText(MainActivity.this, "not ger url2", Toast.LENGTH_SHORT).show();
            }
        });
    }
}