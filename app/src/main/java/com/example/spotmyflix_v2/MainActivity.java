package com.example.spotmyflix_v2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    RequestQueue queue = Volley.newRequestQueue(this);
    String url = "https://api.spotify.com/v1/playlists/{playlist_id}";
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

        Button button1 = (Button) findViewById(R.id.suggestion);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("MyApp", "Watch Arrested Development");
                Toast.makeText(getApplicationContext(), "Watch Arrested Development", Toast.LENGTH_LONG)
                        .show();
//                System.out.println("Watch Arrested Development");
            }
        });
    }
    public void sendMessage(View view) {
        Button button1 = (Button) findViewById(R.id.suggestion);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Watch Arrested Development");
            }
        });
    }
}
