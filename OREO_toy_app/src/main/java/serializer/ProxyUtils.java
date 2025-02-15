package serializer;

public class ProxyUtils {

    public static Class<?> getUnproxiedClass(Object instance) {
        if (instance == null) {
            return null;
        }
        Class<?> clazz = instance.getClass();
        while (clazz != null && clazz != Object.class) {
            if (!isProxyClass(clazz)) {
                return clazz;
            }
            clazz = clazz.getSuperclass();
        }
        return instance.getClass();
    }

    private static boolean isProxyClass(Class<?> clazz) {
        return clazz.getName().contains("$Proxy") 
            || clazz.getName().contains("_Subclass") 
            || clazz.getSimpleName().contains("Weld");
    }
}