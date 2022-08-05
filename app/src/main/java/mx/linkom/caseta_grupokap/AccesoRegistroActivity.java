package mx.linkom.caseta_grupokap;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class AccesoRegistroActivity extends mx.linkom.caseta_grupokap.Menu {
    Configuracion Conf;
    RadioGroup rdgGrupo,rdgGrupo2;
    RadioButton visi,prove,taxi,si,no;
    String valor;
    JSONArray ja1,ja2,ja3,ja4,ja5;
    EditText Nombre,Placas;
    Spinner Calle,Pasajeros,Numero;
    ArrayList<String>calles,names,numero;
    Date FechaA;
    String FechaC,f1,f2,f3;
    LinearLayout espacio1,espacio2,espacio3,espacio4,espacio5,espacio6,espacio7,espacio8,espacio9,espacio10;
    LinearLayout registrar1,registrar2,registrar3,registrar4;
    Button reg1,reg2,reg3,reg4,btn_foto1,btn_foto2,btn_foto3;
    LinearLayout Foto1View,Foto2View,Foto3View;
    LinearLayout Foto1,Foto2,Foto3,CPlacasTexto;
    ImageView view1,view2,view3;
    TextView nombre_foto1,nombre_foto2,nombre_foto3,dato;

    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap,bitmap2,bitmap3;
    ProgressDialog pd,pd2,pd3;
    int foto;
    Uri uri_img,uri_img2,uri_img3;
    LinearLayout Numero_o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accesosregistro);

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();

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

        cargarSpinner2();
        calles();
        menu();

        pd= new ProgressDialog(this);
        pd.setMessage("Subiendo Imagen 1...");

        pd2= new ProgressDialog(this);
        pd2.setMessage("Subiendo Imagen 2...");

        pd3= new ProgressDialog(this);
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
                foto=1;
                imgFoto();
            }
        });

        btn_foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=2;
                imgFoto2();
            }
        });

        btn_foto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=3;
                imgFoto3();
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
                    CPlacasTexto.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.No:
                if (marcado) {
                    CPlacasTexto.setVisibility(View.GONE);

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
    int numRandonsegun = (int) Math.round(Math.random() * 26 ) ;

    Random tercero = new Random();
    int tercer= tercero.nextInt(9);

    String[] cuarto = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandoncuart = (int) Math.round(Math.random() * 26 ) ;

    String numero_aletorio=prime+segundo[numRandonsegun]+tercer+cuarto[numRandoncuart];


    //ALETORIO2

    Random primero2 = new Random();
    int prime2= primero2.nextInt(9);

    String[] segundo2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandonsegun2 = (int) Math.round(Math.random() * 26 ) ;

    Random tercero2 = new Random();
    int tercer2= tercero2.nextInt(9);

    String[] cuarto2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandoncuart2 = (int) Math.round(Math.random() * 26 ) ;

    String numero_aletorio2=prime2+segundo2[numRandonsegun2]+tercer2;



    //ALETORIO3

    Random primero3 = new Random();
    int prime3= primero3.nextInt(9);

    String[] segundo3 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandonsegun3 = (int) Math.round(Math.random() * 26 ) ;

    Random tercero3 = new Random();
    int tercer3= tercero3.nextInt(9);

    String[] cuarto3 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandoncuart3 = (int) Math.round(Math.random() * 26 ) ;

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

    //FOTOS
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {


                Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/accesosRegistro1.png");


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


                Bitmap bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/accesosRegistro2.png");

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


                Bitmap bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/accesosRegistro3.png");


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

    public void submenu(final String id_app) {
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/menu_2.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
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

                Foto1.setVisibility(View.VISIBLE);
                espacio2.setVisibility(View.VISIBLE);
                nombre_foto1.setVisibility(View.VISIBLE);
                nombre_foto1.setText(ja3.getString(4)+":");

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
                    public void onClick(DialogInterface dialog, int id) {
                       busqueda();
                    }
                }).create().show();
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

    public void busqueda(){

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

            String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_2.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    if (response.equals("error")) {

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
    public void registro(){


        if(Placas.getText().toString().equals("") && si.isChecked()){
            Toast.makeText(getApplicationContext(),"Campo de placas", Toast.LENGTH_SHORT).show();
        }else if(Placas.getText().toString().equals(" ") && si.isChecked()){
            Toast.makeText(getApplicationContext(),"Campo de placas ", Toast.LENGTH_SHORT).show();
        }else if( Placas.getText().toString().equals("N/A") && si.isChecked() ){
            Toast.makeText(getApplicationContext(),"Campo de placas", Toast.LENGTH_SHORT).show();
        }else{

            String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_3.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response){

                    if(response.equals("error")){
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
                            if(ja3.getString(0).equals("0") || ja3.getString(3).equals("0")) {
                                Terminar();
                            }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("0") && ja3.getString(7).equals("0")){
                                upload1();
                                Terminar();
                            }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("0")){
                                upload1();
                                upload2();
                                Terminar();
                            }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("1")){
                                upload1();
                                upload2();
                                upload3();
                                Terminar();
                            }
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

                    try {
                        if(ja3.getString(0).equals("0") || ja3.getString(3).equals("0")) {
                            f1="";
                            f2="";
                            f3="";
                        }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("0") && ja3.getString(7).equals("0")){
                            f1="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
                            f2="";
                            f3="";
                        }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("0")){
                            f1="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
                            f2="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png";
                            f3="";
                        }else if(ja3.getString(3).equals("1") && ja3.getString(5).equals("1") && ja3.getString(7).equals("1")){
                            f1="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
                            f2="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png";
                            f3="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio3+".png";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(visi.isChecked()){
                        valor="1";
                    }else if(taxi.isChecked()){
                        valor="3";
                    }else if(prove.isChecked()) {
                        valor="2";
                    }


                    Map<String, String> params = new HashMap<>();
                    try {
                        params.put("id_residencial", Conf.getResid().trim());
                        params.put("id_usuario", ja4.getString(0));
                        params.put("id_tipo",valor);
                        params.put("nombre", Nombre.getText().toString().trim());

                        params.put("placas", Placas.getText().toString().trim());
                        params.put("pasajeros", Pasajeros.getSelectedItem().toString());
                        params.put("guardia_de_entrada", Conf.getUsu().trim());

                        params.put("foto1", f1);
                        params.put("foto2", f2);
                        params.put("foto3", f3);

                        params.put("usuario",ja4.getString(1).trim() + " " + ja4.getString(2).trim() + " " + ja4.getString(3).trim());
                        params.put("token", ja4.getString(5).trim());
                        params.put("correo",ja4.getString(4).trim());
                        params.put("nom_residencial",Conf.getNomResi().trim());


                    } catch (JSONException e) {
                        Log.e("TAG","Error: " + e.toString());
                    }

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    public void upload1(){

        StorageReference mountainImagesRef = null;
        mountainImagesRef =storageReference.child(Conf.getPin()+"/caseta/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png");

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
                Toast.makeText(AccesoRegistroActivity.this,"Fallado", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                //upload2();

            }
        });
    }



    public void upload2(){

        StorageReference mountainImagesRef2 = null;
        mountainImagesRef2 =storageReference.child(Conf.getPin()+"/caseta/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png");

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

                Toast.makeText(AccesoRegistroActivity.this,"Fallado", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                pd2.dismiss();

            }
        });
    }



    public void upload3(){

        StorageReference mountainImagesRef3 = null;
        mountainImagesRef3 =storageReference.child(Conf.getPin()+"/caseta/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio3+".png");

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
                Toast.makeText(AccesoRegistroActivity.this,"Fallado", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd3.dismiss();
            }
        });
    }

    public void Terminar() {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoRegistroActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("Entrada de Visita Exitosa")
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
        startActivity(intent);
        finish();
    }


}