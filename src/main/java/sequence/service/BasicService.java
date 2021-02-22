package sequence.service;

import sequence.model.Prospect;
import sequence.model.Town;
import sequence.repository.ProspectRepo;
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
    TownRepo townRepo;

    @Autowired
    ProspectRepo prospectRepo;

    @Autowired
    AuthService authService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private EmailService emailService;


    public String index() {
        if(authService.isAuthenticated()){
            long id =  authService.getUser().getId();
            return "redirect:/user/edit/" + id;
        }
        return "redirect:/home";
    }

    public String home(ModelMap modelMap){
        List<Prospect> prospects = prospectRepo.getList();
        List<Town> towns = townRepo.getList();
        long sum = 0;
        for(Town town: towns){
            if(!town.getCountZero().equals(""))
                sum += town.getCount();
        }
        String count = NumberFormat.getNumberInstance(Locale.US).format(sum);

        modelMap.put("count", count);
        modelMap.put("towns", towns);
        modelMap.put("prospects", prospects);

        return "home";
    }

    public String towns(ModelMap modelMap){
        List<Town> towns = townRepo.getList();
        long sum = 0;
        for(Town town: towns){
            if(!town.getCountZero().equals(""))
                sum += town.getCount();
        }

        String count = NumberFormat.getInstance(Locale.US).format(sum);

        modelMap.put("count", count);
        modelMap.put("towns", towns);
        return "basic/towns";
    }

    public String shelters(ModelMap modelMap){
        List<Town> locations = new ArrayList();
        List<Town> towns = townRepo.getList();

        long count = 0;
        for(Town town: towns){
            List<Prospect> townProspects = prospectRepo.getList(town.getId());
            town.setLocations(townProspects);
            locations.add(town);
            count += town.getCount();
        }

        modelMap.put("count", count);
        modelMap.put("locations", locations);
        return "basic/shelters";
    }


    public String showSignin() {
        if(authService.isAuthenticated()){
            return "redirect:/";
        }
        return "basic/signin";
    }

    public String beginReport() {
        phoneService.support("Sequence +Gain issue");
        return "basic/report";
    }

    public String reportIssue(String email, String issue, ModelMap modelMap, RedirectAttributes redirect) {

        if (email.equals("")) {
            redirect.addFlashAttribute("error", "Please enter a valid email address");
            return "redirect:/issues/report";
        }

        if (issue.equals("")) {
            redirect.addFlashAttribute("error", "Issue was left black, please tell us what happened.");
            return "redirect:/issues/report";
        }

        StringBuffer sb = new StringBuffer();
        sb.append(email);
        sb.append("<br/>");
        sb.append(issue);
        emailService.send("croteau.mike@gmail.com", "Okay!", sb.toString());

        modelMap.addAttribute("message", "Thank you. Issue has been reported.");
        return "basic/success";
    }

}
