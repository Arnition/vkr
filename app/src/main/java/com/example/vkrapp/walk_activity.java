package com.example.vkrapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.traffic.TrafficLayer;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import android.Manifest;

import java.util.List;

public class walk_activity extends AppCompatActivity implements UserLocationObjectListener {

    private MapView mapview;
    private MapKit mapKit;
    private TrafficLayer traffic;
    private UserLocationLayer userLocationLayer;
//    private int REQUEST_CODE_PERMISSION_INTERNET = 1;
//    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("a322a8d9-c12c-47f7-a67b-740e6adf4be5");
        MapKitFactory.initialize(this);

        requestPermission();

        setContentView(R.layout.activity_walk);
        mapview = (MapView) findViewById(R.id.mapview);
        mapview.getMap().move(
                new CameraPosition(new Point(47.235586, 39.713120), 15.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 3), null);

        mapKit = MapKitFactory.getInstance();
        traffic = MapKitFactory.getInstance().createTrafficLayer(mapview.getMapWindow());
        traffic.setTrafficVisible(true);

        userLocationLayer = mapKit.createUserLocationLayer(mapview.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener((UserLocationObjectListener) this);

    }

  private void requestPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 5622 );
                return;
        }
    }

    private void peremissionInternet(){
        if (ContextCompat.checkSelfPermission(this,
                "android.permission.INTERNET")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"android.permission.INTERNET"},
                    5622);
        }
    }

//        public void onObjectAdded(UserLocationView userLocationView) {
//        userLocationLayer.setAnchor(
//                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.5)),
//                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.83)));
//
//        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
//                this, R.drawable.navigation));
//
//        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
//
//        ((CompositeIcon) pinIcon).setIcon(
//                "icon",
//                ImageProvider.fromResource(this, R.drawable.navigation),
//                new IconStyle().setAnchor(new PointF(0f, 0f))
//                        .setRotationType(RotationType.ROTATE)
//                        .setZIndex(0f)
//                        .setScale(1f)
//        );
//
//        pinIcon.setIcon(
//                "pin",
//                ImageProvider.fromResource(this, R.drawable.navigation),
//                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
//                        .setRotationType(RotationType.ROTATE)
//                        .setZIndex(1f)
//                        .setScale(0.5f)
//        );
//    }


    @Override
    protected void onStop() {
        mapview.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapview.onStart();
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {

    }

    public void onObjectRemoved(UserLocationView view) {
    }


    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}








        /*Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    PermissionListener permissionlistener = new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                           Intent intent = new Intent(walk_activity.this, walk_activity.class);
                           startActivity(intent);
                        }

                        @Override
                        public void onPermissionDenied(List<String> deniedPermissions) {
                            Toast.makeText(walk_activity.this, "Доступ запрещен\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                        }
                    };
                    TedPermission.create()
                            .setPermissionListener(permissionlistener)
                            .setDeniedMessage("Для использования приложения необходимо предоставить доступ к местоположению")
                            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                            .check();
                }
            }
        };
        thread.start();*/