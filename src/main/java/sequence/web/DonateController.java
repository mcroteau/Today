package sequence.web;

import com.google.gson.Gson;
import sequence.model.DonationInput;
import sequence.service.DonateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class DonateController {

    private static final Logger log = Logger.getLogger(DonateController.class);

    Gson gson = new Gson();

    @Autowired
    DonateService donateService;

    @GetMapping(value="/donate")
    public String index(ModelMap modelMap){
        return donateService.index(modelMap);
    }

    @GetMapping(value="/donate/{id}")
    public String location(@PathVariable Long id, ModelMap modelMap){
        return donateService.location(id, modelMap);
    }

    @GetMapping(value="/donate/cleanup")
    public String cleanup(){
        return donateService.cleanup();
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @CrossOrigin(origins="*")
    @RequestMapping(value="/donate/make", method=RequestMethod.POST, consumes="application/json")
    public @ResponseBody String make(@RequestBody DonationInput donationInput, Exception ex){
        return gson.toJson(donateService.make(donationInput));
    }

    @CrossOrigin(origins="*")
    @RequestMapping(value="/donate/cancel/{subscriptionId}", method=RequestMethod.DELETE)
    public @ResponseBody String cancel(@PathVariable String subscriptionId){
        return gson.toJson(donateService.cancel(subscriptionId));
    }

    @CrossOrigin(origins="*")
    @RequestMapping(value="/donate/cancel/{locationId}/{subscriptionId}", method=RequestMethod.DELETE)
    public @ResponseBody String cancel(@PathVariable Long locationId,
                                        @PathVariable String subscriptionId){
        return gson.toJson(donateService.cancel(locationId, subscriptionId));
    }

    @GetMapping(value="/donate/momentum")
    public String momentum(ModelMap modelMap){
        return donateService.momentum(modelMap);
    }

}
