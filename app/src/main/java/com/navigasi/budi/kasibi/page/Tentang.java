package com.navigasi.budi.kasibi.page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.navigasi.budi.kasibi.List_Data.ListDataActivity;
import com.navigasi.budi.kasibi.R;
import com.navigasi.budi.kasibi.coba;

public class Tentang extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    Button kritik;

    static {
        System.setProperty("java.protocol.handler.pkgs", "org.andresoviedo.util.android");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tentang_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tentang);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_tentang);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_tentang);
        navigationView.setNavigationItemSelectedListener(this);

        //tombol kritik dan saran
        kritik = findViewById(R.id.kritik);
        kritik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://bit.ly/kamus_sibi"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.kamus3d) {
            Intent demoIntent = new Intent(this.getApplicationContext(), coba.class);
            demoIntent.putExtra("immersiveMode", "false");
            demoIntent.putExtra("item", "");
            startActivity(demoIntent);
        } else if (id == R.id.kosakata) {
            startActivity(new Intent(Tentang.this, ListDataActivity.class));
        } else if (id == R.id.tentang) {
            Toast.makeText(this, "Tentang Page", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_tentang);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
