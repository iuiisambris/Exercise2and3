package repository;

import repository.specifications.ISpecification;

import java.util.List;

public interface IRepository<T> {

    void add (T t);

    List<T> getAll();

    List<T> findBySpecification(ISpecification<T> specification);

    void update(T old, T updated);

    void remove(T t);

    void load();

    void commit();
}
