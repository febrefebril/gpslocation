package com.example.febre.gpslocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    private Button btIniciarLeituraGps;
    private Button btGravarPonto;
    private TextView cordenadaAtual;
    private TextView cordenadaSalva;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double Latitude;
    private double Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btIniciarLeituraGps = (Button) findViewById(R.id.btIniciarGps);
        btGravarPonto = (Button) findViewById(R.id.btMarcarPontoDestino);
        cordenadaAtual = (TextView) findViewById(R.id.txtPosicaoAtual);
        cordenadaSalva = (TextView) findViewById(R.id.txtDestinoSalvo);

        btGravarPonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cordenadaSalva.setText("Destino marcado\nLatitude: " + Latitude + " Longitude: " + Longitude);
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Eh chamada sempre que location eh atualizada
                //Toast.makeText(getApplicationContext(),"onLocationChanged", LENGTH_SHORT).show();
                btGravarPonto.setVisibility(View.VISIBLE);
                cordenadaAtual.setText("Posição atual\nLatitude: " + location.getLatitude() + "\n" + "Longitude: " + location.getLongitude() +
                        "\nPrecisão: " + location.getAccuracy() + " m");
                Latitude = location.getLatitude();
                Longitude = location.getLongitude();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Toast.makeText(getApplicationContext(),"onStatuschanged", LENGTH_SHORT).show();
                btGravarPonto.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(),"onProviderEnabled", LENGTH_SHORT).show();

            }

            @Override
            public void onProviderDisabled(String provider) {
                // Eh chamado sempre que o GPS eh desligado
                btGravarPonto.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"onProviderDisabled", LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            }
        } else {
            configureButton();
        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }

    }

    private void configureButton() {
        btIniciarLeituraGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
