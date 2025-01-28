package br.com.mvassoler.credentials.services;


import br.com.mvassoler.credentials.Repositories.SecretKeyRepository;
import br.com.mvassoler.credentials.core.enums.TipoChave;
import br.com.mvassoler.credentials.core.records.SecretKeyRecord;
import br.com.mvassoler.credentials.core.utils.Utils;
import br.com.mvassoler.credentials.entities.SecretKey;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SecretKeyService {

    private final SecretKeyRepository secretKeyRepository;
    private final Utils utils;

    @Transactional
    public SecretKey create(TipoChave tipoChave) {
        SecretKey secretKey = this.utils.generateKey(tipoChave);
        return secretKeyRepository.save(secretKey);
    }

    @Transactional
    public void deleteById(Long id) {
        SecretKey secretKey = this.getKeyById(id);
        secretKeyRepository.deleteById(id);
    }

    public SecretKeyRecord getById(Long id) {
        SecretKey secretKey = this.getKeyById(id);
        return SecretKeyRecord.fromSecretKey(secretKey);
    }

    public Page<SecretKeyRecord> getPageKeystByTipoChave(TipoChave tipoChave, Pageable pageable) {
        Page<SecretKey> secretKeys = secretKeyRepository.findAllByTipoChave(tipoChave, pageable);
        return secretKeys.map(SecretKeyRecord::fromSecretKey);
    }

    public void deleteBySecretKey(String secretKeyField) {
        SecretKey secretKey = this.getKeyBySecretKey(secretKeyField);
        secretKeyRepository.deleteById(secretKey.getId());
    }

    public List<SecretKey> getAllKeys() {
        return secretKeyRepository.findAll();
    }

    public SecretKey getKeyById(Long id) {
        return secretKeyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chave não encontrada para o ID: " + id));
    }

    public SecretKey getKeyBySecretKey(String secretKey) {
        return secretKeyRepository.findSecretKeyBySecretKey(secretKey)
                .orElseThrow(() -> new IllegalArgumentException("Chave não encontrada para a Secret Key informada. "));
    }


}