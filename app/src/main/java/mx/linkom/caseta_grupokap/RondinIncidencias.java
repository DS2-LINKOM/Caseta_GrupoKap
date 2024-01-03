package mx.linkom.caseta_grupokap;


import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import mx.linkom.caseta_grupokap.detectPlaca.DetectarPlaca;
import mx.linkom.caseta_grupokap.offline.Database.UrisContentProvider;
import mx.linkom.caseta_grupokap.offline.Servicios.subirFotos;


public class RondinIncidencias  extends mx.linkom.caseta_grupokap.Menu{

    Button foto1_boton,foto2_boton,foto3_boton;
    int foto;
    ImageView view_foto1,view_foto2,view_foto3;
    Bitmap bitmap,bitmap2,bitmap3;
    Button btnContinuar,btnContinuar2,btnContinuar3,btnContinuar4,btnContinuar5,btnContinuar6;
    LinearLayout Viewfoto1,Viewfoto2,Viewfoto3;
    LinearLayout registrar1,registrar2,registrar3,registrar4;
    LinearLayout foto2,foto3,espacio1,espacio2,espacio3,espacio4,espacio5,espacio6,espacio7;
    String ima1,ima2,ima3;
    ProgressDialog pd,pd2,pd3;
    FirebaseStorage storage;
    StorageReference storageReference;
    mx.linkom.caseta_grupokap.Configuracion Conf;
    EditText Comentarios,Accion;
    Uri uri_img,uri_img2,uri_img3;
    JSONArray ja1;

    String rutaImagen1, rutaImagen2, rutaImagen3, nombreImagen1, nombreImagen2, nombreImagen3;

    private int btnRegistrarPresionado = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rondinincidencias);

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        Conf = new mx.linkom.caseta_grupokap.Configuracion(this);

        Comentarios = (EditText) findViewById(R.id.setComen);
        Accion = (EditText) findViewById(R.id.setAccion);


        foto1_boton = (Button) findViewById(R.id.foto1_boton);
        foto2_boton = (Button) findViewById(R.id.foto2_boton);
        foto3_boton = (Button) findViewById(R.id.foto3_boton);

        registrar1 = (LinearLayout) findViewById(R.id.registrar1);
        registrar2 = (LinearLayout) findViewById(R.id.registrar2);
        registrar3 = (LinearLayout) findViewById(R.id.registrar3);
        registrar4 = (LinearLayout) findViewById(R.id.registrar4);
        espacio1 = (LinearLayout) findViewById(R.id.espacio1);
        espacio2 = (LinearLayout) findViewById(R.id.espacio2);
        espacio3 = (LinearLayout) findViewById(R.id.espacio3);
        espacio4 = (LinearLayout) findViewById(R.id.espacio4);
        espacio5 = (LinearLayout) findViewById(R.id.espacio5);
        espacio6 = (LinearLayout) findViewById(R.id.espacio6);
        espacio7 = (LinearLayout) findViewById(R.id.espacio7);

        Viewfoto1 = (LinearLayout) findViewById(R.id.Viewfoto1);
        view_foto1 = (ImageView) findViewById(R.id.view_foto1);
        Viewfoto2 = (LinearLayout) findViewById(R.id.Viewfoto2);
        view_foto2 = (ImageView) findViewById(R.id.view_foto2);
        Viewfoto3 = (LinearLayout) findViewById(R.id.Viewfoto3);
        view_foto3 = (ImageView) findViewById(R.id.view_foto3);

        btnContinuar = (Button) findViewById(R.id.btnContinuar);
        btnContinuar2 = (Button) findViewById(R.id.btnContinuar2);
        btnContinuar3 = (Button) findViewById(R.id.btnContinuar3);
        btnContinuar4 = (Button) findViewById(R.id.btnContinuar4);
        btnContinuar5 = (Button) findViewById(R.id.btnContinuar5);
        btnContinuar6 = (Button) findViewById(R.id.btnContinuar6);

        foto2 = (LinearLayout) findViewById(R.id.foto2);
        foto3 = (LinearLayout) findViewById(R.id.foto3);


        foto1_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=1;
                imgFoto();
            }
        });


        foto2_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=2;
                imgFoto2();
            }
        });

        foto3_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=3;
                imgFoto3();
            }
        });

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnContinuar.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        btnRegistrarPresionado = 1;
                        botonPresionado(0);
                        Validacion(1);
                    }
                }, 300);
            }
        });

        btnContinuar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=2;
                imgFoto2();
                registrar2.setVisibility(View.GONE);
                foto2.setVisibility(View.VISIBLE);
            }
        });

        btnContinuar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnContinuar3.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btnRegistrarPresionado = 2;
                        botonPresionado(0);
                        Validacion(2);
                    }
                }, 300);
            }
        });

        btnContinuar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=3;
                imgFoto3();
                registrar3.setVisibility(View.GONE);
                foto3.setVisibility(View.VISIBLE);
            }
        });

        btnContinuar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnContinuar5.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btnRegistrarPresionado = 3;
                        botonPresionado(0);
                        Validacion(3);
                    }
                }, 300);
            }
        });

        btnContinuar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnContinuar6.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btnRegistrarPresionado = 4;
                        botonPresionado(0);
                        Validacion(4);
                    }
                }, 300);
            }
        });

        pd= new ProgressDialog(this);
        pd.setMessage("Registrando...");

        pd2= new ProgressDialog(this);
        pd2.setMessage("Subiendo Foto 2...");

        pd3= new ProgressDialog(this);
        pd3.setMessage("Subiendo Foto 3...");
        rondin();
    }

    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (Character.isSpaceChar(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };

    //ALETORIO
    Random primero = new Random();
    int prime= primero.nextInt(9);

    String[] segundo = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandonsegun = (int) Math.round(Math.random() * 25 ) ;

    Random tercero = new Random();
    int tercer= tercero.nextInt(9);

    String[] cuarto = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandoncuart = (int) Math.round(Math.random() * 25 ) ;

    String numero_aletorio=prime+segundo[numRandonsegun]+tercer+cuarto[numRandoncuart];

    //ALETORIO2

    Random primero2 = new Random();
    int prime2= primero2.nextInt(9);

    String[] segundo2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandonsegun2 = (int) Math.round(Math.random() * 25 ) ;

    Random tercero2 = new Random();
    int tercer2= tercero2.nextInt(9);

    String[] cuarto2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandoncuart2 = (int) Math.round(Math.random() * 25 ) ;

    String numero_aletorio2=prime2+segundo2[numRandonsegun2]+tercer2+cuarto2[numRandoncuart2];

    //ALETORIO3

    Random primero3 = new Random();
    int prime3= primero3.nextInt(9);

    String[] segundo3 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandonsegun3 = (int) Math.round(Math.random() * 25 ) ;

    Random tercero3 = new Random();
    int tercer3= tercero3.nextInt(9);

    String[] cuarto3 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandoncuart3 = (int) Math.round(Math.random() * 25 ) ;

    String numero_aletorio3=prime3+segundo3[numRandonsegun3]+tercer3+cuarto3[numRandoncuart3];


    Calendar fecha = Calendar.getInstance();
    int anio = fecha.get(Calendar.YEAR);
    int mes = fecha.get(Calendar.MONTH) + 1;
    int dia = fecha.get(Calendar.DAY_OF_MONTH);


    public void rondin() {
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_2.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja1 = new JSONArray(response);
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
                params.put("id", Conf.getRondin().trim());
                params.put("guardia_de_entrada", Conf.getUsu().trim());
                params.put("id_residencial", Conf.getResid().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    //IMAGEN FOTO

    //FOTOS


    public void imgFoto(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto=null;
            try {
                nombreImagen1 = "app"+numero_aletorio+numero_aletorio3+".png";
                foto= new File(getApplication().getExternalFilesDir(null),nombreImagen1);
                rutaImagen1 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinIncidencias.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {

                uri_img= FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT,uri_img);
                startActivityForResult(intentCaptura, 0);
            }
        }
    }


    public void imgFoto2(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {
            File foto=null;
            try {
                nombreImagen2 = "app"+numero_aletorio2+numero_aletorio+".png";
                foto = new File(getApplication().getExternalFilesDir(null),nombreImagen2);
                rutaImagen2 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinIncidencias.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {
                uri_img2= FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT,uri_img2);
                startActivityForResult( intentCaptura, 1);
            }
        }
    }


    public void imgFoto3(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto=null;
            try {
                nombreImagen3 = "app"+numero_aletorio3+numero_aletorio2+".png";
                foto = new File(getApplication().getExternalFilesDir(null),nombreImagen3);
                rutaImagen3 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinIncidencias.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {
                uri_img3= FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT,uri_img3);
                startActivityForResult( intentCaptura, 2);
            }
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {


                Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen1);

                bitmap = DetectarPlaca.fechaHoraFoto(bitmap);

                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(rutaImagen1);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // compress and save as JPEG
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                registrar1.setVisibility(View.GONE);
                Viewfoto1.setVisibility(View.VISIBLE);
                view_foto1.setVisibility(View.VISIBLE);
                view_foto1.setImageBitmap(bitmap);
                registrar2.setVisibility(View.VISIBLE);
                espacio1.setVisibility(View.VISIBLE);
                espacio2.setVisibility(View.VISIBLE);

            }
            if (requestCode == 1) {


                Bitmap bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen2);

                bitmap2 = DetectarPlaca.fechaHoraFoto(bitmap2);

                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(rutaImagen2);
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fos); // compress and save as JPEG
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Viewfoto2.setVisibility(View.VISIBLE);
                view_foto2.setVisibility(View.VISIBLE);
                view_foto2.setImageBitmap(bitmap2);
                registrar3.setVisibility(View.VISIBLE);
                espacio2.setVisibility(View.GONE);
                espacio3.setVisibility(View.VISIBLE);
                espacio4.setVisibility(View.VISIBLE);



            }

            if (requestCode == 2) {


                Bitmap bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen3);

                bitmap3 = DetectarPlaca.fechaHoraFoto(bitmap3);

                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(rutaImagen3);
                    bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, fos); // compress and save as JPEG
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Viewfoto3.setVisibility(View.VISIBLE);
                view_foto3.setVisibility(View.VISIBLE);
                view_foto3.setImageBitmap(bitmap3);
                registrar4.setVisibility(View.VISIBLE);

                espacio4.setVisibility(View.GONE);
                espacio5.setVisibility(View.VISIBLE);
                espacio6.setVisibility(View.VISIBLE);
                espacio7.setVisibility(View.VISIBLE);

            }
        }
    }


    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public void Validacion(final int Ids) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinIncidencias.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿ Desea registrar la incidencia ?")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        /*btnContinuar.setEnabled(false);
                        btnContinuar3.setEnabled(false);
                        btnContinuar5.setEnabled(false);
                        btnContinuar6.setEnabled(false);*/
                        pd.show();
                        Registrar(Ids);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        botonPresionado(1);

                        /*Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.ReportesActivity.class);
                        startActivity(i);
                        finish();*/
                    }
                }).setCancelable(false).create().show();
    }

    public void Registrar(final int Id){

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_incidencias.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){


                if(response.equals("error")){
                    pd.dismiss();
                    botonPresionado(1);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinIncidencias.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Registro de Incidencia No Exitoso")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.Rondines.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }else {

                    if(Id==1){
                        pd.dismiss();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinIncidencias.this);
                        alertDialogBuilder.setTitle("Alerta");
                        alertDialogBuilder
                                .setMessage("Registro de Incidencia Exitosa")
                                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.Rondines.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }).create().show();

                    }else if(Id==2){
                        ContentValues val_img4 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/incidencias/"+nombreImagen1.trim(), rutaImagen1);
                        Uri uri4 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img4);

                        //upload1();
                        terminar();
                    }else if(Id==3){
                        ContentValues val_img4 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/incidencias/"+nombreImagen1.trim(), rutaImagen1);
                        Uri uri4 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img4);

                        ContentValues val_img5 =  ValuesImagen(nombreImagen2, Conf.getPin()+"/incidencias/"+nombreImagen2.trim(), rutaImagen2);
                        Uri uri5 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img5);
                        //upload1();
                        //upload2();
                        terminar();
                    }else if(Id==4){
                        ContentValues val_img4 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/incidencias/"+nombreImagen1.trim(), rutaImagen1);
                        Uri uri4 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img4);

                        ContentValues val_img5 =  ValuesImagen(nombreImagen2, Conf.getPin()+"/incidencias/"+nombreImagen2.trim(), rutaImagen2);
                        Uri uri5 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img5);

                        ContentValues val_img6 =  ValuesImagen(nombreImagen3, Conf.getPin()+"/incidencias/"+nombreImagen3.trim(), rutaImagen3);
                        Uri uri6 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img6);

/*
                        upload1();
                        upload2();
                        upload3();
*/
                        terminar();
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG","Error: " + error.toString());
                botonPresionado(1);
                alertaErrorAlRegistrar("Error al registrar \n\nNo se ha podido establecer comunicación con el servidor, inténtelo de nuevo");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                if(Id==1){
                    ima1="";
                    ima2="";
                    ima3="";
                }else if(Id==2){
                    ima1="app"+numero_aletorio+numero_aletorio3;
                    ima2="";
                    ima3="";
                }else if(Id==3){
                    ima1="app"+numero_aletorio+numero_aletorio3;
                    ima2="app"+numero_aletorio2+numero_aletorio;
                    ima3="";
                }else if(Id==4){
                    ima1="app"+numero_aletorio+numero_aletorio3;
                    ima2="app"+numero_aletorio2+numero_aletorio;
                    ima3="app"+numero_aletorio3+numero_aletorio2;
                }

                params.put("id_residencial", Conf.getResid().trim());
                params.put("id_usuario", Conf.getUsu().trim());
                params.put("Comentario", Comentarios.getText().toString().trim());
                params.put("Accion", Accion.getText().toString().trim());
                params.put("foto1", ima1);
                params.put("foto2", ima2);
                params.put("foto3", ima3);
                params.put("foto3", ima3);
                try {
                    params.put("id_rondin", ja1.getString(5));
                    params.put("id_ubicacion", ja1.getString(0));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("id_tipo","0");


                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public ContentValues ValuesImagen(String nombre, String rutaFirebase, String rutaDispositivo){
        ContentValues values = new ContentValues();
        values.put("titulo", nombre);
        values.put("direccionFirebase", rutaFirebase);
        values.put("rutaDispositivo", rutaDispositivo);
        return values;
    }

    public void upload1() {
        StorageReference mountainImagesRef = null;
        mountainImagesRef = storageReference.child(Conf.getPin()+"/incidencias/app"+numero_aletorio+numero_aletorio3);

        final UploadTask uploadTask = mountainImagesRef.putFile(uri_img);


        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                pd.show(); // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //System.out.println("Upload is " + progress + "% done");
                // Toast.makeText(getApplicationContext(),"Cargando Imagen INE " + progress + "%", Toast.LENGTH_SHORT).show();

            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(AccesoActivity.this,"Pausado",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(RondinIncidencias.this,"Fallado", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();

            }
        });
    }
    public void upload2() {

        StorageReference mountainImagesRef2 = null;
        mountainImagesRef2 = storageReference.child(Conf.getPin()+"/incidencias/app"+numero_aletorio2+numero_aletorio);

        final UploadTask uploadTask = mountainImagesRef2.putFile(uri_img2);

        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //System.out.println("Upload is " + progress + "% done");
                //Toast.makeText(getApplicationContext(),"Cargando Imagen PLACA " + progress + "%", Toast.LENGTH_SHORT).show();
                pd2.show();
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(AccesoActivity.this,"Pausado",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(RondinIncidencias.this,"Fallado", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd2.dismiss();




            }
        });
    }
    public void upload3() {
        StorageReference mountainImagesRef3 = null;
        mountainImagesRef3 = storageReference.child(Conf.getPin()+"/incidencias/app"+numero_aletorio3+numero_aletorio2);

        final UploadTask uploadTask = mountainImagesRef3.putFile(uri_img3);

        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //System.out.println("Upload is " + progress + "% done");
                //Toast.makeText(getApplicationContext(),"Cargando Imagen PLACA " + progress + "%", Toast.LENGTH_SHORT).show();
                pd3.show();
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(AccesoActivity.this,"Pausado",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(RondinIncidencias.this,"Fallado", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd3.dismiss();

            }
        });
    }

    private void terminar() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinIncidencias.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("Registro de Incidencia Exitosa")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Solo ejecutar si el servicio no se esta ejecutando
                        if (!servicioFotos()) {
                            Intent cargarFotos = new Intent(RondinIncidencias.this, subirFotos.class);
                            startService(cargarFotos);
                        }

                        Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.Rondines.class);
                        startActivity(i);
                        finish();
                    }
                }).create().show();
    }

    //Método para saber si es que el servicio ya se esta ejecutando
    public boolean servicioFotos() {
        //Obtiene los servicios que se estan ejecutando
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //Se recorren todos los servicios obtnidos para saber si el servicio creado ya se esta ejecutando
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (subirFotos.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void botonPresionado(int estado){
        //estado --> 0=presionado   1=restablecer

        Button button = btnContinuar;

        switch (btnRegistrarPresionado){
            case 1:
                button = btnContinuar;
                break;
            case 2:
                button = btnContinuar3;
                break;
            case 3:
                button = btnContinuar5;
                break;
            case 4:
                button = btnContinuar6;
                break;
            default:
                break;
        }

        if (estado == 0){
            button.setBackgroundResource(R.drawable.btn_presionado);
            button.setTextColor(0xFF5A6C81);
        }else if (estado == 1){
            button.setBackgroundResource(R.drawable.ripple_effect);
            button.setTextColor(0xFF27374A);
            button.setEnabled(true);
        }
    }

    public void alertaErrorAlRegistrar(String texto){
        pd.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RondinIncidencias.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage(texto)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create().show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.Rondines.class);
        startActivity(intent);
        finish();
    }

}
