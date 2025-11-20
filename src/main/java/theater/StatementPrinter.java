package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {
    private final Invoice invoice;
    private final Map<String, Play> plays;

    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */
    public String statement() {
        int totalAmount = 0;
        int totalVolumeCredits = 0;

        final StringBuilder result = new StringBuilder(
                "Statement for " + invoice.getCustomer() + System.lineSeparator());

        final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

        for (Performance performance : invoice.getPerformances()) {
            final Play play = plays.get(performance.getPlayID());

            final int performanceAmount = calculateAmount(performance, play);
            final int performanceCredits = calculateVolumeCredits(performance, play);

            totalAmount += performanceAmount;
            totalVolumeCredits += performanceCredits;

            result.append(formatPerformanceLine(performance, play, performanceAmount, currencyFormatter));
        }

        result.append(String.format("Amount owed is %s%n",
                currencyFormatter.format(totalAmount / Constants.PERCENT_FACTOR)));
        result.append(String.format("You earned %s credits%n", totalVolumeCredits));

        return result.toString();
    }

    private int calculateAmount(Performance performance, Play play) {
        int amount = 0;
        switch (play.getType()) {
            case "tragedy":
                amount = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    amount += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                amount = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    amount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD);
                }
                amount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new RuntimeException("unknown type: " + play.getType());
        }
        return amount;
    }

    private int calculateVolumeCredits(Performance performance, Play play) {
        int credits = Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        if ("comedy".equals(play.getType())) {
            credits += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return credits;
    }

    private String formatPerformanceLine(Performance performance, Play play, int amount, NumberFormat currencyFormatter) {
        return String.format("  %s: %s (%s seats)%n",
                play.getName(),
                currencyFormatter.format(amount / Constants.PERCENT_FACTOR),
                performance.getAudience());
    }

}
