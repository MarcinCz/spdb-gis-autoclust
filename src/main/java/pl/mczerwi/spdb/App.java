package pl.mczerwi.spdb;

import pl.mczerwi.spdb.autoclust.AutoClust;
import pl.mczerwi.spdb.autoclust.ClusterSaver;
import pl.mczerwi.spdb.autoclust.DelaunayGraphProvider;
import pl.mczerwi.spdb.helper.BeanHelper;
import pl.mczerwi.spdb.model.Graph;

/**
 * App main class
 */
public class App 
{
    public static void main( String[] args )
    {
    	DelaunayGraphProvider graphProvider = BeanHelper.getInstance().getInitializedBean(new DelaunayGraphProvider());
    	AutoClust autoClust = new AutoClust();
    	Graph graph = graphProvider.getDelaunayGraph();
    	autoClust.run(graph);
    	ClusterSaver clusterSaver = BeanHelper.getInstance().getInitializedBean(new ClusterSaver());
    	clusterSaver.saveForGraph(graph);
    }
}
