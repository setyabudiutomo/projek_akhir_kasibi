package com.navigasi.budi.kasibi.List_Data;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.navigasi.budi.kasibi.Database.DatabaseSQLHelper;
import com.navigasi.budi.kasibi.R;
import com.navigasi.budi.kasibi.coba;
import com.navigasi.budi.kasibi.page.Tentang;

import java.util.ArrayList;


public class ListDataActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static {
        System.setProperty("java.protocol.handler.pkgs", "org.andresoviedo.util.android");
    }

    protected Cursor cursor;
    DatabaseSQLHelper dbHelper;
    private ListView listView;
    ArrayList<String> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kosakata);

        listView = (ListView) findViewById(R.id.list_view);
        dbHelper = new DatabaseSQLHelper(this);

        read_data();

        //navigation drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_kosakata);
        toolbar.setTitleTextColor(Color.rgb(255,255,255));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_kosakata);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_kosakata);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void read_data()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] nama = null;
        cursor = db.rawQuery("SELECT * FROM kota",nama);

        listData = new ArrayList<>(); //can resizeable
        cursor.moveToFirst();

        for (int cc=0; cc < cursor.getCount(); cc++)
        {
            cursor.moveToPosition(cc);
            listData.add(cursor.getString(1)); //urutan kolom
        }
        //create list adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);

        //add action to list view
        listView.setOnItemClickListener((parentAdapter, view, position, id) -> {
            int itemPosition = position;
            String itemValue = (String) listView.getItemAtPosition(itemPosition);
            Intent demoIntent = new Intent(this.getApplicationContext(), coba.class);
            demoIntent.putExtra("immersiveMode", "false");
            demoIntent.putExtra("item", itemValue);
            startActivity(demoIntent);

            Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> userList = new ArrayList<>();

                for(String user : listData){
                    if(user.toLowerCase().contains(newText.toLowerCase())){
                        userList.add(user);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListDataActivity.this, android.R.layout.simple_list_item_1, userList);
                listView.setAdapter(adapter);

                //add action to list view
                listView.setOnItemClickListener((parentAdapter, view, position, id) -> {
                    int itemPosition = position;
                    String itemValue = (String) listView.getItemAtPosition(itemPosition);
                    Intent demoIntent = new Intent(ListDataActivity.this, coba.class);
                    demoIntent.putExtra("immersiveMode", "false");
                    demoIntent.putExtra("item", itemValue);
                    startActivity(demoIntent);

                    Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                });

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.kamus3d) {
            Intent demoIntent = new Intent(ListDataActivity.this, coba.class);
            demoIntent.putExtra("immersiveMode", "false");
            demoIntent.putExtra("item", "");
            startActivity(demoIntent);
        } else if (id == R.id.kosakata) {
            Toast.makeText(this, "list data", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.tentang) {
            startActivity(new Intent(ListDataActivity.this, Tentang.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_kosakata);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
