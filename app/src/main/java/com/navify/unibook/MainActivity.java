/*
    Emilio Tomás Peralta Váldez
    Erick Hernández Trejo
    Iván Alberto Izaguirre Vázquez
    Teodoro Juan de Dios Ramos García
 */

package com.navify.unibook;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.navify.unibook.fragments.ActivityFragment;
import com.navify.unibook.fragments.HomeFragment;
import com.navify.unibook.fragments.PerfilFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout tabHome, tabActividades, tabPerfil;
    private LottieAnimationView lottieHome, lottieBook, lottieUser;
    private View indicatorHome, indicatorBook, indicatorUser;

    private final int COLOR_NORMAL = Color.TRANSPARENT;
    private final int COLOR_SELECTED = Color.TRANSPARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar Tema (Default: Claro) antes de setContentView
        SharedPreferences prefs = getSharedPreferences("UnibookPrefs", MODE_PRIVATE);
        int defaultNightMode = AppCompatDelegate.MODE_NIGHT_NO; // Forzar tema claro por defecto
        int nightMode = prefs.getInt("night_mode", defaultNightMode);
        AppCompatDelegate.setDefaultNightMode(nightMode);

        setContentView(R.layout.activity_main);

        // Bind views
        tabHome = findViewById(R.id.tabHome);
        tabActividades = findViewById(R.id.tabActividades);
        tabPerfil = findViewById(R.id.tabPerfil);

        lottieHome = findViewById(R.id.lottieHome);
        lottieBook = findViewById(R.id.lottieBook);
        lottieUser = findViewById(R.id.lottieUser);

        indicatorHome = findViewById(R.id.indicatorHome);
        indicatorBook = findViewById(R.id.indicatorBook);
        indicatorUser = findViewById(R.id.indicatorUser);

        // Inicializar Home como activo
        selectTab(tabHome, lottieHome, indicatorHome);
        loadFragment(new HomeFragment());

        // Listeners de tabs
        tabHome.setOnClickListener(v -> {
            selectTab(tabHome, lottieHome, indicatorHome);
            loadFragment(new HomeFragment());
        });

        tabActividades.setOnClickListener(v -> {
            selectTab(tabActividades, lottieBook, indicatorBook);
            loadFragment(new ActivityFragment());
        });

        tabPerfil.setOnClickListener(v -> {
            selectTab(tabPerfil, lottieUser, indicatorUser);
            loadFragment(new PerfilFragment());
        });
    }

    // Metodo para cambiar tab activo
    private void selectTab(FrameLayout selectedTab, LottieAnimationView lottie, View indicator) {

        // RESET: colores
        tabHome.setBackgroundColor(COLOR_NORMAL);
        tabActividades.setBackgroundColor(COLOR_NORMAL);
        tabPerfil.setBackgroundColor(COLOR_NORMAL);

        // RESET: animaciones y líneas
        // Cancelamos y reseteamos al frame 0 para evitar que se queden congeladas
        lottieHome.cancelAnimation();
        lottieHome.setProgress(0f);

        lottieBook.cancelAnimation();
        lottieBook.setProgress(0f);

        lottieUser.cancelAnimation();
        lottieUser.setProgress(0f);

        indicatorHome.setVisibility(View.GONE);
        indicatorBook.setVisibility(View.GONE);
        indicatorUser.setVisibility(View.GONE);

        // ACTIVAR TAB SELECCIONADO
        selectedTab.setBackgroundColor(COLOR_SELECTED);
        indicator.setVisibility(View.VISIBLE);

        // Reproducir animación
        lottie.setMinAndMaxFrame(0, 60);  // ajusta según tu animación
        lottie.playAnimation();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
