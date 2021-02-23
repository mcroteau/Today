package sequence.web;

import sequence.model.Prospect;
import sequence.service.ProspectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProspectController {

    @Autowired
    ProspectService prospectService;

    @GetMapping(value="/prospects/{id}")
    public String index(ModelMap modelMap,
                        @PathVariable Long id){
        return prospectService.index(id, modelMap);
    }

    @GetMapping(value="/prospects/create")
    public String index(ModelMap modelMap){
        return prospectService.create(modelMap);
    }

    @PostMapping(value="/prospects/save")
    public String save(@ModelAttribute("prospect") Prospect prospect,
                          RedirectAttributes redirect){
        return prospectService.save(prospect, redirect);
    }

    @GetMapping(value="/prospects")
    public String getProspectes(@RequestParam String query, ModelMap modelMap){
        return prospectService.getProspects(query, modelMap);
    }

    @GetMapping(value="/prospects/edit/{id}")
    public String getEdit(ModelMap modelMap,
                          @PathVariable Long id){
        return prospectService.getEdit(id, modelMap);
    }

    @PostMapping(value="/prospects/update")
    public String update(@ModelAttribute("prospect") Prospect prospect,
                            RedirectAttributes redirect){
        return prospectService.update(prospect, redirect);
    }

    @PostMapping(value="/prospects/delete/{id}")
    public String delete(@PathVariable Long id,
                            RedirectAttributes redirect){
        return prospectService.delete(id, redirect);
    }

}
