package sequence.service;

import org.apache.commons.collections.ArrayStack;
import org.springframework.ui.Model;
import sequence.common.Constants;
import sequence.model.*;
import sequence.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.*;

@Service
public class BasicService {

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
            Avg Sequence.
            Avg Sequence for Customers
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

        modelMap.put("prospectCount", count);
        modelMap.put("efforts", efforts);
        modelMap.put("prospectCounts", prospectCounts);

        return "index";
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

}
