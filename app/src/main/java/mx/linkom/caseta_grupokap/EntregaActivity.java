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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import mx.linkom.caseta_grupokap.offline.Database.UrisContentProvider;
import mx.linkom.caseta_grupokap.offline.Global_info;
import mx.linkom.caseta_grupokap.offline.Servicios.subirFotos;

public class EntregaActivity extends mx.linkom.caseta_grupokap.Menu {

    TextView setNumero,setComent,setPara;
    ImageView foto_recep,viewFoto;
    Button foto,btnRegistrar;
    LinearLayout View,espacio,espacio2,BtnReg,rlVista,rlPermitido;
    ProgressDialog pd;
    JSONArray ja1,ja2;
    private Configuracion Conf;
    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap;
    String fotos;
    Uri uri_img;

    TextView txtFoto;
    String rutaImagen1, nombreImagen1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega);

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        Conf = new Configuracion(this);

        setPara = (TextView) findViewById(R.id.setPara);
        setNumero = (TextView) findViewById(R.id.setNumero);
        setComent = (TextView) findViewById(R.id.setComent);
        foto_recep = (ImageView) findViewById(R.id.foto_recep);
        viewFoto = (ImageView) findViewById(R.id.viewFoto);
        foto = (Button) findViewById(R.id.foto);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        rlVista = (LinearLayout) findViewById(R.id.rlVista);
        rlPermitido = (LinearLayout) findViewById(R.id.rlPermitido);
        View = (LinearLayout) findViewById(R.id.View);
        espacio = (LinearLayout) findViewById(R.id.espacio);
        espacio2 = (LinearLayout) findViewById(R.id.espacio2);
        BtnReg = (LinearLayout) findViewById(R.id.BtnReg);

        txtFoto = (TextView) findViewById(R.id.txtFotoEntrega);

        txtFoto.setText(Global_info.getTexto1Imagenes());

        pd= new ProgressDialog(this);
        pd.setMessage("Registrando...");

        check();

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFoto();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion();
            }});

    }




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


    public void check() {
        String url = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/correspondencia_5.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        ja1 = new JSONArray(response);
                        check2();
                    } catch (JSONException e) {

                    }
                }


            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG","Error: " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Folio", Conf.getPlacas());
                params.put("id_residencial", Conf.getResid().trim());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    public void check2() {
        String url = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/correspondencia_6.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        ja2 = new JSONArray(response);
                        ValidarQR();
                    } catch (JSONException e) {

                    }
                }


            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG","Error: " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                try {
                    params.put("id_usuario", ja1.getString(2));
                    params.put("id_residencial", Conf.getResid().trim());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    public void ValidarQR(){
        try {
            rlVista.setVisibility(View.GONE);
            rlPermitido.setVisibility(View.VISIBLE);

            setNumero.setText(ja1.getString(0));

            setPara.setText(ja2.getString(0));
            setComent.setText(ja1.getString(6));

           storageReference.child(Conf.getPin()+"/correspondencia/"+ja1.getString(7))
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                @Override

                public void onSuccess(Uri uri) {
                    Glide.with(EntregaActivity.this)
                            .load(uri)
                            .error(R.drawable.log)
                            .centerInside()
                            .into(foto_recep);

                    txtFoto.setVisibility(android.view.View.GONE);
                    foto_recep.setVisibility(android.view.View.VISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("TAG","Error123: " + exception);

                    txtFoto.setText(Global_info.getTexto2Imagenes());
                }
            });






        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //IMAGEN FOTO

    public void imgFoto(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto=null;
            try {
                nombreImagen1 = "app"+ja1.getString(2)+"-"+numero_aletorio+".png";
                foto= new File(getApplication().getExternalFilesDir(null),nombreImagen1);
                rutaImagen1 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {


                Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen1);

                View.setVisibility(android.view.View.VISIBLE);
                viewFoto.setVisibility(android.view.View.VISIBLE);
                viewFoto.setImageBitmap(bitmap);
                espacio.setVisibility(android.view.View.VISIBLE);
                espacio2.setVisibility(android.view.View.VISIBLE);
                BtnReg.setVisibility(android.view.View.VISIBLE);
                btnRegistrar.setVisibility(android.view.View.VISIBLE);

            }
        }
    }


    public void Validacion() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿ Desea Entregar Paquete ?")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        pd.show();
                        Registrar();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent(getApplicationContext(), CorrespondenciaActivity.class);
                        startActivity(i);
                        finish();
                    }
                }).create().show();
    }


    public void Registrar(){

        String url = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/correspondencia_7.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){


                if(response.equals("error")){
                    pd.dismiss();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Entrega No Exitosa")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }else {

                    //Registrar fotos en SQLite
                    ContentValues val_img1 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/correspondencia/"+nombreImagen1.trim(), rutaImagen1);
                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                    pd.dismiss();
                    terminar();
                        //upload1();

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.e("TAG","Error: " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                try {
                    fotos="app"+ja1.getString(2)+"-"+numero_aletorio+".png";
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Map<String, String> params = new HashMap<>();
                params.put("Folio",Conf.getPlacas());
                params.put("Foto",fotos );
                params.put("id_residencial", Conf.getResid().trim());

                try {
                    params.put("token", ja2.getString(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("nom_residencial",Conf.getNomResi().trim());

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


    public void upload1(){

        StorageReference mountainImagesRef = null;
        try {
            mountainImagesRef =storageReference.child(Conf.getPin()+"/correspondencia/app"+ja1.getString(2)+"-"+numero_aletorio+".png");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = mountainImagesRef.putFile(uri_img);


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
                Toast.makeText(EntregaActivity.this,"Fallado", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                terminar();
            }
        });
    }
    public void terminar() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("Entrega Exitosa")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Solo ejecutar si el servicio no se esta ejecutando
                        if (!servicioFotos()){
                            Intent cargarFotos = new Intent(EntregaActivity.this, subirFotos.class);
                            startService(cargarFotos);
                        }

                        Intent i = new Intent(getApplicationContext(), CorrespondenciaActivity.class);
                        startActivity(i);
                        finish();
                    }
                }).create().show();
    }


    //Método para saber si es que el servicio ya se esta ejecutando
    public boolean servicioFotos(){
        //Obtiene los servicios que se estan ejecutando
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //Se recorren todos los servicios obtnidos para saber si el servicio creado ya se esta ejecutando
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(subirFotos.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), CorrespondenciaActivity.class);
        startActivity(intent);
        finish();

    }

}
