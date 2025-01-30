package br.com.mvassoler.credentials.repositories;

import br.com.mvassoler.credentials.core.enums.TipoChave;
import br.com.mvassoler.credentials.entities.SecretKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecretKeyRepository extends CustomJpaRepository<SecretKey, Long> {

    Optional<SecretKey> findSecretKeyByTipoChave(TipoChave tipoChave);

    Page<SecretKey> findAllByTipoChave(TipoChave tipoChave, Pageable pageable);

}
