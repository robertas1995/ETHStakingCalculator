import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ETHCalculatorTest {


    @Test
    void calculateRewardDailyValueOfMonth() {
        int days = 28;
        double eth = 25;
        double rate = 10;
        double sum = (eth*(rate/100)/12)/days;
        assertEquals(0.00074404, sum,0.01 );
    }
}