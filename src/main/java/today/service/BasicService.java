package today.service;

import com.google.gson.Gson;
import org.apache.commons.collections.ArrayStack;
import org.springframework.ui.Model;
import today.common.Constants;
import today.model.*;
import today.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.*;

@Service
public class BasicService {

    Gson gson = new Gson();

    @Autowired
    AuthService authService;

    @Autowired
    ProspectRepo prospectRepo;

    @Autowired
    StatusRepo statusRepo;

    @Autowired
    EffortRepo effortRepo;

    @Autowired
    ActivityRepo activityRepo;

    public String index(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/signin";
        }
        /*
            Prospects Count
            # Prospect in prospect|working|customer
            Actions by percent
            Avg Today.
            Avg Today for Customers
         */
        Long count = prospectRepo.getCount();
        List<ProspectCount> prospectCounts = new ArrayList<>();
        List<Status> statuses = statusRepo.getList();
        for(Status status: statuses){
            ProspectCount prospectCount = new ProspectCount();
            prospectCount.setCount(prospectRepo.getCount(status.getId()));
            prospectCount.setStatus(status);
            prospectCounts.add(prospectCount);
        }

        List<Effort> efforts = effortRepo.getSuccesses();
        for(Effort effort: efforts){
            Prospect prospect = prospectRepo.get(effort.getProspectId());
            effort.setProspect(prospect);

            Status startingStatus = statusRepo.get(effort.getStartingStatusId());
            Status endingStatus = statusRepo.get(effort.getEndingStatusId());

            effort.setStartingStatus(startingStatus);
            effort.setEndingStatus(endingStatus);

            List<ProspectActivity> prospectActivities = effortRepo.getActivities(effort.getId());
            for(ProspectActivity prospectActivity: prospectActivities){
                Activity activity = activityRepo.get(prospectActivity.getActivityId());
                prospectActivity.setName(activity.getName());
            }
            effort.setProspectActivities(prospectActivities);
        }

        /**
         Call > Meeting > Email > Call > Meeting >

         Call > Meeting > Email > Meeting > Call >

         Meeting > Meeting > Demo > Call > Email >

         Email > Demo > Meeting > Meeting > Meeting >

         results in

         call(2), meeting(2), email(2), call, meeting
         call(2), meeting(2), email(2), meeting, call
         meeting(1), meeting(1) demo(1)...
         email(1), demo(1)

         data: Object {
             name: "flare"
             children: Array(10) [
                 0: Object {
                    name: "analytics"
                    children: Array(3) [Object, Object, Object]
                 }
         */

//        List<List<ActivityCount>> data = new ArrayList<>();
//        List<ActivityCount> emptyCounts = new ArrayList<>();
//        for(Effort effort: efforts){
//            List<ActivityCount> counts = getConnections(0, effort, emptyCounts);
//            data.add(counts);
//        }
//        System.out.println(gson.toJson(data));



        modelMap.put("efforts", efforts);
        modelMap.put("prospectCount", count);
        modelMap.put("prospectCounts", prospectCounts);

        return "index";
    }


    private List<ActivityCount> getConnections(Integer idx,
                                               Effort effort,
                                               List<ActivityCount> counts){

        List<ProspectActivity> prospectActivities = effortRepo.getActivities(effort.getId());
        for(int z = idx; z < prospectActivities.size(); z++){
            ProspectActivity prospectActivity = prospectActivities.get(z);
            Activity activity = activityRepo.get(prospectActivity.getActivityId());

            if(counts.size() > z){
                if(counts.get(z).getName().equals(activity.getName())) {

                    Boolean isSame = counts.hashCode() == counts.get(z).getChildren().hashCode();

                    ActivityCount existing = counts.get(z);
                    int count = existing.getCount();
                    count++;
                    existing.setIndex(idx);
                    existing.setCount(count);

                    List<ActivityCount> children = new ArrayList<>();
//                    if(counts.size() > z+1) {
//                        ActivityCount childActivityCount = new ActivityCount();
//                        childActivityCount.setName(counts.get(z+1).getName());
//                        childActivityCount.setCount(1);
//                        childActivityCount.setIndex(idx);
//                        childActivityCount.setChildren(new ArrayList<>());
//                        children.add(childActivityCount);
//                    }

//                    if(isSame) {
//                        getConnections(z, effort, counts);
//                    }else{
//                        getConnections(z, effort, existing.getChildren());
//                    }
                }
            }else{

                ActivityCount activityCount = new ActivityCount();
                activityCount.setName(activity.getName());
                activityCount.setCount(1);
                activityCount.setIndex(idx);

                List<ActivityCount> children = new ArrayList<>();
                if(counts.size() > z+1) {
                    ActivityCount childActivityCount = new ActivityCount();
                    childActivityCount.setName(counts.get(z+1).getName());
                    childActivityCount.setCount(1);
                    childActivityCount.setIndex(idx);
                    childActivityCount.setChildren(new ArrayList<>());
                    children.add(childActivityCount);
                }

                activityCount.setChildren(children);
                counts.add(activityCount);
                continue;
            }
            idx++;
            if(counts.size() > z){
                Boolean isSame = counts.hashCode() == counts.get(z).getChildren().hashCode();

                if(isSame) {
                    getConnections(z, effort, counts);
                }else{
                    getConnections(z, effort, counts.get(z).getChildren());
                }
            }
        }
        return counts;
    }




    private List<ActivityCount> getIt(List<ActivityCount> acs, List<ActivityCount> combined){
        for(int z = 0; z < acs.size(); z++){
            ActivityCount ac = acs.get(z);
            if(combined.get(z).equals(ac.getName())){
                int count = ac.getCount();
                count++;
                ac.setCount(count);
            }else{
                combined.add(ac);
                getIt(acs, combined);
            }
        }
        return combined;
    }


    public String showSignin() {
        if(authService.isAuthenticated()){
            return "redirect:/overview";
        }
        return "basic/signin";
    }

    public String showSignup() {
        authService.signout();
        return "basic/signup";
    }

    public String tidyTree() {
        List<Effort> efforts = effortRepo.getSuccesses();

        ActivityGraph activityGraph = new ActivityGraph();
        activityGraph.setName("Prospect");
        activityGraph.setChildren(new ArrayList<>());
        List<ActivityCount> emptyCounts = new ArrayList<>();

        for(Effort effort: efforts){
            List<ActivityCount> counts = getConnections(0, effort, emptyCounts);
            activityGraph.setChildren(counts);
        }
        System.out.println(gson.toJson(activityGraph));


//        for(int n = 0; n < 4; n++){
//            ActivityCount activityCount = new ActivityCount();
//            activityCount.setName("Mock" + n);
//            activityCount.setChildren(new ArrayList<>());
//            activityGraph.getChildren().add(activityCount);
//        }

        System.out.println(gson.toJson(activityGraph));

        return gson.toJson(activityGraph);
    }
}
