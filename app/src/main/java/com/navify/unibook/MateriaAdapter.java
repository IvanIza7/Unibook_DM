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
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_materia, parent, false);
        }

        Materia materia = getItem(position);

        TextView txtNombre = convertView.findViewById(R.id.txtNombreMateria);
        TextView txtProfesor = convertView.findViewById(R.id.txtProfesorMateria);
        ImageView imgIcono = convertView.findViewById(R.id.imgIconoMateria);

        if (materia != null) {
            txtNombre.setText(materia.getNombre());

            if (materia.getProfesor().isEmpty()) {
                txtProfesor.setVisibility(View.GONE);
            } else {
                txtProfesor.setText(materia.getProfesor());
                txtProfesor.setVisibility(View.VISIBLE);
            }

            try {
                int color = Color.parseColor(materia.getColor());

                convertView.getBackground().mutate().setTint(color);

                int colorAccent = getContext().getResources().getColor(R.color.colorAccent, null);
                imgIcono.setColorFilter(colorAccent); 
                
            } catch (Exception e) {

            }

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
