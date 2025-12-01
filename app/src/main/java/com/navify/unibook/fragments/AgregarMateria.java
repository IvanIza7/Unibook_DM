package com.navify.unibook.fragments; // <--- CONFIRMA QUE ESTE SEA TU PAQUETE

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.navify.unibook.DatabaseHelper;
import com.navify.unibook.R;

public class AgregarMateria extends Fragment {

    // Componentes de la interfaz
    private TextInputEditText etNombre, etProfesor;
    private ImageView previewIconImage;
    private TextView previewText;
    private ImageView btnBack;

    // Variables de selección (Valores por defecto)
    private int selectedColorInt = Color.parseColor("#4CAF50"); // Verde default
    private String selectedColorHex = "#4CAF50";
    private int selectedIconResId = R.drawable.ic_book; // Icono default

    private DatabaseHelper dbHelper;

    public AgregarMateria() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agregar_materia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getContext());

        // 1. VINCULACIÓN (Asegúrate que estos IDs existan en tu XML)
        etNombre = view.findViewById(R.id.inputNombre);
        etProfesor = view.findViewById(R.id.inputProfesor);
        previewIconImage = view.findViewById(R.id.pvwIcon);
        previewText = view.findViewById(R.id.txtMateriaPrevia);
        btnBack = view.findViewById(R.id.btnBack);

        // 2. CONFIGURACIÓN INICIAL DE VISTA PREVIA
        actualizarVistaPrevia();

        // 3. BOTÓN ATRÁS
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) getParentFragmentManager().popBackStack();
        });

        // 4. LISTENER DE TEXTO (NOMBRE)
        etNombre.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    previewText.setText(s.toString());
                } else {
                    previewText.setText("Nombre de la materia");
                }
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // 5. LISTENERS DE COLORES (Aquí corregimos con .mutate())
        setupColorClick(view.findViewById(R.id.color1), "#FF5252");
        setupColorClick(view.findViewById(R.id.color2), "#4CAF50");
        setupColorClick(view.findViewById(R.id.color3), "#2196F3");
        setupColorClick(view.findViewById(R.id.color4), "#FFC107");
        setupColorClick(view.findViewById(R.id.color5), "#9C27B0");
        setupColorClick(view.findViewById(R.id.color6), "#F875AA");
        setupColorClick(view.findViewById(R.id.color7), "#F4F754");

        // 6. [NUEVO] LISTENERS DE ÍCONOS
        // Asegúrate de que en tu XML los FrameLayout de los iconos tengan estos IDs:
        // iconOption1, iconOption2, iconOption3...
        setupIconClick(view.findViewById(R.id.iconOption1), R.drawable.ic_book);
        setupIconClick(view.findViewById(R.id.iconOption2), R.drawable.ic_calculator);
        setupIconClick(view.findViewById(R.id.iconOption3), R.drawable.ic_science);
        setupIconClick(view.findViewById(R.id.iconOption4), R.drawable.ic_code);
        setupIconClick(view.findViewById(R.id.iconOption5), R.drawable.ic_computer);

        // 7. BOTÓN GUARDAR
        view.findViewById(R.id.btnGuardarMateria).setOnClickListener(v -> guardarMateria());
    }

    // --- MÉTODOS AUXILIARES ---

    private void setupColorClick(View view, String colorHex) {
        if (view == null) return; // Evita crash si el ID no existe
        view.setOnClickListener(v -> {
            selectedColorHex = colorHex;
            selectedColorInt = Color.parseColor(colorHex);
            actualizarVistaPrevia();
        });
    }

    private void setupIconClick(View view, int iconResId) {
        if (view == null) return; // Evita crash si no existe
        view.setOnClickListener(v -> {
            selectedIconResId = iconResId;
            actualizarVistaPrevia();
        });
    }

    private void actualizarVistaPrevia() {
        // Actualizamos el icono
        previewIconImage.setImageResource(selectedIconResId);

        // Actualizamos el color de fondo del círculo
        // .mutate() es CLAVE: asegura que no cambiemos el color de todos los círculos de la app, solo este
        previewIconImage.getBackground().mutate().setTint(selectedColorInt);

        // Opcional: Cambiar el tinte del icono mismo a blanco o dejarlo acento
        previewIconImage.setColorFilter(Color.WHITE);
    }

    private void guardarMateria() {
        String nombre = etNombre.getText().toString().trim();
        String profesor = etProfesor.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("Nombre obligatorio");
            return;
        }

        // Recuperamos el nombre del recurso (ej. "ic_book") para guardarlo como texto
        String nombreIcono = getResources().getResourceEntryName(selectedIconResId);

        long id = dbHelper.agregarMateria(nombre, profesor, selectedColorHex, nombreIcono);

        if (id > 0) {
            Toast.makeText(getContext(), "Guardado correctamente", Toast.LENGTH_SHORT).show();
            if (getParentFragmentManager() != null) getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.customBottomNav);
            if (bottomNav != null) {
                bottomNav.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.customBottomNav);
            if (bottomNav != null) {
                bottomNav.setVisibility(View.VISIBLE);
            }
        }
    }
}