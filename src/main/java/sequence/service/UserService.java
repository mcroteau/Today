package sequence.service;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.model.Price;
import sequence.common.Sequence;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sequence.common.Constants;
import sequence.model.*;
import sequence.repository.*;
import xyz.strongperched.Parakeet;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

@Service
public class UserService {

    private static final Logger log = Logger.getLogger(UserService.class);

    Gson gson = new Gson();

    @Autowired
    AuthService authService;

    @Autowired
    PhoneService phoneService;

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    ProspectRepo prospectRepo;

    @Autowired
    private Environment env;

    private String getPermission(String id){
        return Constants.USER_MAINTENANCE + id;
    }


    public String getUsers(ModelMap modelMap){
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(!authService.isAdministrator()){
            return Constants.UNAUTHORIZED_REDIRECT;
        }

        List<User> people = userRepo.findAll();
        modelMap.addAttribute("users", people);

        return "user/index";
    }

    public String getEditUser(Long id, ModelMap modelMap){
        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        User user = userRepo.get(id);
        modelMap.put("user", user);

        return "user/edit";
    }



    public String editPassword(Long id, ModelMap modelMap) {

        String permission = getPermission(Long.toString(id));
        if(!authService.isAdministrator() ||
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        User user = userRepo.get(id);
        modelMap.addAttribute("user", user);
        return "user/password";
    }


    public String updatePassword(User user, ModelMap modelMap, RedirectAttributes redirect) {

        String permission = getPermission(Long.toString(user.getId()));
        if(!authService.isAdministrator() &&
                !authService.hasPermission(permission)){
            return "redirect:/";
        }

        if(user.getPassword().length() < 7){
            redirect.addFlashAttribute("error", "Passwords must be at least 7 characters long.");
            return "redirect:/signup";
        }

        if(!user.getPassword().equals("")){
            user.setPassword(xyz.strongperched.Parakeet.dirty(user.getPassword()));
            userRepo.updatePassword(user);
        }

        redirect.addFlashAttribute("message", "password successfully updated");
        Long id = authService.getUser().getId();
        return "redirect:/user/edit_password/" + id;

    }

    public String deleteUser(Long id, ModelMap modelMap, RedirectAttributes redirect) {
        if(!authService.isAdministrator()){
            redirect.addFlashAttribute("message", "You don't have permission");
            return Constants.UNAUTHORIZED_REDIRECT;
        }

        redirect.addFlashAttribute("message", "Successfully deleted user");
        return "redirect:/admin/users";
    }

    public String register(String reCaptchaResponse, User user, HttpServletRequest req, RedirectAttributes redirect) {

        if(user == null){
            redirect.addFlashAttribute("message", "a error on our end, please give it another go.");
            return "redirect:/signup";
        }

        if(!Sequence.isValidMailbox(user.getUsername())){
            redirect.addFlashAttribute("message", "Username must be a valid email.");
            return "redirect:/signup";
        }

        User existingUser = userRepo.getByUsername(user.getUsername());
        if(existingUser != null){
            redirect.addFlashAttribute("message", "User exists with same username.");
            return "redirect:/signup";
        }

        if(user.getPassword().equals("")) {
            redirect.addFlashAttribute("message", "Password cannot be blank");
            return "redirect:/signup";
        }

        if(user.getPassword().length() < 7){
            redirect.addFlashAttribute("message", "Password must be at least 7 characters long.");
            return "redirect:/signup";
        }

        String password = user.getPassword();
        String passwordHashed = Parakeet.dirty(user.getPassword());

        try{

            user.setPassword(passwordHashed);
            userRepo.save(user);

            User savedUser = userRepo.getByUsername(user.getUsername());
            Role defaultRole = roleRepo.find(Constants.USER_ROLE);

            userRepo.saveUserRole(savedUser.getId(), defaultRole.getId());
            String permission = getPermission(Long.toString(savedUser.getId()));
            userRepo.savePermission(savedUser.getId(), permission);


        }catch(Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
            redirect.addFlashAttribute("message", "Will you contact us? Email us with the subject, support@amadeus.social. Our programmers missed something. Gracias!");
            return("redirect:/signup");
        }

        log.info(userRepo.getCount() + " " + authService + " " + user.getUsername());
        if(!authService.signin(user.getUsername(), password)) {
            redirect.addFlashAttribute("message", "Thank you for registering. We hope you enjoy!");
            return "redirect:/";
        }

        req.getSession().setAttribute("username", user.getUsername());
        req.getSession().setAttribute("userId", user.getId());

        return "redirect:/";
    }

    public String sendReset(String username, RedirectAttributes redirect, HttpServletRequest req) {

        try {

            User user = userRepo.getByUsername(username);
            if (user == null) {
                redirect.addFlashAttribute("message", "Unable to find user.");
                return ("redirect:/user/reset");
            }

            String resetUuid = Sequence.getString(13);
            user.setUuid(resetUuid);
            userRepo.updateUuid(user);

            StringBuffer url = req.getRequestURL();

            String[] split = url.toString().split(req.getContextPath());
            String httpSection = split[0];

            String resetUrl = httpSection + req.getContextPath() + "/user/confirm?";
            String params = "username=" + URLEncoder.encode(user.getUsername(), "utf-8") + "&uuid=" + resetUuid;
            resetUrl += params;

            String body = "<h1>Sequence</h1>" +
                    "<p>Reset password</p>" +
                    "<p><a href=\"" + resetUrl + "\">" + resetUrl + "</a></p>";

            emailService.send(user.getUsername(), "Reset Password", body);

        }catch(Exception e){
            e.printStackTrace();
        }

        return "user/send";
    }

    public String confirm(String uuid, String username, ModelMap modelMap,RedirectAttributes redirect) {

        User user = userRepo.getByUsernameAndUuid(username, uuid);
        if (user == null) {
            redirect.addFlashAttribute("error", "Unable to locate user.");
            return "redirect:/user/reset";
        }

        modelMap.addAttribute("user", user);
        return "user/confirm";
    }

    public String resetPassword(User user, ModelMap modelMap, RedirectAttributes redirect) {

        if(user.getPassword().length() < 7){
            redirect.addFlashAttribute("user", user);
            redirect.addFlashAttribute("message", "Passwords must be at least 7 characters long.");
            return "redirect:/user/confirm?username=" + user.getUsername() + "&uuid=" + user.getUuid();
        }

        if(!user.getPassword().equals("")){
            String password = xyz.strongperched.Parakeet.dirty(user.getPassword());
            user.setPassword(password);
            userRepo.updatePassword(user);
        }

        redirect.addFlashAttribute("message", "Password successfully updated");
        return "user/success";
    }

}
