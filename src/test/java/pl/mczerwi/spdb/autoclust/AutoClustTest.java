package pl.mczerwi.spdb.autoclust;

import static org.mockito.Mockito.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class AutoClustTest {
	
	private PointsDAO pointsDAO = mock(PointsDAO.class);
	private DelaunayGraphProvider graphProvider = new DelaunayGraphProvider();
	private ClusterSaver clusterSaver = new ClusterSaver();
	private AutoClust testee = new AutoClust();
	
	@Before
	public void setUp() {
		Logger.getRootLogger().setLevel(Level.TRACE);
	}
	
	@Test
	public void shouldClusterPoints() {
		trainGraphProvider();
		trainClusterSaver();
		
		Graph graph = graphProvider.getDelaunayGraph();
		testee.clusterGraph(graph);
		clusterSaver.saveForGraph(graph);
		
		for(Point point: graph.getPoints()) {
			System.out.println(point);
		}
	}

	@SuppressWarnings("unchecked")
	private void trainClusterSaver() {
		clusterSaver.setPointsDAO(pointsDAO);
		doNothing().when(pointsDAO).updatePoints(anySet());
	}

	private void trainGraphProvider() {
		graphProvider.setPointsDao(pointsDAO);
		when(pointsDAO.getDelaunayTriangulationPolygonPoints()).thenReturn(DelaunayTestData.getPolygonPoints());
	}
}
