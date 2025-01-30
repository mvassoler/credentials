package br.com.mvassoler.credentials.core.mappers;

import br.com.mvassoler.credentials.core.records.CredentialRecord;
import br.com.mvassoler.credentials.core.utils.EncryptionUtil;
import br.com.mvassoler.credentials.entities.Credential;
import br.com.mvassoler.credentials.services.SecretKeyService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class CredentialMapper implements GenericMapper<Credential, CredentialRecord> {

    private final ModelMapper modelMapper;
    private final SecretKeyService secretKeyService;

    public CredentialRecord toRecord(Credential credential) {
        try {
            var key = this.secretKeyService.getKeyByTipoChave(credential.getTipoChave());
            credential.setLogin(EncryptionUtil.decrypt(credential.getLogin(), EncryptionUtil.getSecretKey(key.getSecretKey())));
            credential.setLogin(EncryptionUtil.decrypt(credential.getPassword(), EncryptionUtil.getSecretKey(key.getSecretKey())));
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro no processo de encriptar as credenciais.");
        }
        return modelMapper.map(credential, CredentialRecord.class);
    }

    public Page<CredentialRecord> toRecordPage(Page<Credential> credentialPage) {
        List<CredentialRecord> records = credentialPage.getContent().stream()
                .map(this::toRecord)
                .collect(Collectors.toList());
        return new PageImpl<>(records, credentialPage.getPageable(), credentialPage.getTotalElements());
    }

    public List<CredentialRecord> toRecordList(List<Credential> credentialList) {
        return credentialList.stream()
                .map(this::toRecord)
                .collect(Collectors.toList());
    }

    @Override
    public List<Credential> toEntityList(List<CredentialRecord> recordList) {
        return recordList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public Credential toEntity(CredentialRecord record) {
        return modelMapper.map(record, Credential.class);
    }

}
