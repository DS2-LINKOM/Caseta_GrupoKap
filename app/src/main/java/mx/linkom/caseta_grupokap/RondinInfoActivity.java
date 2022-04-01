package mx.linkom.caseta_grupokap;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RondinInfoActivity extends mx.linkom.caseta_grupokap.Menu  implements OnMapReadyCallback {
    private mx.linkom.caseta_grupokap.Configuracion Conf;
    private GoogleMap mMap;
    JSONArray ja1,ja2;
    TextView Nombre;
    Button Registrar;
    LinearLayout registrar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rondines);
        Conf = new mx.linkom.caseta_grupokap.Configuracion(this);
        registrar1 = (LinearLayout) findViewById(R.id.registrar1);
        Nombre = (TextView) findViewById(R.id.nombre);
        Registrar = (Button) findViewById(R.id.btnRegistrar);
        dtl_rondines();
        Nombre.setText(Conf.getRondinNombre());
        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Validacion();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            registrar1.setVisibility(View.VISIBLE);

            locationStart();
        }
    }

    public void dtl_rondines (){
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_7.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.e("TAG", "rondines1: " + response);

                    if (response.trim().equals("error")){

                        registrar1.setVisibility(View.VISIBLE);
                        rondin();
                    }else{
                        response = response.replace("][",",");
                        registrar1.setVisibility(View.GONE);
                        rondin();

                    }

            }
     }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_rondin", Conf.getRondin());
                params.put("id_dia", Conf.getDia());
                params.put("id_ubicaciones", Conf.getUbicacion());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void rondin() {
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_5.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja1 = new JSONArray(response);

                        Conf.setUsuLatitud(ja1.getString(4));
                        Conf.setUsuLongitud(ja1.getString(3));
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(RondinInfoActivity.this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ", "Id: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("id", Conf.getUbicacion());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = null;

        sydney = new LatLng( Double.parseDouble(Conf.getUsuLatitud()), Double.parseDouble( Conf.getUsuLongitud()));
        mMap.addMarker(new MarkerOptions().position(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,18 ));
    }

    private void locationStart() {

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        RondinInfoActivity mainActivity;
        public RondinInfoActivity getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(RondinInfoActivity mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {

            loc.getLatitude();
            loc.getLongitude();
            Conf.setUsuLatitud2(String.valueOf(loc.getLatitude()));
            Conf.setUsuLongitud2(String.valueOf(loc.getLongitude()));


            this.mainActivity.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //mensaje1.setText("GPS Desactivado");
            registrar1.setVisibility(View.GONE);

        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
           // mensaje1.setText("GPS Activado");
            registrar1.setVisibility(View.VISIBLE);

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    public void Validacion() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinInfoActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿ Desea realizar el registro ?")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Ubicacion();
                        Registrar.setEnabled(false);

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent(getApplicationContext(), RondinInfoActivity.class);
                        startActivity(i);
                        finish();

                    }
                }).create().show();
    }

    public void Ubicacion(){
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_6.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){


                if(response.equals("error")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinInfoActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Registro de Asistencia No Exitoso")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.ListaRondinesUbicacionesActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinInfoActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Registro de Asistencia Exitoso")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.ListaRondinesUbicacionesActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ", "Id: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("id_rondin", Conf.getRondin());
                params.put("id_dia", Conf.getDia());
                params.put("id_ubicaciones", Conf.getUbicacion());
                params.put("latitud", Conf.getUsuLatitud2());
                params.put("longitud", Conf.getUsuLongitud2());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.ListaRondinesUbicacionesActivity.class);
        startActivity(intent);
        finish();
    }


}
