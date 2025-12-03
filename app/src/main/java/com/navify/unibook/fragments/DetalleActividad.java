package com.navify.unibook.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.navify.unibook.ActividadHome;
import com.navify.unibook.DatabaseHelper;
import com.navify.unibook.R;

import java.io.File;

public class DetalleActividad extends Fragment {
    private static final String ARG_ACTIVIDAD_ID = "actividad_id";
    private int actividadId;
    private DatabaseHelper db;

    public static DetalleActividad newInstance(int actividadId) {
        DetalleActividad fragment = new DetalleActividad();
        Bundle args = new Bundle();
        args.putInt(ARG_ACTIVIDAD_ID, actividadId);
        fragment.setArguments(args);
        return fragment;
    }

    public DetalleActividad() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            actividadId = getArguments().getInt(ARG_ACTIVIDAD_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_actividad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DatabaseHelper(getContext());

        // 1. Vincular Vistas
        TextView txtMateria = view.findViewById(R.id.txtMateriaDetalle);
        TextView txtTitulo = view.findViewById(R.id.txtTituloDetalle);
        TextView txtFecha = view.findViewById(R.id.txtFechaDetalle);
        TextView txtDescripcion = view.findViewById(R.id.txtDescripcionDetalle);
        TextView txtPorcentaje = view.findViewById(R.id.txtPorcentajeNum);
        ProgressBar progressBar = view.findViewById(R.id.progressBarDetalle);
        ImageView imgEvidencia = view.findViewById(R.id.imgEvidenciaDetalle);
        ImageView btnBack = view.findViewById(R.id.btnBack);
        MaterialButton btnEditar = view.findViewById(R.id.btnEditarDetalle);
        MaterialButton btnEliminar = view.findViewById(R.id.btnEliminarDetalle);

        ActividadHome actividadHome = db.obtenerActividadporId(actividadId);

        if (actividadHome != null) {
            txtTitulo.setText(actividadHome.getTitulo());
            txtFecha.setText(actividadHome.getFechaEntrega());
            txtPorcentaje.setText(actividadHome.getPorcentaje() + "%");
            progressBar.setProgress(actividadHome.getPorcentaje());
            txtMateria.setText(actividadHome.getNombreMateria());

            // Descripción
            if (actividadHome.getDescripcion() != null && !actividadHome.getDescripcion().isEmpty()) {
                txtDescripcion.setText(actividadHome.getDescripcion());
            } else {
                txtDescripcion.setText("Sin descripción.");
            }

            // Colores
            try {
                int colorMateria = Color.parseColor(actividadHome.getColorMateria());
                int colorFondo = ColorUtils.setAlphaComponent(colorMateria, 50);
                GradientDrawable background = (GradientDrawable) txtMateria.getBackground();
                background.setColor(colorFondo);
                txtMateria.setTextColor(colorMateria);
                progressBar.getProgressDrawable().setTint(colorMateria);
            } catch (Exception e) { }

            // 5. CARGAR FOTO (Lógica Corregida)
            String rutaFoto = actividadHome.getFotoUri();
            File imgFile = null;

            if (rutaFoto != null && !rutaFoto.isEmpty()) {
                imgFile = new File(rutaFoto);
            }

            // VERIFICAMOS: ¿Hay archivo válido?
            if (imgFile != null && imgFile.exists() && imgFile.length() > 0) {

                // --- CASO A: SÍ HAY FOTO ---
                // 1. Limpiamos cualquier filtro de color
                imgEvidencia.clearColorFilter();
                // 2. Quitamos el relleno
                imgEvidencia.setPadding(0, 0, 0, 0);
                // 3. Ajustamos la escala
                imgEvidencia.setScaleType(ImageView.ScaleType.CENTER_CROP);

                // 4. Cargamos con Glide
                Glide.with(this)
                        .load(imgFile)
                        .into(imgEvidencia);

            } else {

                // --- CASO B: NO HAY FOTO (O archivo vacío) ---
                // Restauramos el estilo de "Placeholder" (Icono verde)
                imgEvidencia.setImageResource(android.R.drawable.ic_menu_gallery);

                // Aplicamos el tinte verde programáticamente
                imgEvidencia.setColorFilter(Color.parseColor("#A5D6A7"));

                // Agregamos padding para que se vea como ícono pequeño
                imgEvidencia.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                int paddingPixel = 200; // Un valor aproximado a 60-80dp
                imgEvidencia.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
            }
        } else {
            Toast.makeText(getContext(), "Error al cargar actividad", Toast.LENGTH_SHORT).show();
        }

        // Botones
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) getParentFragmentManager().popBackStack();
        });

        btnEditar.setOnClickListener(v -> {
            EditarActividad editarActividad = new EditarActividad();
            Bundle args = new Bundle();
            args.putInt("actividad_id", actividadId);
            editarActividad.setArguments(args);
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, editarActividad)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnEliminar.setOnClickListener(v -> mostrarDialogoEliminar());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottomNavigation); // O customBottomNav
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottomNavigation);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarDialogoEliminar() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Eliminar Actividad")
                .setMessage("¿Estás seguro de que deseas eliminar esta actividad? No se podrá recuperar.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    // Llamamos a la BD para borrar
                    db.eliminarActividad(actividadId); // Asegúrate de tener este método en DBHelper (te lo paso abajo)

                    Toast.makeText(getContext(), "Actividad eliminada", Toast.LENGTH_SHORT).show();

                    // Regresamos a la pantalla anterior
                    if (getParentFragmentManager() != null) {
                        getParentFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}