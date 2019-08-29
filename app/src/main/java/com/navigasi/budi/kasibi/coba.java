package com.navigasi.budi.kasibi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.navigasi.budi.kasibi.Database.DatabaseSQLHelper;
import com.navigasi.budi.kasibi.IIID_Demo.SceneLoader;
import com.navigasi.budi.kasibi.IIID_View.ModelSurfaceView;
import com.navigasi.budi.kasibi.List_Data.ListDataActivity;
import com.navigasi.budi.kasibi.page.Tentang;

import org.andresoviedo.util.android.ContentUtils;

import java.io.IOException;
import java.util.ArrayList;

public class coba extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    static {
        System.setProperty("java.protocol.handler.pkgs", "org.andresoviedo.util.android");
    }

    //--------------3D---------------//
    private static final int REQUEST_CODE_LOAD_TEXTURE = 1000;
    /**
     * Type of model if file name has no extension (provided though content provider)
     */
    private int paramType;
    /**
     * Set kecepatan dari karakter
     */
    private float paramSpeed;
    /**
     * Set kecepatan dari karakter
     */
    public String paramkata;
    /**
     * The file to load. Passed as input parameter
     */
    private Uri paramUri;
    /**
     * Background GL clear color. Default is light gray
     */
    private float[] backgroundColor = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    private ModelSurfaceView gLView;

    private SceneLoader scene;


    //-----------speech recognition--------------//
    private static final int REQUEST_SUCCESS = 200;
    ImageButton ButtonSending, ButtonVoice;
    EditText inputKata;
    TextView kosakata;

    //ImageView fav;
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    ArrayList<String> matches;

    ActionBarDrawerToggle toggle;
    public String kata;
    String alamat, katas;

    //-----------------database----------------//
    //untuk mengecek keberadaan kata yang sesuai dengan database
    boolean isUserFound;

    //deklarasi database SQLite
    DatabaseSQLHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coba);

        //inisialisasi kecepatan play dari animasi
        this.paramSpeed = 1f;

        //---------------navigation drawer---------------//
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_coba);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle = new ActionBarDrawerToggle
                (
                        this,
                        drawer,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                )
        {
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_coba);
        navigationView.setNavigationItemSelectedListener(this);

        //-----------------voice dan input---------------------------//
        //tombol suara
        ButtonVoice = (ImageButton) findViewById(R.id.voiceCommandCoba);

        //masukkan pada edit text
        inputKata = (EditText) findViewById(R.id.editTextCoba);

        //tampilan kosakata dibagian atas aplikasi
        kosakata = (TextView)findViewById(R.id.tampilan_kosakata_coba);

        //button checking
        ButtonSending = (ImageButton) findViewById(R.id.sendingCoba);
        ButtonSending.setOnClickListener(view -> {
            if(inputKata == null)
            {
                Toast.makeText(this, "Masukan Sebuah Kata", Toast.LENGTH_SHORT).show();
            }
            else {
                Verification();

                paramkata = inputKata.getText().toString().toLowerCase();

                isUserFound = db.getUserDetails(paramkata);

                if(isUserFound)
                {
                    kosakata.setText(inputKata.getText().toString().toLowerCase());
                    this.paramSpeed = 1f;
                    loader3D(paramkata);
                }
                else{
                    kosakata.setText(inputKata.getText().toString().toLowerCase());
                }
            }
        });

        //-------------------3D----------------------------//
        //membuat container pada layout tanpa mendeklarasikan dalam XML
        gLView = new ModelSurfaceView(this, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        drawer.addView(gLView, 0, layoutParams);

        //-----------------voice recognition---------------------------//
        VoiceRecognitionProccess();

        //-----------------Verification Database---------------------------//
        db = new DatabaseSQLHelper(this);

        //-----------------Set Text dari page kumpulan kata ------------------//
        Bundle b = getIntent().getExtras();
        katas = b.getString("item");

        if(katas == null){
            kata = "";
            loader3D(kata);
            Toast.makeText(getApplicationContext(), "awal", Toast.LENGTH_SHORT).show();
        }
        else{
            kata = katas;
            kosakata.setText(katas);
            loader3D(katas);
            Toast.makeText(getApplicationContext(), katas, Toast.LENGTH_SHORT).show();
        }
        //boolean immersiveMode = "false".equalsIgnoreCase(b.getString("immersiveMode"));

        //-----------------Load 3D------------------//

    }

    //fungsi mengambil identifier untuk memuat animasi
    public void loader3D(String katass){
        //-------------------3D----------------------------//
        if(katass.equals("")){
            katass = "awal";
            alamat = "assets://" + getPackageName() + "/models/" + katass +".dae";
            this.paramUri = Uri.parse(alamat);
        }
        else if(katass != null){
            alamat = "assets://" + getPackageName() + "/models/" + katass + ".dae";
            this.paramUri = Uri.parse(alamat);
        }

        ContentUtils.provideAssets(this);
        this.paramType = 2;

        //Handler handler = new Handler(getMainLooper());

        //loader untuk model 3D nya
        scene = new SceneLoader(this);
        scene.init();

        // Show the Up button in the action bar.
        setupActionBar();

        // TODO: Alert user when there is no multitouch support (2 fingers). He won't be able to rotate or zoom
        ContentUtils.printTouchCapabilities(getPackageManager());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        getSupportActionBar().setElevation(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public Uri getParamUri() {
        return paramUri;
    }

    public int getParamType() {
        return paramType;
    }

    public float getParamSpeed(){return paramSpeed;}

    public float[] getBackgroundColor() {
        return backgroundColor;
    }

    public SceneLoader getScene() {
        return scene;
    }

    public ModelSurfaceView getGLView() {
        return gLView;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_LOAD_TEXTURE:
                final Uri uri = Uri.parse("assets://" + getPackageName() + "/models/" + "animasipanjang.dae");
                if (uri != null) {
                    try {
                        ContentUtils.setThreadActivity(this);
                        scene.loadTexture(null, uri);
                    } catch (IOException ex) {
                        Toast.makeText(this, "Error loading texture '" + uri + "'. " + ex
                                .getMessage(), Toast.LENGTH_LONG).show();
                    } finally {
                        ContentUtils.setThreadActivity(null);
                    }
                }
        }
    }

    //-----------------voice recognition---------------------------//
    @SuppressLint("ClickableViewAccessibility")
    public void VoiceRecognitionProccess()
    {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {
            }

            @Override
            public void onResults(Bundle bundle) {
                matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(matches != null)
                {
                    String text = matches.get(0);
                    Verification();

                    isUserFound = db.getUserDetails(text.toLowerCase());

                    if(isUserFound)
                    {
                        kosakata.setText(text.toLowerCase());
                        loader3D(text.toLowerCase());
                        paramSpeed = 1f;
                    }
                    else{
                        kosakata.setText(text.toLowerCase());
                        dialogBox();
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        ButtonVoice.setOnTouchListener((view, motionEvent) -> {
            {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        inputKata.setHint("Masukkan Kosakata");
                        break;
                    case MotionEvent.ACTION_DOWN:
                        inputKata.setText("");
                        inputKata.setHint("Bicara...");

                        //check permission
                        checkPermission();

                        //mulai bicara
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

                        //vibration
                        final Vibrator vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(80); //80 represents the milliseconds (the duration of the vibration)

                        break;
                }
            }
            return false;
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.kamus3d) {
            Toast.makeText(this, "Kamus 3D", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.kosakata) {
            startActivity(new Intent(coba.this, ListDataActivity.class));
        } else if (id == R.id.tentang) {
            startActivity(new Intent(coba.this, Tentang.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_coba);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //-----------------Check Permission Microphone---------------------------//
    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //marshmello min. requirement
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_SUCCESS);
            }
        }
    }

    public void Verification(){
        if(inputKata.getText().toString().isEmpty()){
            Toast.makeText(this, "Masukkan kosakata yang dicari", Toast.LENGTH_SHORT).show();
            return;
        }else{
            isUserFound = db.getUserDetails(inputKata.getText().toString().toLowerCase());
            if(isUserFound){
                Toast.makeText(this, "ada", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "tidak ada", Toast.LENGTH_SHORT).show();
                dialogBox();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    void dialogBox(){

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("KATA TIDAK DITEMUKAN");
        title.setPadding(10, 20, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) cancelBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.BLUE);
        cancelBT.setLayoutParams(negBtnLP);
    }
}
