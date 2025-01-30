package br.com.mvassoler.credentials.core.records;

import br.com.mvassoler.credentials.core.enums.Profile;
import br.com.mvassoler.credentials.core.enums.TipoChave;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record CredentialRecord(

        @JsonProperty(value = "id")
        Long id,

        @JsonProperty(value = "owner")
        @NotBlank
        String owner,

        @JsonProperty(value = "tipo_chave")
        @NotNull
        TipoChave tipoChave,

        @JsonProperty(value = "service")
        @NotBlank
        String service,

        @JsonProperty(value = "profile")
        @NotNull
        Profile profile,

        @JsonProperty(value = "url_service")
        @NotBlank
        String urlService,

        @JsonProperty(value = "login")
        @NotBlank
        String login,

        @JsonProperty(value = "password")
        @NotBlank
        String password,

        @JsonProperty(value = "token")
        String token
) {

}