package sequence.repository;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sequence.model.Status;

import java.util.List;


@Repository
public class StatusRepo {

    private static final Logger log = Logger.getLogger(StatusRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from statuses";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from statuses";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Status get(long id){
        String sql = "select * from statuses where id = ?";
        Status status = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(Status.class));
        return status;
    }


    public List<Status> getList(){
        String sql = "select * from statuses";
        List<Status> statuses = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Status.class));
        return statuses;
    }

    public Status save(Status status){
        String sql = "insert into statuses (name) values (?)";
        jdbcTemplate.update(sql, new Object[] {
                status.getName()
        });

        Long id = getId();
        Status savedStatus = get(id);
        return savedStatus;
    }

    public boolean update(Status status) {
        String sql = "update statuses set name = ?, where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                status.getName(), status.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from statuses where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

}
