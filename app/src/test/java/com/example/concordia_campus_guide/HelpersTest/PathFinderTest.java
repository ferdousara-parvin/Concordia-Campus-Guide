package com.example.concordia_campus_guide.HelpersTest;

import android.content.SharedPreferences;

import com.example.concordia_campus_guide.database.AppDatabase;
import com.example.concordia_campus_guide.database.daos.WalkingPointDao;
import com.example.concordia_campus_guide.helper.PathFinder;
import com.example.concordia_campus_guide.models.Building;
import com.example.concordia_campus_guide.models.Coordinates;
import com.example.concordia_campus_guide.models.Floor;
import com.example.concordia_campus_guide.models.PointType;
import com.example.concordia_campus_guide.models.RoomModel;
import com.example.concordia_campus_guide.models.WalkingPoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class PathFinderTest {
    private WalkingPoint classRoomInHBuildingWalkingPoint1;
    private WalkingPoint walkingPoint2;
    private WalkingPoint walkingPoint3;
    private WalkingPoint walkingPoint4;
    private WalkingPoint classRoomInHBuildingWalkingPoint2;
    private WalkingPoint entranceHBuildingWalkingPoint;
    private WalkingPoint elevatorWalkingPoint;
    private WalkingPoint entranceMBBuildingWalkingPoint;
    private WalkingPoint walkingPoint9;
    private WalkingPoint classRoomInMBBuildingWalkingPoint;
    private List<WalkingPoint> walkingPointList;
    private PathFinder pathFinder;
    private RoomModel roomStartingPoint;
    private RoomModel roomDestination;

    private PathFinder.WalkingPointNode walkingPointNode1;
    private PathFinder.WalkingPointNode walkingPointNode2;

    @Mock
    AppDatabase mockAppDb;

    @Mock
    WalkingPointDao mockWalkingPointDao;

    @Mock
    SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        settingUpWalkingPoints();

        roomStartingPoint = new RoomModel(new Coordinates(1, 1), "937", "H-9", "SGW");
        roomDestination = new RoomModel(new Coordinates(3, 3), "921", "H-9", "SGW");

        walkingPointList = Arrays.asList(classRoomInHBuildingWalkingPoint1, walkingPoint2, walkingPoint3, walkingPoint4, classRoomInHBuildingWalkingPoint2,
                entranceHBuildingWalkingPoint,elevatorWalkingPoint,entranceMBBuildingWalkingPoint, walkingPoint9, classRoomInMBBuildingWalkingPoint);

        when(mockAppDb.walkingPointDao()).thenReturn(mockWalkingPointDao);
        when(mockWalkingPointDao.getAll()).thenReturn(walkingPointList);
        when(mockAppDb.walkingPointDao().getWalkingPoint(roomStartingPoint.getFloorCode(), roomStartingPoint.getRoomCode())).thenReturn(classRoomInHBuildingWalkingPoint1);
        when(mockAppDb.walkingPointDao().getWalkingPoint(roomDestination.getFloorCode(), roomDestination.getRoomCode())).thenReturn(classRoomInHBuildingWalkingPoint2);

        pathFinder = new PathFinder(mockAppDb, sharedPreferences, roomStartingPoint, roomDestination);
    }

    private void settingUpWalkingPoints(){
        classRoomInHBuildingWalkingPoint1 = new WalkingPoint(1,new Coordinates(1, 1), "H-9",new ArrayList<>(Arrays.asList(new Integer[]{2,3,4})), PointType.CLASSROOM, "937");
        walkingPoint2 = new WalkingPoint(2,new Coordinates(2,2), "H-9", new ArrayList<>(Arrays.asList(new Integer[]{1,3,7})),PointType.NONE,"NONE");
        walkingPoint3 = new WalkingPoint(3,new Coordinates(2,-2.5), "H-9", new ArrayList<>(Arrays.asList(new Integer[]{2,4,5})),PointType.NONE,"NONE");
        walkingPoint4 = new WalkingPoint(4,new Coordinates(1, 1.5), "H-9", new ArrayList<>(Arrays.asList(new Integer[]{1,3,5,6})),PointType.NONE,"NONE");
        classRoomInHBuildingWalkingPoint2 = new WalkingPoint(5,new Coordinates(5, 5), "H-9", new ArrayList<>(Arrays.asList(new Integer[]{3,4})),PointType.CLASSROOM,"921");
        entranceHBuildingWalkingPoint = new WalkingPoint(6,new Coordinates(1.2, 2), "H-9", new ArrayList<>(Arrays.asList(new Integer[]{4,8})),PointType.ENTRANCE,"ENTRANCE");
        elevatorWalkingPoint = new WalkingPoint(7,new Coordinates(1, 3), "H-9", new ArrayList<>(Arrays.asList(new Integer[]{2})),PointType.ELEVATOR,"elevators-1");
        entranceMBBuildingWalkingPoint = new WalkingPoint(8, new Coordinates(2.4,3.2),"MB-S2",new ArrayList<>(Arrays.asList(new Integer[]{7,9})),PointType.ENTRANCE, "ENTRANCE");
        walkingPoint9 = new WalkingPoint(9,new Coordinates(-2.4,2.2),"MB-S2",new ArrayList<>(Arrays.asList(new Integer[]{8,10})),PointType.NONE, "NONE");
        classRoomInMBBuildingWalkingPoint = new WalkingPoint(10,new Coordinates(2.45,-2.2),"MB-S2",new ArrayList<>(Arrays.asList(new Integer[]{9})),PointType.CLASSROOM, "279");
    }

    @Test
    public void isGoalTrueTest(){
        assertTrue(pathFinder.isGoal(classRoomInHBuildingWalkingPoint2));
    }

    @Test
    public void getWalkingPointCorrespondingToPlaceForBuildingTest(){
        Building building = new Building( new Coordinates(45.49739588, -73.5787094), new ArrayList<String>(Arrays.asList("8","9")), 68, 68, 34, null, "H", null, null, null, null, null);
        String entranceFloor = building.getBuildingCode() + "-" + building.getEntranceFloor();
        when(mockAppDb.walkingPointDao().getWalkingPoint(entranceFloor, "entrance")).thenReturn(entranceHBuildingWalkingPoint);

        assertEquals(entranceHBuildingWalkingPoint,pathFinder.getWalkingPointCorrespondingToPlace(building));
    }

    @Test
    public void getWalkingPointCorrespondingToPlaceForFloorTest(){
        Floor floorWithCampus=new Floor(new Coordinates(45.496832,-73.578842), "H-9", 0, "SGW");

        when(mockAppDb.walkingPointDao().getAllAccessPointsOnFloor(floorWithCampus.getFloorCode(), PointType.ELEVATOR)).thenReturn(Arrays.asList(elevatorWalkingPoint));

        assertEquals(elevatorWalkingPoint,pathFinder.getWalkingPointCorrespondingToPlace(floorWithCampus));
    }

    @Test
    public void getWalkingPointCorrespondingToPlaceForRoomTest(){
        when(mockAppDb.walkingPointDao().getAllAccessPointsOnFloor(roomStartingPoint.getFloorCode(), PointType.CLASSROOM)).thenReturn(Arrays.asList(classRoomInHBuildingWalkingPoint1));
        assertEquals(classRoomInHBuildingWalkingPoint1,pathFinder.getWalkingPointCorrespondingToPlace(roomStartingPoint));
    }

    @Test
    public void getPathToDestinationBetweenTwoClassroomsInTheSameBuildingTest(){
        List<WalkingPoint> walkingPointsToDestination = Arrays.asList(classRoomInHBuildingWalkingPoint1, walkingPoint4, classRoomInHBuildingWalkingPoint2);
        assertEquals(walkingPointsToDestination,pathFinder.getPathToDestination());
    }

    @Test
    public void getPathToDestinationBetweenTwoClassroomsInTwoBuildingsTest(){
        RoomModel roomInH  = new RoomModel(new Coordinates(1, 1), "937", "H-9", "SGW");
        RoomModel roomInMB = new RoomModel(new Coordinates(2.45,-2.2),"279","MB-S2","SGW");

        when(mockAppDb.walkingPointDao().getWalkingPoint(roomInH.getFloorCode(), roomInH.getRoomCode())).thenReturn(classRoomInHBuildingWalkingPoint1);
        when(mockAppDb.walkingPointDao().getWalkingPoint(roomInMB.getFloorCode(), roomInMB.getRoomCode())).thenReturn(classRoomInMBBuildingWalkingPoint);
        when(mockAppDb.walkingPointDao().getAllAccessPointsOnFloor(classRoomInHBuildingWalkingPoint1.getFloorCode(), PointType.ENTRANCE)).thenReturn(Arrays.asList(entranceHBuildingWalkingPoint));

        PathFinder pathFinderBetweenBuildings = new PathFinder(mockAppDb, sharedPreferences, roomInH,roomInMB);
        List<WalkingPoint> walkingPointsToDestination = Arrays.asList(classRoomInHBuildingWalkingPoint1, walkingPoint4, entranceHBuildingWalkingPoint, entranceMBBuildingWalkingPoint, walkingPoint9, classRoomInMBBuildingWalkingPoint);
        assertEquals(walkingPointsToDestination,pathFinderBetweenBuildings.getPathToDestination());
    }


    @Test
    public void populateWalkingPointMapTest(){
        Map<Integer, PathFinder.WalkingPointNode> hashMap = pathFinder.populateWalkingPointMap(walkingPointList);
        for(int i =0; i<walkingPointList.size(); i++){
            assertEquals(walkingPointList.get(i).getId(),hashMap.get(i+1).getWalkingPoint().getId());
            assertEquals(walkingPointList.get(i).getFloorCode(),hashMap.get(i+1).getWalkingPoint().getFloorCode());
            assertEquals(walkingPointList.get(i).getPlaceCode(),hashMap.get(i+1).getWalkingPoint().getPlaceCode());
            assertEquals(walkingPointList.get(i).getCoordinate(),hashMap.get(i+1).getWalkingPoint().getCoordinate());
            assertTrue(verifyIds(walkingPointList.get(i).getConnectedPointsId(),hashMap.get(i+1).getWalkingPoint().getConnectedPointsId()));
            assertEquals(walkingPointList.get(i).getPointType(),hashMap.get(i+1).getWalkingPoint().getPointType());
        }
    }

    //WalkingNode Inner Class Tests:
    @Test
    public void getAndSetWalkingPointTest(){
        walkingPointNode1 = pathFinder.new WalkingPointNode(classRoomInHBuildingWalkingPoint2, null, 22.3, 123.312);
        assertEquals(classRoomInHBuildingWalkingPoint2,walkingPointNode1.getWalkingPoint());
        walkingPointNode1.setWalkingPoint(walkingPoint2);
        assertEquals(walkingPoint2, walkingPointNode1.getWalkingPoint());
    }

    @Test
    public void getAndSetHeuristicTest(){
        walkingPointNode1 = pathFinder.new WalkingPointNode(classRoomInHBuildingWalkingPoint2, null, 22.3, 123.312);
        assertEquals(22.3, walkingPointNode1.getHeuristic());
        walkingPointNode1.setHeuristic(1102.33);
        assertEquals(1102.33, walkingPointNode1.getHeuristic());
    }

    @Test
    public void getAndSetCostTest(){
        walkingPointNode1 = pathFinder.new WalkingPointNode(classRoomInHBuildingWalkingPoint2, null, 22.3, 123.312);
        assertEquals(123.312, walkingPointNode1.getCost());
        walkingPointNode1.setCost(1102.33);
        assertEquals(1102.33, walkingPointNode1.getCost());
    }

    @Test
    public void compareWalkingPointComparatorTest(){
        walkingPointNode1 = pathFinder.new WalkingPointNode(classRoomInHBuildingWalkingPoint2, null, 22.3, 123.312);
        PathFinder.WalkingPointComparator walkingPointComparator = pathFinder.new WalkingPointComparator();
        walkingPointNode2 = pathFinder.new WalkingPointNode(classRoomInHBuildingWalkingPoint1, walkingPointNode1, 31.3, 993.312);
        assertEquals(-1, walkingPointComparator.compare(walkingPointNode1,walkingPointNode2));
        assertEquals(1,walkingPointComparator.compare(walkingPointNode2,walkingPointNode1));
        assertEquals(0,walkingPointComparator.compare(walkingPointNode1,walkingPointNode1));
    }


    @Test
    public void addNearestWalkingPointsTest(){
        walkingPointNode1 = pathFinder.new WalkingPointNode(classRoomInHBuildingWalkingPoint2, null, 22.3, 123.312);
        pathFinder.addNearestWalkingPoints(walkingPointNode1);
        assertEquals(walkingPoint4,pathFinder.getWalkingPointsToVisit().peek().getWalkingPoint());
    }


    private boolean verifyIds(List<Integer>walkingPointsIds, List<Integer> walkingPointNodeList){
        for(int i=0; i < walkingPointsIds.size(); i++){
            if(!walkingPointsIds.get(i).equals(walkingPointNodeList.get(i))){
                return false;
            }
        }
        return true;
    }
}
