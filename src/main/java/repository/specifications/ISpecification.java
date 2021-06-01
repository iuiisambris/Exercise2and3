package repository.specifications;

import java.util.HashMap;
import java.util.Map;

public interface ISpecification<T> {
    boolean match(T t);
    HashMap<String, String> getFieldValueMap();

    void addNameValuePair(String field, String value);
}
