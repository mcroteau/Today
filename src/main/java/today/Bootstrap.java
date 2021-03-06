package today;

import org.springframework.core.env.Environment;
import today.access.TodayAccessor;
import today.common.Today;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import today.access.TodayAccessor;
import today.model.*;
import today.common.Constants;
import org.springframework.stereotype.Component;
import today.repository.*;
import xyz.strongperched.Parakeet;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Bootstrap {

	private static final Logger log = Logger.getLogger(Bootstrap.class);

	@Autowired
	TodayAccessor accessor;

	@Autowired
	UserRepo userRepo;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	StatusRepo statusRepo;

	@Autowired
	ActivityRepo activityRepo;

	@Autowired
	ProspectRepo prospectRepo;

	@Autowired
	EffortRepo effortRepo;

	@Autowired
	Environment env;


	@PostConstruct
	public void startup() {
		Parakeet.perch(accessor);

		Role superRole = roleRepo.find(Constants.SUPER_ROLE);
		Role userRole = roleRepo.find(Constants.USER_ROLE);

		if(superRole == null){
			superRole = new Role();
			superRole.setName(Constants.SUPER_ROLE);
			roleRepo.save(superRole);
		}

		if(userRole == null){
			userRole = new Role();
			userRole.setName(Constants.USER_ROLE);
			roleRepo.save(userRole);
		}

		User existing = userRepo.getByUsername(Constants.SUPER_USERNAME);
		String password = Parakeet.dirty(Constants.SUPER_PASSWORD);

		if(existing == null){
			User superUser = new User();
			superUser.setUsername(Constants.SUPER_USERNAME);
			superUser.setPassword(password);
			userRepo.saveAdministrator(superUser);
		}


		String[] names = { Constants.IDLE,
				           Constants.PROSPECT,
				           Constants.WORKING,
				           Constants.CUSTOMER };

		for(String name: names){
			Status status = new Status();
			status.setName(name);
			statusRepo.save(status);
		}

		log.info("Users : " + userRepo.getCount());
		log.info("Roles : " + roleRepo.count());
		log.info("Statuses : " + statusRepo.getCount());

		if(Today.isDevEnv(env)){
			createMocks();
		}
	}

	private boolean createMocks(){

		User mockUser = userRepo.getByUsername(Constants.MOCK_USERNAME);
		String password = Parakeet.dirty(Constants.SUPER_PASSWORD);

		if(mockUser == null){
			User user = new User();
			user.setUsername(Constants.MOCK_USERNAME);
			user.setPassword(password);
			mockUser = userRepo.save(user);
		}

		String[] prospectNames = {  "Blue Water Trucking Co.",
									"Love Hour Meditation",
									"Jeff's Silly Suds Brew House",
									"Dr. Suese's Chiropractor's Masseuse",
									"Tidy Tim's Bean Factory",
									"Grand Rapids Auto Park",
									"Dirken's Fluffanutters" };

		List<Status> statuses = statusRepo.getList();
		for(String name: prospectNames){
			Status status = statuses.get(Today.getNumber(statuses.size()));
			Prospect prospect = new Prospect();
			prospect.setName(name);
			prospect.setEmail(Today.getString(13));
			prospect.setPhone(Integer.toString(Today.getNumber(10)));
			prospect.setStatusId(status.getId());
			prospectRepo.save(prospect);
		}
		log.info("Prospects : " + prospectRepo.getCount());


		String[] activityNames = {  "Call",
				"Email",
				"Meeting",
		};
		for(String name : activityNames){
			Activity activity = new Activity();
			activity.setName(name);
			activityRepo.save(activity);
		}
		log.info("Activities : " + activityRepo.getCount());

		List<Activity> activities = activityRepo.getList();

		List<Prospect> prospects = prospectRepo.getList();
		for(Prospect prospect: prospects){
			int index = Today.getNumber(activityNames.length);
			Activity activity = activities.get(index);
			ProspectActivity prospectActivity = new ProspectActivity();
			prospectActivity.setActivityId(activity.getId());
			prospectActivity.setProspectId(prospect.getId());
			prospectActivity.setUserId(mockUser.getId());
			prospectRepo.saveActivity(prospectActivity);
		}
		log.info("Prospect Activities : " + prospectRepo.getActivityCount());

		Status endingStatus = statuses.get(2);
		for(int z = 0; z < 3; z++){
			Status startingStatus = statuses.get(Today.getNumber(statuses.size()));

			Prospect prospect = prospects.get(Today.getNumber(prospectNames.length));
			Effort effort = new Effort();
			effort.setProspectId(prospect.getId());
			effort.setStartingStatusId(startingStatus.getId());
			effort.setStartDate(Today.getYesterday(Today.getNumber(31)));
			Effort savedEffort = effortRepo.save(effort);

			for(int k = 0; k < 7; k++){
				int index = Today.getNumber(activityNames.length);
				Activity activity = activities.get(index);
				ProspectActivity prospectActivity = new ProspectActivity();
				prospectActivity.setEffortId(savedEffort.getId());
				prospectActivity.setActivityId(activity.getId());
				prospectActivity.setProspectId(prospect.getId());
				prospectActivity.setUserId(mockUser.getId());
				prospectActivity.setCompleteDate(Today.getDate());
				if(k % 2 == 0) {
					prospectActivity.setCompleted(true);
				}
				prospectRepo.saveActivity(prospectActivity);
			}

			savedEffort.setEndingStatusId(endingStatus.getId());
			savedEffort.setSuccess(true);
			savedEffort.setFinished(true);
			savedEffort.setEndDate(Today.getDate());
			effortRepo.update(savedEffort);

			log.info("Efforts : " + effortRepo.getCount());
			log.info("Effort Activities : " + effortRepo.getActivityCount());
		}

		return true;
	}

}