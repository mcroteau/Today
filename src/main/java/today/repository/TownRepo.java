package sequence.repository;

import sequence.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TownRepo {

    private static final Logger log = Logger.getLogger(TownRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from towns";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from towns";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Town get(long id){
        String sql = "select * from towns where id = ?";
        Town town = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(Town.class));
        return town;
    }


    public List<Town> getList(){
        String sql = "select * from towns";
        List<Town> towns = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Town.class));
        return towns;
    }


    public Town save(Town town){
        String sql = "insert into towns (name, count, town_uri) values (?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                town.getName(), town.getCount(), town.getTownUri()
        });

        Long id = getId();
        Town savedTown = get(id);
        return savedTown;
    }

    public boolean update(Town town) {
        String sql = "update towns set name = ?, town_uri = ?, count = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                town.getName(), town.getTownUri(), town.getCount(), town.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from towns where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public Town get(String uri) {
        String sql = "select * from towns where town_uri = ?";
        Town town = jdbcTemplate.queryForObject(sql, new Object[] { uri },
                new BeanPropertyRowMapper<>(Town.class));
        return town;
    }
}
