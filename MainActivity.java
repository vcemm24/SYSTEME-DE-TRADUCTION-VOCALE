package com.spokeapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends Activity implements RecognitionListener {
    private static  final String KWS_SEARCH="wakeup";
    private static  final String MENU_SEARCH="menu";
    private static  final String KEYPHRASE="speak";
    private SpeechRecognizer recognizer;
    TextView toto,toto2;
    String recup="";
    String recup3="";
    TextToSpeech textToSpeech;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runRecognizerSetup();
        toto2 = findViewById(R.id.textView2);

        ((TextView)findViewById(R.id.textView))
                .setText("Preparing the recognizer...");
        int permissionCheck= ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.RECORD_AUDIO},PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }
        textToSpeech=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int lang=textToSpeech.setLanguage(Locale.FRENCH);}/*
                    if(lang==TextToSpeech.LANG_MISSING_DATA || lang==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error","this language is not supported");
                        Toast.makeText(MainActivity.this,"this language is not supported",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        parler();
                    }
                }
                else{
                    Log.e("error","Failed to Initialize");
                    Toast.makeText(MainActivity.this,"Faied to Initialize",Toast.LENGTH_SHORT).show();

                }*/
            }
        });
        //affiche();
    }
    private void parler(String recup4){
        //String reponse=toto2.getText().toString();
        recup3=recup4;
        if("".equals(recup4)){
            Toast.makeText(MainActivity.this,"le champs est vide",Toast.LENGTH_SHORT).show();
        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            textToSpeech.speak(recup4,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else{
            textToSpeech.speak(recup4,TextToSpeech.QUEUE_FLUSH,null);

        }
    }

    private void reCupText(String recup2) {
        //Hypothesis hypo="";
        //recup=hypo.getHypstr();
        recup2=recup;

        Gson gson=new GsonBuilder()
                .setLenient()
                .create();
        final String url="http://192.168.43.135";
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ServiceInterface serviceInterface=retrofit.create(ServiceInterface.class);

        Call<ServiceResponse> usercall=serviceInterface.deposer(recup2);
        usercall.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    toto2.setText(response.body().getMessage());
                }
                else
                    Toast.makeText(MainActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {

            }
        });



    }

    private void affiche(){

        Gson gson=new GsonBuilder()
                .setLenient()
                .create();
        final String url="http://192.168.43.135";
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ServiceInterface serviceInterface=retrofit.create(ServiceInterface.class);

        Call<ModelGet> call=serviceInterface.getModel();

        call.enqueue(new Callback<ModelGet>() {
            @Override
            public void onResponse(Call<ModelGet> call, Response<ModelGet> response) {

                if(response.isSuccessful()){
                    toto2.setText(response.body().getReponse());

                }
                else {
                    Toast.makeText(MainActivity.this, "il ya un probleme 1", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ModelGet> call, Throwable t) {
                Toast.makeText(MainActivity.this,"il ya un probleme 2",Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                toto2.setText(t.getMessage());

            }
        });

    /*
        Call<List<ModelGet>> call= serviceInterface.getModel();

        call.enqueue(new Callback<List<ModelGet>>() {
            @Override
            public void onResponse(Call<List<ModelGet>> call, Response<List<ModelGet>> response) {
                if(!response.isSuccessful()){
                    //toto2.setText("code :"+response.code());
                    Toast.makeText(MainActivity.this,"il ya un probleme",Toast.LENGTH_SHORT).show();
                    return;
                    }

                List<ModelGet> posts=response.body();
                for(ModelGet post:posts){
                    String content="";
                    content=post.getReponse();
                    toto2.setText(content);
                }
            }

            @Override
            public void onFailure(Call<List<ModelGet>> call, Throwable t) {

                Toast.makeText(MainActivity.this,"il ya un probleme 2",Toast.LENGTH_SHORT).show();

                toto2.setText(t.getMessage());
            }
        });
*/
    }

    private void runRecognizerSetup() {

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... voids) {
                try {
                    Assets assets=new Assets(MainActivity.this);
                    File assetDir=assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                   return e;
                }

                return null;
            }
            protected void onPostExecute(Exception result){
                if(result!=null){
                    System.out.println(result.getMessage());
                } else{
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }
    private void switchSearch(String searchName){
        recognizer.stop();
        if(searchName.equals(KWS_SEARCH)) {
            recognizer.startListening(searchName);

            }



        else
        { recognizer.startListening(searchName,10000);
            //affiche();
            }
    }
    private void setupRecognizer(File assetDir) throws IOException{
        recognizer= SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetDir,"en-us-ptm"))
                .setDictionary(new File(assetDir,"cmudict-en-us.dict"))
                .getRecognizer();
            recognizer.addListener(this);

            recognizer.addKeyphraseSearch(KWS_SEARCH,KEYPHRASE);
            File menuGrammar=new File(assetDir,"mymenu.gram");
            recognizer.addGrammarSearch(MENU_SEARCH,menuGrammar);

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

        if(!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if(hypothesis==null){
            return;}
        String text=hypothesis.getHypstr();

        if(text.equals(KEYPHRASE)) {
            switchSearch(MENU_SEARCH);


        }
        else if(text.equals("hello")){
            //System.out.println("Hello");
            //reCupText("hello");
            //toto2.setText();
            recup = text;
            reCupText(recup);
            parler(toto2.getText().toString());


            //affiche();

        }
        else if(text.equals("good morning")){
            System.out.println("Good morning");
            //reCupText("good morning");
            recup = text;
            reCupText(recup);
            parler(toto2.getText().toString());

            //affiche();
        }
        else{
            System.out.println(hypothesis.getHypstr());

        }

    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if(hypothesis!=null){
            //System.out.println(hypothesis.getHypstr());
            // (hypothesis.getHypstr().equals("speak"))
                Toast.makeText(MainActivity.this, hypothesis.getHypstr() + "OnResult", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onError(Exception e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void onTimeout() {

        switchSearch(KWS_SEARCH);
    }

    public void onStop(){
        super.onStop();
        if(recognizer!=null){
            recognizer.cancel();
            recognizer.shutdown();
        }
    }
}
