package repository;

import repository.specifications.ISpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InMemoryRepository<T> implements IRepository<T> {
    protected static final Logger LOGGER = Logger.getLogger(JsonRepository.class.getName());
    protected final Class<T> clazz;
    protected List<T> context;

    public InMemoryRepository(Class<T> clazz) {
        this.clazz = clazz;
        context  = new ArrayList<T>();
    }

    public void add(T t) {
        context.add(t);
    }

    public List<T> getAll() {
        return context;
    }

    public List<T> findBySpecification(ISpecification<T> specification) {
        List<T> resultsList = new ArrayList<T>();

        for (var entity : context){
            if (specification.match(entity)){
                resultsList.add(entity);
            }
        }

        return resultsList;
    }

    public void update(T old, T updated) {
        if(context.contains(old)){
            int oldIdx = context.indexOf(old);
            context.set(oldIdx, updated);
        }
        else{
            LOGGER.log(Level.WARNING, "update failed for entity: " + old.getClass().getName());
        }
    }

    public void remove(T t) {
        if (context.contains(t)){
            int idx = context.indexOf(t);
            context.remove(idx);
        }else{
            LOGGER.log(Level.WARNING, "remove failed for entity: " + t.getClass().getName());
        }
    }

    public void load(){

    }

    public void commit(){

    }
}
