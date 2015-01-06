package pl.mczerwi.spdb.autoclust;

public class ClusterNumberGenerator {

	private int lastNumber = 0;
	
	public int getNext() {
		return ++lastNumber;
	}
}
