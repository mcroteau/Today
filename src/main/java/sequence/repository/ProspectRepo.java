package sequence.repository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sequence.model.Prospect;
import sequence.model.ProspectActivity;

import java.util.List;

@Repository
public class ProspectRepo {

    private static final Logger log = Logger.getLogger(ProspectRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from prospects";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from prospects";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Long getCount(Long id) {
        String sql = "select count(*) from prospects where status_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{ id }, Long.class);
        return count;
    }

    public Prospect get(long id){
        String sql = "select * from prospects where id = ?";
        Prospect prospect = jdbcTemplate.queryForObject(sql, new Object[]{ id },
                new BeanPropertyRowMapper<>(Prospect.class));
        return prospect;
    }

    public List<Prospect> getList(){
        String sql = "select * from prospects";
        List<Prospect> prospects = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Prospect.class));
        return prospects;
    }

    public List<Prospect> getList(Long id){
        String sql = "select * from prospects where status_id = ?";
        List<Prospect> prospects = jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Prospect.class));
        return prospects;
    }

    public List<Prospect> getResults(String query) {
        String sql = "select * from prospects where upper(name) like upper('%?%')";

        List<Prospect> prospects = jdbcTemplate.query(sql, new Object[] { query },
                new BeanPropertyRowMapper<>(Prospect.class));
        return prospects;
    }

    public Prospect save(Prospect prospect){
        String sql = "insert into prospects (name, phone, email, status_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
            prospect.getName(), prospect.getPhone(), prospect.getEmail(), prospect.getStatusId()
        });

        Long id = getId();
        Prospect savedProspect = get(id);
        return savedProspect;
    }

    public boolean update(Prospect prospect) {
        String sql = "update prospects set name = ?, phone = ?, email = ?, status_id = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
            prospect.getName(), prospect.getPhone(), prospect.getEmail(), prospect.getStatusId(), prospect.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from prospects where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public ProspectActivity getActivity(long id){
        String sql = "select pa.id, pa.activity_id, pa.effort_id, pa.complete_date, a.name " +
                "from prospect_activities pa inner join activity a on pa.activity_id = a.id " +
                "where pa.id = ?";
        ProspectActivity prospectActivity = jdbcTemplate.queryForObject(sql, new Object[]{ id },
                new BeanPropertyRowMapper<>(ProspectActivity.class));
        return prospectActivity;
    }

    public boolean saveActivity(ProspectActivity prospectActivity) {
        String sql = "insert into prospect_activities (activity_id, prospect_id, effort_id) values (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                prospectActivity.getActivityId(), prospectActivity.getProspectId(), prospectActivity.getEffortId()
        });
        return true;
    }

    public boolean updateActivity(ProspectActivity prospectActivity) {
        String sql = "update prospect_activities set complete_date = ?, completed = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                prospectActivity.getCompleteDate(), prospectActivity.getCompleted(), prospectActivity.getId()
        });
        return true;
    }

    public List<ProspectActivity> getActivities(Long id){
        String sql = "select pa.id, pa.activity_id, pa.effort_id, pa.complete_date, a.name " +
                "from prospect_activities pa inner join activities a on pa.activity_id = a.id " +
                "where pa.id = ? order by pa.complete_date asc";

        List<ProspectActivity> prospectActivities = jdbcTemplate.query(sql, new Object[]{ id }, new BeanPropertyRowMapper<>(ProspectActivity.class));
        return prospectActivities;
    }

    public boolean deleteActivity(Long id) {
        String sql = "delete from prospect_activities where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean deleteActivities(Long id) {
        String sql = "delete from prospect_activities where prospect_id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public Long getActivityCount() {
        String sql = "select count(*) from prospect_activities";
        Long count = jdbcTemplate.queryForObject(sql, new Object[]{ }, Long.class);
        return count;
    }
}
