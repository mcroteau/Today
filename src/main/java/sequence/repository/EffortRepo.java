package sequence.repository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sequence.model.Effort;
import sequence.model.EffortActivity;

import java.util.List;

@Repository
public class EffortRepo {

    private static final Logger log = Logger.getLogger(EffortRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from efforts";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from efforts";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Long getCount(Long id) {
        String sql = "select count(*) from efforts where status_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{ id }, Long.class);
        return count;
    }

    public Effort get(long id){
        String sql = "select * from efforts where id = ?";
        Effort effort = jdbcTemplate.queryForObject(sql, new Object[]{ id },
                new BeanPropertyRowMapper<>(Effort.class));
        return effort;
    }

    public List<Effort> getList(){
        String sql = "select * from efforts";
        List<Effort> efforts = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Effort.class));
        return efforts;
    }

    public List<Effort> getList(Long id){
        String sql = "select * from efforts where prospect_id = ?";
        List<Effort> efforts = jdbcTemplate.query(sql, new Object[]{ id }, new BeanPropertyRowMapper<>(Effort.class));
        return efforts;
    }

    public Effort save(Effort effort){
        String sql = "insert into efforts (start_date, end_date, prospect_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
            effort.getStartDate(), effort.getEndDate(), effort.getProspectId()
        });

        Long id = getId();
        Effort savedEffort = get(id);
        return savedEffort;
    }

    public boolean update(Effort effort) {
        String sql = "update efforts set end_date = ?, finished = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
            effort.getEndDate(), effort.getFinished()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from efforts where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public EffortActivity getActivity(long id){
        String sql = "select ea.id, ea.activity_id, ea.effort_id, ea.complete_date, a.name " +
                "from effort_activities ea inner join activity a on ea.activity_id = a.id " +
                "where ea.id = ?";
        EffortActivity effortActivity = jdbcTemplate.queryForObject(sql, new Object[]{ id },
                new BeanPropertyRowMapper<>(EffortActivity.class));
        return effortActivity;
    }

    public boolean saveActivity(EffortActivity effortActivity) {
        String sql = "insert into effort_activites (effort_id, activity_id) values (?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                effortActivity.getEffortId(), effortActivity.getActivityId()
        });
        return true;
    }

    public boolean updateActivity(EffortActivity effortActivity) {
        String sql = "update effort_activities set complete_date = ?, completed = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                effortActivity.getCompleteDate(), effortActivity.getCompleted(), effortActivity.getId()
        });
        return true;
    }

    public List<EffortActivity> getActivities(Long id){
        String sql = "select ea.id, ea.activity_id, ea.effort_id, ea.complete_date, a.name " +
                "from effort_activities ea inner join activity a on ea.activity_id = a.id " +
                "where ea.id = ? order by ea.complete_date asc";

        List<EffortActivity> effortActivities = jdbcTemplate.query(sql, new Object[]{ id }, new BeanPropertyRowMapper<>(EffortActivity.class));
        return effortActivities;
    }

    public boolean deleteActivity(Long id) {
        String sql = "delete from effort_activities where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean deleteActivities(Long id) {
        String sql = "delete from effort_activities where effort_id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public Long getActivityCount() {
        String sql = "select count(*) from effort_activities";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{ }, Long.class);
        return count;
    }
}
