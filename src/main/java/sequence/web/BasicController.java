package sequence.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sequence.service.BasicService;


@Controller
public class BasicController {

	private static final Logger log = Logger.getLogger(BasicController.class);

	@Autowired
	BasicService basicService;

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(ModelMap modelMap){
		return basicService.index(modelMap);
	}

	@RequestMapping(value="/signin", method=RequestMethod.GET)
	public String signin(){
		return basicService.showSignin();
	}

	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public String signup(){
		return basicService.showSignup();
	}

	@RequestMapping(value="/unauthorized", method=RequestMethod.GET)
	public String unauthorized(){
		return "basic/401";
	}

}