package br.com.mvassoler.credentials.repositories.queries;

import br.com.mvassoler.credentials.entities.Credential;
import br.com.mvassoler.credentials.repositories.filters.CredentialFiltro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentiallRepositoryQueries extends GenericRepositoryQueries<Credential, Long> {

    Page<Credential> findByCriteria(CredentialFiltro filtro, Pageable pageable);

}
