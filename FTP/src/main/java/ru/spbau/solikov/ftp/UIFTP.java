package ru.spbau.solikov.ftp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A user interface of frp-client that allows to traverse and download files from directories.
 * To go to the parent folder use "Up Folder" button. When you want to download some file from current folder, hit
 * "Download/Browse" button and after that select a file or hit "Download/Browse" button again to continue browsing.
 */
public class UIFTP extends Application {

    private FTPClient client;
    private Path current;
    private BorderPane root;
    private Button download;
    private TableView<FTPFile> table = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        TextField address = new TextField();
        address.setPromptText("Address:");
        TextField port = new TextField();
        port.setPromptText("Port:");
        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            try {
                client = new FTPClient(Integer.parseInt(address.getText()),
                        port.getText());
                setUpApp(primaryStage);
            } catch (NumberFormatException n) {
                showAlert("Bad format. Try again.");
            } catch (IOException ex) {
                showAlert("Can't connect to the server. Try again.");
            }
        });
        address.setMaxSize(200, 10);
        port.setMaxSize(200, 10);
        VBox vBox = new VBox(submit, address, port);
        vBox.setAlignment(Pos.CENTER);
        Scene connecting = new Scene(vBox, 500, 500);
        primaryStage.setTitle("FTP Client");
        primaryStage.setScene(connecting);
        primaryStage.show();
    }

    private void setUpApp(Stage primaryStage) {
        primaryStage.setTitle("FTP Client");
        table.setEditable(true);
        table.setPrefSize(500, 500);
        TableColumn<FTPFile, String> files = new TableColumn<>("Files");
        TableColumn<FTPFile, String> isDir = new TableColumn<>("Is directory");
        files.setCellValueFactory(new PropertyValueFactory<>("name"));
        files.setPrefWidth(250);
        isDir.setCellValueFactory(new PropertyValueFactory<>("dir"));
        isDir.setPrefWidth(250);
        table.getColumns().add(files);
        table.getColumns().add(isDir);
        download = new Button("Download");
        Button up = new Button("Up Folder");
        download.setAlignment(Pos.CENTER);
        up.setAlignment(Pos.CENTER);
        download.setOnAction(event -> setDownload());
        up.setOnAction(event -> {
            stepBack();
            reBuild("");
        });
        HBox hBox = new HBox(download, up);
        hBox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(download, Pos.CENTER);
        root = new BorderPane();
        root.setTop(hBox);
        current = Paths.get(System.getProperty("user.dir"));
        setScene(current);
        setBrowse();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }

    private void setDownload() {
        download.setStyle("-fx-background-color: rgba(43,46,53,0.19)");
        download.setOnAction(event -> setBrowse());
        download.setText("Browse");
        table.setOnMouseClicked(e -> {
            FTPFile ftp = table.getSelectionModel().getSelectedItem();
            if (ftp == null) return;
            if (!ftp.getDir()) {
                try {
                    client.get(current + "/" + ftp.getName());
                    showAlert("Downloaded!");
                } catch (IOException ex) {
                    showAlert("Sorry, can't download the file : " + ex.getMessage());
                }
            }
        });
    }

    private void setBrowse() {
        download.setText("Download");
        download.setStyle("");
        download.setOnAction(event -> setDownload());
        table.setOnMouseClicked(e -> {
            FTPFile ftp = table.getSelectionModel().getSelectedItem();
            if (ftp == null) return;
            if (ftp.getDir()) {
                reBuild(ftp.getName());
            }
        });
    }

    private void setScene(Path path) {
        try {
            table.setItems(FXCollections.observableArrayList(client.list(path.toString())));
        } catch (IOException e) {
            showAlert("Can't get list of files");
            e.printStackTrace();
        }
        root.setCenter(table);
    }

    private void stepBack() {
        if (current.getParent() != null) current = current.getParent();
    }

    private void reBuild(String name) {
        current = Paths.get(current + "/" + name);
        setScene(current);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * To start the UI run that method.
     *
     * @param args host name and port number
     */
    public static void main(String[] args) {
        launch(args);
    }
}
