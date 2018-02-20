package lt.donatas.vaadin_laivu_musis.services;

import lt.donatas.vaadin_laivu_musis.beans.TableData;

public class BotService {

    public int[][] turnOnBotMethods(int[][] lentele) {
        lentele=uzblokuojaPavieniusLangeliusJeiVienvieciaiNumusti(lentele);
        lentele=sautiKurDidesneTikimybePataikyti(lentele);
      //  patikrintiIrAtblokuotiJeiNeraLaisvuLangeliu(lentele);
        return lentele;
    }

    private int[][] uzblokuojaPavieniusLangeliusJeiVienvieciaiNumusti(int[][] lentele) {
        int vienvieciai = 0;
        try {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (lentele[x][y] == TableData.SET_SHIP && (x + 1 > 9 || lentele[x + 1][y] == TableData.ILLEGAL_PLACE_FOR_SHIP) && (x - 1 < 0 || lentele[x - 1][y] == TableData.ILLEGAL_PLACE_FOR_SHIP)
                            && (y + 1 > 9 || lentele[x][y + 1] == TableData.ILLEGAL_PLACE_FOR_SHIP) && (y - 1 < 0 || lentele[x][y - 1] == TableData.ILLEGAL_PLACE_FOR_SHIP)) {
                        vienvieciai++;
                    }
                }
            }
            if (vienvieciai >= 4) {
                for (int x = 0; x < 10; x++) {
                    for (int y = 0; y < 10; y++) {
                        if ((lentele[x][y] != TableData.SET_SHIP && lentele[x][y] != TableData.ILLEGAL_PLACE_FOR_SHIP) && (x + 1 > 9 || lentele[x + 1][y] == TableData.ILLEGAL_PLACE_FOR_SHIP) && (x - 1 < 0 || lentele[x - 1][y] == TableData.ILLEGAL_PLACE_FOR_SHIP)
                                && (y + 1 > 9 || lentele[x][y + 1] == TableData.ILLEGAL_PLACE_FOR_SHIP) && (y - 1 < 0 || lentele[x][y - 1] == TableData.ILLEGAL_PLACE_FOR_SHIP)) {
                            lentele[x][y] = TableData.ILLEGAL_PLACE_FOR_SHIP;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return lentele;
    }

    private int[][] sautiKurDidesneTikimybePataikyti(int[][] lentele) {
        int geresnesGalimybes = 0;
        int tikimybePataikyti = 0;
        try {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (lentele[x][y] == TableData.NO_SHIP) {
                        if (x + 1 <= 9 && lentele[x + 1][y] == TableData.NO_SHIP) {
                            tikimybePataikyti++;
                        }
                        if (x - 1 >= 0 && lentele[x - 1][y] == TableData.NO_SHIP) {
                            tikimybePataikyti++;
                        }
                        if (y + 1 <= 9 && lentele[x][y + 1] == TableData.NO_SHIP) {
                            tikimybePataikyti++;
                        }
                        if (y - 1 >= 0 && lentele[x][y - 1] == TableData.NO_SHIP) {
                            tikimybePataikyti++;
                        }
                        if (tikimybePataikyti > 1) {
                            geresnesGalimybes++;
                        }
                    }
                    tikimybePataikyti = 0;
                }
            }
        } catch (Exception e) {
            System.out.println("out of bounds");
            e.printStackTrace(System.out);
        }

//        if (geresnesGalimybes > 0) {
//            for (int x = 0; x < 10; x++) {
//                for (int y = 0; y < 10; y++) {
//                    if (lentele[x][y] == 0
//                            && (((x + 1 <= 9 && lentele[x + 1][y] == 0) && ((x - 1 < 0 || lentele[x - 1][y] == -1) && (y + 1 > 9 || lentele[x][y + 1] == -1) && (y - 1 < 0 || lentele[x][y - 1] == -1)))
//                            || ((x - 1 >= 0 && lentele[x - 1][y] == 0) && ((y + 1 > 9 || lentele[x][y + 1] == -1) && (y - 1 < 0 && lentele[x][y - 1] == 0)))
//                            || ((y + 1 <= 9 && lentele[x][y+1] == 0) && ((y - 1 < 0 || lentele[x][y - 1] == -1))))) {
//                        lentele[x][y] = -5;
//                    }
//                }
//            }
//        } else {
//            for (int i = 0; i < 100; i++) {
//                if (lentele[i / 10][i % 10] == -5) {
//                    lentele[i / 10][i % 10] = 0;
//                }
//            }
//        }
        return lentele;
    }
}
