package fr.sunderia.sunderialauncher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.model.response.RefreshResponse;
import fr.sunderia.sunderialauncher.ui.PanelManager;
import fr.sunderia.sunderialauncher.ui.panels.pages.HomePage;
import fr.sunderia.sunderialauncher.ui.panels.pages.Login;
import fr.sunderia.sunderialauncher.utils.Helpers;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;

public class SunderiaLauncher extends Application {

    private static SunderiaLauncher instance;
    private final ILogger logger;
    private final File launcherDir = Helpers.generateGamePath("SL");
    private final Saver saver;
    private PanelManager panelManager;
    private AuthInfos authInfos;


    public SunderiaLauncher() {
        instance = this;
        this.logger = new Logger("[SL]", new File(launcherDir, "launcher.log").toPath());
        if (!this.launcherDir.exists() && !this.launcherDir.mkdirs()) {
            this.logger.err("Unable to create launcher directory");
        }

        this.saver = new Saver(new File(launcherDir, "config.properties").toPath());

        saver.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.logger.info("Starting launcher...");
        this.panelManager = new PanelManager(this, primaryStage);
        this.panelManager.init();
        //Files.newBufferedReader(new File(launcherDir, "config.properties").toPath()).lines().forEach(this.logger::info);

        if(!this.isUserAlreadyLoggedIn()){
            this.panelManager.showPanel(new Login());
        } else {
            this.logger.info("Hello " + this.authInfos.getUsername());
            this.panelManager.showPanel(new HomePage());
        }
    }

    public boolean isUserAlreadyLoggedIn() {
        if(saver.get("accessToken") != null && saver.get("clientToken") != null) {
            Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
            try {
                RefreshResponse response = authenticator.refresh(saver.get("accessToken"), saver.get("clientToken"));
                saver.set("accessToken", response.getAccessToken());
                saver.set("clientToken", response.getClientToken());
                saver.save();
                this.authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getClientToken(), response.getSelectedProfile().getId());
                return true;
            } catch (AuthenticationException ignored) {
                saver.remove("accessToken");
                saver.remove("clientToken");
                saver.save();
            }
        } else if(saver.get("msAccessToken") != null && saver.get("msRefreshToken") != null) {
            try {
                MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                MicrosoftAuthResult response = authenticator.loginWithRefreshToken(saver.get("msRefreshToken"));
                saver.set("msAccessToken", response.getAccessToken());
                saver.set("msRefreshToken", response.getRefreshToken());
                saver.save();
                this.authInfos = new AuthInfos(response.getProfile().getName(), response.getAccessToken(), response.getProfile().getId());
                return true;
            } catch (MicrosoftAuthenticationException e) {
                saver.remove("msAccessToken");
                saver.remove("msRefreshToken");
                saver.save();
                logger.err(e.getMessage());
            }
        }
        return false;
    }

    public ILogger getLogger() {
        return logger;
    }

    public static SunderiaLauncher getInstance() {
        return instance;
    }

    public Saver getSaver() {
        return saver;
    }

    public AuthInfos getAuthInfos() {
        return authInfos;
    }

    public void setAuthInfo(AuthInfos selectedProfile) {
        this.authInfos = selectedProfile;
    }
}
