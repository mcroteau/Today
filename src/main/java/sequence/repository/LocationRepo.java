package sequence.repository;

import sequence.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationRepo {

    private static final Logger log = Logger.getLogger(LocationRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from locations";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from locations";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Location get(long id){
        String sql = "select * from locations where id = ?";
        Location location = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(Location.class));
        return location;
    }


    public List<Location> getList(){
        String sql = "select * from locations";
        List<Location> locations = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Location.class));
        return locations;
    }

    public Location save(Location location){
        String sql = "insert into locations (address, prospect_id) values (?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                location.getAddress(), location.getProspectId()
        });
        Long id = getId();
        Location savedLocation = get(id);
        return savedLocation;
    }

    public boolean delete(long id){
        String sql = "delete from locations where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public List<Location> getList(long id) {
        String sql = "select * from locations where prospect_id = ?";
        List<Location> locations = jdbcTemplate.query(sql, new Object[]{ id }, new BeanPropertyRowMapper<>(Location.class));
        return locations;
    }

    public boolean deleteLocations(Long id) {
        String sql = "delete from locations where prospect_id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean update(Location location) {
        String sql = "update locations set address = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] { location.getAddress(), location.getId() });
        return true;
    }

}
