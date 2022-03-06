package fr.sunderia.sunderialauncher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.litarvan.openauth.AuthPoints;
import fr.litarvan.openauth.AuthenticationException;
import fr.litarvan.openauth.Authenticator;
import fr.litarvan.openauth.model.AuthProfile;
import fr.litarvan.openauth.model.response.RefreshResponse;
import fr.sunderia.sunderialauncher.ui.PanelManager;
import fr.sunderia.sunderialauncher.ui.panels.pages.Login;
import fr.sunderia.sunderialauncher.utils.Helpers;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class SunderiaLauncher extends Application {

    private static SunderiaLauncher instance;
    private final ILogger logger;
    private final File launcherDir = Helpers.generateGamePath("SL");
    private final Saver saver;
    private PanelManager panelManager;
    private AuthProfile authProfile;


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

        if(!this.isUserAlreadyLoggedIn()){
            this.panelManager.showPanel(new Login());
        } else {
            logger.info("Hello " + this.authProfile.getName());
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
                this.authProfile = response.getSelectedProfile();
                return true;
            } catch (AuthenticationException ignored) {
                saver.remove("accessToken");
                saver.remove("clientToken");
                saver.save();
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

    public AuthProfile getAuthProfile() {
        return authProfile;
    }

    public void setAuthProfile(AuthProfile selectedProfile) {
        this.authProfile = selectedProfile;
    }
}
