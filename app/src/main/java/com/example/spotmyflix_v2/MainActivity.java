package com.example.spotmyflix_v2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
//    private RequestQueue queue = Volley.newRequestQueue(this);
    private String id;
    private JSONObject playlist;
    private String userId;
    final Context context = this;
    private Button button;
    private String playlistID;
    private String playlistName;
    final private Map<String, String[]> matches = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        button = findViewById(R.id.buttonPrompt);
        setMatches();
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = promptsView.findViewById(R.id.editTextDialogUserInput);

                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = userInput.getText().toString();
                        getUserId(url);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        getPlaylist();
    }


    public void sendMessage(View view) {
        Button button1 = (Button) findViewById(R.id.suggestion);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Watch Arrested Development");
            }
        });
    }

    /**
     * I think the easiest way to do this is have them copy the link to their spotify account and then we can get their user id from that
     * then using that we can get their playlists? you feel?
     * I was trying to figure out how to get them to log in and ish but its so freaking complicated
     * this is my url https://open.spotify.com/user/22vjetdrjh5ec725snht3q7ea?si=dD8VL0YhQNeHJEJoBeULFg
     * the part after the last slash is my userid
     */
    public void getUserId(String url) {
        int slash = url.lastIndexOf("/");
        userId = url.substring(slash + 1);
    }

    /**
     * Gets a list of all of the users playlists and randomly selects one to use
     * idk how to do this not randomly without them either knowing the playlist id or typing in the name correctly
     * random is kind of fun though
     */
    public void getPlaylist(){
        String url = "https://api.spotify.com/v1/users/" + userId + "/playlists";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int numLists = response.length();
                    Random random = new Random();
                    int randomNum = random.nextInt(numLists - 1);
                    playlist = (JSONObject) response.get(randomNum);
                    playlistID = playlist.get("id").toString();
//                  playlistName = playlist.get("name");
                } catch (JSONException e) {
                    Log.e("Main Activity", "There was an error parsing the JSON array");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Main Activity", "There was an error loading your playlists. Please try again");
            }
        });
    }

    public void getGenre() {
        String url = "https://api.spotify.com/v1/users/playlists/" + playlistID + "/tracks";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int numTracks = response.length();
                    for (int i = 0; i < numTracks; i++) {
                        JSONObject track = (JSONObject) response.get(i);
                        JSONObject album = (JSONObject) track.get("album");
                        JSONObject external_urls = (JSONObject) album.get("external_urls");
                        String spotifyURL = external_urls.get("spotify").toString();
                        int slash = spotifyURL.lastIndexOf("/");
                        String albumID = spotifyURL.substring(slash + 1);
                        String albumURL = "https://api.spotify.com/v1/albums/" + albumID;
                        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, albumURL, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
//                                try {
//
//                                } catch (JSONException e) {
//                                    Log.e("Main Activity", "There was an error parsing the JSON array");
//                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Main Activity", "There was an error loading your playlists. Please try again");
                            }
                        });
                    }
                } catch (JSONException e) {
                    Log.e("Main Activity", "There was an error parsing the JSON array");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Main Activity", "There was an error loading your playlists. Please try again");
            }
        });

    }

    public void setMatches() {
        String[] rock = {"Arrested Development", "Breaking Bad", "Peaky Blinders"};
        matches.put("rock", rock);
        String[] edm = {"Naruto", "Bleach", "Attack on Titan"};
        matches.put("edm", edm);
        String[] pop = {"New Girl", "Gossip Girl", "Outer Banks"};
        matches.put("pop", pop);
        String[] jazz = {"Parks And Recreation", "The Great British Baking Show", "The Blacklist"};
        matches.put("jazz", jazz);
        String[] rnb = {"Too Hot to Handle", "The 100", "Chill with Bob Ross"};
        matches.put("rnb", rnb);
        String[] hiphop = {"Community", "Schitt's Creek", "Shameless"};
        matches.put("hiphop", hiphop);
        String[] country = {"How to Get Away With Murder", "The Punisher", "Outer Banks"};
        matches.put("country", country);
        String[] indie = {"Mad Men", "Criminal Minds", "Bojack Horseman"};
        matches.put("indie", indie);
        String[] classical = {"Sherlock", "Glee", "Portlandia"};
        matches.put("classical", classical);
        String[] other = {"The Office", "Stranger Things", "Black Mirror"};
        matches.put("other", other);
    }

}
