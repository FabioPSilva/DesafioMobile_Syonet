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

        //Instancia a classe GetGPS passando o contexto
        getGPS = new GetGPS(MainActivity.this);

        //Ação do botão de Usar a localização atual
        butGetAtualLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getGPS.checkPermission()) {
                    //Obtem as coordenadas atuais
                    double latitude = getGPS.getLatitude();
                    double longitude = getGPS.getLongitude();

                    //Joga as coordenadas nos campos de texto
                    txtLatitude.setText(Double.toString(latitude));
                    txtLongitude.setText(Double.toString(longitude));
                }else{
                    Toast.makeText(MainActivity.this, "Você deve permitir o uso do GPS pelo aplicativo!", Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(getApplicationContext(), latitude+","+longitude , Toast.LENGTH_SHORT).show();
            }
        });

        //Ação do botão de abrir o mapa com as coordenadas
        butOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verifica a validação dos campos
                if(validationCoordinate(txtLatitude.getText().toString(),txtLongitude.getText().toString())) {
                    //Abre o mapa
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + txtLatitude.getText().toString() + "," + txtLongitude.getText().toString() + "?z=15"));
                    startActivity(intent);
                }else{
                    //Aviso de coordenadas invalidas
                    Toast.makeText(MainActivity.this, "Entre com um valor válido para a Latitude e Longitude", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Solicita a permissão para uso do GPS ao iniciar o app
        askPermission();
    }

    public boolean validationCoordinate(String lat, String lon){
        //Validar latitude
        Pattern patLat = Pattern.compile("^-?[0-9]{1,2}(?:\\.[0-9]{1,10})?$");
        //Validar longitude
        Pattern patLon = Pattern.compile("^-?[0-9]{1,3}(?:\\.[0-9]{1,10})?$");
        //Retorna validação
        return (patLat.matcher(lat).find()&&patLon.matcher(lon).find());
    }

    //Metodo que solicita permissão
    private void askPermission(){
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this,permissions,PERMISSIONS_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        boolean permissionOK = true;
        for(int i=0;i<grantResults.length;i++){
            if(grantResults[i] < 0){
                permissionOK = false;
            }
        }

        if(permissionOK){
            getGPS.getLocation();
        }
    }
}