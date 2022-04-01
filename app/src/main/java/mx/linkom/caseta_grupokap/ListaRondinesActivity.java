package mx.linkom.caseta_grupokap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import mx.linkom.caseta_grupokap.adaptadores.ListasClassGrid;
import mx.linkom.caseta_grupokap.adaptadores.adaptador_Modulo;


public class ListaRondinesActivity extends mx.linkom.caseta_grupokap.Menu {

    private GridView gridList;
    private mx.linkom.caseta_grupokap.Configuracion Conf;
    JSONArray ja1;
    ArrayList<String> ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rondines);

        Conf = new mx.linkom.caseta_grupokap.Configuracion(this);

        ubicacion = new ArrayList<String>();


        gridList = (GridView) findViewById(R.id.gridList);


        horarios();


    }


    public void horarios() {
        String URL = "https://2210.kap-adm.mx/plataforma/casetaV2/controlador/grupokap_access/rondines_1.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja1 = new JSONArray(response);

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
                params.put("guardia_de_entrada", Conf.getUsu().trim());
                params.put("id_residencial", Conf.getResid().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }




    public void llenado(){
        ArrayList<ListasClassGrid> ubicacion = new ArrayList<ListasClassGrid>();


        for (int i = 0; i < ja1.length(); i += 3) {
            try {
                String sCadena = ja1.getString(i + 1);
                String hora = sCadena.substring(0,5);

                ubicacion.add(new ListasClassGrid(hora+" - "+ja1.getString(i + 2), "ID:"+ja1.getString(i + 0)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        gridList.setAdapter(new adaptador_Modulo(this, R.layout.activity_listas, ubicacion){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    final TextView title = (TextView) view.findViewById(R.id.title);
                    if (title != null)
                        title.setText(((ListasClassGrid) entrada).getTitle());

                    final TextView subtitle = (TextView) view.findViewById(R.id.sub);
                    if (subtitle != null)
                        subtitle.setText(((ListasClassGrid) entrada).getSubtitle());

                    gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                                int posicion=position*3;
                            try {
                                //RONDIN DIA
                                Conf.setDia(ja1.getString(posicion));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent i = new Intent(getApplicationContext(), mx.linkom.caseta_grupokap.ListaRondinesUbicacionesActivity.class);
                                startActivity(i);
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