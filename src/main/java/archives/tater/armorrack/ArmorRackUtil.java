package archives.tater.armorrack;

import java.util.Map;
import java.util.function.Predicate;

public class ArmorRackUtil {
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> filterValues(Map<K, V> map, Predicate<V> condition) {
        return Map.ofEntries(map.entrySet().stream().filter(entry -> condition.test(entry.getValue())).toArray(Map.Entry[]::new));
    }

    private ArmorRackUtil() {}
}
