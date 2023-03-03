package com.example.reproductordevideo;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.net.Uri;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private int currentOrientation;
    private boolean isBackgroundBlack = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Guardar estado actual de orientación
        currentOrientation = getResources().getConfiguration().orientation;
        // Obtener el identificador del video que se ha pasado como extra en el Intent
        int videoId = getIntent().getIntExtra("VIDEO_ID", -1);
        if (videoId == -1) {
            // Si no se ha pasado un identificador de video válido, salir de la actividad
            finish();
            return;
        }
        // Obtener el objeto VideoView de la interfaz y establecer la ruta del video
        videoView = findViewById(R.id.video_view);
        String videoPath = "android.resource://" + getPackageName() + "/" + videoId;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        // Crear un objeto MediaController para controlar la reproducción del video
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        // Comenzar la reproducción del video
        videoView.start();
        // Agregar un listener al VideoView para detectar cuando se toca
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Si el fondo está blanco, cambiarlo a negro; si está negro, cambiarlo a blanco
                if (isBackgroundBlack) {
                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                    isBackgroundBlack = false;
                    getSupportActionBar().show();

                } else {
                    getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                    isBackgroundBlack = true;
                    getSupportActionBar().hide();
                }
                // Retornar false para permitir que el evento de toque se propague y el VideoView continúe reproduciendo
                return false;
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener la reproducción del video al salir de la actividad
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Configurar el VideoView para que ocupe toda la pantalla en modo horizontal
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Configurar el VideoView para que vuelva a su tamaño original en modo vertical
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().show();

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Navegar a la actividad lista_videos al presionar el botón de regreso en la ActionBar
            Intent intent = new Intent(this, ListaVideos.class);
            // Agregar la bandera FLAG_ACTIVITY_CLEAR_TOP al intent para reiniciar la actividad anterior
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
