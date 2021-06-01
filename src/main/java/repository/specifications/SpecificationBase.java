package repository.specifications;

import java.util.HashMap;

public class SpecificationBase<T> implements ISpecification<T> {

    private final HashMap<String, String> fieldValueMap;

    public SpecificationBase(){
        fieldValueMap = new HashMap<>();
    }

    @Override
    public boolean match(T t) {
        return false;
    }

    @Override
    public HashMap<String, String> getFieldValueMap() {
        return fieldValueMap;
    }

    @Override
    public void addNameValuePair(String field, String value) {
        fieldValueMap.put(field, value);
    }
}
