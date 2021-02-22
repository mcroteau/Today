package sequence.service;

import sequence.common.Constants;
import sequence.model.*;
import sequence.repository.DailyRepo;
import sequence.repository.ProspectRepo;
import sequence.repository.TownRepo;
import sequence.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class LocationService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    TownRepo townRepo;

    @Autowired
    ProspectRepo prospectRepo;

    @Autowired
    AuthService authService;

    public String index(String uri, ModelMap modelMap) {
        Prospect prospect = prospectRepo.get(uri);
        String count = NumberFormat.getInstance(Locale.US).format(prospect.getCount());
        modelMap.put("count", count);
        modelMap.put("prospect", prospect);
        return "prospect/index";
    }

    public String create(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return "redirect:/";
        }
        List<Town> towns = townRepo.getList();
        modelMap.put("towns", towns);
        return "location/create";
    }

    public String getLocations(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return "redirect:/";
        }

        List<Prospect> prospects = prospectRepo.getList();
        modelMap.addAttribute("locations", prospects);

        return "location/list";
    }

    public String save(Prospect prospect, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return "redirect:/";
        }

        if(prospect.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web prospect a name...");
            return "redirect:/admin/locations/create";
        }

        Prospect savedProspect = prospectRepo.save(prospect);
        return "redirect:/admin/locations/edit/" + savedProspect.getId();
    }

    public String getEdit(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return "redirect:/";
        }

        List<Town> towns = townRepo.getList();
        Prospect prospect = prospectRepo.get(id);

        String devKey = lightService.get("dev." + prospect.getId());
        String liveKey = lightService.get("live." + prospect.getId());

        prospect.setDevKey(devKey);
        prospect.setLiveKey(liveKey);

        modelMap.put("towns", towns);
        modelMap.put("prospect", prospect);

        return "prospect/edit";
    }

    public String update(Prospect prospect, RedirectAttributes redirect) throws Exception {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Constants.SUPER_DUPER)){
            return "redirect:/";
        }

        if(prospect.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your prospect a name...");
            return "redirect:/admin/prospects/edit/" + prospect.getId();
        }

        if(prospect.getDevKey() != null &&
                prospect.getLiveKey() != null){
            lightService.write("dev." + prospect.getId(), prospect.getDevKey());
            lightService.write("live." + prospect.getId(), prospect.getLiveKey());
        }

        List<Prospect> prospects = prospectRepo.getList();
        try {
            sitemapService.writeLocations(prospects);
        }catch(Exception ex){
            ex.printStackTrace();
        }


        prospectRepo.update(prospect);

        redirect.addFlashAttribute("message", "Successfully updated prospect");
        return "redirect:/admin/prospects/edit/" + prospect.getId();
    }

    public String delete(Long id, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        dailyRepo.deleteCounts(id);
        prospectRepo.delete(id);
        redirect.addFlashAttribute("message", "Successfully deleted location.");

        return "redirect:/admin/locations";
    }

}
