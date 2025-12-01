/*
    Emilio Tomás Peralta Váldez
    Erick Hernández Trejo
    Iván Alberto Izaguirre Vázquez
    Teodoro Juan de Dios Ramos García
 */

package com.navify.unibook;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.navify.unibook.fragments.ActivityFragment;
import com.navify.unibook.fragments.HomeFragment;
import com.navify.unibook.fragments.PerfilFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout tabHome, tabActividades, tabPerfil;
    private LottieAnimationView lottieHome, lottieBook, lottieUser;
    private View indicatorHome, indicatorBook, indicatorUser;

    // Colores del bottom navigation
    private final int COLOR_NORMAL = Color.parseColor("#B7B7B7"); // gris claro
    private final int COLOR_SELECTED = Color.parseColor("#A0A0A0"); // gris más oscuro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    // Método para cambiar tab activo
    private void selectTab(FrameLayout selectedTab, LottieAnimationView lottie, View indicator) {

        // RESET: colores
        tabHome.setBackgroundColor(COLOR_NORMAL);
        tabActividades.setBackgroundColor(COLOR_NORMAL);
        tabPerfil.setBackgroundColor(COLOR_NORMAL);

        // RESET: animaciones y líneas
        lottieHome.pauseAnimation();
        lottieBook.pauseAnimation();
        lottieUser.pauseAnimation();

        indicatorHome.setVisibility(View.GONE);
        indicatorBook.setVisibility(View.GONE);
        indicatorUser.setVisibility(View.GONE);

        // ACTIVAR TAB SELECCIONADO
        selectedTab.setBackgroundColor(COLOR_SELECTED);
        indicator.setVisibility(View.VISIBLE);

        // Reproducir animación
        lottie.setMinAndMaxFrame(15, 60);  // ajusta según tu animación
        lottie.playAnimation();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
