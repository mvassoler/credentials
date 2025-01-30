package br.com.mvassoler.credentials.services;

import br.com.mvassoler.credentials.core.enums.Profile;
import br.com.mvassoler.credentials.core.enums.TipoChave;
import br.com.mvassoler.credentials.core.mappers.CredentialMapper;
import br.com.mvassoler.credentials.core.records.CredentialRecord;
import br.com.mvassoler.credentials.core.utils.EncryptionUtil;
import br.com.mvassoler.credentials.entities.Credential;
import br.com.mvassoler.credentials.entities.SecretKey;
import br.com.mvassoler.credentials.repositories.CredentialRepository;
import br.com.mvassoler.credentials.repositories.filters.CredentialFiltro;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private final SecretKeyService secretKeyService;
    private final CredentialMapper credentialMapper;

    @Transactional
    public CredentialRecord createCredential(CredentialRecord credentialRecord) {
        var credential = this.credentialMapper.toEntity(credentialRecord);
        this.saveRecord(credential);
        return this.credentialMapper.toRecord(credential);
    }

    @Transactional
    public CredentialRecord updateCredential(Long id, CredentialRecord credentialRecord) {
        var credential = this.credentialMapper.toEntity(credentialRecord);
        this.updateRecord(id, credential);
        return this.credentialMapper.toRecord(credential);
    }

    @Transactional
    public void deleteCredentialById(Long id) {
        var credential = this.getDCredentialById(id);
        this.credentialRepository.delete(credential);
    }

    public Page<CredentialRecord> pageRecords(CredentialFiltro filtro, Pageable pageable) {
        return this.credentialMapper.toRecordPage(
                this.pageCredentials(filtro, pageable)
        );
    }

    public List<CredentialRecord> findAllRecords() {
        return this.credentialMapper.toRecordList(
                this.findAllCredentials()
        );
    }

    public List<CredentialRecord> findRecordByOwner(String owner) {
        return this.credentialMapper.toRecordList(
                this.findCredentialByOwner(owner)
        );

    }

    public List<CredentialRecord> findRecordByTipoChave(TipoChave tipoChave) {
        return this.credentialMapper.toRecordList(
                this.findCredentialByTipoChave(tipoChave)
        );
    }

    public List<CredentialRecord> findRecordByService(String service) {
        return this.credentialMapper.toRecordList(
                this.findCredentialByService(service)
        );
    }

    public List<CredentialRecord> findRecordByProfile(Profile profile) {
        return this.credentialMapper.toRecordList(
                this.findCredentialByProfile(profile)
        );
    }

    public CredentialRecord findRecordByOwnerAndTipoChaveAndServiceAndProfileAndUrlService(
            String owner,
            TipoChave tipoChave,
            String service,
            Profile profile,
            String urlService
    ) {
        var credential = this.findCredentialByOwnerAndTipoChaveAndServiceAndProfileAndUrlService(
                owner, tipoChave, service, profile, urlService
        );
        return this.credentialMapper.toRecord(credential);
    }


    public void saveRecord(Credential credential) {
        this.validateCredentialExists(credential);
        var key = this.secretKeyService.getKeyByTipoChave(credential.getTipoChave());
        this.encriptCredentials(credential, key);
        this.credentialRepository.save(credential);
    }

    public void updateRecord(Long id, Credential credential) {
        this.getDCredentialById(id);
        this.validateCredentialExistsUpdate(credential, id);
        var key = this.secretKeyService.getKeyByTipoChave(credential.getTipoChave());
        this.encriptCredentials(credential, key);
        credential.setId(id);
        this.credentialRepository.save(credential);
    }

    public void validateCredentialExists(Credential credential) {
        Optional<Credential> optionalCredential = this.credentialRepository.findByOwnerAndTipoChaveAndServiceAndProfileAndUrlService(
                credential.getOwner(),
                credential.getTipoChave(),
                credential.getService(),
                credential.getProfile(),
                credential.getService());
        if (optionalCredential.isPresent()) {
            throw new IllegalArgumentException("Credencial para os valores informados já existe na base de dados.");
        }
    }

    public void validateCredentialExistsUpdate(Credential credential, Long id) {
        Optional<Credential> optionalCredential = this.credentialRepository.findByOwnerAndTipoChaveAndServiceAndProfileAndUrlService(
                credential.getOwner(),
                credential.getTipoChave(),
                credential.getService(),
                credential.getProfile(),
                credential.getService());
        if (optionalCredential.isPresent() && !optionalCredential.get().getId().equals(id)) {
            throw new IllegalArgumentException("Credencial para os valores informados para atualização pertencem a outra Credencial.");
        }
    }

    public void encriptCredentials(Credential credential, SecretKey key) {
        try {
            credential.setLogin(EncryptionUtil.encrypt(credential.getLogin(), EncryptionUtil.getSecretKey(key.getSecretKey())));
            credential.setLogin(EncryptionUtil.encrypt(credential.getPassword(), EncryptionUtil.getSecretKey(key.getSecretKey())));
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro no processo de encriptar as credenciais.");
        }
    }

    public Credential getDCredentialById(Long id) {
        return credentialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Credential não encontrada para o ID: " + id));
    }

    public List<Credential> findCredentialByOwner(String owner) {
        return this.credentialRepository.findByOwner(owner);
    }

    public List<Credential> findCredentialByTipoChave(TipoChave tipoChave) {
        return this.credentialRepository.findByTipoChave(tipoChave);
    }

    public List<Credential> findCredentialByService(String service) {
        return this.credentialRepository.findByService(service);
    }

    public List<Credential> findCredentialByProfile(Profile profile) {
        return this.credentialRepository.findByProfile(profile);
    }

    public Credential findCredentialByOwnerAndTipoChaveAndServiceAndProfileAndUrlService(
            String owner,
            TipoChave tipoChave,
            String service,
            Profile profile,
            String urlService
    ) {
        return this.credentialRepository.findByOwnerAndTipoChaveAndServiceAndProfileAndUrlService(owner, tipoChave, service, profile, urlService)
                .orElseThrow(() -> new IllegalArgumentException("Credential não encontrada para os valores informados."));
    }

    public Page<Credential> pageCredentials(CredentialFiltro filtro, Pageable pageable) {
        return this.credentialRepository.findByCriteria(filtro, pageable);
    }

    public List<Credential> findAllCredentials() {
        return this.credentialRepository.findAllRecords();
    }
}
