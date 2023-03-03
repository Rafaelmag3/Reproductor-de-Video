package com.example.reproductordevideo;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;

public class ListaVideos extends AppCompatActivity {
    private ListView listViewVideos;
    private List<String> videoNames;
    private List<Integer> videoIds;
    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_videos);
        // Obtener la referencia al ListView y al EditText de la interfaz
        listViewVideos = findViewById(R.id.listview_videos);
        searchBox = findViewById(R.id.search_box);

        // Obtener la lista de nombres y de identificadores de recursos de los videos
        Resources res = getResources();
        String[] rawVideoNames = res.getStringArray(R.array.nombres_videos); // obtener los nombres de los archivos de video
        videoNames = new ArrayList<>();
        videoIds = new ArrayList<>();
        for (String videoName : rawVideoNames) {
            int id = res.getIdentifier(videoName, "raw", getPackageName());
            videoNames.add(videoName);
            videoIds.add(id);
        }

        // Crear un adaptador para el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videoNames);
        listViewVideos.setAdapter(adapter);

        // Asignar un listener al ListView para reproducir los videos al hacer clic en ellos
        listViewVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String videoName = videoNames.get(position);
                int videoId = videoIds.get(position);
                Toast.makeText(ListaVideos.this, "Reproduciendo video " + videoName, Toast.LENGTH_SHORT).show();

                // Aquí se crea el Intent para abrir la actividad MainActivity y se le pasa el identificador del video como extra
                Intent intent = new Intent(ListaVideos.this, MainActivity.class);
                intent.putExtra("VIDEO_ID", videoId);
                startActivity(intent);

            }
        });
        // Agregar un listener para detectar cambios en el texto de la barra de búsqueda
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtrar los videos según el texto de búsqueda
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
