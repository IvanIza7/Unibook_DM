package com.navify.unibook.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.navify.unibook.Actividad;
import com.navify.unibook.ActividadAdapter;
import com.navify.unibook.DatabaseHelper;
import com.navify.unibook.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityFragment extends Fragment {

    private RecyclerView listActividades;
    private ActividadAdapter actividadAdapter;
    private DatabaseHelper dbHelper;

    private List<Actividad> listaCompletaActividades;
    private final List<Actividad> listaFiltradaActividades = new ArrayList<>();
    private List<String> listaMaterias;

    private EditText txtBuscar;
    private LinearLayout btnFiltroMateria, btnFiltroAntiguedad;
    private TextView txtFiltroMateria, txtFiltroAntiguedad;

    private String filtroMateriaActual;
    private String ordenActual = "reciente"; // "reciente" o "porcentaje"

    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actividades, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getContext());

        initViews(view);
        // Inicializar el filtro con el valor del recurso
        filtroMateriaActual = getString(R.string.filtro_todas_materias);
        txtFiltroMateria.setText(filtroMateriaActual);

        setupListeners();
        cargarDatos();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar datos por si hay cambios
        cargarDatos();
    }

    private void initViews(View view) {
        listActividades = view.findViewById(R.id.listActividades);
        listActividades.setLayoutManager(new LinearLayoutManager(getContext()));

        txtBuscar = view.findViewById(R.id.txtBuscar);
        btnFiltroMateria = view.findViewById(R.id.btnFiltroMateria);
        btnFiltroAntiguedad = view.findViewById(R.id.btnFiltroAntiguedad);
        txtFiltroMateria = view.findViewById(R.id.txtFiltroMateria);
        txtFiltroAntiguedad = view.findViewById(R.id.txtFiltroAntiguedad);
    }

    private void setupListeners() {
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
        listaCompletaActividades = dbHelper.getTodasLasActividades();
        listaMaterias = dbHelper.getNombresDeTodasLasMaterias();

        if (actividadAdapter == null) {
            actividadAdapter = new ActividadAdapter(getContext(), listaFiltradaActividades);
            listActividades.setAdapter(actividadAdapter);
        }
        filtrarYOrdenarLista();
    }

    private void showMateriaFilterMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenu().add(Menu.NONE, Menu.NONE, 0, getString(R.string.filtro_todas_materias));
        if (listaMaterias != null) {
            for (int i = 0; i < listaMaterias.size(); i++) {
                popup.getMenu().add(Menu.NONE, i + 1, i + 1, listaMaterias.get(i));
            }
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
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenu().add(Menu.NONE, 1, 1, "Más reciente");
        popup.getMenu().add(Menu.NONE, 2, 2, "Porcentaje");

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                ordenActual = "reciente";
                txtFiltroAntiguedad.setText("Más reciente");
            } else {
                ordenActual = "porcentaje";
                txtFiltroAntiguedad.setText("Porcentaje");
            }
            filtrarYOrdenarLista();
            return true;
        });
        popup.show();
    }

    private void filtrarYOrdenarLista() {
        if (listaCompletaActividades == null) return;
        List<Actividad> tempLista = new ArrayList<>(listaCompletaActividades);
        if (txtBuscar != null) {
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
        }


        // Filtrado por materia
        if (!filtroMateriaActual.equals(getString(R.string.filtro_todas_materias))) {
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
            } else { // "porcentaje"
                return Integer.compare(a2.getPorcentaje(), a1.getPorcentaje()); // Descendente
            }
        });

        listaFiltradaActividades.clear();
        listaFiltradaActividades.addAll(tempLista);
        if (actividadAdapter != null) {
            actividadAdapter.notifyDataSetChanged();
        }
    }
}
