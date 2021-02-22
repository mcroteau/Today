package sequence.service;

import sequence.common.Constants;
import sequence.model.Prospect;
import sequence.model.Town;
import sequence.repository.DailyRepo;
import sequence.repository.ProspectRepo;
import sequence.repository.TownRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class TownService {

    @Autowired
    TownRepo townRepo;

    @Autowired
    ProspectRepo prospectRepo;

    @Autowired
    DailyRepo dailyRepo;

    @Autowired
    AuthService authService;

    @Autowired
    SitemapService sitemapService;

    public String getPermission(String id){
        return Constants.TOWN_MAINTENANCE + id;
    }

    public String index(String uri, ModelMap modelMap) {
        Town town = townRepo.get(uri);
        List<Prospect> prospects = prospectRepo.getList(town.getId());

        long sum = 0;
        for(Prospect prospect : prospects){
            sum = sum + prospect.getCount();
        }
        String count = NumberFormat.getInstance(Locale.US).format(sum);

        modelMap.put("count", count);
        modelMap.put("town", town);
        modelMap.put("prospects", prospects);

        return "town/index";
    }

    public String create(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        return "town/create";
    }

    public String save(Town town, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(town.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web town a name...");
            return "redirect:/admin/towns/create";
        }

        townRepo.save(town);
        return "redirect:/admin/towns";
    }

    public String getEdit(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        Town town = townRepo.get(id);
        modelMap.put("town", town);

        return "town/edit";
    }

    public String update(Town town, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/";
        }

        if(town.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web town a name...");
            return "redirect:/admin/towns/edit/" + town.getId();
        }

        townRepo.update(town);

        List<Town> towns = townRepo.getList();
        try {
            sitemapService.writeTowns(towns);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        redirect.addFlashAttribute("message", "Successfully updated town");
        return "redirect:/admin/towns/edit/" + town.getId();
    }

    public String getTowns(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(!authService.isAdministrator()){
            return "redirect:/";
        }

        List<Town> towns = townRepo.getList();
        modelMap.put("towns", towns);

        return "town/list";
    }

    public String delete(Long id, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        List<Prospect> prospects = prospectRepo.getList(id);
        for(Prospect prospect : prospects){
            dailyRepo.deleteCounts(prospect.getId());
        }
        prospectRepo.deleteLocations(id);
        townRepo.delete(id);
        redirect.addFlashAttribute("message", "Successfully deleted town.");

        return "redirect:/admin/towns";
    }

}
