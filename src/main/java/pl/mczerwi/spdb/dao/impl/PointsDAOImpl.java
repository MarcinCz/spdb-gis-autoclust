package pl.mczerwi.spdb.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Point;

public class PointsDAOImpl implements PointsDAO {

	private JdbcTemplate jdbcTemplate;
	private final static String TABLE_NAME = "AfricaSchools2";
	private final static String ID_COLUMN = "gid";
	private final static String GEOM_COLUMN = "the_geom";
	private final static String CLUSTER_COLUMN = "cluster";
	private final static String X_ALIAS = "x";
	private final static String Y_ALIAS = "y";

	private final static String SELECT_DELAUNAY_POLYGONS = 
			String.format("select sA.%1$s, ST_X(sA.%2$s) as %3$s, ST_Y(sA.%2$s) as %4$s " +
			 			"from \"%5$s\" Sa, (select (ST_DumpPoints(sB.geom)).geom " +
			 				"from (SELECT (ST_Dump(ST_DelaunayTriangles(ST_Collect(%2$s)))).geom " +
			 					"from \"%5$s\") as sB) as sC " +
			 			"where ST_Equals(sA.%2$s, sC.geom)",
			 			ID_COLUMN,//1
			 			GEOM_COLUMN,//2
			 			X_ALIAS,//3
						Y_ALIAS,//4
						TABLE_NAME);//5;
	
	private final static String UPDATE_POINTS = String.format("UPDATE \"%1$s\" SET %2$s=? WHERE %3$s=?",
																TABLE_NAME,//1
																CLUSTER_COLUMN,//2
																ID_COLUMN);//3
																
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	public List<Point> getDelaunayTriangulationPolygonPoints() {
		return jdbcTemplate.query(SELECT_DELAUNAY_POLYGONS, new RowMapper<Point>() {

			public Point mapRow(ResultSet rs, int rowNumber) throws SQLException {
				Point point = new Point();
				point.setId(rs.getInt(1));
				point.setX(rs.getDouble(2));
				point.setY(rs.getDouble(3));
				return point;
			}
		});
	}

	public void updatePoints(final Set<Point> points) {
		final Iterator<Point> iterator = points.iterator();
		jdbcTemplate.batchUpdate(UPDATE_POINTS, new BatchPreparedStatementSetter() {
			
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Point point = iterator.next();
				ps.setInt(1, point.getCluster());
				ps.setInt(2, point.getId());
			}
			
			public int getBatchSize() {
				return points.size();
			}
		});
	}

}
