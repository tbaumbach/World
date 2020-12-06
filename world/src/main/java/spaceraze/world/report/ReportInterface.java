package spaceraze.world.report;

import java.util.List;

public interface ReportInterface {

    String getFullReport();

    List<EventReport> getChildReports();

    String getReport();

    EventReport getParent();
}
