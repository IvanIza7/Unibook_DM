package com.navify.unibook.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.navify.unibook.DatabaseHelper;
import com.navify.unibook.R;
import com.navify.unibook.Actividad;
import com.navify.unibook.Materia;

import java.util.List;

public class PerfilFragment extends Fragment {

    private DatabaseHelper db;
    private TextView tvMateriasCount;
    private TextView tvActividadesCount;
    private LinearLayout themeLightSelector;
    private LinearLayout themeDarkSelector;
    private ImageView ivCheckLight;
    private ImageView ivCheckDark;

    public PerfilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        db = new DatabaseHelper(getContext());
        tvMateriasCount = view.findViewById(R.id.tvMateriasCount);
        tvActividadesCount = view.findViewById(R.id.tvActividadesCount);
        
        themeLightSelector = view.findViewById(R.id.themeLightSelector);
        themeDarkSelector = view.findViewById(R.id.themeDarkSelector);
        
        ivCheckLight = view.findViewById(R.id.ivCheckLight);
        ivCheckDark = view.findViewById(R.id.ivCheckDark);

        // Configurar estado inicial de los checks según el tema actual
        actualizarEstadoChecks();

        // Configurar listeners para cambio de tema
        themeLightSelector.setOnClickListener(v -> {
            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                // No necesitamos llamar actualizarEstadoChecks() aquí porque la activity se recreará
            }
        });

        themeDarkSelector.setOnClickListener(v -> {
            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                // No necesitamos llamar actualizarEstadoChecks() aquí porque la activity se recreará
            }
        });

        // Configurar botón Eliminar Datos
        View btnEliminarDatos = view.findViewById(R.id.btnEliminarDatos);
        if (btnEliminarDatos != null) {
            btnEliminarDatos.setOnClickListener(v -> confirmarEliminarDatos());
        }

        cargarEstadisticas();

        return view;
    }

    private void actualizarEstadoChecks() {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            // Modo Oscuro Activo
            ivCheckLight.setVisibility(View.INVISIBLE);
            ivCheckDark.setVisibility(View.VISIBLE);
        } else {
            // Modo Claro Activo (por defecto o explícito)
            ivCheckLight.setVisibility(View.VISIBLE);
            ivCheckDark.setVisibility(View.INVISIBLE);
        }
    }

    private void confirmarEliminarDatos() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.zona_peligro)
                .setMessage(R.string.advertencia_eliminar_datos)
                .setPositiveButton(R.string.btn_eliminar_todos_datos, (dialog, which) -> {
                    eliminarTodosLosDatos();
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void eliminarTodosLosDatos() {
        // Eliminar todas las materias (y sus actividades en cascada)
        List<Materia> materias = db.obtenerTodasLasMaterias();
        for (Materia m : materias) {
            db.eliminarMateria(m.getId());
        }

        Toast.makeText(getContext(), R.string.datos_eliminados_exito, Toast.LENGTH_LONG).show();
        cargarEstadisticas(); // Actualizar contadores a 0
    }

    private void cargarEstadisticas() {
        if (db == null) return;
        
        List<Materia> materias = db.obtenerTodasLasMaterias();
        List<Actividad> actividades = db.getTodasLasActividades();

        if (tvMateriasCount != null) {
            tvMateriasCount.setText(String.valueOf(materias.size()));
        }
        
        if (tvActividadesCount != null) {
            tvActividadesCount.setText(String.valueOf(actividades.size()));
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        cargarEstadisticas();
    }
}
