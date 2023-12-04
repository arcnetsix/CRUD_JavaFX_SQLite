package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import dao.UsuarioDAO;





import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField textLogin;

    @FXML
    private TextField textPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCancel;

    @FXML
    private void login() {
        try {
            String login = textLogin.getText();
            String senha = textPassword.getText();

            if ("usuario".equals(login) && "123".equals(senha)) {
                System.out.println("Login bem-sucedido!");
                abrirNovaJanela("/app/cadMotorista.fxml", "Cadastro de Motorista!");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Falha no Login");
                alert.setHeaderText(null);
                alert.setContentText("Usuário ou senha incorretos. Tente novamente.");


                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel() {
        System.out.println("Operação de cancelamento.");
    }

    private void abrirNovaJanela(String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();

        Stage loginStage;
        loginStage = (Stage) btnLogin.getScene().getWindow();
        loginStage.close();
    }





    @FXML
    public void pesquisarCadastro(ActionEvent actionEvent) {

    }

}
