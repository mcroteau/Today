package sequence.service;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sequence.common.Constants;
import sequence.model.User;
import sequence.repository.UserRepo;
import xyz.strongperched.Parakeet;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Service
public class AuthService {

    private static final Logger log = Logger.getLogger(AuthService.class);

    Gson gson = new Gson();

    @Autowired
    private UserRepo userRepo;

    public boolean signin(String username, String password){
        return Parakeet.login(username, password);
    }

    public boolean signout(){
        return Parakeet.logout();
    }

    public boolean isAuthenticated(){
        return Parakeet.isAuthenticated();
    }

    public boolean isAdministrator(){
        return Parakeet.hasRole(Constants.ADMIN_ROLE);
    }

    public boolean hasPermission(String permission){
        return Parakeet.hasPermission(permission);
    }

    public boolean hasRole(String role){
        return Parakeet.hasRole(role);
    }

    public User getUser(){
        String username = Parakeet.getUser();
        User user = userRepo.getByUsername(username);
        return user;
    }

    public String authenticate(User user, RedirectAttributes redirect, HttpServletRequest request) {

        try{
            if(user == null) {
                String payload = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
                user = gson.fromJson(payload, User.class);
            }

            if(!signin(user.getUsername(), user.getPassword())){
                redirect.addFlashAttribute("message", "Wrong username and password");
                return "redirect:/";
            }

            User authdUser = userRepo.getByUsername(user.getUsername());

            request.getSession().setAttribute("username", authdUser.getUsername());
            request.getSession().setAttribute("userId", authdUser.getId());

        } catch ( Exception e ) {
            e.printStackTrace();
            redirect.addFlashAttribute("message", "Please yell at one of us, something is a little off.");
            return "redirect:/";
        }

        return "redirect:/";
    }

    public String deAuthenticate(RedirectAttributes redirect, HttpServletRequest request) {
        signout();
        redirect.addFlashAttribute("message", "Successfully signed out");
        request.getSession().setAttribute("username", "");
        request.getSession().setAttribute("userId", "");
        return "redirect:/";
    }
}
