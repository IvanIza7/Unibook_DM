package com.navify.unibook;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder> {

    private final List<Actividad> actividades;
    private final Context context;

    public ActividadAdapter(Context context, List<Actividad> actividades) {
        this.context = context;
        this.actividades = actividades;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_actividad, parent, false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        Actividad actividad = actividades.get(position);
        holder.bind(actividad);
    }

    @Override
    public int getItemCount() {
        return actividades.size();
    }

    class ActividadViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNombreActividad, tvNombreMateria, tvFechaEntrega, tvHoraEntrega, tvPorcentaje;
        private final ImageView ivMateriaIcon;
        private final FrameLayout iconContainer;
        private final ProgressBar progressBar;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreActividad = itemView.findViewById(R.id.tvNombreActividad);
            tvNombreMateria = itemView.findViewById(R.id.tvNombreMateria);
            tvFechaEntrega = itemView.findViewById(R.id.tvFechaEntrega);
            tvHoraEntrega = itemView.findViewById(R.id.tvHoraEntrega);
            ivMateriaIcon = itemView.findViewById(R.id.ivMateriaIcon);
            iconContainer = itemView.findViewById(R.id.iconContainer);
            progressBar = itemView.findViewById(R.id.progressBar);
            tvPorcentaje = itemView.findViewById(R.id.tvPorcentaje);
        }

        void bind(final Actividad actividad) {
            // 1. Asignar textos
            tvNombreActividad.setText(actividad.getNombre());
            tvNombreMateria.setText(actividad.getMateriaNombre());
            tvFechaEntrega.setText(actividad.getFechaEntrega());
            tvHoraEntrega.setText(actividad.getHoraEntrega());

            // 2. Asignar barra de progreso y porcentaje
            int porcentaje = actividad.getPorcentaje();
            progressBar.setProgress(porcentaje);
            tvPorcentaje.setText(String.format(Locale.getDefault(), "%d%%", porcentaje));

            // 3. Asignar colores
            int color;
            try {
                String colorHex = actividad.getMateriaColor();
                if (colorHex != null && !colorHex.isEmpty()) {
                    color = Color.parseColor(colorHex);
                } else {
                    color = ContextCompat.getColor(context, R.color.colorAccent);
                }
            } catch (IllegalArgumentException e) {
                color = ContextCompat.getColor(context, R.color.colorAccent);
            }

            if (iconContainer != null) {
                iconContainer.setBackgroundTintList(ColorStateList.valueOf(color));
            }

            if (progressBar != null) {
                progressBar.setProgressTintList(ColorStateList.valueOf(color));
            }

            // 4. Asignar icono
            if (actividad.getMateriaIcono() != null && !actividad.getMateriaIcono().isEmpty()) {
                int iconResId = context.getResources().getIdentifier(
                        actividad.getMateriaIcono(), "drawable", context.getPackageName());
                if (iconResId != 0) {
                    ivMateriaIcon.setImageResource(iconResId);
                } else {
                    ivMateriaIcon.setImageResource(R.drawable.ic_book);
                }
            } else {
                ivMateriaIcon.setImageResource(R.drawable.ic_book);
            }
        }
    }
}
