
package hello-world.reflection;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

public class JuniorRef<T> {

    // different fields;
    private String name;
    public Integer age;
    protected Boolean isJunior;


    // different constructions
    public JuniorRef() {

    }

    public JuniorRef(String name) {
        this.name = name;
    }
    public JuniorRef(Integer age) {
        this.age = age;
    }

    public JuniorRef(Boolean isJunior) {
        this.isJunior = isJunior;
    }
    public JuniorRef(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    public JuniorRef(String name, Integer age, Boolean isJunior) {
        this.name = name;
        this.age = age;
        this.isJunior = isJunior;
    }

    // print superclass name and name of class
    public void printClassInfo(Class c) {
        System.out.println("=== printClassInfo() ===");
        System.out.println("Superclass: " + c.getSuperclass());
        System.out.println("Class name: " + c.getName() + " or just " + c.getSimpleName());
    }

    // print method's name of some class
    public void printClassMethods(Class c) {
        System.out.println("=== printClassMethods() ===");
        Method[] methods = c.getDeclaredMethods();
//        getConstructorsByClass(c);
        for (Method method : methods) {

            System.out.println("Method: " + method);
        }
    }
    // print constructors name of some class
    public void getConstructorsByClass(Class c) {
        System.out.println("=== getConstructorsByClass() ===");
        Constructor[] constructors = c.getConstructors();
        for (Constructor cons : constructors) {
            System.out.println("Construction: " + cons);
        }
    }

    // print all type of fields in class
    public void printClassFields(Class c) {
        System.out.println("=== printClassFields() ===");
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            System.out.println("Field modifier: " + checkModifiers(f));
            System.out.println("Field type: " + f.getType().getSimpleName());
            System.out.println("Field name: " + f.getName());

        }
    }

    // frame 23 and 25
    public T initClass(Class c, Map<String, Object> map) throws IllegalAccessException, InstantiationException {

        T obj = (T) c.newInstance();
        Field[] fields = c.getDeclaredFields();
        Object[] keys = map.keySet().toArray();

        for (Field f : fields) {
            for (Object key : keys) {
                if (f.getName().equals(key)) {
                    f.setAccessible(true);
                    f.set(obj, map.get(key));
                }
            }

        }
        System.out.println("=== new instance object through methods ===");
        fieldsTest(obj);
        return obj;
    }

    //frame 24
    public T initClass(Class c, List<Object> list) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

        T obj = (T) c.newInstance();

        Class[] typesOfList = getTypesOfList(list);

        if (list.size() == 1) {
            obj = (T) c.getConstructor(typesOfList).newInstance(list.get(0));
        } else if (list.size() == 2) {
            obj = (T) c.getConstructor(typesOfList).newInstance(list.get(0), list.get(1));
        } else if (list.size() == 3) {
            obj = (T) c.getConstructor(typesOfList).newInstance(list.get(0), list.get(1),list.get(2));
//        } else if (list.size() == 4) {
//            obj = (T) c.getConstructor(typesOfList).newInstance(list.get(0), list.get(1), list.get(2), list.get(3));
//        } else if (list.size() == 5) {
//            obj = (T) c.getConstructor(typesOfList).newInstance(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));
        }
        System.out.println("=== new instance object through constructors ===");
        fieldsTest(obj);
        return obj;
    }

    private void fieldsTest(T obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field f :fields) {
            System.out.print(f.getType().getSimpleName() + " " + f.getName() + " ");
            System.out.println(f.get(obj));
        }
    }

    private Class[] getTypesOfList(List<Object> list) {
        Class[] tOfL = new Class[list.size()];
        for (int i = 0; i < list.size(); i++) {
            tOfL[i] = list.get(i).getClass();
        }
        return tOfL;
    }
    private String checkModifiers(Field f) {
        int mod = f.getModifiers();
        if (Modifier.isPublic(mod)) {
            return "public";
        } else if (Modifier.isPrivate(mod)) {
            return "private";
        } else if (Modifier.isProtected(mod)) {
            return "protected";
        } else if (Modifier.isFinal(mod)) {
            return "final";
        }
        return "not public, private, protected or final modifiers";
    }

}
