package interview;

public enum Denomination {
    FIVE_SOUTHAND(5_000),
    ONE_SOUTHAND(1_000),
    FIVE_HUNDRED(500),
    ONE_HUNDRED(100),
    FIFTY(50);

    public final int numRepresentation;

    private Denomination(int numRepresentation) {
        this.numRepresentation = numRepresentation;
    }

    public int getNumRepresentation() {
        return this.numRepresentation;
    }
}
