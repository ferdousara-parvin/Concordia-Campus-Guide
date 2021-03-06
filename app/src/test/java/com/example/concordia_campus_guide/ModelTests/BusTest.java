package com.example.concordia_campus_guide.ModelTests;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.DirectionsStep;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.TransitDetails;
import com.example.concordia_campus_guide.googleMapsServicesTools.googleMapsServicesModels.TransitLine;
import com.example.concordia_campus_guide.models.routes.Bus;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BusTest {
    private Bus bus;

    @Test
    public void getBusNumberTest() {
        // Arrange
        DirectionsStep directionsStep = new DirectionsStep();
        directionsStep.transitDetails = new TransitDetails();
        directionsStep.transitDetails.line = new TransitLine();
        directionsStep.transitDetails.line.shortName = "128";

        bus = new Bus(directionsStep);

        // Act & Assert
        assertEquals("128", bus.getBusNumber());
    }
}
