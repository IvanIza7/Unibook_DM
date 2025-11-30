package com.navify.unibook;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

public class RegistrarActividadActivity extends AppCompatActivity {

    private EditText inputNombreMateria, inputProfesor;
    private DatabaseHelper dbHelper;

    // Vistas de la UI
    private LinearLayout layoutColores, layoutIconos;
    private LinearLayout cardPreview;
    private ImageView previewIcon;
    private TextView previewText;
    private ImageView btnBack;

    // Valores por defecto y seleccionados
    private String colorSeleccionado = "#4CAF50"; // Verde como color inicial
    private int iconoSeleccionadoId = R.drawable.ic_book;

    private final int[] iconIds = {
            R.drawable.ic_book, R.drawable.ic_home, R.drawable.ic_person, R.drawable.ic_add, R.drawable.ic_search
    };

    private View ultimoIconoSeleccionado = null;
    private View ultimoColorSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_actividad);

        dbHelper = new DatabaseHelper(this);

        // Inicializar vistas
        inputNombreMateria = findViewById(R.id.inputNombreMateria);
        inputProfesor = findViewById(R.id.inputProfesor);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnBack = findViewById(R.id.btnBack);
        layoutColores = findViewById(R.id.layoutColores);
        layoutIconos = findViewById(R.id.layoutIconos);
        cardPreview = findViewById(R.id.cardPreview);
        previewIcon = findViewById(R.id.previewIcon);
        previewText = findViewById(R.id.previewText);

        // Configurar listeners
        btnGuardar.setOnClickListener(v -> guardarMateria());
        btnBack.setOnClickListener(v -> finish());
        setupColorPicker();
        setupIconPicker();
        setupPreviewListeners();

        // Simular click en el primer color y el primer ícono para establecer el estado inicial
        if (layoutColores.getChildCount() > 0) {
            layoutColores.getChildAt(0).performClick();
        }
        if (layoutIconos.getChildCount() > 0) {
            layoutIconos.getChildAt(0).performClick();
        }
        updatePreview();
    }

    private void setupPreviewListeners() {
        inputNombreMateria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePreview();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupColorPicker() {
        for (int i = 0; i < layoutColores.getChildCount(); i++) {
            View colorView = layoutColores.getChildAt(i);
            colorView.setOnClickListener(v -> {
                if (v.getBackgroundTintList() != null) {
                    colorSeleccionado = String.format("#%06X", (0xFFFFFF & v.getBackgroundTintList().getDefaultColor()));
                    updatePreview();
                    actualizarSeleccionColor(v);
                }
            });
        }
    }

    private void setupIconPicker() {
        // Asignar los iconos dinámicamente
        for (int i = 0; i < layoutIconos.getChildCount() && i < iconIds.length; i++) {
            FrameLayout container = (FrameLayout) layoutIconos.getChildAt(i);
            ImageView iconView = (ImageView) container.getChildAt(0);
            iconView.setImageResource(iconIds[i]);

            final int index = i;
            container.setOnClickListener(v -> {
                iconoSeleccionadoId = iconIds[index];
                updatePreview();
                actualizarSeleccionIcono(v);
            });
        }
    }

    private void actualizarSeleccionColor(View nuevoColorSeleccionado) {
        if (ultimoColorSeleccionado != null) {
            ultimoColorSeleccionado.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200);
        }
        nuevoColorSeleccionado.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200);
        ultimoColorSeleccionado = nuevoColorSeleccionado;
    }

    private void actualizarSeleccionIcono(View nuevoIconoSeleccionado) {
        if (ultimoIconoSeleccionado != null) {
            ultimoIconoSeleccionado.setBackgroundResource(R.drawable.bg_icon_default);
        }
        nuevoIconoSeleccionado.setBackgroundResource(R.drawable.bg_icon_selected);
        ultimoIconoSeleccionado = nuevoIconoSeleccionado;
    }

    private void updatePreview() {
        String currentText = inputNombreMateria.getText().toString();
        previewText.setText(!currentText.isEmpty() ? currentText : getString(R.string.texto_preview_materia));

        int color = Color.parseColor(colorSeleccionado);

        // Usar DrawableCompat para no sobreescribir el drawable de fondo con esquinas redondeadas
        Drawable cardBackground = DrawableCompat.wrap(cardPreview.getBackground()).mutate();
        DrawableCompat.setTint(cardBackground, color);
        cardPreview.setBackground(cardBackground);

        // Para el fondo del ícono, usar una versión más oscura del color principal
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // Reducir brillo para un tono más oscuro
        int darkerColor = Color.HSVToColor(hsv);
        previewIcon.setBackgroundTintList(ColorStateList.valueOf(darkerColor));

        previewIcon.setImageResource(iconoSeleccionadoId);
    }

    private void guardarMateria() {
        String nombreMateria = inputNombreMateria.getText().toString().trim();
        String nombreProfesor = inputProfesor.getText().toString().trim();

        if (nombreMateria.isEmpty()) {
            Toast.makeText(this, getString(R.string.materia_nombre_obligatorio), Toast.LENGTH_SHORT).show();
            return;
        }

        String iconName = getResources().getResourceEntryName(iconoSeleccionadoId);
        long resultado = dbHelper.agregarMateria(nombreMateria, nombreProfesor, colorSeleccionado, iconName);

        if (resultado != -1) {
            Toast.makeText(this, getString(R.string.materia_guardada_exito), Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.materia_guardada_error), Toast.LENGTH_SHORT).show();
        }
    }
}