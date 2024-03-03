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
    private List<String> buildingSelfDestructs = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "DESTROY_TROOP")
    @Builder.Default
    private List<String> troopSelfDestructs = new ArrayList<>();
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

    public List<String> getVIPSelfDestructs() {
        return VIPSelfDestructs;
    }

    public List<VIPMovement> getVIPMoves() {
        return VIPMoves;
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

    public List<String> getBuildingSelfDestructs() {
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
            if (aShip.getUuid() == screenedShips.get(i)) {
                found = i;
            }
        }

        return found;
    }


    public void addOrRemoveScreenedShip(Spaceship aShip) {
        // finns det en screened ship order redan?
        int found = -1;
        for (int i = 0; i < screenedShips.size(); i++) {
            if (aShip.getUuid() == screenedShips.get(i)) {
                found = i;
            }
        }
        if (found > -1) { // ta bort den
            screenedShips.remove(found);
        } else { // annars l�gg till den
            screenedShips.add(aShip.getUuid());
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
            if (inPlanet.getName().equalsIgnoreCase(aTroopToPlanetMovement.getPlanetName())) {
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

    public void addNewTransaction(int aSum, Player recipient) {
        // först kolla om det finns en gammal transaktion som skall tas bort
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


    public boolean haveSpaceshipTypeBuildOrder(SpaceshipType aSpaceshipType) {
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (aSpaceshipType.getName().equals(tempExpense.getSpaceshipTypeUuid())) {
                return true;
            }
        }
        return false;
    }


    public boolean haveBuildingTypeBuildOrder(BuildingType aBuildingType, String buildingKey) {
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (aBuildingType.getName().equals(tempExpense.getBuildingTypeUuid())) {
                if (buildingKey == null || buildingKey.equalsIgnoreCase(tempExpense.getBuildingUuid())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean haveVIPTypeBuildOrder(VIPType aVIPType) {
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (aVIPType.getUuid().equals(tempExpense.getTypeVIPUuid())) {
                return true;
            }
        }
        return false;
    }

    public boolean haveTroopTypeBuildOrder(TroopType aTroopType) {
        for (int i = 0; i < expenses.size(); i++) {
            Expense tempExpense = (Expense) expenses.get(i);
            if (aTroopType.getName().equals(tempExpense.getTroopTypeUuid())) {
                return true;
            }
        }
        return false;
    }

    public void addShipSelfDestruct(Spaceship currentss) {
        shipSelfDestructs.add(currentss.getUuid());
    }

    public void addTroopSelfDestruct(Troop aTroop) {
        troopSelfDestructs.add(aTroop.getUuid());
    }

    public void addVIPSelfDestruct(VIP aVIP) {

        int found = -1;
        for (int i = 0; i < VIPMoves.size(); i++) {
            VIPMovement tempVIPMove = VIPMoves.get(i);
            if (aVIP.getUuid().equalsIgnoreCase(tempVIPMove.getVipKey())) {
                found = i;
            }
        }
        if (found > -1) {
            VIPMoves.remove(found);
        }

        VIPSelfDestructs.add(aVIP.getUuid());
    }

    public void removeShipSelfDestruct(Spaceship currentss) {
        for (int i = 0; i < shipSelfDestructs.size(); i++) {
            if (shipSelfDestructs.get(i) == currentss.getUuid()) {
                shipSelfDestructs.remove(i);
            }
        }
    }

    public void removeTroopSelfDestruct(Troop aTroop) {
        for (int i = 0; i < troopSelfDestructs.size(); i++) {
            if (troopSelfDestructs.get(i).equalsIgnoreCase(aTroop.getUuid())) {
                troopSelfDestructs.remove(i);
            }
        }
    }

    public void removeVIPSelfDestruct(VIP aVIP) {
        for (int i = 0; i < VIPSelfDestructs.size(); i++) {
            if (VIPSelfDestructs.get(i) == aVIP.getUuid()) {
                VIPSelfDestructs.remove(i);
            }
        }
    }


    public void addBuildingSelfDestruct(Building currentBuilding) {
        buildingSelfDestructs.add(currentBuilding.getUuid());
    }

    public void removeBuildingSelfDestruct(Building currentBuilding) {
        for (int i = 0; i < buildingSelfDestructs.size(); i++) {
            if (buildingSelfDestructs.get(i).equalsIgnoreCase(currentBuilding.getUuid())) {
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
            if (ss.getUuid() == shipSelfDestructs.get(i)) {
                found = true;
            }
        }
        return found;
    }

    public boolean getVIPSelfDestruct(VIP aVIP) {
        boolean found = false;
        for (int i = 0; i < VIPSelfDestructs.size(); i++) {
            String tempVIP = VIPSelfDestructs.get(i);
            if (tempVIP == aVIP.getUuid()) {
                found = true;
            }
        }
        return found;
    }

    public boolean getScreenedShip(Spaceship ss) {
        boolean found = false;
        for (int i = 0; i < screenedShips.size(); i++) {
            if (ss.getUuid() == screenedShips.get(i)) {
                found = true;
            }
        }
        return found;
    }

    public boolean getBuildingSelfDestruct(Building aBuilding) {
        boolean found = false;
        for (int i = 0; i < buildingSelfDestructs.size(); i++) {
            if (buildingSelfDestructs.get(i).equalsIgnoreCase(aBuilding.getUuid())) {
                found = true;
            }
        }
        return found;
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

    public void addResearchOrder(ResearchOrder researchOrder, Player p) {
        this.researchOrder.add(researchOrder);
        if (researchOrder.getCost() > 0) {
            addExpenses(new Expense("research", researchOrder, p));
        }


        //Expense(String temptype, ResearchOrder ro, Player aPlayer, int aSum){
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