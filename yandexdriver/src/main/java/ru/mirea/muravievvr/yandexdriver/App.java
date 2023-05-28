package ru.mirea.muravievvr.yandexdriver;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class App  extends Application {
    private final String MAPKIT_API_KEY = "36d8648e-e0c5-4d4c-8e93-3346f390a654";
    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}
