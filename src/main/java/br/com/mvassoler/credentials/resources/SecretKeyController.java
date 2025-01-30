package br.com.mvassoler.credentials.resources;


import br.com.mvassoler.credentials.core.enums.TipoChave;
import br.com.mvassoler.credentials.core.records.SecretKeyRecord;
import br.com.mvassoler.credentials.core.utils.Utils;
import br.com.mvassoler.credentials.services.SecretKeyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/secret-keys")
@AllArgsConstructor
public class SecretKeyController {

    private final SecretKeyService secretKeyService;
    private final Utils utils;


    @PostMapping("/{tipoChave}")
    public ResponseEntity<SecretKeyRecord> generateKey(@PathVariable TipoChave tipoChave) {
        var secretKey = secretKeyService.create(tipoChave);
        return ResponseEntity.ok(SecretKeyRecord.fromSecretKey(secretKey));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleletKeyById(@PathVariable Long id) {
        secretKeyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/byTipoChave/{key}")
    public ResponseEntity<Void> deleletKeyByTipoChave(@PathVariable TipoChave key) {
        secretKeyService.deleteByTipoChave(key);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allRecords")
    public ResponseEntity<List<SecretKeyRecord>> getAllKeys() {
        var keys = secretKeyService.getAllKeys();
        return ResponseEntity.ok(SecretKeyRecord.fromListSecretKey(keys));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecretKeyRecord> getKeyById(@PathVariable Long id) {
        return ResponseEntity.ok(this.secretKeyService.getById(id));
    }

    @GetMapping("/pageRecords")
    public ResponseEntity<Page<SecretKeyRecord>> getSecretKeys(
            @RequestParam TipoChave tipoChave,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "ASC") String sortOrder) {
        Page<SecretKeyRecord> secretKeys = secretKeyService.getPageKeystByTipoChave(tipoChave, this.utils.getPageable(page, size, sortField, sortOrder));
        return ResponseEntity.ok(secretKeys);
    }

}