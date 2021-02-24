package sequence.repository;

import sequence.common.Constants;
import sequence.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserRepo {

	private static final Logger log = Logger.getLogger(UserRepo.class);

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public long getId() {
		String sql = "select max(id) from users";
		long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
		return id;
	}

	public long getCount() {
		String sql = "select count(*) from users";
		long count = jdbcTemplate.queryForObject(sql, new Object[] { }, Long.class);
	 	return count; 
	}

	public User get(long id) {
		String sql = "select * from users where id = ?";
		
		User user = jdbcTemplate.queryForObject(sql, new Object[] { id },
				new BeanPropertyRowMapper<>(User.class));
		
		if(user == null) user = new User();

		return user;
	}

	public User getByUsername(String username) {
		User user = null;
		try{
			String sql = "select * from users where username = ?";
			user = jdbcTemplate.queryForObject(sql, new Object[] { username },
				new BeanPropertyRowMapper<User>(User.class));

		}catch(EmptyResultDataAccessException e){
			//TODO:
		}
		return user;
	}
	
	public List<User> findAll() {
		String sql = "select * from users";
		List<User> people = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<User>(User.class));
		return people;
	}

	public User save(User user) {
		String sql = "insert into users (username, password, date_created) values (?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {
				user.getUsername(), user.getPassword(), user.getDateCreated()
		});

		long id = getId();
		User savedUser = get(id);

		checkSaveDefaultUserRole(id);
		checkSaveDefaultUserPermission(id);

		return savedUser;
	}

	public User saveAdministrator(User user) {
		String sql = "insert into users (username, password) values (?, ?)";
		jdbcTemplate.update(sql, new Object[] {
				user.getUsername(), user.getPassword()
		});

		long id = getId();
		User savedUser = get(id);

		checkSaveAdministratorRole(id);
		checkSaveDefaultUserPermission(id);

		return savedUser;
	}

	public boolean updateUuid(User user) {
		String sql = "update users set uuid = ? where id = ?";
		jdbcTemplate.update(sql, new Object[] {
				user.getUuid(), user.getId()
		});
		return true;
	}
	
	public boolean updatePassword(User user) {
		String sql = "update users set password = ? where id = ?";
		jdbcTemplate.update(sql, new Object[] { 
			user.getPassword(), user.getId()
		});
		return true;
	}

	public User getByUsernameAndUuid(String username, String uuid){
		User user = null;
		try{
			String sql = "select * from users where username = '" + username + "' and uuid = '" + uuid + "'";
			user = jdbcTemplate.queryForObject(sql, new Object[] {},
					new BeanPropertyRowMapper<User>(User.class));

		}catch(EmptyResultDataAccessException e){}

		return user;
	}

	public User getBySubscription(String subscriptionId){
		User user = null;
		try{
			String sql = "select * from users where stripe_subscription_id = ?";
			user = jdbcTemplate.queryForObject(sql, new Object[] {subscriptionId},
					new BeanPropertyRowMapper<>(User.class));

		}catch(EmptyResultDataAccessException e){}

		return user;
	}
	
	public boolean delete(long id) {
		String sql = "delete from users where id = ?";
		jdbcTemplate.update(sql, new Object[] {id });
		return true;
	}

	public String getUserPassword(String username) {
		User user = getByUsername(username);
		return user.getPassword();
	}

	public boolean checkSaveAdministratorRole(long accountId){
		Role role = roleRepo.find(Constants.ADMIN_ROLE);
		UserRole existing = getUserRole(accountId, role.getId());
		if(existing == null){
			saveUserRole(accountId, role.getId());
		}
		return true;
	}

	public boolean checkSaveDefaultUserRole(long accountId){
		Role role = roleRepo.find(Constants.USER_ROLE);
		UserRole existing = getUserRole(accountId, role.getId());
		if(existing == null){
			saveUserRole(accountId, role.getId());
		}
		return true;
	}

	public UserRole getUserRole(long accountId, long roleId){
		String sql = "select * from user_roles where user_id = ? and role_id = ?";
		try {
			UserRole  accountRole = jdbcTemplate.queryForObject(sql, new Object[]{accountId, roleId},
					new BeanPropertyRowMapper<UserRole>(UserRole.class));
			return accountRole;
		}catch(Exception e){
			return null;
		}
	}

	public boolean checkSaveDefaultUserPermission(long accountId){
		String permission = Constants.USER_MAINTENANCE + accountId;
		UserPermission existing = getUserPermission(accountId, permission);
		if(existing == null){
			savePermission(accountId, permission);
		}
		return true;
	}

	public UserPermission getUserPermission(long accountId, String permission){
		String sql = "select * from user_permissions where user_id = ? and permission = ?";
		try {
			UserPermission  accountPermission = jdbcTemplate.queryForObject(sql, new Object[]{accountId, permission},
					new BeanPropertyRowMapper<UserPermission>(UserPermission.class));
			return accountPermission;
		}catch(Exception e){
			return null;
		}
	}

	public boolean saveUserRole(long accountId, long roleId){
		String sql = "insert into user_roles (role_id, user_id) values (?, ?)";
		jdbcTemplate.update(sql, new Object[] { 
			roleId, accountId
		});
		return true;
	}
	
	public boolean savePermission(long accountId, String permission){
		String sql = "insert into user_permissions (user_id, permission) values (?, ?)";
		jdbcTemplate.update(sql, new Object[] { 
			accountId, permission
		});
		return true;
	}
	
	public boolean deleteUserRoles(long accountId){
		String sql = "delete from user_roles where user_id = ?";
		jdbcTemplate.update(sql, new Object[] { accountId });
		return true;
	}
	
	public boolean deleteUserPermissions(long accountId){
		String sql = "delete from user_permissions where user_id = ?";
		jdbcTemplate.update(sql, new Object[] { accountId });
		return true;
	}

	public Set<String> getUserRoles(long id) {
		String sql = "select r.name from user_roles ur, roles r where ur.role_id = r.id and ur.user_id = " + id;
		List<String> rolesList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> roles = new HashSet<String>(rolesList);
		return roles;
	}
	
	public Set<String> getUserRoles(String username) {
		User user = getByUsername(username);
		String sql = "select r.name from user_roles ur, roles r where ur.role_id = r.id and ur.user_id = " + user.getId();
		List<String> rolesList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> roles = new HashSet<String>(rolesList);
		return roles;
	}

	public Set<String> getUserPermissions(long id) {
		String sql = "select permission from user_permissions where user_id = " + id;
		List<String> rolesList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> roles = new HashSet<String>(rolesList);
		return roles;
	}

	public Set<String> getUserPermissions(String username) {
		User user = getByUsername(username);
		String sql = "select permission from user_permissions where user_id = " + user.getId();
		List<String> permissionsList = jdbcTemplate.queryForList(sql, String.class);
		Set<String> permissions = new HashSet<String>(permissionsList);
		return permissions;
	}

    public boolean update(User user) {
		String sql = "update users set username = ?, password = ?, stripe_user_id = ? where id = ?";
		jdbcTemplate.update(sql, new Object[] { user.getUsername(), user.getPassword(), user.getStripeUserId(), user.getId() });
		return true;
    }
}