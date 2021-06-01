package repository;

import model.Settings;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RepositoryFactory<T> {
    protected static final Logger LOGGER = Logger.getLogger(RepositoryFactory.class.getName());
    protected final Class<T> clazz;

    public RepositoryFactory(Class<T> clazz){
        this.clazz = clazz;
    }

    public IRepository<T> create(){
        try{

            JsonRepository<Settings> settingsJsonRepository = new JsonRepository<>(Settings.class);
            settingsJsonRepository.load();
            var option = settingsJsonRepository.getAll().stream().findFirst();

            if (!option.isPresent()){
                LOGGER.log(Level.INFO, "Settings not present, using default json repository type");
                return new JsonRepository<T>(clazz);
            }

            var value = option.get();

           if (value.getKey().equals("repoType")){
               if (value.getValue().equals("inMemory")){
                   return new InMemoryRepository<T>(clazz) ;
               }
               if (value.getValue().equals("json")) {
                   return new JsonRepository<T>(clazz);
               }
               if (value.getValue().equals("database")){
                   return new DBRepository<T>(clazz);
               }
           }

        }catch (Exception e){
            LOGGER.log(Level.WARNING, e.getMessage());
            throw e;
        }

        return null;
    }
}
