package com.navify.unibook.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class HomeFragment extends Fragment implements SensorEventListener {
    private MaterialButton btnAddMateria;
    private FloatingActionButton fabAdd;
    private ListView listMaterias;
    private ListView listActividades;
    // Eliminada la referencia a LottieAnimationView
    private TextView txtUniBook;
    private DatabaseHelper db;

    // Variables para el sensor
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private boolean isSensorRegistered = false;
    private boolean isSensorReady = false;

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
        txtUniBook = view.findViewById(R.id.txtUniBook);

        // Se eliminó la configuración de la animación Lottie

        // Inicializar sensor
        if (getActivity() != null) {
            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager != null) {
                proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            }
        }

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

        listMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Materia materiaSeleccionada = (Materia) parent.getItemAtPosition(position);
                abrirFragmentEditarMateria(materiaSeleccionada.getId());
            }
        });

        listActividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActividadHome actividadSeleccionada = (ActividadHome) parent.getItemAtPosition(position);
                abrirFragmentDetallesActividad(actividadSeleccionada.getId());
            }
        });
    }

    // Eliminado método configurarAnimacionTitulo()

    private void abrirFragmentAgregarMateria() {
        if (!isAdded()) return;

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        transaction.replace(R.id.fragmentContainer, new AgregarMateria());
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void abrirFragmentEditarMateria(int materiaId) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        
        EditarMateria fragment = new EditarMateria();
        Bundle args = new Bundle();
        args.putInt("materia_id", materiaId);
        fragment.setArguments(args);

        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void abrirFragmentAgregarActividad() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        transaction.replace(R.id.fragmentContainer, new AgregarActividad());
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void abrirFragmentDetallesActividad(int actividadId) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        DetalleActividad fragment = new DetalleActividad();
        Bundle args = new Bundle();
        args.putInt("actividad_id", actividadId);
        fragment.setArguments(args);

        transaction.replace(R.id.fragmentContainer, fragment);
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

        if (sensorManager != null && proximitySensor != null && !isSensorRegistered) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
            isSensorRegistered = true;
            isSensorReady = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null && isSensorRegistered) {
            sensorManager.unregisterListener(this);
            isSensorRegistered = false;
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
        if (getContext() == null) return;
        if (db == null) db = new DatabaseHelper(getContext());

        List<ActividadHome> listaActividades = db.obtenerActividadesRecientes();

        if (listaActividades != null) {
            ActividadAdapterHome adapter = new ActividadAdapterHome(getContext(), listaActividades);
            listActividades.setAdapter(adapter);

            if (listaActividades.size() > 3) {
                limitarAlturaListView(listActividades, 3);
            } else {
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float distance = event.values[0];
            float maxRange = proximitySensor.getMaximumRange();

            boolean isNear = distance < maxRange;

            if (!isNear) {
                isSensorReady = true;
            } else if (isSensorReady) {
                abrirFragmentAgregarMateria();
                isSensorReady = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
