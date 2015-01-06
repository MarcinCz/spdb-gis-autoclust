package pl.mczerwi.spdb;

import pl.mczerwi.spdb.autoclust.AutoClust;

/**
 * Hello world!
 *
 */
public class App 
{

	
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        AutoClust.getInstance().run();
    }
}
