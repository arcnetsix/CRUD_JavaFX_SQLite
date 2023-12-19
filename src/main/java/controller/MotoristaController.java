package controller;

import dao.MotoristaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Motorista;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MotoristaController {

    @FXML
    private Button btnSalvar, btnAtualizar, btnEditar, btnExcluir, btnSalvarEdicao, btnCancelarEdicao;
    @FXML
    private TextField textNome, textCPF, textCNH, textPesquisar;
    @FXML
    private TableView<Motorista> tabelaMotoristas;

    private Motorista motoristaEmEdicao;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarDadosDoBanco();
    }

    private void configurarTabela() {
        TableColumn<Motorista, Integer> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Motorista, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Motorista, String> colunaCpf = new TableColumn<>("CPF");
        colunaCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        TableColumn<Motorista, String> colunaCnh = new TableColumn<>("CNH");
        colunaCnh.setCellValueFactory(new PropertyValueFactory<>("cnh"));

        TableColumn<Motorista, Date> colunaDataCadastro = new TableColumn<>("Data de Cadastro");
        colunaDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));

        tabelaMotoristas.getColumns().addAll(colunaId, colunaNome, colunaCpf, colunaCnh, colunaDataCadastro);
    }

    private void carregarDadosDoBanco() {
        try {
            MotoristaDAO motoristaDAO = new MotoristaDAO();
            List<Motorista> motoristas = motoristaDAO.listar();
            ObservableList<Motorista> motoristasObservable = FXCollections.observableList(motoristas);
            tabelaMotoristas.setItems(motoristasObservable);
        } catch (SQLException e) {
            handleSQLException(e, "Erro ao carregar dados", "Ocorreu um erro ao carregar dados do banco");
        }
    }

    @FXML
    public void salvarCadastro() {
        try {
            Motorista motorista = (motoristaEmEdicao == null) ? new Motorista() : motoristaEmEdicao;
            motorista.setNome(textNome.getText());
            motorista.setCnh(textCNH.getText());
            motorista.setCpf(textCPF.getText());
            motorista.setIdCidade(1);
            motorista.setIdVeiculo(1);

            try (MotoristaDAO motoristaDAO = new MotoristaDAO()) {
                if (motoristaEmEdicao == null) {
                    motorista.setDataCadastro(new Date());
                    motoristaDAO.inserir(motorista);
                } else {
                    motoristaDAO.atualizar(motorista);
                    motoristaEmEdicao = null;
                }
            }

            limparCampos();
            carregarDadosDoBanco();
        } catch (SQLException e) {
            handleSQLException(e, "Erro ao salvar cadastro", "Ocorreu um erro ao salvar o cadastro");
        }
    }

    @FXML
    public void atualizarCadastro() {
        try {
            if (textPesquisar != null) {
                String idText = textPesquisar.getText();
                if (!idText.isEmpty()) {
                    int id = Integer.parseInt(idText);
                    Motorista motorista;

                    try (MotoristaDAO motoristaDAO = new MotoristaDAO()) {
                        motorista = motoristaDAO.pesquisar(id);

                        motoristaDAO.iniciarTransacao();

                        // Chama o método para excluir
                        motoristaDAO.excluir(id);

                        // Finaliza a transação (comita as alterações)
                        motoristaDAO.finalizarTransacao();
                    }

                    if (motorista == null) {
                        limparCampos();
                        exibirMensagemErro("Erro na atualização", "Nenhum motorista encontrado com o ID fornecido.");
                    } else {
                        carregarDadosMotorista(motorista);
                    }
                } else {
                    exibirMensagemErro("Erro na atualização", "Por favor, insira um ID antes de atualizar.");
                }
            } else {
                exibirMensagemErro("Erro na atualização", "O campo de pesquisa é nulo.");
            }
        } catch (SQLException | NumberFormatException e) {
            handleSQLException(e, "Erro na atualização", "Ocorreu um erro ao buscar os dados para atualização");
        }
    }



    @FXML
    public void editarCadastro() {
        Motorista motoristaSelecionado = tabelaMotoristas.getSelectionModel().getSelectedItem();

        if (motoristaSelecionado != null) {
            motoristaEmEdicao = motoristaSelecionado;
            carregarDadosMotorista(motoristaEmEdicao);
            habilitarEdicao(true);
        } else {
            exibirMensagemErro("Erro na edição", "Nenhum motorista selecionado para edição.");
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

                exibirMensagemErro("Edição bem-sucedida", "As alterações foram salvas com sucesso.");

                // Carregar os dados do banco novamente para refletir as alterações no painel
                carregarDadosDoBanco();
            }
        } catch (SQLException e) {
            handleSQLException(e, "Erro na edição", "Ocorreu um erro ao salvar as alterações.");
        }
    }


    @FXML
    public void cancelarEdicao() {
        if (motoristaEmEdicao != null) {
            carregarDadosMotorista(motoristaEmEdicao);
            habilitarEdicao(false);
        }
    }

    @FXML
    public void excluirCadastro() {
        try {
            Motorista motoristaSelecionado = tabelaMotoristas.getSelectionModel().getSelectedItem();

            if (motoristaSelecionado != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmação de Exclusão");
                alert.setHeaderText("Você tem certeza?");
                alert.setContentText("Esta ação excluirá o registro. Deseja continuar?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    try (MotoristaDAO motoristaDAO = new MotoristaDAO()) {
                        motoristaDAO.excluir(motoristaSelecionado.getId());
                    }

                    limparCampos();
                    motoristaEmEdicao = null;

                    // Exiba a mensagem de exclusão bem-sucedida
                    exibirMensagemErro("Exclusão bem-sucedida", "O registro foi excluído com sucesso.");

                    carregarDadosDoBanco();
                }
            } else {
                exibirMensagemErro("Erro na exclusão", "Nenhum motorista selecionado para exclusão.");
            }
        } catch (SQLException e) {
            handleSQLException(e, "Erro na exclusão", "Ocorreu um erro ao excluir o registro");
        }
    }


    private void exibirMensagemErro(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    private void carregarDadosMotorista(Motorista motorista) {
        if (motorista != null) {
            textNome.setText(motorista.getNome());
            textCNH.setText(motorista.getCnh());
            textCPF.setText(motorista.getCpf());

            motoristaEmEdicao = motorista;
        } else {
            exibirMensagemErro("Erro na atualização", "Motorista não encontrado.");
            limparCampos();
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

    private void limparCampos() {
        textNome.clear();
        textCNH.clear();
        textCPF.clear();
        textPesquisar.clear();
    }

    private void handleSQLException(Exception e, String title, String content) {
        e.printStackTrace();
        exibirMensagemErro(title, content + ": " + e.getMessage());

        // Adicione o seguinte bloco para capturar exceções específicas
        if (e instanceof SQLException) {
            SQLException sqlException = (SQLException) e;
            System.err.println("SQLState: " + sqlException.getSQLState());
            System.err.println("Error Code: " + sqlException.getErrorCode());
        }
    }


}
