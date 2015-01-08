package pl.mczerwi.spdb.autoclust;

public class ComponentIdGenerator {

	private int lastNumber = 0;
	
	public int getNext() {
		return ++lastNumber;
	}
}
