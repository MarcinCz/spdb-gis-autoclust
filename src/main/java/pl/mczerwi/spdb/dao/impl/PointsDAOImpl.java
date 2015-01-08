package pl.mczerwi.spdb.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Point;

public class PointsDAOImpl implements PointsDAO {

	private final static String X_ALIAS = "x";
	private final static String Y_ALIAS = "y";

	private JdbcTemplate jdbcTemplate;

	@Value("#{configProperties['points.tableName']}")
	private String tableName;
	
	@Value("#{configProperties['points.idColumn']}")
	private String idColumn;
	
	@Value("#{configProperties['points.geomColumn']}")
	private String geomColumn;
	
	@Value("#{configProperties['points.clusterColumn']}")
	private String clusterColumn;

																
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	public List<Point> getDelaunayTriangulationPolygonPoints() {
		return jdbcTemplate.query(getSelectDelaunayPolygonQuery(), new RowMapper<Point>() {

			public Point mapRow(ResultSet rs, int rowNumber) throws SQLException {
				Point point = new Point();
				point.setId(rs.getInt(1));
				point.setX(rs.getDouble(2) * 100);
				point.setY(rs.getDouble(3) * 100);
				return point;
			}
		});
	}

	public void updatePoints(final Set<Point> points) {
		final Iterator<Point> iterator = points.iterator();
		jdbcTemplate.batchUpdate(getUpdatePointsQuery(), new BatchPreparedStatementSetter() {
			
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
	
	private String getSelectDelaunayPolygonQuery() {
		return String.format("select sA.%1$s, ST_X(sA.%2$s) as %3$s, ST_Y(sA.%2$s) as %4$s " +
	 			"from \"%5$s\" Sa, (select (ST_DumpPoints(sB.geom)).geom " +
 				"from (SELECT (ST_Dump(ST_DelaunayTriangles(ST_Collect(%2$s)))).geom " +
 					"from \"%5$s\") as sB) as sC " +
 				"where ST_Equals(sA.%2$s, sC.geom)",
 				idColumn,//1
 				geomColumn,//2
 				X_ALIAS,//3
 				Y_ALIAS,//4
 				tableName);//5;
	}
	
	private String getUpdatePointsQuery() {
		return String.format("UPDATE \"%1$s\" SET %2$s=? WHERE %3$s=?",
				tableName,//1
				clusterColumn,//2
				idColumn);//3
	}

}
