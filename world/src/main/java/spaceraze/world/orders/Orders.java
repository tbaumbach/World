package spaceraze.world.orders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.*;
import spaceraze.util.general.Logger;
import spaceraze.world.BlackMarketBid;
import spaceraze.world.BlackMarketOffer;
import spaceraze.world.Building;
import spaceraze.world.BuildingType;
import spaceraze.world.Galaxy;
import spaceraze.world.Planet;
import spaceraze.world.Player;
import spaceraze.world.Spaceship;
import spaceraze.world.SpaceshipType;
import spaceraze.world.Troop;
import spaceraze.world.TroopType;
import spaceraze.world.VIP;
import spaceraze.world.VIPType;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "ORDERS")
public class Orders implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @OneToOne(mappedBy = "orders")
    private Player player;
    */
    private boolean abandonGame;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    @Builder.Default
    private List<Expense> expenses = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    @Builder.Default
    private List<ShipMovement> shipMoves = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "TOGGLE_PLANET_VISIBILITY")
    @Builder.Default
    private List<String> planetVisibilities = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "ABANDON_PLANET")
    @Builder.Default
    private List<String> abandonPlanets  = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "DESTROY_SHIP")
    @Builder.Default
    private List<String> shipSelfDestructs = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "TOGGLE_SHIP_SCREEN")
    @Builder.Default
    private List<String> screenedShips = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "DESTROY_BUILDNING")
    @Builder.Default
    private List<Integer> buildingSelfDestructs = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "DESTROY_TROOP")
    @Builder.Default
    private List<Integer> troopSelfDestructs = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "DESTROY_VIP")
    @Builder.Default
    private List<String> VIPSelfDestructs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    @Builder.Default
    private List<ResearchOrder> researchOrder = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    @Builder.Default
    private List<VIPMovement> VIPMoves = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    @Builder.Default
    private List<ShipToCarrierMovement> shipToCarrierMoves = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    @Builder.Default
    private List<TroopToCarrierMovement> troopToCarrierMoves = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    @Builder.Default
    private List<TroopToPlanetMovement> troopToPlanetMoves = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    @Builder.Default
    private List<PlanetNotesChange> planetNotesChanges = new ArrayList<>();

    public Orders(Player p) {
        this();
        // adding research Orders that should continue.
        for (int i = 0; i < p.getOrders().researchOrder.size(); i++) {
            if (!p.getResearchProgress(p.getOrders().researchOrder.get(i).getAdvantageName()).isDeveloped()) {
                // p.getOrders().getExpense(p.getOrders().researchOrder.get(i).getAdvantageName());
                this.addResearchOrder(p.getOrders().researchOrder.get(i), p);
            }
        }
    }

    private void addExpenses(Expense aExpense) {
        expenses.add(aExpense);
    }

    private void removeExpense(Expense aExpense) {
        expenses.remove(aExpense);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<ShipMovement> getShipMoves() {
        return shipMoves;
    }

    public List<ShipToCarrierMovement> getShipToCarrierMoves() {
        return shipToCarrierMoves;
    }

    public List<TroopToCarrierMovement> getTroopToCarrierMoves() {
        return troopToCarrierMoves;
    }

    public List<TroopToPlanetMovement> getTroopToPlanetMoves() {
        return troopToPlanetMoves;
    }

    public List<Integer> getTroopSelfDestructs() {
        return troopSelfDestructs;
    }

    public List<String> getVIPSelfDestructs() {
        return VIPSelfDestructs;
    }

    public List<VIPMovement> getVIPMoves() {
        return VIPMoves;
    }

    public List<VIPMovement> getVIPMoves(Planet aPlanet) {
        List<VIPMovement> vipMoves = new LinkedList<VIPMovement>();
        for (VIPMovement aVIPMovement : VIPMoves) {
            if (aPlanet.getName().equals(aVIPMovement.getPlanetDestinationName())) {
                vipMoves.add(aVIPMovement);
            }
        }
        return vipMoves;
    }

    public List<String> getPlanetVisibilities() {
        return planetVisibilities;
    }

    public List<String> getAbandonPlanets() {
        return abandonPlanets;
    }

    public List<String> getShipSelfDestructs() {
        return shipSelfDestructs;
    }

    public List<String> getScreenedShips() {
        return screenedShips;
    }

    public List<Integer> getBuildingSelfDestructs() {
        return buildingSelfDestructs;
    }

    public List<PlanetNotesChange> getPlanetNotesChanges() {
        return planetNotesChanges;
    }

    public PlanetNotesChange getPlanetNotesChange(Planet aPlanet) {
        PlanetNotesChange planetNotesChange = null;
        for (PlanetNotesChange aPlanetNotesChange : planetNotesChanges) {
            if (aPlanet.getName().equals(aPlanetNotesChange.getPlanetName())) {
                planetNotesChange = aPlanetNotesChange;
            }
        }
        return planetNotesChange;
    }

    public List<BlackMarketBid> getBlackMarketBids() {
        List<BlackMarketBid> allBids = new LinkedList<>();
        for (Expense anExpense : expenses) {
            if (anExpense.isBlackMarketBid()) {
                allBids.add(anExpense.getBlackMarketBid());
            }
        }
        return allBids;
    }

    public boolean getPlanetVisibility(Planet aPlanet) {
        boolean found = false;
        for (int i = 0; i < planetVisibilities.size(); i++) {
            String tempPlanet = planetVisibilities.get(i);
            if (aPlanet.getName().equalsIgnoreCase(tempPlanet)) {
                found = true;
            }
        }
        return found;
    }

    public boolean getAbandonPlanet(Planet aPlanet) {
        boolean found = false;
        for (int i = 0; i < abandonPlanets.size(); i++) {
            if (aPlanet.getName().equalsIgnoreCase(abandonPlanets.get(i))) {
                found = true;
            }
        }
        return found;
    }

    public boolean buildBuildingExpenseExist(Planet p, BuildingType bt) {
        boolean returnValue = false;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isBuildBuildingAt(p, bt)) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    public boolean incPopExpenseExist(Planet p) {
        boolean returnValue = false;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isIncPopAt(p)) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    public boolean incResExpenseExist(Planet p) {
        boolean returnValue = false;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isIncResAt(p)) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    public void addOrRemovePlanetVisib(Planet aPlanet) {
        // finns det en planet visibility redan?
        int found = -1;
        for (int i = 0; i < planetVisibilities.size(); i++) {
            if (aPlanet.getName().equalsIgnoreCase(planetVisibilities.get(i))) {
                found = i;
            }
        }
        if (found > -1) { // ta bort den
            planetVisibilities.remove(found);
        } else { // annars l�gg till den
            planetVisibilities.add(aPlanet.getName());
        }
    }


    public int checkScreenedShip(Spaceship aShip) {
        // finns det en screened ship order redan?


        int found = -1;
        for (int i = 0; i < screenedShips.size(); i++) {
            if (aShip.getKey() == screenedShips.get(i)) {
                found = i;
            }
        }

        return found;
    }


    public void addOrRemoveScreenedShip(Spaceship aShip) {
        // finns det en screened ship order redan?
        int found = -1;
        for (int i = 0; i < screenedShips.size(); i++) {
            if (aShip.getKey() == screenedShips.get(i)) {
                found = i;
            }
        }
        if (found > -1) { // ta bort den
            screenedShips.remove(found);
        } else { // annars l�gg till den
            screenedShips.add(aShip.getKey());
        }
    }

    public void addPlanetNotesChange(Planet aPlanet, String notesText) {
        Logger.fine("Notesorder: " + notesText);
        PlanetNotesChange aPlanetNotesChange = getPlanetNotesChange(aPlanet);
        if (aPlanetNotesChange != null) {
            aPlanetNotesChange.setNotesText(notesText);
        } else {
            planetNotesChanges.add(new PlanetNotesChange(aPlanet.getName(), notesText));
        }
    }

    public void removePlanetNotesChange(Planet aPlanet) {
        PlanetNotesChange aPlanetNotesChange = getPlanetNotesChange(aPlanet);
        if (aPlanetNotesChange != null) {
            planetNotesChanges.remove(aPlanetNotesChange);
        }
    }

    public void removeAllGroundAttacksAgainstPlanet(Planet inPlanet, Player inPlayer) {
        Logger.fine("inPlanet: " + inPlanet.getName() + " player: " + inPlayer.getName());

        // remove troop moves to planet
        List<TroopToPlanetMovement> removeList2 = new ArrayList<TroopToPlanetMovement>();
        for (TroopToPlanetMovement aTroopToPlanetMovement : troopToPlanetMoves) {
            if (aTroopToPlanetMovement.isThisDestination(inPlanet)) {
                removeList2.add(aTroopToPlanetMovement);
            }
        }
        // remove found orders
        for (TroopToPlanetMovement aTroopToPlanetMovement : removeList2) {
            troopToPlanetMoves.remove(aTroopToPlanetMovement);
        }
    }

    public void addOrRemoveAbandonPlanet(Planet aPlanet) {
        // finns det en planet visibility redan?
        int found = -1;
        for (int i = 0; i < abandonPlanets.size(); i++) {
            if (aPlanet.getName().equalsIgnoreCase(abandonPlanets.get(i))) {
                found = i;
            }
        }
        if (found > -1) { // ta bort den
            abandonPlanets.remove(found);
        } else { // annars l�gg till den
            abandonPlanets.add(aPlanet.getName());
        }
    }

    public void addNewBuilding(Planet aPlanet, String playerName, BuildingType tempbuilding, Galaxy aGalaxy) {
        addExpenses(new Expense("building", tempbuilding, playerName, aPlanet, null));
    }

    //public void removeNewBuilding(Planet aPlanet, BuildingType tempbuilding){
    public void removeNewBuilding(Planet aPlanet, Galaxy aGalaxy) {
        int findIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            // if (tempExpense.isBuildBuildingAt(aPlanet, tempbuilding)){
            if (tempExpense.isBuildBuildingAt(aPlanet)) {
                findIndex = i;
            }
        }
        if (findIndex > -1) {
            removeExpense(expenses.get(findIndex));
        }

    }

    public void addReconstruct(Planet aPlanet, Player aPlayer) {
        addExpenses(new Expense("reconstruct", aPlanet, aPlayer));
    }

    public void removeReconstruct(Planet aPlanet, Galaxy aGalaxy) {
        int findIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isReconstructAt(aPlanet)) {
                findIndex = i;
            }
        }
        if (findIndex > -1) {
            removeExpense(expenses.get(findIndex));
        }
    }

    public void addIncPop(Planet aPlanet, Player aPlayer) {
        addExpenses(new Expense("pop", aPlanet, aPlayer));
    }

    public void removeIncPop(Planet aPlanet, Galaxy aGalaxy) {
        int findIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isIncPopAt(aPlanet)) {
                findIndex = i;
            }
        }
        if (findIndex > -1) {
            removeExpense(expenses.get(findIndex));
        }
    }

    public void addIncRes(Planet aPlanet, Player aPlayer) {
        addExpenses(new Expense("res", aPlanet, aPlayer));
    }

    public void removeIncRes(Planet aPlanet, Galaxy aGalaxy) {
        int findIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isIncResAt(aPlanet)) {
                findIndex = i;
            }
        }
        if (findIndex > -1) {
            removeExpense(expenses.get(findIndex));
        }
    }

    public void addNewShipMove(Spaceship ss, Planet destination) {
        // f�rst kolla om det finns en gammal order f�r detta skepp som skall tas bort
        int found = -1;
        for (int i = 0; i < shipMoves.size(); i++) {
            ShipMovement tempShipMove = (ShipMovement) shipMoves.get(i);
            if (tempShipMove.isThisShip(ss)) {
                found = i;
            }
        }
        if (found > -1) {
            shipMoves.remove(found);
        }
        if (destination != null) {
            shipMoves.add(new ShipMovement(ss, destination));
        }
    }

    public void addNewTroopToPlanetMove(Troop aTroop, Planet destination, int turn) {
        // f�rst kolla om det finns en gammal order f�r detta skepp som skall tas bort
        int found = -1;
        for (int i = 0; i < troopToPlanetMoves.size(); i++) {
            TroopToPlanetMovement tempTroopToPlanetMove = troopToPlanetMoves.get(i);
            if (tempTroopToPlanetMove.isThisTroop(aTroop)) {
                found = i;
            }
        }
        if (found > -1) {
            troopToPlanetMoves.remove(found);
        }
        if (destination != null) {
            troopToPlanetMoves.add(new TroopToPlanetMovement(aTroop, destination, turn));
        }
    }

    public void addNewTroopToCarrierMove(Troop aTroop, Spaceship destinationCarrier) {
        // f�rst kolla om det finns en gammal order f�r detta skepp som skall tas bort
        int found = -1;
        for (int i = 0; i < troopToCarrierMoves.size(); i++) {
            TroopToCarrierMovement tempTroopToCarrierMove = troopToCarrierMoves.get(i);
            if (tempTroopToCarrierMove.isThisTroop(aTroop)) {
                found = i;
            }
        }
        if (found > -1) {
            troopToCarrierMoves.remove(found);
        }
        if (destinationCarrier != null) {
            troopToCarrierMoves.add(new TroopToCarrierMovement(aTroop, destinationCarrier));
        }
    }

    public void addNewShipToCarrierMove(Spaceship ss, Spaceship destinationCarrier) {
        // först kolla om det finns en gammal order f�r detta skepp som skall tas bort
        int found = -1;
        for (int i = 0; i < shipToCarrierMoves.size(); i++) {
            ShipToCarrierMovement tempShipToCarrierMove = (ShipToCarrierMovement) shipToCarrierMoves.get(i);
            if (tempShipToCarrierMove.isThisShip(ss)) {
                found = i;
            }
        }
        if (found > -1) {
            shipToCarrierMoves.remove(found);
        }
        if (destinationCarrier != null) {
            shipToCarrierMoves.add(new ShipToCarrierMovement(ss, destinationCarrier));
        }
    }

    public void addNewVIPMove(VIP aVIP, Object destination) {
        // först kolla om det finns en gammal order f�r denna vip som skall tas bort
        int found = -1;
        for (int i = 0; i < VIPMoves.size(); i++) {
            VIPMovement tempVIPMove = VIPMoves.get(i);
            if (tempVIPMove.isThisVIP(aVIP)) {
                found = i;
            }
        }
        if (found > -1) {
            VIPMoves.remove(found);
        }
        if (destination != null) {
            if (destination instanceof Planet) {
                VIPMoves.add(new VIPMovement(aVIP, (Planet) destination));
            } else if (destination instanceof Spaceship) {
                VIPMoves.add(new VIPMovement(aVIP, (Spaceship) destination));
            } else { // troop move
                VIPMoves.add(new VIPMovement(aVIP, (Troop) destination));
            }
        }
    }

    // kolla om det finns en gammal order f�r detta skepp
    public boolean checkShipMove(Spaceship ss) {
        boolean found = false;
        int i = 0;
        while ((found == false) & (i < shipMoves.size())) {
            ShipMovement tempShipMove = (ShipMovement) shipMoves.get(i);
            if (tempShipMove.isThisShip(ss)) {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }

    // kolla om det finns en gammal order för detta VIP
    public boolean checkVIPMove(VIP vip) {
        boolean found = false;
        int i = 0;
        while ((found == false) & (i < VIPMoves.size())) {
            VIPMovement tempVIPMove = (VIPMovement) VIPMoves.get(i);
            if (tempVIPMove.isThisVIP(vip)) {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }

    // check if there exist a planet move order for this troop
    public boolean checkTroopToPlanetMove(Troop aTroop) {
        boolean found = false;
        int i = 0;
        while ((found == false) & (i < troopToPlanetMoves.size())) {
            TroopToPlanetMovement tempMove = troopToPlanetMoves.get(i);
            if (tempMove.isThisTroop(aTroop)) {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }

    // check if there exist a carrier move order for this troop
    public boolean checkTroopToCarrierMove(Troop aTroop) {
        boolean found = false;
        int i = 0;
        while ((found == false) & (i < troopToCarrierMoves.size())) {
            TroopToCarrierMovement tempMove = troopToCarrierMoves.get(i);
            if (tempMove.isThisTroop(aTroop)) {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }

    // check if there exist a carrier move order for this troop to aCarrier
    public boolean checkTroopToCarrierMove(Troop aTroop, Spaceship aCarrier) {
        boolean found = false;
        boolean moveToCarrier = false;
        int i = 0;
        while ((found == false) & (i < troopToCarrierMoves.size())) {
            TroopToCarrierMovement tempMove = troopToCarrierMoves.get(i);
            if (tempMove.isThisTroop(aTroop)) {
                found = true;
                if (tempMove.getDestinationCarrierId().equals(aCarrier.getKey())) {
                    moveToCarrier = true;
                }
            } else {
                i++;
            }
        }
        return moveToCarrier;
    }

    // kolla om det finns en gammal order f�r detta skepp
    public boolean checkShipToCarrierMove(Spaceship ss) {
        boolean found = false;
        int i = 0;
        while ((found == false) & (i < shipToCarrierMoves.size())) {
            ShipToCarrierMovement tempShipToCarrierMove = (ShipToCarrierMovement) shipToCarrierMoves.get(i);
            if (tempShipToCarrierMove.isThisShip(ss)) {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }

    // kolla om det finns en gammal order f�r detta skepp
    public boolean checkShipToCarrierMove(Spaceship aSqd, Spaceship aCarrier) {
        boolean found = false;
        int i = 0;
        while ((found == false) & (i < shipToCarrierMoves.size())) {
            ShipToCarrierMovement tempShipToCarrierMove = (ShipToCarrierMovement) shipToCarrierMoves.get(i);
            if (tempShipToCarrierMove.isThisShip(aSqd)) {
                if (tempShipToCarrierMove.isThisDestination(aCarrier)) {
                    found = true;
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }
        return found;
    }

    // kolla hur m�nga moveToCarrier orders det finns till den anvivna carriern
    public int countShipToCarrierMoves(Spaceship aCarrier) {
        int count = 0;
        for (ShipToCarrierMovement aShipToCarrierMove : shipToCarrierMoves) {
//		LoggingHandler.finest( "STCM dest" + aShipToCarrierMove.getDestinationCarrier().getName());
            if (aShipToCarrierMove.isThisDestination(aCarrier)) {
                count++;
                Logger.finest("adding carrier count = " + count);
            }
        }
        return count;
    }

    // count how many troops are ordered to move to acarrier
    public int countTroopToCarrierMoves(Spaceship aCarrier) {
        int count = 0;
        for (TroopToCarrierMovement aTroopToCarrierMove : troopToCarrierMoves) {
            if (aTroopToCarrierMove.isThisDestination(aCarrier)) {
                count++;
            }
        }
        return count;
    }

    // count how many troops are ordered to move from a carrier to a planet
    public int countTroopToPlanetMoves(Spaceship aCarrier, Planet aPlanet, Galaxy aGalaxy) {
        int count = 0;
        for (TroopToPlanetMovement aTroopToPlanetMove : troopToPlanetMoves) {
            if (aTroopToPlanetMove.getTroop(aGalaxy).getShipLocation() == aCarrier) {
                if (aTroopToPlanetMove.isThisDestination(aPlanet)) {
                    count++;
                }
            }
        }
        return count;
    }

    // count how many troops are ordered to move to a planet
    public List<TroopToPlanetMovement> getTroopToPlanetMoves(Planet aPlanet) {
        List<TroopToPlanetMovement> troopMoves = new LinkedList<TroopToPlanetMovement>();
        for (TroopToPlanetMovement aTroopToPlanetMove : troopToPlanetMoves) {
            if (aTroopToPlanetMove.isThisDestination(aPlanet)) {
                troopMoves.add(aTroopToPlanetMove);
            }
        }
        return troopMoves;
    }

    public void addNewTransaction(int aSum, Player recipient) {
        // f�rst kolla om det finns en gammal transaktion som skall tas bort
        int findIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.getType().equalsIgnoreCase("transaction")) {
                if (tempExpense.getPlayerName() == recipient.getName()) {
                    findIndex = i;
                }
            }
        }
        if (findIndex > -1) {
            removeExpense(expenses.get(findIndex));
        }

        if (aSum > 0) {
            addExpenses(new Expense("transaction", recipient, aSum));
        }
    }

    public Planet getDestination(Spaceship tempss, Galaxy aGalaxy) {
        Planet dest = null;
        boolean found = false;
        int i = 0;
        ShipMovement tempShipMove = null;
        while ((i < shipMoves.size()) & !found) {
            tempShipMove = (ShipMovement) shipMoves.get(i);
            if (tempShipMove.isThisShip(tempss)) {
                found = true;
            } else {
                i++;
            }
        }
        if (found) {

            dest = aGalaxy.getPlanet(tempShipMove.getDestinationName());
        }
        return dest;
    }

    public Planet getDestinationPlanet(Troop aTroop, Galaxy aGalaxy) {
        Planet dest = null;
        boolean found = false;
        int i = 0;
        TroopToPlanetMovement tempMove = null;
        while ((i < troopToPlanetMoves.size()) & !found) {
            tempMove = troopToPlanetMoves.get(i);
            if (tempMove.isThisTroop(aTroop)) {
                found = true;
            } else {
                i++;
            }
        }
        if (found) {
            dest = tempMove.getDestination(aGalaxy);
        }
        return dest;
    }

    public Spaceship getDestinationCarrier(Spaceship tempss, Galaxy aGalaxy) {
        Spaceship dest = null;
        boolean found = false;
        int i = 0;
        ShipToCarrierMovement tempShipToCarrierMove = null;
        while ((i < shipToCarrierMoves.size()) & !found) {
            tempShipToCarrierMove = (ShipToCarrierMovement) shipToCarrierMoves.get(i);
            if (tempShipToCarrierMove.isThisShip(tempss)) {
                found = true;
            } else {
                i++;
            }
        }
        if (found) {
            dest = tempShipToCarrierMove.getDestinationCarrier(aGalaxy);
        }
        return dest;
    }

    public Spaceship getTroopDestinationCarrier(Troop aTroop, Galaxy aGalaxy) {
        Spaceship dest = null;
        boolean found = false;
        int i = 0;
        TroopToCarrierMovement tempTroopToCarrierMove = null;
        while ((i < troopToCarrierMoves.size()) & !found) {
            tempTroopToCarrierMove = (TroopToCarrierMovement) troopToCarrierMoves.get(i);
            if (tempTroopToCarrierMove.isThisTroop(aTroop)) {
                found = true;
            } else {
                i++;
            }
        }
        if (found) {
            dest = tempTroopToCarrierMove.getDestinationCarrier(aGalaxy);
        }
        return dest;
    }

    public String getDestinationName(Spaceship tempss, Galaxy aGalaxy) {
        String destName = "";
        Planet destination = getDestination(tempss, aGalaxy);
        if (destination != null) {
            destName = destination.getName();
        }
        return destName;
    }

    public String getDestinationPlanetName(Troop aTroop, Galaxy aGalaxy) {
        String destName = "";
        Planet destination = getDestinationPlanet(aTroop, aGalaxy);
        if (destination != null) {
            destName = destination.getName();
        }
        return destName;
    }

    public String getDestinationCarrierName(Spaceship tempss, Galaxy aGalaxy) {
        String destName = "";
        Spaceship destination = getDestinationCarrier(tempss, aGalaxy);
        if (destination != null) {
            destName = destination.getName();
        }
        return destName;
    }

    public String getDestinationCarrierShortName(Spaceship tempss, Galaxy aGalaxy) {
        String destName = "";
        Spaceship destination = getDestinationCarrier(tempss, aGalaxy);
        if (destination != null) {
            destName = destination.getShortName();
        }
        return destName;
    }

    public String getTroopDestinationCarrierName(Troop aTroop, Galaxy aGalaxy) {
        String destName = "";
        Spaceship destination = getTroopDestinationCarrier(aTroop, aGalaxy);
        if (destination != null) {
            destName = destination.getName();
        }
        return destName;
    }

    public String getTroopDestinationCarrierShortName(Troop aTroop, Galaxy aGalaxy) {
        String destName = "";
        Spaceship destination = getTroopDestinationCarrier(aTroop, aGalaxy);
        if (destination != null) {
            destName = destination.getShortName();
        }
        return destName;
    }

    public String getDestinationName(VIP tempVIP, Galaxy aGalaxy, boolean longName) {
        String destName = "";
        boolean found = false;
        int i = 0;
        VIPMovement tempVIPMove = null;
        while ((i < VIPMoves.size()) & !found) {
            tempVIPMove = VIPMoves.get(i);
            if (tempVIPMove.isThisVIP(tempVIP)) {
                found = true;
            } else {
                i++;
            }
        }
        if (found) {
            destName = tempVIPMove.getDestinationName(longName, aGalaxy);
        }
        return destName;
    }

    public void addBuildShip(Building aBuilding, SpaceshipType sst, Player aPlayer) {
        // skapa ny order
        addExpenses(new Expense("buildship", aBuilding, sst, aPlayer.getName()));
    }

    public void addBuildTroop(Building aBuilding, TroopType tt, Player aPlayer) {
        // skapa ny order
        addExpenses(new Expense("buildtroop", aBuilding, tt, aPlayer.getName()));
    }

    public void addBuildVIP(Building aBuilding, VIPType vt, Player aPlayer) {
        // skapa ny order
        addExpenses(new Expense("buildVIP", aBuilding, vt, aPlayer.getName()));
    }

    public void removeAllBuildShip(Building aBuilding, Galaxy aGalaxy) {
        int nrFoundIndexes = 0;
        int[] removeIndexes = new int[expenses.size()];
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isBuildingBuildingShip(aBuilding)) {
                Logger.finest("Shall remove: " + nrFoundIndexes);
                removeIndexes[nrFoundIndexes] = i;
                nrFoundIndexes++;
            }
        }
        for (int j = nrFoundIndexes - 1; j >= 0; j--) {
            Logger.finest("Removing: " + j);
            Logger.finest("Removing: " + expenses.get(j).getSpaceshipTypeName());
            removeExpense(expenses.get(removeIndexes[j]));
        }
    }

    public void removeAllBuildTroop(Building aBuilding, Galaxy aGalaxy) {
        int nrFoundIndexes = 0;
        int[] removeIndexes = new int[expenses.size()];
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isBuildingBuildingTroop(aBuilding)) {
                Logger.finest("Shall remove: " + nrFoundIndexes);
                removeIndexes[nrFoundIndexes] = i;
                nrFoundIndexes++;
            }
        }
        for (int j = nrFoundIndexes - 1; j >= 0; j--) {
            Logger.finest("Removing: " + j);
            removeExpense(expenses.get(removeIndexes[j]));
        }
    }

    public void removeBuildVIP(Building aBuilding, Galaxy aGalaxy) {
        int foundIndexes = -1;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isBuildingBuildingVIP(aBuilding)) {
                foundIndexes = i;
            }
        }
        if (foundIndexes >= 0) {
            removeExpense(expenses.get(foundIndexes));
        }
    }

    public void removeUpgradeBuilding(Building aBuilding, Galaxy aGalaxy) {
        int findIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isUpgradeBuilding(aBuilding)) {
                findIndex = i;
            }
        }
        if (findIndex > -1) {
            removeExpense(expenses.get(findIndex));
        }
    }

    public void addUpgradeBuilding(Building currentBuilding, BuildingType newBuilding, Player aPlayer) {
        // skapa ny order om inte varvet redan �r satt att uppgradera
        if (!alreadyUpgrading(currentBuilding)) {
            addExpenses(new Expense("building", newBuilding, aPlayer.getName(), currentBuilding.getLocation(), currentBuilding));
        }
    }


    public boolean alreadyUpgrading(Building currentBuilding) {
        boolean found = false;
        int i = 0;
        while ((i < expenses.size()) && (!found)) {
            Logger.finer("alreadyUpgrading lop index: " + i);
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isBuilding(currentBuilding)) {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }

    /**
     * Check if there already exist a reconstruct order for this planet
     *
     * @return
     */
    public boolean alreadyReconstructing(Planet aPlanet) {
        boolean found = false;
        int i = 0;
        while ((i < expenses.size()) & (!found)) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isReconstructAt(aPlanet)) {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }

    public boolean haveSpaceshipTypeBuildOrder(SpaceshipType aSpaceshipType) {
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (aSpaceshipType.getName().equals(tempExpense.getSpaceshipTypeName())) {
                return true;
            }
        }
        return false;
    }


    public boolean haveBuildingTypeBuildOrder(BuildingType aBuildingType, int buildingId) {
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (aBuildingType.getName().equals(tempExpense.getBuildingTypeName())) {
                if (buildingId < 0 || buildingId != tempExpense.getCurrentBuildingId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean haveVIPTypeBuildOrder(VIPType aVIPType) {
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (aVIPType.getName().equals(tempExpense.getVipTypeName())) {
                return true;
            }
        }
        return false;
    }

    public boolean haveTroopTypeBuildOrder(TroopType aTroopType) {
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (aTroopType.getUniqueName().equals(tempExpense.getTroopTypeName())) {
                return true;
            }
        }
        return false;
    }


    public String getVIPBuild(Building currentBuilding) {
        String tempVIPName = null;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isBuildingBuildingVIP(currentBuilding)) {
                tempVIPName = tempExpense.getVIPType();
            }
        }
        return tempVIPName;
    }

    public void addShipSelfDestruct(Spaceship currentss) {
        shipSelfDestructs.add(currentss.getKey());
    }

    public void addTroopSelfDestruct(Troop aTroop) {
        troopSelfDestructs.add(aTroop.getUniqueId());
    }

    public void addVIPSelfDestruct(VIP aVIP) {

        int found = -1;
        for (int i = 0; i < VIPMoves.size(); i++) {
            VIPMovement tempVIPMove = VIPMoves.get(i);
            if (tempVIPMove.isThisVIP(aVIP)) {
                found = i;
            }
        }
        if (found > -1) {
            VIPMoves.remove(found);
        }

        VIPSelfDestructs.add(aVIP.getUniqueId());
    }

    public void removeShipSelfDestruct(Spaceship currentss) {
        for (int i = 0; i < shipSelfDestructs.size(); i++) {
            if (shipSelfDestructs.get(i) == currentss.getKey()) {
                shipSelfDestructs.remove(i);
            }
        }
    }

    public void removeTroopSelfDestruct(Troop aTroop) {
        for (int i = 0; i < troopSelfDestructs.size(); i++) {
            if (troopSelfDestructs.get(i) == aTroop.getUniqueId()) {
                troopSelfDestructs.remove(i);
            }
        }
    }

    public void removeVIPSelfDestruct(VIP aVIP) {
        for (int i = 0; i < VIPSelfDestructs.size(); i++) {
            if (VIPSelfDestructs.get(i) == aVIP.getUniqueId()) {
                VIPSelfDestructs.remove(i);
            }
        }
    }


    public void addBuildingSelfDestruct(Building currentBuilding) {
        buildingSelfDestructs.add(currentBuilding.getUniqueId());
    }

    public void removeBuildingSelfDestruct(Building currentBuilding) {
        for (int i = 0; i < buildingSelfDestructs.size(); i++) {
            if (buildingSelfDestructs.get(i) == currentBuilding.getUniqueId()) {
                buildingSelfDestructs.remove(i);
            }
        }
    }

    public void addNewBlackMarketBid(int aSum, BlackMarketOffer aOffer, Planet destination, Player aPlayer) {
        // remove old any bid to this offer
        Expense oldExpenseBid = getBidToOffer(aOffer);
        if (oldExpenseBid != null) {
            removeExpense(oldExpenseBid);
        }
        // add new bid if sum > 0
        if (aSum > 0) {
            if (destination == null) {
                addExpenses(new Expense("blackmarketbid", new BlackMarketBid(aSum, aOffer.getUniqueId(), null), aPlayer));
            } else {
                addExpenses(new Expense("blackmarketbid", new BlackMarketBid(aSum, aOffer.getUniqueId(), destination.getName()), aPlayer));
            }
        }
    }

    /**
     * Returns the amount to give to aPlayer. If none is to be given (no gift exists),
     * 0 is returned.
     *
     * @param aPlayer The player to find gift to
     * @return gift sum to aPlayer
     */
    public int findGift(Player aPlayer) {
        int giftSum = 0;
        for (Expense anExpense : expenses) {
            if (anExpense.isTransaction()) {
                if (aPlayer.isPlayer(anExpense.getPlayerName())) {
                    giftSum = anExpense.getSum();
                }
            }
        }
        return giftSum;
    }

    public boolean getShipSelfDestruct(Spaceship ss) {
        boolean found = false;
        for (int i = 0; i < shipSelfDestructs.size(); i++) {
            if (ss.getKey() == shipSelfDestructs.get(i)) {
                found = true;
            }
        }
        return found;
    }

    public boolean getTroopSelfDestruct(Troop aTroop) {
        boolean found = false;
        for (int i = 0; i < troopSelfDestructs.size(); i++) {
            int tempTroop = troopSelfDestructs.get(i);
            if (tempTroop == aTroop.getUniqueId()) {
                found = true;
            }
        }
        return found;
    }

    public boolean getVIPSelfDestruct(VIP aVIP) {
        boolean found = false;
        for (int i = 0; i < VIPSelfDestructs.size(); i++) {
            String tempVIP = VIPSelfDestructs.get(i);
            if (tempVIP == aVIP.getUniqueId()) {
                found = true;
            }
        }
        return found;
    }

    public boolean getScreenedShip(Spaceship ss) {
        boolean found = false;
        for (int i = 0; i < screenedShips.size(); i++) {
            if (ss.getKey() == screenedShips.get(i)) {
                found = true;
            }
        }
        return found;
    }

    public boolean getBuildingSelfDestruct(Building aBuilding) {
        boolean found = false;
        for (int i = 0; i < buildingSelfDestructs.size(); i++) {
            if (buildingSelfDestructs.get(i) == aBuilding.getUniqueId()) {
                found = true;
            }
        }
        return found;
    }

    // leta igenom alla VIPMoves och kolla om någon av dem förflyttar iväg denna vip
    public boolean VIPWillStay(VIP tempEngineer) {
        boolean vipStays = true;
        int i = 0;
        while ((i < VIPMoves.size()) & (vipStays)) {
            VIPMovement tempVIPMove = VIPMoves.get(i);
            if (tempVIPMove.isThisVIP(tempEngineer)) {
                vipStays = false;
            } else {
                i++;
            }
        }
        return vipStays;
    }

    public Expense getBidToOffer(BlackMarketOffer tempOffer) {
        Expense found = null;
        int i = 0;
        while ((i < expenses.size()) & (found == null)) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.getBlackMarketBid() != null) {
                if (tempExpense.getBlackMarketBid().getOfferUniqueId() == tempOffer.getUniqueId()) {
                    found = tempExpense;
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }
        return found;
    }

    public boolean isAbandonGame() {
        return abandonGame;
    }

    public void setAbandonGame(boolean abandonGame) {
        this.abandonGame = abandonGame;
    }

    public List<ResearchOrder> getResearchOrders() {
        return researchOrder;
    }

    public ResearchOrder getResearchOrder(String name) {
        for (int i = 0; i < researchOrder.size(); i++) {
            if (researchOrder.get(i).getAdvantageName().equals(name)) {
                return researchOrder.get(i);
            }
        }
        return null;
    }

    public void setResearchOrders(List<ResearchOrder> researchOrder) {
        this.researchOrder = researchOrder;
    }

    public void addResearchOrder(ResearchOrder researchOrder, Player p) {
        this.researchOrder.add(researchOrder);
        if (researchOrder.getCost() > 0) {
            addExpenses(new Expense("research", researchOrder, p));
        }


        //Expense(String temptype, ResearchOrder ro, Player aPlayer, int aSum){
    }

    public void removeResearchOrder(ResearchOrder researchOrder, Galaxy aGalaxy) {
        int findIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isResearchOrder(researchOrder.getAdvantageName())) {
                findIndex = i;
            }
        }
        if (findIndex > -1) {
            removeExpense(expenses.get(findIndex));
        }
        this.researchOrder.remove(researchOrder);
    }

    public void removeResearchOrder(String rOrder, Galaxy aGalaxy) {
        for (int i = 0; i < researchOrder.size(); i++) {
            if (researchOrder.get(i).getAdvantageName().equals(rOrder)) {
                researchOrder.remove(i);
            }
        }
        int findIndex = -1;
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (tempExpense.isResearchOrder(rOrder)) {
                findIndex = i;
            }
        }
        if (findIndex > -1) {
            removeExpense(expenses.get(findIndex));
        }
    }

    // kolla om det finns en gammal order för denna forskning
    public boolean checkResearchOrder(String rOrder) {
        boolean found = false;
        int i = 0;
        while ((found == false) & (i < researchOrder.size())) {
            ResearchOrder tempResearchOrder = researchOrder.get(i);
            if (tempResearchOrder.getAdvantageName().equals(rOrder)) {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }
}