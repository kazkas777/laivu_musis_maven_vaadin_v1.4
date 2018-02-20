package lt.donatas.vaadin_laivu_musis;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.UIEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import lt.donatas.vaadin_laivu_musis.beans.GameData;
import lt.donatas.vaadin_laivu_musis.beans.TableData;
import lt.donatas.vaadin_laivu_musis.beans.User;
import lt.donatas.vaadin_laivu_musis.services.BotService;
import lt.donatas.vaadin_laivu_musis.services.GameService;
import lt.donatas.vaadin_laivu_musis.services.TableService;
import lt.donatas.vaadin_laivu_musis.services.UserService;

import java.util.*;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    static final String ADD_COMPONENT = "addComponent";
    static final String REMOVE_COMPONENT = "removeComponent";
    static final String SET_CAPTION = "setCaption";
    static final String SET_STYLE = "setStyle";
    static final String SET_VALUE = "setValue";

    static final String BOT_ON = "BOT_ON";
    static final String BOT_OFF = "BOT_OFF";
    static final String CHEATS_ENABLED = "CHEATS_ENABLED";
    static final String CHEATS_DISABLED = "CHEATS_DISABLED";

    static final int SIZE_OF_BUTTONS = 40;
    static final int GAP_BETWEEN_BUTTONS = 0;
    static final int SHIFT_TO_RIGHT = 200;  //30
    static final int SHIFT_TO_DOWN = 50;
    static final int GAP_BETWEEN_TABLES = SIZE_OF_BUTTONS * 10 + 80;

    boolean setupSent = false;

    final static String TRANSPARENT = "transparent";
    String botStatus = BOT_OFF;
    String cheatstatus = CHEATS_DISABLED;

    GameService gameService = new GameService();
    UserService userService = new UserService();
    User user = new User();
    GameData gameData = new GameData();
    BotService botService = new BotService();
    TableData manoTableData = new TableData();
    TableData priesininkoTableData = new TableData();
    TableService tableService = new TableService();

    AbsoluteLayout layout = new AbsoluteLayout();
    AbsoluteLayout messageLayout = new AbsoluteLayout();
    Button patvirtintiLaivus = new Button("Create new game");
    Button reset = new Button("Reset");
    Button random = new Button("Random");
    Button bot = new Button("Enable bot");
    Button statusButton = new Button("Status");
    Button cheatButton = new Button("Enable Cheats");
    List<Button> generatedButtons = new ArrayList<>();
    Label message = new Label("Spauskite Create new game, jei norite pradeti zaidima");

    List<Image> explodeGifs = new ArrayList<>();
    int indexOfExplodeGifs = 0;

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        createExplodeGifs();
        createGridLabels();
        createGridButtons();
        createLoginLogoutComponents();
        createMenuButtons();

        messageLayout.setWidth("340px");
        messageLayout.setHeight("100px");
        messageLayout.setStyleName("menuStyleMessage");
        message.setWidth("300px");
        message.setHeight("100px");
        message.setStyleName("whiteLabel");

        messageLayout.addComponent(message);

        layout.setHeight("662px");
        layout.setWidth("1366px");
        layout.setStyleName("backgroundimage3");
        setContent(layout);

        startPolling(1000);
        startStatusAndComponentsUpdater();
    }

    public void startPolling(int miliSeconds) {
        setPollInterval(miliSeconds);
        addPollListener(new UIEvents.PollListener() {
            @Override
            public void poll(UIEvents.PollEvent event) {
            }
        });
    }

    public void createExplodeGifs() {
        for (int i = 0; i < 30; i++) {
            Image explodeGif = new Image(null, new ThemeResource("images/explode2.gif"));
            explodeGif.setHeight("55px");
            explodeGif.setWidth("55px");
            explodeGifs.add(explodeGif);

        }
    }

    private void createGridButtons() {
        for (int i = 0; i < 200; i++) {
            Button gridButton = new Button();
            gridButton.setWidth(String.valueOf(SIZE_OF_BUTTONS - 1));
            gridButton.setHeight(String.valueOf(SIZE_OF_BUTTONS + 1));
            gridButton.setId(String.valueOf("KILOMETRAS".charAt(i % 10) + String.valueOf((i % 100) / 10)));
            gridButton.setStyleName(TRANSPARENT);
            generatedButtons.add(gridButton);
            layout.addComponent(gridButton, "left: " + ((SHIFT_TO_RIGHT + (SIZE_OF_BUTTONS + GAP_BETWEEN_BUTTONS) * (i % 10)) + GAP_BETWEEN_TABLES * (i / 100)) + "px; top: " + (SHIFT_TO_DOWN + (SIZE_OF_BUTTONS + GAP_BETWEEN_BUTTONS) * ((i % 100) / 10)) + "px;");

            int index = i;

            if (i < 100) {
                gridButton.addClickListener(e -> {
                    if (gameData.getStatus().equals(GameData.LOGIN)) {
                        if (tableService.getLangelioStatus(manoTableData.getLentele(), gridButton.getId()) == TableData.NO_SHIP) {
                            if (tableService.arSustatytiManoLaivai(manoTableData.getLentele())) {
                                Notification.show("Visi laivai sudeti");
                                return;
                            }
                            tableService.setLangelioStatus(manoTableData.getLentele(), gridButton.getId(), TableData.SET_SHIP);
                            gridButton.setStyleName("blue");
                            int kokiLaivaDeti = tableService.paskaiciuotiKokioDidziausioLaivoTruksta(manoTableData.getLentele());
                            if (kokiLaivaDeti == 4)
                                message.setValue("Uždekite keturvietį laivą");
                            if (kokiLaivaDeti == 3)
                                message.setValue("Uždekite trivietį laivą");
                            if (kokiLaivaDeti == 2)
                                message.setValue("Uždekite dvivietį laivą");
                            if (kokiLaivaDeti == 1)
                                message.setValue("Uždekite vienvietį laivą");
                            if (kokiLaivaDeti == 0)
                                message.setValue("Galite spausti Join");
                        }
                    }
                });
            }

            if (i >= 100) {
                gridButton.addClickListener(e -> {
                    showClickExplodeGif(index);
                    gameData = gameService.status(user.getId(), gameData.getGameID());
                    if (gameData.getStatus().equals(GameData.READY_TO_PLAY)) {
                        if (!gameData.isMyTurn()) {
                            Notification.show("Ne mano eile");
                            return;
                        }
                        if (tableService.getLangelioStatus(priesininkoTableData.getLentele(), generatedButtons.get(index).getId()) == TableData.NO_SHIP) {
                            gameData = gameService.turn(user.getId(), gameData.getGameID(), generatedButtons.get(index).getId());
                            if (gameData.getMyLastHit()) {
                                tableService.setLangelioStatus(priesininkoTableData.getLentele(), generatedButtons.get(index).getId(), TableData.SET_SHIP);
                                gridButton.setStyleName("red");
                            } else {
                                gridButton.setStyleName("lightGray");
                                message.setValue("Priesininko ejimas");
                                tableService.setLangelioStatus(priesininkoTableData.getLentele(), generatedButtons.get(index).getId(), TableData.ILLEGAL_PLACE_FOR_SHIP);
                            }
                        }
                    }
                });
            }
        }
    }

    private void createGridLabels() {
        for (int i = 0; i < 40; i++) {
            Label numberLabel = new Label(String.valueOf(i % 10));
            numberLabel.setHeight(String.valueOf(SIZE_OF_BUTTONS));
            numberLabel.setWidth(String.valueOf(SIZE_OF_BUTTONS - 20));
            numberLabel.setStyleName("redLabel");
            layout.addComponent(numberLabel, "left: " + (SHIFT_TO_RIGHT - 20 + ((SIZE_OF_BUTTONS * 10 + 23) * ((i % 20) / 10)) + GAP_BETWEEN_TABLES * (i / 20)) + "px; top: " + (SHIFT_TO_DOWN - 5 + SIZE_OF_BUTTONS * (i % 10)) + "px;");
        }
        for (int i = 0; i < 40; i++) {
            Label charLabel = new Label(String.valueOf("KILOMETRAS".charAt(i % 10)));
            charLabel.setHeight(String.valueOf(SIZE_OF_BUTTONS - 10));
            charLabel.setWidth(String.valueOf(SIZE_OF_BUTTONS));
            charLabel.setStyleName("redLabel");
            layout.addComponent(charLabel, "left: " + (10 + SHIFT_TO_RIGHT + (SIZE_OF_BUTTONS * (i % 10)) + (GAP_BETWEEN_TABLES * (i / 20))) + "px; top: " + (SHIFT_TO_DOWN - 40 + (SIZE_OF_BUTTONS * 10 + 35) * ((i % 20) / 10)) + "px;");
        }
    }

    private void createLoginLogoutComponents() {
        Label userLabel = new Label("Name ");
        userLabel.setWidth("80px");
        userLabel.setHeight("25px");
        userLabel.setStyleName("redLabel");
        layout.addComponent(userLabel, "left: " + (SHIFT_TO_RIGHT + 660) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 40 + SHIFT_TO_DOWN) + "px;");

        TextField userText = new TextField();
        userText.setWidth("200px");
        userText.setHeight("25px");
        layout.addComponent(userText, "left: " + (755 + SHIFT_TO_RIGHT) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 50 + SHIFT_TO_DOWN) + "px;");

        Label emailLabel = new Label("Email ");
        emailLabel.setWidth("80px");
        emailLabel.setHeight("25px");
        emailLabel.setStyleName("redLabel");
        layout.addComponent(emailLabel, "left: " + (SHIFT_TO_RIGHT + 660) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 70 + SHIFT_TO_DOWN) + "px;");

        TextField emailText = new TextField();
        emailText.setWidth("200px");
        emailText.setHeight("25px");
        layout.addComponent(emailText, "left: " + (755 + SHIFT_TO_RIGHT) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 80 + SHIFT_TO_DOWN) + "px;");

        Button loginButton = new Button("login");
        loginButton.setWidth("80px");
        loginButton.setHeight("25px");
        loginButton.setStyleName("menuStyle");
        layout.addComponent(loginButton, "left: " + (780 + SHIFT_TO_RIGHT) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 120 + SHIFT_TO_DOWN) + "px;");
        loginButton.addClickListener(e -> {
            if (gameData.getStatus().equals(GameData.LOGIN)) {
                if (userText.getValue().trim().length() > 0) {
                    if (emailText.getValue().trim().length() > 0) {
                        if (emailText.getValue().trim().indexOf('@') > 0 && emailText.getValue().trim().indexOf('.') > 2 && emailText.getValue().trim().length() > 4) {
                            String name = userText.getValue().trim();
                            String email = emailText.getValue().trim();
                            user.setName(name);
                            user.setEmail(email);
                            if (userService.createUser(user)) {

                                layout.addComponent(messageLayout, "left: " + (SHIFT_TO_RIGHT - 30) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 35 + SHIFT_TO_DOWN) + "px;");
                                layout.addComponent(patvirtintiLaivus, "left: " + (SHIFT_TO_RIGHT + 350) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 35 + SHIFT_TO_DOWN) + "px;");
                                layout.addComponent(reset, "left: " + (SHIFT_TO_RIGHT + 350) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 70 + SHIFT_TO_DOWN) + "px;");
                                layout.addComponent(random, "left: " + (350 + SHIFT_TO_RIGHT) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 105 + SHIFT_TO_DOWN) + "px;");
                                layout.addComponent(bot, "left: " + (350 + SHIFT_TO_RIGHT) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 140 + SHIFT_TO_DOWN) + "px;");
                                layout.addComponent(cheatButton, "left: " + (350 + SHIFT_TO_RIGHT) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 175 + SHIFT_TO_DOWN) + "px;");

                                userLabel.setWidth("450px");
                                emailLabel.setWidth("450px");
                                userLabel.setValue("Name: " + user.getName());
                                emailLabel.setValue("Email:" + user.getEmail());

                                userText.setValue("");
                                layout.removeComponent(userText);
                                emailText.setValue("");
                                layout.removeComponent(emailText);
                                loginButton.setCaption("logout");
                            } else {
                                Notification.show("Nepavyko prisijungti"); //TODO sukurti vertima
                            }
                        } else {
                            Notification.show("Blogas emailo formatas");
                        }
                    } else Notification.show("Iveskite emaila");
                } else {
                    Notification.show("Iveskite varda");
                }
                return;
            }
            if (!gameData.getStatus().equals(GameData.LOGIN)) {
                resetGame();
                gameData.setStatus(GameData.LOGIN);
                user.setEmail("");
                user.setName("");
                user.setId("");

                layout.removeComponent(messageLayout);
                layout.removeComponent(patvirtintiLaivus);
                layout.removeComponent(reset);
                layout.removeComponent(random);
                layout.removeComponent(bot);
                layout.removeComponent(cheatButton);

                userLabel.setValue("Name ");
                emailLabel.setValue("Email ");

                layout.addComponent(userText, "left: " + (755 + SHIFT_TO_RIGHT) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 50 + SHIFT_TO_DOWN) + "px;"); //Todo sukurti pozicijos metoda kuriam paduotum skaicius ir grazintu string
                layout.addComponent(emailText, "left: " + (755 + SHIFT_TO_RIGHT) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 80 + SHIFT_TO_DOWN) + "px;");
                loginButton.setCaption("login");
            }
        });
    }

    private void createMenuButtons() {
        setMenuStyle(patvirtintiLaivus);
        patvirtintiLaivus.addClickListener(e -> {
            switch (patvirtintiLaivus.getCaption()) {
                case "Create new game":
                    if (tableService.arSustatytiManoLaivai(manoTableData.getLentele())) {
                        message.setValue("Galite spausti Join");
                    } else {
                        message.setValue("Uždekite keturvietį laivą");
                    }
                    patvirtintiLaivus.setCaption("Join");
                    break;
                case "Join":
                    if (tableService.arSustatytiManoLaivai(manoTableData.getLentele())) {
                        gameData = gameService.join(user.getId());
                        patvirtintiLaivus.setCaption("Exit game");
                    } else {
                        Notification.show("Sudekite visus laivus");
                    }
                    break;

                case "Exit game":
                    resetGame();
                    break;
            }
        });

        setMenuStyle(reset);
        reset.addClickListener(e -> {
            resetGame();
        });

        setMenuStyle(random);
        random.addClickListener(e -> {
            Random rand = new Random();
            while (!tableService.arSustatytiManoLaivai(manoTableData.getLentele())) {
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);
                String coordinate = "" + "KILOMETRAS".charAt(x) + y;
                if (tableService.getLangelioStatus(manoTableData.getLentele(), coordinate) == TableData.NO_SHIP) {
                    if (tableService.arSustatytiManoLaivai(manoTableData.getLentele())) {
                        break;
                    }
                    tableService.setLangelioStatus(manoTableData.getLentele(), coordinate, TableData.SET_SHIP);
                    tableService.paskaiciuotiKokioDidziausioLaivoTruksta(manoTableData.getLentele());
                    generatedButtons.get(x + y * 10).setStyleName("blue");
                }
                if (tableService.arSustatytiManoLaivai(manoTableData.getLentele())) {
                    break;
                }
            }
            if (patvirtintiLaivus.getCaption().equals("Create new game")) {
                message.setValue("Spauskite Create new game, jei norite pradeti zaidima");
            }
            if (patvirtintiLaivus.getCaption().equals("Join")) {
                message.setValue("Galite spausti Join");
            }
        });

        setMenuStyle(bot);
        bot.addClickListener(e -> {
            if (botStatus.equals(BOT_OFF)) {
                botStatus = BOT_ON;
                bot.setCaption("Disable bot");
                bot.setStyleName("menuStyleOn");
                return;
            }
            if (botStatus.equals(BOT_ON)) {
                botStatus = BOT_OFF;
                bot.setCaption("Enable bot");
                bot.setStyleName("menuStyle");
            }
        });

        setMenuStyle(cheatButton);
        cheatButton.addClickListener(e -> {
            if (cheatstatus.equals(CHEATS_ENABLED)) {
                cheatstatus = CHEATS_DISABLED;
                cheatButton.setCaption("Enable Cheats");
                cheatButton.setStyleName("menuStyle");
                return;
            }
            if (cheatstatus.equals(CHEATS_DISABLED)) {
                cheatstatus = CHEATS_ENABLED;
                cheatButton.setCaption("Disable Cheats");
                cheatButton.setStyleName("menuStyleOn");
            }
        });

        statusButton.setWidth("120px");
        statusButton.setHeight("30px");
        statusButton.setStyleName("menuStyle");
        statusButton.addClickListener(e -> {
            gameData = gameService.status(user.getId(), gameData.getGameID());
        });
        layout.addComponent(statusButton, "left: " + (150 + SHIFT_TO_RIGHT) + "px; top: " + (SIZE_OF_BUTTONS * 10 + 170 + SHIFT_TO_DOWN) + "px;");
    }

    private void setMenuStyle(Component component){
        component.setWidth("180px");
        component.setHeight("30px");
        component.setStyleName("menuStyle");
    }

    private void resetGame() {
        message.setValue("Spauskite Create new game, jei norite pradeti zaidima");
        manoTableData = new TableData();
        priesininkoTableData = new TableData();
        gameData = new GameData();
        gameData.setStatus(GameData.LOGIN);
        setupSent = false;
        patvirtintiLaivus.setCaption("Create new game");
        for (Button button : generatedButtons) {
            button.setStyleName(TRANSPARENT);
        }
    }

    private void spalvintiManoLentele() {
        for (int i = 0; i < 100; i++) {
            Button button = generatedButtons.get(i);
            int langelioStatus = tableService.getLangelioStatus(manoTableData.getLentele(), button.getId());
            switch (langelioStatus) {
                case 1:
                    updateComponent(button, SET_STYLE, "blue");
                    break;
                case 0:
                    updateComponent(button, SET_STYLE, TRANSPARENT);
                    break;
                case -1:
                    updateComponent(button, SET_STYLE, "darkGray");
                    break;
                case -2:
                    updateComponent(button, SET_STYLE, "yellow");
                    break;
                case -4:
                    updateComponent(button, SET_STYLE, "gray");
                    break;
            }
        }
    }

    private void spalvintiPriesininkoLentele() {
        for (int i = 100; i < 200; i++) {
            Button button = generatedButtons.get(i);
            int langelioStatus = tableService.getLangelioStatus(priesininkoTableData.getLentele(), button.getId());
            switch (langelioStatus) {
                case 1:
                    updateComponent(button, SET_STYLE, "red");
                    break;
                case 0:
                    updateComponent(button, SET_STYLE, TRANSPARENT);
                    break;
                case -1:
                    updateComponent(button, SET_STYLE, "darkGray");
                    break;
                case -2:
                    updateComponent(button, SET_STYLE, "yellow");
                    break;
                case -4:
                    updateComponent(button, SET_STYLE, "gray");
                    break;
                case -6:
                    updateComponent(button, SET_STYLE, "lightGreen");
                    break;
            }
        }
    }

    private void turnOnBot() {
        if (gameData.getStatus().equals(GameData.READY_TO_PLAY) && gameData.isMyTurn()) {
            Random rand = new Random();
            boolean hit = false;
            while (!hit) {
                int x = rand.nextInt(10);
                int y = rand.nextInt(10);
                String coordinate = "" + "KILOMETRAS".charAt(x) + y;
                if (tableService.getLangelioStatus(priesininkoTableData.getLentele(), coordinate) == TableData.NO_SHIP) {
                    hit = true;
                    gameData = gameService.turn(user.getId(), gameData.getGameID(), coordinate);
                    if (gameData.getMyLastHit()) {
                        tableService.setLangelioStatus(priesininkoTableData.getLentele(), coordinate, TableData.SET_SHIP);
                        tableService.paskaiciuotiKokioDidziausioLaivoTruksta(priesininkoTableData.getLentele());
                        botService.turnOnBotMethods(priesininkoTableData.getLentele());
                        spalvintiPriesininkoLentele();
                    } else {
                        updateComponent(message, SET_VALUE, "Priesininko ejimas");
                        tableService.setLangelioStatus(priesininkoTableData.getLentele(), coordinate, TableData.ILLEGAL_PLACE_FOR_SHIP);
                        tableService.paskaiciuotiKokioDidziausioLaivoTruksta(priesininkoTableData.getLentele());
                        botService.turnOnBotMethods(priesininkoTableData.getLentele());
                        if (cheatstatus.equals(CHEATS_ENABLED)) {
                            gameService.turnCheat(user.getPriesininkoid(), gameData.getGameID());
                        }
                        spalvintiPriesininkoLentele();
                    }
                }
            }
        }
    }

    public void checkIfFinished() {
        if (gameData.getStatus().equals(GameData.FINISHED)) {
            if (gameData.getWinnerId().equals(user.getId())) {
                updateComponent(message, SET_VALUE, "Jus laimejote");
            } else if (gameData.getWinnerId().trim().length() > 1) {
                updateComponent(message, SET_VALUE, "Jus pralaimejote");
            }
        }
    }

    public void spalvintiPriesininkoEjimus() {
        try {
            if (gameData.getPriesininkoEvents().size() > 0) {
                String paskutinioEjimokoordinate = "";
                for (lt.donatas.vaadin_laivu_musis.beans.Event event : gameData.getPriesininkoEvents()) {
                    String coordinate = event.getCoordinate();
                    paskutinioEjimokoordinate = coordinate.trim();
                    if (tableService.getLangelioStatus(manoTableData.getLentele(), paskutinioEjimokoordinate) > 0) {
                        updateComponent(generatedButtons.get("KILOMETRAS".indexOf(paskutinioEjimokoordinate.charAt(0)) + ((int) paskutinioEjimokoordinate.charAt(1) - 48) * 10), SET_STYLE, "red");
                    } else {
                        updateComponent(generatedButtons.get("KILOMETRAS".indexOf(paskutinioEjimokoordinate.charAt(0)) + ((int) paskutinioEjimokoordinate.charAt(1) - 48) * 10), SET_STYLE, "gray");
                    }
                }
                if (tableService.getLangelioStatus(manoTableData.getLentele(), paskutinioEjimokoordinate) > 0) {
                    updateComponent(generatedButtons.get("KILOMETRAS".indexOf(paskutinioEjimokoordinate.charAt(0)) + ((int) paskutinioEjimokoordinate.charAt(1) - 48) * 10), SET_STYLE, "darkRed");
                    showExplodeGif("KILOMETRAS".indexOf(paskutinioEjimokoordinate.charAt(0)) + ((int) paskutinioEjimokoordinate.charAt(1) - 48) * 10);
                } else {
                    updateComponent(generatedButtons.get("KILOMETRAS".indexOf(paskutinioEjimokoordinate.charAt(0)) + ((int) paskutinioEjimokoordinate.charAt(1) - 48) * 10), SET_STYLE, "darkGray");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void spalvintiPaskutiniManoEjima() {
        try {
            String paskutinioEjimokoordinate = "";
            for (lt.donatas.vaadin_laivu_musis.beans.Event event : gameData.getManoEvents()) {
                String coordinate = event.getCoordinate();
                paskutinioEjimokoordinate = coordinate.trim();
            }
            if (gameData.getStatus().equals(GameData.READY_TO_PLAY) && tableService.getLangelioStatus(priesininkoTableData.getLentele(), paskutinioEjimokoordinate) > 0) {
                showExplodeGif("KILOMETRAS".indexOf(paskutinioEjimokoordinate.charAt(0)) + ((int) paskutinioEjimokoordinate.charAt(1) - 48) * 10 + 100);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void startStatusAndComponentsUpdater() {
        new Thread(new MyThread()).start();
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            gameData.setStatus(GameData.LOGIN);
            while (true) {
                if (gameData.isMyTurn()) {
                    updateComponent(message, SET_VALUE, "Jusu ejimas");
                }
                if (!(gameData.getStatus().equals(GameData.READY_TO_PLAY) && cheatstatus.equals(CHEATS_ENABLED))) {
                    sleep(900);
                }
                if (botStatus.equals(BOT_ON)) {
                    turnOnBot();
                }
                checkIfFinished();
                if (gameData.getStatus().equals(GameData.READY_FOR_SECOND_PLAYER) || gameData.getStatus().equals(GameData.READY_FOR_SHIPS)) {
                    updateComponent(message, SET_VALUE, "Laukiama kito zaidejo");
                    gameData = gameService.status(user.getId(), gameData.getGameID());
                }

                if (gameData.getStatus().equals(GameData.READY_FOR_SHIPS) && !setupSent) {
                    String visiManoLaivai = tableService.convertShipsFromIntArrayToString(manoTableData.getLentele());
                    gameData = gameService.setup(user.getId(), gameData.getGameID(), visiManoLaivai);
                    setupSent = true;
                }

                if (gameData.getStatus().equals(GameData.READY_TO_PLAY) || (gameData.getStatus().equals(GameData.READY_FOR_SHIPS) && setupSent)) {

                    if (!gameData.isMyTurn()) {
                        updateComponent(message, SET_VALUE, "Priesininko ejimas");
                        gameData = gameService.status(user.getId(), gameData.getGameID());
                    } else {
                        updateComponent(message, SET_VALUE, "Jusu ejimas");
                    }
                    spalvintiPriesininkoEjimus();
                    spalvintiPaskutiniManoEjima();
                }
            }
        }
    }

    public void sleep(int miliSeconds) {
        try {
            Thread.sleep(miliSeconds);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void showExplodeGif(int i) {
        try {
            String positionOfGif = "";
            if (i < 100) {
                explodeGifs.get(indexOfExplodeGifs).setWidth("90px");
                explodeGifs.get(indexOfExplodeGifs).setHeight("90px");
                positionOfGif = "left: " + ((SHIFT_TO_RIGHT - 32 + (SIZE_OF_BUTTONS + GAP_BETWEEN_BUTTONS) * (i % 10)) + GAP_BETWEEN_TABLES * (i / 100)) + "px; top: " + (SHIFT_TO_DOWN - 50 + (SIZE_OF_BUTTONS + GAP_BETWEEN_BUTTONS) * ((i % 100) / 10)) + "px;";
            } else if (i < 200) {
                explodeGifs.get(indexOfExplodeGifs).setWidth("55px");
                explodeGifs.get(indexOfExplodeGifs).setHeight("55px");
                positionOfGif = "left: " + ((SHIFT_TO_RIGHT - 10 + (SIZE_OF_BUTTONS + GAP_BETWEEN_BUTTONS) * (i % 10)) + GAP_BETWEEN_TABLES * (i / 100)) + "px; top: " + (SHIFT_TO_DOWN - 20 + (SIZE_OF_BUTTONS + GAP_BETWEEN_BUTTONS) * ((i % 100) / 10)) + "px;";
            }
            updateComponent(explodeGifs.get(indexOfExplodeGifs), ADD_COMPONENT, positionOfGif, 1);
            updateComponent(explodeGifs.get(indexOfExplodeGifs), REMOVE_COMPONENT, null, 2500);
            indexOfExplodeGifs++;
            if (indexOfExplodeGifs > explodeGifs.size() - 1) {
                indexOfExplodeGifs = 0;
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void showClickExplodeGif(int i) {
        try {
            Component explode2 = new Image(null, new ThemeResource("images/explode4.gif"));
            String position = "left: " + ((SHIFT_TO_RIGHT + (SIZE_OF_BUTTONS + GAP_BETWEEN_BUTTONS) * (i % 10)) + GAP_BETWEEN_TABLES * (i / 100)) + "px; top: " + (SHIFT_TO_DOWN - 12 + (SIZE_OF_BUTTONS + GAP_BETWEEN_BUTTONS) * ((i % 100) / 10)) + "px;";
            updateComponent(explode2, ADD_COMPONENT, position, 1);
            updateComponent(explode2, REMOVE_COMPONENT, null, 1600);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }


    public void updateComponent(Component obj, String method, String position, int miliSeconds) {
        new Thread(new Updater(obj, method, position, miliSeconds)).start();
    }

    public void updateComponent(Component obj, String method, String styleOrCaptionOrValue) {
        new Thread(new Updater(obj, method, styleOrCaptionOrValue)).start();
    }

    public class Updater implements Runnable {
        Component obj;
        String method;
        int miliSeconds;
        String styleOrCaptionOrValue;
        String position;

        Updater(Component obj, String method, String position, int miliSeconds) {
            this.obj = obj;
            this.method = method;
            this.position = position;
            this.miliSeconds = miliSeconds;
        }

        Updater(Component obj, String method, String styleOrCaptionOrValue) {
            this.obj = obj;
            this.method = method;
            this.styleOrCaptionOrValue = styleOrCaptionOrValue;
            this.miliSeconds = 1;
        }

        @Override
        public void run() {
            sleep(miliSeconds);
            access(new Runnable() {
                @Override
                public void run() {
                    switch (method) {
                        case ADD_COMPONENT:
                            layout.addComponent(obj, position);
                            break;
                        case REMOVE_COMPONENT:
                            layout.removeComponent(obj);
                            break;
                        case SET_STYLE:
                            obj.setStyleName(styleOrCaptionOrValue);
                            break;
                        case SET_CAPTION:
                            obj.setCaption(styleOrCaptionOrValue);
                            break;
                        case SET_VALUE:
                            ((Label) obj).setValue(styleOrCaptionOrValue);
                            break;
                    }
                }
            });
        }
    }
}
