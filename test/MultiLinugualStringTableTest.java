import static org.junit.jupiter.api.Assertions.*;

class MultiLinugualStringTableTest {

    @org.junit.jupiter.api.Test
    void getMessage() {
        assertEquals(MultiLinugualStringTable.getMessage(0), "Enter your name:");
        assertEquals(MultiLinugualStringTable.getMessage(1), "Welcome");
        assertEquals(MultiLinugualStringTable.getMessage(2), "Have a good time playing Abominodo");

    }
}