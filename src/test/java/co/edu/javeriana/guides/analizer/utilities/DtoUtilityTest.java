package co.edu.javeriana.guides.analizer.utilities;

import org.junit.jupiter.api.Assertions;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

public class DtoUtilityTest { // NOSONAR

    public static void testDto(Class<?> classDto) throws InstantiationException, IllegalAccessException, InvocationTargetException, DatatypeConfigurationException {
        Object dto = classDto.newInstance();
        Method[] methods = classDto.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                Class<?>[] params = method.getParameterTypes();
                List<Object> args = getParameters(params);
                if (!args.isEmpty()) {
                    method.invoke(dto, args.toArray());
                }
            }
        }
        for (Method method : methods) {
            if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
                method.invoke(dto);
            }
        }
        Constructor<?>[] constructors = classDto.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] params = constructor.getParameterTypes();
            List<Object> args = getParameters(params);
            dto = constructor.newInstance(args.toArray());
            Assertions.assertNotNull(dto);
        }
        Assertions.assertNotNull(dto.toString());
    }

    private static List<Object> getParameters(Class<?>[] params)
            throws DatatypeConfigurationException {
        List<Object> args = new ArrayList<>();
        for (Class<?> param : params) {
            if (param.equals(Integer.class) || param.equals(int.class) || param.equals(float.class)) {
                args.add(1);
            } else if (param.equals(String.class)) {
                args.add("2018-09-13");
            } else if (param.equals(Boolean.class) || param.equals(boolean.class)) {
                args.add(true);
            } else if (param.equals(Long.class) || param.equals(long.class)) {
                args.add(1L);
            } else if (param.equals(Double.class) || param.equals(double.class)) {
                args.add(1d);
            } else if (param.equals(Map.class)) {
                args.add(Collections.EMPTY_MAP);
            } else if (param.equals(List.class)) {
                args.add(Collections.EMPTY_LIST);
            } else if (param.equals(Set.class)) {
                args.add(Collections.EMPTY_SET);
            } else if (param.equals(byte.class)) {
                args.add((byte) 1);
            } else if (param.equals(BigDecimal.class)) {
                args.add(BigDecimal.ZERO);
            } else if (param.equals(Float.class)) {
                args.add(Float.MIN_NORMAL);
            } else if (param.equals(BigInteger.class)) {
                args.add(BigInteger.ONE);
            } else if (param.equals(XMLGregorianCalendar.class)) {
                GregorianCalendar date = new GregorianCalendar();
                args.add(DatatypeFactory.newInstance().newXMLGregorianCalendar(date));
            } else if (param.equals(Date.class)) {
                args.add(new Date());
            } else if (param.equals(Timestamp.class)) {
                args.add(new Timestamp(1));
            } else if (param.equals(Byte.class)) {
                args.add(Byte.valueOf("123"));
            }
        }
        return args;
    }

}
