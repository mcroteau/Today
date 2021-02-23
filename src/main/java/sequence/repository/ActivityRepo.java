package sequence.repository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sequence.model.Activity;

import java.util.List;

@Repository
public class ActivityRepo {

    private static final Logger log = Logger.getLogger(ActivityRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from activities";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from activities";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Activity get(long id){
        String sql = "select * from activities where id = ?";
        Activity activity = jdbcTemplate.queryForObject(sql, new Object[]{ id },
                new BeanPropertyRowMapper<>(Activity.class));
        return activity;
    }

    public List<Activity> getList(){
        String sql = "select * from activities";
        List<Activity> activities = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Activity.class));
        return activities;
    }

    public Activity save(Activity activity){
        String sql = "insert into activities (name) values (?)";
        jdbcTemplate.update(sql, new Object[] {
            activity.getName()
        });

        Long id = getId();
        Activity savedActivity = get(id);
        return savedActivity;
    }

    public boolean update(Activity activity) {
        String sql = "update activities set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
            activity.getName(), activity.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from activities where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

}
