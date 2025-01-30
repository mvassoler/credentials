package br.com.mvassoler.credentials.repositories.queries;

import br.com.mvassoler.credentials.entities.Credential;
import br.com.mvassoler.credentials.repositories.filters.CredentialFiltro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CredentiallRepositoryQueriesImpl implements CredentiallRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Credential> findByCriteria(CredentialFiltro filtro, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Credential> criteriaQuery = criteriaBuilder.createQuery(Credential.class);
        Root<Credential> root = criteriaQuery.from(Credential.class);
        List<Predicate> predicates = new ArrayList<>();
        this.constructPredicates(criteriaBuilder, root, predicates, filtro);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        List<Credential> resultList = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageable.getOffset())   // Define o ponto inicial
                .setMaxResults(pageable.getPageSize())       // Define o número de resultados por página
                .getResultList();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Credential> countRoot = countQuery.from(Credential.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public List<Credential> findAllRecords() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Credential> criteriaQuery = criteriaBuilder.createQuery(Credential.class);
        Root<Credential> root = criteriaQuery.from(Credential.class);
        List<Predicate> predicates = new ArrayList<>();
        this.constructPredicates(criteriaBuilder, root, predicates, new CredentialFiltro());
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private void constructPredicates(CriteriaBuilder criteriaBuilder,
                                     Root<Credential> root,
                                     List<Predicate> predicates,
                                     CredentialFiltro filtro) {
        if (filtro.getOwner() != null && !filtro.getOwner().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("owner"), filtro.getOwner()));
        }
        if (filtro.getTipoChave() != null) {
            predicates.add(criteriaBuilder.equal(root.get("tipoChave"), filtro.getTipoChave()));
        }
        if (filtro.getService() != null && !filtro.getService().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("service"), filtro.getService()));
        }
        if (filtro.getProfile() != null) {
            predicates.add(criteriaBuilder.equal(root.get("profile"), filtro.getProfile()));
        }
        if (filtro.getUrlService() != null && !filtro.getUrlService().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("urlService"), filtro.getUrlService()));
        }
        if (filtro.getLogin() != null && !filtro.getLogin().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("login"), filtro.getLogin()));
        }
        if (filtro.getPassword() != null && !filtro.getPassword().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("password"), filtro.getPassword()));
        }
        if (filtro.getToken() != null && !filtro.getToken().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("token"), filtro.getToken()));
        }
    }

}
