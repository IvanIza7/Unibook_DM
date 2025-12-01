package com.navify.unibook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ActividadAdapterHome extends ArrayAdapter<ActividadHome> {

    public ActividadAdapterHome(Context context, List<ActividadHome> actividades) {
        super(context, 0, actividades);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_actividad_home, parent, false);
        }

        ActividadHome actividadHome = getItem(position);

        TextView txtMateria = convertView.findViewById(R.id.txtNombreMateriaActHome);
        TextView txtTitulo = convertView.findViewById(R.id.txtTituloActividadHome);
        TextView txtFecha = convertView.findViewById(R.id.txtFechaActividadHome);
        TextView txtPorcentaje = convertView.findViewById(R.id.txtPorcentajeActHome);
        View lineaColor = convertView.findViewById(R.id.viewColorMateriaHome);

        if (actividadHome != null) {
            txtMateria.setText(actividadHome.getNombreMateria());
            txtTitulo.setText(actividadHome.getTitulo());
            txtFecha.setText("Entrega: " + actividadHome.getFechaEntrega());
            txtPorcentaje.setText(actividadHome.getPorcentaje() + "%");

            try {
                // Validamos que no sea nulo antes de intentar leerlo
                if (actividadHome.getColorMateria() != null && !actividadHome.getColorMateria().isEmpty()) {
                    int color = Color.parseColor(actividadHome.getColorMateria());
                    lineaColor.setBackgroundColor(color);
                    txtMateria.setTextColor(color);
                } else {
                    // Color por defecto si viene nulo
                    lineaColor.setBackgroundColor(Color.GRAY);
                    txtMateria.setTextColor(Color.GRAY);
                }

                // ... resto del código ...

            } catch (IllegalArgumentException e) {
                // Si el código Hex es inválido (ej: "rojo" en vez de "#FF0000")
                lineaColor.setBackgroundColor(Color.GRAY);
                txtMateria.setTextColor(Color.GRAY);
            }
        }

        return convertView;
    }
}
