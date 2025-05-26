package com.delorenzo.Cinema;

import com.delorenzo.Cinema.entity.Room;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RoomParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return Objects.equals(parameter.getParameterizedType().getTypeName(), "java.util.Map<java.lang.String, com.delorenzo.Cinema.entity.Room>");
    }


    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<String, Room> rooms = new HashMap<>();
        rooms.put("Troisi",  new Room(1L,"Sala ", 50, false));
        rooms.put("Mastroianni",  new Room(2L,"Sala Mastroianni", 300, false));
        rooms.put("Bertolucci",  new Room(3L,"Sala Bertolucci", 200, false));
        rooms.put("Magnani",  new Room(4L,"Sala Magnani", 70, false));
        rooms.put("Vitti",  new Room(5L,"Sala Vitti", 130, false));
        return rooms;

    }
}
