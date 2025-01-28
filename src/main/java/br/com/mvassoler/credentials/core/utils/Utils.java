package br.com.mvassoler.credentials.core.utils;

import br.com.mvassoler.credentials.core.enums.TipoChave;
import br.com.mvassoler.credentials.entities.SecretKey;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class Utils {

    public Pageable getPageable(int page, int size, String sortField, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    public SecretKey generateKey(TipoChave tipoChave) {
        try {
            javax.crypto.SecretKey aesKey = EncryptionUtil.generateKey();
            String encodedKey = Base64.getEncoder().encodeToString(aesKey.getEncoded());
            // Criar e salvar no banco
            return SecretKey.builder()
                    .secretKey(encodedKey)
                    .tipoChave(tipoChave) // Setar o tipo da chave
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar chave AES", e);
        }
    }

}
