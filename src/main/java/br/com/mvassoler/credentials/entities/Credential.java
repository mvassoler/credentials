package br.com.mvassoler.credentials.entities;

import br.com.mvassoler.credentials.core.enums.Profile;
import br.com.mvassoler.credentials.core.enums.TipoChave;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credential implements Serializable {

    @Serial
    private static final long serialVersionUID = 8300966844542182347L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "tipo_chave", nullable = false)
    private TipoChave tipoChave;

    @Column(name = "service")
    private String service;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile", nullable = false)
    private Profile profile;

    @Column(name = "url_service", nullable = false)
    private String urlService;

    @Column(name = "login", nullable = false, columnDefinition = "text")
    private String login;

    @Column(name = "password", nullable = false, columnDefinition = "text")
    private String password;

    @Column(name = "token", columnDefinition = "text")
    private String token;

}
