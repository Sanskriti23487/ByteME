import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class Test1 {

    @Test
    void testAddition() {
        int num1 = 1;
        int num2 = 1;

        int result = num1 + num2;

        assertEquals(2, result, "1 + 1 should equal 2");
    }

}
