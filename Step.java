package barGraph;

public class Step {

	  // constants
	  public static final int COMPARE = 0;
	  public static final int SWAP = 1;
	  public static final int STORETOBUFFER = 2;
	  public static final int SAVEFROMBUFFER = 3;
	  
	  // fields
	  private int index1;
	  private int index2;
	  private int type;
	  
	  public Step(int index1, int index2, int type)
	  {
	    this.index1 = index1;
	    this.index2 = index2;
	    this.type = type;
	  }
	  
	  // getter methods
	  public int getIndex1() { return index1; }
	  public int getIndex2() { return index2; }
	  public int getType() { return type; }
	}