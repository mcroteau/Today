package sequence.repository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sequence.model.Effort;
import sequence.model.ProspectActivity;

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
        String sql = "insert into efforts (start_date, end_date, prospect_id, starting_status_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
            effort.getStartDate(), effort.getEndDate(), effort.getProspectId(), effort.getStartingStatusId()
        });

        Long id = getId();
        Effort savedEffort = get(id);
        return savedEffort;
    }

    public boolean update(Effort effort) {
        String sql = "update efforts set end_date = ?, finished = ?, ending_status_id = ?, success = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
            effort.getEndDate(), effort.getFinished(), effort.getEndingStatusId(), effort.getSuccess(), effort.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from efforts where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public List<ProspectActivity> getActivities(Long id){
        String sql = "select pa.id, pa.activity_id, pa.effort_id, pa.complete_date, a.name " +
                "from prospect_activities pa inner join activities a on pa.activity_id = a.id " +
                "where pa.effort_id = ? order by pa.complete_date asc";

        List<ProspectActivity> prospectActivities = jdbcTemplate.query(sql,
                new Long[]{ id },
                new BeanPropertyRowMapper<>(ProspectActivity.class));
        return prospectActivities;
    }

    public boolean deleteActivity(Long id) {
        String sql = "delete from prospect_activities where effort_id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean deleteActivities(Long id) {
        String sql = "delete from prospect_activities where effort_id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public Long getActivityCount() {
        String sql = "select count(*) from prospect_activities where effort_id >= 0";
        Long count = jdbcTemplate.queryForObject(sql, new Long[]{ }, Long.class);
        return count;
    }

    public List<Effort> getSuccesses() {
        String sql = "select * from efforts where success = true";
        List<Effort> efforts = jdbcTemplate.query(sql, new Object[]{ }, new BeanPropertyRowMapper<>(Effort.class));
        return efforts;
    }
}
