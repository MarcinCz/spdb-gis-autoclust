package pl.mczerwi.spdb.algorithm.delaunay;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import pl.mczerwi.spdb.algorithm.DelaunayTestData;
import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Graph;

public class DelaunayGraphProviderTest {

	private DelaunayGraphProvider testee = new DelaunayGraphProvider();
	private PointsDAO pointsDAO = mock(PointsDAO.class);
	
	@Before
	public void setUp() {
		testee.setPointsDao(pointsDAO);
	}
	
	@Test
	public void shouldCreateCorrectDelaunayGraph() {
		when(pointsDAO.getDelaunayTriangulationPolygonPoints()).thenReturn(DelaunayTestData.getPolygonPoints());
		
		Graph graph = testee.getDelaunayGraph();
		
		assertEquals(4, graph.getPoints().size());
		assertEquals(2, graph.getEdgesForPoint(DelaunayTestData.P1).size());
		assertEquals(3, graph.getEdgesForPoint(DelaunayTestData.P2).size());
		assertEquals(3, graph.getEdgesForPoint(DelaunayTestData.P3).size());
		assertEquals(2, graph.getEdgesForPoint(DelaunayTestData.P4).size());
	}
}
