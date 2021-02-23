package sequence;

import sequence.common.Sequence;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import sequence.access.SequenceAccessor;
import sequence.model.*;
import sequence.common.Constants;
import org.springframework.stereotype.Component;
import sequence.repository.ProspectRepo;
import sequence.repository.RoleRepo;
import sequence.repository.StatusRepo;
import sequence.repository.UserRepo;
import xyz.strongperched.Parakeet;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class AppRunner {

	private static final Logger log = Logger.getLogger(AppRunner.class);

	@Autowired
	SequenceAccessor accessor;

	@Autowired
	UserRepo userRepo;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	ProspectRepo prospectRepo;

	@Autowired
	StatusRepo statusRepo;


	@PostConstruct
	public void init() {
		Parakeet.perch(accessor);
		createRoles();
		createAdministrator();
		createGuest();
		createStatuses();
		createMocks();
	}

	private boolean createStatuses(){
		String[] names = { "Prospect",
				              "Working",
							  "Customer" };

		for(String name: names){
			Status status = new Status();
			status.setName(name);
			statusRepo.save(status);
		}

		log.info("Statuses : " + statusRepo.getCount());
		return true;
	}

	private boolean createMocks(){
		String[] names = { "Blue Water Trucking Co.",
							"Lovely Hour Meditation",
							"Derbin's Fluffanutters" };

		List<Status> statuses = statusRepo.getList();
		for(String name: names){
			Status status = statuses.get(Sequence.getNumber(statuses.size() - 1));
			Prospect prospect = new Prospect();
			prospect.setName(name);
			prospect.setEmail(Sequence.getString(13));
			prospect.setPhone(Integer.toString(Sequence.getNumber(10)));
			prospect.setStatusId(status.getId());
			prospectRepo.save(prospect);
		}

		log.info("Prospects : " + prospectRepo.getCount());
		return true;
	}


	private void createRoles(){
		Role adminRole = roleRepo.find(Constants.ADMIN_ROLE);
		Role userRole = roleRepo.find(Constants.USER_ROLE);

		if(adminRole == null){
			adminRole = new Role();
			adminRole.setName(Constants.ADMIN_ROLE);
			roleRepo.save(adminRole);
		}

		if(userRole == null){
			userRole = new Role();
			userRole.setName(Constants.USER_ROLE);
			roleRepo.save(userRole);
		}

		log.info("Roles : " + roleRepo.count());
	}

	
	private void createAdministrator(){
		
		try{
			User existing = userRepo.getByUsername(Constants.ADMIN_USERNAME);
			String password = Parakeet.dirty(Constants.PASSWORD);

			if(existing == null){
				User admin = new User();
				admin.setUsername(Constants.ADMIN_USERNAME);
				admin.setPassword(password);
				userRepo.saveAdministrator(admin);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}


	private void createGuest(){
		User existing = userRepo.getByUsername(Constants.GUEST_USERNAME);
		String password = Parakeet.dirty(Constants.GUEST_PASSWORD);

		if(existing == null){
			User user = new User();
			user.setUsername(Constants.GUEST_USERNAME);
			user.setPassword(password);
			userRepo.save(user);
		}
		log.info("Users : " + userRepo.getCount());
	}

}