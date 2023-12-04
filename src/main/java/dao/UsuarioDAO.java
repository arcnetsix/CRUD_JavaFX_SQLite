package dao;

import database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO{

    private Connection connection;

    public UsuarioDAO() {
        try {
            this.connection = ConnectionFactory.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para autenticar usuário
    public boolean autenticarUsuario(String login, String senha) {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, senha);

            try (ResultSet resultSet = statement.executeQuery()) {
                boolean autenticado = resultSet.next();
                if (autenticado) {
                    registrarLogin(login);
                }
                return autenticado;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para registrar um login no relatório
    private void registrarLogin(String login) {
        String sql = "INSERT INTO relatorio_logins (login, data_login) VALUES (?, NOW())";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
