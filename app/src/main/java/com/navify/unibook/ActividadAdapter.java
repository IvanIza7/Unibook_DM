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
            // 1. Validaciones básicas de texto para evitar "null" en pantalla
            if (actividad.getNombre() != null) tvNombreActividad.setText(actividad.getNombre());
            // Nota: Verifica si tu objeto Actividad tiene getMateriaNombre() o getNombreMateria()
            // Si te marca error en rojo aquí, ajusta el nombre del getter según TU clase Actividad.
            // tvNombreMateria.setText(actividad.getMateriaNombre());

            if (actividad.getFechaEntrega() != null) tvFechaEntrega.setText(actividad.getFechaEntrega());
            // tvHoraEntrega.setText(actividad.getHoraEntrega());

            // 2. PROTECCIÓN DE COLOR (Evita crash si el color viene mal o nulo)
            int color;
            try {
                String colorHex = actividad.getMateriaColor();
                if (colorHex != null && !colorHex.isEmpty()) {
                    color = Color.parseColor(colorHex);
                } else {
                    color = Color.GRAY; // Color por defecto si no hay
                }
            } catch (Exception e) {
                color = Color.GRAY; // Color por defecto si el código Hex está mal
            }

            // 3. SOLUCIÓN DEL CRASH (Solo intentamos pintar si EXISTE un fondo)
            if (cardContainer.getBackground() != null) {
                Drawable cardBackground = DrawableCompat.wrap(cardContainer.getBackground()).mutate();
                DrawableCompat.setTint(cardBackground, color);
                cardContainer.setBackground(cardBackground);
            } else {
                // Si no hay fondo en el XML, le ponemos un color de fondo plano para que se vea algo
                cardContainer.setBackgroundColor(color);
            }

            // 4. Tinta el contenedor del icono (con validación)
            if (iconContainer != null) {
                float[] hsv = new float[3];
                Color.colorToHSV(color, hsv);
                hsv[2] *= 0.8f; // Reduce el brillo
                int darkerColor = Color.HSVToColor(hsv);
                iconContainer.setBackgroundTintList(ColorStateList.valueOf(darkerColor));
            }

            // 5. Asigna el icono (con validación)
            // Asegúrate que getMateriaIcono() devuelva el nombre del recurso (ej: "ic_book")
            if (actividad.getMateriaIcono() != null) {
                int iconResId = context.getResources().getIdentifier(
                        actividad.getMateriaIcono(), "drawable", context.getPackageName());

                if (iconResId != 0) {
                    ivMateriaIcon.setImageResource(iconResId);
                } else {
                    // Icono por defecto si no encuentra la imagen
                    ivMateriaIcon.setImageResource(R.drawable.ic_book);
                }
            }
        }
    }
}
