import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DominoTest {

    @Test
    void place() {
        Domino dominoRequest = new Domino();
        dominoRequest.setHx(1);
        dominoRequest.setHy(2);
        dominoRequest.setLx(1 + 1);
        dominoRequest.setLy(2);

        Domino domino = new Domino();
        domino.place(dominoRequest);

        assertEquals(1, domino.getHx());
        assertEquals(2, domino.getHy());
        assertEquals(2, domino.getLx());
        assertEquals(2, domino.getLy());


    }

    @Test
    void invert() {
        Domino dominoRequest = new Domino();
        dominoRequest.setHx(1);
        dominoRequest.setHy(2);
        dominoRequest.setLx(1 + 1);
        dominoRequest.setLy(2);

        Domino domino = new Domino();
        domino.place(dominoRequest);

        domino.invert();

        assertEquals(1, domino.getLx());
        assertEquals(2, domino.getLy());
        assertEquals(2, domino.getHx());
        assertEquals(2, domino.getHy());
    }

    @Test
    void ishl() {
        Domino dominoRequest = new Domino();
        dominoRequest.setHx(1);
        dominoRequest.setHy(2);
        dominoRequest.setLx(1 + 1);
        dominoRequest.setLy(2);

        Domino domino = new Domino();
        domino.place(dominoRequest);
        assertTrue(domino.ishl());
    }

    @Test
    void compareTo() {
        Domino domino = new Domino(5,4);
        Domino arg = new Domino(7,5);
        assertEquals(1, domino.compareTo(arg));

        Domino domino2 = new Domino(5,4);
        Domino arg2 = new Domino(4,2);
        assertEquals(2, domino2.compareTo(arg2));
    }
}