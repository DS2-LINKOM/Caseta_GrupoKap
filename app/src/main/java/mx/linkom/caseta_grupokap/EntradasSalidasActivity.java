package mx.linkom.caseta_grupokap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.linkom.caseta_grupokap.adaptadores.ModuloClassGrid;
import mx.linkom.caseta_grupokap.adaptadores.adaptador_Modulo;
import mx.linkom.caseta_grupokap.offline.Database.UrisContentProvider;
import mx.linkom.caseta_grupokap.offline.Global_info;

public class EntradasSalidasActivity extends  mx.linkom.caseta_grupokap.Menu {
    private FirebaseAuth fAuth;
    private mx.linkom.caseta_grupokap.Configuracion Conf;
    JSONArray ja1;

    public GridView gridList,gridList2,gridList3,gridList4,gridList5;

    /*ImageView iconoInternet;
    boolean Offline = false;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradassalidas);
        fAuth = FirebaseAuth.getInstance();
        Conf = new mx.linkom.caseta_grupokap.Configuracion(this);
        gridList = (GridView)findViewById(R.id.gridList);
        gridList2 = (GridView)findViewById(R.id.gridList2);
        gridList3 = (GridView)findViewById(R.id.gridList3);
        gridList4 = (GridView)findViewById(R.id.gridList4);
        gridList5 = (GridView)findViewById(R.id.gridList5);

        /*iconoInternet = (ImageView) findViewById(R.id.iconoInternetEntradasSalidas);

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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntradasSalidasActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOffline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntradasSalidasActivity.this);
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

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();

        menu();

        /*if (Offline){
            menuOffline();
        }else {
            menu();
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void menuOffline(){

        try {
            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_APP_CASETA, null, null, null);

            ja1 = new JSONArray();

            if (cursor.moveToFirst()){
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

            }
            cursor.close();


            if(ja1.getString(2).equals("1") && ja1.getString(4).equals("1")){
                Conf.setPreQr(ja1.getString(4));

            }else if(ja1.getString(2).equals("1") && ja1.getString(4).equals("0")){
                Conf.setPreQr("0");

            }

            if(ja1.getString(2).equals("1")) {
                Conf.setTicketE(ja1.getString(11));
            }else{
                Conf.setTicketE("0");
            }

            if(ja1.getString(3).equals("1")) {
                Conf.setTicketR(ja1.getString(12));
            }else{
                Conf.setTicketR("0");
            }

            llenado();
            llenado2();
            llenado3();

        }catch (Exception ex){
            System.out.println(ex.toString());
            Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña Incorrectos", Toast.LENGTH_LONG).show();
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
                        ja1 = new JSONArray(response);
                        if(ja1.getString(2).equals("1") && ja1.getString(4).equals("1")){
                            Conf.setPreQr(ja1.getString(4));

                        }else if(ja1.getString(2).equals("1") && ja1.getString(4).equals("0")){
                            Conf.setPreQr("0");

                        }

                        if(ja1.getString(2).equals("1")) {
                            Conf.setTicketE(ja1.getString(11));
                        }else{
                            Conf.setTicketE("0");
                        }

                        if(ja1.getString(3).equals("1")) {
                            Conf.setTicketR(ja1.getString(12));
                        }else{
                            Conf.setTicketR("0");
                        }

                        llenado();
                        llenado2();

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Usuario y/o Contraseña Incorrectos", Toast.LENGTH_LONG).show();

                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "NO HAY CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();

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




    public void llenado(){
        ArrayList<ModuloClassGrid> lista = new ArrayList<ModuloClassGrid>();

        try {
            if(ja1.getString(2).equals("1")  ){
                Conf.setPreQr(ja1.getString(4));
                lista.add(new ModuloClassGrid(R.drawable.entradas,"Entradas","#FF4081"));
                lista.add(new ModuloClassGrid(R.drawable.entradas,"Salidas","#4cd2c7"));
            }else{

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        gridList.setAdapter(new adaptador_Modulo(this, R.layout.activity_modulo_lista, lista){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    ImageView add = (ImageView) view.findViewById(R.id.imageView);
                    if (add != null)
                        add.setImageResource(((ModuloClassGrid) entrada).getImagen());

                    final TextView title = (TextView) view.findViewById(R.id.title);
                    if (title != null)
                        title.setText(((ModuloClassGrid) entrada).getTitle());

                    final LinearLayout line = (LinearLayout) view.findViewById(R.id.line);
                    if (line != null)
                        line.setBackgroundColor(Color.parseColor(((ModuloClassGrid) entrada).getColorCode()));

                    gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            if(position==0) {
                                Intent docugen = new Intent(getApplication(), mx.linkom.caseta_grupokap.EscaneoVisitaActivity.class);
                                startActivity(docugen);
                                finish();
                            }else if(position==1){
                                Intent docugen = new Intent(getApplication(), mx.linkom.caseta_grupokap.EscaneoVisitaSalidaActivity.class);
                                startActivity(docugen);
                                finish();
                            }

                        }
                    });

                }
            }

        });
    }

    public void llenado2(){
        ArrayList<ModuloClassGrid> lista4 = new ArrayList<ModuloClassGrid>();

        try {


            if(ja1.getString(5).equals("1")  ){
                lista4.add(new ModuloClassGrid(R.drawable.entradas,"Entradas Trabaja.","#FF4081"));
                lista4.add(new ModuloClassGrid(R.drawable.entradas,"Salidas Trabaja.","#4cd2c7"));
            }else{

            }




        } catch (JSONException e) {
            e.printStackTrace();
        }


        gridList4.setAdapter(new adaptador_Modulo(this, R.layout.activity_modulo_lista, lista4){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    ImageView add = (ImageView) view.findViewById(R.id.imageView);
                    if (add != null)
                        add.setImageResource(((ModuloClassGrid) entrada).getImagen());

                    final TextView title = (TextView) view.findViewById(R.id.title);
                    if (title != null)
                        title.setText(((ModuloClassGrid) entrada).getTitle());

                    final LinearLayout line = (LinearLayout) view.findViewById(R.id.line);
                    if (line != null)
                        line.setBackgroundColor(Color.parseColor(((ModuloClassGrid) entrada).getColorCode()));

                    gridList4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                           if(position==0){
                                Intent docugen = new Intent(getApplication(), mx.linkom.caseta_grupokap.EscaneoTrabajadorEntradaActivity.class);
                                startActivity(docugen);
                                finish();
                            }else if(position==1){
                                Intent docugen = new Intent(getApplication(), mx.linkom.caseta_grupokap.EscaneoTrabajadorSalidaActivity.class);
                                startActivity(docugen);
                                finish();
                            }

                        }
                    });

                }
            }

        });
    }

    public void llenado3(){
        ArrayList<ModuloClassGrid> lista5 = new ArrayList<ModuloClassGrid>();


        try {
            if(ja1.getString(3).equals("1") ){
                lista5.add(new ModuloClassGrid(R.drawable.reportes,"Recepción Visitas","#4cd2c7"));
            }else{
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        gridList5.setAdapter(new adaptador_Modulo(this, R.layout.activity_modulo_lista, lista5){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    ImageView add = (ImageView) view.findViewById(R.id.imageView);
                    if (add != null)
                        add.setImageResource(((ModuloClassGrid) entrada).getImagen());

                    final TextView title = (TextView) view.findViewById(R.id.title);
                    if (title != null)
                        title.setText(((ModuloClassGrid) entrada).getTitle());
                    final LinearLayout line = (LinearLayout) view.findViewById(R.id.line);
                    if (line != null)
                        line.setBackgroundColor(Color.parseColor(((ModuloClassGrid) entrada).getColorCode()));

                    gridList5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                            Intent docugen = new Intent(getApplication(), RecepcionVisitasActivity.class);
                            startActivity(docugen);
                            finish();



                        }
                    });
                }
            }
        });
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.DashboardActivity.class);
        startActivity(intent);
        finish();
    }



}