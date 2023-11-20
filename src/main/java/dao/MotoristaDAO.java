package dao;

import database.ConnectionFactory;
import model.Motorista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MotoristaDAO {

    private Connection connection;

    public MotoristaDAO() throws SQLException {
        // Inicialize a conexão aqui usando um ConnectionFactory.
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

    public Motorista pesquisar(int id) {
        String sql = "SELECT * FROM motorista WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            Motorista motorista = null;
            if (rs.next()) {
                motorista = new Motorista();
                motorista.setId(rs.getInt("id"));
                motorista.setIdCidade(rs.getInt("id_cidade"));
                motorista.setIdVeiculo(rs.getInt("id_veiculo"));
                motorista.setNome(rs.getString("nome"));
                motorista.setCpf(rs.getString("cpf"));
                motorista.setCnh(rs.getString("cnh"));
                motorista.setDataCadastro(rs.getDate("dataCadastro"));
            }
            rs.close();
            stmt.close();
            return motorista;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Motorista motorista) {
        String sql = "UPDATE motorista SET id_cidade = ?, id_veiculo = ?, nome = ?, cpf = ?, cnh = ?, dataCadastro = ? WHERE id = ?";
        try {
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

    public void excluir(int id) {
        String sql = "DELETE FROM motorista WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);

            stmt.executeUpdate();
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


                motorista.setDataCadastro(rs.getDate("dataCadastro"));
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

