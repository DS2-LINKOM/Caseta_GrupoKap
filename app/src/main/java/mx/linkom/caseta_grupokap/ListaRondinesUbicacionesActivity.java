package mx.linkom.caseta_grupokap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.linkom.caseta_grupokap.adaptadores.ListasHoraFechaClassGrid;
import mx.linkom.caseta_grupokap.adaptadores.adaptador_Modulo;

public class ListaRondinesUbicacionesActivity extends mx.linkom.caseta_grupokap.Menu{

    private GridView gridList;
    private mx.linkom.caseta_grupokap.Configuracion Conf;
    JSONArray ja1,ja2,ja3;
    ArrayList<String> ubicacion;
    TextView Nombre;
    Button Incidencia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rondines_ubi);

        Conf = new mx.linkom.caseta_grupokap.Configuracion(this);
        ubicacion = new ArrayList<String>();
        Nombre = (TextView) findViewById(R.id.nombre);
        Incidencia = (Button) findViewById(R.id.btnIncidencia);
        gridList = (GridView) findViewById(R.id.gridList);
        horarios();
        Incidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.RondinIncidencias.class);
                startActivity(i);
                finish();
            }
        });

    }


    public void horarios() {

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_2.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja1 = new JSONArray(response);
                        //ID RONDIN
                        Conf.setRondin(ja1.getString(2));
                        rondin(ja1.getString(2));

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
                params.put("id", Conf.getDia());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void rondin(final String id_rondin) {

        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_3.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja2 = new JSONArray(response);
                        //RONDIN NOMBRE
                        Conf.setRondinNombre(ja2.getString(2));
                        Nombre.setText(Conf.getRondinNombre());
                        ubicaciones(Conf.getRondin());

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
                params.put("id", id_rondin.trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    public void ubicaciones(final String id_app) {
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_4.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja3 = new JSONArray(response);
                        llenado();

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
                params.put("id", id_app.trim());
                params.put("id_dia", Conf.getDia());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    public void llenado(){
        ArrayList<ListasHoraFechaClassGrid> ubicacion = new ArrayList<ListasHoraFechaClassGrid>();


        for (int i = 0; i < ja3.length(); i += 3) {
            try {
                String sCadena = ja3.getString(i + 2);
                String hora = sCadena.substring(0,5);

                ubicacion.add(new ListasHoraFechaClassGrid(ja3.getString(i + 1),"|"+hora, "ID:"+ja3.getString(i + 0)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        gridList.setAdapter(new adaptador_Modulo(this, R.layout.activity_lista_hora_fecha, ubicacion){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    final TextView title = (TextView) view.findViewById(R.id.title);
                    if (title != null)
                        title.setText(((ListasHoraFechaClassGrid) entrada).getTitle());

                    final TextView fecha = (TextView) view.findViewById(R.id.fecha);
                    if (fecha != null)
                        fecha.setText(((ListasHoraFechaClassGrid) entrada).getFecha());
                    
                    final TextView subtitle = (TextView) view.findViewById(R.id.sub);
                    if (subtitle != null)
                        subtitle.setText(((ListasHoraFechaClassGrid) entrada).getSubtitle());

                    gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                            int posicion=position*3;
                            try {
                                //RONDINES UBICACIONES
                                Conf.setUbicacion(ja3.getString(posicion));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.RondinInfoActivity.class);
                            startActivity(i);


                        }
                    });


                }
            }

        });
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.ListaRondinesActivity.class);
        startActivity(intent);
        finish();
    }

}