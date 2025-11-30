package com.navify.unibook;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

        private final TextView tvNombreActividad, tvNombreMateria, tvFechaEntrega, tvHoraEntrega;
        private final ImageView ivMateriaIcon;
        private final FrameLayout iconContainer;
        private final LinearLayout cardContainer;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreActividad = itemView.findViewById(R.id.tvNombreActividad);
            tvNombreMateria = itemView.findViewById(R.id.tvNombreMateria);
            tvFechaEntrega = itemView.findViewById(R.id.tvFechaEntrega);
            tvHoraEntrega = itemView.findViewById(R.id.tvHoraEntrega);
            ivMateriaIcon = itemView.findViewById(R.id.ivMateriaIcon);
            iconContainer = itemView.findViewById(R.id.iconContainer);
            cardContainer = itemView.findViewById(R.id.cardContainer);
        }

        void bind(final Actividad actividad) {
            tvNombreActividad.setText(actividad.getNombre());
            tvNombreMateria.setText(actividad.getMateriaNombre());
            tvFechaEntrega.setText(actividad.getFechaEntrega());
            tvHoraEntrega.setText(actividad.getHoraEntrega());

            int color = Color.parseColor(actividad.getMateriaColor());

            // Tinta el fondo de la tarjeta
            Drawable cardBackground = DrawableCompat.wrap(cardContainer.getBackground()).mutate();
            DrawableCompat.setTint(cardBackground, color);
            cardContainer.setBackground(cardBackground);

            // Tinta el contenedor del icono con un color más oscuro
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f; // Reduce el brillo
            int darkerColor = Color.HSVToColor(hsv);
            iconContainer.setBackgroundTintList(ColorStateList.valueOf(darkerColor));

            // Asigna el icono
            int iconResId = context.getResources().getIdentifier(actividad.getMateriaIcono(), "drawable", context.getPackageName());
            if (iconResId != 0) {
                ivMateriaIcon.setImageResource(iconResId);
            }
        }
    }
}
