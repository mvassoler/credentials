package br.com.mvassoler.credentials.repositories.filters;

import br.com.mvassoler.credentials.core.enums.Profile;
import br.com.mvassoler.credentials.core.enums.TipoChave;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CredentialFiltro implements Serializable {

    private String owner;
    private TipoChave tipoChave;
    private String service;
    private Profile profile;
    private String urlService;
    private String login;
    private String password;
    private String token;

}