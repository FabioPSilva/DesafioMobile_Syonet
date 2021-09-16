package com.example.desafiomobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.desafiomobile.controller.GetGPS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_CODE = 0;
    GetGPS getGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText txtLatitude = findViewById(R.id.txtLatitude);
        EditText txtLongitude = findViewById(R.id.txtLongitude);
        Button butOpenMap = findViewById(R.id.buttonOpenMap);
        ImageButton butGetAtualLocation = findViewById(R.id.butGetAtualLocation);

        getGPS = new GetGPS(MainActivity.this);

        butGetAtualLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = getGPS.getLatitude();
                double longitude = getGPS.getLongitude();

                txtLatitude.setText(Double.toString(latitude));
                txtLongitude.setText(Double.toString(longitude));

                //Toast.makeText(getApplicationContext(), latitude+","+longitude , Toast.LENGTH_SHORT).show();
            }
        });

        butOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validationCoordinate(txtLatitude.getText().toString(),txtLongitude.getText().toString())) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + txtLatitude.getText().toString() + "," + txtLongitude.getText().toString() + "?z=15"));
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Entre com um valor válido para a Latitude e Longitude", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Solicita a permissão ao iniciar o app
        askPermission();
    }

    public boolean validationCoordinate(String lat, String lon){
        //lat +90 -90 lon +180 -180
        //Validar latitude
        Pattern patLat = Pattern.compile("^-?[0-9]{1,2}(?:\\.[0-9]{1,10})?$");
        //Validar longitude
        Pattern patLon = Pattern.compile("^-?[0-9]{1,3}(?:\\.[0-9]{1,10})?$");
        //System.out.println(patLat.matcher(lat).find()&&patLon.matcher(lon).find());
        return (patLat.matcher(lat).find()&&patLon.matcher(lon).find());
    }

    private void askPermission(){
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this,permissions,PERMISSIONS_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        System.out.println(requestCode);
    }
}