package archives.tater.armorrack;

import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ArmorRackUtil {
    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> entriesToMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <K, V> Map<K, V> filterValues(Map<K, V> map, Predicate<V> condition) {
        return map.entrySet().stream().filter(entry -> condition.test(entry.getValue())).collect(entriesToMap());
    }

    public static <K> boolean stackMapsEqual(Map<K, ItemStack> first, Map<K, ItemStack> second) {
        var keys = first.keySet();
        if (!keys.equals(second.keySet()))
            return false;
        for (var key : keys) {
            if (!ItemStack.areItemsAndComponentsEqual(first.get(key), second.get(key)))
                return false;
        }
        return true;
    }

    public static int stackMapHash(Map<?, ItemStack> map) {
        var values = new Object[2 * map.size()];

        var i = 0;
        for (var entry : map.entrySet()) {
            values[i] = entry.getKey();
            values[i + 1] = entry.getValue();
            i++;
        }

        return Objects.hash(values);
    }

    private ArmorRackUtil() {}
}
