package com.example.desafiomobile.controller;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.desafiomobile.MainActivity;

public class GetGPS extends Service implements LocationListener {
    private final Context ctx;
    boolean isGPSEnabed = false;
    boolean canGetLocation = false;
    boolean permission = false;

    Location location;
    double latitude;
    double longitude;

    private LocationManager locationManager;

    public GetGPS(Context ctx) {
        this.ctx = ctx;
        getLocation();
        Toast.makeText(ctx, (checkPermission()?"Permitido":"Nao permitido"), Toast.LENGTH_SHORT).show();
        //System.out.println((checkPermission()?"Permitido":"Não permitido"));
    }

    //Verifica se já foi dada permissão
    public boolean checkPermission(){
        if(!permission) {
            permission = (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        }
        return permission;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);
            //Obtem o status do GPS
            isGPSEnabed = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isGPSEnabed) {
                if (location == null) {
                    if (checkPermission()) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                            if(locationManager != null){
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if(location != null){
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }else{
                            System.out.println("Permission NO");
                        }
                    }
                }else{
                System.out.println("GPS disabled");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
