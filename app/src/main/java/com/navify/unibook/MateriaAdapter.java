package com.navify.unibook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MateriaAdapter extends ArrayAdapter<Materia> {

    public MateriaAdapter(Context context, List<Materia> materias) {
        super(context, 0, materias);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Inflar la vista si no existe
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_materia, parent, false);
        }

        // 2. Obtener la materia actual
        Materia materia = getItem(position);

        // 3. Obtener referencias a los elementos visuales
        TextView txtNombre = convertView.findViewById(R.id.txtNombreMateria);
        TextView txtProfesor = convertView.findViewById(R.id.txtProfesorMateria);
        ImageView imgIcono = convertView.findViewById(R.id.imgIconoMateria);
        // Ya no obtenemos fondoIcono para teñirlo, se queda estático

        // 4. Asignar datos
        if (materia != null) {
            txtNombre.setText(materia.getNombre());

            if (materia.getProfesor().isEmpty()) {
                txtProfesor.setVisibility(View.GONE);
            } else {
                txtProfesor.setText(materia.getProfesor());
                txtProfesor.setVisibility(View.VISIBLE);
            }

            // --- MAGIA DEL COLOR ---
            try {
                int color = Color.parseColor(materia.getColor());
                
                // CAMBIO: Teñimos el fondo de toda la tarjeta (convertView)
                // El drawable bg_round_card es el background del layout raíz en item_materia.xml
                convertView.getBackground().mutate().setTint(color);

                // CAMBIO: El ícono ahora usa el color de acento (o blanco si prefieres, pero acento destaca más sobre pastel)
                // Usaremos Color Accent definido en recursos
                int colorAccent = getContext().getResources().getColor(R.color.colorAccent, null);
                imgIcono.setColorFilter(colorAccent); 
                
            } catch (Exception e) {
                // Color por defecto si falla
            }

            // --- MAGIA DEL ÍCONO ---
            int resId = getContext().getResources().getIdentifier(
                    materia.getIcono(), "drawable", getContext().getPackageName());

            if (resId != 0) {
                imgIcono.setImageResource(resId);
            } else {
                imgIcono.setImageResource(R.drawable.ic_book); 
            }
        }

        return convertView;
    }
}
