package sequence.web;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import sequence.model.*;
import sequence.service.*;


@Controller
public class UserController {

	private static final Logger log = Logger.getLogger(UserController.class);

	Gson gson = new Gson();

	@Autowired
	UserService userService;

	@GetMapping(value="/admin/user/list")
	public String getUsers(ModelMap modelMap){
		return userService.getUsers(modelMap);
	}

	@GetMapping(value="/user/edit/{id}")
	public String getEditUser(ModelMap modelMap,
								 @PathVariable Long id){
		return userService.getEditUser(id, modelMap);
	}


	@GetMapping(value="/user/edit_password/{id}")
	public String editPassword(ModelMap modelMap,
	                     	   @PathVariable Long id){
		return userService.editPassword(id, modelMap);
	}


	@PostMapping(value="/user/update_password/{id}")
	public String updatePassword(ModelMap modelMap,
								 RedirectAttributes redirect,
								 @ModelAttribute("user") User user){
    	return userService.updatePassword(user, modelMap, redirect);
	}

	@PostMapping(value="/user/delete/{id}")
	public String deleteUser(ModelMap modelMap,
								 RedirectAttributes redirect,
								 @PathVariable Long id) {
		return userService.deleteUser(id, modelMap, redirect);
	}

	@PostMapping(value="/register")
	protected String register(@ModelAttribute("user") User user,
							  @RequestParam(value="g-recaptcha-response", required = true ) String reCaptchaResponse,
							  HttpServletRequest request,
							  RedirectAttributes redirect){
    	return userService.register(reCaptchaResponse, user, request, redirect);
	}

	@GetMapping(value="/user/reset")
	public String reset(){
		return "user/reset";
	}


	@PostMapping(value="/user/send")
	public String sendReset(RedirectAttributes redirect,
				    		HttpServletRequest request,
				    		@RequestParam(value="username", required = true ) String username){
		return userService.sendReset(username, redirect, request);
	}


	@GetMapping(value="/user/confirm")
	public String confirm(ModelMap modelMap,
							RedirectAttributes redirect,
							@RequestParam(value="username", required = true ) String username,
							@RequestParam(value="uuid", required = true ) String uuid){
		return userService.confirm(uuid, username, modelMap, redirect);
	}



	@RequestMapping(value="/user/reset/{id}", method=RequestMethod.POST)
	public String resetPassword(@ModelAttribute("user") User user,
								ModelMap modelMap,
								RedirectAttributes redirect){
    	return userService.resetPassword(user, modelMap, redirect);
	}

}