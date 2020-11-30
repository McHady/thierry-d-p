package matcher.perform;

public class DataPair {

    private final Double sharesOutstanding;
    private final Double publicFloat;

    public DataPair(Double sharesOutstanding, Double publicFloat) {

        this.sharesOutstanding = sharesOutstanding;
        this.publicFloat = publicFloat;
    }

    public Double getSharesOutstanding() {
        return sharesOutstanding;
    }

    public Double getPublicFloat() {
        return publicFloat;
    }
}
