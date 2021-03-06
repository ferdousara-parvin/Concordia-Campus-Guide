package com.example.concordia_campus_guide.helper.ui_helpers;

import android.content.Context;

import com.example.concordia_campus_guide.ClassConstants;
import com.example.concordia_campus_guide.view_models.LocationFragmentViewModel;
import com.example.concordia_campus_guide.models.RoomModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class RoomMarkers {

    private List<Marker> markers;

    public List<Marker> getMarkers() {
        return markers;
    }

    public RoomMarkers(){
        markers = new ArrayList<>();
    }

    public void addRoomToMap(RoomModel room, LocationFragmentViewModel locationFragmentViewModel, GoogleMap googleMap, Context context){
        BitmapDescriptor roomIcon = locationFragmentViewModel.getCustomSizedIcon("class_markers/marker.png", context, 30, 30);
        if(room != null){
            LatLng latLng = new LatLng(room.getLatitude(), room.getLongitude());
            String tag = ClassConstants.ROOM_TAG + "_" + room.getRoomCode() +"_" + room.getFloorCode();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(roomIcon)
                    .alpha(0.2f)
                    .visible(true)
                    .title(room.getRoomCode());

            Marker marker = googleMap.addMarker(markerOptions);
            marker.setTag(tag);

            markers.add(marker);
        }
    }



}
