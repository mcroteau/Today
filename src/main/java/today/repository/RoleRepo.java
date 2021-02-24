package sequence.repository;

import sequence.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepo {
	
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	

	public int count() {
		String sql = "select count(*) from roles";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { }, Integer.class);
	 	return count; 
	}


	public Role get(int id) {
		String sql = "select * from roles where id = ?";
		Role role = jdbcTemplate.queryForObject(sql, new Object[] { id }, 
				new BeanPropertyRowMapper<Role>(Role.class));
		return role;
	}

	
	public Role find(String name) {
		Role role = null; 
		try{
			String sql = "select * from roles where name = '" + name + "'";
			role = jdbcTemplate.queryForObject(sql, new Object[] {}, 
				new BeanPropertyRowMapper<Role>(Role.class));
		}catch(EmptyResultDataAccessException e){
			
		}
		return role;
	}
	
	
	public List<Role> findAll() {
		String sql = "select * from roles";
		List<Role> roles = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class));
		return roles;
	}
	
	
	public void save(Role role) {
		String sql = "insert into roles (name) values(?)";
		jdbcTemplate.update(sql, new Object[] { 
			role.getName()  
		});
	}
	
}