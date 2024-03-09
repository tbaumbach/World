package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.*;
import spaceraze.world.enums.HighlightType;

import jakarta.persistence.*;

/**
 * @author WMPABOD
 * <p>
 * All information about a turn is stored in the infoStrings.
 * The information that should be highlighted for the player is also added to
 * the highlights list.
 * Lost and destroyed ships are also added in a separate list
 */
//TODO 2020-11-28 This should be replaced by EvenReport logic.
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity()
@Table(name = "REPORT")
public class Report implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "FK_TURN_INFO_GENERAL")
    TurnInfo turnInfoGeneralReport;

    @ManyToOne
    @JoinColumn(name = "FK_TURN_INFO_EXPENSE")
    TurnInfo turnInfoExpenseReports;

    @ManyToOne
    @JoinColumn(name = "FK_TURN_INFO_VIP")
    TurnInfo turnInfoVIPReports;

    @ManyToOne
    @JoinColumn(name = "FK_TURN_INFO_BLACK_MARKET")
    TurnInfo turnInfoBlackMarketReports;

    @ManyToOne
    @JoinColumn(name = "FK_TURN_INFO_CIVILIAN")
    TurnInfo turnInfoCivilianReports;

    @ManyToOne
    @JoinColumn(name = "FK_TURN_INFO_RESSEARCH")
    TurnInfo turnInfoResearchReports;

    @ManyToOne
    @JoinColumn(name = "FK_TURN_INFO_BUILDING")
    TurnInfo turnInfoBuildingReports;

    @ManyToOne
    @JoinColumn(name = "FK_TURN_INFO_DIPLOMACY")
    TurnInfo turnInfoDiplomacyReports;

    // All information about last turn is stored in this vector
    @ElementCollection
    @CollectionTable(name = "REPORT_INFORMATIONS")
    @Builder.Default
    List<String> infoStrings = new ArrayList<>();
    // important information from last turn will also be stored in this vector
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    @Builder.Default
    List<Highlight> highlights = new ArrayList<>();

    //TODO 2020-11-28 This should be replaced by EvenReport logic. So add the lost ships to the new specific created Report (for the typ of event) extending EvenReport. Try to reuse the EnemySpaceship and OwnSpaceship

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportLostShips")
    @Builder.Default
    List<CanBeLostInSpace> shipsLostInSpace = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportLostTroops")
    @Builder.Default
    List<CanBeLostInSpace> troopsLostInSpace = new ArrayList<>();


    public void addReport(String str) {
        infoStrings.add(str);
    }

    public void addReportAt(String str, int index) {
        infoStrings.add(index, str);
    }

    public void addHighlight(String str, HighlightType type) {
        highlights.add(new Highlight(str, type));
    }


    /**
     * Returns a sorted list of highlight objects
     *
     * @return
     */
    public List<Highlight> getHighlights() {
        // sort the list first
        Collections.sort(highlights);
        return highlights;
    }

    public List<CanBeLostInSpace> getLostShips() {
      //TODO 2020-11-28 This should be replaced by EvenReport logic. So add the lost ships to the new specific created Report (for the typ of event) extending EvenReport. Try to reuse the EnemySpaceship and OwnSpaceship
  	return shipsLostInSpace;
    }

    public List<CanBeLostInSpace> getLostTroops() {
      //TODO 2020-11-28 This should be replaced by EvenReport logic. So add the lost ships to the new specific created Report (for the typ of event) extending EvenReport. Try to reuse the EnemySpaceship and OwnSpaceship

        return troopsLostInSpace;
    }

    public List<String> getAllReports() {
        return infoStrings;
    }

    public int size() {
        return infoStrings.size();
    }

    public String getInfoAt(int nr) {
        return infoStrings.get(nr);
    }

    public int findInfoIndex(String findString) {
        int found = -1;
        int index = 0;
        while ((found == -1) & (index < infoStrings.size())) {
            String tempString = infoStrings.get(index);
            if (tempString.equalsIgnoreCase(findString)) {
                found = index;
            } else {
                index++;
            }
        }
        return found;
    }
}