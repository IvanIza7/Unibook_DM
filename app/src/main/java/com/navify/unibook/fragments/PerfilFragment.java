package com.navify.unibook.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navify.unibook.DatabaseHelper;
import com.navify.unibook.R;
import com.navify.unibook.Actividad;
import com.navify.unibook.Materia;

import java.util.List;

public class PerfilFragment extends Fragment {

    private DatabaseHelper db;
    private TextView tvMateriasCount;
    private TextView tvActividadesCount;

    public PerfilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        db = new DatabaseHelper(getContext());
        tvMateriasCount = view.findViewById(R.id.tvMateriasCount);
        tvActividadesCount = view.findViewById(R.id.tvActividadesCount);

        List<Materia> materias = db.obtenerTodasLasMaterias();
        List<Actividad> actividades = db.getTodasLasActividades();

        tvMateriasCount.setText(String.valueOf(materias.size()));
        tvActividadesCount.setText(String.valueOf(actividades.size()));

        return view;
    }
}
