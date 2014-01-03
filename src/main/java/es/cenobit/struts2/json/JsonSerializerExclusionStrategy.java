package es.cenobit.struts2.json;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import es.cenobit.struts2.json.annotations.NoJson;

public class JsonSerializerExclusionStrategy implements ExclusionStrategy {

    @SuppressWarnings("rawtypes")
    private final Set toExcludeClasses;
    private final Set<String> fieldsToExclude;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public JsonSerializerExclusionStrategy(Set<String> fieldsToExclude, Class<?>... classes) {
        this.fieldsToExclude = fieldsToExclude;
        this.toExcludeClasses = new LinkedHashSet(Arrays.asList(classes));
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        if (f.getAnnotation(NoJson.class) != null) {
            return true;
        }

        if (fieldsToExclude == null || fieldsToExclude.isEmpty()) {
            return false;
        }

        for (String fieldToExclude : fieldsToExclude) {
            if (fieldToExclude == null || fieldToExclude.lastIndexOf('.') == -1) {
                continue;
            }

            String[] split = fieldToExclude.split("\\.");
            if (split != null && split.length > 1) {
                String fieldClassName = (split[split.length - 2]).toLowerCase();
                String fieldAttrName = (split[split.length - 1]).toLowerCase();

                String className = (className(f)).toLowerCase();
                String classAttrName = (f.getName()).toLowerCase();

                if (className.equals(fieldClassName) && classAttrName.equals(fieldAttrName)) {
                    return true;
                }
            }
        }

        return fieldsToExclude.contains(f.getName());
    }

    private String className(FieldAttributes f) {
        String declaringClass = f.getDeclaringClass().toString();
        if (declaringClass.indexOf('$') != -1) { // Inner class
            int index = declaringClass.lastIndexOf('$');
            return declaringClass.substring(index + 1, declaringClass.length());
        } else if (declaringClass.indexOf('.') != -1) { // Public class
            int index = declaringClass.lastIndexOf('.');
            return declaringClass.substring(index + 1, declaringClass.length());
        }

        return declaringClass;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return toExcludeClasses.contains(clazz);
    }

}
