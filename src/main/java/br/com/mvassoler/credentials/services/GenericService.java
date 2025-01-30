package br.com.mvassoler.credentials.services;

import jakarta.transaction.Transactional;

import java.util.List;

public interface GenericService<ENTITY, RECORD, RECORDLIST, ENTITYLIST, REPOSITORY, MAPPER> {

    @Transactional
    default RECORD create(RECORD record) {
        var entity = this.mapperToEntity(record);
        this.saveEntity(entity);
        return this.mapperToRecord(entity);
    }

    @Transactional
    default RECORD upate(RECORD record) {
        var entity = this.mapperToEntity(record);
        this.updateEntity(entity);
        return this.mapperToRecord(entity);
    }

    @Transactional
    default void deleteById(Long id) {
        var entity = this.getEntityById(id);
        this.deleteEntity(entity);
    }

    @Transactional
    default List<RECORD> createList(List<RECORD> records) {
        List<ENTITY> entities = this.mapperToEntityList(records);
        this.saveEntityList(entities);
        return this.mapperToRecordList(entities);
    }

    @Transactional
    default List<RECORD> upate(List<RECORD> records) {
        List<ENTITY> entities = this.mapperToEntityList(records);
        this.updateEntityList(entities);
        return this.mapperToRecordList(entities);
    }

    @Transactional
    default void deleteByIdList(List<Long> ids) {
        this.deleteEntityList(ids);
    }

    void saveEntity(ENTITY entity);

    void updateEntity(ENTITY entity);

    void deleteEntity(ENTITY entity);

    void saveEntityList(List<ENTITY> entities);

    void updateEntityList(List<ENTITY> entities);

    void deleteEntityList(List<Long> ids);

    void preInsert(ENTITY entity);

    void preUdate(ENTITY entity);

    void posInsert(ENTITY entity);

    void posUpdate(ENTITY entity);

    ENTITY mapperToEntity(RECORD record);

    RECORD mapperToRecord(ENTITY entity);

    List<ENTITY> mapperToEntityList(List<RECORD> records);

    List<RECORD> mapperToRecordList(List<ENTITY> entities);

    ENTITY getEntityById(Long id);


}
