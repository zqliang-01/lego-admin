package com.lego.core.data.hibernate.jpa;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.transaction.annotation.Transactional;

class LegoJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID> {

	private SimpleJpaRepository<T, ID> jpaRepository;

	protected void initJpaRepository(EntityManager entityManager, Class<T> domainType) {
		this.jpaRepository = new SimpleJpaRepository<T, ID>(domainType, entityManager);
	}

	@Override
	public List<T> findAll() {
		return jpaRepository.findAll();
	}

	@Override
	public List<T> findAll(Sort sort) {
		return jpaRepository.findAll(sort);
	}

	@Override
	public List<T> findAllById(Iterable<ID> ids) {
		return jpaRepository.findAllById(ids);
	}

	@Override
	@Transactional
	public <S extends T> List<S> saveAll(Iterable<S> entities) {
		return jpaRepository.saveAll(entities);
	}

	@Override
	@Transactional
	public void flush() {
		jpaRepository.flush();
	}

	@Override
	@Transactional
	public <S extends T> S saveAndFlush(S entity) {
		return jpaRepository.saveAndFlush(entity);
	}

	@Override
	@Transactional
	public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
		return jpaRepository.saveAllAndFlush(entities);
	}

	@Override
	public void deleteAllInBatch(Iterable<T> entities) {
		jpaRepository.deleteAllInBatch(entities);
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<ID> ids) {
		jpaRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	public void deleteAllInBatch() {
		jpaRepository.deleteAllInBatch();
	}

	@Override
	@Deprecated
	public T getOne(ID id) {
		return jpaRepository.getOne(id);
	}

	@Override
	@Deprecated
	public T getById(ID id) {
		return jpaRepository.getById(id);
	}

	@Override
	public T getReferenceById(ID id) {
		return jpaRepository.getReferenceById(id);
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> example) {
		return jpaRepository.findAll(example);
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		return jpaRepository.findAll(example, sort);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return jpaRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public <S extends T> S save(S entity) {
		return jpaRepository.save(entity);
	}

	@Override
	public Optional<T> findById(ID id) {
		return jpaRepository.findById(id);
	}

	@Override
	public boolean existsById(ID id) {
		return jpaRepository.existsById(id);
	}

	@Override
	public long count() {
		return jpaRepository.count();
	}

	@Override
	public void deleteById(ID id) {
		jpaRepository.deleteById(id);
	}

	@Override
	public void delete(T entity) {
		jpaRepository.delete(entity);
	}

	@Override
	public void deleteAllById(Iterable<? extends ID> ids) {
		jpaRepository.deleteAllById(ids);
	}

	@Override
	public void deleteAll(Iterable<? extends T> entities) {
		jpaRepository.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		jpaRepository.deleteAll();
	}

	@Override
	public <S extends T> Optional<S> findOne(Example<S> example) {
		return jpaRepository.findOne(example);
	}

	@Override
	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
		return jpaRepository.findAll(example, pageable);
	}

	@Override
	public <S extends T> long count(Example<S> example) {
		return jpaRepository.count(example);
	}

	@Override
	public <S extends T> boolean exists(Example<S> example) {
		return jpaRepository.exists(example);
	}

	@Override
	public <S extends T, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return jpaRepository.findBy(example, queryFunction);
	}

	@Override
	public Optional<T> findOne(Specification<T> spec) {
		return jpaRepository.findOne(spec);
	}

	@Override
	public List<T> findAll(Specification<T> spec) {
		return jpaRepository.findAll(spec);
	}

	@Override
	public Page<T> findAll(Specification<T> spec, Pageable pageable) {
		return jpaRepository.findAll(spec, pageable);
	}

	@Override
	public List<T> findAll(Specification<T> spec, Sort sort) {
		return jpaRepository.findAll(spec, sort);
	}

	@Override
	public long count(Specification<T> spec) {
		return jpaRepository.count(spec);
	}

	@Override
	public boolean exists(Specification<T> spec) {
		return jpaRepository.exists(spec);
	}

	@Override
	public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
		jpaRepository.setRepositoryMethodMetadata(crudMethodMetadata);
	}

}
