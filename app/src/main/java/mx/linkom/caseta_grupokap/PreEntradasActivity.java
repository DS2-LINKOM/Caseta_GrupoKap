package mx.linkom.caseta_grupokap;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import mx.linkom.caseta_grupokap.offline.Database.UrisContentProvider;
import mx.linkom.caseta_grupokap.offline.Global_info;
import mx.linkom.caseta_grupokap.offline.Servicios.subirFotos;

public class PreEntradasActivity extends mx.linkom.caseta_grupokap.Menu {
    private mx.linkom.caseta_grupokap.Configuracion Conf;
    private RadioGroup rdgGrupo;
    RadioButton visi, prove, taxi;
    String valor;
    JSONArray ja1, ja2, ja3, ja4, ja5, ja6;
    EditText Nombre, Placas;
    Spinner Calle, Pasajeros, Numero;
    ArrayList<String> calles, names, numero;
    LinearLayout Numero_o;


    String f1, f2, f3;
    LinearLayout espacio1, espacio2, espacio3, espacio4, espacio5, espacio6, espacio7, espacio8, espacio9, espacio10;
    LinearLayout registrar1, registrar2, registrar3, registrar4;
    Button reg1, reg2, reg3, reg4, btn_foto1, btn_foto2, btn_foto3;
    LinearLayout Foto1View, Foto2View, Foto3View;
    LinearLayout Foto1, Foto2, Foto3;
    ImageView view1, view2, view3;
    TextView nombre_foto1, nombre_foto2, nombre_foto3;

    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap, bitmap2, bitmap3;
    ProgressDialog pd, pd2, pd3;
    int foto;
    int fotos1, fotos2, fotos3;
    Uri uri_img, uri_img2, uri_img3;
    EditText Comentarios;

    ImageView iconoInternet;
    boolean Offline = false;

    String rutaImagen1, rutaImagen2, rutaImagen3, rutaImagenPlaca = "", nombreImagen1, nombreImagen2, nombreImagen3, nombreImagenPlaca = "";
    TextView txtFoto1, txtFoto2, txtFoto3;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preentradas);
        Conf = new mx.linkom.caseta_grupokap.Configuracion(this);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Comentarios = (EditText) findViewById(R.id.setComentarios);

        reg1 = (Button) findViewById(R.id.reg1);
        reg2 = (Button) findViewById(R.id.reg2);
        reg3 = (Button) findViewById(R.id.reg3);
        reg4 = (Button) findViewById(R.id.reg4);
        btn_foto1 = (Button) findViewById(R.id.btn_foto1);
        btn_foto2 = (Button) findViewById(R.id.btn_foto2);
        btn_foto3 = (Button) findViewById(R.id.btn_foto3);

        nombre_foto1 = (TextView) findViewById(R.id.nombre_foto1);
        nombre_foto2 = (TextView) findViewById(R.id.nombre_foto2);
        nombre_foto3 = (TextView) findViewById(R.id.nombre_foto3);

        view1 = (ImageView) findViewById(R.id.view1);
        view2 = (ImageView) findViewById(R.id.view2);
        view3 = (ImageView) findViewById(R.id.view3);

        txtFoto1 = (TextView) findViewById(R.id.txtFotoPreEntradas1);
        txtFoto2 = (TextView) findViewById(R.id.txtFotoPreEntradas2);
        txtFoto3 = (TextView) findViewById(R.id.txtFotoPreEntradas3);

        txtFoto1.setText(Global_info.getTexto1Imagenes());
        txtFoto2.setText(Global_info.getTexto1Imagenes());
        txtFoto3.setText(Global_info.getTexto1Imagenes());

        espacio1 = (LinearLayout) findViewById(R.id.espacio1);
        espacio2 = (LinearLayout) findViewById(R.id.espacio2);
        espacio3 = (LinearLayout) findViewById(R.id.espacio3);
        espacio4 = (LinearLayout) findViewById(R.id.espacio4);
        espacio5 = (LinearLayout) findViewById(R.id.espacio5);
        espacio6 = (LinearLayout) findViewById(R.id.espacio6);
        espacio7 = (LinearLayout) findViewById(R.id.espacio7);
        espacio8 = (LinearLayout) findViewById(R.id.espacio8);
        espacio9 = (LinearLayout) findViewById(R.id.espacio9);
        espacio10 = (LinearLayout) findViewById(R.id.espacio10);
        registrar1 = (LinearLayout) findViewById(R.id.registrar1);
        registrar2 = (LinearLayout) findViewById(R.id.registrar2);
        registrar3 = (LinearLayout) findViewById(R.id.registrar3);
        registrar4 = (LinearLayout) findViewById(R.id.registrar4);
        Foto1View = (LinearLayout) findViewById(R.id.Foto1View);
        Foto2View = (LinearLayout) findViewById(R.id.Foto2View);
        Foto3View = (LinearLayout) findViewById(R.id.Foto3View);
        Foto1 = (LinearLayout) findViewById(R.id.Foto1);
        Foto2 = (LinearLayout) findViewById(R.id.Foto2);
        Foto3 = (LinearLayout) findViewById(R.id.Foto3);

        Conf = new mx.linkom.caseta_grupokap.Configuracion(this);
        calles = new ArrayList<String>();
        names = new ArrayList<String>();
        numero = new ArrayList<String>();

        rdgGrupo = (RadioGroup) findViewById(R.id.rdgGrupo);
        visi = (RadioButton) findViewById(R.id.Visita);
        prove = (RadioButton) findViewById(R.id.Proveedor);
        taxi = (RadioButton) findViewById(R.id.Taxista);
        Nombre = (EditText) findViewById(R.id.setNombre);
        Numero = (Spinner) findViewById(R.id.setNumero);
        Numero_o = (LinearLayout) findViewById(R.id.numero);
        Numero_o.setVisibility(View.GONE);
        Placas = (EditText) findViewById(R.id.setPlacas);


        Calle = (Spinner) findViewById(R.id.setCalle);
        Pasajeros = (Spinner) findViewById(R.id.setPasajeros);

        iconoInternet = (ImageView) findViewById(R.id.iconoInternetPreentradas);

        if (Global_info.getINTERNET().equals("Si")) {
            iconoInternet.setImageResource(R.drawable.ic_online);
            Offline = false;
        } else {
            iconoInternet.setImageResource(R.drawable.ic_offline);
            Offline = true;
        }

        iconoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Offline) {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PreEntradasActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOffline())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                } else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PreEntradasActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOnline())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }
            }
        });

        cargarSpinner2();
        if (Offline) {
            callesOffline();
            menuOffline();
        } else {
            calles();
            menu();
        }
        // Visita();

        pd = new ProgressDialog(this);
        pd.setMessage("Registrando...");

        pd2 = new ProgressDialog(this);
        pd2.setMessage("Subiendo Imagen 2...");

        pd3 = new ProgressDialog(this);
        pd3.setMessage("Subiendo Imagen 3...");


        reg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verificar();
            }
        });

        reg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verificar();
            }
        });

        reg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verificar();
            }
        });

        reg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verificar();
            }
        });

        btn_foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 1;
                imgFotoOffline();
                /*if (Offline){
                    imgFotoOffline();
                }else {
                    imgFoto();
                }*/
            }
        });

        btn_foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 2;
                imgFoto2Offline();
                /*if (Offline){
                    imgFoto2Offline();
                }else {
                    imgFoto2();
                }*/
            }
        });

        btn_foto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 3;
                imgFoto3Offline();
                /*if (Offline){
                    imgFoto3Offline();
                }else {
                    imgFoto3();
                }*/
            }
        });
        Placas.setText(Conf.getPlacas().trim());

        Numero_o.setVisibility(View.VISIBLE);
        cargarSpinner4();
    }

    //ALETORIO
    Random primero = new Random();
    int prime = primero.nextInt(9);

    String[] segundo = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    int numRandonsegun = (int) Math.round(Math.random() * 25);

    Random tercero = new Random();
    int tercer = tercero.nextInt(9);

    String[] cuarto = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    int numRandoncuart = (int) Math.round(Math.random() * 25);

    String numero_aletorio = prime + segundo[numRandonsegun] + tercer + cuarto[numRandoncuart];


    //ALETORIO2

    Random primero2 = new Random();
    int prime2 = primero2.nextInt(9);

    String[] segundo2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    int numRandonsegun2 = (int) Math.round(Math.random() * 25);

    Random tercero2 = new Random();
    int tercer2 = tercero2.nextInt(9);

    String[] cuarto2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    int numRandoncuart2 = (int) Math.round(Math.random() * 25);

    String numero_aletorio2 = prime2 + segundo2[numRandonsegun2] + tercer2;


    //ALETORIO3

    Random primero3 = new Random();
    int prime3 = primero3.nextInt(9);

    String[] segundo3 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    int numRandonsegun3 = (int) Math.round(Math.random() * 25);

    Random tercero3 = new Random();
    int tercer3 = tercero3.nextInt(9);

    String[] cuarto3 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    int numRandoncuart3 = (int) Math.round(Math.random() * 25);

    String numero_aletorio3 = prime3 + segundo3[numRandonsegun3] + tercer3 + cuarto3[numRandoncuart3];

    Calendar fecha = Calendar.getInstance();
    int anio = fecha.get(Calendar.YEAR);
    int mes = fecha.get(Calendar.MONTH) + 1;
    int dia = fecha.get(Calendar.DAY_OF_MONTH);


    public void cargarSpinner2() {
        names.add("Selecciona...");
        names.add("1");
        names.add("2");
        names.add("3");
        names.add("4");
        names.add("5");
        names.add("6");
        names.add("7");
        names.add("8");
        names.add("9");
        names.add("10");
        names.add("11");
        names.add("12");
        names.add("13");
        names.add("14");
        names.add("15");
        names.add("16");
        names.add("17");
        names.add("18");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, names);
        Pasajeros.setAdapter(adapter1);


    }

    //FOTOS

    public void imgFotoOffline() {
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto = null;
            try {
                nombreImagen1 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio + ".png";
                foto = new File(getApplication().getExternalFilesDir(null), nombreImagen1);
                rutaImagen1 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {

                uri_img = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT, uri_img);
                startActivityForResult(intentCaptura, 0);
            }
        }
    }

    public void imgFoto() {
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto = null;
            try {
                foto = new File(getApplication().getExternalFilesDir(null), "preentradas1.png");
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {

                uri_img = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT, uri_img);
                startActivityForResult(intentCaptura, 0);
            }
        }
    }

    public void imgFoto2Offline() {
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {
            File foto = null;
            try {
                nombreImagen2 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio2 + ".png";
                foto = new File(getApplication().getExternalFilesDir(null), nombreImagen2);
                rutaImagen2 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {
                uri_img2 = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT, uri_img2);
                startActivityForResult(intentCaptura, 1);
            }
        }
    }

    public void imgFoto2() {
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {
            File foto = null;
            try {
                foto = new File(getApplication().getExternalFilesDir(null), "preentradas2.png");
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {
                uri_img2 = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT, uri_img2);
                startActivityForResult(intentCaptura, 1);
            }
        }
    }

    public void imgFoto3Offline() {
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto = null;
            try {
                nombreImagen3 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio3 + ".png";
                foto = new File(getApplication().getExternalFilesDir(null), nombreImagen3);
                rutaImagen3 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {
                uri_img3 = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT, uri_img3);
                startActivityForResult(intentCaptura, 2);
            }
        }
    }

    public void imgFoto3() {
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto = null;
            try {
                foto = new File(getApplication().getExternalFilesDir(null), "preentradas3.png");
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {
                uri_img3 = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT, uri_img3);
                startActivityForResult(intentCaptura, 2);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {

                Bitmap bitmap;
                bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/" + nombreImagen1);
                /*if (Offline){
                    bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png");
                }else{
                    bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/preentradas1.png");
                }*/

                view1.setVisibility(View.VISIBLE);
                view1.setImageBitmap(bitmap);
                fotos1 = 1;

            }
            if (requestCode == 1) {

                Bitmap bitmap2;
                bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/" + nombreImagen2);

                /*if (Offline){
                    bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png");
                }else {
                    bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/preentradas2.png");
                }*/

                view2.setVisibility(View.VISIBLE);
                view2.setImageBitmap(bitmap2);
                fotos2 = 1;


            }

            if (requestCode == 2) {


                Bitmap bitmap3;
                bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/" + nombreImagen3);

                /*if (Offline){
                    bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio3+".png");
                }else {
                    bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/preentradas3.png");
                }*/

                view3.setVisibility(View.VISIBLE);
                view3.setImageBitmap(bitmap3);
                fotos3 = 1;


            }
        }
    }

    public void callesOffline() {

        try {
            String id_residencial = Conf.getResid().trim();
            String parametros[] = {id_residencial};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "calles", parametros, null);

            if (cursor.moveToFirst()) {
                ja1 = new JSONArray();
                do {
                    ja1.put(cursor.getString(0));
                } while (cursor.moveToNext());

                cargarSpinner();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al obtener calles")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }

    }

    public void calles() {

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_1.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja1 = new JSONArray(response);
                        cargarSpinner();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_residencial", Conf.getResid().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void cargarSpinner() {


        try {
            calles.add("Seleccionar..");
            calles.add("Seleccionar...");

            for (int i = 0; i < ja1.length(); i += 1) {
                calles.add(ja1.getString(i + 0));
            }

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, calles);
            Calle.setAdapter(adapter1);
            Calle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (Calle.getSelectedItem().equals("Seleccionar..")) {
                        calles.remove(0);
                    } else if (Calle.getSelectedItem().equals("Seleccionar...")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                        alertDialogBuilder.setTitle("Alerta");
                        alertDialogBuilder
                                .setMessage("No selecciono ninguna calle...")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                }).create().show();
                    } else {
                        numero.clear();
                        if (Offline) {
                            numerosOffline(Calle.getSelectedItem().toString());
                        } else {
                            numeros(Calle.getSelectedItem().toString());
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void menuOffline() {
        Log.e("info", "menu offline");
        try {
            Cursor cursoAppCaseta = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_APP_CASETA, null, null, null);

            ja2 = new JSONArray();

            if (cursoAppCaseta.moveToFirst()) {
                ja2.put(cursoAppCaseta.getString(0));
                ja2.put(cursoAppCaseta.getString(1));
                ja2.put(cursoAppCaseta.getString(2));
                ja2.put(cursoAppCaseta.getString(3));
                ja2.put(cursoAppCaseta.getString(4));
                ja2.put(cursoAppCaseta.getString(5));
                ja2.put(cursoAppCaseta.getString(6));
                ja2.put(cursoAppCaseta.getString(7));
                ja2.put(cursoAppCaseta.getString(8));
                ja2.put(cursoAppCaseta.getString(9));
                ja2.put(cursoAppCaseta.getString(10));
                ja2.put(cursoAppCaseta.getString(11));
                ja2.put(cursoAppCaseta.getString(12));

                submenuOffline(ja2.getString(0));

            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al obtener datos")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
            cursoAppCaseta.close();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void menu() {
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/menu.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja2 = new JSONArray(response);
                        submenu(ja2.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_residencial", Conf.getResid());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void submenuOffline(final String id_app) {
        Log.e("info", "submenu offline");

        try {
            Cursor cursoAppCaseta = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_APPCASETAIMA, null, null, null, null);

            ja3 = new JSONArray();

            if (cursoAppCaseta.moveToFirst()) {
                ja3.put(cursoAppCaseta.getString(0));
                ja3.put(cursoAppCaseta.getString(1));
                ja3.put(cursoAppCaseta.getString(2));
                ja3.put(cursoAppCaseta.getString(3));
                ja3.put(cursoAppCaseta.getString(4));
                ja3.put(cursoAppCaseta.getString(5));
                ja3.put(cursoAppCaseta.getString(6));
                ja3.put(cursoAppCaseta.getString(7));
                ja3.put(cursoAppCaseta.getString(8));
                ja3.put(cursoAppCaseta.getString(9));
                ja3.put(cursoAppCaseta.getString(10));

                imagenes();
            } else {
                int $arreglo[] = {0};
                try {
                    ja3 = new JSONArray($arreglo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imagenes();
            }
            cursoAppCaseta.close();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void submenu(final String id_app) {
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/menu_2.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if (response.equals("error")) {
                    int $arreglo[] = {0};
                    try {
                        ja3 = new JSONArray($arreglo);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    imagenes();
                } else {
                    response = response.replace("][", ",");
                    if (response.length() > 0) {
                        try {
                            ja3 = new JSONArray(response);

                            imagenes();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_app", id_app.trim());
                params.put("id_residencial", Conf.getResid());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void imagenes() {
        try {
            Visita();
            if (ja3.getString(0).equals("0") || ja3.getString(3).equals("0")) {
                registrar1.setVisibility(View.VISIBLE);
                espacio1.setVisibility(View.VISIBLE);

                Foto1.setVisibility(View.GONE);
                espacio2.setVisibility(View.GONE);
                Foto1View.setVisibility(View.GONE);
                espacio3.setVisibility(View.GONE);
                registrar2.setVisibility(View.GONE);
                espacio4.setVisibility(View.GONE);
                Foto2.setVisibility(View.GONE);
                espacio5.setVisibility(View.GONE);
                Foto2View.setVisibility(View.GONE);
                espacio6.setVisibility(View.GONE);
                registrar3.setVisibility(View.GONE);
                espacio7.setVisibility(View.GONE);
                Foto3.setVisibility(View.GONE);
                espacio8.setVisibility(View.GONE);
                Foto3View.setVisibility(View.GONE);
                espacio9.setVisibility(View.GONE);
                registrar4.setVisibility(View.VISIBLE);
                espacio10.setVisibility(View.VISIBLE);
            } else if (ja3.getString(3).equals("1") && ja3.getString(5).equals("0") && ja3.getString(7).equals("0")) {
                registrar1.setVisibility(View.GONE);
                espacio1.setVisibility(View.GONE);

                Foto1.setVisibility(View.VISIBLE);
                espacio2.setVisibility(View.VISIBLE);
                nombre_foto1.setVisibility(View.VISIBLE);
                nombre_foto1.setText(ja3.getString(4) + ":");

                Foto1View.setVisibility(View.VISIBLE);
                espacio3.setVisibility(View.VISIBLE);
                registrar2.setVisibility(View.GONE);
                espacio4.setVisibility(View.GONE);
                Foto2.setVisibility(View.GONE);
                espacio5.setVisibility(View.GONE);
                Foto2View.setVisibility(View.GONE);
                espacio6.setVisibility(View.GONE);
                registrar3.setVisibility(View.GONE);
                espacio7.setVisibility(View.GONE);
                Foto3.setVisibility(View.GONE);
                espacio8.setVisibility(View.GONE);
                Foto3View.setVisibility(View.GONE);
                espacio9.setVisibility(View.GONE);
                registrar4.setVisibility(View.VISIBLE);
                espacio10.setVisibility(View.VISIBLE);
            } else if (ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("0")) {
                registrar1.setVisibility(View.GONE);
                espacio1.setVisibility(View.GONE);

                Foto1.setVisibility(View.VISIBLE);
                espacio2.setVisibility(View.VISIBLE);
                nombre_foto1.setVisibility(View.VISIBLE);
                nombre_foto1.setText(ja3.getString(4) + ":");
                Foto1View.setVisibility(View.VISIBLE);
                espacio3.setVisibility(View.VISIBLE);

                registrar2.setVisibility(View.GONE);
                espacio4.setVisibility(View.GONE);
                nombre_foto2.setVisibility(View.VISIBLE);
                nombre_foto2.setText(ja3.getString(6) + ":");
                Foto2.setVisibility(View.VISIBLE);
                espacio5.setVisibility(View.VISIBLE);
                Foto2View.setVisibility(View.VISIBLE);
                espacio6.setVisibility(View.VISIBLE);

                registrar3.setVisibility(View.GONE);
                espacio7.setVisibility(View.GONE);
                Foto3.setVisibility(View.GONE);
                espacio8.setVisibility(View.GONE);
                Foto3View.setVisibility(View.GONE);
                espacio9.setVisibility(View.GONE);
                registrar4.setVisibility(View.VISIBLE);
                espacio10.setVisibility(View.VISIBLE);
            } else if (ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("1")) {
                registrar1.setVisibility(View.GONE);
                espacio1.setVisibility(View.GONE);

                Foto1.setVisibility(View.VISIBLE);
                espacio2.setVisibility(View.VISIBLE);
                nombre_foto1.setVisibility(View.VISIBLE);
                nombre_foto1.setText(ja3.getString(4) + ":");
                Foto1View.setVisibility(View.VISIBLE);
                espacio3.setVisibility(View.VISIBLE);

                registrar2.setVisibility(View.GONE);
                espacio4.setVisibility(View.GONE);
                nombre_foto2.setVisibility(View.VISIBLE);
                nombre_foto2.setText(ja3.getString(6) + ":");
                Foto2.setVisibility(View.VISIBLE);
                espacio5.setVisibility(View.VISIBLE);
                Foto2View.setVisibility(View.VISIBLE);
                espacio6.setVisibility(View.VISIBLE);

                registrar3.setVisibility(View.GONE);
                espacio7.setVisibility(View.GONE);
                nombre_foto3.setVisibility(View.VISIBLE);
                nombre_foto3.setText(ja3.getString(8) + ":");
                Foto3.setVisibility(View.VISIBLE);
                espacio8.setVisibility(View.VISIBLE);
                Foto3View.setVisibility(View.VISIBLE);
                espacio9.setVisibility(View.VISIBLE);
                registrar4.setVisibility(View.VISIBLE);
                espacio10.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Visita() {

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_5.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja5 = new JSONArray(response);
                        Dtl(ja5.getString(0));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", Conf.getQR());
                params.put("id_residencial", Conf.getResid().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void Dtl(final String IdUsu) {

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_8.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja6 = new JSONArray(response);

                        Datos();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", IdUsu.trim());
                params.put("id_residencial", Conf.getResid().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void Datos() {
        try {
            Nombre.setText(ja5.getString(7));
            Comentarios.setText(ja5.getString(9));

            if (ja5.getString(4).equals("1") || ja5.getString(4).equals("0")) {
                visi.setChecked(true);
                taxi.setChecked(false);
                prove.setChecked(false);
            } else if (ja5.getString(4).equals("2")) {
                visi.setChecked(false);
                taxi.setChecked(false);
                prove.setChecked(true);
            } else if (ja5.getString(4).equals("3")) {
                visi.setChecked(false);
                taxi.setChecked(true);
                prove.setChecked(false);
            }

            if (Offline) {
                storageReference.child(Conf.getPin() + "/caseta/" + ja6.getString(11))
                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override

                            public void onSuccess(Uri uri) {
                                Glide.with(PreEntradasActivity.this)
                                        .load(uri)
                                        .error(R.drawable.log)
                                        .centerInside()
                                        .into(view1);

                                txtFoto1.setVisibility(android.view.View.GONE);
                                view1.setVisibility(android.view.View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                txtFoto1.setText(Global_info.getTexto2Imagenes());
                            }
                        });

                storageReference.child(Conf.getPin() + "/caseta/" + ja6.getString(12))
                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override

                            public void onSuccess(Uri uri) {
                                Glide.with(PreEntradasActivity.this)
                                        .load(uri)
                                        .error(R.drawable.log)
                                        .centerInside()
                                        .into(view2);

                                txtFoto2.setVisibility(android.view.View.GONE);
                                view2.setVisibility(android.view.View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                txtFoto2.setText(Global_info.getTexto2Imagenes());
                            }
                        });

                storageReference.child(Conf.getPin() + "/caseta/" + ja6.getString(13))
                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override

                            public void onSuccess(Uri uri) {
                                Glide.with(PreEntradasActivity.this)
                                        .load(uri)
                                        .error(R.drawable.log)
                                        .centerInside()
                                        .into(view3);

                                txtFoto3.setVisibility(android.view.View.GONE);
                                view3.setVisibility(android.view.View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                txtFoto3.setText(Global_info.getTexto2Imagenes());
                            }
                        });
            } else {
                txtFoto1.setText(Global_info.getTexto3Imagenes());
                txtFoto2.setText(Global_info.getTexto3Imagenes());
                txtFoto3.setText(Global_info.getTexto3Imagenes());
            }


            Placas.setText(Conf.getPlacas());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Verificar() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿ Desea registrar la entrada ?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        if (Offline) {
                            busquedaOffline();
                        } else {
                            pd.show();
                            busqueda();
                        }
                    }
                }).create().show();
    }

    public void numerosOffline(final String IdUsu) {

        try {
            String id_residencial = Conf.getResid().trim();
            String calle = IdUsu;

            String parametros[] = {calle, id_residencial};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "numeros", parametros, null);

            if (cursor.moveToFirst()) {
                ja2 = new JSONArray();
                do {
                    ja2.put(cursor.getString(0));
                } while (cursor.moveToNext());

                cargarSpinner3();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al obtener numeros de calles")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }
    }


    public void numeros(final String IdUsu) {

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_9.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja2 = new JSONArray(response);
                        Numero_o.setVisibility(View.VISIBLE);
                        cargarSpinner3();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_residencial", Conf.getResid().trim());
                params.put("calle", IdUsu);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void cargarSpinner3() {

        numero.add("Seleccionar...");

        try {
            for (int i = 0; i < ja2.length(); i += 1) {
                numero.add(ja2.getString(i + 0));
            }

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, numero);
            Numero.setAdapter(adapter1);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarSpinner4() {

        numero.add("Seleccionar...");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, numero);
        Numero.setAdapter(adapter1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void busquedaOffline() {
        if (Calle.getSelectedItem().equals("Seleccionar..") || Calle.getSelectedItem().equals("Seleccionar...") || Numero.getSelectedItem().equals("Seleccionar...")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
            alertDialogBuilder.setTitle("Alerta");
            alertDialogBuilder
                    .setMessage("No selecciono ninguna calle o número...")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).create().show();
        } else {

            try {
                String id_residencial = Conf.getResid().trim();
                String numero = Numero.getSelectedItem().toString();
                String calle = Calle.getSelectedItem().toString();

                String parametros[] = {id_residencial, calle, numero};

                Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "verificaUP", parametros, null);

                if (cursor.moveToFirst()) {
                    int contador = 0;
                    do {
                        contador++;
                    } while (cursor.moveToNext());

                    Log.e("INFO ", "Valor de contador: " + contador);

                    if (contador > 2) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                        alertDialogBuilder.setTitle("Alerta");
                        alertDialogBuilder
                                .setMessage("UP no encontrada en modo offline")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                }).create().show();
                    } else if (contador == 1) {
                        ja4 = new JSONArray();
                        if (cursor.moveToFirst()) {
                            ja4.put(cursor.getString(0));
                            ja4.put(cursor.getString(1));
                            ja4.put(cursor.getString(2));
                            ja4.put(cursor.getString(3));
                            ja4.put(cursor.getString(4));
                            ja4.put(cursor.getString(5));
                            registroOffline();
                        }
                    } else if (contador == 2) {
                        try {
                            Cursor cursor1 = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "verificaUP2", parametros, null);
                            if (cursor1.moveToFirst()) {
                                ja4 = new JSONArray();
                                ja4.put(cursor1.getString(0));
                                ja4.put(cursor1.getString(1));
                                ja4.put(cursor1.getString(2));
                                ja4.put(cursor1.getString(3));
                                ja4.put(cursor1.getString(4));
                                ja4.put(cursor1.getString(5));
                                registroOffline();
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                                alertDialogBuilder.setTitle("Alerta");
                                alertDialogBuilder
                                        .setMessage("UP no encontrada en modo offline")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        }).create().show();
                            }
                        } catch (Exception ex) {
                            Log.e("Exception", ex.toString());
                        }
                    }

                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("UP no encontrada en modo offline")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            }).create().show();
                }

                cursor.close();

            } catch (Exception ex) {
                Log.e("Exception", ex.toString());
            }
        }
    }

    public void busqueda() {
        if (Calle.getSelectedItem().equals("Seleccionar..") || Calle.getSelectedItem().equals("Seleccionar...") || Numero.getSelectedItem().equals("Seleccionar...")) {
            pd.dismiss();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
            alertDialogBuilder.setTitle("Alerta");
            alertDialogBuilder
                    .setMessage("No selecciono ninguna calle o número...")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).create().show();
        } else {
            String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_2.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if (response.equals("error")) {

                        pd.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                        alertDialogBuilder.setTitle("Alerta");
                        alertDialogBuilder
                                .setMessage("No existe UP")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                }).create().show();

                    } else {
                        response = response.replace("][", ",");
                        try {
                            ja4 = new JSONArray(response);
                            registro();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", "Error: " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_residencial", Conf.getResid().trim());
                    params.put("calle", Calle.getSelectedItem().toString());
                    params.put("numero", Numero.getSelectedItem().toString());
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void registroOffline() {


        if (Placas.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Campo de placas", Toast.LENGTH_SHORT).show();
        } else if (Placas.getText().toString().equals(" ")) {
            Toast.makeText(getApplicationContext(), "Campo de placas ", Toast.LENGTH_SHORT).show();
        } else if (Placas.getText().toString().equals("N/A")) {
            Toast.makeText(getApplicationContext(), "Campo de placas", Toast.LENGTH_SHORT).show();
        } else {

            try {
                if (fotos1 == 1) {
                    f1 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio + ".png";

                    ContentValues val_img1 = ValuesImagen(f1, Conf.getPin() + "/caseta/" + f1.trim(), rutaImagen1);
                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                } else {
                    f1 = ja6.getString(11);
                }
                if (fotos2 == 1) {
                    f2 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio2 + ".png";

                    ContentValues val_img2 = ValuesImagen(f2, Conf.getPin() + "/caseta/" + f2.trim(), rutaImagen2);
                    Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);

                } else {
                    f2 = ja6.getString(12);
                }
                if (fotos3 == 1) {
                    f3 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio3 + ".png";

                    ContentValues val_img3 = ValuesImagen(f3, Conf.getPin() + "/caseta/" + f3.trim(), rutaImagen3);
                    Uri uri3 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img3);
                } else {
                    f3 = ja6.getString(13);
                }

                if (visi.isChecked()) {
                    valor = "1";
                } else if (taxi.isChecked()) {
                    valor = "3";
                } else if (prove.isChecked()) {
                    valor = "2";
                }


                LocalDateTime hoy = LocalDateTime.now();

                int year = hoy.getYear();
                int month = hoy.getMonthValue();
                int day = hoy.getDayOfMonth();
                int hour = hoy.getHour();
                int minute = hoy.getMinute();
                int second = hoy.getSecond();

                String fecha = "";

                //Poner el cero cuando el mes o dia es menor a 10
                if (day < 10 || month < 10) {
                    if (month < 10 && day >= 10) {
                        fecha = year + "-0" + month + "-" + day;
                    } else if (month >= 10 && day < 10) {
                        fecha = year + "-" + month + "-0" + day;
                    } else if (month < 10 && day < 10) {
                        fecha = year + "-0" + month + "-0" + day;
                    }
                } else {
                    fecha = year + "-" + month + "-" + day;
                }

                String hora = "";

                if (hour < 10 || minute < 10) {
                    if (hour < 10 && minute >= 10) {
                        hora = "0" + hour + ":" + minute;
                    } else if (hour >= 10 && minute < 10) {
                        hora = hour + ":0" + minute;
                    } else if (hour < 10 && minute < 10) {
                        hora = "0" + hour + ":0" + minute;
                    }
                } else {
                    hora = hour + ":" + minute;
                }

                String segundos = "00";

                if (second < 10) {
                    segundos = "0" + second;
                } else {
                    segundos = "" + second;
                }

                if (visi.isChecked()) {
                    valor = "1";
                } else if (taxi.isChecked()) {
                    valor = "3";
                } else if (prove.isChecked()) {
                    valor = "2";
                }

                ContentValues values = new ContentValues();
                values.put("id_residencial", Conf.getResid().trim());
                values.put("id_usuario", ja4.getString(0));
                values.put("id_tipo_visita", "0");
                values.put("id_tipo", valor);
                values.put("ilimitada", 1);
                values.put("evento", "");
                values.put("nombre_visita", Nombre.getText().toString().trim());
                values.put("correo_electronico", "");
                values.put("comentarios", Comentarios.getText().toString().trim());
                values.put("fecha_entrada", fecha + " " + hora + ":" + segundos);
                values.put("fecha_salida", "0000-00-00 00:00:00");
                values.put("codigo_qr", "");
                values.put("fecha_registro", fecha);
                values.put("club", "0");
                values.put("estatus", 1);
                values.put("sqliteEstatus", 1);

                Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_VISITA, values);

                String idUri = uri.getLastPathSegment();

                int insertar = Integer.parseInt(idUri);

                if (insertar != -1) { //Registrar Codigo qr
                    int actualizar;

                    try {

                        ContentValues values2 = new ContentValues();
                        values2.put("codigo_qr", insertar + "-" + numero_aletorio);

                        actualizar = getContentResolver().update(UrisContentProvider.URI_CONTENIDO_VISITA, values2, "id = " + insertar, null);

                        if (actualizar != -1) {//Registrar dtl entradas salidas

                            ContentValues values3 = new ContentValues();
                            values3.put("id_residencial", Conf.getResid().trim());
                            values3.put("id_visita", insertar);
                            values3.put("entrada_real", fecha + " " + hora + ":" + segundos);
                            values3.put("salida_real", "0000-00-00 00:00:00");
                            values3.put("guardia_de_entrada", Conf.getUsu().trim());
                            values3.put("guardia_de_salida", "0");
                            values3.put("cajon", "N/A");
                            values3.put("personas", Pasajeros.getSelectedItem().toString());
                            values3.put("placas", Placas.getText().toString().trim());
                            values3.put("descripcion_transporte", "");
                            values3.put("foto1", f1);
                            values3.put("foto2", f2);
                            values3.put("foto3", f3);
                            values3.put("comentarios_salida_tardia", "");
                            values3.put("estatus", 1);
                            values3.put("sqliteEstatus", 1);

                            Uri uri3 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_DTL_ENTRADAS_SALIDAS, values3);

                            String idUri3 = uri3.getLastPathSegment();

                            int insertar3 = Integer.parseInt(idUri3);

                            if (insertar3 != -1) {
                                Conf.setQR(insertar + "-" + numero_aletorio);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                                alertDialogBuilder.setTitle("Alerta");
                                alertDialogBuilder
                                        .setMessage("Entrada de visita exitosa en modo offline")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (Integer.parseInt(Conf.getTicketE()) == 1) {
                                                    Imprimir();
                                                } else {
                                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }
                                        }).create().show();
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                                alertDialogBuilder.setTitle("Alerta");
                                alertDialogBuilder
                                        .setMessage("Visita no exitosa en modo offline")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Toast.makeText(getApplicationContext(), "Visita No Registrada", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }).create().show();
                            }
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                            alertDialogBuilder.setTitle("Alerta");
                            alertDialogBuilder
                                    .setMessage("Visita no exitosa en modo offline")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(getApplicationContext(), "Visita No Registrada", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }).create().show();
                        }
                    } catch (Exception ex) {
                        Log.e("ExceptionAct", ex.toString());
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Visita no exitosa en modo offline")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(getApplicationContext(), "Visita No Registrada", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }

            } catch (Exception ex) {
                Log.e("Exception", ex.toString());
            }

        }
    }

    public ContentValues ValuesImagen(String nombre, String rutaFirebase, String rutaDispositivo) {
        ContentValues values = new ContentValues();
        values.put("titulo", nombre);
        values.put("direccionFirebase", rutaFirebase);
        values.put("rutaDispositivo", rutaDispositivo);
        return values;
    }

    public void registro() {


        if (Placas.getText().toString().equals("")) {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Campo de placas", Toast.LENGTH_SHORT).show();
        } else if (Placas.getText().toString().equals(" ")) {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Campo de placas ", Toast.LENGTH_SHORT).show();
        } else if (Placas.getText().toString().equals("N/A")) {
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Campo de placas", Toast.LENGTH_SHORT).show();
        } else {

            String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_3.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if (response.equals("error")) {
                        pd.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
                        alertDialogBuilder.setTitle("Alerta");
                        alertDialogBuilder
                                .setMessage("Visita No Exitosa")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getApplicationContext(), "Visita No Registrada", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.EscaneoVisitaActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }).create().show();
                    } else {
                        Conf.setQR(response);
                        if (fotos1 == 1) {
                            f1 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio + ".png";

                            ContentValues val_img1 = ValuesImagen(f1, Conf.getPin() + "/caseta/" + f1.trim(), rutaImagen1);
                            Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                        }
                        if (fotos2 == 1) {
                            f2 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio2 + ".png";

                            ContentValues val_img2 = ValuesImagen(f2, Conf.getPin() + "/caseta/" + f2.trim(), rutaImagen2);
                            Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);

                        }
                        if (fotos3 == 1) {
                            f3 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio3 + ".png";

                            ContentValues val_img3 = ValuesImagen(f3, Conf.getPin() + "/caseta/" + f3.trim(), rutaImagen3);
                            Uri uri3 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img3);
                        }

                        try {
                            if (ja3.getString(9).trim().equals("1")) {
                                ContentValues val_img4 = ValuesImagen(nombreImagenPlaca, Conf.getPin() + "/caseta/" + nombreImagenPlaca.trim(), rutaImagenPlaca);
                                Uri uri4 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img4);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                            /*if(fotos1==1){
                                upload1();
                            }
                            if(fotos2==1){
                                upload2();
                            }
                            if(fotos3==1){
                                upload3();
                            }*/

                        Terminar();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", "Error: " + error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    try {
                        if (fotos1 == 1) {
                            f1 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio + ".png";
                        } else {
                            f1 = ja6.getString(11);
                        }
                        if (fotos2 == 1) {
                            f2 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio2 + ".png";
                        } else {
                            f2 = ja6.getString(12);
                        }
                        if (fotos3 == 1) {
                            f3 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio3 + ".png";
                        } else {
                            f3 = ja6.getString(13);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (visi.isChecked()) {
                        valor = "1";
                    } else if (taxi.isChecked()) {
                        valor = "3";
                    } else if (prove.isChecked()) {
                        valor = "2";
                    }


                    Map<String, String> params = new HashMap<>();
                    try {

                        params.put("id_residencial", Conf.getResid().trim());
                        params.put("id_usuario", ja4.getString(0));
                        params.put("id_tipo", valor);
                        params.put("nombre", Nombre.getText().toString().trim());


                        params.put("placas", Placas.getText().toString().trim());
                        params.put("pasajeros", Pasajeros.getSelectedItem().toString());
                        params.put("guardia_de_entrada", Conf.getUsu().trim());

                        params.put("foto1", f1);
                        params.put("foto2", f2);
                        params.put("foto3", f3);

                        params.put("usuario", ja4.getString(1).trim() + " " + ja4.getString(2).trim() + " " + ja4.getString(3).trim());
                        params.put("token", ja4.getString(5).trim());
                        params.put("correo", ja4.getString(4).trim());
                        params.put("nom_residencial", Conf.getNomResi().trim());

                        params.put("comentarios", Comentarios.getText().toString().trim());


                    } catch (JSONException e) {
                        Log.e("TAG", "Error: " + e.toString());
                    }
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    public void upload1() {

        StorageReference mountainImagesRef = null;
        mountainImagesRef = storageReference.child(Conf.getPin() + "/caseta/app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio + ".png");

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
                Toast.makeText(PreEntradasActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                //upload2();

            }
        });
    }


    public void upload2() {

        StorageReference mountainImagesRef2 = null;
        mountainImagesRef2 = storageReference.child(Conf.getPin() + "/caseta/app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio2 + ".png");

        UploadTask uploadTask = mountainImagesRef2.putFile(uri_img2);


        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                pd2.show(); // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
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

                Toast.makeText(PreEntradasActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
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
        mountainImagesRef3 = storageReference.child(Conf.getPin() + "/caseta/app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio3 + ".png");

        UploadTask uploadTask = mountainImagesRef3.putFile(uri_img3);

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
                Toast.makeText(PreEntradasActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd3.dismiss();
            }
        });
    }


    public void Terminar() {

        pd.dismiss();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("Entrada de Visita Exitosa")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (!Offline) {
                            //Solo ejecutar si el servicio no se esta ejecutando
                            if (!servicioFotos()) {
                                Intent cargarFotos = new Intent(PreEntradasActivity.this, subirFotos.class);
                                startService(cargarFotos);
                            }
                        }

                        if (Integer.parseInt(Conf.getTicketE()) == 1) {
                            Imprimir();
                        } else {
                            Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }).create().show();

    }

    public void Imprimir() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreEntradasActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("Desea imprimir ticket?")
                .setPositiveButton("Si ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(), TicketImprimirActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.EntradasSalidasActivity.class);
        startActivity(intent);
        finish();
    }


}