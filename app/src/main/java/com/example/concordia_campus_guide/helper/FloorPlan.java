package com.example.concordia_campus_guide.helper;

import android.graphics.Color;

import com.example.concordia_campus_guide.database.AppDatabase;
import com.example.concordia_campus_guide.models.WalkingPoint;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.geojson.GeoJsonLayer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FloorPlan {

    private static final List<PatternItem> WALK_PATTERN = Arrays.asList(new Gap(20), new Dash(20));

    private GeoJsonLayer floorLayer;
    private Polyline currentlyDisplayedLine;
    private AppDatabase appDatabase;

    public RoomsInAFloor getRoomsInAFloor() {
        return roomsInAFloor;
    }

    public void setRoomsInAFloor(RoomsInAFloor roomsInAFloor) {
        this.roomsInAFloor = roomsInAFloor;
    }

    private RoomsInAFloor roomsInAFloor;

    public FloorPlan(AppDatabase appDB){
        this.appDatabase = appDB;
        roomsInAFloor = new RoomsInAFloor();
    }

    /**
     * @param buildingCode it represents which building we will be covering
     * @return Int of drawable resource's bitmap representation
     */
    public void setFloorPlan(GroundOverlay groundOverlay, String buildingCode, String floor, GoogleMap mMap, Map<String, List<WalkingPoint>> walkingPointsMap) {
        String fileName = buildingCode.toLowerCase()+"_"+floor.toLowerCase();
        groundOverlay.setImage(BitmapDescriptorFactory.fromAsset("buildings_floorplans/"+fileName+".png"));
        setFloorMarkers(buildingCode, floor, mMap,walkingPointsMap);
    }

    public void setFloorMarkers(String buildingCode, String floor, GoogleMap mMap, Map<String, List<WalkingPoint>> walkingPointsMap) {

        if (floorLayer != null) {
            floorLayer.removeLayerFromMap();
        }

        roomsInAFloor.setListOfRooms(buildingCode+"-"+floor, appDatabase);
        if (currentlyDisplayedLine != null) {
            currentlyDisplayedLine.remove();
        }
        PolylineOptions displayedPolylineOption = getFloorPolylines(buildingCode + "-" + floor, walkingPointsMap);
        currentlyDisplayedLine = mMap.addPolyline(displayedPolylineOption);
    }

    public PolylineOptions getFloorPolylines(String floorCode, Map<String, List<WalkingPoint>> walkingPointsMap) {
        // previously drawindoorpaths
        List<WalkingPoint> floorWalkingPoints = walkingPointsMap.get(floorCode);
        PolylineOptions option = new PolylineOptions();
        if (floorWalkingPoints == null) {
            return option;
        }
        for (int i = 0; i < floorWalkingPoints.size() - 1; i++) {
            LatLng point1 = floorWalkingPoints.get(i).getLatLng();
            LatLng point2 = floorWalkingPoints.get(i + 1).getLatLng();
            option.add(point1, point2);
        }
        return option
                .width(10)
                .pattern(WALK_PATTERN)
                .color(Color.rgb(147, 35, 57))
                .visible(true);
    }
}
