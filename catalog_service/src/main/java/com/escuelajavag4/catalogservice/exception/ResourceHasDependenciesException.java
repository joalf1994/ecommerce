package com.escuelajavag4.catalogservice.exception;

public class ResourceHasDependenciesException extends AppException{
    public ResourceHasDependenciesException(String resource, String dependency) {
        super("HAS_DEPENDENCIES", "No se puede eliminar " + resource + " porque tiene " + dependency + " asociados");
    }
}
