package br.com.mvassoler.credentials.repositories.queries;


import java.util.List;

public interface GenericRepositoryQueries<T, I> {

    List<T> findAllRecords();

}
