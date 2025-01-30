package br.com.mvassoler.credentials.resources;

import br.com.mvassoler.credentials.core.enums.Profile;
import br.com.mvassoler.credentials.core.enums.TipoChave;
import br.com.mvassoler.credentials.core.records.CredentialRecord;
import br.com.mvassoler.credentials.core.utils.Utils;
import br.com.mvassoler.credentials.repositories.filters.CredentialFiltro;
import br.com.mvassoler.credentials.services.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/credentials")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;
    private final Utils utils;

    @PostMapping
    public ResponseEntity<CredentialRecord> createCredential(@RequestBody CredentialRecord credentialRecord) {
        CredentialRecord createdRecord = credentialService.createCredential(credentialRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CredentialRecord> updateCredential(@PathVariable Long id, @RequestBody CredentialRecord credentialRecord) {
        CredentialRecord updatedRecord = credentialService.updateCredential(id, credentialRecord);
        return ResponseEntity.ok(updatedRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredentialById(@PathVariable Long id) {
        credentialService.deleteCredentialById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<CredentialRecord>> pageRecords(
            @RequestBody CredentialFiltro filtro,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortField", defaultValue = "id") String sortField,
            @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder
    ) {
        Page<CredentialRecord> recordsPage = credentialService.pageRecords(filtro, this.utils.getPageable(page, size, sortField, sortOrder));
        return ResponseEntity.ok(recordsPage);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CredentialRecord>> findAllRecords() {
        List<CredentialRecord> records = credentialService.findAllRecords();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/by-owner/{owner}")
    public ResponseEntity<List<CredentialRecord>> findRecordByOwner(@PathVariable String owner) {
        List<CredentialRecord> records = credentialService.findRecordByOwner(owner);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/by-tipo-chave/{tipoChave}")
    public ResponseEntity<List<CredentialRecord>> findRecordByTipoChave(@PathVariable TipoChave tipoChave) {
        List<CredentialRecord> records = credentialService.findRecordByTipoChave(tipoChave);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/by-service/{service}")
    public ResponseEntity<List<CredentialRecord>> findRecordByService(@PathVariable String service) {
        List<CredentialRecord> records = credentialService.findRecordByService(service);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/by-profile/{profile}")
    public ResponseEntity<List<CredentialRecord>> findRecordByProfile(@PathVariable Profile profile) {
        List<CredentialRecord> records = credentialService.findRecordByProfile(profile);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/search")
    public ResponseEntity<CredentialRecord> findRecordByOwnerAndTipoChaveAndServiceAndProfileAndUrlService(
            @RequestParam String owner,
            @RequestParam TipoChave tipoChave,
            @RequestParam String service,
            @RequestParam Profile profile,
            @RequestParam String urlService
    ) {
        CredentialRecord record = credentialService.findRecordByOwnerAndTipoChaveAndServiceAndProfileAndUrlService(
                owner, tipoChave, service, profile, urlService
        );
        return ResponseEntity.ok(record);
    }

}