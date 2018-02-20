package lt.donatas.vaadin_laivu_musis.beans;

public class TableData {
    public static final int NO_SHIP = 0;
    public static final int SET_SHIP = 1;
    public static final int SHIP_IS_FOUND = 2;
    public static final int ILLEGAL_PLACE_FOR_SHIP = -1;
    public static final int TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET = -2;
    public static final int OPEN_UNTIL_THE_SHIP_IS_SET = -3;
    public static final int TEMPORARY_BLOKED_UNTIL_3_SIZE_SHIPS_IS_SET = -4;
    public static final int TEMPORARY_BLOKED_UNTIL_BIGGER_SHIP_IS_FOUND = -6;

    private int[][] lentele = new int[10][10];

    public int[][] getLentele() {
        return lentele;
    }
}
