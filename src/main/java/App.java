import java.io.IOException;
import java.time.LocalDate;


public class App {




    public static void main(String[] args) throws IOException {



        ETHCalculator ethCalculator = new ETHCalculator(25.000000, 10.00,23, LocalDate.of(2019,04,15),2);

        ethCalculator.calculateTotalAfterYears();




    }




}
