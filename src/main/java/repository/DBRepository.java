package repository;

import repository.specifications.ISpecification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class DBRepository<T> extends InMemoryRepository<T>{

    private final EntityManagerFactory factory;
    private final EntityManager entityManager;

    public DBRepository(Class<T> clazz) {
        super(clazz);

        try{
            factory = Persistence.createEntityManagerFactory("TaskMgrDb");
            entityManager = factory.createEntityManager();
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw e;
        }
    }

    @Override
    public void add(T t) {
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(t);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public void remove(T t){
        try
        {
            entityManager.getTransaction().begin();
            entityManager.remove(t);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public void update(T toBeUpdated, T newValue){
        try
        {
            entityManager.getTransaction().begin();
            entityManager.merge(toBeUpdated);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public List<T> findBySpecification(ISpecification<T> specification){

        try{
            var fieldMap = specification.getFieldValueMap();

            var bld = new StringBuilder();
            bld.append("select e from ");
            bld.append(clazz.getSimpleName());
            bld.append(" e ");
            bld.append("where ");

            for (var k : fieldMap.keySet()){
                bld.append("e");
                bld.append(".").append(k).append(" = '").append(fieldMap.get(k)).append("'");
                bld.append(" and ");
            }

            bld.delete(bld.lastIndexOf(" and "), bld.length());

            var query = entityManager.createQuery(bld.toString(), clazz);
            var res = query.getResultList();
            return res;
        }
        catch (Exception e){
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public List<T> getAll(){
        try {
            StringBuilder bld = new StringBuilder();
            bld.append("select e from ");
            bld.append(clazz.getSimpleName());
            bld.append(" e");
            return entityManager.createQuery(bld.toString(), clazz).getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        LOGGER.log(Level.FINE, "getAll returned no results");
        return Collections.emptyList();
    }

    public void deleteAll(){
        try{
            StringBuilder bld = new StringBuilder();
            bld.append("delete from ");
            bld.append(clazz.getSimpleName());
            entityManager.getTransaction().begin();
            entityManager.createQuery(bld.toString()).executeUpdate();
            entityManager.getTransaction().commit();
        } catch(Exception e){
            LOGGER.log(Level.WARNING, e.getMessage());
        }

    }

    public void closeConnections(){
        entityManager.close();
        factory.close();
    }
}
