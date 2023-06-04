package com.example.vkrapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.vkrapp.utils.DatabaseHelper;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.traffic.TrafficLayer;
import com.yandex.runtime.image.ImageProvider;

import java.util.List;

public class WalkActivity extends AppCompatActivity {

    private MapView mapView;
    private Point selectedPoint;

    private TrafficLayer traffic;
    private MapObjectCollection mapObjects;

    private DatabaseHelper databaseHelper;

    private LocationManager locationManager;
    private WalkRouteListener walkRouteListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey("a322a8d9-c12c-47f7-a67b-740e6adf4be5");
        MapKitFactory.initialize(this);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        requestPermission();

        setContentView(R.layout.activity_walk);
        initDepends();
    }

    //маркеры, зум камеры
    private void initDepends() {
        mapView = findViewById(R.id.mapview);
        mapView.getMap().move(
                new CameraPosition(new Point(47.235586, 39.713120), 15.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 3), null);

        mapObjects = mapView.getMap().getMapObjects();
        addWalkPoints();

        walkRouteListener = new WalkRouteListener(getApplicationContext(), mapObjects);
        locationManager = MapKitFactory.getInstance().createLocationManager();
        locationManager.subscribeForLocationUpdates(5000, 0, 0, false, FilteringMode.OFF,walkRouteListener);
    }


    //добавление собственных маркеров
    private void addWalkPoints() {
        List<Point> walkPoints = databaseHelper.getAllWalkPoints();
        ImageProvider icon = ImageProvider.fromResource(this, R.drawable.marker);

        for (Point walkPoint : walkPoints) {
            PlacemarkMapObject viewWalkPoint = mapObjects.addPlacemark(walkPoint);
            viewWalkPoint.addTapListener(walkPointTapListener);
            viewWalkPoint.setIcon(icon);
        }
    }

    //обработчик нажатия на маркер
    private MapObjectTapListener walkPointTapListener = (mapObject, point) -> {
        selectedPoint = point;
        walkRouteListener.findPathToPoint(selectedPoint);
        return true;
    };


    //запрос на местонахождение устройства
    private void requestPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 5622 );
        }
    }

    @Override
    protected void onStop() {
        mapView.onStop();

        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();

        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
}