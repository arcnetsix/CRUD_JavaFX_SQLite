package dao;

import database.ConnectionFactory;
import model.Motorista;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




public class MotoristaDAO implements AutoCloseable{

    private Connection connection;

    public MotoristaDAO() throws SQLException {

        this.connection = ConnectionFactory.connect();
    }

    public void inserir(Motorista motorista) {
        String sql = "INSERT INTO motorista (id_cidade, id_veiculo, nome, cpf, cnh, dataCadastro) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, motorista.getIdCidade());
            stmt.setInt(2, motorista.getIdVeiculo());
            stmt.setString(3, motorista.getNome());
            stmt.setString(4, motorista.getCpf());
            stmt.setString(5, motorista.getCnh());
            stmt.setString(6, motorista.getDataCadastro().toString());

            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM motorista WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Nenhum registro encontrado para exclusão.");
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }


    public Motorista pesquisar(int id) throws SQLException {
        String query = "SELECT * FROM motorista WHERE id = ?";
        Motorista motorista = null;

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:cadastro.db");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    motorista = new Motorista();
                    motorista.setId(resultSet.getInt("id"));
                    motorista.setNome(resultSet.getString("nome"));
                    motorista.setCpf(resultSet.getString("cpf"));
                    motorista.setCnh(resultSet.getString("cnh"));

                    // Tratar a conversão da string para Date
                    String dataCadastroStr = resultSet.getString("data_cadastro");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                    try {
                        Date dataCadastro = dateFormat.parse(dataCadastroStr);
                        motorista.setDataCadastro(dataCadastro);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return motorista;
    }

    public void iniciarTransacao() throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(false);
        }
    }

    public void finalizarTransacao() throws SQLException {
        if (connection != null) {
            connection.setAutoCommit(true);
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    public void atualizar(Motorista motorista) {
        String sql = "UPDATE motorista SET id_cidade = ?, id_veiculo = ?, nome = ?, cpf = ?, cnh = ?, dataCadastro = ? WHERE id = ?";
        try {Connection connection = DriverManager.getConnection("jdbc:sqlite:cadastro.db");
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, motorista.getIdCidade());
            stmt.setInt(2, motorista.getIdVeiculo());
            stmt.setString(3, motorista.getNome());
            stmt.setString(4, motorista.getCpf());
            stmt.setString(5, motorista.getCnh());
            stmt.setString(6, motorista.getDataCadastro().toString());
            stmt.setInt(7, motorista.getId());

            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Motorista> listar() {
        String sql = "SELECT * FROM motorista";
        List<Motorista> motoristas = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Motorista motorista = new Motorista();
                motorista.setId(rs.getInt("id"));
                motorista.setIdCidade(rs.getInt("id_cidade"));
                motorista.setIdVeiculo(rs.getInt("id_veiculo"));
                motorista.setNome(rs.getString("nome"));
                motorista.setCpf(rs.getString("cpf"));
                motorista.setCnh(rs.getString("cnh"));

                String dataCadastroString = rs.getString("dataCadastro");
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                java.util.Date dataCadastroUtil = dateFormat.parse(dataCadastroString);

                String dataCadastroStr = rs.getString("dataCadastro");

                // Convertendo a string para um objeto Date
                dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                Date dataCadastro = dateFormat.parse(dataCadastroStr);

                motorista.setDataCadastro(dataCadastro);

                motoristas.add(motorista);
            }
            rs.close();
            stmt.close();
            return motoristas;
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}

