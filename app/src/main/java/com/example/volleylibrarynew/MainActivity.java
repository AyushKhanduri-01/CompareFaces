package com.example.volleylibrarynew;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  private  TextView t1;
  private EditText url1,url2;
  Button check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1 = findViewById(R.id.result);
        url1=findViewById(R.id.url1);
        url2=findViewById(R.id.url2);
        check = findViewById(R.id.check);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postdata();
            }
        });

    }

    public  void postdata(){

        String url = "https://api-us.faceplusplus.com/facepp/v3/compare";
        String apikey = "AbFP2l7o8-_vC-9a2nqhhASIMMiK3Lks";
        String apiSecret = "1g4zEvczboQRhySWplg-dN5Ac4Rf7odX";
        String image1 = url1.getText().toString();
        String image2 = url2.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    Double confidence = respObj.getDouble("confidence");
                   if(confidence > 70.1){
                   t1.setText("FACES MATCHED");
                   }
                   else{
                       t1.setText("FACES NOT MATCHED");
                   }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                url1.setText("");
                url2.setText("");
            }
        },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", apikey);
                params.put("api_secret", apiSecret);
                params.put("image_url1", image1);
                params.put("image_url2", image2);
                return params;
            }
        };
        queue.add(request);
    }

}