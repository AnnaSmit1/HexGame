import general.Board;
import general.Color;
import org.junit.jupiter.api.*;

class BoardTest {

    Board testBoard;
    Color[][] testFields;

    @BeforeEach
    void setUp() {
        testBoard = new Board();
        testFields = testBoard.fields;

    }

    @Test
    void isGameOver() {
        for (int i = 0; i < 9; i++) {
            testBoard.setField(1, i, Color.BLUE);
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (i + j == 8)
                    testBoard.setField(i, j, Color.RED);
            }
        }
        System.out.println(testBoard);
        System.out.println(testBoard.isGameOver(Color.RED));
    }
}