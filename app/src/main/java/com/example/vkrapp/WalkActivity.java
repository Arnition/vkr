package com.example.vkrapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vkrapp.utils.DatabaseHelper;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.geometry.SubpolylineHelper;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.traffic.TrafficLayer;
import com.yandex.mapkit.transport.TransportFactory;
import com.yandex.mapkit.transport.masstransit.PedestrianRouter;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Section;
import com.yandex.mapkit.transport.masstransit.SectionMetadata;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.mapkit.transport.masstransit.Transport;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

public class WalkActivity extends AppCompatActivity implements UserLocationObjectListener, Session.RouteListener {

    private Point END_ROUTE;

    private MapKit mapKit;
    private MapView mapview;
    private TrafficLayer traffic;
    private DatabaseHelper databaseHelper;

    private MapObjectCollection mapObjects;
    private UserLocationLayer userLocationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getApplicationContext());

        MapKitFactory.setApiKey("a322a8d9-c12c-47f7-a67b-740e6adf4be5");
        MapKitFactory.initialize(this);

        requestPermission();

        setContentView(R.layout.activity_walk);
        mapview = findViewById(R.id.mapview);
        mapKit = MapKitFactory.getInstance();
        mapview.getMap().move(new CameraPosition(new Point(0, 0), 15, 0, 0));
//                new Animation(Animation.Type.SMOOTH, 5), null);
        traffic = MapKitFactory.getInstance().createTrafficLayer(mapview.getMapWindow());
        traffic.setTrafficVisible(true);


        userLocationLayer = mapKit.createUserLocationLayer(mapview.getMapWindow());
        userLocationLayer.setAutoZoomEnabled(true);
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
        mapObjects = mapview.getMap().getMapObjects();
        addPlacemark();
    }


    private final Point ROUTE_START_LOCATION = new Point(47.236865, 39.7126067);
    private void addPlacemark() {
        List<Point> walkPoints = databaseHelper.getAllWalkPoints();
        ImageProvider icon = ImageProvider.fromResource(this, R.drawable.icon);

        for (Point walkPoint : walkPoints) {
            PlacemarkMapObject viewPlacemark = mapObjects.addPlacemark(walkPoint);  // вид метки, здесь мы используем CustomPlacemarkView, свой класс вида меток
            viewPlacemark.setIcon(icon);
        }

        END_ROUTE = walkPoints.get(walkPoints.size() - 1);
        findPath(ROUTE_START_LOCATION, END_ROUTE);
    }

    private void findPath(Point route_start, Point route_end) {
        TimeOptions options = new TimeOptions();
        List<RequestPoint> points = new ArrayList<RequestPoint>();
        points.add(new RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT, null));
        points.add(new RequestPoint(route_end, RequestPointType.WAYPOINT, null));

        PedestrianRouter pr = TransportFactory.getInstance().createPedestrianRouter();
        Session test = pr.requestRoutes(points, options, this);
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 5622);
        }
    }

    private void peremissionInternet() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.INTERNET") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.INTERNET"}, 5622);
        }
    }


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
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
        Toast.makeText(this, "onPointerCaptureChanged", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.5)),
                new PointF((float)(mapview.getWidth() * 0.5), (float)(mapview.getHeight() * 0.83)));

        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {
        findPath(userLocationView.getArrow().getGeometry(), END_ROUTE);

        Toast.makeText(this, userLocationView.getArrow().getGeometry().getLatitude() + " "
                + userLocationView.getArrow().getGeometry().getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMasstransitRoutes(List<Route> routes) {
        // In this example we consider first alternative only
        if (routes.size() > 0) {
            for (Section section : routes.get(0).getSections()) {
                drawSection(
                        section.getMetadata().getData(),
                        SubpolylineHelper.subpolyline(
                                routes.get(0).getGeometry(), section.getGeometry()));
            }
        }
    }

    @Override
    public void onMasstransitRoutesError(Error error) {
        String errorMessage = "getString(R.string.unknown_error_message)";
        if (error instanceof RemoteError) {
            errorMessage = "getString(R.string.remote_error_message)";
        } else if (error instanceof NetworkError) {
            errorMessage = "getString(R.string.network_error_message);";
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void drawSection(SectionMetadata.SectionData data, Polyline geometry) {
        // Draw a section polyline on a map
        // Set its color depending on the information which the section contains
        PolylineMapObject polylineMapObject = mapObjects.addPolyline(geometry);
        // Masstransit route section defines exactly one on the following
        // 1. Wait until public transport unit arrives
        // 2. Walk
        // 3. Transfer to a nearby stop (typically transfer to a connected
        //    underground station)
        // 4. Ride on a public transport
        // Check the corresponding object for null to get to know which
        // kind of section it is
        if (data.getTransports() != null) {
            // A ride on a public transport section contains information about
            // all known public transport lines which can be used to travel from
            // the start of the section to the end of the section without transfers
            // along a similar geometry
            for (Transport transport : data.getTransports()) {
                // Some public transport lines may have a color associated with them
                // Typically this is the case of underground lines
                if (transport.getLine().getStyle() != null) {
                    polylineMapObject.setStrokeColor(
                            // The color is in RRGGBB 24-bit format
                            // Convert it to AARRGGBB 32-bit format, set alpha to 255 (opaque)
                            transport.getLine().getStyle().getColor() | 0xFF000000
                    );
                    return;
                }
            }
        } else {
            // This is not a public transport ride section
            // In this example let us draw it in black
            polylineMapObject.setStrokeColor(0xFF000000);  // Black
        }
    }


//    @Override
//    public void onLocationUpdated(@NonNull Location location) {
//        double latitude = location.getPosition().getLatitude();
//        double longitude = location.getPosition().getLongitude();
//
//
//        // Do something with the latitude and longitude values
//        Toast.makeText(this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
//    }
//
}