package spaceraze.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import spaceraze.world.enums.HighlightType;
import spaceraze.world.incomeExpensesReports.IncomeReport;
import spaceraze.world.incomeExpensesReports.IncomeType;
import spaceraze.util.general.Logger;

import javax.persistence.*;

//TODO 2020-11-28 This should be replaced by EvenReport logic.
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity()
@Table(name = "TURN_INFO")
public class TurnInfo implements Serializable {
    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnInfoGeneralReport")
    @Builder.Default
    private List<Report> generalReports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnInfoExpenseReports")
    @Builder.Default
    private List<Report> expenseReports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnInfoVIPReports")
    @Builder.Default
    private List<Report> VIPReports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnInfoBlackMarketReports")
    @Builder.Default
    private List<Report> blackMarketReports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnInfoCivilianReports")
    @Builder.Default
    private List<Report>civilianReports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnInfoResearchReports")
    @Builder.Default
    private List<Report>researchReports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnInfoBuildingReports")
    @Builder.Default
    private List<Report>buildingReports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnInfoDiplomacyReports")
    @Builder.Default
    private List<Report> diplomacyReports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnInfo")
    @Builder.Default
    private List<EconomyReport> economyReports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnInfo")
    @Builder.Default
    private List<IncomeReport> incomeReports = new ArrayList<>();

    @Transient
    public void newTurn() {
        generalReports.add(new Report());
        expenseReports.add(new Report());
        VIPReports.add(new Report());
        blackMarketReports.add(new Report());
        economyReports.add(new EconomyReport());
        civilianReports.add(new Report());
        researchReports.add(new Report());
        buildingReports.add(new Report());
        diplomacyReports.add(new Report());
        incomeReports.add(new IncomeReport());
    }

    @Transient
    public void addToLatestIncomeReport(IncomeType type, String desc, String location, int value) {
        IncomeReport ir = incomeReports.get(incomeReports.size() - 1);
        ir.addNewRow(type, desc, location, value);
    }
    @Transient
    public void addToLatestGeneralReport(String str) {
        Report r = generalReports.get(generalReports.size() - 1);
        r.addReport(str);
    }
    @Transient
    public void addToLatestGeneralReportAt(String str, int index) {
        Report r = generalReports.get(generalReports.size() - 1);
        r.addReportAt(str, index);
    }
    @Transient
    public void addToLatestDiplomacyReport(String str) {
        Report r = diplomacyReports.get(diplomacyReports.size() - 1);
        r.addReport(str);
    }
    @Transient
    public void addToLatestHighlights(String str, HighlightType type) {
        Report r = generalReports.get(generalReports.size() - 1);
        r.addHighlight(str, type);
    }
    @Transient
    public void addToLatestExpenseReport(String str) {
        Report r = expenseReports.get(expenseReports.size() - 1);
        r.addReport(str);
    }
    @Transient
    public void addToLatestVIPReport(String str) {
        Logger.finest("adding to general: " + str);
        Report r = VIPReports.get(VIPReports.size() - 1);
        r.addReport(str);
    }
    @Transient
    public void addToLatestBlackMarketReport(String str) {
        Report r = blackMarketReports.get(blackMarketReports.size() - 1);
        r.addReport(str);
    }
    @Transient
    public void addToLatestCivilianReport(String str) {
        Report r = civilianReports.get(civilianReports.size() - 1);
        r.addReport(str);
    }
    @Transient
    public void addToLatestResearchReport(String str) {
        Report r = researchReports.get(researchReports.size() - 1);
        r.addReport(str);
    }
    @Transient
    public void addToLatestBuldingsReport(String str) {
        Report r = buildingReports.get(buildingReports.size() - 1);
        r.addReport(str);
    }
    @Transient
    public Report getLatestGeneralReport() {
        Report r = generalReports.get(generalReports.size() - 1);
        return r;
    }
    @Transient
    public Report getLatestExpenseReport() {
        Report r = expenseReports.get(expenseReports.size() - 1);
        return r;
    }
    @Transient
    public Report getLatestVIPReports() {
        Report r = VIPReports.get(VIPReports.size() - 1);
        return r;
    }
    @Transient
    public Report getLatestBlackMarketReports() {
        Report r = blackMarketReports.get(blackMarketReports.size() - 1);
        return r;
    }
    @Transient
    public Report getLatestCivilianReports() {
        Report r = civilianReports.get(civilianReports.size() - 1);
        return r;
    }
    @Transient
    public Report getLatestResearchReports() {
        Report r = researchReports.get(researchReports.size() - 1);
        return r;
    }
    @Transient
    public IncomeReport getLatestIncomeReports() {
        IncomeReport ir = incomeReports.get(incomeReports.size() - 1);
        return ir;
    }
    @Transient
    public Report getLatestBuildingReports() {
        Report r = buildingReports.get(buildingReports.size() - 1);
        return r;
    }
    @Transient
    public Report getLatestDiplomacyReports() {
        Report r = diplomacyReports.get(diplomacyReports.size() - 1);
        return r;
    }
    @Transient
    public EconomyReport getLatestEconomyReport() {
        return getEconomyReport(economyReports.size() - 1);
    }
    @Transient
    public Report getGeneralReport(int index) {
        Report r = generalReports.get(index);
        return r;
    }
    @Transient
    public Report getExpenseReport(int index) {
        Report r = expenseReports.get(index);
        return r;
    }
    @Transient
    public Report getVIPReport(int index) {
        Report r = VIPReports.get(index);
        return r;
    }
    @Transient
    public Report getBlackMarketReport(int index) {
        Report r = blackMarketReports.get(index);
        return r;
    }
    @Transient
    public EconomyReport getEconomyReport(int index) {
        EconomyReport er = economyReports.get(index);
        return er;
    }
    @Transient
    public Report getCivilianReport(int index) {
        Report cr = civilianReports.get(index);
        return cr;
    }
    @Transient
    public Report getResearchReport(int index) {
        Report r = researchReports.get(index);
        return r;
    }
    @Transient
    public Report getBuildingsReport(int index) {
        Report r = buildingReports.get(index);
        return r;
    }
    @Transient
    public Report getDiplomacyReport(int index) {
        Report r = diplomacyReports.get(index);
        return r;
    }
    @Transient
    public int getGeneralSize() {
        Report r = generalReports.get(generalReports.size() - 1);
        return r.size();
    }

}

