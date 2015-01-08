package pl.mczerwi.spdb.autoclust;

import static org.mockito.Mockito.*;

import org.junit.Test;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Graph;
import pl.mczerwi.spdb.model.Point;

public class AutoClustTest {
	
	private PointsDAO pointsDAO = mock(PointsDAO.class);
	private DelaunayGraphProvider graphProvider = new DelaunayGraphProvider();
	private AutoClust testee = new AutoClust();
	
	@Test
	public void shouldClusterPoints() {
		trainGraphProvider();
		
		Graph graph = graphProvider.getDelaunayGraph();
		testee.clusterGraph(graph);
		
		for(Point point: graph.getPoints()) {
			System.out.println(point);
		}
	}

	private void trainGraphProvider() {
		graphProvider.setPointsDao(pointsDAO);
		when(pointsDAO.getDelaunayTriangulationPolygonPoints()).thenReturn(DelaunayTestData.getPolygonPoints());
	}
}
