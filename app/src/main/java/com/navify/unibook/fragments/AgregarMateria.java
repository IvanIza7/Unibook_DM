package com.navify.unibook.fragments; 

import android.graphics.Color;
import android.content.res.ColorStateList;
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
    private View cardPreviewContainer;
    private ImageView btnBack;

    // Variables de selección
    private int selectedColorInt;
    private String selectedColorHex = "#E9F5EA";
    private int selectedIconResId = R.drawable.ic_book;

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

        selectedColorInt = getResources().getColor(R.color.colorPastelGreen, null);
        selectedColorHex = String.format("#%06X", (0xFFFFFF & selectedColorInt));

        etNombre = view.findViewById(R.id.inputNombre);
        etProfesor = view.findViewById(R.id.inputProfesor);
        previewIconImage = view.findViewById(R.id.pvwIcon);
        previewText = view.findViewById(R.id.txtMateriaPrevia);
        cardPreviewContainer = view.findViewById(R.id.cardPreview);
        btnBack = view.findViewById(R.id.btnBack);

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

        view.findViewById(R.id.btnGuardarMateria).setOnClickListener(v -> guardarMateria());
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

    private void guardarMateria() {
        String nombre = etNombre.getText().toString().trim();
        String profesor = etProfesor.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("Nombre obligatorio");
            return;
        }

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
