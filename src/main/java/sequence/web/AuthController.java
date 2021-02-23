package sequence.web;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sequence.model.User;
import sequence.service.AuthService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

	private static final Logger log = Logger.getLogger(AuthController.class);

    Gson gson = new Gson();

	@Autowired
	AuthService authService;

	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public String authenticate(ModelMap modelMap,
							   RedirectAttributes redirect,
							   HttpServletRequest request,
							   @ModelAttribute("user") User user){
		return authService.authenticate(user, redirect, request);
	}


	@RequestMapping(value="/signout", method=RequestMethod.GET)
	public String deAuthenticate(HttpServletRequest request,
						  RedirectAttributes redirect){
		return authService.deAuthenticate(redirect, request);
	}

}