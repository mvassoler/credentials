package br.com.mvassoler.credentials.services;


import br.com.mvassoler.credentials.core.enums.TipoChave;
import br.com.mvassoler.credentials.core.handlers.MessageExceptionsConstants;
import br.com.mvassoler.credentials.core.handlers.exception.BusinessException;
import br.com.mvassoler.credentials.core.handlers.exception.EntityNotFoundException;
import br.com.mvassoler.credentials.core.records.SecretKeyRecord;
import br.com.mvassoler.credentials.core.utils.Utils;
import br.com.mvassoler.credentials.entities.SecretKey;
import br.com.mvassoler.credentials.repositories.SecretKeyRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SecretKeyService {

    private final SecretKeyRepository secretKeyRepository;
    private final Utils utils;

    @Transactional
    public SecretKey create(TipoChave tipoChave) {
        this.validatetKeyByTipoChave(tipoChave);
        SecretKey secretKey = this.utils.generateKey(tipoChave);
        return secretKeyRepository.save(secretKey);
    }

    @Transactional
    public void deleteById(Long id) {
        SecretKey secretKey = this.getKeyById(id);
        secretKeyRepository.delete(secretKey);
    }

    public SecretKeyRecord getById(Long id) {
        SecretKey secretKey = this.getKeyById(id);
        return SecretKeyRecord.fromSecretKey(secretKey);
    }

    public void deleteByTipoChave(TipoChave tipoChave) {
        SecretKey secretKey = this.getKeyByTipoChave(tipoChave);
        secretKeyRepository.deleteById(secretKey.getId());
    }

    public List<SecretKey> getAllKeys() {
        return secretKeyRepository.findAll();
    }

    public SecretKey getKeyById(Long id) {
        return secretKeyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(utils.retornaMensagem(MessageExceptionsConstants.REGISTER_NOT_FOUND, SecretKey.class.getSimpleName(), id)));
    }

    public SecretKey getKeyByTipoChave(TipoChave tipoChave) {
        return secretKeyRepository.findSecretKeyByTipoChave(tipoChave)
                .orElseThrow(() -> new EntityNotFoundException(utils.retornaMensagem(MessageExceptionsConstants.KEY_TYPE_NOT_FOUND, SecretKey.class.getSimpleName(), tipoChave)));
    }

    public void validatetKeyByTipoChave(TipoChave tipoChave) {
        Optional<SecretKey> optionalSecretKey = this.secretKeyRepository.findSecretKeyByTipoChave(tipoChave);
        if (optionalSecretKey.isPresent()) {
            throw new BusinessException(utils.retornaMensagem(MessageExceptionsConstants.SECRET_kEY_EXISTS_FOR_TYPE_KEY, SecretKey.class.getSimpleName(), tipoChave));
        }
    }

}