package com.navify.unibook.fragments;

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

    // Variables para guardar la selección (valores por defecto)
    private int selectedColorInt = Color.parseColor("#4CAF50"); // Verde
    private String selectedColorHex = "#4CAF50"; // Para enviar a la BD
    private int selectedIconResId = R.drawable.ic_book; // ID del recurso (asegúrate que exista en drawable)

    // Instancia de tu base de datos
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

        // 1. Inicializar Base de Datos
        dbHelper = new DatabaseHelper(getContext());

        // 2. Vincular vistas
        etNombre = view.findViewById(R.id.inputNombre);
        etProfesor = view.findViewById(R.id.inputProfesor);
        previewIconImage = view.findViewById(R.id.pvwIcon);
        previewText = view.findViewById(R.id.txtMateriaPrevia);
        btnBack = view.findViewById(R.id.btnBack);

        // 3. Botón Atrás
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        // 4. Configurar Clicks de Colores (Actualiza variable y vista previa)
        setupColorClick(view.findViewById(R.id.color1), "#FF5252"); // Rojo
        setupColorClick(view.findViewById(R.id.color2), "#4CAF50"); // Verde
        setupColorClick(view.findViewById(R.id.color3), "#2196F3"); // Azul
        setupColorClick(view.findViewById(R.id.color4), "#FFC107"); // Amarillo
        setupColorClick(view.findViewById(R.id.color5), "#9C27B0"); // Morado

        // 5. Actualizar texto de vista previa mientras escribes
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

        // 6. Botón Guardar
        view.findViewById(R.id.btnGuardarMateria).setOnClickListener(v -> guardarMateria());
    }

    // Método para manejar selección de color
    private void setupColorClick(View view, String colorHex) {
        view.setOnClickListener(v -> {
            // Guardamos el HEX para la base de datos
            selectedColorHex = colorHex;
            // Guardamos el INT para pintar la vista previa ahora mismo
            selectedColorInt = Color.parseColor(colorHex);

            // Pintamos el fondo del ícono en la vista previa
            previewIconImage.getBackground().setTint(selectedColorInt);
        });
    }

    private void guardarMateria() {
        String nombre = etNombre.getText().toString().trim();
        String profesor = etProfesor.getText().toString().trim();

        // Validación: El nombre es obligatorio
        if (nombre.isEmpty()) {
            etNombre.setError("Escribe el nombre de la materia");
            etNombre.requestFocus();
            return;
        }

        // Obtener el nombre del recurso del icono (ej. "ic_book") para guardarlo como TEXT
        // Esto permite recuperarlo después con getIdentifier
        String nombreIcono = getResources().getResourceEntryName(selectedIconResId);

        // ==========================================
        // INSERTAR EN BASE DE DATOS
        // ==========================================
        long idResult = dbHelper.agregarMateria(nombre, profesor, selectedColorHex, nombreIcono);

        if (idResult > 0) {
            Toast.makeText(getContext(), "¡Materia guardada con éxito!", Toast.LENGTH_SHORT).show();

            // Cerrar el fragment y volver a la lista
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        } else {
            Toast.makeText(getContext(), "Error al guardar la materia", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cerrar conexión BD si está abierta (buena práctica)
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}