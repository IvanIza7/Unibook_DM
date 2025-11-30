package com.navify.unibook;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActividadesActivity extends AppCompatActivity {

    private RecyclerView listActividades;
    private ActividadAdapter actividadAdapter;
    private DatabaseHelper dbHelper;

    private List<Actividad> listaCompletaActividades;
    private final List<Actividad> listaFiltradaActividades = new ArrayList<>();
    private List<String> listaMaterias;

    private EditText txtBuscar;
    private LinearLayout btnFiltroMateria, btnFiltroAntiguedad;
    private TextView txtFiltroMateria, txtFiltroAntiguedad;
    private ImageView btnBack;

    private String filtroMateriaActual = "Todas";
    private String ordenActual = "reciente"; // "reciente" o "materia"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
        cargarDatos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar datos por si hay cambios
        cargarDatos();
    }

    private void initViews() {
        listActividades = findViewById(R.id.listActividades);
        listActividades.setLayoutManager(new LinearLayoutManager(this));

        txtBuscar = findViewById(R.id.txtBuscar);
        btnFiltroMateria = findViewById(R.id.btnFiltroMateria);
        btnFiltroAntiguedad = findViewById(R.id.btnFiltroAntiguedad);
        txtFiltroMateria = findViewById(R.id.txtFiltroMateria);
        txtFiltroAntiguedad = findViewById(R.id.txtFiltroAntiguedad);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarYOrdenarLista();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnFiltroMateria.setOnClickListener(this::showMateriaFilterMenu);
        btnFiltroAntiguedad.setOnClickListener(this::showSortOrderMenu);
    }

    private void cargarDatos() {
        // Simulación de carga de datos. Reemplazar con la lógica de tu DB.
        listaCompletaActividades = dbHelper.getTodasLasActividades();
        listaMaterias = dbHelper.getNombresDeTodasLasMaterias();

        if (actividadAdapter == null) {
            actividadAdapter = new ActividadAdapter(this, listaFiltradaActividades);
            listActividades.setAdapter(actividadAdapter);
        } 
        filtrarYOrdenarLista(); 
    }

    private void showMateriaFilterMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add(Menu.NONE, Menu.NONE, 0, getString(R.string.filtro_todas_materias));
        for (int i = 0; i < listaMaterias.size(); i++) {
            popup.getMenu().add(Menu.NONE, i + 1, i + 1, listaMaterias.get(i));
        }

        popup.setOnMenuItemClickListener(item -> {
            filtroMateriaActual = item.getTitle().toString();
            txtFiltroMateria.setText(filtroMateriaActual);
            filtrarYOrdenarLista();
            return true;
        });
        popup.show();
    }

    private void showSortOrderMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenu().add(Menu.NONE, 1, 1, "Más reciente");
        popup.getMenu().add(Menu.NONE, 2, 2, "Por materia");

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                ordenActual = "reciente";
                txtFiltroAntiguedad.setText("Más reciente");
            } else {
                ordenActual = "materia";
                txtFiltroAntiguedad.setText("Por materia");
            }
            filtrarYOrdenarLista();
            return true;
        });
        popup.show();
    }

    private void filtrarYOrdenarLista() {
        List<Actividad> tempLista = new ArrayList<>(listaCompletaActividades);
        String query = txtBuscar.getText().toString().toLowerCase().trim();

        // Filtrado por búsqueda
        if (!query.isEmpty()) {
            List<Actividad> buscadas = new ArrayList<>();
            for (Actividad actividad : tempLista) {
                if (actividad.getNombre().toLowerCase().contains(query)) {
                    buscadas.add(actividad);
                }
            }
            tempLista = buscadas;
        }

        // Filtrado por materia
        if (!filtroMateriaActual.equals(getString(R.string.filtro_todas_materias)) && !filtroMateriaActual.equals("Todas")) {
            List<Actividad> porMateria = new ArrayList<>();
            for (Actividad actividad : tempLista) {
                if (actividad.getMateriaNombre().equals(filtroMateriaActual)) {
                    porMateria.add(actividad);
                }
            }
            tempLista = porMateria;
        }

        // Ordenación
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Collections.sort(tempLista, (a1, a2) -> {
            if (ordenActual.equals("reciente")) {
                try {
                    Date d1 = sdf.parse(a1.getFechaEntrega());
                    Date d2 = sdf.parse(a2.getFechaEntrega());
                    return d2.compareTo(d1); // Descendente para más reciente
                } catch (ParseException e) {
                    return 0;
                }
            } else { // "materia"
                return a1.getMateriaNombre().compareTo(a2.getMateriaNombre());
            }
        });

        listaFiltradaActividades.clear();
        listaFiltradaActividades.addAll(tempLista);
        actividadAdapter.notifyDataSetChanged();
    }
}
