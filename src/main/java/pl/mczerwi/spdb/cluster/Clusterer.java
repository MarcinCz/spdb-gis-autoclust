package pl.mczerwi.spdb.cluster;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.helper.BeanHelper;
import pl.mczerwi.spdb.model.Point;

public class Clusterer {
	
	@Autowired
	private PointsDAO pointsDAO;
	
	public static Clusterer getInstance() {
		Clusterer clust = new Clusterer();
		BeanHelper.getInstance().initBean(clust);
		return clust;
	}
	
	public void cluster() {
		List<Point> points = pointsDAO.getPoints();
		points = points;
	}
}
