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
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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


public class AccesoRegistroActivity extends mx.linkom.caseta_grupokap.Menu {
    Configuracion Conf;
    RadioGroup rdgGrupo,rdgGrupo2;
    RadioButton visi,prove,taxi,si,no;
    String valor;
    JSONArray ja1,ja2,ja3,ja4,ja5;
    EditText Nombre,Placas, editTextPlacasPorFoto;
    Spinner Calle,Pasajeros,Numero;
    ArrayList<String>calles,names,numero;
    Date FechaA;
    String FechaC,f1,f2,f3;
    LinearLayout espacio1,espacio2,espacio3,espacio4,espacio5,espacio6,espacio7,espacio8,espacio9,espacio10;
    LinearLayout registrar1,registrar2,registrar3,registrar4;
    Button reg1,reg2,reg3,reg4,btn_foto1,btn_foto2,btn_foto3;
    LinearLayout Foto1View,Foto2View,Foto3View;
    LinearLayout Foto1, Foto2, Foto3, CPlacasTexto, LinLayPlacasTextoPorFoto, CPlacasTexto2, LinLayEspacioPlacasCono;
    ImageView view1,view2,view3;
    TextView nombre_foto1,nombre_foto2,nombre_foto3,dato;
    EditText Comentarios;

    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap,bitmap2,bitmap3;
    ProgressDialog pd, pd2, pd3, pd4, pd5;
    int foto;
    Uri uri_img,uri_img2,uri_img3;
    LinearLayout Numero_o;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accesosregistro);

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        Comentarios = (EditText)findViewById(R.id.setComentarios);

        reg1 = (Button) findViewById(R.id.reg1);
        reg2 = (Button) findViewById(R.id.reg2);
        reg3 = (Button) findViewById(R.id.reg3);
        reg4 = (Button) findViewById(R.id.reg4);
        btn_foto1 = (Button) findViewById(R.id.btn_foto1);
        btn_foto2 = (Button) findViewById(R.id.btn_foto2);
        btn_foto3 = (Button) findViewById(R.id.btn_foto3);

        dato = (TextView) findViewById(R.id.placas_texto);
        nombre_foto1 = (TextView) findViewById(R.id.nombre_foto1);
        nombre_foto2 = (TextView) findViewById(R.id.nombre_foto2);
        nombre_foto3 = (TextView) findViewById(R.id.nombre_foto3);

        view1 = (ImageView) findViewById(R.id.view1);
        view2 = (ImageView) findViewById(R.id.view2);
        view3 = (ImageView) findViewById(R.id.view3);

        CPlacasTexto = (LinearLayout) findViewById(R.id.CPlacasTexto);
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

        Conf = new Configuracion(this);
        calles = new ArrayList<String>();
        names = new ArrayList<String>();
        numero = new ArrayList<String>();
        rdgGrupo = (RadioGroup)findViewById(R.id.rdgGrupo);
        rdgGrupo2 = (RadioGroup)findViewById(R.id.rdgGrupo2);
        visi = (RadioButton)findViewById(R.id.Visita);
        prove = (RadioButton)findViewById(R.id.Proveedor);
        taxi = (RadioButton)findViewById(R.id.Taxista);
        si = (RadioButton)findViewById(R.id.Si);
        no = (RadioButton)findViewById(R.id.No);
        Nombre = (EditText)findViewById(R.id.setNombre);
        Numero = (Spinner)findViewById(R.id.setNumero);
        Numero_o = (LinearLayout) findViewById(R.id.numero);
        Numero_o.setVisibility(View.GONE);
        Placas = (EditText)findViewById(R.id.setPlacas);


        Calle = (Spinner)findViewById(R.id.setCalle);
        Pasajeros = (Spinner)findViewById(R.id.setPasajeros);

        CPlacasTexto = (LinearLayout) findViewById(R.id.CPlacasTexto);
        LinLayPlacasTextoPorFoto = (LinearLayout) findViewById(R.id.LinLayPlacasTextoPorFoto);
        CPlacasTexto2 = (LinearLayout) findViewById(R.id.CPlacasTexto2);
        LinLayEspacioPlacasCono = (LinearLayout) findViewById(R.id.LinLayEspacioPlacasCono);
        editTextPlacasPorFoto = (EditText) findViewById(R.id.setPlacasPorFoto);

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

        /*iconoInternet = (ImageView) findViewById(R.id.iconoInternetAccesosRegistro);

        if (Global_info.getINTERNET().equals("Si")){
            iconoInternet.setImageResource(R.drawable.ic_online);
            Offline = false;
        }else {
            iconoInternet.setImageResource(R.drawable.ic_offline);
            Offline = true;
        }

        iconoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Offline){
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccesoRegistroActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOffline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccesoRegistroActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOnline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
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

        if (Global.getFotoPlaca() && btnFotoPlacaFuePresionado != null) { //Esta activa la opcion de foto placa y viene de buscar la placa
            editTextPlacasPorFoto.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps() {
            }});
            editTextPlacasPorFoto.setText(Conf.getPlacas().trim());
            Placas.setText(Conf.getPlacas().trim());

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
            }
        } else {
            CPlacasTexto.setVisibility(View.VISIBLE);
            Placas.setText(Conf.getPlacas().trim());
            Placas.setFilters(new InputFilter[]{filter, new InputFilter.AllCaps() {
            }});
        }

        try {
            objectDetectorClass = new objectDetectorClass(getAssets(), "detectPlacaLKM.tflite", "labelmapTf.txt", 320);
            Log.e("AccesoRegistro", "Modelo cargado correctamente");
            modeloCargado = true;
        } catch (IOException e) {
            modeloCargado = false;
            Log.e("AccesoRegistro", "Error al cargar modelo");
        }

        cargarSpinner2();
        calles();
        menu();

        /*if (Offline){
            callesOffline();
            menuOffline();
        }else {
            calles();
            menu();
        }*/

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

        btn_fotoPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFotoPlacaOffline();
            }
        });

        btn_foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=1;
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
                foto=2;
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
                foto=3;
                imgFoto3Offline();
                /*if (Offline){
                    imgFoto3Offline();
                }else {
                    imgFoto3();
                }*/
            }
        });
        Placas.setText(Conf.getPlacas().trim());
        Placas.setFilters(new InputFilter[] { filter,new InputFilter.AllCaps() {
        } });
        CPlacasTexto.setVisibility(View.VISIBLE);

        if(Conf.getTipoReg().equals("Nada")){
            dato.setText("Placas / Cono / Gafete / Credencial:");
            si.setChecked(true);
        }else if(Conf.getTipoReg().equals("Auto")) {
            dato.setText("Placas / Cono:");
            si.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
            si.setChecked(true);
        }else if(Conf.getTipoReg().equals("Peatonal")){
            dato.setText("Gafete / Credencial:");
        }

        Numero_o.setVisibility(View.VISIBLE);
        cargarSpinner4();

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

    String numero_aletorio2=prime2+segundo2[numRandonsegun2]+tercer2;



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

    
    public void cargarSpinner2(){

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

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,names);
        Pasajeros.setAdapter(adapter1);


    }

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

    //FOTOS
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
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

    public void imgFotoOffline(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto=null;
            try {
                nombreImagen1 = "app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
                foto= new File(getApplication().getExternalFilesDir(null),nombreImagen1);
                rutaImagen1 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
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


     public void imgFoto(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto=null;
            try {
                foto= new File(getApplication().getExternalFilesDir(null),"accesosRegistro1.png");
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
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

    public void imgFoto2Offline(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {
            File foto=null;
            try {
                nombreImagen2 = "app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png";
                foto = new File(getApplication().getExternalFilesDir(null),nombreImagen2);
                rutaImagen2 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
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

    public void imgFoto2(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {
            File foto=null;
            try {
                foto = new File(getApplication().getExternalFilesDir(null),"accesosRegistro2.png");
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
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

    public void imgFoto3Offline(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto=null;
            try {
                nombreImagen3 = "app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio3+".png";
                foto = new File(getApplication().getExternalFilesDir(null),nombreImagen3);
                rutaImagen3 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
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

    public void imgFoto3(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto=null;
            try {
                foto = new File(getApplication().getExternalFilesDir(null),"accesosRegistro3.png");
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
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


                Bitmap bitmap;

                bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen1);

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


                /*if (Offline){
                    bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png");
                }else {
                    bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/accesosRegistro1.png");
                }*/

                Foto1View.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                view1.setImageBitmap(bitmap);
                espacio3.setVisibility(View.VISIBLE);


                try {
                    if(ja3.getString(3).equals("1") && ja3.getString(5).equals("0") && ja3.getString(7).equals("0")) {
                        registrar2.setVisibility(View.VISIBLE);
                        reg2.setVisibility(View.VISIBLE);
                        espacio4.setVisibility(View.VISIBLE);
                        espacio5.setVisibility(View.VISIBLE);
                    }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("0")){
                        registrar2.setVisibility(View.GONE);
                        Foto2.setVisibility(View.VISIBLE);
                        espacio5.setVisibility(View.VISIBLE);
                        espacio6.setVisibility(View.VISIBLE);
                        nombre_foto2.setVisibility(View.VISIBLE);
                        nombre_foto2.setText(ja3.getString(6)+":");
                    }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("1")) {
                        registrar2.setVisibility(View.GONE);
                        Foto2.setVisibility(View.VISIBLE);
                        espacio5.setVisibility(View.VISIBLE);
                        espacio6.setVisibility(View.VISIBLE);
                        nombre_foto2.setVisibility(View.VISIBLE);
                        nombre_foto2.setText(ja3.getString(6)+":");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (requestCode == 1) {


                Bitmap bitmap2;
                bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen2);

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

                /*if (Offline){
                    bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png");
                }else {
                    bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/accesosRegistro2.png");
                }*/

                Foto2View.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view2.setImageBitmap(bitmap2);
                espacio6.setVisibility(View.VISIBLE);



                try {
                    if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("0")) {
                        registrar3.setVisibility(View.VISIBLE);
                        reg3.setVisibility(View.VISIBLE);
                        espacio7.setVisibility(View.VISIBLE);
                    }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("1")){
                        registrar3.setVisibility(View.GONE);
                        espacio7.setVisibility(View.VISIBLE);
                        espacio8.setVisibility(View.VISIBLE);
                        Foto3.setVisibility(View.VISIBLE);
                        espacio9.setVisibility(View.VISIBLE);
                        nombre_foto3.setVisibility(View.VISIBLE);
                        nombre_foto3.setText(ja3.getString(8)+":");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (requestCode == 2) {


                Bitmap bitmap3;
                bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen3);

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

                /*if (Offline){
                    bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio3+".png");
                }else {
                    bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/accesosRegistro3.png");
                }*/

                Foto3View.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view3.setImageBitmap(bitmap3);
                espacio10.setVisibility(View.VISIBLE);



                try {
                    if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("1")){
                        registrar4.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            if (requestCode == 3) {

                Bitmap bitmap4 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/" + nombreImagenPlaca);

                if (modeloCargado) {
                    String txtPlaca = DetectarPlaca.getTextFromImage(DetectarPlaca.reconocerPlaca(bitmap4, objectDetectorClass, 1), AccesoRegistroActivity.this);
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
                espacioPlaca.setVisibility(View.VISIBLE);

                try {
                    if (ja3.getString(3).equals("1")){
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

    public void callesOffline(){

        try {
            String id_residencial = Conf.getResid().trim();
            String parametros[] = {id_residencial};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "calles", parametros, null);

            if (cursor.moveToFirst()){
                ja1 = new JSONArray();
                do {
                    ja1.put(cursor.getString(0));
                }while (cursor.moveToNext());

                cargarSpinner();
            }else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al obtener calles")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
            cursor.close();
        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }

    }

    public void calles(){

            String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_1.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    response = response.replace("][",",");
                    if (response.length()>0){
                        try {
                            ja1 = new JSONArray(response);
                            cargarSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                    params.put("id_residencial", Conf.getResid().trim());
                    return params;
                }
            };
            requestQueue.add(stringRequest);
    }


    public void cargarSpinner(){


        try{
            calles.add("Seleccionar..");
            calles.add("Seleccionar...");

            for (int i=0;i<ja1.length();i+=1){
                calles.add(ja1.getString(i+0));
            }

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,calles);
            Calle.setAdapter(adapter1);
            Calle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(Calle.getSelectedItem().equals("Seleccionar..")){
                        calles.remove(0);
                    }else if(Calle.getSelectedItem().equals("Seleccionar...")){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                        alertDialogBuilder.setTitle("Alerta");
                        alertDialogBuilder
                                .setMessage("No selecciono ninguna calle...")
                                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                }).create().show();
                    }
                    else{
                        numero.clear();
                        numeros(Calle.getSelectedItem().toString());

                        /*if (Offline){
                            numerosOffline(Calle.getSelectedItem().toString());
                        }else {
                            numeros(Calle.getSelectedItem().toString());
                        }*/
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void menuOffline() {
        Log.e("info", "menu offline");
        try {
            Cursor cursoAppCaseta = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_APP_CASETA, null, null, null);

            ja2 = new JSONArray();

            if (cursoAppCaseta.moveToFirst()){
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

            }else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al obtener datos")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
            cursoAppCaseta.close();

        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public void menu() {
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/menu.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
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

            if (cursoAppCaseta.moveToFirst()){
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
            }else {
                int $arreglo[]={0};
                try {
                    ja3 = new JSONArray($arreglo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imagenes();
            }
            cursoAppCaseta.close();

        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public void submenu(final String id_app) {
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/menu_3.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if(response.equals("error")){
                    int $arreglo[]={0};
                    try {
                        ja3 = new JSONArray($arreglo);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    imagenes();

                }else {
                    response = response.replace("][", ",");
                    if (response.length() > 0) {
                        try {
                            ja3 = new JSONArray(response);

                            imagenes();

                            if (ja3.getString(10).trim().equals("1")){
                                Global.setFotoPlaca(true);
                            }else {
                                Global.setFotoPlaca(false);
                            }

                            //OCULTAR VIEW DE FOTO PLACA
                            if (ja3.getString(3).equals("0") && (ja3.getString(10).trim().equals("1") && !Conf.getTipoReg().equals("Peatonal")) && rutaImagenPlaca != null){
                                try {
                                    Log.e("ja3", "1");
                                    if (ja3.getString(3).equals("1")){
                                        Log.e("ja3", "2");
                                        Foto1.setVisibility(View.VISIBLE);
                                        espacio2.setVisibility(View.VISIBLE);
                                        nombre_foto1.setVisibility(View.VISIBLE);
                                    }else {
                                        Log.e("ja3", "3");
                                        if (!rutaImagenPlaca.isEmpty()){
                                            Log.e("ja3", "4");
                                            registrar1.setVisibility(View.VISIBLE);
                                            espacio1.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else if ((!rutaImagenPlaca.isEmpty() && ja3.getString(3).equals("0")) || (Conf.getTipoReg().equals("Peatonal") && ja3.getString(3).equals("0"))){
                                Log.e("ja3", "5");
                                registrar1.setVisibility(View.VISIBLE);
                                espacio1.setVisibility(View.VISIBLE);
                            }

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


    public void imagenes(){
        try {

            if (ja3.getString(10).trim().equals("1") && !Conf.getTipoReg().equals("Peatonal")) {
                Global.setFotoPlaca(true);
                espacio1Placa.setVisibility(View.VISIBLE);
                FotoPlaca.setVisibility(View.VISIBLE);
                nombre_fotoPlaca.setVisibility(View.VISIBLE);
                btn_fotoPlaca.setVisibility(View.VISIBLE);
                viewPlaca.setVisibility(View.VISIBLE);
                espacio2Placa.setVisibility(View.VISIBLE);

                nombre_fotoPlaca.setText(ja3.getString(11) + ":");

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

            if(ja3.getString(0).equals("0") || ja3.getString(3).equals("0")) {

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
                registrar4.setVisibility(View.GONE);
                espacio10.setVisibility(View.GONE);


            }else if(ja3.getString(3).equals("1")){

                registrar1.setVisibility(View.GONE);
                espacio1.setVisibility(View.GONE);

                if (ja3.getString(10).trim().equals("0") || (ja3.getString(10).trim().equals("1") && Conf.getTipoReg().equals("Peatonal"))) {
                    Foto1.setVisibility(View.VISIBLE);
                    espacio2.setVisibility(View.VISIBLE);
                    nombre_foto1.setVisibility(View.VISIBLE);
                } else if (ja3.getString(10).trim().equals("1")) {
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

                nombre_foto1.setText(ja3.getString(4) + ":");

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

    public void Verificar() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿ Desea registrar la entrada ?")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        pd.show();
                        busqueda();

                        /*if (Offline){
                            busquedaOffline();
                        }else {
                            pd.show();
                            busqueda();
                        }*/
                    }
                }).create().show();
    }

    public void numerosOffline(final String IdUsu){

        try {
            String id_residencial = Conf.getResid().trim();
            String calle = IdUsu;

            String parametros[] = {calle, id_residencial};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "numeros", parametros, null);

            if (cursor.moveToFirst()){
                ja2 = new JSONArray();
                do {
                    ja2.put(cursor.getString(0));
                }while (cursor.moveToNext());

                cargarSpinner3();
            }else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al obtener numeros de calles")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
            cursor.close();
        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }
    }

    public void numeros(final String IdUsu){

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_9.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        ja2 = new JSONArray(response);
                        cargarSpinner3();
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                params.put("id_residencial", Conf.getResid().trim());
                params.put("calle", IdUsu);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void cargarSpinner3(){

        numero.add("Seleccionar...");

        try{
            for (int i=0;i<ja2.length();i+=1){
                numero.add(ja2.getString(i+0));
            }

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,numero);
            Numero.setAdapter(adapter1);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cargarSpinner4() {

        numero.add("Seleccionar...");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,numero);
        Numero.setAdapter(adapter1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void busquedaOffline(){

        if(Calle.getSelectedItem().equals("Seleccionar..") || Calle.getSelectedItem().equals("Seleccionar...") || Numero.getSelectedItem().equals("Seleccionar...")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
            alertDialogBuilder.setTitle("Alerta");
            alertDialogBuilder
                    .setMessage("No selecciono ninguna calle o número...")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).create().show();
        }else {

            try {
                String id_residencial = Conf.getResid().trim();
                String numero = Numero.getSelectedItem().toString();
                String calle = Calle.getSelectedItem().toString();

                String parametros[] = {id_residencial, calle, numero};

                Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "verificaUP", parametros, null);

                if (cursor.moveToFirst()){
                    int contador = 0;
                    do {
                        contador++;
                    }while (cursor.moveToNext());

                    Log.e("INFO ", "Valor de contador: "+contador);

                    if (contador > 2){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                        alertDialogBuilder.setTitle("Alerta");
                        alertDialogBuilder
                                .setMessage("UP no encontrada en modo offline")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                }).create().show();
                    }else if (contador == 1){
                        ja4 = new JSONArray();
                        if (cursor.moveToFirst()){
                            ja4.put(cursor.getString(0));
                            ja4.put(cursor.getString(1));
                            ja4.put(cursor.getString(2));
                            ja4.put(cursor.getString(3));
                            ja4.put(cursor.getString(4));
                            ja4.put(cursor.getString(5));
                            registroOffline();
                        }
                    }else if (contador == 2){
                        try {
                            Cursor cursor1 = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "verificaUP2", parametros, null);
                            if (cursor1.moveToFirst()){
                                ja4 = new JSONArray();
                                ja4.put(cursor1.getString(0));
                                ja4.put(cursor1.getString(1));
                                ja4.put(cursor1.getString(2));
                                ja4.put(cursor1.getString(3));
                                ja4.put(cursor1.getString(4));
                                ja4.put(cursor1.getString(5));
                                registroOffline();
                            }else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                                alertDialogBuilder.setTitle("Alerta");
                                alertDialogBuilder
                                        .setMessage("UP no encontrada en modo offline")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        }).create().show();
                            }
                        }catch (Exception ex){
                            Log.e("Exception", ex.toString());
                        }
                    }

                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("UP no encontrada en modo offline")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            }).create().show();
                }

                cursor.close();

            }catch (Exception ex){
                Log.e("Exception", ex.toString());
            }
        }
    }

    public void busqueda(){

        if(Calle.getSelectedItem().equals("Seleccionar..") || Calle.getSelectedItem().equals("Seleccionar...") || Numero.getSelectedItem().equals("Seleccionar...")){
            pd.dismiss();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
            alertDialogBuilder.setTitle("Alerta");
            alertDialogBuilder
                    .setMessage("No selecciono ninguna calle o número...")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).create().show();
        }else {

            String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_2.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    if (response.equals("error")) {
                        pd.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
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
                    pd.dismiss();
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
    public void registroOffline(){

        if(Placas.getText().toString().equals("") && si.isChecked()){
            Toast.makeText(getApplicationContext(),"Campo de placas", Toast.LENGTH_SHORT).show();
        }else if(Placas.getText().toString().equals(" ") && si.isChecked()){
            Toast.makeText(getApplicationContext(),"Campo de placas ", Toast.LENGTH_SHORT).show();
        }else if( Placas.getText().toString().equals("N/A") && si.isChecked() ){
            Toast.makeText(getApplicationContext(),"Campo de placas", Toast.LENGTH_SHORT).show();
        }else{
            try {

                if(ja3.getString(0).equals("0") || ja3.getString(3).equals("0")) {
                    f1="";
                    f2="";
                    f3="";
                }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("0") && ja3.getString(7).equals("0")){
                    f1="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
                    f2="";
                    f3="";

                    ContentValues val_img1 =  ValuesImagen(f1, Conf.getPin()+"/caseta/"+f1.trim(), rutaImagen1);
                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("0")){
                    f1="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
                    f2="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png";
                    f3="";

                    ContentValues val_img1 =  ValuesImagen(f1, Conf.getPin()+"/caseta/"+f1.trim(), rutaImagen1);
                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                    ContentValues val_img2 =  ValuesImagen(f2, Conf.getPin()+"/caseta/"+f2.trim(), rutaImagen2);
                    Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);

                }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("1")){
                    f1="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
                    f2="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png";
                    f3="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio3+".png";

                    ContentValues val_img1 =  ValuesImagen(f1, Conf.getPin()+"/caseta/"+f1.trim(), rutaImagen1);
                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                    ContentValues val_img2 =  ValuesImagen(f2, Conf.getPin()+"/caseta/"+f2.trim(), rutaImagen2);
                    Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);

                    ContentValues val_img3 =  ValuesImagen(f3, Conf.getPin()+"/caseta/"+f3.trim(), rutaImagen3);
                    Uri uri3 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img3);
                }

                LocalDateTime hoy = LocalDateTime.now();

                int year = hoy.getYear();
                int month = hoy.getMonthValue();
                int day = hoy.getDayOfMonth();
                int hour = hoy.getHour();
                int minute = hoy.getMinute();
                int second =hoy.getSecond();

                String fecha = "";

                //Poner el cero cuando el mes o dia es menor a 10
                if (day < 10 || month < 10){
                    if (month < 10 && day >= 10){
                        fecha = year+"-0"+month+"-"+day;
                    } else if (month >= 10 && day < 10){
                        fecha = year+"-"+month+"-0"+day;
                    }else if (month < 10 && day < 10){
                        fecha = year+"-0"+month+"-0"+day;
                    }
                }else {
                    fecha = year+"-"+month+"-"+day;
                }

                String hora = "";

                if (hour < 10 || minute < 10){
                    if (hour < 10 && minute >=10){
                        hora = "0"+hour+":"+minute;
                    }else if (hour >= 10 && minute < 10){
                        hora = hour+":0"+minute;
                    }else if (hour < 10 && minute < 10){
                        hora = "0"+hour+":0"+minute;
                    }
                }else {
                    hora = hour+":"+minute;
                }

                String segundos = "00";

                if (second < 10){
                    segundos = "0"+second;
                }else {
                    segundos = ""+second;
                }

                if(visi.isChecked()){
                    valor="1";
                }else if(taxi.isChecked()){
                    valor="3";
                }else if(prove.isChecked()) {
                    valor="2";
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
                values.put("fecha_entrada", fecha+" "+hora+":"+segundos);
                values.put("fecha_salida", "0000-00-00 00:00:00");
                values.put("codigo_qr", "");
                values.put("fecha_registro", fecha);
                values.put("club", "0");
                values.put("estatus", 1);
                values.put("sqliteEstatus", 1);

                Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_VISITA,values);

                String idUri = uri.getLastPathSegment();

                int insertar = Integer.parseInt(idUri);

                if (insertar != -1){ //Registrar Codigo qr
                    int actualizar;

                    try {

                        ContentValues values2 = new ContentValues();
                        values2.put("codigo_qr", insertar+"-"+numero_aletorio);

                        actualizar = getContentResolver().update(UrisContentProvider.URI_CONTENIDO_VISITA, values2, "id = "+ insertar, null);

                        if (actualizar != -1){//Registrar dtl entradas salidas

                            ContentValues values3 = new ContentValues();
                            values3.put("id_residencial", Conf.getResid().trim());
                            values3.put("id_visita", insertar);
                            values3.put("entrada_real", fecha+" "+hora+":"+segundos);
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

                            Uri uri3 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_DTL_ENTRADAS_SALIDAS,values3);

                            String idUri3 = uri3.getLastPathSegment();

                            int insertar3 = Integer.parseInt(idUri3);

                            if (insertar3 != -1){
                                Conf.setQR(insertar+"-"+numero_aletorio);

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                                alertDialogBuilder.setTitle("Alerta");
                                alertDialogBuilder
                                        .setMessage("Entrada de visita exitosa en modo offline")
                                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if(Integer.parseInt(Conf.getTicketE())==1){
                                                    Imprimir();
                                                }else {
                                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }
                                        }).create().show();
                            }else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                                alertDialogBuilder.setTitle("Alerta");
                                alertDialogBuilder
                                        .setMessage("Visita no exitosa en modo offline")
                                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Toast.makeText(getApplicationContext(),"Visita No Registrada", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }).create().show();
                            }
                        }else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                            alertDialogBuilder.setTitle("Alerta");
                            alertDialogBuilder
                                    .setMessage("Visita no exitosa en modo offline")
                                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(getApplicationContext(),"Visita No Registrada", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }).create().show();
                        }
                    }catch (Exception ex){
                        Log.e("ExceptionAct", ex.toString());
                    }
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Visita no exitosa en modo offline")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(getApplicationContext(),"Visita No Registrada", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }



            }catch (Exception ex){
                Log.e("Exception", ex.toString());
            }
        }
    }

    public ContentValues ValuesImagen(String nombre, String rutaFirebase, String rutaDispositivo){
        ContentValues values = new ContentValues();
        values.put("titulo", nombre);
        values.put("direccionFirebase", rutaFirebase);
        values.put("rutaDispositivo", rutaDispositivo);
        return values;
    }


    public void registro(){


        if ((Placas.getText().toString().equals("") && si.isChecked() && !Global.getFotoPlaca()) || (Global.getFotoPlaca() && editTextPlacasPorFoto.getText().toString().equals("") && !Conf.getTipoReg().equals("Peatonal"))) {
            pd.dismiss();

            Toast.makeText(getApplicationContext(), "Campo de placas", Toast.LENGTH_SHORT).show();
        } else if ((Placas.getText().toString().equals(" ") && si.isChecked() && !Global.getFotoPlaca()) || (Global.getFotoPlaca() && editTextPlacasPorFoto.getText().toString().equals(" "))) {
            pd.dismiss();

            Toast.makeText(getApplicationContext(), "Campo de placas ", Toast.LENGTH_SHORT).show();
        } else if ((Placas.getText().toString().equals("N/A") && si.isChecked() && !Global.getFotoPlaca()) || (Global.getFotoPlaca() && editTextPlacasPorFoto.getText().toString().equals("N/A"))) {
            pd.dismiss();

            Toast.makeText(getApplicationContext(), "Campo de placas", Toast.LENGTH_SHORT).show();
        }else{

            String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_3_2.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response){

                    if(response.equals("error")){
                        pd.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
                        alertDialogBuilder.setTitle("Alerta");
                        alertDialogBuilder
                                .setMessage("Visita No Exitosa")
                                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getApplicationContext(),"Visita No Registrada", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }).create().show();
                    }else {
                        Conf.setQR(response);
                        try {

                            if (Global_info.getCantidadFotosEnEsperaEnSegundoPlano(AccesoRegistroActivity.this) >= Global_info.getLimiteFotosSegundoPlano()){
                                if(ja3.getString(0).equals("0") || ja3.getString(3).equals("0")) {
                                    //Terminar();
                                }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("0") && ja3.getString(7).equals("0")){

                                    upload1();
                                }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("0")){

                                    upload1();
                                    upload2();
                                }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("1")){

                                    upload1();
                                    upload2();
                                    upload3();
                                }
                                if (!nombreImagenPlaca.isEmpty()) {
                                    upload4();
                                }
                            }else {
                                if(ja3.getString(0).equals("0") || ja3.getString(3).equals("0")) {
                                    //Terminar();
                                }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("0") && ja3.getString(7).equals("0")){

                                    ContentValues val_img1 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/caseta/"+nombreImagen1.trim(), rutaImagen1);
                                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                                    //Terminar();
                                }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("0")){

                                    ContentValues val_img1 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/caseta/"+nombreImagen1.trim(), rutaImagen1);
                                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                                    ContentValues val_img2 =  ValuesImagen(nombreImagen2, Conf.getPin()+"/caseta/"+nombreImagen2.trim(), rutaImagen2);
                                    Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);

                                    //Terminar();
                                }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("1")){

                                    ContentValues val_img1 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/caseta/"+nombreImagen1.trim(), rutaImagen1);
                                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                                    ContentValues val_img2 =  ValuesImagen(nombreImagen2, Conf.getPin()+"/caseta/"+nombreImagen2.trim(), rutaImagen2);
                                    Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);

                                    ContentValues val_img3 =  ValuesImagen(nombreImagen3, Conf.getPin()+"/caseta/"+nombreImagen3.trim(), rutaImagen3);
                                    Uri uri3 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img3);


                                }

                                if (!nombreImagenPlaca.isEmpty()) {
                                    ContentValues val_img1 = ValuesImagen(nombreImagenPlaca, Conf.getPin() + "/caseta/" + nombreImagenPlaca.trim(), rutaImagenPlaca);
                                    Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        pd.dismiss();
                        Terminar();

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

                    if(visi.isChecked()){
                        valor="1";
                    }else if(taxi.isChecked()){
                        valor="3";
                    }else if(prove.isChecked()) {
                        valor="2";
                    }


                    Map<String, String> params = new HashMap<>();
                    try {

                        String placas = "";

                        if (Global.getFotoPlaca() && !Conf.getTipoReg().equals("Peatonal")) {
                            placas = editTextPlacasPorFoto.getText().toString().trim();
                        } else {
                            placas = Placas.getText().toString().trim();
                        }

                        params.put("id_residencial", Conf.getResid().trim());
                        params.put("id_usuario", ja4.getString(0));
                        params.put("id_tipo",valor);
                        params.put("nombre", Nombre.getText().toString().trim());

                        params.put("placas", placas);
                        params.put("pasajeros", Pasajeros.getSelectedItem().toString());
                        params.put("guardia_de_entrada", Conf.getUsu().trim());

                        params.put("foto1", nombreImagen1);
                        params.put("foto2", nombreImagen2);
                        params.put("foto3", nombreImagen3);
                        params.put("foto4", nombreImagenPlaca);

                        params.put("usuario",ja4.getString(1).trim() + " " + ja4.getString(2).trim() + " " + ja4.getString(3).trim());
                        params.put("token", ja4.getString(5).trim());
                        params.put("correo",ja4.getString(4).trim());
                        params.put("nom_residencial",Conf.getNomResi().trim());
                        params.put("comentarios",Comentarios.getText().toString().trim());


                    } catch (JSONException e) {
                        Log.e("TAG","Error: " + e.toString());
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
                Toast.makeText(AccesoRegistroActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AccesoRegistroActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AccesoRegistroActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AccesoRegistroActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
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

        if (Global_info.getCantidadFotosEnEsperaEnSegundoPlano(AccesoRegistroActivity.this) > 0){
            if (!servicioFotos()){
                Intent cargarFotos = new Intent(AccesoRegistroActivity.this, subirFotos.class);
                startService(cargarFotos);
            }
        }

        pd.dismiss();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("Entrada de Visita Exitosa")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



                        /*if (!Offline){
                            //Solo ejecutar si el servicio no se esta ejecutando
                            if (!servicioFotos()){
                                Intent cargarFotos = new Intent(AccesoRegistroActivity.this, subirFotos.class);
                                startService(cargarFotos);
                            }
                        }*/

                        if(Integer.parseInt(Conf.getTicketE())==1){
                            Imprimir();
                        }else {
                            Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }).create().show();

    }

    public void Imprimir() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("Desea imprimir ticket?")
                .setPositiveButton("Si ",new DialogInterface.OnClickListener() {
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
        Intent intent = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
        startActivity(intent);
        finish();
    }


}