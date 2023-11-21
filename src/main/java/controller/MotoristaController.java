package controller;

import dao.MotoristaDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Motorista;

import java.sql.SQLException;
import java.util.Date;

public class MotoristaController {
    @FXML
    Button btnSalvar, btnPesquisar;
    @FXML
    TextField textNome, textCPF, textCNH, textData, textPesquisar;

    @FXML
    public void salvarCadastro() throws SQLException {
        // Criar um objeto motorista
        Motorista motorista = new Motorista();
        // Pegar dados da tela
        motorista.setNome(textNome.getText());
        motorista.setDataCadastro(new Date());
        motorista.setCnh(textCNH.getText());
        motorista.setCpf(textCPF.getText());
        motorista.setIdCidade(1);
        motorista.setIdVeiculo(1);
        // Chamar classe DAO para inserir no banco
        MotoristaDAO motoristaDAO = new MotoristaDAO();
        motoristaDAO.inserir(motorista);
    }

    @FXML
    public void pesquisar() throws SQLException {
        int id = Integer.parseInt(textPesquisar.getText());
        Motorista motorista = new Motorista();
        MotoristaDAO motoristaDAO = new MotoristaDAO();
        motorista = motoristaDAO.pesquisar(id);

        // Escrever nos campos da tela
        textNome.setText(motorista.getNome());
        textCNH.setText(motorista.getCnh());
        textCPF.setText(motorista.getCpf());
    }

    public void pesquisarCadastro(ActionEvent actionEvent) {
    }

    public void editarCadastro(ActionEvent actionEvent) {
    }

    public void excluirCadastro(ActionEvent actionEvent) {
    }
}
