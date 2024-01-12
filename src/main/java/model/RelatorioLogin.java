package model;

import java.time.LocalDateTime;

    public class RelatorioLogin {
        private String login;
        private LocalDateTime dataLogin;

        public RelatorioLogin(String login, LocalDateTime dataLogin) {
            this.login = login;
            this.dataLogin = dataLogin;
        }

        public String getLogin() {
            return login;
        }

        public LocalDateTime getDataLogin() {
            return dataLogin;
        }
    }


