package br.com.mvassoler.credentials.repositories;

import br.com.mvassoler.credentials.core.enums.Profile;
import br.com.mvassoler.credentials.core.enums.TipoChave;
import br.com.mvassoler.credentials.entities.Credential;
import br.com.mvassoler.credentials.repositories.queries.CredentiallRepositoryQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialRepository extends CustomJpaRepository<Credential, Long>, CredentiallRepositoryQueries {

    List<Credential> findByOwner(String owner);

    List<Credential> findByTipoChave(TipoChave tipoChave);

    List<Credential> findByService(String service);

    List<Credential> findByProfile(Profile profile);

    Optional<Credential> findByOwnerAndTipoChaveAndServiceAndProfileAndUrlService(
            String owner,
            TipoChave tipoChave,
            String service,
            Profile profile,
            String urlService
    );

    @Query("SELECT c FROM Credential c WHERE c.owner = :owner AND c.tipoChave = :tipoChave")
    Page<Credential> findByOwnerAndTipoChave(
            @Param("owner") String owner,
            @Param("tipoChave") TipoChave tipoChave,
            Pageable pageable
    );

}
