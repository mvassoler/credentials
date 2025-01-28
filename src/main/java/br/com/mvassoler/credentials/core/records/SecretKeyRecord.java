package br.com.mvassoler.credentials.core.records;

import br.com.mvassoler.credentials.core.enums.TipoChave;
import br.com.mvassoler.credentials.entities.SecretKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record SecretKeyRecord(

        @JsonProperty(value = "id")
        Long id,

        @JsonProperty(required = true, value = "secret_key")
        @NotBlank
        String secretKey,

        @JsonProperty(required = true, value = "tipo_chave")
        @NotNull
        TipoChave tipoChave

) {

    public static SecretKeyRecord fromSecretKey(SecretKey secretKey) {
        return new SecretKeyRecord(
                secretKey.getId(),
                secretKey.getSecretKey(),
                secretKey.getTipoChave()
        );
    }

    public static SecretKey fromSecretKey(SecretKeyRecord secretKey) {
        return SecretKey.builder()
                .id(secretKey.id())
                .secretKey(secretKey.secretKey())
                .tipoChave(secretKey.tipoChave())
                .build();
    }

    public static List<SecretKeyRecord> fromListSecretKey(List<SecretKey> secretKeys) {
        if (Objects.isNull(secretKeys) || secretKeys.isEmpty()) {
            return new ArrayList<>();
        }
        return secretKeys.stream()
                .map(SecretKeyRecord::fromSecretKey)
                .collect(Collectors.toList());
    }

}
