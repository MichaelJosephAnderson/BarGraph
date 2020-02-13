package barGraph;

import java.awt.Color;

public class Bar {

  // fields aka variables
  private Color color;
  private int height;
  
  // constructor
  public Bar(int height)
  {
    this.height = height;
    int r = (int) (Math.random() * 256);
    int g = (int) (Math.random() * 256);
    int b = (int) (Math.random() * 256);
    this.color = new Color(r, g, b);
  }
  
  // getter methods
  public Color getColor() { return this.color; }
  public int getHeight() { return this.height; }
}
