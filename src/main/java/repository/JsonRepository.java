package repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class JsonRepository<T> extends InMemoryRepository<T> {

    public JsonRepository(Class<T> clazz){
        super(clazz);
    }

    @Override
    public void load(){
        try {
            File in = new File(clazz.getSimpleName() + ".json");
            var objectMapper = new ObjectMapper();
            var tmp = objectMapper.readValue(in, Array.newInstance(clazz, 0).getClass());
            context = new ArrayList<T>(Arrays.<T>asList((T[])tmp));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public void commit(){
        try {
            File file = new File(clazz.getSimpleName() + ".json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, context);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING,e.getMessage());
        }
    }

}
