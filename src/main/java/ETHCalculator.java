import lombok.Data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.YearMonth;

@Data

public class ETHCalculator {

    private double ethereumAmount;
    private double monthlyRewardPercentage;
    private int rewardDay;
    private LocalDate startDate;
    private int years;
    private LocalDate date;



    FileWriter fw = new FileWriter("amadeus.csv");
    PrintWriter out = new PrintWriter(fw);


    public ETHCalculator(double ethereumAmount, double monthlyRewardPercentage, int rewardDay, LocalDate startDate, int years) throws IOException {
        this.ethereumAmount = ethereumAmount;
        this.monthlyRewardPercentage = monthlyRewardPercentage;
        this.rewardDay = rewardDay;
        this.startDate = startDate;
        this.years = years;
        this.date = startDate;

    }



    private int daysInMonthCounter(LocalDate date) {
        YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonthValue());
        return yearMonth.lengthOfMonth();
    }

    public double rewardPerGivenDays(int days) {
        return (ethereumAmount * (monthlyRewardPercentage / 100)) / 365 * days;
    }


    private double rewardForCurrentMonth(LocalDate date) {
        return rewardPerGivenDays(daysInMonthCounter(date) - rewardDay);
    }
    private double rewardForCurrentMonthIfStartAfterReward(LocalDate date) {
        return rewardPerGivenDays(daysInMonthCounter(date)-startDate.getDayOfMonth());
    }

    private double rewardForNewMonthUntilRewardDay() {
        return rewardPerGivenDays(rewardDay);
    }

    private double rewardForFullMonth(LocalDate date) {
        return rewardForCurrentMonth(date) + rewardForNewMonthUntilRewardDay();
    }

    private double rewardForLastMonth(LocalDate date) {
        double rewardForPreLastMonthDays = rewardForCurrentMonth(date);
        setDate(date.plusMonths(1));
        double rewardForRemainingDaysLastMonth = rewardPerGivenDays(date.getDayOfMonth());
        return rewardForPreLastMonthDays + rewardForRemainingDaysLastMonth;
    }


    public void calculateTotalAfterYears() throws IOException {
        fw.write("Date" + "," + "Amounth" + "," + "Reward" + "," + "Reward Rate" + "\n");

        if (startDate.getDayOfMonth() < rewardDay) {
            double reward = rewardPerGivenDays(rewardDay - date.getDayOfMonth());
            setEthereumAmount(ethereumAmount + reward);
            System.out.println("Reward for "+date.getMonth()+" "+date.getYear()+": "+reward);
            System.out.println("New Total: "+ethereumAmount);
            out.print(date + "," + ethereumAmount + "," + reward + "," + monthlyRewardPercentage + "," + "\n");

        }
        if(startDate.getDayOfMonth()>rewardDay){
            System.out.println("Reward for "+date.getMonth()+" "+date.getYear()+": "+0);
            System.out.println("New Total: "+ethereumAmount);
            double reward = rewardForCurrentMonthIfStartAfterReward(date) + rewardForNewMonthUntilRewardDay();
            setEthereumAmount(ethereumAmount + reward);
            setDate(date.plusMonths(1));
            System.out.println("Reward for "+date.getMonth()+" "+date.getYear()+": "+reward);
            System.out.println("New Total: "+ethereumAmount);
            out.print(date + "," + ethereumAmount + "," + reward + "," + monthlyRewardPercentage + "," + "\n");

        }
        if(startDate.getDayOfMonth() == rewardDay){
            System.out.println("Reward for "+date.getMonth()+" "+date.getYear()+": "+0);
            System.out.println("New Total: "+ethereumAmount);
            out.print(date + "," + ethereumAmount + "," + 0 + "," + monthlyRewardPercentage + "," + "\n");
        }
        mainEthereumCalcLoop();
    }

    private void mainEthereumCalcLoop() throws IOException {

        while (date.isBefore(startDate.plusYears(years).minusMonths(1))) {
            double reward = rewardForFullMonth(date);
            if (daysInMonthCounter(date) < startDate.getDayOfMonth()) {
                setEthereumAmount(ethereumAmount + reward);
                setDate(date.plusMonths(1));
                setDate(date.withDayOfMonth(startDate.getDayOfMonth()));
                System.out.println("New Total: "+ethereumAmount);
                out.print(date + "," + ethereumAmount + "," + reward + "," + monthlyRewardPercentage + "," + "\n");



                continue;
            }
            setEthereumAmount(ethereumAmount + reward);
            setDate(date.plusMonths(1));
            System.out.println("Reward for "+date.getMonth()+" "+date.getYear()+": "+reward);
            System.out.println("New Total: "+ethereumAmount);
            out.print(date + "," + ethereumAmount + "," + reward + "," + monthlyRewardPercentage + "," + "\n");



        }
        double reward = rewardForLastMonth(date);
        setEthereumAmount(ethereumAmount + reward);
        System.out.println("Reward for "+date.getMonth()+" "+date.getYear()+": "+reward);
        System.out.println("New Total: "+ethereumAmount);
        out.print(date + "," + ethereumAmount + "," + reward + "," + monthlyRewardPercentage + "," + "\n");

        out.close();
    }


}