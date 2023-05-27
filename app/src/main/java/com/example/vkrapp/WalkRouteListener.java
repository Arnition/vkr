package com.example.vkrapp;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.geometry.SubpolylineHelper;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.transport.TransportFactory;
import com.yandex.mapkit.transport.masstransit.PedestrianRouter;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Section;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

import java.util.Arrays;
import java.util.List;

public class WalkRouteListener implements LocationListener, Session.RouteListener {


    private Point userLocation;         //  Сохранение позиции пользователя
    private Point selectedPoint;        //  Сохранение выбранной метки

    private ImageProvider user_icon;                //  Иконка для местоположения пользователя
    private PolylineMapObject wayViewLine;          //  Сохранение нарисованной линии маршрута
    private PlacemarkMapObject userViewPoint;       //  Сохранение нарисованной иконки пользователя

    private MapObjectCollection mapObjects;         //  Коллекция всех отрисованных объектов на карте

    public WalkRouteListener(Context context, MapObjectCollection mapObjects) {
        this.mapObjects = mapObjects;
        this.user_icon = ImageProvider.fromResource(context, R.drawable.user_location_icon);
    }


//    Функция вызывается после успешного построения маршрута в функции findPathToPoint
//    Отрисовываем самый первый маршрут
    @Override
    public void onMasstransitRoutes(@NonNull List<Route> routes) {
        if (routes.size() > 0) {
            for (Section section : routes.get(0).getSections()) {
                drawSection(SubpolylineHelper.subpolyline(routes.get(0).getGeometry(), section.getGeometry()));
            }
        }
    }


//    Функция вызывается после изменения геопозиции пользователя
    @Override
    public void onLocationUpdated(@NonNull Location location) {
        userLocation = location.getPosition();             //   Сохраняем новую позицию пользователя

        if(userViewPoint == null) {                                     //  Проверяем, что мы еще не рисовали иконку местоположения пользователя
            userViewPoint = mapObjects.addPlacemark(userLocation);      //  Добавляем точку на местоположение пользователя
            userViewPoint.setIcon(user_icon);                           //  Устанавливаем на эту точку иконку пользователя
        }else {
            userViewPoint.setGeometry(userLocation);                    //  Если мы уже рисовали иконку - изменяем ее координаты
        }

        if(selectedPoint != null && userLocation != null) {             //  Проверяем, что выбранная точка не null и позиция пользователя не null
            findPathToPoint(selectedPoint);                             //  Вызываем функцию для поиска маршута
        }
    }

    @Override
    public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {}

    @Override
    public void onMasstransitRoutesError(@NonNull Error error) {}

//    Функция поиска пути между местоположением пользователя и выбранной точкой
    public void findPathToPoint(Point route_end) {
        selectedPoint = route_end;                      //  Выбранная конечная точка маршрута

        List<RequestPoint> points = Arrays.asList(      //  Список из точек: Местоположение пользователя и конечная точка
                new RequestPoint(userLocation, RequestPointType.WAYPOINT, null),
                new RequestPoint(selectedPoint, RequestPointType.WAYPOINT, null)
        );

        PedestrianRouter pedestrianRouter = TransportFactory.getInstance().createPedestrianRouter();        //  Создаем роут для пешеходов
        Session session = pedestrianRouter.requestRoutes(points, new TimeOptions(), this);       //  Пытаемся найти маршрут между этими точками
    }

//  Функция для рисования маршрута на карте
    private void drawSection(Polyline geometry) {
        if(wayViewLine == null) {                               //  Проверям что маршрут еще не рисовался
            wayViewLine = mapObjects.addPolyline(geometry);     //  Добавляем линию маршрута
            wayViewLine.setStrokeColor(Color.BLUE);             //  Ставим ей цвет
        }else {
            wayViewLine.setGeometry(geometry);                  //  Если линия рисовалась - меняем ее местоположение
        }
    }
}
