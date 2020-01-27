
public class Location {
  private int c;
  private int r;
  DIRECTION d;
  
  public enum DIRECTION {VERTICAL, HORIZONTAL};
  
  public Location(int r, int c) {
    this.r = r;
    this.c = c;
  }

  public Location(int r, int c, DIRECTION d) {    
    this(r,c);
    this.d=d;
  }
  
  public String toString() {
    return "(" + (c+1) + "," + (r+1) + "," + d + ")";
  }
}
