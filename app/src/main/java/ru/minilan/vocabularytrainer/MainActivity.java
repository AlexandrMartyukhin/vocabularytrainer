package ru.minilan.vocabularytrainer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String KEYAPI = "trnsl.1.1.20190604T083938Z.ccae0317394f913d.1f3ce33ba505571cedd60a5dc251e81be0c19234";
    private WordsAdapter wordsAdapter;
    private YandexTranslate translater;
    private EditText editTextWord1, editTextWord2;
    public static final String MYLOGTAG = "MYLOGTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFAB();
        initNaviDrawerAndToolbar();
        initRecyclerView();
        initRetrofit();

        refreshWordList();
        initFirebase();
    }

    private void initFirebase() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e(MYLOGTAG, "firebase getInstance onComplete failed");
                        }
                        String token = task.getResult().getToken();
                        Log.i(MYLOGTAG, " Our token = " + token);
                    }
                });


    }

    private void refreshWordList() {
        ArrayList<WordCard> list = DatabaseHelper.getInstance(this).query();
        wordsAdapter.setWords(list);
    }

    private void initRecyclerView() {
        RecyclerView recyclerViewWordsList = findViewById(R.id.recyclerViewWordsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewWordsList.setLayoutManager(linearLayoutManager);
        wordsAdapter = new WordsAdapter();
        wordsAdapter.setOnMenuItemClickListener(new WordsAdapter.OnMenuItemClickListener() {
            @Override
            public void onItemEditClick(WordCard wordCard) {
                editElement(wordCard);
            }

            @Override
            public void onItemDeleteClick(WordCard wordCard) {
                deleteElement(wordCard);
            }
        });
        recyclerViewWordsList.setAdapter(wordsAdapter);
    }

    private void deleteElement(WordCard wordCard) {
        DatabaseHelper.getInstance(MainActivity.this).deleteWord(wordCard);
        refreshWordList();
    }

    private void editElement(WordCard wordCard) {
        // TO DO
    }

    private void initNaviDrawerAndToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                final View alertView = layoutInflater.inflate(R.layout.layout_add_word, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(alertView);
                editTextWord1 = alertView.findViewById(R.id.editTextWord1);
                editTextWord2 = alertView.findViewById(R.id.editTextWord2);
                builder.setTitle(R.string.alert_title_add);
                builder.setNeutralButton("AUTO Translate", null);
                builder.setNegativeButton(R.string.alert_cancel, null);
                builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        DatabaseHelper.getInstance(MainActivity.this)
                                .addWord(editTextWord1.getText().toString(), editTextWord2.getText().toString());
                        refreshWordList();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
                // override neutral button
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "translating...", Toast.LENGTH_SHORT).show();
                        requestRetrofit("en-ru", editTextWord1.getText().toString(), KEYAPI);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initRetrofit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        translater = retrofit.create(YandexTranslate.class);
    }

    private void requestRetrofit(String lang, String wordToTranlate, String keyApi) {
        translater.translate(lang, wordToTranlate, keyApi)
                .enqueue(new Callback<Translate>() {
                    @Override
                    public void onResponse(Call<Translate> call, Response<Translate> response) {
                        if (response.body() != null) {
                            editTextWord2.setText(response.body().getText().get(0));
                        }
                    }

                    @Override
                    public void onFailure(Call<Translate> call, Throwable t) {
                        Log.i(MYLOGTAG, "onfailure " + t.toString());
                    }
                });

    }


}
