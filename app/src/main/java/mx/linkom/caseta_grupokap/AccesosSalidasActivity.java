package mx.linkom.caseta_grupokap;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

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
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mx.linkom.caseta_grupokap.offline.Database.UrisContentProvider;
import mx.linkom.caseta_grupokap.offline.Global_info;

public class AccesosSalidasActivity extends mx.linkom.caseta_grupokap.Menu {
    Configuracion Conf;
    FirebaseStorage storage;
    StorageReference storageReference;

    LinearLayout rlPermitido, rlDenegado,rlVista,ContinuarBoton;
    TextView  tvMensaje;
    TextView Nombre,Dire,Visi,Pasajeros,Placas,Tipo;

    ArrayList<String> names;
    JSONArray ja1,ja2,ja3,ja4,ja5,ja6;
    Date FechaA;
    String FechaC;

    Button Registrar,Continuar;
    ImageView view1,view2,view3;
    TextView nombre_foto1,nombre_foto2,nombre_foto3;
    LinearLayout Foto1, Foto2,Foto3,Foto1View,Foto2View,Foto3View,espacio2,espacio3,espacio4,espacio5,espacio6,espacio8,espacio9,espacio10;
    LinearLayout PlacasL;
    EditText Comentarios;

    /*ImageView iconoInternet;
    boolean Offline = false;*/

    TextView txtFoto1, txtFoto2, txtFoto3, txtFotoPlaca;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accesos_salidas);

        Conf = new Configuracion(this);
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        names = new ArrayList<String>();

        Comentarios = (EditText) findViewById(R.id.setComentarios);

        PlacasL = (LinearLayout) findViewById(R.id.PlacasL);
        Foto1 = (LinearLayout) findViewById(R.id.Foto1);
        Foto2 = (LinearLayout) findViewById(R.id.Foto2);
        Foto3 = (LinearLayout) findViewById(R.id.Foto3);
        Foto1View = (LinearLayout) findViewById(R.id.Foto1View);
        Foto2View = (LinearLayout) findViewById(R.id.Foto2View);
        Foto3View = (LinearLayout) findViewById(R.id.Foto3View);
        espacio2 = (LinearLayout) findViewById(R.id.espacio2);
        espacio3 = (LinearLayout) findViewById(R.id.espacio3);
        espacio4 = (LinearLayout) findViewById(R.id.espacio4);
        espacio5 = (LinearLayout) findViewById(R.id.espacio5);
        espacio6 = (LinearLayout) findViewById(R.id.espacio6);
        espacio8 = (LinearLayout) findViewById(R.id.espacio8);
        espacio9 = (LinearLayout) findViewById(R.id.espacio9);
        espacio10 = (LinearLayout) findViewById(R.id.espacio10);
        rlVista = (LinearLayout) findViewById(R.id.rlVista);
        rlPermitido = (LinearLayout) findViewById(R.id.rlPermitido);
        rlDenegado = (LinearLayout) findViewById(R.id.rlDenegado);
        ContinuarBoton = (LinearLayout) findViewById(R.id.ContinuarBoton);
        tvMensaje = (TextView)findViewById(R.id.setMensaje);

        Registrar = (Button) findViewById(R.id.Registrar);

        nombre_foto1 = (TextView) findViewById(R.id.nombre_foto1);
        nombre_foto2 = (TextView) findViewById(R.id.nombre_foto2);
        nombre_foto3 = (TextView) findViewById(R.id.nombre_foto3);

        view1 = (ImageView) findViewById(R.id.view1);
        view2 = (ImageView) findViewById(R.id.view2);
        view3 = (ImageView) findViewById(R.id.view3);

        txtFoto1 = (TextView) findViewById(R.id.txtFotoAccesosSalidas1);
        txtFoto2 = (TextView) findViewById(R.id.txtFotoAccesosSalidas2);
        txtFoto3 = (TextView) findViewById(R.id.txtFotoAccesosSalidas3);

        txtFoto1.setText(Global_info.getTexto1Imagenes());
        txtFoto2.setText(Global_info.getTexto1Imagenes());
        txtFoto3.setText(Global_info.getTexto1Imagenes());

        rlVista = (LinearLayout) findViewById(R.id.rlVista);
        rlPermitido = (LinearLayout) findViewById(R.id.rlPermitido);
        rlDenegado = (LinearLayout) findViewById(R.id.rlDenegado);
        tvMensaje = (TextView)findViewById(R.id.setMensaje);

        /*iconoInternet = (ImageView) findViewById(R.id.iconoInternetAccesosSalidas);

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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccesosSalidasActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOffline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccesosSalidasActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOffline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }
            }
        });*/


        Nombre = (TextView)findViewById(R.id.setNombre);
        Dire = (TextView)findViewById(R.id.setDire);
        Visi = (TextView) findViewById(R.id.setVisi);
        Placas = (TextView) findViewById(R.id.setPlacas);
        Pasajeros = (TextView) findViewById(R.id.setPasajeros);
        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion();
            }
        });
        Tipo = (TextView)findViewById(R.id.setTipo);
        Continuar = (Button) findViewById(R.id.continuar);

        //SI ES ACEPTADO O DENEGAODO
        if(Conf.getST().equals("Aceptado")){
            rlVista.setVisibility(View.VISIBLE);
            rlPermitido.setVisibility(View.GONE);
            rlDenegado.setVisibility(View.GONE);

            menu();

            /*if (Offline){
                menuOffline();
            }else {
                menu();
            }*/
        }else if(Conf.getST().equals("Denegado")){
            rlDenegado.setVisibility(View.VISIBLE);
            rlVista.setVisibility(View.GONE);
            rlPermitido.setVisibility(View.GONE);
            tvMensaje.setText("Placa Inexistente");
        }

        Continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), AccesosMorososActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void menuOffline() {
        Log.e("info", "menu offline");
        try {
            Cursor cursoAppCaseta = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_APP_CASETA, null, null, null);

            ja5 = new JSONArray();

            if (cursoAppCaseta.moveToFirst()){
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

            }else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
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

            if (cursoAppCaseta.moveToFirst()){
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

                VisitaOffline();
            }else {
                int $arreglo[]={0};
                ja6 = new JSONArray($arreglo);
                VisitaOffline();
            }
            cursoAppCaseta.close();

        }catch (Exception ex){
            System.out.println(ex.toString());
        }
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
                        ja6 = new JSONArray($arreglo);
                        Visita();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    response = response.replace("][", ",");
                    if (response.length() > 0) {
                        try {
                            ja6 = new JSONArray(response);
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

    public void VisitaOffline(){
        Log.e("info", "visita offline");
        try {
            String qr = Conf.getQR().trim();
            String id_resid= Conf.getResid().trim();
            String parametros[] = {qr, id_resid};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_VISITA, null, "vst1", parametros, null);

            if (cursor.moveToFirst()){
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
                ja1.put(cursor.getString(16)); //Estatus Offline de visita

                UsuarioOffline(ja1.getString(2));
            }else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al obtener datos de la visita")
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

    public void Visita(){

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_php1.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        ja1 = new JSONArray(response);
                        Usuario(ja1.getString(2));
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
                params.put("QR", Conf.getQR().trim());
                params.put("id_residencial", Conf.getResid().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void UsuarioOffline(final String IdUsu){ //DATOS USUARIO
        Log.e("info", "usuario offline");
        try {
            String id_residencial = Conf.getResid().trim();
            String id = IdUsu.trim();

            String parametros[] ={id, id_residencial};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_USUARIO, null, "dts_accesso_autos", parametros, null);

            if (cursor.moveToFirst()){
                ja2 = new JSONArray();

                ja2.put(cursor.getString(0));
                ja2.put(cursor.getString(1));
                ja2.put(cursor.getString(2));
                ja2.put(cursor.getString(3));
                ja2.put(cursor.getString(4));
                ja2.put(cursor.getString(5));
                ja2.put(cursor.getString(6));

                dtlLugarOffline(ja2.getString(0));
            }else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al obtener datos de usuario")
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

    public void Usuario(final String IdUsu){ //DATOS USUARIO

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_php2.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        ja2 = new JSONArray(response);
                        dtlLugar(ja2.getString(0));
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
                params.put("IdUsu", IdUsu.trim());
                params.put("id_residencial", Conf.getResid().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void dtlLugarOffline(final String idUsuario){
        Log.e("info", "dtllugar offline");
        try {
            String id_residencial = Conf.getResid().trim();
            String id = idUsuario.trim();

            String parametros[] ={id_residencial, id};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "dtl_lugar_usuario", parametros, null);

            if (cursor.moveToFirst()){
                ja3 = new JSONArray();
                ja3.put(cursor.getString(0));

                salidasOffline(ja1.getString(0));

            }else {
                sincasa();
            }
            cursor.close();
        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }
    }

    public void dtlLugar(final String idUsuario){
        String URLResidencial = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_php3.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
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
        }){
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

    public void salidasOffline (final String id_visitante){

        try {
            String id_visita = id_visitante.trim();
            String id_residencial = Conf.getResid().trim();
            String parametros[] = {id_residencial, id_visita};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_DTL_ENTRADAS_SALIDAS, null, "vst_php6", parametros, null);

            if (cursor.moveToFirst()){
                ja4 = new JSONArray();
                ja4.put(cursor.getString(0));
                ja4.put(cursor.getString(1));
                ja4.put(cursor.getString(2));
                ja4.put(cursor.getString(3));
                ja4.put(cursor.getString(4));
                ja4.put(cursor.getString(5));
                ja4.put(cursor.getString(6));
                ja4.put(cursor.getString(7));
                ja4.put(cursor.getString(8)); //Estatus Offline de dltEntradasSalidas

                ValidarQR();
            }else {
                String $arreglo[]={"0","0","0"};
                ja4 = new JSONArray($arreglo);
                ValidarQR();
            }

        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }
    }

    public void salidas (final String id_visitante){
        String URLResidencial = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_php6.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLResidencial, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (response.trim().equals("error")){

                        String $arreglo[]={"0","0","0"};
                        ja4 = new JSONArray($arreglo);
                        ValidarQR();
                    }else{
                        response = response.replace("][",",");
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
        }){
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




    public void sincasa(){

        rlVista.setVisibility(View.GONE);
        rlPermitido.setVisibility(View.GONE);
        rlDenegado.setVisibility(View.VISIBLE);
        tvMensaje.setText(" No tiene asignada una unidad privativa.");

    }



    public void ValidarQR(){
        try {
            if(ja1.getString(11).equals("0000-00-00 00:00:00")){
                rlVista.setVisibility(View.GONE);
                rlPermitido.setVisibility(View.VISIBLE);
                rlDenegado.setVisibility(View.GONE);

                Nombre.setText(ja2.getString(1) + " " + ja2.getString(2) + " " + ja2.getString(3));
                if(ja1.getString(4).equals("1") || ja1.getString(12).equals("0")){
                    Tipo.setText("Visita");
                }else if(ja1.getString(4).equals("2")){
                    Tipo.setText("Proveedor / Servicios");
                }else if(ja1.getString(4).equals("3")){
                    Tipo.setText("Taxista");
                }
                Dire.setText(ja3.getString(0));
                Visi.setText(ja1.getString(7));
                Pasajeros.setText(ja4.getString(6));
                Comentarios.setText(ja1.getString(9));
                if(ja4.getString(7).equals("")){
                    PlacasL.setVisibility(View.GONE);

                }else{
                    PlacasL.setVisibility(View.VISIBLE);
                    Placas.setText(ja4.getString(7));
                }
                //FOTO1
                if(ja4.getString(3).equals("")){
                    Foto1.setVisibility(View.GONE);
                    espacio2.setVisibility(View.GONE);
                    Foto1View.setVisibility(View.GONE);
                    espacio3.setVisibility(View.GONE);
                    txtFoto1.setVisibility(View.GONE);

                }else{
                    nombre_foto1.setText(ja6.getString(4)+":");

                    storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(3))
                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override

                                public void onSuccess(Uri uri) {
                                    Glide.with(AccesosSalidasActivity.this)
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

                   /*if (!Offline){
                       storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(3))
                               .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                   @Override

                                   public void onSuccess(Uri uri) {
                                       Glide.with(AccesosSalidasActivity.this)
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
                   }else txtFoto1.setText(Global_info.getTexto3Imagenes());*/
                }

                //FOTO2
                if(ja4.getString(4).equals("")){
                    Foto2.setVisibility(View.GONE);
                    espacio5.setVisibility(View.GONE);
                    Foto2View.setVisibility(View.GONE);
                    espacio6.setVisibility(View.GONE);
                    txtFoto2.setVisibility(View.GONE);
                }else{
                    nombre_foto2.setText(ja6.getString(6)+":");

                    storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(4))
                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override

                                public void onSuccess(Uri uri) {
                                    Glide.with(AccesosSalidasActivity.this)
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
                                    txtFoto2.setVisibility(View.GONE);
                                }
                            });

                   /*if (!Offline){
                       storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(4))
                               .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                   @Override

                                   public void onSuccess(Uri uri) {
                                       Glide.with(AccesosSalidasActivity.this)
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
                                       txtFoto2.setVisibility(View.GONE);
                                   }
                               });
                   }else txtFoto2.setText(Global_info.getTexto3Imagenes());*/
                }
                //FOTO3
                if(ja4.getString(5).equals("")){
                    Foto3.setVisibility(View.GONE);
                    espacio8.setVisibility(View.GONE);
                    Foto3View.setVisibility(View.GONE);
                    espacio9.setVisibility(View.GONE);
                    txtFoto3.setVisibility(View.GONE);
                }else{
                    nombre_foto3.setText(ja6.getString(8)+":");

                    storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(5))
                            .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override

                                public void onSuccess(Uri uri) {
                                    Glide.with(AccesosSalidasActivity.this)
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

                   /*if (!Offline){
                       storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(5))
                               .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                   @Override

                                   public void onSuccess(Uri uri) {
                                       Glide.with(AccesosSalidasActivity.this)
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
                   }else txtFoto3.setText(Global_info.getTexto3Imagenes());*/
                }

            }else{

                    Calendar c = Calendar.getInstance();
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    FechaA = Calendar.getInstance().getTime();
                    Date dateentrada = (Date)formatter.parse(ja1.getString(10));
                    Date datesalida = (Date)formatter.parse(ja1.getString(11));


                    //ANTES DE LA ENTRADA
                    if(c.getTime().before(dateentrada) && ja4.getString(2).equals("0")) {//NUEVO
                        rlVista.setVisibility(View.GONE);
                        rlPermitido.setVisibility(View.GONE);
                        rlDenegado.setVisibility(View.VISIBLE);
                        tvMensaje.setText("Aún no es hora de entrada");

                    }   else if( c.getTime().equals(dateentrada) || c.getTime().before(datesalida) ) {

                        if(ja4.getString(2).equals("0")){ //NUEVO
                            rlVista.setVisibility(View.GONE);
                            rlPermitido.setVisibility(View.GONE);
                            rlDenegado.setVisibility(View.VISIBLE);
                            tvMensaje.setText("No estas dentro del complejo");

                        }else if(ja4.getString(2).equals("1")){ //ENTRO Y QUIERE SALIR
                            rlVista.setVisibility(View.GONE);
                            rlPermitido.setVisibility(View.VISIBLE);
                            rlDenegado.setVisibility(View.GONE);

                            Nombre.setText(ja2.getString(1) + " " + ja2.getString(2) + " " + ja2.getString(3));
                            if(ja1.getString(4).equals("1") || ja1.getString(12).equals("0")){
                                Tipo.setText("Visita");
                            }else if(ja1.getString(4).equals("2")){
                                Tipo.setText("Proveedor / Servicios");
                            }else if(ja1.getString(4).equals("3")){
                                Tipo.setText("Taxista");
                            }
                            Dire.setText(ja3.getString(0));
                            Visi.setText(ja1.getString(7));
                            Pasajeros.setText(ja4.getString(6));
                            Comentarios.setText(ja1.getString(9));
                            if(ja4.getString(7).equals("")){
                                PlacasL.setVisibility(View.GONE);

                            }else{
                                PlacasL.setVisibility(View.VISIBLE);
                                Placas.setText(ja4.getString(7));
                            }
                            //FOTO1
                            if(ja4.getString(3).equals("")){
                                Foto1.setVisibility(View.GONE);
                                espacio2.setVisibility(View.GONE);
                                Foto1View.setVisibility(View.GONE);
                                espacio3.setVisibility(View.GONE);
                                txtFoto1.setVisibility(View.GONE);

                            }else{
                                nombre_foto1.setText(ja6.getString(4)+":");

                                storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(3))
                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                            @Override

                                            public void onSuccess(Uri uri) {
                                                Glide.with(AccesosSalidasActivity.this)
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

                               /*if (!Offline){
                                   storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(3))
                                           .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                               @Override

                                               public void onSuccess(Uri uri) {
                                                   Glide.with(AccesosSalidasActivity.this)
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
                               }else txtFoto1.setText(Global_info.getTexto3Imagenes());*/
                            }

                            //FOTO2
                            if(ja4.getString(4).equals("")){
                                Foto2.setVisibility(View.GONE);
                                espacio5.setVisibility(View.GONE);
                                Foto2View.setVisibility(View.GONE);
                                espacio6.setVisibility(View.GONE);
                                txtFoto2.setVisibility(View.GONE);
                            }else{
                                nombre_foto2.setText(ja6.getString(6)+":");

                                storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(4))
                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                            @Override

                                            public void onSuccess(Uri uri) {
                                                Glide.with(AccesosSalidasActivity.this)
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

                               /*if (!Offline){
                                   storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(4))
                                           .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                               @Override

                                               public void onSuccess(Uri uri) {
                                                   Glide.with(AccesosSalidasActivity.this)
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
                               }else txtFoto2.setText(Global_info.getTexto3Imagenes());*/
                            }
                            //FOTO3
                            if(ja4.getString(5).equals("")){
                                Foto3.setVisibility(View.GONE);
                                espacio8.setVisibility(View.GONE);
                                Foto3View.setVisibility(View.GONE);
                                espacio9.setVisibility(View.GONE);
                                txtFoto3.setVisibility(View.GONE);
                            }else{
                                nombre_foto3.setText(ja6.getString(8)+":");

                                storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(5))
                                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                            @Override

                                            public void onSuccess(Uri uri) {
                                                Glide.with(AccesosSalidasActivity.this)
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

                               /*if (!Offline){
                                   storageReference.child(Conf.getPin()+"/caseta/"+ja4.getString(5))
                                           .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                               @Override

                                               public void onSuccess(Uri uri) {
                                                   Glide.with(AccesosSalidasActivity.this)
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
                               }else txtFoto3.setText(Global_info.getTexto3Imagenes());*/
                            }

                        }else if(ja4.getString(2).equals("2")){ //SALIO Y ENTRO PERO QUIERE VOLVER A SALIR
                            rlVista.setVisibility(View.GONE);
                            rlPermitido.setVisibility(View.GONE);
                            rlDenegado.setVisibility(View.VISIBLE);
                            tvMensaje.setText("Esté código QR ya fue utilizado");
                        }

                        //DESPUES DE LA SALIDA
                    } else if(c.getTime().after(datesalida)  && ja4.getString(2).equals("2") ){//SALIO Y ENTRO  Y QUIERE VOLVER A SALIR
                        rlVista.setVisibility(View.GONE);
                        rlPermitido.setVisibility(View.GONE);
                        rlDenegado.setVisibility(View.VISIBLE);
                        tvMensaje.setText("Esté código QR ha caducado");
                    } else if(c.getTime().after(datesalida)  && ja4.getString(2).equals("0") ){ //NUEVO NUNCA ENTRO
                        rlVista.setVisibility(View.GONE);
                        rlPermitido.setVisibility(View.GONE);
                        rlDenegado.setVisibility(View.VISIBLE);
                        tvMensaje.setText("Esté código QR ha caducado");
                    } else if(c.getTime().equals(datesalida) || c.getTime().after(datesalida)  && ja4.getString(2).equals("1") ){
                        rlVista.setVisibility(View.GONE);
                        rlPermitido.setVisibility(View.GONE);
                        rlDenegado.setVisibility(View.VISIBLE);
                        ContinuarBoton.setVisibility(View.VISIBLE);
                        tvMensaje.setText("Salida fuera de horario \n Llamar a administración");

                    }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    public void Validacion() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿ Desea realizar la salida ?")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if(ja1.getString(11).equals("0000-00-00 00:00:00")) {
                                RegistrarH();

                                /*if (Offline){
                                    RegistrarHOffline();
                                }else {
                                    RegistrarH();
                                }*/
                            }else {

                                Registrar();

                                /*if (Offline){
                                    RegistrarOffline();
                                }else {
                                    Registrar();
                                }*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void RegistrarOffline(){
        Log.e("info", "RegistrarOffline");
        try {
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

            int actualizar;

            ContentValues values = new ContentValues();
            values.put("salida_real", fecha+" "+hora+":"+segundos);
            values.put("guardia_de_salida", Conf.getUsu().trim());
            values.put("estatus", 2);
            if (ja4.getString(8).equals("0")){
                values.put("sqliteEstatus", 2);
            }

            actualizar = getContentResolver().update(UrisContentProvider.URI_CONTENIDO_DTL_ENTRADAS_SALIDAS, values, "id = "+ ja4.getString(0).trim(), null);

            if (actualizar != -1){

                int actualizarVisita;

                ContentValues values2 = new ContentValues();
                values2.put("comentarios", Comentarios.getText().toString().trim());
                if (ja1.getString(16).equals("0")){
                    values2.put("sqliteEstatus", 2);
                }

                actualizarVisita = getContentResolver().update(UrisContentProvider.URI_CONTENIDO_VISITA, values2, "id = "+ ja1.getString(0).trim(), null);

                if (actualizarVisita != -1){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Salida exitosa en modo offline")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Salida no exitosa en modo offline 2")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }
            }else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Salida no exitosa en modo offline 1")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }
    }

    public void Registrar (){


        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_php7.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response.equals("succes")){
                    //Toast.makeText(getApplicationContext(),"Salida Registrada", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Salida Exitosa")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();


                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Salida No Exitosa")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();


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

                    params.put("id_residencial", Conf.getResid().trim());
                    params.put("id", ja4.getString(0).trim());
                    params.put("guardia_de_salida", Conf.getUsu().trim());
                    params.put("usuario",ja2.getString(1).trim() + " " + ja2.getString(2).trim() + " " + ja2.getString(3).trim());
                    params.put("token", ja2.getString(5).trim());
                    params.put("correo",ja2.getString(6).trim());
                    params.put("visita",ja1.getString(7).trim());
                    params.put("nom_residencial",Conf.getNomResi().trim());

                    params.put("id_visita", ja1.getString(0).trim());
                    params.put("comentarios",Comentarios.getText().toString().trim());

                } catch (JSONException e) {
                    Log.e("TAG","Error: " + e.toString());
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void RegistrarHOffline (){
        Log.e("info", "RegistrarHOffline");
        try {
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

            int actualizar;

            ContentValues values = new ContentValues();
            values.put("salida_real", fecha+" "+hora+":"+segundos);
            values.put("guardia_de_salida", Conf.getUsu().trim());
            values.put("estatus", 2);
            if (ja4.getString(8).equals("0")){
                values.put("sqliteEstatus", 2);
            }

            Log.e("id", ja4.getString(0).trim());

            actualizar = getContentResolver().update(UrisContentProvider.URI_CONTENIDO_DTL_ENTRADAS_SALIDAS, values, "id = "+ ja4.getString(0).trim(), null);

            Log.e("actualizar", ""+actualizar);

            if (actualizar != -1){

                int actualizarVisita;

                ContentValues values2 = new ContentValues();
                values2.put("comentarios", Comentarios.getText().toString().trim());
                values2.put("fecha_salida", fecha+" "+hora+":"+segundos);
                if (ja1.getString(16).equals("0")){
                    values2.put("sqliteEstatus", 2);
                }

                actualizarVisita = getContentResolver().update(UrisContentProvider.URI_CONTENIDO_VISITA, values2, "id = "+ ja1.getString(0).trim(), null);

                if (actualizarVisita != -1){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Salida exitosa en modo offline")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Salida no exitosa en modo offline 2")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }
            }else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Salida no exitosa en modo offline 1")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }
        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }
    }

    public void RegistrarH (){
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/vst_reg_7.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response.equals("succes")){

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Salida de Visita Exitosa")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();


                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesosSalidasActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Salida de Visita No Exitosa")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
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
                    params.put("id", ja4.getString(0).trim());
                    params.put("guardia_de_salida", Conf.getUsu().trim());
                    params.put("id_residencial", Conf.getResid().trim());
                    params.put("usuario",ja2.getString(1).trim() + " " + ja2.getString(2).trim() + " " + ja2.getString(3).trim());
                    params.put("token", ja2.getString(5).trim());
                    params.put("correo",ja2.getString(6).trim());
                    params.put("visita",ja1.getString(7).trim());
                    params.put("nom_residencial",Conf.getNomResi().trim());

                    params.put("id_visita", ja1.getString(0).trim());
                    params.put("comentarios",Comentarios.getText().toString().trim());

                } catch (JSONException e) {
                    Log.e("TAG","Error: " + e.toString());
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
        startActivity(intent);
        finish();
    }
}
