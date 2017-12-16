package org.triplea.client.ui.javafx;

import java.io.IOException;
import java.util.Arrays;

import games.strategy.triplea.settings.SettingType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

class SettingsPane extends StackPane {
  private final TripleA triplea;

  @FXML
  private TabPane tabPane;


  /**
   * @param triplea The root pane.
   * @throws IOException If the FXML file is not present.
   */
  public SettingsPane(final TripleA triplea) throws IOException {
    final FXMLLoader loader = FxmlManager.getLoader(getClass().getResource(FxmlManager.SETTINGS_PANE.toString()));
    loader.setRoot(this);
    loader.setController(this);
    loader.load();
    this.triplea = triplea;
    Arrays.stream(SettingType.values()).forEach(type -> {
      final Tab tab = new Tab(type.toString());
      final GridPane pane = new GridPane();
      tab.setContent(new ScrollPane(pane));
      Arrays.stream(ClientSettingJavaFxUiBinding.values())
          .filter(b -> b.getCategory() == type)
          .forEach(b -> {
            final Label description = new Label();
            final Node node = b.buildSelectionComponent();
            description.setText(loader.getResources().getString(
                getSettingLocalizationKey(node, b.name().toLowerCase())));
            pane.addColumn(0, description);
            pane.addColumn(1, node);
          });
      tabPane.getTabs().add(tab);
    });
  }

  @FXML
  private void back() {
    // TODO check if some changes haven't been saved
    triplea.returnToMainMenu(this);
  }

  @FXML
  private void reset() {}

  @FXML
  private void resetToDefault() {}

  @FXML
  private void save() {}


  private static String getSettingLocalizationKey(final Node rootNode, final String name) {
    return "settings." + rootNode.getClass().getSimpleName().toLowerCase() + "." + name;
  }
}
