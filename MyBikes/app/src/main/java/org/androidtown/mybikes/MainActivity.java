package org.androidtown.mybikes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Button search;
    private Button gps;
    private Button charge;
    private Button direction;
    private ListView list;
    private ListViewAdapter adapter;
    private Toolbar toolbar;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;
    public static AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.app_name, R.string.app_name);
        dlDrawer.setDrawerListener(dtToggle);

        list = (ListView)findViewById(R.id.listView);
        search = (Button) findViewById(R.id.search);
        gps = (Button) findViewById(R.id.gps);
        charge = (Button) findViewById(R.id.charge);
        direction = (Button) findViewById(R.id.direction);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), TabActivity.class);
                startActivity(in);
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), GPSTabActivity.class);
                startActivity(in);
            }
        });
        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ChargeActivity.class);
                startActivity(in);
            }
        });
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), DirectionTapActivity.class);
                startActivity(in);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = null;
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                switch (position){
                    case 0 :
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 1 :
                        in = new Intent(getApplicationContext(), TabActivity.class);
                        startActivity(in);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                    case 2 :
                        in = new Intent(getApplicationContext(), SourceActivity.class);
                        startActivity(in);
                        drawer.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });

        new GetUpload().execute();
    }
    private class GetUpload extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Adapter 생성
            adapter = new ListViewAdapter() ;

            // 리스트뷰 참조 및 Adapter달기
            list.setAdapter(adapter);

            // 첫 번째 아이템 추가.
            adapter.addItem(ContextCompat.getDrawable(MainActivity.this, R.drawable.w_home),
                    "홈") ;
            // 두 번째 아이템 추가.
            adapter.addItem(ContextCompat.getDrawable(MainActivity.this, R.drawable.b_search),
                    "검색") ;
            // 세 번째 아이템 추가.
            adapter.addItem(ContextCompat.getDrawable(MainActivity.this, R.drawable.b_source),
                    "출처") ;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("종료");
            builder.setMessage("종료하시겠습니까?");

            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}