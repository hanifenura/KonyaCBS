package org.example.geoback.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class LocationRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, String> findLocationInfo(double lat, double lng) {
        String sql = "SELECT m.adi_numara AS mahalle, i.adi_numara AS ilce " +
                "FROM mahalleler m " +
                "JOIN ilceler i ON m.ilceref = i.ilceref " +
                "WHERE ST_Contains(m.geoloc, ST_Transform(ST_SetSRID(ST_MakePoint(?, ?), 4326), 3857)) " +
                "LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{lng, lat}, (rs, rowNum) -> {
                Map<String, String> result = new HashMap<>();
                result.put("mahalle", rs.getString("mahalle"));
                result.put("ilce", rs.getString("ilce"));
                return result;
            });
        } catch (EmptyResultDataAccessException e) {
            Map<String, String> result = new HashMap<>();
            result.put("mahalle", null);
            result.put("ilce", null);
            return result;
        } catch (Exception e) {
            Map<String, String> errorResult = new HashMap<>();
            errorResult.put("mahalle", null);
            errorResult.put("ilce", null);
            return errorResult;
        }

    }
}
