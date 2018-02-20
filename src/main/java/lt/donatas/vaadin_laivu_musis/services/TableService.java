package lt.donatas.vaadin_laivu_musis.services;

import lt.donatas.vaadin_laivu_musis.beans.GameData;
import lt.donatas.vaadin_laivu_musis.beans.Koordinate;
import lt.donatas.vaadin_laivu_musis.beans.TableData;

public class TableService {
    public void setLangelioStatus(int[][] lentele, String langelis, int status) {
        lentele[convertCoordinateStringToInt(langelis).getX()][convertCoordinateStringToInt(langelis).getY()] = status;
    }

    public int getLangelioStatus(int[][] lentele, String langelis) {
        return lentele[convertCoordinateStringToInt(langelis).getX()][convertCoordinateStringToInt(langelis).getY()];
    }

    private Koordinate convertCoordinateStringToInt(String langelis) {
        int x = "KILOMETRAS".indexOf(langelis.charAt(0));
        int y = langelis.charAt(1) - 48;
        return new Koordinate(x, y);
    }

    private String convertCoordinateIntToString(int x, int y) {
        return String.valueOf("KILOMETRAS".charAt(x)) + y;
    }

    public String convertShipsFromIntArrayToString(int[][] lentele) {
        StringBuilder visiLaivai = new StringBuilder();
        StringBuilder laivoKoordinates = new StringBuilder();

        StringBuilder keturvietisLaivas = new StringBuilder();
        StringBuilder trivieciaiLaivai = new StringBuilder();
        StringBuilder dvivieciaiLaivai = new StringBuilder();
        StringBuilder vienvieciaiLaivai = new StringBuilder();

        String coordinate;

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (lentele[x][y] == TableData.SET_SHIP) {
                    coordinate = convertCoordinateIntToString(x, y);
                    laivoKoordinates.append(coordinate);
                    lentele[x][y] = TableData.SHIP_IS_FOUND;
                    uzblokuojaLaivoKoordinatesKampuoseEsanciusLangelius(lentele, coordinate);
                    for (int laivoIlgisY = 1; laivoIlgisY < 4; laivoIlgisY++) {
                        if (y + laivoIlgisY < 10 && lentele[x][y + laivoIlgisY] == TableData.SET_SHIP) {
                            coordinate = convertCoordinateIntToString(x, y + laivoIlgisY);
                            laivoKoordinates.append(coordinate);
                            lentele[x][y + laivoIlgisY] = TableData.SHIP_IS_FOUND;
                            uzblokuojaLaivoKoordinatesKampuoseEsanciusLangelius(lentele, coordinate);
                        }else{
                            break;
                        }
                    }
                    for (int laivoIlgisX = 1; laivoIlgisX < 4; laivoIlgisX++) {
                        if (x + laivoIlgisX < 10 && lentele[x + laivoIlgisX][y] == TableData.SET_SHIP) {
                            coordinate = convertCoordinateIntToString(x + laivoIlgisX, y);
                            laivoKoordinates.append(coordinate);
                            lentele[x + laivoIlgisX][y] = TableData.SHIP_IS_FOUND;
                            uzblokuojaLaivoKoordinatesKampuoseEsanciusLangelius(lentele, coordinate);
                        }else{
                            break;
                        }
                    }
                }

                if (laivoKoordinates.length() == 2) {
                    vienvieciaiLaivai.append(laivoKoordinates.charAt(0)).append(laivoKoordinates.charAt(1)).append("-");
                    vienvieciaiLaivai.append(laivoKoordinates.charAt(laivoKoordinates.length() - 2)).append(laivoKoordinates.charAt(laivoKoordinates.length() - 1)).append("!");
                }
                if (laivoKoordinates.length() == 4) {
                    dvivieciaiLaivai.append(laivoKoordinates.charAt(0)).append(laivoKoordinates.charAt(1)).append("-");
                    dvivieciaiLaivai.append(laivoKoordinates.charAt(laivoKoordinates.length() - 2)).append(laivoKoordinates.charAt(laivoKoordinates.length() - 1)).append("!");
                }
                if (laivoKoordinates.length() == 6) {
                    trivieciaiLaivai.append(laivoKoordinates.charAt(0)).append(laivoKoordinates.charAt(1)).append("-");
                    trivieciaiLaivai.append(laivoKoordinates.charAt(laivoKoordinates.length() - 2)).append(laivoKoordinates.charAt(laivoKoordinates.length() - 1)).append("!");

                }
                if (laivoKoordinates.length() == 8) {
                    keturvietisLaivas.append(laivoKoordinates.charAt(0)).append(laivoKoordinates.charAt(1)).append("-");
                    keturvietisLaivas.append(laivoKoordinates.charAt(laivoKoordinates.length() - 2)).append(laivoKoordinates.charAt(laivoKoordinates.length() - 1)).append("!");
                }

                laivoKoordinates = new StringBuilder();
            }
        }
        if (vienvieciaiLaivai.length() > 0) {
            vienvieciaiLaivai.deleteCharAt(vienvieciaiLaivai.length() - 1);
        }

        for (int i = 0; i < 100; i++) {
            if (lentele[i / 10][i % 10] == TableData.SHIP_IS_FOUND) {
                lentele[i / 10][i % 10] = TableData.SET_SHIP;
            }
        }

        visiLaivai.append(keturvietisLaivas).append(trivieciaiLaivai).append(dvivieciaiLaivai).append(vienvieciaiLaivai);
        return visiLaivai.toString();
    }

    private void uzblokuojaLaivoKoordinatesKampuoseEsanciusLangelius(int[][] lentele, String coordinate) {
        Koordinate xy = convertCoordinateStringToInt(coordinate);
        int x = xy.getX();
        int y = xy.getY();

        if (x - 1 >= 0 && y - 1 >= 0 && (lentele[x - 1][y - 1] != TableData.SET_SHIP || lentele[x - 1][y - 1] != TableData.SHIP_IS_FOUND))
            lentele[x - 1][y - 1] = TableData.ILLEGAL_PLACE_FOR_SHIP;
        if (x - 1 >= 0 && y + 1 <= 9 && (lentele[x - 1][y + 1] != TableData.SET_SHIP || lentele[x - 1][y + 1] != TableData.SHIP_IS_FOUND))
            lentele[x - 1][y + 1] = TableData.ILLEGAL_PLACE_FOR_SHIP;
        if (x + 1 <= 9 && y - 1 >= 0 && (lentele[x + 1][y - 1] != TableData.SET_SHIP || lentele[x + 1][y - 1] != TableData.SHIP_IS_FOUND))
            lentele[x + 1][y - 1] = TableData.ILLEGAL_PLACE_FOR_SHIP;
        if (x + 1 <= 9 && y + 1 <= 9 && (lentele[x + 1][y + 1] != TableData.SET_SHIP || lentele[x + 1][y + 1] != TableData.SHIP_IS_FOUND))
            lentele[x + 1][y + 1] = TableData.ILLEGAL_PLACE_FOR_SHIP;
    }


    private void uzblokuotiLaivoPriekiIrPabaiga(int[][] lentele, String laivas) {
        int xIlgis = "KILOMETRAS".indexOf(laivas.charAt(3)) - "KILOMETRAS".indexOf(laivas.charAt(0));
        int yIlgis = (int) laivas.charAt(4) - (int) laivas.charAt(1);

        if (yIlgis > 0 || xIlgis + yIlgis == 0) {
            if ((int) laivas.charAt(1) - 48 - 1 >= 0 && lentele["KILOMETRAS".indexOf(laivas.charAt(0))][(int) laivas.charAt(1) - 48 - 1] != TableData.SET_SHIP)
                lentele["KILOMETRAS".indexOf(laivas.charAt(0))][(int) laivas.charAt(1) - 48 - 1] = TableData.ILLEGAL_PLACE_FOR_SHIP;
            if ((int) laivas.charAt(4) - 48 + 1 <= 9 && lentele["KILOMETRAS".indexOf(laivas.charAt(3))][(int) laivas.charAt(4) - 48 + 1] != TableData.SET_SHIP)
                lentele["KILOMETRAS".indexOf(laivas.charAt(3))][(int) laivas.charAt(4) - 48 + 1] = TableData.ILLEGAL_PLACE_FOR_SHIP;
        }
        if (xIlgis > 0 || xIlgis + yIlgis == 0) {
            if ("KILOMETRAS".indexOf(laivas.charAt(0)) - 1 >= 0 && lentele["KILOMETRAS".indexOf(laivas.charAt(0)) - 1][(int) laivas.charAt(1) - 48] != TableData.SET_SHIP)
                lentele["KILOMETRAS".indexOf(laivas.charAt(0)) - 1][(int) laivas.charAt(1) - 48] = TableData.ILLEGAL_PLACE_FOR_SHIP;
            if ("KILOMETRAS".indexOf(laivas.charAt(3)) + 1 <= 9 && lentele["KILOMETRAS".indexOf(laivas.charAt(3)) + 1][(int) laivas.charAt(4) - 48] != TableData.SET_SHIP)
                lentele["KILOMETRAS".indexOf(laivas.charAt(3)) + 1][(int) laivas.charAt(4) - 48] = TableData.ILLEGAL_PLACE_FOR_SHIP;
        }
    }

    private void uzblokuotiLaikinaiVisusLaisvusLangelius(int[][] lentele, String laivas) {
        int xIlgis = "KILOMETRAS".indexOf(laivas.charAt(3)) - "KILOMETRAS".indexOf(laivas.charAt(0));
        int yIlgis = (int) laivas.charAt(4) - (int) laivas.charAt(1);

        for (int i = 0; i < 100; i++) {
            if (lentele[i / 10][i % 10] == TableData.NO_SHIP) {
                lentele[i / 10][i % 10] = TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET;
            }
        }
        if (yIlgis > 0 || xIlgis + yIlgis == 0) {
            if ((int) laivas.charAt(1) - 48 - 1 >= 0 && lentele["KILOMETRAS".indexOf(laivas.charAt(0))][(int) laivas.charAt(1) - 48 - 1] == TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET)
                lentele["KILOMETRAS".indexOf(laivas.charAt(0))][(int) laivas.charAt(1) - 48 - 1] = TableData.OPEN_UNTIL_THE_SHIP_IS_SET;
            if ((int) laivas.charAt(4) - 48 + 1 <= 9 && lentele["KILOMETRAS".indexOf(laivas.charAt(3))][(int) laivas.charAt(4) - 48 + 1] == TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET)
                lentele["KILOMETRAS".indexOf(laivas.charAt(3))][(int) laivas.charAt(4) - 48 + 1] = TableData.OPEN_UNTIL_THE_SHIP_IS_SET;
        }
        if (xIlgis > 0 || xIlgis + yIlgis == 0) {
            if ("KILOMETRAS".indexOf(laivas.charAt(0)) - 1 >= 0 && lentele["KILOMETRAS".indexOf(laivas.charAt(0)) - 1][(int) laivas.charAt(1) - 48] == TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET)
                lentele["KILOMETRAS".indexOf(laivas.charAt(0)) - 1][(int) laivas.charAt(1) - 48] = TableData.OPEN_UNTIL_THE_SHIP_IS_SET;
            if ("KILOMETRAS".indexOf(laivas.charAt(3)) + 1 <= 9 && lentele["KILOMETRAS".indexOf(laivas.charAt(3)) + 1][(int) laivas.charAt(4) - 48] == TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET)
                lentele["KILOMETRAS".indexOf(laivas.charAt(3)) + 1][(int) laivas.charAt(4) - 48] = TableData.OPEN_UNTIL_THE_SHIP_IS_SET;
        }
    }

    public boolean arSustatytiManoLaivai(int[][] lentele) {
        int uzimtuLangeliuSkaicius = 0;
        for (int i = 0; i < 100; i++) {
            if (lentele[i / 10][i % 10] > 0) {
                uzimtuLangeliuSkaicius++;
            }
        }
        return (uzimtuLangeliuSkaicius >= 20);
    }

    public int paskaiciuotiKokioDidziausioLaivoTruksta(int[][] lentele) {
        int keturvieciai = 0;
        int trivieciai = 0;
        int dvivieciai = 0;
        int vienvieciai = 0;
        int ilgis;
        int xIlgis;
        int yIlgis;
        int result;
        String laivas;

        String visiLaivai = convertShipsFromIntArrayToString(lentele);
        while (true) {
            if (visiLaivai.indexOf('!') != -1) {
                laivas = visiLaivai.substring(0, visiLaivai.indexOf('!'));
                if (visiLaivai.indexOf('!') + 1 != -1)
                    visiLaivai = visiLaivai.substring(visiLaivai.indexOf('!') + 1);
            } else {
                laivas = visiLaivai;
                visiLaivai = "";
            }

            try {
                xIlgis = "KILOMETRAS".indexOf(laivas.charAt(3)) - "KILOMETRAS".indexOf(laivas.charAt(0));
                yIlgis = (int) laivas.charAt(4) - (int) laivas.charAt(1);
                ilgis = 1 + xIlgis + yIlgis;
            } catch (Exception e) {
                ilgis = 0;
                e.printStackTrace(System.out);
            }

            if (ilgis == 1) {
                if (dvivieciai >= GameData.MAKSIMALUS_SKAICIUS_DVIVIECIU && trivieciai >= GameData.MAKSIMALUS_SKAICIUS_TRIVIECIU && keturvieciai >= GameData.MAKSIMALUS_SKAICIUS_KETURVIECIU) {
                    uzblokuotiLaivoPriekiIrPabaiga(lentele, laivas);
                } else {
                    uzblokuotiLaikinaiVisusLaisvusLangelius(lentele, laivas);
                }
                vienvieciai++;
            } else if (ilgis == 2) {
                if (trivieciai >= GameData.MAKSIMALUS_SKAICIUS_TRIVIECIU && keturvieciai >= GameData.MAKSIMALUS_SKAICIUS_KETURVIECIU) {
                    uzblokuotiLaivoPriekiIrPabaiga(lentele, laivas);
                } else {
                    uzblokuotiLaikinaiVisusLaisvusLangelius(lentele, laivas);
                }
                dvivieciai++;
            } else if (ilgis == 3) {
                if (keturvieciai >= GameData.MAKSIMALUS_SKAICIUS_KETURVIECIU) {
                    uzblokuotiLaivoPriekiIrPabaiga(lentele, laivas);
                } else {
                    uzblokuotiLaikinaiVisusLaisvusLangelius(lentele, laivas);
                }
                trivieciai++;
            } else if (ilgis == 4) {
                uzblokuotiLaivoPriekiIrPabaiga(lentele, laivas);
                keturvieciai++;
            }

            result = 4;
            if (keturvieciai == GameData.MAKSIMALUS_SKAICIUS_KETURVIECIU) {
                result = 3;
            }
            if (keturvieciai == GameData.MAKSIMALUS_SKAICIUS_KETURVIECIU && trivieciai == GameData.MAKSIMALUS_SKAICIUS_TRIVIECIU) {
                result = 2;
            }
            if (keturvieciai == GameData.MAKSIMALUS_SKAICIUS_KETURVIECIU && trivieciai == GameData.MAKSIMALUS_SKAICIUS_TRIVIECIU && dvivieciai == GameData.MAKSIMALUS_SKAICIUS_DVIVIECIU) {
                result = 1;
            }
            if (keturvieciai == GameData.MAKSIMALUS_SKAICIUS_KETURVIECIU && trivieciai == GameData.MAKSIMALUS_SKAICIUS_TRIVIECIU && dvivieciai == GameData.MAKSIMALUS_SKAICIUS_DVIVIECIU && vienvieciai == GameData.MAKSIMALUS_SKAICIUS_VIENVIECIU) {
                result = 0;
            }

            if (visiLaivai.trim().length() < 4) break;
        }

        int countLegalMoves = 0;

        for (int i = 0; i < 100; i++) {
            if (lentele[i / 10][i % 10] == TableData.OPEN_UNTIL_THE_SHIP_IS_SET) {
                countLegalMoves++;
            }
        }

        for (int i = 0; i < 100; i++) {
            if (lentele[i / 10][i % 10] == TableData.OPEN_UNTIL_THE_SHIP_IS_SET) {
                lentele[i / 10][i % 10] = TableData.NO_SHIP;
            }
        }

        blokuojalikusiusKolNesudetiTrivieciai(lentele, keturvieciai, trivieciai, dvivieciai, countLegalMoves);
        atblokuotiLaikinaiUzblokuotusLangelius(lentele);
        laikinaiBlokuojaVienvieciusLangeliusJeiNesudetiDidesniLaivai(lentele, dvivieciai, trivieciai, keturvieciai);
        patikrintiIrAtblokuotiJeiNeraLaisvuLangeliu(lentele);
        return result;
    }

    private void patikrintiIrAtblokuotiJeiNeraLaisvuLangeliu(int[][] lentele) {
        int laisvuEjimu = 0;
        for (int i = 0; i < 100; i++) {
            if (lentele[i / 10][i % 10] == TableData.NO_SHIP) {
                laisvuEjimu++;
            }
        }
        if (laisvuEjimu == 0) {
            for (int i = 0; i < 100; i++) {
                if (lentele[i / 10][i % 10] <= TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET) {
                    lentele[i / 10][i % 10] = TableData.NO_SHIP;
                }
            }
        }
    }

    private void laikinaiBlokuojaVienvieciusLangeliusJeiNesudetiDidesniLaivai(int[][] lentele, int dvivieciai, int trivieciai, int keturvieciai) {
        if (!(dvivieciai >= GameData.MAKSIMALUS_SKAICIUS_DVIVIECIU && trivieciai >= GameData.MAKSIMALUS_SKAICIUS_TRIVIECIU && keturvieciai >= GameData.MAKSIMALUS_SKAICIUS_KETURVIECIU)) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if ((lentele[x][y] != TableData.SET_SHIP && lentele[x][y] != TableData.ILLEGAL_PLACE_FOR_SHIP) && (x + 1 > 9 || lentele[x + 1][y] == TableData.ILLEGAL_PLACE_FOR_SHIP) && (x - 1 < 0 || lentele[x - 1][y] == TableData.ILLEGAL_PLACE_FOR_SHIP)
                            && (y + 1 > 9 || lentele[x][y + 1] == TableData.ILLEGAL_PLACE_FOR_SHIP) && (y - 1 < 0 || lentele[x][y - 1] == TableData.ILLEGAL_PLACE_FOR_SHIP)) {
                        lentele[x][y] = TableData.TEMPORARY_BLOKED_UNTIL_BIGGER_SHIP_IS_FOUND;
                    }
                }
            }
        } else {
            for (int i = 0; i < 100; i++) {
                if (lentele[i / 10][i % 10] == TableData.TEMPORARY_BLOKED_UNTIL_BIGGER_SHIP_IS_FOUND) {
                    lentele[i / 10][i % 10] = TableData.NO_SHIP;
                }
            }
        }
    }

    private void atblokuotiLaikinaiUzblokuotusLangelius(int[][] lentele) {
        int neublokuotiLangeliai = 0;
        for (int i = 0; i < 100; i++) {
            if (lentele[i / 10][i % 10] == TableData.NO_SHIP) {
                neublokuotiLangeliai++;
            }
        }

        if (neublokuotiLangeliai == 0) {
            for (int i = 0; i < 100; i++) {
                if (lentele[i / 10][i % 10] == TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET) {
                    lentele[i / 10][i % 10] = TableData.NO_SHIP;
                }
            }
        }
    }

    private void blokuojalikusiusKolNesudetiTrivieciai(int[][] lentele, int keturvieciai, int trivieciai, int dvivieciai, int countLegalMoves) {

        if (keturvieciai == GameData.MAKSIMALUS_SKAICIUS_KETURVIECIU && dvivieciai <= GameData.MAKSIMALUS_SKAICIUS_DVIVIECIU && trivieciai != GameData.MAKSIMALUS_SKAICIUS_TRIVIECIU) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (countLegalMoves == 0 && lentele[x][y] == TableData.NO_SHIP) {
                        if (x + 1 > 9 || (x + 1 <= 9 && lentele[x + 1][y] != TableData.NO_SHIP && lentele[x + 1][y] != TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET && lentele[x + 1][y] != TableData.SET_SHIP)) {
                            if (x - 1 < 0 || (x - 1 >= 0 && lentele[x - 1][y] != TableData.NO_SHIP && lentele[x - 1][y] != TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET && lentele[x - 1][y] != TableData.SET_SHIP)) {
                                if (y - 1 < 0 || (y - 1 >= 0 && lentele[x][y - 1] != TableData.NO_SHIP && lentele[x][y - 1] != TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET && lentele[x][y - 1] != TableData.SET_SHIP)) {
                                    if (y + 1 > 9 || (y + 1 <= 9 && lentele[x][y + 1] != TableData.NO_SHIP && lentele[x][y + 1] != TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET && lentele[x][y + 1] != TableData.SET_SHIP)) {
                                        lentele[x][y] = TableData.TEMPORARY_BLOKED_UNTIL_3_SIZE_SHIPS_IS_SET;
                                    }
                                }
                            }
                        }
                    }
                    if (countLegalMoves != 0 && dvivieciai >= GameData.MAKSIMALUS_SKAICIUS_DVIVIECIU && lentele[x][y] == TableData.SET_SHIP) {
                        if (x + 1 <= 9 && lentele[x + 1][y] == TableData.NO_SHIP && !(x - 1 >= 0 && lentele[x - 1][y] == TableData.SET_SHIP)) {
                            if (x + 2 <= 9 && lentele[x + 2][y] != TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET) {
                                lentele[x + 1][y] = TableData.TEMPORARY_BLOKED_UNTIL_3_SIZE_SHIPS_IS_SET;
                            }
                        }
                        if (x - 1 >= 0 && lentele[x - 1][y] == TableData.NO_SHIP && !(x + 1 <= 9 && lentele[x + 1][y] == TableData.SET_SHIP)) {
                            if (x - 2 >= 0 && lentele[x - 2][y] != TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET) {
                                lentele[x - 1][y] = TableData.TEMPORARY_BLOKED_UNTIL_3_SIZE_SHIPS_IS_SET;
                            }
                        }
                        if (y + 1 <= 9 && lentele[x][y + 1] == TableData.NO_SHIP && !(y - 1 >= 0 && lentele[x][y - 1] == TableData.SET_SHIP)) {
                            if (y + 2 <= 9 && lentele[x][y + 2] != TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET) {
                                lentele[x][y + 1] = TableData.TEMPORARY_BLOKED_UNTIL_3_SIZE_SHIPS_IS_SET;
                            }
                        }
                        if (y - 1 >= 0 && lentele[x][y - 1] == TableData.NO_SHIP && !(y + 1 <= 9 && lentele[x][y + 1] == TableData.SET_SHIP)) {
                            if (y - 2 >= 0 && lentele[x][y - 2] != TableData.TEMPORARY_BLOKED_UNTIL_THE_SHIP_IS_SET) {
                                lentele[x][y - 1] = TableData.TEMPORARY_BLOKED_UNTIL_3_SIZE_SHIPS_IS_SET;
                            }
                        }
                    }
                }
            }
        } else {
            atblokuotiLaikinaiUzblokuotusLangelius(lentele);
            for (int i = 0; i < 100; i++) {
                if (lentele[i / 10][i % 10] == TableData.TEMPORARY_BLOKED_UNTIL_3_SIZE_SHIPS_IS_SET) {
                    lentele[i / 10][i % 10] = TableData.NO_SHIP;
                }
            }
        }
    }
}
