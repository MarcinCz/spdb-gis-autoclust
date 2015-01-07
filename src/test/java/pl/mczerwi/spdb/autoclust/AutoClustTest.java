package pl.mczerwi.spdb.autoclust;

import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import pl.mczerwi.spdb.dao.PointsDAO;
import pl.mczerwi.spdb.model.Point;

public class AutoClustTest {
	
	private PointsDAO pointsDAO = mock(PointsDAO.class);
	private DelaunayGraphProvider provider = new DelaunayGraphProvider(pointsDAO);
	private AutoClust testee = new AutoClust(pointsDAO, provider, new ClusterNumberGenerator());
	
	@Test
	public void shouldClusterPoints() {
		when(pointsDAO.getDelaunayTriangulationPolygonPoints()).thenReturn(DelaunayTestData.getPolygonPoints());
		doAnswer(new Answer<Void>() {

			public Void answer(InvocationOnMock invocation) throws Throwable {
				Set<Point> points = invocation.getArgumentAt(0, Set.class);
				for(Point point: points) {
					System.out.println(point);
				}
				return null;
			}
		}).when(pointsDAO).updatePoints(anySet());
		
		testee.run();
	}
}
