package com.navify.unibook.fragments;

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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.navify.unibook.DatabaseHelper;
import com.navify.unibook.Materia;
import com.navify.unibook.R;

public class EditarMateria extends Fragment {

    // Componentes de la interfaz
    private TextInputEditText etNombre, etProfesor;
    private ImageView previewIconImage;
    private TextView previewText;
    private View cardPreviewContainer;
    private ImageView btnBack;

    // Variables de selección (Valores por defecto)
    private int selectedColorInt;
    private String selectedColorHex;
    private int selectedIconResId = R.drawable.ic_book;

    private DatabaseHelper dbHelper;

    // ID de la materia a editar
    private int materiaId = -1; 

    public EditarMateria() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_materia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            materiaId = getArguments().getInt("materia_id", -1);
        }

        selectedColorInt = getResources().getColor(R.color.colorPastelGreen, null);
        selectedColorHex = String.format("#%06X", (0xFFFFFF & selectedColorInt));

        etNombre = view.findViewById(R.id.inputNombre);
        etProfesor = view.findViewById(R.id.inputProfesor);
        previewIconImage = view.findViewById(R.id.pvwIcon);
        previewText = view.findViewById(R.id.txtMateriaPrevia);
        cardPreviewContainer = view.findViewById(R.id.cardPreview);

        btnBack = view.findViewById(R.id.btnBack1);

        if (materiaId != -1) {
            cargarDatosMateria(materiaId);
        }

        actualizarVistaPrevia();

        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) getParentFragmentManager().popBackStack();
        });

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

        setupColorClick(view.findViewById(R.id.color1));
        setupColorClick(view.findViewById(R.id.color2));
        setupColorClick(view.findViewById(R.id.color3));
        setupColorClick(view.findViewById(R.id.color4));
        setupColorClick(view.findViewById(R.id.color5));
        setupColorClick(view.findViewById(R.id.color6));
        setupColorClick(view.findViewById(R.id.color7));

        setupIconClick(view.findViewById(R.id.iconOption1), R.drawable.ic_book);
        setupIconClick(view.findViewById(R.id.iconOption2), R.drawable.ic_calculator);
        setupIconClick(view.findViewById(R.id.iconOption3), R.drawable.ic_science);
        setupIconClick(view.findViewById(R.id.iconOption4), R.drawable.ic_code);
        setupIconClick(view.findViewById(R.id.iconOption5), R.drawable.ic_computer);

        view.findViewById(R.id.btnGuardarMateria).setOnClickListener(v -> guardarCambiosMateria());
        view.findViewById(R.id.btnEliminarMateria).setOnClickListener(v -> confirmarEliminarMateria());
    }

    private void cargarDatosMateria(int id) {
        Materia materia = dbHelper.obtenerMateria(id);
        if (materia != null) {
            etNombre.setText(materia.getNombre());
            etProfesor.setText(materia.getProfesor());
            
            selectedColorHex = materia.getColor();
            try {
                selectedColorInt = Color.parseColor(selectedColorHex);
            } catch (Exception e) {
                selectedColorInt = Color.GRAY;
            }

            String iconName = materia.getIcono();
            int resId = getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
            if (resId != 0) {
                selectedIconResId = resId;
            }

            actualizarVistaPrevia();
        }
    }

    private void setupColorClick(View view) {
        if (view == null) return; 
        view.setOnClickListener(v -> {

            ColorStateList tintList = view.getBackgroundTintList();
            if (tintList != null) {
                selectedColorInt = tintList.getDefaultColor();

                selectedColorHex = String.format("#%06X", (0xFFFFFF & selectedColorInt));
                actualizarVistaPrevia();
            }
        });
    }

    private void setupIconClick(View view, int iconResId) {
        if (view == null) return; 
        view.setOnClickListener(v -> {
            selectedIconResId = iconResId;
            actualizarVistaPrevia();
        });
    }

    private void actualizarVistaPrevia() {
        previewIconImage.setImageResource(selectedIconResId);

        if (cardPreviewContainer != null && cardPreviewContainer.getBackground() != null) {
            cardPreviewContainer.getBackground().mutate().setTint(selectedColorInt);
        }

        previewIconImage.setColorFilter(getResources().getColor(R.color.colorAccent, null));
    }

    private void guardarCambiosMateria() {
        String nombre = etNombre.getText().toString().trim();
        String profesor = etProfesor.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("Nombre obligatorio");
            return;
        }
        
        if (materiaId == -1) {
            Toast.makeText(getContext(), "Error: No se puede editar (Falta ID)", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombreIcono = getResources().getResourceEntryName(selectedIconResId);
        
        dbHelper.actualizarMateria(materiaId, nombre, profesor, selectedColorHex, nombreIcono);
        Toast.makeText(getContext(), "Materia actualizada", Toast.LENGTH_SHORT).show();
        if (getParentFragmentManager() != null) getParentFragmentManager().popBackStack();
    }

    private void confirmarEliminarMateria() {
        new AlertDialog.Builder(getContext())
            .setTitle("Eliminar Materia")
            .setMessage("¿Estás seguro de eliminar esta materia? Se borrarán también sus actividades.")
            .setPositiveButton("Eliminar", (dialog, which) -> {
                eliminarMateria();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void eliminarMateria() {
        if (materiaId != -1) {
            dbHelper.eliminarMateria(materiaId);
            Toast.makeText(getContext(), "Materia eliminada", Toast.LENGTH_SHORT).show();
            if (getParentFragmentManager() != null) getParentFragmentManager().popBackStack();
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
