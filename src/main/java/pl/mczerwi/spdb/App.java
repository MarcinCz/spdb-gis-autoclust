package pl.mczerwi.spdb;

import pl.mczerwi.spdb.autoclust.AutoClust;
import pl.mczerwi.spdb.autoclust.ClusterSaver;
import pl.mczerwi.spdb.autoclust.DelaunayGraphProvider;
import pl.mczerwi.spdb.model.Graph;

/**
 * App main class
 */
public class App 
{
    public static void main( String[] args )
    {
    	DelaunayGraphProvider graphProvider = DelaunayGraphProvider.getInstance();
    	AutoClust autoClust = new AutoClust();
    	Graph graph = graphProvider.getDelaunayGraph();
    	autoClust.clusterGraph(graph);
    	ClusterSaver clusterSaver = ClusterSaver.getInstance();
    	clusterSaver.saveForGraph(graph);
    }
}
