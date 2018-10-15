package fr.wcs.apifirstcontact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static String API_KEY = "7a9b1ba98c0ed7a9230a482269f565a8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Crée une file d'attente pour les requêtes vers l'API
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // TODO : URL de la requête vers l'API
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Toulouse&appid=" + API_KEY;
        String urlFiveDays = "http://api.openweathermap.org/data/2.5/forecast?q=Toulouse &appid="+API_KEY;

        // Création de la requête vers l'API, ajout des écouteurs pour les réponses et erreurs possibles
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weather = response.getJSONArray("weather");
                            for(int i = 0 ; i < weather.length() ; i++) {
                                JSONObject weatherInfos = (JSONObject) weather.get(i);
                                String weatherDescription = weatherInfos.getString("description");
                                //Toast.makeText(getApplicationContext(), "Le temps à Toulouse : "
                                        //+ weatherDescription, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Afficher l'erreur
                        Log.d("VOLLEY_ERROR", "onErrorResponse: " + error.getMessage());
                    }
                }
        );

        // On ajoute la requête à la file d'attente
        //requestQueue.add(jsonObjectRequest);

        /* Mise en place de la requête pour connaître la météo sur 5 jours */

        final ArrayList<String> meteo = new ArrayList<>();

        JsonObjectRequest myRequestFiveDays = new JsonObjectRequest(
                Request.Method.GET, urlFiveDays, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getApplicationContext(), "coucou", Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray days = response.getJSONArray("list");
                            int dayIndex = 0;
                            for (int i = 0 ; i < days.length() ; i += 8) {
                                JSONObject day = days.getJSONObject(i);
                                JSONArray weather = day.getJSONArray("weather");
                                for (int j = 0 ; j < weather.length(); j ++) {
                                    JSONObject descriptionContainer = weather.getJSONObject(j);
                                    String description = descriptionContainer.getString("description");
                                    Toast.makeText(getApplicationContext(), "Météo au jour " + dayIndex + " : " + description , Toast.LENGTH_SHORT).show();
                                    dayIndex ++;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Afficher l'erreur
                        Log.d("VOLLEY_ERROR", "onErrorResponse: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(myRequestFiveDays);

    }
}
