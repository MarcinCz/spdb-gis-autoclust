package pl.mczerwi.spdb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Point;

public class PointsDAOImpl implements PointsDAO {

	private JdbcTemplate jdbcTemplate;
	private final static String TABLE_NAME = "africaschools";
	private final static String X_ALIAS = "x";
	private final static String Y_ALIAS = "y";
	
	private final static String SELECT_POINTS = String.format("SELECT ST_X(the_geom) as %1$s, ST_Y(the_geom) as %2$s FROM \"%3$s\" LIMIT 100",
																X_ALIAS,//1
																Y_ALIAS,//2
																TABLE_NAME);//3
																
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	public List<Point> getPoints() {
		List<Point> points = jdbcTemplate.query(SELECT_POINTS, new RowMapper<Point>() {

			public Point mapRow(ResultSet rs, int rowNumber) throws SQLException {
				Point point = new Point();
				point.setX(rs.getDouble(1));
				point.setY(rs.getDouble(2));
				return point;
			}
		});
		
		return points;
	}

}
