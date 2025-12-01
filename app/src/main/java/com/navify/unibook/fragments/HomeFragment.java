package com.navify.unibook.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.navify.unibook.ActividadAdapterHome;
import com.navify.unibook.ActividadHome;
import com.navify.unibook.DatabaseHelper;
import com.navify.unibook.Materia;
import com.navify.unibook.MateriaAdapter;
import com.navify.unibook.R;

import java.util.List;

public class HomeFragment extends Fragment {
    private MaterialButton btnAddMateria;
    private FloatingActionButton fabAdd;
    private ListView listMaterias;
    private ListView listActividades;
    private DatabaseHelper db;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddMateria = view.findViewById(R.id.btnAddMateria);
        fabAdd = view.findViewById(R.id.fabAdd);
        listMaterias = view.findViewById(R.id.listMaterias);
        listActividades = view.findViewById(R.id.listActividades);

        btnAddMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFragmentAgregarMateria();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFragmentAgregarActividad();
            }
        });
    }

    private void abrirFragmentAgregarMateria() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        transaction.replace(R.id.fragmentContainer, new AgregarMateria());
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void abrirFragmentAgregarActividad() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        transaction.replace(R.id.fragmentContainer, new AgregarActividad());
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listMaterias != null && listActividades != null) {
            cargarMaterias();
            cargarActividades();
        }
    }

    private void cargarMaterias() {
        db = new DatabaseHelper(getContext());

        List<Materia> listaMaterias = db.obtenerTodasLasMaterias();

        MateriaAdapter materiaAdapter = new MateriaAdapter(getContext(), listaMaterias);

        if (listMaterias != null) {
            listMaterias.setAdapter(materiaAdapter);

            if (listaMaterias.size() > 3) {
                limitarAlturaListView(listMaterias, 3);
            } else {
                limitarAlturaListView(listMaterias, 3);
                limitarAlturaListView(listMaterias, listaMaterias.size());
            }
        }
    }

    public void cargarActividades() {
        // 1. Evitar crash si el contexto es nulo (el fragment se cerró)
        if (getContext() == null) return;

        // 2. Inicializar DB si es nula
        if (db == null) db = new DatabaseHelper(getContext());

        List<ActividadHome> listaActividades = db.obtenerActividadesRecientes();

        // 3. Verificar que la lista no sea nula
        if (listaActividades != null) {
            ActividadAdapterHome adapter = new ActividadAdapterHome(getContext(), listaActividades);
            listActividades.setAdapter(adapter);

            if (listaActividades.size() > 3) {
                limitarAlturaListView(listActividades, 3);
            } else {
                // Usar Math.min para evitar errores de índice
                limitarAlturaListView(listActividades, Math.max(1, listaActividades.size()));
            }
        }
    }

    private void limitarAlturaListView(ListView listView, int itemsVer) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null || listAdapter.getCount() == 0) {
            return;
        }

        View listItem = listAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        int alturaRenglon = listItem.getMeasuredHeight();
        int alturaDivider = listView.getDividerHeight();

        int itemsReales = Math.min(listAdapter.getCount(), itemsVer);

        int alturaTotal = (alturaRenglon + alturaDivider) * itemsReales;

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = alturaTotal;
        listView.setLayoutParams(params);
        listView.requestLayout();

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }
        });
    }
}
