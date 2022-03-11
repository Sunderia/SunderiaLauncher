package fr.sunderia.sunderialauncher.ui.panels.pages.content;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.sunderia.sunderialauncher.SunderiaLauncher;
import fr.sunderia.sunderialauncher.ui.PanelManager;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Settings extends ContentPanel {

    private final Saver saver = SunderiaLauncher.getInstance().getSaver();
    private final GridPane contentPane = new GridPane();

    @Override
    public String getName() {
        return "Settings";
    }

    @Override
    public String getStylesheetPath() {
        return "css/content/settings.css";
    }

    @Override
    public void init(PanelManager manager) {
        super.init(manager);

        //Background
        this.layout.getStyleClass().add("settings-layout");
        this.layout.setPadding(new Insets(40));
        setCanTakeAllSize(this.layout);

        //Content
        contentPane.getStyleClass().add("content-pane");
        setCanTakeAllSize(contentPane);
        this.layout.getChildren().add(contentPane);

        //Title
        Label title = new Label("Paramètres");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 25F));
        title.getStyleClass().add("settings-title");
        setLeft(title);
        setCanTakeAllSize(title);
        setTop(title);
        title.setTextAlignment(TextAlignment.LEFT);
        title.setTranslateX(25d);
        title.setTranslateY(40d);
        contentPane.getChildren().add(title);

        //RAM
        Label ramLabel = new Label("Mémorire max");
        ramLabel.getStyleClass().add("settings-labels");
        setLeft(ramLabel);
        setCanTakeAllSize(ramLabel);
        setTop(ramLabel);
        ramLabel.setTextAlignment(TextAlignment.LEFT);
        ramLabel.setTranslateX(25d);
        ramLabel.setTranslateY(100d);
        contentPane.getChildren().add(ramLabel);

        //Ram selector Slider
        SystemInfo info = new SystemInfo();
        GlobalMemory memory = info.getHardware().getMemory();
        AtomicReference<Double> val = new AtomicReference<>((double) 1024);
        try {
            val.set(Double.parseDouble(saver.get("maxRam")));
        } catch (NumberFormatException e) {
            saver.set("maxRam", String.valueOf(val.get()));
            saver.save();
        }
        Slider slider = new Slider(512, Math.ceil(memory.getTotal() / Math.pow(1024, 2)), val.get());
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1024);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1024);
        slider.setSnapToTicks(true);
        slider.setLabelFormatter(new StringConverter<>() {

            @Override
            public String toString(Double object) {
                return String.format("%.1f Go", object / 1024.0);
            }

            @Override
            public Double fromString(String string) {
                return Double.parseDouble(string.replace(" Go", "")) * 1024.0;
            }
        });
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(Objects.equals(oldValue, newValue)) return;
            double value = slider.getValue();
            val.set(value);
        });

        setLeft(slider);
        setTop(slider);
        setCanTakeAllSize(slider);
        slider.setTranslateX(35d);
        slider.setTranslateY(130d);
        contentPane.getChildren().add(slider);

        //Save button
        Button saveButton = new Button("Sauvegarder");
        saveButton.getStyleClass().add("save-btn");
        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.SAVE);
        iconView.getStyleClass().add("save-icon");
        saveButton.setGraphic(iconView);
        setCanTakeAllSize(saveButton);
        setBottom(saveButton);
        setCenterH(saveButton);
        saveButton.setOnMouseClicked(e -> {
            saver.set("maxRam", String.valueOf(slider.getValue()));
            saver.save();
        });
        contentPane.getChildren().add(saveButton);
    }
}
