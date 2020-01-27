
public class Domino implements Comparable<Domino> {
    int high;
    int low;
    int hx;
    int hy;
    int lx;
    int ly;
    boolean placed = false;

    public Domino() {
    }

    public Domino(int high, int low) {
        super();
        this.high = high;
        this.low = low;
    }

    //TODO : Long Parameter List
    void place(Domino domino) {
        this.hx = domino.getHx();
        this.hy = domino.getHy();
        this.lx = domino.getLx();
        this.ly = domino.getLy();
        placed = true;
    }

    //TODO :  duplicate code
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("[").append(high).append(low).append("]");
        if (!placed) {
            result.append("unplaced");
        } else {
            result.append("(").append(hx + 1).append(",").append((hy + 1)).append(")").append("(").append((lx + 1)).append(",").append((ly + 1)).append(")");
        }
        return result.toString();
    }

    /**
     * turn the domino around 180 degrees clockwise
     */

    void invert() {
        int tx = hx;
        hx = lx;
        lx = tx;

        int ty = hy;
        hy = ly;
        ly = ty;
    }

    boolean ishl() {
        return hy == ly;
    }


    public int compareTo(Domino arg0) {
        if (this.high < arg0.high) {
            return 1;
        }
        return this.low - arg0.low;
    }

    public int getHx() {
        return hx;
    }

    public void setHx(int hx) {
        this.hx = hx;
    }

    public int getHy() {
        return hy;
    }

    public void setHy(int hy) {
        this.hy = hy;
    }

    public int getLx() {
        return lx;
    }

    public void setLx(int lx) {
        this.lx = lx;
    }

    public int getLy() {
        return ly;
    }

    public void setLy(int ly) {
        this.ly = ly;
    }

}
