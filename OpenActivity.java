package com.example.sarvajna.gpstracking;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OpenActivity extends AppCompatActivity {
Button btnTrack,btnInsert;
    TextView tvLatitude,tvLongitude,tvLocation;
    String server_url="http://192.168.0.104/gps_info.php";
    AlertDialog.Builder   builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);


        btnTrack= (Button) findViewById(R.id.btnTrack);
        btnInsert= (Button) findViewById(R.id.btnInsert);
        tvLatitude= (TextView) findViewById(R.id.tvLatitude);
        tvLongitude= (TextView) findViewById(R.id.tvLongitude);
        tvLocation= (TextView) findViewById(R.id.tvLocation);
        builder=new AlertDialog.Builder(OpenActivity.this);



        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(OpenActivity.this,MapsActivity.class);
                startActivity(i);

            }
        });

        //Intent i=getIntent();
        Bundle bundle = getIntent().getExtras();
        final Double lat=bundle.getDouble("lat");
        final Double lon=bundle.getDouble("lon");
        final String location=bundle.getString("location");


        tvLatitude.setText(lat+"");
        tvLongitude.setText(lon+"");
        tvLocation.setText(location);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String dbloc,dblat,dblon;
                dbloc = location;
                dblat=lat.toString();
                dblon=lon.toString();


                StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        builder.setTitle("Server response");
                        builder.setMessage("Response :"+response);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                tvLatitude.setText("");
                                tvLongitude.setText("");
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OpenActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params =new HashMap<String, String>();
                        params.put("dbloc",dbloc);
                        params.put("dblat",dblat);
                        params.put("dblon",dblon);
                        return params;
                    }
                };
                MySingleton.getInstance(OpenActivity.this).addToRequestQueue(stringRequest);
            }
        });
    }
}
