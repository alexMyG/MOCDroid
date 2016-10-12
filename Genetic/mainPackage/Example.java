package mainPackage;
import java.util.ArrayList;

public class Example {

	public Example(int label, String [] importsArray) {
		super();
		this.label = label;
		this.importsArray = importsArray;
	}
	private int label;
	
	private String [] importsArray;
	
	public String [] getImportsArray() {
		return importsArray;
	}
	public void setImportsArray(String [] importsArray) {
		this.importsArray = importsArray;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	
	
	
	
}
