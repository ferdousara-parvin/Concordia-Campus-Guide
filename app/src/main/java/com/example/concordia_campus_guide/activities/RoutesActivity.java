package com.example.concordia_campus_guide.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.concordia_campus_guide.adapters.RoutesAdapter;
import com.example.concordia_campus_guide.ClassConstants;
import com.example.concordia_campus_guide.global.SelectingToFromState;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.DirectionsResult;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.DirectionsRoute;
import com.example.concordia_campus_guide.helper.routes_helpers.DirectionsApiDataRetrieval;
import com.example.concordia_campus_guide.helper.routes_helpers.UrlBuilder;
import com.example.concordia_campus_guide.helper.ViewModelFactory;
import com.example.concordia_campus_guide.interfaces.DirectionsApiCallbackListener;
import com.example.concordia_campus_guide.models.Coordinates;
import com.example.concordia_campus_guide.models.routes.Route;
import com.example.concordia_campus_guide.models.Shuttle;
import com.example.concordia_campus_guide.R;
import com.example.concordia_campus_guide.view_models.RoutesActivityViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivity extends AppCompatActivity implements DirectionsApiCallbackListener {

    RoutesActivityViewModel mViewModel;
    TextView fromText;
    TextView toText;
    TextView content;
    ListView allRoutes;
    RoutesAdapter adapter;

    ImageButton transitButton;
    ImageButton shuttleButton;
    ImageButton walkButton;
    ImageButton carButton;
    ImageButton disabilityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponent();
        setViewModelAttributes();
        setToolbar();
        setFromAndTo();
        setBackButtonOnClick();
        getAllRoutes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Fetching the stored data from the SharedPreference
        SharedPreferences sharedPreferences = getSharedPreferences(ClassConstants.SHARED_PREFERENCES, MODE_PRIVATE);
        boolean disability = sharedPreferences.getBoolean(ClassConstants.ACCESSIBILITY_TOGGLE, false);
        disabilityButton.setSelected(disability);
    }

    private void initComponent() {
        setContentView(R.layout.routes_activity);
        allRoutes = findViewById(R.id.allRoutes);
        mViewModel = new ViewModelProvider(this, new ViewModelFactory(this.getApplication())).get(RoutesActivityViewModel.class);

        // get view
        fromText = findViewById(R.id.fromText);
        toText = findViewById(R.id.toText);
        content = findViewById(R.id.content);

        // set up listeners for buttons
        transitButton = findViewById(R.id.filterButtonTransit);
        shuttleButton = findViewById(R.id.filterButtonShuttle);
        walkButton = findViewById(R.id.filterButtonWalk);
        carButton = findViewById(R.id.filterButtonCar);
        disabilityButton = findViewById(R.id.filterButtonDisability);
    }

    private void setFromAndTo() {
        this.fromText.setText(mViewModel.getFromDisplayName());
        this.toText.setText(mViewModel.getToDisplayName());
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setViewModelAttributes() {
        mViewModel.setFrom(SelectingToFromState.getFrom());
        mViewModel.setTo(SelectingToFromState.getTo());
    }

    public void onClickTo(View v) {
        SelectingToFromState.setSelectToToTrue();
        openSearchPage();
    }

    public void onClickFrom(View v) {
        SelectingToFromState.setSelectFromToTrue();
        openSearchPage();
    }

    public void onClickTransit(View v) {
        mViewModel.setTransportType(ClassConstants.TRANSIT);
        getAllRoutes();
        setTransitSelect();
    }

    private void setTransitSelect() {
        transitButton.setSelected(true);
        shuttleButton.setSelected(false);
        walkButton.setSelected(false);
        carButton.setSelected(false);
    }

    public void onClickCar(View v) {
        mViewModel.setTransportType(ClassConstants.DRIVING);
        getAllRoutes();
        setCarSelect();
    }

    private void setCarSelect() {
        transitButton.setSelected(false);
        shuttleButton.setSelected(false);
        walkButton.setSelected(false);
        carButton.setSelected(true);
    }

    public void onClickDisability(View v) {
        disabilityButton.setSelected(!disabilityButton.isSelected());

        SharedPreferences sharedPreferences = getSharedPreferences(ClassConstants.SHARED_PREFERENCES, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(ClassConstants.ACCESSIBILITY_TOGGLE, disabilityButton.isSelected()).commit();
    }

    public void onClickShuttle(View v) {
        mViewModel.setRouteOptions(new ArrayList<>());
        List<Shuttle> shuttles = mViewModel.filterShuttles();
        if (!shuttles.isEmpty()) {
            mViewModel.adaptShuttleToRoutes(shuttles);
        } else {
            mViewModel.setTransportType(ClassConstants.SHUTTLE);
            mViewModel.noRoutesOptions(getResources().getString(R.string.no_shuttle));
        }
        setShuttleSelect();
        setRoutesAdapter();
    }

    private void setShuttleSelect() {
        transitButton.setSelected(false);
        shuttleButton.setSelected(true);
        walkButton.setSelected(false);
        carButton.setSelected(false);
    }

    public void onClickWalk(View v) {
        mViewModel.setTransportType(ClassConstants.WALKING);
        getAllRoutes();
        setWalkSelect();
    }

    private void setWalkSelect() {
        transitButton.setSelected(false);
        shuttleButton.setSelected(false);
        walkButton.setSelected(true);
        carButton.setSelected(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitSelectToFrom();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void directionsApiCallBack(DirectionsResult result, List<Route> routeOptions) {
        mViewModel.setDirectionsResult(result);
        mViewModel.setRouteOptions(routeOptions);
        if (result.routes.length == 0) {
            mViewModel.noRoutesOptions(getResources().getString(R.string.no_routes));
        }
        setRoutesAdapter();
    }

    private void setRoutesAdapter() {
        // Android adapter for list view
        adapter = new RoutesAdapter(this, R.layout.list_routes, mViewModel.getRouteOptions());
        allRoutes.setAdapter(adapter);
        allRoutes.setOnItemClickListener((adapterView, view, i, l) -> {
            if (carButton.isSelected() || transitButton.isSelected() || walkButton.isSelected()) {
                if (mViewModel.getRouteOptions().get(0) == null || mViewModel.getRouteOptions().get(0).getDuration() == null) {
                    Toast.makeText(getApplicationContext(), "Paths view is not available.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent openPaths = new Intent(RoutesActivity.this,
                            PathsActivity.class);
                    DirectionsRoute directionsResult = mViewModel.getDirectionsResult().routes[i];
                    openPaths.putExtra("directionsResult", directionsResult);
                    openPaths.putExtra("shuttle", false);
                    startActivity(openPaths);
                }
            } else {
                if (mViewModel.getShuttles() == null || mViewModel.getShuttles().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Paths view for Shuttle route is not available if no shuttles exist.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent openPaths = new Intent(RoutesActivity.this,
                            PathsActivity.class);
                    openPaths.putExtra("shuttle", true);
                    startActivity(openPaths);
                }
            }
        });
    }

    private void setBackButtonOnClick() {
        ImageButton backButton = this.findViewById(R.id.routesPageBackButton);
        backButton.setOnClickListener(v -> exitSelectToFrom());
    }

    private void exitSelectToFrom() {
        Intent exitSelectToFrom = new Intent(RoutesActivity.this,
                MainActivity.class);
        SelectingToFromState.reset();
        startActivity(exitSelectToFrom);
    }

    private void openSearchPage() {
        Intent openSearch = new Intent(RoutesActivity.this, SearchActivity.class);
        startActivity(openSearch);
    }

    /**
     * Calls the google Maps Directions API
     */
    public void getAllRoutes() {
        setTransitSelect();

        Coordinates fromCenterCoordinates = mViewModel.getFromEntranceCoordinates();
        Coordinates toCenterCoordinates = mViewModel.getToEntranceCoordinates();

        if (fromCenterCoordinates != null && toCenterCoordinates != null) {
            LatLng from = new LatLng(fromCenterCoordinates.getLatitude(), fromCenterCoordinates.getLongitude());
            LatLng to = new LatLng(toCenterCoordinates.getLatitude(), toCenterCoordinates.getLongitude());
            String transportType = mViewModel.getTransportType();

            String url = UrlBuilder.build(from, to, transportType,  getBaseContext().getResources().getConfiguration().getLocales().get(0));
            new DirectionsApiDataRetrieval(RoutesActivity.this).execute(url, transportType);
        }
    }
}
