package com.example.concordia_campus_guide.HelpersTest.RoutesHelperTest;

import com.example.concordia_campus_guide.activities.RoutesActivity;
import com.example.concordia_campus_guide.ClassConstants;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.DirectionsLeg;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.DirectionsRoute;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.DirectionsStep;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.Duration;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.TransitDetails;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.TransitLine;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.TravelMode;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.Vehicle;
import com.example.concordia_campus_guide.helper.routes_helpers.DirectionsApiDataParser;
import com.example.concordia_campus_guide.helper.routes_helpers.DirectionsApiDataRetrieval;
import com.example.concordia_campus_guide.models.routes.Bus;
import com.example.concordia_campus_guide.models.routes.Car;
import com.example.concordia_campus_guide.models.routes.Route;
import com.example.concordia_campus_guide.models.routes.Subway;
import com.example.concordia_campus_guide.models.routes.Train;
import com.example.concordia_campus_guide.models.routes.Walk;
import com.example.concordia_campus_guide.models.Time;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DirectionsApiDataParserTest {
    private DirectionsApiDataParser directionsApiDataParser;
    private DirectionsApiDataRetrieval directionsApiDataRetrieval;

    @Mock
    RoutesActivity routesActivity;

    @Before
    public void setUp() {
        directionsApiDataParser = new DirectionsApiDataParser();
        directionsApiDataRetrieval = new DirectionsApiDataRetrieval(routesActivity);
    }

    @Test
    public void getDirectionsResultObj_CorrectDataFormatTest() {
        // Arrange
        directionsApiDataParser.setDataRetrieval(directionsApiDataRetrieval);

        Object directionsResultObj = null;
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("data_from_api");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder dataFromApi = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                dataFromApi.append(line);
            }
            directionsApiDataParser.getDataRetrieval().setData(dataFromApi.toString());
            is.close();
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        // Act
        try{
            // Accessing and testing a private method
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getDirectionsResultObj", null);
            method.setAccessible(true);
            directionsResultObj = method.invoke(directionsApiDataParser);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertNotNull(directionsResultObj);
    }

    @Test
    public void getDirectionsResultObj_IncorrectDataFormatTest() {
        // Arrange
        directionsApiDataParser.setDataRetrieval(directionsApiDataRetrieval);
        Object directionsResultObj = null;
        directionsApiDataParser.getDataRetrieval().setData("");

        // Act
        try{
            // Accessing and testing a private method
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getDirectionsResultObj", null);
            method.setAccessible(true);
            directionsResultObj = method.invoke(directionsApiDataParser);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertNull(directionsResultObj);
    }

    @Test
    public void getDrivingRouteTest() {
        // Arrange
        Object route = null;

        DirectionsRoute directionsRoute = new DirectionsRoute();
        directionsRoute.legs = new DirectionsLeg[1];
        directionsRoute.legs[0] = new DirectionsLeg();
        directionsRoute.legs[0].duration = new Duration();
        directionsRoute.legs[0].duration.text = "10 mins";
        directionsRoute.summary = "test";

        DirectionsStep d1 = new DirectionsStep();
        d1.duration = new Duration();
        d1.duration.value = 142;
        d1.travelMode = TravelMode.DRIVING;

        DirectionsStep[] steps = {d1};

        directionsRoute.legs[0].steps = steps;

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getDrivingRoute", DirectionsRoute.class);
            method.setAccessible(true);
            route = method.invoke(directionsApiDataParser, directionsRoute);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertEquals("", ((Route) route).getDepartureTime());
        Assert.assertEquals("", ((Route) route).getArrivalTime());
        Assert.assertEquals("10 mins", ((Route) route).getDuration());
        Assert.assertEquals(ClassConstants.DRIVING, ((Route) route).getMainTransportType());
        Assert.assertEquals("test", ((Route) route).getSummary());
        Assert.assertNull(((Route) route).getSteps());
    }

    @Test
    public void getWalkingRouteTest() {
        // Arrange
        Object route = null;

        DirectionsRoute directionsRoute = new DirectionsRoute();
        directionsRoute.legs = new DirectionsLeg[1];
        directionsRoute.legs[0] = new DirectionsLeg();
        directionsRoute.legs[0].duration = new Duration();
        directionsRoute.legs[0].duration.text = "10 mins";
        directionsRoute.summary = "test";

        DirectionsStep d1 = new DirectionsStep();
        d1.duration = new Duration();
        d1.duration.value = 142;
        d1.travelMode = TravelMode.WALKING;

        DirectionsStep[] steps = {d1};

        directionsRoute.legs[0].steps = steps;

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getWalkingRoute", DirectionsRoute.class);
            method.setAccessible(true);
            route = method.invoke(directionsApiDataParser, directionsRoute);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertEquals("", ((Route) route).getDepartureTime());
        Assert.assertEquals("", ((Route) route).getArrivalTime());
        Assert.assertEquals("10 mins", ((Route) route).getDuration());
        Assert.assertEquals(ClassConstants.WALKING, ((Route) route).getMainTransportType());
        Assert.assertEquals("test", ((Route) route).getSummary());
        Assert.assertNull(((Route) route).getSteps());
    }

    @Test
    public void getTransitRouteTest() {
        // Arrange
        Object route = null;

        DirectionsRoute directionsRoute = new DirectionsRoute();
        directionsRoute.legs = new DirectionsLeg[1];
        directionsRoute.legs[0] = new DirectionsLeg();
        directionsRoute.legs[0].departureTime = new Time();
        directionsRoute.legs[0].departureTime.setText("4:23pm");
        directionsRoute.legs[0].arrivalTime = new Time();
        directionsRoute.legs[0].arrivalTime.setText("4:33pm");
        directionsRoute.legs[0].duration = new Duration();
        directionsRoute.legs[0].duration.text = "10 mins";

        DirectionsStep d1 = new DirectionsStep();
        d1.travelMode = TravelMode.TRANSIT;
        d1.transitDetails = new TransitDetails();
        d1.transitDetails.line = new TransitLine();
        d1.transitDetails.line.vehicle = new Vehicle();
        d1.transitDetails.line.vehicle.name = "bus";
        d1.transitDetails.line.shortName = "128";
        DirectionsStep[] steps = {d1};

        directionsRoute.legs[0].steps = steps;

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransitRoute", DirectionsRoute.class);
            method.setAccessible(true);
            route = method.invoke(directionsApiDataParser, directionsRoute);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertEquals("4:23pm", ((Route) route).getDepartureTime());
        Assert.assertEquals("4:33pm", ((Route) route).getArrivalTime());
        Assert.assertEquals("10 mins", ((Route) route).getDuration());
        Assert.assertEquals(ClassConstants.TRANSIT, ((Route) route).getMainTransportType());
        Assert.assertEquals("", ((Route) route).getSummary());
        Assert.assertEquals(1, ((Route) route).getSteps().size());
    }

    @Test
    public void getTransitRoute_DepartureAndArrivalNullTest() {
        // Arrange
        Object route = null;

        DirectionsRoute directionsRoute = new DirectionsRoute();
        directionsRoute.legs = new DirectionsLeg[1];
        directionsRoute.legs[0] = new DirectionsLeg();
        directionsRoute.legs[0].duration = new Duration();
        directionsRoute.legs[0].duration.text = "10 mins";

        DirectionsStep d1 = new DirectionsStep();
        d1.travelMode = TravelMode.WALKING;
        d1.duration = new Duration();
        d1.duration.text  = "2 mins";

        DirectionsStep[] steps = {d1};

        directionsRoute.legs[0].steps = steps;

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransitRoute", DirectionsRoute.class);
            method.setAccessible(true);
            route = method.invoke(directionsApiDataParser, directionsRoute);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertEquals("10 mins", ((Route) route).getDuration());
        Assert.assertEquals("", ((Route) route).getArrivalTime());
        Assert.assertEquals("", ((Route) route).getDepartureTime());
        Assert.assertEquals(ClassConstants.TRANSIT, ((Route) route).getMainTransportType());
        Assert.assertEquals("", ((Route) route).getSummary());
        Assert.assertEquals(1, ((Route) route).getSteps().size());

    }

    @Test
    public void getTransportType_CarTest() {
        // Arrange
        Object transportType = null;
        DirectionsStep step = new DirectionsStep();
        step.travelMode = TravelMode.DRIVING;
        step.duration = new Duration();
        step.duration.value = 142;

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransportType", DirectionsStep.class);
            method.setAccessible(true);
            transportType = method.invoke(directionsApiDataParser, step);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertTrue(transportType instanceof Car);

    }

    @Test
    public void getTransportType_WalkTest() {
        // Arrange
        Object transportType = null;
        DirectionsStep step = new DirectionsStep();
        step.travelMode = TravelMode.WALKING;
        step.duration = new Duration();
        step.duration.text  = "2 mins";

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransportType", DirectionsStep.class);
            method.setAccessible(true);
            transportType = method.invoke(directionsApiDataParser, step);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertTrue(transportType instanceof Walk);
    }

    @Test
    public void getTransportType_BicycleTest() {
        // Arrange
        Object transportType = null;
        DirectionsStep step = new DirectionsStep();
        step.travelMode = TravelMode.BICYCLING;

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransportType", DirectionsStep.class);
            method.setAccessible(true);
            transportType = method.invoke(directionsApiDataParser, step);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertNull(transportType);
    }

    @Test
    public void getTransportType_TransitTest() {
        // Arrange
        Object transportType = null;
        DirectionsStep step = new DirectionsStep();
        step.travelMode = TravelMode.TRANSIT;
        step.transitDetails = new TransitDetails();
        step.transitDetails.line = new TransitLine();
        step.transitDetails.line.vehicle = new Vehicle();
        step.transitDetails.line.vehicle.name = "bus";
        step.transitDetails.line.shortName = "128";

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransportType", DirectionsStep.class);
            method.setAccessible(true);
            transportType = method.invoke(directionsApiDataParser, step);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertTrue(transportType instanceof Bus);
    }

    @Test
    public void getTransportType_OtherTest() {
        // Arrange
        Object transportType = null;
        DirectionsStep step = new DirectionsStep();
        step.travelMode = TravelMode.UNKNOWN;

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransportType", DirectionsStep.class);
            method.setAccessible(true);
            transportType = method.invoke(directionsApiDataParser, step);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertNull(transportType);
    }

    @Test
    public void getTransitType_BusTest() {
        // Arrange
        Object transportType = null;
        DirectionsStep step = new DirectionsStep();
        step.transitDetails = new TransitDetails();
        step.transitDetails.line = new TransitLine();
        step.transitDetails.line.vehicle = new Vehicle();
        step.transitDetails.line.vehicle.name = "bus";
        step.transitDetails.line.shortName = "128";

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransitType", DirectionsStep.class);
            method.setAccessible(true);
            transportType = method.invoke(directionsApiDataParser, step);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertTrue(transportType instanceof Bus);
    }

    @Test
    public void getTransitType_SubwayTest() {
        // Arrange
        Object transportType = null;
        DirectionsStep step = new DirectionsStep();
        step.transitDetails = new TransitDetails();
        step.transitDetails.line = new TransitLine();
        step.transitDetails.line.vehicle = new Vehicle();
        step.transitDetails.line.vehicle.name = "subway";
        step.transitDetails.line.color = "orange";

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransitType", DirectionsStep.class);
            method.setAccessible(true);
            transportType = method.invoke(directionsApiDataParser, step);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertTrue(transportType instanceof Subway);
    }

    @Test
    public void getTransitType_TrainTest() {
        // Arrange
        Object transportType = null;
        DirectionsStep step = new DirectionsStep();
        step.transitDetails = new TransitDetails();
        step.transitDetails.line = new TransitLine();
        step.transitDetails.line.vehicle = new Vehicle();
        step.transitDetails.line.vehicle.name = "train";
        step.transitDetails.line.shortName = "DM";

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransitType", DirectionsStep.class);
            method.setAccessible(true);
            transportType = method.invoke(directionsApiDataParser, step);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertTrue(transportType instanceof Train);
    }

    @Test
    public void getTransitType_OtherTest() {
        // Arrange
        Object transportType = null;
        DirectionsStep step = new DirectionsStep();
        step.transitDetails = new TransitDetails();
        step.transitDetails.line = new TransitLine();
        step.transitDetails.line.vehicle = new Vehicle();
        step.transitDetails.line.vehicle.name = "other";

        // Act
        try{
            Method method = directionsApiDataParser.getClass().getDeclaredMethod("getTransitType", DirectionsStep.class);
            method.setAccessible(true);
            transportType = method.invoke(directionsApiDataParser, step);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertNull(transportType);
    }

    @Test
    public void getSetDataRetrievalTest() {
        // Arrange
        DirectionsApiDataRetrieval expectedDirectionsApiDataRetrieval = directionsApiDataRetrieval;
        directionsApiDataParser.setDataRetrieval(directionsApiDataRetrieval);

        // Act & Assert
        Assert.assertEquals(expectedDirectionsApiDataRetrieval, directionsApiDataParser.getDataRetrieval());
    }

}
