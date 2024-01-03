package mx.linkom.caseta_grupokap;

import static android.view.View.GONE;
import static solar.blaz.date.week.WeekDatePicker.TAG;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import mx.linkom.caseta_grupokap.detectPlaca.DetectarPlaca;
import mx.linkom.caseta_grupokap.detectPlaca.objectDetectorClass;
import mx.linkom.caseta_grupokap.offline.Database.UrisContentProvider;
import mx.linkom.caseta_grupokap.offline.Global_info;
import mx.linkom.caseta_grupokap.offline.Servicios.subirFotos;

public class AccesosActivity extends mx.linkom.caseta_grupokap.Menu {
    Configuracion Conf;
    FirebaseStorage storage;
    StorageReference storageReference;

    LinearLayout rlPermitido, rlDenegado, rlVista;
    TextView tvMensaje;
    TextView Nombre, Dire, Visi, Tipo;

    EditText Placas, editTextPlacasPorFoto;
    Spinner Pasajeros;
    LinearLayout Pasajeros1;

    ArrayList<String> names;
    JSONArray ja1, ja2, ja3, ja4, ja5, ja6;
    Bitmap bitmap, bitmap2, bitmap3;
    ProgressDialog pd, pd2, pd3, pd4, pd5;
    int foto;
    String f1, f2, f3;

    LinearLayout espacio1, espacio2, espacio3, espacio4, espacio5, espacio6, espacio7, espacio8, espacio9, espacio10;
    LinearLayout registrar1, registrar2, registrar3, registrar4;
    Button reg1, reg2, reg3, reg4, btn_foto1, btn_foto2, btn_foto3;
    LinearLayout Foto1View, Foto2View, Foto3View;
    LinearLayout Foto1, Foto2, Foto3, LinLayPlacasTextoPorFoto, CPlacasTexto2, LinLayEspacioPlacasCono, LinLayRadioButtonsPlacas;
    ImageView view1, view2, view3;
    TextView nombre_foto1, nombre_foto2, nombre_foto3;
    Uri uri_img, uri_img2, uri_img3;
    RadioGroup rdgGrupo2;
    RadioButton si, no;
    TextView dato, textViewPlacasPorFoto;
    LinearLayout CPlacasTexto;
    EditText Comentarios;

    /*ImageView iconoInternet;
    boolean Offline = false;*/
    String rutaImagen1 = "", rutaImagen2 = "", rutaImagen3 = "", rutaImagenPlaca = "", nombreImagen1 = "", nombreImagen2 = "", nombreImagen3 = "", nombreImagenPlaca = "";

    LinearLayout espacio1Placa, FotoPlaca, espacioPlaca, FotoPlacaView, espacio2Placa;
    TextView nombre_fotoPlaca;
    Button btn_fotoPlaca;
    ImageView viewPlaca;

    private mx.linkom.caseta_grupokap.detectPlaca.objectDetectorClass objectDetectorClass;
    boolean modeloCargado = false;
    private String btnFotoPlacaFuePresionado = "";

    private ImageButton btnMicrofonoComentarios;
    private static final int TXT_COMENTARIOS = 200;

    private int btnRegistrarPresionado = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accesos);

        Conf = new Configuracion(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        names = new ArrayList<String>();
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


        Nombre = (TextView) findViewById(R.id.setNombre);
        Tipo = (TextView) findViewById(R.id.setTipo);
        Dire = (TextView) findViewById(R.id.setDire);
        Visi = (TextView) findViewById(R.id.setVisi);
        Pasajeros1 = (LinearLayout) findViewById(R.id.setPasajeros1);
        Pasajeros = (Spinner) findViewById(R.id.setPasajeros);
        Placas = (EditText) findViewById(R.id.setPlacas);
        tvMensaje = (TextView) findViewById(R.id.setMensaje);

        rlVista = (LinearLayout) findViewById(R.id.rlVista);
        rlPermitido = (LinearLayout) findViewById(R.id.rlPermitido);
        rlDenegado = (LinearLayout) findViewById(R.id.rlDenegado);

        CPlacasTexto = (LinearLayout) findViewById(R.id.CPlacasTexto);

        rdgGrupo2 = (RadioGroup) findViewById(R.id.rdgGrupo2);
        si = (RadioButton) findViewById(R.id.Si);
        no = (RadioButton) findViewById(R.id.No);
        dato = (TextView) findViewById(R.id.placas_texto);

        textViewPlacasPorFoto = (TextView) findViewById(R.id.textViewPlacasPorFoto);

        CPlacasTexto = (LinearLayout) findViewById(R.id.CPlacasTexto);
        LinLayPlacasTextoPorFoto = (LinearLayout) findViewById(R.id.LinLayPlacasTextoPorFoto);
        CPlacasTexto2 = (LinearLayout) findViewById(R.id.CPlacasTexto2);
        LinLayEspacioPlacasCono = (LinearLayout) findViewById(R.id.LinLayEspacioPlacasCono);
        editTextPlacasPorFoto = (EditText) findViewById(R.id.setPlacasPorFoto);
        LinLayRadioButtonsPlacas = (LinearLayout) findViewById(R.id.LinLayRadioButtonsPlacas);

        //Variables para placa
        espacio1Placa = (LinearLayout) findViewById(R.id.espacio1Placa);
        FotoPlaca = (LinearLayout) findViewById(R.id.FotoPlaca);
        espacioPlaca = (LinearLayout) findViewById(R.id.espacioPlaca);
        FotoPlacaView = (LinearLayout) findViewById(R.id.FotoPlacaView);
        nombre_fotoPlaca = (TextView) findViewById(R.id.nombre_fotoPlaca);
        btn_fotoPlaca = (Button) findViewById(R.id.btn_fotoPlaca);
        viewPlaca = (ImageView) findViewById(R.id.viewPlaca);
        espacio2Placa = (LinearLayout) findViewById(R.id.espacio2Placa);

        btnMicrofonoComentarios = (ImageButton) findViewById(R.id.btnMicrofonoComentarios);
        Comentarios.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        btnMicrofonoComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradVoz("Diga los comentarios para esta visita", TXT_COMENTARIOS);
            }
        });

        /*iconoInternet = (ImageView) findViewById(R.id.iconoInternetActivitiAccesos);

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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccesosActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOffline())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                } else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccesosActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOnline())
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }
            }
        });*/

        Intent intent = getIntent();
        nombreImagenPlaca = intent.getStringExtra("nombreFotoPlaca");
        rutaImagenPlaca = intent.getStringExtra("rutaDispositivo");
        btnFotoPlacaFuePresionado = intent.getStringExtra("btnPlacas");

        if (nombreImagenPlaca == null || rutaImagenPlaca == null) {
            Log.e("INTENT", "No se enviaron los datos");
            nombreImagenPlaca = "";
            rutaImagenPlaca = "";
        }

        Log.e("AccesosActivity", "AccesosActivity: " + Global.getFotoPlaca() + "btn: " + btnFotoPlacaFuePresionado);

        if (Global.getFotoPlaca() && btnFotoPlacaFuePresionado != null) { //Esta activa la opcion de foto placa y viene de buscar la placa
            editTextPlacasPorFoto.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps() {
            }});
            editTextPlacasPorFoto.setText(Conf.getPlacas().trim());

            if (rutaImagenPlaca.isEmpty()){
                CPlacasTexto.setVisibility(View.VISIBLE);
                LinLayEspacioPlacasCono.setVisibility(View.VISIBLE);
            }else {
                CPlacasTexto.setVisibility(View.GONE);
                CPlacasTexto2.setVisibility(View.GONE);
                LinLayEspacioPlacasCono.setVisibility(View.GONE);
            }
        }else if (Global.getFotoPlaca() && btnFotoPlacaFuePresionado == null && !Conf.getTipoReg().equals("Peatonal")) { //Esta activa la opcion de foto placa y viene de codigo qr
            editTextPlacasPorFoto.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps() {
            }});
            if (!Conf.getTipoReg().equals("Peatonal")){
                CPlacasTexto.setVisibility(View.GONE);
                LinLayEspacioPlacasCono.setVisibility(View.GONE);
                CPlacasTexto2.setVisibility(View.GONE);
                LinLayRadioButtonsPlacas.setVisibility(GONE);
            }
        } else {
            CPlacasTexto.setVisibility(View.VISIBLE);
            Placas.setText(Conf.getPlacas().trim());
            Placas.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps() {
            }});
        }

        try {
            objectDetectorClass = new objectDetectorClass(getAssets(), "detectPlacaLKM.tflite", "labelmapTf.txt", 320);
            Log.e("AccesosActivity", "Modelo cargado correctamente");
            modeloCargado = true;
        } catch (IOException e) {
            modeloCargado = false;
            Log.e("AccesosActivity", "Error al cargar modelo");
        }


        //SI ES ACEPTADO O DENEGAODO
        if (Conf.getST().equals("Aceptado")) {
            rlVista.setVisibility(View.VISIBLE);
            rlPermitido.setVisibility(View.GONE);
            rlDenegado.setVisibility(View.GONE);

            menu();

            /*if (Offline) {
                menuOffline();
            } else {
                menu();
            }*/
        } else if (Conf.getST().equals("Denegado")) {
            rlDenegado.setVisibility(View.VISIBLE);
            rlVista.setVisibility(View.GONE);
            rlPermitido.setVisibility(View.GONE);
            tvMensaje.setText("QR Inexistente");
        }

        pd = new ProgressDialog(this);
        pd.setMessage("Registrando...");

        pd2 = new ProgressDialog(this);
        pd2.setMessage("Subiendo Imagenes.");

        pd3 = new ProgressDialog(this);
        pd3.setMessage("Subiendo Imagenes..");

        pd4 = new ProgressDialog(this);
        pd4.setMessage("Subiendo Imagenes...");

        pd5 = new ProgressDialog(this);
        pd5.setMessage("Subiendo Imagenes....");

        reg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg1.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        btnRegistrarPresionado = 1;
                        botonPresionado(0);
                        Validacion();
                    }
                }, 300);
            }
        });

        reg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg2.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btnRegistrarPresionado = 2;
                        botonPresionado(0);
                        Validacion();
                    }
                }, 300);
            }
        });

        reg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg3.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btnRegistrarPresionado = 3;
                        botonPresionado(0);
                        Validacion();
                    }
                }, 300);
            }
        });

        reg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg4.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        btnRegistrarPresionado = 4;
                        botonPresionado(0);
                        Validacion();
                    }
                }, 300);
            }
        });

        btn_fotoPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFotoPlacaOffline();
            }
        });

        btn_foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 1;
                imgFotoOffline();
                /*if (Offline) {
                    imgFotoOffline();
                } else {
                    imgFoto();
                }*/
            }
        });

        btn_foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 2;
                imgFoto2Offline();
                /*if (Offline) {
                    imgFoto2Offline();
                } else {
                    imgFoto2();
                }*/
            }
        });

        btn_foto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 3;
                imgFoto3Offline();
                /*if (Offline) {
                    imgFoto3Offline();
                } else {
                    imgFoto3();
                }*/
            }
        });
        Placas.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps() {
        }});
        cargarSpinner();

        if (Conf.getTipoReg().equals("Nada")) {
            dato.setText("Placas / Cono / Gafete / Credencial:");
            si.setChecked(true);
        } else if (Conf.getTipoReg().equals("Auto")) {
            dato.setText("Placas / Cono:");
            si.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
            si.setChecked(true);
        } else if (Conf.getTipoReg().equals("Peatonal")) {
            dato.setText("Gafete / Credencial:");
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean marcado = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.Si:
                if (marcado) {
                    if (Global.getFotoPlaca() && btnFotoPlacaFuePresionado != null) {
                        LinLayPlacasTextoPorFoto.setVisibility(View.VISIBLE);
                    } else {
                        CPlacasTexto.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.No:
                if (marcado) {
                    if (Global.getFotoPlaca() && btnFotoPlacaFuePresionado != null) {
                        LinLayPlacasTextoPorFoto.setVisibility(GONE);
                    } else {
                        CPlacasTexto.setVisibility(GONE);
                    }
                }
                break;
        }
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

    public void cargarSpinner() {

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

    String numero_aletorio2 = prime2 + segundo2[numRandonsegun2] + tercer2 + cuarto2[numRandoncuart2];

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void menuOffline() {
        Log.e("info", "menu offline");
        try {
            Cursor cursoAppCaseta = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_APP_CASETA, null, null, null);

            ja5 = new JSONArray();

            if (cursoAppCaseta.moveToFirst()) {
                ja5.put(cursoAppCaseta.getString(0));
                ja5.put(cursoAppCaseta.getString(1));
                ja5.put(cursoAppCaseta.getString(2));
                ja5.put(cursoAppCaseta.getString(3));
                ja5.put(cursoAppCaseta.getString(4));
                ja5.put(cursoAppCaseta.getString(5));
                ja5.put(cursoAppCaseta.getString(6));
                ja5.put(cursoAppCaseta.getString(7));
                ja5.put(cursoAppCaseta.getString(8));
                ja5.put(cursoAppCaseta.getString(9));
                ja5.put(cursoAppCaseta.getString(10));
                ja5.put(cursoAppCaseta.getString(11));
                ja5.put(cursoAppCaseta.getString(12));

                submenuOffline(ja5.getString(0));

            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
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
                        ja5 = new JSONArray(response);
                        submenu(ja5.getString(0));
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

            ja6 = new JSONArray();

            if (cursoAppCaseta.moveToFirst()) {
                ja6.put(cursoAppCaseta.getString(0));
                ja6.put(cursoAppCaseta.getString(1));
                ja6.put(cursoAppCaseta.getString(2));
                ja6.put(cursoAppCaseta.getString(3));
                ja6.put(cursoAppCaseta.getString(4));
                ja6.put(cursoAppCaseta.getString(5));
                ja6.put(cursoAppCaseta.getString(6));
                ja6.put(cursoAppCaseta.getString(7));
                ja6.put(cursoAppCaseta.getString(8));
                ja6.put(cursoAppCaseta.getString(9));
                ja6.put(cursoAppCaseta.getString(10));

                imagenes();
                VisitaOffline();
            } else {
                int $arreglo[] = {0};
                ja6 = new JSONArray($arreglo);
                imagenes();
                VisitaOffline();
            }
            cursoAppCaseta.close();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void submenu(final String id_app) {
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/menu_3.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                if (response.equals("error")) {
                    int $arreglo[] = {0};
                    try {
                        ja6 = new JSONArray($arreglo);
                        imagenes();
                        Visita();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    response = response.replace("][", ",");
                    if (response.length() > 0) {
                        try {
                            ja6 = new JSONArray(response);
                            imagenes();

                            if (ja6.getString(10).trim().equals("1")){
                                Global.setFotoPlaca(true);
                            }else {
                                Global.setFotoPlaca(false);
                            }

                            //OCULTAR VIEW DE FOTO PLACA
                            if (ja6.getString(3).equals("0") && (ja6.getString(10).trim().equals("1") && !Conf.getTipoReg().equals("Peatonal")) && rutaImagenPlaca != null){
                                try {
                                    if (ja6.getString(3).equals("1")){
                                        Foto1.setVisibility(View.VISIBLE);
                                        espacio2.setVisibility(View.VISIBLE);
                                        nombre_foto1.setVisibility(View.VISIBLE);
                                    }else {
                                        if (!rutaImagenPlaca.isEmpty()){
                                            registrar1.setVisibility(View.VISIBLE);
                                            espacio1.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else if ((!rutaImagenPlaca.isEmpty() && ja6.getString(3).equals("0")) || (Conf.getTipoReg().equals("Peatonal") && ja6.getString(3).equals("0"))){
                                registrar1.setVisibility(View.VISIBLE);
                                espacio1.setVisibility(View.VISIBLE);
                                Log.e("borrar", "OK");
                            }

                            Visita();
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

            if (ja6.getString(10).trim().equals("1") && !Conf.getTipoReg().equals("Peatonal")) {
                Global.setFotoPlaca(true);
                espacio1Placa.setVisibility(View.VISIBLE);
                FotoPlaca.setVisibility(View.VISIBLE);
                nombre_fotoPlaca.setVisibility(View.VISIBLE);
                btn_fotoPlaca.setVisibility(View.VISIBLE);
                viewPlaca.setVisibility(View.VISIBLE);
                espacio2Placa.setVisibility(View.VISIBLE);

                nombre_fotoPlaca.setText(ja6.getString(11) + ":");

                if (!nombreImagenPlaca.isEmpty() && !rutaImagenPlaca.isEmpty()) {
                    Bitmap bitmap;
                    bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/" + nombreImagenPlaca);

                    if (bitmap != null) {
                        espacioPlaca.setVisibility(View.VISIBLE);
                        FotoPlacaView.setVisibility(View.VISIBLE);
                        viewPlaca.setImageBitmap(bitmap);

                        CPlacasTexto.setVisibility(GONE);
                        LinLayPlacasTextoPorFoto.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                Global.setFotoPlaca(false);
            }

            if (ja6.getString(0).equals("0") || ja6.getString(3).equals("0")) {

                if (!Global.getFotoPlaca() && !rutaImagenPlaca.isEmpty()){
                    registrar1.setVisibility(View.VISIBLE);
                    espacio1.setVisibility(View.VISIBLE);
                }

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
                registrar4.setVisibility(View.GONE);
                espacio10.setVisibility(View.GONE);


            } else if (ja6.getString(3).equals("1")) {

                registrar1.setVisibility(View.GONE);
                espacio1.setVisibility(View.GONE);

                if (ja6.getString(10).trim().equals("0") || (ja6.getString(10).trim().equals("1") && Conf.getTipoReg().equals("Peatonal"))) {
                    Foto1.setVisibility(View.VISIBLE);
                    espacio2.setVisibility(View.VISIBLE);
                    nombre_foto1.setVisibility(View.VISIBLE);
                } else if (ja6.getString(10).trim().equals("1")) {
                    if (!nombreImagenPlaca.isEmpty() && !rutaImagenPlaca.isEmpty()) {

                        Bitmap bitmap;
                        bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/" + nombreImagenPlaca);

                        if (bitmap != null) {
                            Foto1.setVisibility(View.VISIBLE);
                            espacio2.setVisibility(View.VISIBLE);
                            nombre_foto1.setVisibility(View.VISIBLE);
                        }
                    }
                }
                nombre_foto1.setText(ja6.getString(4) + ":");

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
                registrar4.setVisibility(View.GONE);
                espacio10.setVisibility(View.GONE);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //FOTOS

    private void iniciarEntradVoz(String promt, int campo) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, promt);

        intent.putExtra("FIELD_EXTRA", campo);

        try {
            startActivityForResult(intent, campo);
        } catch (ActivityNotFoundException e) {
            Log.e("RECTETXT", e.toString());
        }
    }

    public void imgFotoPlacaOffline() {
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto = null;
            try {
                nombreImagenPlaca = "appPlaca" + anio + mes + dia + "-" + numero_aletorio + numero_aletorio2 + numero_aletorio3 + ".png";
                foto = new File(getApplication().getExternalFilesDir(null), nombreImagenPlaca);
                rutaImagenPlaca = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
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
                startActivityForResult(intentCaptura, 3);
            }
        }
    }

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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
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
                foto = new File(getApplication().getExternalFilesDir(null), "accesos1.png");
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
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
                foto = new File(getApplication().getExternalFilesDir(null), "accesos2.png");
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
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
                foto = new File(getApplication().getExternalFilesDir(null), "accesos3.png");
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {


                Bitmap bitmap;

                bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/" + nombreImagen1);

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

                /*if (Offline) {
                    bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio + ".png");
                } else {
                    bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/accesos1.png");
                }*/

                Foto1View.setVisibility(View.VISIBLE);

                view1.setVisibility(View.VISIBLE);
                view1.setImageBitmap(bitmap);
                espacio3.setVisibility(View.VISIBLE);


                try {
                    if (ja6.getString(3).equals("1") && ja6.getString(5).equals("0") && ja6.getString(7).equals("0")) {
                        registrar2.setVisibility(View.VISIBLE);
                        reg2.setVisibility(View.VISIBLE);
                        espacio4.setVisibility(View.VISIBLE);
                        espacio5.setVisibility(View.VISIBLE);
                    } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("0")) {
                        registrar2.setVisibility(View.GONE);
                        Foto2.setVisibility(View.VISIBLE);
                        espacio5.setVisibility(View.VISIBLE);
                        espacio6.setVisibility(View.VISIBLE);
                        nombre_foto2.setVisibility(View.VISIBLE);
                        nombre_foto2.setText(ja6.getString(6) + ":");
                    } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")) {
                        registrar2.setVisibility(View.GONE);
                        Foto2.setVisibility(View.VISIBLE);
                        espacio5.setVisibility(View.VISIBLE);
                        espacio6.setVisibility(View.VISIBLE);
                        nombre_foto2.setVisibility(View.VISIBLE);
                        nombre_foto2.setText(ja6.getString(6) + ":");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            if (requestCode == 1) {


                Bitmap bitmap2;

                bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/" + nombreImagen2);


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

                /*if (Offline) {
                    bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio2 + ".png");
                } else {
                    bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/accesos2.png");
                }*/


                Foto2View.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view2.setImageBitmap(bitmap2);
                espacio6.setVisibility(View.VISIBLE);


                try {
                    if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("0")) {
                        registrar3.setVisibility(View.VISIBLE);
                        reg3.setVisibility(View.VISIBLE);
                        espacio7.setVisibility(View.VISIBLE);
                    } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")) {
                        registrar3.setVisibility(View.GONE);
                        espacio7.setVisibility(View.VISIBLE);
                        espacio8.setVisibility(View.VISIBLE);
                        Foto3.setVisibility(View.VISIBLE);
                        espacio9.setVisibility(View.VISIBLE);
                        nombre_foto3.setVisibility(View.VISIBLE);
                        nombre_foto3.setText(ja6.getString(8) + ":");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (requestCode == 2) {


                Bitmap bitmap3;
                bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/" + nombreImagen3);

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

                /*if (Offline) {
                    bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio3 + ".png");
                } else {
                    bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/accesos3.png");
                }*/

                Foto3View.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view3.setImageBitmap(bitmap3);
                espacio10.setVisibility(View.VISIBLE);


                try {
                    if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")) {
                        registrar4.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            if (requestCode == 3) {

                Bitmap bitmap4 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/" + nombreImagenPlaca);

                if (modeloCargado) {
                    String txtPlaca = DetectarPlaca.getTextFromImage(DetectarPlaca.reconocerPlaca(bitmap4, objectDetectorClass, 1), AccesosActivity.this);
                    Log.e("PLACA", txtPlaca);
                    if (!txtPlaca.isEmpty()) {
                        editTextPlacasPorFoto.setText(txtPlaca);
                        Placas.setText(txtPlaca);

                    }

                }

                bitmap4 = DetectarPlaca.fechaHoraFoto(bitmap4);

                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(rutaImagenPlaca);
                    bitmap4.compress(Bitmap.CompressFormat.JPEG, 100, fos); // compress and save as JPEG
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                FotoPlacaView.setVisibility(View.VISIBLE);
                viewPlaca.setVisibility(View.VISIBLE);
                viewPlaca.setImageBitmap(bitmap4);
                espacio2Placa.setVisibility(View.VISIBLE);

                try {
                    if (ja6.getString(3).equals("1")){
                        Foto1.setVisibility(View.VISIBLE);
                        espacio2.setVisibility(View.VISIBLE);
                        nombre_foto1.setVisibility(View.VISIBLE);
                    }else {
                        registrar1.setVisibility(View.VISIBLE);
                        espacio1.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (Global.getFotoPlaca() && !Conf.getTipoReg().equals("Peatonal")){
                    CPlacasTexto2.setVisibility(View.GONE);
                    CPlacasTexto.setVisibility(View.GONE);
                    LinLayPlacasTextoPorFoto.setVisibility(View.VISIBLE);
                    LinLayEspacioPlacasCono.setVisibility(View.GONE);
                }

            }

            if (requestCode == TXT_COMENTARIOS && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String txtAnterior = " " + Comentarios.getText() + " " + result.get(0);
                Comentarios.setText(txtAnterior);
            }
        }
    }

    public void VisitaOffline() {
        Log.e("info", "visita offline");
        try {
            String qr = Conf.getQR().trim();
            String id_resid = Conf.getResid().trim();
            String parametros[] = {qr, id_resid};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_VISITA, null, "vst1", parametros, null);

            if (cursor.moveToFirst()) {
                ja1 = new JSONArray();
                ja1.put(cursor.getString(0));
                ja1.put(cursor.getString(1));
                ja1.put(cursor.getString(2));
                ja1.put(cursor.getString(3));
                ja1.put(cursor.getString(4));
                ja1.put(cursor.getString(5));
                ja1.put(cursor.getString(6));
                ja1.put(cursor.getString(7));
                ja1.put(cursor.getString(8));
                ja1.put(cursor.getString(9));
                ja1.put(cursor.getString(10));
                ja1.put(cursor.getString(11));
                ja1.put(cursor.getString(12));
                ja1.put(cursor.getString(13));
                ja1.put(cursor.getString(14));
                ja1.put(cursor.getString(15));

                UsuarioOffline(ja1.getString(2));
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al obtener datos de la visita")
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


    public void Visita() {

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_php1.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja1 = new JSONArray(response);
                        Usuario(ja1.getString(2));
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
                params.put("QR", Conf.getQR().trim());
                params.put("id_residencial", Conf.getResid().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void UsuarioOffline(final String IdUsu) { //DATOS USUARIO
        Log.e("info", "usuario offline");
        try {
            String id_residencial = Conf.getResid().trim();
            String id = IdUsu.trim();

            String parametros[] = {id, id_residencial};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_USUARIO, null, "dts_accesso_autos", parametros, null);

            if (cursor.moveToFirst()) {
                ja2 = new JSONArray();

                ja2.put(cursor.getString(0));
                ja2.put(cursor.getString(1));
                ja2.put(cursor.getString(2));
                ja2.put(cursor.getString(3));
                ja2.put(cursor.getString(4));
                ja2.put(cursor.getString(5));
                ja2.put(cursor.getString(6));

                dtlLugarOffline(ja2.getString(0));
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al obtener datos de usuario")
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

    public void Usuario(final String IdUsu) { //DATOS USUARIO

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_php2.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja2 = new JSONArray(response);
                        dtlLugar(ja2.getString(0));

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
                params.put("IdUsu", IdUsu.trim());
                params.put("id_residencial", Conf.getResid().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void dtlLugarOffline(final String idUsuario) {
        Log.e("info", "dtllugar offline");
        try {
            String id_residencial = Conf.getResid().trim();
            String id = idUsuario.trim();

            String parametros[] = {id_residencial, id};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "dtl_lugar_usuario", parametros, null);

            if (cursor.moveToFirst()) {
                ja3 = new JSONArray();
                ja3.put(cursor.getString(0));

                salidasOffline(ja1.getString(0));

            } else {
                sincasa();
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }
    }

    public void dtlLugar(final String idUsuario) {
        String URLResidencial = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_php3.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLResidencial, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("error")) {
                    sincasa();
                } else {

                    response = response.replace("][", ",");
                    if (response.length() > 0) {
                        try {
                            ja3 = new JSONArray(response);
                            salidas(ja1.getString(0));
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
                params.put("id_usuario", idUsuario.trim());
                params.put("id_residencial", Conf.getResid().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void salidasOffline(final String id_visitante) {
        Log.e("info", "salidas offline");
        try {
            String id_resid = Conf.getResid().trim();
            String id_visit = id_visitante.trim();
            String parametros[] = {id_resid, id_visit};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_DTL_ENTRADAS_SALIDAS, null, "vst_php4", parametros, null);

            if (cursor.moveToFirst()) {
                ja4 = new JSONArray();
                ja4.put(cursor.getString(0));
                ValidarQR();
            } else {
                int $arreglo[] = {0};
                ja4 = new JSONArray($arreglo);
                ValidarQR();
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }
    }

    public void salidas(final String id_visitante) {
        String URLResidencial = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_php4.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLResidencial, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    if (response.trim().equals("error")) {

                        int $arreglo[] = {0};
                        ja4 = new JSONArray($arreglo);
                        ValidarQR();

                    } else {
                        response = response.replace("][", ",");
                        ja4 = new JSONArray(response);
                        ValidarQR();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("id_visitante", id_visitante.trim());
                params.put("id_residencial", Conf.getResid().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void sincasa() {

        rlVista.setVisibility(View.GONE);
        rlPermitido.setVisibility(View.GONE);
        rlDenegado.setVisibility(View.VISIBLE);
        tvMensaje.setText(" No tiene asignada una unidad privativa.");

    }


    public void ValidarQR() {

        try {
            Calendar c = Calendar.getInstance();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateentrada = (Date) formatter.parse(ja1.getString(10));
            Date datesalida = (Date) formatter.parse(ja1.getString(11));

            //ANTES DE LA ENTRADA
            if (c.getTime().before(dateentrada) && ja4.getString(0).equals("0")) {//NUEVO
                rlVista.setVisibility(View.GONE);
                rlPermitido.setVisibility(View.GONE);
                rlDenegado.setVisibility(View.VISIBLE);
                tvMensaje.setText("Aún no es hora de entrada");
                //EN MEDIO
            } else if (c.getTime().equals(dateentrada) || c.getTime().before(datesalida)) {

                if (ja4.getString(0).equals("0")) { //NUEVO
                    rlVista.setVisibility(View.GONE);
                    rlDenegado.setVisibility(View.GONE);
                    rlPermitido.setVisibility(View.VISIBLE);

                    Nombre.setText(ja2.getString(1) + " " + ja2.getString(2) + " " + ja2.getString(3));
                    if (ja1.getString(4).equals("1") || ja1.getString(12).equals("0")) {
                        Tipo.setText("Visita");
                    } else if (ja1.getString(4).equals("2")) {
                        Tipo.setText("Proveedor / Servicios");
                    } else if (ja1.getString(4).equals("3")) {
                        Tipo.setText("Taxista");
                    }
                    Dire.setText(ja3.getString(0));
                    Visi.setText(ja1.getString(7));

                    if (Integer.parseInt(Conf.getPreQr()) == 1) {
                        Placas.setText(Conf.getPlacas());
                    }
                    Comentarios.setText(ja1.getString(9));

                } else if (ja4.getString(0).equals("1")) { //Entro y quiere volver a entrar
                    rlVista.setVisibility(View.GONE);
                    rlPermitido.setVisibility(View.GONE);
                    rlDenegado.setVisibility(View.VISIBLE);
                    tvMensaje.setText("Esté auto se encuentra dentro del complejo");

                } else if (ja4.getString(0).equals("2")) { //Entro y salio ; y quiere volver a entrar
                    rlVista.setVisibility(View.GONE);
                    rlPermitido.setVisibility(View.GONE);
                    rlDenegado.setVisibility(View.VISIBLE);
                    tvMensaje.setText("Esté código QR ya fue utilizado");
                }

            } else if (c.getTime().after(datesalida) && ja4.getString(0).equals("0")) { //NUEVO
                rlVista.setVisibility(View.GONE);
                rlPermitido.setVisibility(View.GONE);
                rlDenegado.setVisibility(View.VISIBLE);
                tvMensaje.setText("Esté código QR ha caducado");

            } else if (c.getTime().after(datesalida) && ja4.getString(0).equals("2")) { //ENTRO Y SALIO
                rlVista.setVisibility(View.GONE);
                rlPermitido.setVisibility(View.GONE);
                rlDenegado.setVisibility(View.VISIBLE);
                tvMensaje.setText("Esté código QR ha caducado");

            } else if (c.getTime().after(datesalida) && ja4.getString(0).equals("1")) {//ESTA ADENTRO
                rlVista.setVisibility(View.GONE);
                rlPermitido.setVisibility(View.GONE);
                rlDenegado.setVisibility(View.VISIBLE);
                tvMensaje.setText("Esté código QR ha caducado, Leer Salida");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Validacion() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿ Desea realizar la entrada ?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        pd.show();
                        Registrar();

                        /*if (Offline) {
                            RegistrarOffline();
                        } else {
                            pd.show();
                            Registrar();
                        }*/
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        botonPresionado(1);

                        /*Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                        startActivity(i);
                        finish();*/

                    }
                }).setCancelable(false).create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void RegistrarOffline() {

        if (Placas.getText().toString().equals("") && si.isChecked()) {
            Toast.makeText(getApplicationContext(), "Campo de placas", Toast.LENGTH_SHORT).show();
        } else if (Placas.getText().toString().equals(" ") && si.isChecked()) {
            Toast.makeText(getApplicationContext(), "Campo de placas ", Toast.LENGTH_SHORT).show();
        } else if (Placas.getText().toString().equals("N/A") && si.isChecked()) {
            Toast.makeText(getApplicationContext(), "Campo de placas", Toast.LENGTH_SHORT).show();
        } else {

            try {
                if (ja6.getString(0).equals("0") || ja6.getString(3).equals("0")) {
                    f1 = "";
                    f2 = "";
                    f3 = "";
                } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("0") && ja6.getString(7).equals("0")) {
                    f1 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio + ".png";
                    f2 = "";
                    f3 = "";

                    ContentValues val_img1 = ValuesImagen(f1, Conf.getPin() + "/caseta/" + f1.trim(), rutaImagen1);
                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("0")) {
                    f1 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio + ".png";
                    f2 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio2 + ".png";
                    f3 = "";

                    ContentValues val_img1 = ValuesImagen(f1, Conf.getPin() + "/caseta/" + f1.trim(), rutaImagen1);
                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                    ContentValues val_img2 = ValuesImagen(f2, Conf.getPin() + "/caseta/" + f2.trim(), rutaImagen2);
                    Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);


                } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")) {
                    f1 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio + ".png";
                    f2 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio2 + ".png";
                    f3 = "app" + anio + mes + dia + Placas.getText().toString() + "-" + numero_aletorio3 + ".png";

                    ContentValues val_img1 = ValuesImagen(f1, Conf.getPin() + "/caseta/" + f1.trim(), rutaImagen1);
                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                    ContentValues val_img2 = ValuesImagen(f2, Conf.getPin() + "/caseta/" + f2.trim(), rutaImagen2);
                    Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);

                    ContentValues val_img3 = ValuesImagen(f3, Conf.getPin() + "/caseta/" + f3.trim(), rutaImagen3);
                    Uri uri3 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img3);

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

                ContentValues values = new ContentValues();
                values.put("id_residencial", Conf.getResid().trim());
                values.put("id_visita", ja1.getString(0).trim());
                values.put("entrada_real", fecha + " " + hora + ":" + segundos);
                values.put("salida_real", "0000-00-00 00:00:00");
                values.put("guardia_de_entrada", Conf.getUsu().trim());
                values.put("guardia_de_salida", "0");
                values.put("cajon", "N/A");
                values.put("personas", Pasajeros.getSelectedItem().toString());
                values.put("placas", Placas.getText().toString().trim());
                values.put("descripcion_transporte", "");
                values.put("foto1", f1);
                values.put("foto2", f2);
                values.put("foto3", f3);
                values.put("comentarios_salida_tardia", "");
                values.put("estatus", 1);
                values.put("sqliteEstatus", 1);

                Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_DTL_ENTRADAS_SALIDAS, values);

                String idUri = uri.getLastPathSegment();

                int insertar = Integer.parseInt(idUri);

                if (insertar != -1) {

                    int actualizar;

                    try {
                        ContentValues values2 = new ContentValues();
                        values2.put("comentarios", Comentarios.getText().toString().trim());
                        values2.put("sqliteEstatus", 2);

                        actualizar = getContentResolver().update(UrisContentProvider.URI_CONTENIDO_VISITA, values2, "id = " + ja1.getString(0).trim(), null);

                        if (actualizar != -1) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
                            alertDialogBuilder.setTitle("Alerta");
                            alertDialogBuilder
                                    .setMessage("Entrada de visita exitosa en modo offline")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                            startActivity(i);
                                            finish();

                                        }
                                    }).create().show();


                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
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


                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
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
                Log.e(TAG, "ERROR: " + ex.toString());
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

    public void Registrar() {

        if ((Placas.getText().toString().equals("") && si.isChecked() && !Global.getFotoPlaca()) || (Global.getFotoPlaca() && editTextPlacasPorFoto.getText().toString().equals(""))) {
            pd.dismiss();
            botonPresionado(1);

            Toast.makeText(getApplicationContext(), "Campo de placas", Toast.LENGTH_SHORT).show();
        } else if ((Placas.getText().toString().equals(" ") && si.isChecked() && !Global.getFotoPlaca()) || (Global.getFotoPlaca() && editTextPlacasPorFoto.getText().toString().equals(" "))) {
            pd.dismiss();
            botonPresionado(1);

            Toast.makeText(getApplicationContext(), "Campo de placas ", Toast.LENGTH_SHORT).show();
        } else if ((Placas.getText().toString().equals("N/A") && si.isChecked() && !Global.getFotoPlaca()) || (Global.getFotoPlaca() && editTextPlacasPorFoto.getText().toString().equals("N/A"))) {
            pd.dismiss();
            botonPresionado(1);

            Toast.makeText(getApplicationContext(), "Campo de placas", Toast.LENGTH_SHORT).show();
        } else {

            String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_php5_2.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {


                    if (response.equals("error")) {
                        pd.dismiss();
                        botonPresionado(1);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
                        alertDialogBuilder.setTitle("Alerta");
                        alertDialogBuilder
                                .setMessage("Visita No Exitosa")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getApplicationContext(), "Visita No Registrada", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }).create().show();


                    } else {

                        try {

                            if (Global_info.getCantidadFotosEnEsperaEnSegundoPlano(AccesosActivity.this) >= Global_info.getLimiteFotosSegundoPlano()){
                                if (ja6.getString(0).equals("0") || ja6.getString(3).equals("0")) {
                                    //Terminar();
                                } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("0") && ja6.getString(7).equals("0")) {
                                    upload1();
                                    //Terminar();

                                } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("0")) {
                                    upload1();
                                    upload2();

                                    //Terminar();

                                } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")) {
                                    upload1();
                                    upload2();
                                    upload3();

                                    //Terminar();
                                }

                                if (!nombreImagenPlaca.isEmpty()) {
                                    upload4();
                                }

                            }else {
                                if (ja6.getString(0).equals("0") || ja6.getString(3).equals("0")) {

                                    //Terminar();
                                } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("0") && ja6.getString(7).equals("0")) {
                                    ContentValues val_img1 = ValuesImagen(nombreImagen1, Conf.getPin() + "/caseta/" + nombreImagen1.trim(), rutaImagen1);
                                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                                    //Terminar();

                                } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("0")) {
                                    ContentValues val_img1 = ValuesImagen(nombreImagen1, Conf.getPin() + "/caseta/" + nombreImagen1.trim(), rutaImagen1);
                                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                                    ContentValues val_img2 = ValuesImagen(nombreImagen2, Conf.getPin() + "/caseta/" + nombreImagen2.trim(), rutaImagen2);
                                    Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);

                                    //Terminar();

                                } else if (ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")) {
                                    ContentValues val_img1 = ValuesImagen(nombreImagen1, Conf.getPin() + "/caseta/" + nombreImagen1.trim(), rutaImagen1);
                                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                                    ContentValues val_img2 = ValuesImagen(nombreImagen2, Conf.getPin() + "/caseta/" + nombreImagen2.trim(), rutaImagen2);
                                    Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);

                                    ContentValues val_img3 = ValuesImagen(nombreImagen3, Conf.getPin() + "/caseta/" + nombreImagen3.trim(), rutaImagen3);
                                    Uri uri3 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img3);

                                    //Terminar();
                                }

                                if (!nombreImagenPlaca.isEmpty()) {
                                    ContentValues val_img1 = ValuesImagen(nombreImagenPlaca, Conf.getPin() + "/caseta/" + nombreImagenPlaca.trim(), rutaImagenPlaca);
                                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Terminar();

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", "Error: " + error.toString());
                    botonPresionado(1);
                    alertaErrorAlRegistrar("Error al registrar visita \n\nNo se ha podido establecer comunicación con el servidor, inténtelo de nuevo");
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    try {

                        String placas = "";

                        if (Global.getFotoPlaca()) {
                            placas = editTextPlacasPorFoto.getText().toString().trim();
                        } else {
                            placas = Placas.getText().toString().trim();
                        }

                        params.put("id_residencial", Conf.getResid().trim());
                        params.put("id_visita", ja1.getString(0).trim());
                        params.put("guardia_de_entrada", Conf.getUsu().trim());
                        params.put("pasajeros", Pasajeros.getSelectedItem().toString());
                        params.put("placas", placas);
                        params.put("foto1", nombreImagen1);
                        params.put("foto2", nombreImagen2);
                        params.put("foto3", nombreImagen3);
                        params.put("foto4", nombreImagenPlaca);
                        params.put("usuario", ja2.getString(1).trim() + " " + ja2.getString(2).trim() + " " + ja2.getString(3).trim());
                        params.put("token", ja2.getString(5).trim());
                        params.put("correo", ja2.getString(6).trim());
                        params.put("visita", ja1.getString(7).trim());
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
        mountainImagesRef = storageReference.child(Conf.getPin() + "/caseta/" + nombreImagen1);

        Uri uri  = Uri.fromFile(new File(rutaImagen1));
        UploadTask uploadTask = mountainImagesRef.putFile(uri);

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
                Toast.makeText(AccesosActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
                pd2.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                eliminarFotoDirectorioLocal(nombreImagen1);
                pd2.dismiss();

            }
        });
    }

    public void upload2() {

        StorageReference mountainImagesRef2 = null;
        mountainImagesRef2 = storageReference.child(Conf.getPin() + "/caseta/" + nombreImagen2);

        Uri uri  = Uri.fromFile(new File(rutaImagen2));
        UploadTask uploadTask = mountainImagesRef2.putFile(uri);


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
                Toast.makeText(AccesosActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
                pd3.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                eliminarFotoDirectorioLocal(nombreImagen2);
                pd3.dismiss();
            }
        });


    }

    public void upload3() {

        StorageReference mountainImagesRef3 = null;
        mountainImagesRef3 = storageReference.child(Conf.getPin() + "/caseta/" + nombreImagen3);

        Uri uri  = Uri.fromFile(new File(rutaImagen3));
        UploadTask uploadTask = mountainImagesRef3.putFile(uri);

        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //System.out.println("Upload is " + progress + "% done");
                //Toast.makeText(getApplicationContext(),"Cargando Imagen PLACA " + progress + "%", Toast.LENGTH_SHORT).show();
                pd4.show();
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(AccesoActivity.this,"Pausado",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(AccesosActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
                pd4.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                eliminarFotoDirectorioLocal(nombreImagen3);
                pd4.dismiss();

            }
        });
    }

    public void upload4() {

        StorageReference mountainImagesRef3 = null;
        mountainImagesRef3 = storageReference.child(Conf.getPin() + "/caseta/" + nombreImagenPlaca);

        Uri uri = Uri.fromFile(new File(rutaImagenPlaca));
        UploadTask uploadTask = mountainImagesRef3.putFile(uri);

        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //System.out.println("Upload is " + progress + "% done");
                //Toast.makeText(getApplicationContext(),"Cargando Imagen PLACA " + progress + "%", Toast.LENGTH_SHORT).show();
                pd5.show();
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(AccesoActivity.this,"Pausado",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(AccesosActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
                pd5.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                eliminarFotoDirectorioLocal(nombreImagenPlaca);
                pd5.dismiss();

            }
        });
    }

    public void eliminarFotoDirectorioLocal(String nombreFoto){
        String tempfilepath ="";
        File externalFilesDir = getExternalFilesDir(null);
        if (externalFilesDir != null) {
            tempfilepath = externalFilesDir.getAbsolutePath();
            Log.e(TAG, tempfilepath);
            try {
                File grTempFiles = new File(tempfilepath);
                if (grTempFiles.exists()) {
                    File[] files = grTempFiles.listFiles();
                    if (grTempFiles.isDirectory() && files != null) {
                        int numofFiles = files.length;

                        for (int i = 0; i < numofFiles; i++) {
                            try {
                                File path = new File(files[i].getAbsolutePath());
                                if (!path.isDirectory() && path.getName().equals(nombreFoto)) {
                                    path.delete();
                                }
                            }catch (Exception e){
                                Log.e(TAG, e.toString());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("ErrorFile", "deleteDirectory: Failed to onCreate directory  " + tempfilepath + " for an unknown reason.");

            }

        }else {
        }
    }


    public void Terminar() {

        if (Global_info.getCantidadFotosEnEsperaEnSegundoPlano(AccesosActivity.this) > 0){
            if (!servicioFotos()) {
                Intent cargarFotos = new Intent(AccesosActivity.this, subirFotos.class);
                startService(cargarFotos);
            }
        }

        pd.dismiss();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("Entrada de Visita Exitosa")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*if (!Offline){
                            //Solo ejecutar si el servicio no se esta ejecutando
                            if (!servicioFotos()) {
                                Intent cargarFotos = new Intent(AccesosActivity.this, subirFotos.class);
                                startService(cargarFotos);
                            }
                        }*/

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

    public void botonPresionado(int estado){
        //estado --> 0=presionado   1=restablecer

        Button button = reg1;

        switch (btnRegistrarPresionado){
            case 1:
                button = reg1;
                break;
            case 2:
                button = reg2;
                break;
            case 3:
                button = reg3;
                break;
            case 4:
                button = reg4;
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage(texto)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
        startActivity(intent);
        finish();
    }
}
