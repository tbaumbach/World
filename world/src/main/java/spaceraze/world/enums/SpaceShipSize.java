package spaceraze.world.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum SpaceShipSize {

    NONE("NONE", "None", 0, 0),
    SQUADRON("SQUADRON", "Squadron", 1, 75),
    SMALL("SMALL", "Small", 1, 150),
    MEDIUM("MEDIUM", "Medium", 2, 450),
    LARGE("LARGE", "Large", 3, 750),
    HUGE("Huge", "Huge", 5, 1200);

    private String id;
    private String description;
    private int slots;
    private int compareSize;

    SpaceShipSize(String id, String description, int slots, int compareSize){
        this.id = id;
        this.description = description;
        this.slots = slots;
        this.compareSize = compareSize;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getSlots(){
        return slots;
    }

    public int getCompareSize(){
        return compareSize;
    }

    static public SpaceShipSize createFromSlots(int slots){
        ArrayList<SpaceShipSize> list = new ArrayList<>();
        Collections.addAll(list, SpaceShipSize.values());
        final SpaceShipSize[] spaceShipSize = new SpaceShipSize[1];
        list.stream().filter(size -> size.getSlots() == slots).findFirst().ifPresent(size -> spaceShipSize[0] = size);
        return spaceShipSize[0];
    }
}
