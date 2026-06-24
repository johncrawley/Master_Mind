package com.jcrawley.codebreaker;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;


public class MainActivity extends AppCompatActivity {


    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initViewModel();
        setupInsets();
        configureNavAndStatusBarAppearance();
    }


    private void initViewModel(){
        viewModel  = new ViewModelProvider(this).get(MainViewModel.class);
    }


    public MainViewModel getViewModel(){
        return viewModel;
    }


    private void setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            var systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void configureNavAndStatusBarAppearance(){
        var window = getWindow();
        var insetsController = WindowCompat.getInsetsController(window, window.getDecorView());
        insetsController.setAppearanceLightNavigationBars(false);
        insetsController.setAppearanceLightStatusBars(false);
    }



}