package com.navify.unibook.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.navify.unibook.DatabaseHelper;
import com.navify.unibook.Materia;
import com.navify.unibook.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgregarActividad extends Fragment {
    private TextInputEditText edtTitulo, edtFecha, edtDescripcion;
    private Spinner spinnerMaterias;
    private SeekBar seekBarAvance;
    private TextView txtPorcentajeValor;
    private MaterialButton btnTomarFoto;
    private ImageView imgPreviewFoto;
    private ImageView btnBack;

    private DatabaseHelper db;
    private List<Integer> listaIdMaterias = new ArrayList<>();
    private List<String> listaNombresMaterias = new ArrayList<>();

    private int porcentajeSeleccionado = 0;
    private String uriFoto = "";

    public AgregarActividad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment_agregar_actividad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DatabaseHelper(getContext());

        edtTitulo = view.findViewById(R.id.inputTitulo);
        edtDescripcion = view.findViewById(R.id.inputDescripcion);
        edtFecha = view.findViewById(R.id.inputFecha);
        spinnerMaterias = view.findViewById(R.id.spinnerMaterias);
        seekBarAvance = view.findViewById(R.id.seekBarAvance);
        txtPorcentajeValor = view.findViewById(R.id.txtPorcentajeValor);
        btnTomarFoto = view.findViewById(R.id.btnTomarFoto);
        imgPreviewFoto = view.findViewById(R.id.imgPreviewFoto);
        btnBack = view.findViewById(R.id.btnBack);

        edtFecha.setOnClickListener(v -> mostrarCalendario());

        seekBarAvance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progreso, boolean fromUser) {
                porcentajeSeleccionado = progreso;
                txtPorcentajeValor.setText(progreso + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnTomarFoto.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Tomar Foto", Toast.LENGTH_SHORT).show();

            imgPreviewFoto.setVisibility(View.VISIBLE);
            uriFoto = "ruta_simulada";
        });

        cargarMateriasenSpinner();

        view.findViewById(R.id.btnGuardarActividad).setOnClickListener(v -> guardarActividad());

        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager() != null)
                getParentFragmentManager().popBackStack();
        });
    }

    private void mostrarCalendario() {
        final Calendar calendario = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                    edtFecha.setText(fechaSeleccionada);
                }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void cargarMateriasenSpinner() {
        List<Materia> materias = db.obtenerTodasLasMaterias();
        listaIdMaterias.clear();
        listaNombresMaterias.clear();

        if (materias.isEmpty()) {
            listaNombresMaterias.add("No hay materias registradas");
        } else {
            for (Materia m : materias) {
                listaNombresMaterias.add(m.getNombre());
                listaIdMaterias.add(m.getId());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listaNombresMaterias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaterias.setAdapter(adapter);
    }

    private void guardarActividad() {
        String titulo = edtTitulo.getText().toString().trim();
        String descripcion = edtDescripcion.getText().toString().trim();
        String fecha = edtFecha.getText().toString().trim();

        if (titulo.isEmpty()) {
            edtTitulo.setError("Falta titulo");
            return;
        }
        if (fecha.isEmpty()) {
            edtFecha.setError("Falta fecha");
            return;
        }
        if (listaIdMaterias.isEmpty()) {
            Toast.makeText(getContext(), "Registra una materia primero", Toast.LENGTH_LONG).show();
            return;
        }

        int posicionSpinner = spinnerMaterias.getSelectedItemPosition();
        int idMateria = listaIdMaterias.get(posicionSpinner);

        long id = db.agregarActividad(
                titulo,
                descripcion,
                "HOY",
                porcentajeSeleccionado,
                uriFoto,
                fecha,
                idMateria);

        if (id > 0) {
            Toast.makeText(getContext(), "Actividad guardada con " +porcentajeSeleccionado + " % de avance", Toast.LENGTH_SHORT).show();
            if (getParentFragmentManager() != null)
                getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            View custombottomNav = getActivity().findViewById(R.id.customBottomNav);
            if (custombottomNav != null)
                    custombottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            View custombottomNav = getActivity().findViewById(R.id.customBottomNav);
            if (custombottomNav != null)
                custombottomNav.setVisibility(View.VISIBLE);
        }
    }
}