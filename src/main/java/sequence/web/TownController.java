package sequence.web;

import sequence.model.Town;
import sequence.service.TownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TownController {

    @Autowired
    TownService townService;

    @GetMapping(value="/towns/{uri}")
    public String index(ModelMap modelMap,
                        @PathVariable String uri){
        return townService.index(uri, modelMap);
    }

    @GetMapping(value="/admin/towns/create")
    public String index(ModelMap modelMap){
        return townService.create(modelMap);
    }

    @PostMapping(value="/admin/towns/save")
    public String save(@ModelAttribute("town") Town town,
                          RedirectAttributes redirect){
        return townService.save(town, redirect);
    }

    @GetMapping(value="/admin/towns")
    public String getProjects(ModelMap modelMap){
        return townService.getTowns(modelMap);
    }

    @GetMapping(value="/admin/towns/edit/{id}")
    public String getEdit(ModelMap modelMap,
                          @PathVariable Long id){
        return townService.getEdit(id, modelMap);
    }

    @PostMapping(value="/admin/towns/update")
    public String update(@ModelAttribute("town") Town town,
                            RedirectAttributes redirect){
        return townService.update(town, redirect);
    }

    @PostMapping(value="/admin/towns/delete/{id}")
    public String delete(@PathVariable Long id,
                            RedirectAttributes redirect){
        return townService.delete(id, redirect);
    }

}
