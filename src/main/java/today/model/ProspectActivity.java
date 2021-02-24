package sequence.model;

import sequence.common.Constants;
import sequence.common.Sequence;
import sun.java2d.pipe.SpanShapeRenderer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProspectActivity {

    Long id;
    Long prospectId;
    Long activityId;
    Long effortId;
    Long completeDate;
    Boolean completed;

    String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProspectId() {
        return prospectId;
    }

    public void setProspectId(Long prospectId) {
        this.prospectId = prospectId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getEffortId() {
        return effortId;
    }

    public void setEffortId(Long effortId) {
        this.effortId = effortId;
    }

    public Long getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Long completeDate) {
        this.completeDate = completeDate;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getDate() {
        return Sequence.getPretty(completeDate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
