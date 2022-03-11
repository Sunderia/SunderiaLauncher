package fr.sunderia.sunderialauncher.ui.panels.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.sunderia.sunderialauncher.SunderiaLauncher;
import fr.sunderia.sunderialauncher.ui.PanelManager;
import fr.sunderia.sunderialauncher.ui.panel.IPanel;
import fr.sunderia.sunderialauncher.ui.panel.Panel;
import fr.sunderia.sunderialauncher.ui.panels.pages.content.Settings;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HomePage extends Panel {

    private final GridPane sideMenu = new GridPane();
    private final GridPane navContent = new GridPane();
    private final Saver saver = SunderiaLauncher.getInstance().getSaver();

    private Node activeLink;
    private Button homeBtn, settingsBtn;

    @Override
    public String getName() {
        return "Home";
    }

    @Override
    public String getStylesheetPath() {
        return "css/home.css";
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);

        //Background
        this.layout.getStyleClass().add("home-layout");
        setCanTakeAllSize(this.layout);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.LEFT);
        columnConstraints.setMinWidth(350);
        columnConstraints.setMaxWidth(350);
        this.layout.getColumnConstraints().addAll(columnConstraints, new ColumnConstraints());

        //Side Menu
        this.layout.add(this.sideMenu, 0, 0);
        this.sideMenu.getStyleClass().add("sidemenu");
        setLeft(this.sideMenu);
        setCenterH(this.sideMenu);
        setCenterV(this.sideMenu);

        //Background image
        GridPane bgImage = new GridPane();
        setCanTakeAllSize(bgImage);
        bgImage.getStyleClass().add("bg-image");
        this.layout.add(bgImage, 1, 0);

        //Nav content
        this.layout.add(this.navContent, 1, 0);
        this.navContent.getStyleClass().add("nav-content");
        setCenterH(this.navContent);
        setCenterV(this.navContent);
        setLeft(this.navContent);

        //Side menu
        //Title
        Label title = new Label("Sunderia Launcher");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 30F));
        title.getStyleClass().add("home-title");
        setCenterH(title);
        setCanTakeAllSize(title);
        setTop(title);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(30d);
        this.sideMenu.getChildren().add(title);

        //Navigation
        this.homeBtn = new Button("Accueil");
        this.homeBtn.getStyleClass().add("sidemenu-nav-btn");
        this.homeBtn.setGraphic(setIconColor(new FontAwesomeIconView(FontAwesomeIcon.HOME), Color.WHITE));
        setCanTakeAllSize(this.homeBtn);
        setTop(this.homeBtn);
        this.homeBtn.setTranslateY(90d);
        this.homeBtn.setOnMouseClicked(event -> setPage(null, homeBtn));

        this.settingsBtn = new Button("Paramètres");
        this.settingsBtn.getStyleClass().add("sidemenu-nav-btn");
        this.settingsBtn.setGraphic(setIconColor(new FontAwesomeIconView(FontAwesomeIcon.GEARS), Color.WHITE));
        setCanTakeAllSize(this.settingsBtn);
        setTop(this.settingsBtn);
        this.settingsBtn.setTranslateY(130d);
        this.settingsBtn.setOnMouseClicked(event -> setPage(new Settings(), settingsBtn));

        this.sideMenu.getChildren().addAll(this.homeBtn, this.settingsBtn);

        //Username + avatar
        GridPane userPane = new GridPane();
        setCanTakeAllWidth(userPane);
        userPane.setMaxHeight(80);
        userPane.setMinWidth(80);
        userPane.getStyleClass().add("user-pane");
        setBottom(userPane);

        String avatarUrl = "https://minotar.net/avatar/" + SunderiaLauncher.getInstance().getAuthInfos().getUuid() + "/50.png";
        ImageView avatarView = new ImageView();
        Image avatarImg = new Image(avatarUrl);
        avatarView.setImage(avatarImg);
        avatarView.setPreserveRatio(true);
        avatarView.setFitHeight(50d);
        setCenterV(avatarView);
        setCanTakeAllSize(avatarView);
        setLeft(avatarView);
        avatarView.setTranslateX(15d);

        userPane.getChildren().add(avatarView);

        Label usernameLabel = new Label(SunderiaLauncher.getInstance().getAuthInfos().getUsername());
        usernameLabel.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 25f));
        setCanTakeAllSize(usernameLabel);
        setCenterV(usernameLabel);
        setLeft(usernameLabel);
        usernameLabel.getStyleClass().add("username-label");
        usernameLabel.setTranslateX(75d);
        setCanTakeAllWidth(usernameLabel);

        userPane.getChildren().add(usernameLabel);

        Button logoutBtn = new Button();
        FontAwesomeIconView logoutIcon = new FontAwesomeIconView(FontAwesomeIcon.SIGN_OUT);
        logoutIcon.getStyleClass().add("logout-icon");
        setCanTakeAllSize(logoutBtn);
        setCenterV(logoutBtn);
        setRight(logoutBtn);
        logoutBtn.getStyleClass().add("logout-btn");
        logoutBtn.setGraphic(logoutIcon);
        logoutBtn.setOnMouseClicked(event -> {
            saver.remove("accessToken");
            saver.remove("clientToken");
            saver.remove("msAccessToken");
            saver.remove("msRefreshToken");
            saver.save();
            SunderiaLauncher.getInstance().setAuthInfo(null);
            this.panelManager.showPanel(new Login());
        });

        userPane.getChildren().add(logoutBtn);

        sideMenu.getChildren().add(userPane);
    }

    private FontAwesomeIconView setIconColor(FontAwesomeIconView fontAwesomeIconView, Color color) {
        fontAwesomeIconView.setFill(color);
        return fontAwesomeIconView;
    }

    @Override
    public void onShow() {
        super.onShow();
        setPage(null, this.homeBtn);
    }

    public void setPage(IPanel panel, Node navBtn) {
        if(this.activeLink != null) this.activeLink.getStyleClass().remove("active");
        activeLink = navBtn;
        activeLink.getStyleClass().add("active");

        this.navContent.getChildren().clear();
        if(panel != null) {
            this.navContent.getChildren().add(panel.getLayout());
            if(panel.getStylesheetPath() != null) {
                this.panelManager.getStage().getScene().getStylesheets().clear();
                this.panelManager.getStage().getScene().getStylesheets().addAll(this.getStylesheetPath(), panel.getStylesheetPath());
            }
            panel.init(this.panelManager);
            panel.onShow();
        }
    }
}
