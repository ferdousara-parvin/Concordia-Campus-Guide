package com.example.concordia_campus_guide.Activities;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.concordia_campus_guide.Database.AppDatabase;
import com.example.concordia_campus_guide.Models.Building;
import com.example.concordia_campus_guide.Models.Floor;
import com.example.concordia_campus_guide.Models.Place;
import com.example.concordia_campus_guide.Models.Shuttle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RoutesActivityViewModel extends AndroidViewModel {

    private AppDatabase appDB;
    private Place from;
    private Place to;
    private List<Shuttle> shuttles;
    private final String noShuttles = "No available routes using the shuttle service";

    private Location myCurrentLocation;

    public RoutesActivityViewModel(@NonNull Application application) {
        super(application);

        appDB = AppDatabase.getInstance(application.getApplicationContext());
        setShuttles();
    }

    public void setTo(Place place){
        this.to = place;
    }

    public Place getTo(){
        return to;
    }

    public Place getFrom(){
        return from;
    }

    public void setFrom(Place from) {
        this.from = from;
    }

    public void setShuttles() {
        shuttles = appDB.shuttleDao().getAll();
    }

    public List<Shuttle> getShuttles() {
        String campusFrom = "";
        String campusTo = "";
        if (getFrom() != null && getTo() != null) {
            if (from.getClass() == Building.class) {
                campusFrom = ((Building) from).getCampus();
            } else if (to.getClass() == Building.class) {
                campusTo = ((Building) to).getCampus();
            } else if (from.getClass() == Floor.class) {
                campusFrom = ((Floor) from).getCampus();
            } else if(to.getClass() == Floor.class) {
                campusTo = ((Floor) to).getCampus();
            }
            if (campusFrom.compareTo(campusTo) == 0) {
                return null;
            }
        }

        Calendar calendar = Calendar.getInstance();
        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        shuttles = appDB.shuttleDao().getScheduleByCampusAndDayAndTime(campusFrom, day, time.format(calendar.getTime()));
        return shuttles;
    }

    public String getShuttleDisplayText(List<Shuttle> shuttles) {
        String content = "";
        if (shuttles == null || shuttles.size() == 0) {
            return this.noShuttles;
        }
        String campusTo = shuttles.get(0).getCampus().compareTo("SGW") == 0 ? "LOY" : "SGW";
        for (Shuttle shuttle : shuttles) {
            content += shuttle.getCampus() + "  >   " + campusTo + ", leaves at: " + shuttle.getTime() + "\n";
        }
        return content;
    }
}
