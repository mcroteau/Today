package sequence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sequence.model.Prospect;
import sequence.repository.ProspectRepo;

import java.util.List;

@Service
public class ProspectService {

    @Autowired
    AuthService authService;

    @Autowired
    ProspectRepo prospectRepo;


    public String index(Long id, ModelMap modelMap) {
        Prospect prospect = prospectRepo.get(id);
        modelMap.put("prospect", prospect);
        return "prospect/index";
    }

    public String create(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        return "prospect/create";
    }

    public String save(Prospect prospect, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(prospect.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web prospect a name...");
            return "redirect:/prospects/create";
        }

        prospectRepo.save(prospect);
        return "redirect:/prospects";
    }

    public String getEdit(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        Prospect prospect = prospectRepo.get(id);
        modelMap.put("prospect", prospect);

        return "prospect/edit";
    }

    public String update(Prospect prospect, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(prospect.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web prospect a name...");
            return "redirect:/prospects/edit/" + prospect.getId();
        }

        prospectRepo.update(prospect);
        redirect.addFlashAttribute("message", "Successfully updated prospect");
        return "redirect:/prospects/edit/" + prospect.getId();
    }

    public String getProspects(String query, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        List<Prospect> prospects = prospectRepo.getResults(query);
        modelMap.put("prospects", prospects);

        return "prospect/list";
    }

    public String delete(Long id, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        prospectRepo.deleteActivities(id);
        prospectRepo.delete(id);
        redirect.addFlashAttribute("message", "Successfully deleted prospect.");

        return "redirect:/prospects";
    }

}
