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
    AuthService authService;

    public String index() {
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
