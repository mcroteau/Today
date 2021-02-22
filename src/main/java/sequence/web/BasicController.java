package sequence.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	public String index(){
		return basicService.index();
	}

	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String home(ModelMap modelMap){ return basicService.home(modelMap); }

	@RequestMapping(value="/towns", method=RequestMethod.GET)
	public String towns(ModelMap modelMap){ return basicService.towns(modelMap); }

	@RequestMapping(value="/shelters", method=RequestMethod.GET)
	public String shelters(ModelMap modelMap){ return basicService.shelters(modelMap); }

	@RequestMapping(value="/cost", method=RequestMethod.GET)
	public String cost(){ return "basic/cost"; }

	@RequestMapping(value="/developers", method=RequestMethod.GET)
	public String developers(){ return "basic/developers"; }

	@RequestMapping(value="/about", method=RequestMethod.GET)
	public String about(){ return "basic/about"; }

	@RequestMapping(value="/signin", method=RequestMethod.GET)
	public String signin(){
		return basicService.showSignin();
	}

	@RequestMapping(value="/privacy", method=RequestMethod.GET)
	public String privacy(){
		return "basic/privacy";
	}

	@RequestMapping(value="/issues/report", method=RequestMethod.GET)
	public String beginReport(){
		return basicService.beginReport();
	}

	@RequestMapping(value="/issues/report", method=RequestMethod.POST)
	public String reportIssue(ModelMap modelMap,
							  RedirectAttributes redirect,
							  @RequestParam(value="email", required = true ) String email,
							  @RequestParam(value="issue", required = true ) String issue){
		return basicService.reportIssue(email, issue, modelMap, redirect);
	}

	@RequestMapping(value="/unauthorized", method=RequestMethod.GET)
	public String unauthorized(){
		return "basic/401";
	}

}