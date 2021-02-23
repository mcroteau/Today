package sequence.web;

import sequence.model.Prospect;
import sequence.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping(value="/locations/{id}")
    public String index(ModelMap modelMap,
                        @PathVariable Long id){
        return locationService.index(id, modelMap);
    }

    @GetMapping(value="/locations/create")
    public String index(ModelMap modelMap){
        return locationService.create(modelMap);
    }

    @PostMapping(value="/locations/save")
    protected String save(@ModelAttribute("location") Prospect prospect,
                          RedirectAttributes redirect){
        return locationService.save(prospect, redirect);
    }

    @GetMapping(value="/locations")
    public String getProjects(ModelMap modelMap){
        return locationService.getLocations(modelMap);
    }

    @GetMapping(value="/locations/edit/{id}")
    public String getEdit(ModelMap modelMap,
                              @PathVariable Long id){
        return locationService.getEdit(id, modelMap);
    }

    @PostMapping(value="/locations/update")
    protected String update(@ModelAttribute("location") Prospect prospect,
                            RedirectAttributes redirect) throws Exception {
        return locationService.update(prospect, redirect);
    }

    @PostMapping(value="/locations/delete/{id}")
    protected String delete(@PathVariable Long id,
                            RedirectAttributes redirect){
        return locationService.delete(id, redirect);
    }

}
