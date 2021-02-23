package sequence.service;

import org.springframework.ui.Model;
import sequence.model.Prospect;
import sequence.model.ProspectCount;
import sequence.model.Status;
import sequence.model.Town;
import sequence.repository.ProspectRepo;
import sequence.repository.StatusRepo;
import sequence.repository.TownRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class BasicService {

    @Autowired
    AuthService authService;

    @Autowired
    ProspectRepo prospectRepo;

    @Autowired
    StatusRepo statusRepo;

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




        modelMap.put("count", count);
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
