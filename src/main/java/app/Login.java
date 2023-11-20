package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {

    public class CriarTabela {

        public void main(String[] args) {
            String url = "jdbc:sqlite:cadastro.db";

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                // Criação da tabela
                String sql = "CREATE TABLE IF NOT EXISTS usuarios (\n"
                        + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + "	nome TEXT NOT NULL,\n"
                        + "	email TEXT NOT NULL\n"
                        + ");";
                stmt.execute(sql);

                System.out.println("Tabela criada com sucesso!");

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
