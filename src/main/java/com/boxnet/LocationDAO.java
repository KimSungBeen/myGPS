package com.boxnet;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LocationDAO {
    final JdbcTemplate jt;

    /**
     * Column Number
     */
    final static int _ID = 1;
    final static int _DATE = 2;
    final static int _LATITUDE = 3;
    final static int _LONGITUDE = 4;

    public LocationDAO(JdbcTemplate jt) {
        this.jt = jt;
    }

    /**
     * Location 데이터 Select
     */
    public List<LocationDTO> selectLocationData(String id) {

        return jt.query("select * from location where id = ?", (rs, rowNum) -> {
            System.out.println("# Select " + id + " Data #");
            LocationDTO locationDTO = new LocationDTO();

            locationDTO.setId(rs.getString(_ID));
            locationDTO.setDate(rs.getString(_DATE));
            locationDTO.setLatitude(rs.getDouble(_LATITUDE));
            locationDTO.setLongitude(rs.getDouble(_LONGITUDE));

            return locationDTO;
        }, id);
    }

    /**
     * Location 데이터 Insert
     */
    public void insertLocationDataList(List<LocationDTO> locationDTOlist) {
        if (locationDTOlist.size() > 0) {
            // 파라미터로 받은 리스트의 사이즈가 0보다 많일 때만 실행
            System.out.println("# Insert " + locationDTOlist.get(0).getId() + " Data #");

            jt.batchUpdate("insert into location (id, insertDate, latitude, longitude) values (?, ?, ?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            LocationDTO locationDTO = locationDTOlist.get(i);
                            ps.setString(1, locationDTO.getId());
                            ps.setString(2, locationDTO.getDate());
                            ps.setDouble(3, locationDTO.getLatitude());
                            ps.setDouble(4, locationDTO.getLongitude());
                        }

                        @Override
                        public int getBatchSize() {
                            return locationDTOlist.size();
                        }
                    });
        }
    }

}
