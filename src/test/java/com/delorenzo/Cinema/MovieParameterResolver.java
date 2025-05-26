package com.delorenzo.Cinema;

import com.delorenzo.Cinema.entity.Movie;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.*;

public class MovieParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return Objects.equals(parameter.getParameterizedType().getTypeName(), "java.util.Map<java.lang.String, com.delorenzo.Cinema.entity.Movie>");
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<String, Movie> movies = new HashMap<>();
        movies.put("Il Padrino 1",  new Movie(1L,"Il Padrino 1", "F. F. Coppola","1970", 180, false,10.0));
        movies.put("Il Padrino 2",  new Movie(2L,"Il Padrino 2", "F. F. Coppola","1980", 180, false,9.0));
        movies.put("Il Padrino 3",  new Movie(3L,"Il Padrino 3", "F. F. Coppola","1990", 180, false,8.0));
        movies.put("Shining",  new Movie(4L,"Shining", "S. Kubrick","1970", 180, false,10.0));
        movies.put("Full Metal Jacket",  new Movie(5L,"Full Metal Jacket", "S. Kubrick","1971", 180, false,10.0));
        movies.put("Il dottor Stranamore",  new Movie(6L,"Il Dottor Stranamore", "S. Kubrick","1973", 180, false,10.0));

        return movies;
    }
}
