
package hello-world.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionLauncher {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        JuniorRef rt = new JuniorRef();

        rt.printClassInfo(rt.getClass());
        rt.printClassFields(rt.getClass());
        rt.printClassMethods(rt.getClass());
        rt.getConstructorsByClass(rt.getClass());


        // new instance object through methods
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("name", "An");
        testMap.put("isJunior", true);
        testMap.put("age", 22);

        // new instance object through constructors
        List<Object> testList = new ArrayList<>();

        testList.add("Serhii");
        testList.add(new Integer(24));
        testList.add(new Boolean(true));

        Object o1 = rt.initClass(JuniorRef.class, testMap);

        Object o2 = rt.initClass(JuniorRef.class, testList);
    }
}
