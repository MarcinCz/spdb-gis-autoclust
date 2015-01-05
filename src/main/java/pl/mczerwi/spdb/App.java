package pl.mczerwi.spdb;

import pl.mczerwi.spdb.cluster.Clusterer;

/**
 * Hello world!
 *
 */
public class App 
{

	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Clusterer.getInstance().cluster();
    }
}
