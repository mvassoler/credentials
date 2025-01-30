package br.com.mvassoler.credentials.core.mappers;

import org.springframework.data.domain.Page;

import java.util.List;

public interface GenericMapper<E, R> {

    R toRecord(E entity);

    E toEntity(R record);

    List<R> toRecordList(List<E> entityList);

    List<E> toEntityList(List<R> recordList);

    Page<R> toRecordPage(Page<E> entityListPage);

}
