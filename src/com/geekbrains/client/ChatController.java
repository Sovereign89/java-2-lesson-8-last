package com.geekbrains.client;

import com.geekbrains.server.ServerCommandConstants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

import static com.sun.glass.events.MouseEvent.BUTTON_LEFT;

public class ChatController implements Initializable {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField messageField, loginField;
    @FXML
    private HBox messagePanel, authPanel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ListView<String> clientList;

    private final Network network;

    public ChatController() {
        this.network = new Network(this);
    }

    public void setAuthenticated(boolean authenticated) {
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        messagePanel.setVisible(authenticated);
        messagePanel.setManaged(authenticated);
        clientList.setVisible(authenticated);
        clientList.setManaged(authenticated);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setAuthenticated(false);
    }

    public void displayMessage(String text) {
        if (textArea.getText().isEmpty()) {
            textArea.setText(text);
        } else {
            textArea.setText(textArea.getText() + "\n" + text);
        }
    }

    public void displayMessage(String text, String color) {
        if (textArea.getText().isEmpty()) {
            textArea.setText(text);
        } else {
            textArea.setText(textArea.getText() + "\n" + text);
        }
    }

    public void displayClient(String nickName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientList.getItems().add(nickName);
            }
        });
    }

    public void removeClient(String nickName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientList.getItems().remove(nickName);
            }
        });
    }


    public void sendAuth(ActionEvent event) {
        boolean authenticated = network.sendAuth(loginField.getText(), passwordField.getText());
        if(authenticated) {
            loginField.clear();
            passwordField.clear();
            setAuthenticated(true);
        }
    }

    public void sendMessage(ActionEvent event) {
        if (clientList.getSelectionModel().getSelectedItem().isEmpty()) {
            network.sendMessage(messageField.getText());
        } else {
            network.sendMessage(ServerCommandConstants.PRIVATE_MESSAGE + " " + clientList.getSelectionModel().getSelectedItem().replace("\n","") + " " + messageField.getText());
        }
        messageField.clear();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            System.out.println("Выбран " + clientList.getSelectionModel().getSelectedItem());
        } else {
            clientList.getSelectionModel().clearSelection();
        }
    }

    public void close() {
        network.closeConnection();
    }
}
