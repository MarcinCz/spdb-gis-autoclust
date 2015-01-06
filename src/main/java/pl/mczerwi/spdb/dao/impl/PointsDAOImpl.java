package pl.mczerwi.spdb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Point;

public class PointsDAOImpl implements PointsDAO {

	private JdbcTemplate jdbcTemplate;
	private final static String TABLE_NAME = "africaschools";
	private final static String ID_COLUMN = "gid";
	private final static String GEOM_COLUMN = "the_geom";
	private final static String GROUP_COLUMN = "group";
	private final static String X_ALIAS = "x";
	private final static String Y_ALIAS = "y";
	
	private final static String SELECT_POINTS = String.format("SELECT ST_X(the_geom) as %1$s, ST_Y(the_geom) as %2$s FROM \"%3$s\" LIMIT 100",
																X_ALIAS,//1
																Y_ALIAS,//2
																TABLE_NAME);//3
	private final static String SELECT_DELAUNAY_POLYGONS = 
			String.format("select a.%1$s, ST_X(%2$s) as %3$s, ST_Y(%2$s) " +
			 			"from \"%4$s\" a, (select (ST_DumpPoints(tri.geom)).geom as geom " +
			 				"from (SELECT (ST_Dump(ST_DelaunayTriangles(ST_Collect(%2$s)))).geom as geom " +
			 					"from \"%5$s\" limit 10) as tri) as points " +
			 			"where ST_Equals(a.the_geom, points.geom)",
			 			ID_COLUMN,//1
			 			GEOM_COLUMN,//2
			 			X_ALIAS,//3
						Y_ALIAS,//4
						TABLE_NAME);//5;
																
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

	public List<Point> getDelaunayTriangulationPolygonPoints() {
		return jdbcTemplate.query(SELECT_DELAUNAY_POLYGONS, new RowMapper<Point>() {

			public Point mapRow(ResultSet rs, int rowNumber) throws SQLException {
				Point point = new Point();
				point.setId(String.valueOf(rs.getInt(1)));
				point.setX(rs.getDouble(2));
				point.setY(rs.getDouble(3));
				return point;
			}
		});
	}

}
