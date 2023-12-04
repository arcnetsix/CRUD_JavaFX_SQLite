package controller;

import dao.MotoristaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Motorista;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.List;



public class MotoristaController {
    @FXML
    private Button btnSalvar, btnPesquisar, btnEditar, btnExcluir, btnSalvarEdicao, btnCancelarEdicao;
    @FXML
    private TextField textNome, textCPF, textCNH, textPesquisar;
    @FXML
    private TableView<Motorista> tabelaMotoristas;

    private Motorista motoristaEmEdicao;

    @FXML
    public void initialize() {
        // Configurar a tabela
        TableColumn<Motorista, Integer> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Motorista, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Motorista, String> colunaCpf = new TableColumn<>("CPF");
        colunaCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        // Adicionar as colunas à tabela
        tabelaMotoristas.getColumns().addAll(colunaId, colunaNome, colunaCpf);

        // Preencher a tabela com dados do banco de dados
        carregarDadosDoBanco();
    }

    private void carregarDadosDoBanco() {
        try (MotoristaDAO motoristaDAO = new MotoristaDAO()) {
            List<Motorista> motoristas = motoristaDAO.listar();
            ObservableList<Motorista> motoristasObservable = FXCollections.observableList(motoristas);
            tabelaMotoristas.setItems(motoristasObservable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void salvarCadastro() {
        try {
            if (motoristaEmEdicao == null) {
                Motorista motorista = new Motorista();
                motorista.setNome(textNome.getText());
                motorista.setDataCadastro(new Date());
                motorista.setCnh(textCNH.getText());
                motorista.setCpf(textCPF.getText());
                motorista.setIdCidade(1);
                motorista.setIdVeiculo(1);

                try (MotoristaDAO motoristaDAO = new MotoristaDAO()) {
                    motoristaDAO.inserir(motorista);
                }
            } else {
                motoristaEmEdicao.setNome(textNome.getText());
                motoristaEmEdicao.setCnh(textCNH.getText());
                motoristaEmEdicao.setCpf(textCPF.getText());

                try (MotoristaDAO motoristaDAO = new MotoristaDAO()) {
                    motoristaDAO.atualizar(motoristaEmEdicao);
                }

                motoristaEmEdicao = null;
            }

            limparCampos();
            carregarDadosDoBanco();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void carregarDadosMotorista(int id) {
        try {
            Motorista motorista;

            try (MotoristaDAO motoristaDAO = new MotoristaDAO()) {
                motorista = motoristaDAO.pesquisar(id);
            }

            if (motorista != null) {
                textNome.setText(motorista.getNome());
                textCNH.setText(motorista.getCnh());
                textCPF.setText(motorista.getCpf());

                motoristaEmEdicao = motorista;
            } else {
                limparCampos();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void atualizarCadastro() {
        try {
            int id = Integer.parseInt(textPesquisar.getText());
            Motorista motorista;

            try (MotoristaDAO motoristaDAO = new MotoristaDAO()) {
                motorista = motoristaDAO.pesquisar(id);
                System.out.println("Valor de motorista após pesquisar: " + motorista);
            }

            if (motorista != null) {
                textNome.setText(motorista.getNome());
                textCNH.setText(motorista.getCnh());
                textCPF.setText(motorista.getCpf());

                motoristaEmEdicao = motorista;
            } else {
                limparCampos();
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void editarCadastro() {
        if (motoristaEmEdicao != null) {
            habilitarEdicao(true);
        }
    }

    @FXML
    public void salvarEdicao() {
        try {
            if (motoristaEmEdicao != null) {
                motoristaEmEdicao.setNome(textNome.getText());
                motoristaEmEdicao.setCnh(textCNH.getText());
                motoristaEmEdicao.setCpf(textCPF.getText());

                try (MotoristaDAO motoristaDAO = new MotoristaDAO()) {
                    motoristaDAO.atualizar(motoristaEmEdicao);
                }

                habilitarEdicao(false);

                exibirMensagem("Edição bem-sucedida", "As alterações foram salvas com sucesso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            exibirMensagemErro("Erro na edição", "Ocorreu um erro ao salvar as alterações.");
        }
    }
    private void habilitarEdicao(boolean habilitar) {
        textNome.setEditable(habilitar);
        textCNH.setEditable(habilitar);
        textCPF.setEditable(habilitar);

        btnSalvarEdicao.setVisible(habilitar);
        btnCancelarEdicao.setVisible(habilitar);
        btnEditar.setDisable(habilitar);
    }

    @FXML
    public void cancelarEdicao() {
        if (motoristaEmEdicao != null) {
            carregarDadosMotorista(motoristaEmEdicao.getId());
            habilitarEdicao(false);
        }
    }
    @FXML
    public void autenticarUsuario() {
    }


    @FXML
    public void excluirCadastro() {
        try {
            if (motoristaEmEdicao != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmação de Exclusão");
                alert.setHeaderText("Você tem certeza?");
                alert.setContentText("Esta ação excluirá o registro. Deseja continuar?");

                ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

                if (result == ButtonType.OK) {
                    try (MotoristaDAO motoristaDAO = new MotoristaDAO()) {
                        motoristaDAO.excluir(motoristaEmEdicao.getId());
                    }

                    motoristaEmEdicao = null;

                    limparCampos();

                    exibirMensagem("Exclusão bem-sucedida", "O registro foi excluído com sucesso.");
                    carregarDadosDoBanco();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            exibirMensagemErro("Erro na exclusão", "Ocorreu um erro ao excluir o registro.");
        }
    }

    private void exibirMensagem(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    private void exibirMensagemErro(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }
    private void limparCampos() {
        textNome.clear();
        textCNH.clear();
        textCPF.clear();
        textPesquisar.clear();
    }
}

