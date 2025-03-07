package br.com.mvassoler.credentials.entities;


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
public class SecretKey implements Serializable {

    @Serial
    private static final long serialVersionUID = -2367202867501800175L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "secret_key", nullable = false, columnDefinition = "text")
    private String secretKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_chave", nullable = false)
    private TipoChave tipoChave;

}