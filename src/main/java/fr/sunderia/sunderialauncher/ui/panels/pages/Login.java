package fr.sunderia.sunderialauncher.ui.panels.pages;

import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import fr.sunderia.sunderialauncher.SunderiaLauncher;
import fr.sunderia.sunderialauncher.ui.PanelManager;
import fr.sunderia.sunderialauncher.ui.panel.Panel;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Login extends Panel {
    private final GridPane loginCard = new GridPane();
    private final Saver saver = SunderiaLauncher.getInstance().getSaver();
    private final TextField userField = new TextField();
    private final PasswordField passField = new PasswordField();
    private final Label userErrorLabel = new Label();
    private final Label passErrorLabel = new Label();
    private final Button btnLogin = new Button("Connexion");
    private final Button msLoginBtn = new Button();

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public String getStylesheetPath() {
        return "css/login.css";
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);

        //Background
        this.layout.getStyleClass().add("login-layout");

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.LEFT);
        columnConstraints.setMaxWidth(350);
        columnConstraints.setMinWidth(350);
        this.layout.getColumnConstraints().addAll(columnConstraints, new ColumnConstraints());
        this.layout.add(loginCard, 0, 0);

        //Background Image
        GridPane bgImage = new GridPane();
        setCanTakeAllSize(bgImage);
        bgImage.getStyleClass().add("bg-image");
        this.layout.add(bgImage, 1, 0);

        //Login card
        setCanTakeAllSize(this.layout);
        loginCard.getStyleClass().add("login-card");
        setLeft(loginCard);
        setCenterH(loginCard);
        setCenterV(loginCard);

        //Login sideBar
        Label title = new Label("Sunderia Launcher");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 30F));
        title.getStyleClass().add("login-title");
        setCenterH(title);
        setCanTakeAllSize(title);
        setTop(title);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(30D);
        loginCard.getChildren().add(title);

        //Logo
        ImageView logo = new ImageView("images/icon.png");
        logo.setFitWidth(150);
        logo.setFitHeight(150);
        logo.setPreserveRatio(true);
        setCenterH(logo);
        setCanTakeAllSize(logo);
        setTop(logo);
        loginCard.getChildren().add(logo);
        logo.setTranslateY(80D);

        //Email
        setCanTakeAllSize(userField);
        setCenterV(userField);
        setCenterH(userField);
        userField.setPromptText("Adresse E-mail");
        userField.setMaxWidth(300);
        userField.setTranslateY(-70d);
        userField.getStyleClass().add("login-input");
        userField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(Boolean.FALSE.equals(newValue)) this.updateLoginBtn(userField, userErrorLabel);
        });

        //User error
        setCanTakeAllSize(userErrorLabel);
        setCenterV(userErrorLabel);
        setCenterH(userErrorLabel);
        userErrorLabel.getStyleClass().add("login-error");
        userErrorLabel.setTranslateY(-45d);
        userErrorLabel.setMaxWidth(280);
        userErrorLabel.setTextAlignment(TextAlignment.LEFT);

        //Password
        setCanTakeAllSize(passField);
        setCenterV(passField);
        setCenterH(passField);
        passField.setPromptText("Mot de passe");
        passField.setMaxWidth(300);
        passField.setTranslateY(-15d);
        passField.getStyleClass().add("login-input");
        passField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(Boolean.FALSE.equals(newValue)) this.updateLoginBtn(passField, passErrorLabel);
        });

        //Password error
        setCanTakeAllSize(passErrorLabel);
        setCenterV(passErrorLabel);
        setCenterH(passErrorLabel);
        passErrorLabel.getStyleClass().add("login-error");
        passErrorLabel.setTranslateY(10d);
        passErrorLabel.setMaxWidth(280);
        passErrorLabel.setTextAlignment(TextAlignment.LEFT);

        //Login button
        setCanTakeAllSize(btnLogin);
        setCenterV(btnLogin);
        setCenterH(btnLogin);
        btnLogin.setDisable(true);
        btnLogin.setMaxWidth(300);
        btnLogin.setTranslateY(40d);
        btnLogin.getStyleClass().add("login-log-btn");
        btnLogin.setOnMouseClicked(event -> authenticate(userField.getText(), passField.getText()));

        Separator separator = new Separator();
        setCanTakeAllWidth(separator);
        setCenterV(separator);
        setCenterH(separator);
        separator.getStyleClass().add("login-separator");
        separator.setMaxWidth(300);
        separator.setTranslateY(90d);

        //"Login with" label
        Label loginWithLabel = new Label("Ou se connecter avec".toUpperCase());
        setCanTakeAllSize(loginWithLabel);
        setCenterV(loginWithLabel);
        setCenterH(loginWithLabel);
        loginWithLabel.setFont(Font.font(loginWithLabel.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, 14F));
        loginWithLabel.getStyleClass().add("login-with-label");
        loginWithLabel.setTranslateY(110d);
        loginWithLabel.setMaxWidth(280d);

        //Microsoft login button
        ImageView view = new ImageView("images/microsoft.png");
        view.setPreserveRatio(true);
        view.setFitHeight(30D);
        setCanTakeAllSize(msLoginBtn);
        setCenterH(msLoginBtn);
        setCenterV(msLoginBtn);
        msLoginBtn.getStyleClass().add("ms-login-btn");
        msLoginBtn.setMaxWidth(300);
        msLoginBtn.setTranslateY(145d);
        msLoginBtn.setGraphic(view);
        msLoginBtn.setOnMouseClicked(event -> authenticateMS());

        loginCard.getChildren().addAll(userField, userErrorLabel, passField, passErrorLabel, btnLogin, separator, loginWithLabel, msLoginBtn);
    }

    public void updateLoginBtn(TextField textField, Label errorLabel) {
        if(textField.getText().isEmpty()) {
            errorLabel.setText("Le champ ne peut être vide.");
        } else {
            errorLabel.setText("");
        }

        btnLogin.setDisable(userField.getText().isEmpty() || passField.getText().isEmpty());
    }

    public void authenticate(String user, String password) {
        Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);

        try {
            AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, user, password, null);
            saver.set("accessToken", response.getAccessToken());
            saver.set("clientToken", response.getClientToken());
            saver.save();
            AuthInfos infos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getClientToken(), response.getSelectedProfile().getId());

            SunderiaLauncher.getInstance().setAuthInfo(infos);

            /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Authentification réussie");
            alert.setHeaderText("Bienvenue " + response.getSelectedProfile().getName() + " sur Sunderia !");
            alert.setContentText("Vous êtes maintenant connecté.");
            alert.showAndWait();*/

            panelManager.showPanel(new HomePage());
        } catch (AuthenticationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue lors de l’authentification.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void authenticateMS() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        authenticator.loginWithAsyncWebview().whenComplete((response, error) -> {
            if (error != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(error.getMessage());
                alert.show();
                return;
            }

            saver.set("msAccessToken", response.getAccessToken());
            saver.set("msRefreshToken", response.getRefreshToken());
            saver.save();
            SunderiaLauncher.getInstance().setAuthInfo(new AuthInfos(
                    response.getProfile().getName(),
                    response.getAccessToken(),
                    response.getProfile().getId()
            ));
            this.logger.info("Hello " + response.getProfile().getName());
            Platform.runLater(() -> panelManager.showPanel(new HomePage()));
        });
    }
}