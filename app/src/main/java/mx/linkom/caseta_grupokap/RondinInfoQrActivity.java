package mx.linkom.caseta_grupokap;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.URLUtil;
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
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RondinInfoQrActivity extends mx.linkom.caseta_grupokap.Menu {

    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String token = "";
    private String tokenanterior = "";
    mx.linkom.caseta_grupokap.Configuracion Conf;
    JSONArray ja1,ja2,ja3;
    LinearLayout camara,camara2;
    TextView nombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rondines_qr);


        Conf = new mx.linkom.caseta_grupokap.Configuracion(this);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        camara = (LinearLayout) findViewById(R.id.camara);
        camara2 = (LinearLayout) findViewById(R.id.camara2);
        nombre = (TextView) findViewById(R.id.nombre);
        nombre.setText(Conf.getRondinNombre());
        dtl_rondines();

        initQR();

    }

    public void dtl_rondines (){

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_qr_7.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                if (response.trim().equals("error")){

                    camara.setVisibility(View.VISIBLE);
                    camara2.setVisibility(View.GONE);


                }else{
                    response = response.replace("][",",");
                    camara.setVisibility(View.GONE);
                    camara2.setVisibility(View.VISIBLE);

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


    public void initQR() {


        // Creo el detector qr
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        // Creo la camara
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1800, 1124)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        // Listener de ciclo de vida de la camara
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                // Verifico si el usuario dio los permisos para la camara
                if (ActivityCompat.checkSelfPermission(RondinInfoQrActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Verificamos la version de ANdroid que sea al menos la M para mostrar
                        // El dialog de la solicitud de la camara
                        if (shouldShowRequestPermissionRationale(
                                Manifest.permission.CAMERA)) ;
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                    return;
                } else {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException ie) {
                        Log.e("CAMERA SOURCE", ie.getMessage());
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        // Preparo el detector de QR
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() > 0) {

                    // Obtenemos el token
                    token = barcodes.valueAt(0).displayValue.toString();

                    // Verificamos que el token anterior no se igual al actual
                    // Esto es util para evitar multiples llamadas empleando el mismo token
                    if (!token.equals(tokenanterior)) {

                        // Guardamos el ultimo token proceado
                        tokenanterior = token;
                        Log.i("Token", token);

                        if (URLUtil.isValidUrl(token)) {
                            Conf.setQRondines(token);
                            rondin();

                        } else {
                            Conf.setQRondines(token);
                            rondin();

                        }

                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    synchronized (this) {
                                        wait(5000);
                                        // Limpiamos el token
                                        tokenanterior = "";
                                    }
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    Log.e("Error", "Waiting didnt work!!");
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        });
    }






    public void rondin() {

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_qr_5.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja1 = new JSONArray(response);
                        Ubicacion();

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




    public void Ubicacion(){


        try {

            if(Conf.getQRondines().equals(ja1.getString(3))){

                String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_qr_6.php";
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        if(response.equals("error")){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinInfoQrActivity.this);
                            alertDialogBuilder.setTitle("Alerta");
                            alertDialogBuilder
                                    .setMessage("Registro de Asistencia No Exitoso")
                                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.ListaRondinesUbicacionesQrActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }).create().show();
                        }else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinInfoQrActivity.this);
                            alertDialogBuilder.setTitle("Alerta");
                            alertDialogBuilder
                                    .setMessage("Registro de Asistencia Exitoso")
                                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.ListaRondinesUbicacionesQrActivity.class);
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

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }else{

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinInfoQrActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("El qr no corresponde a la ubicación")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.ListaRondinesUbicacionesQrActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.ListaRondinesUbicacionesQrActivity.class);
        startActivity(intent);
        finish();
    }

}
