package util;


import cn.hutool.core.collection.CollectionUtil;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 列表工具类。
 */
public final class ListUtils {

    public static <T, R> List<R> extract(List<T> source, Function<T, R> extractor) {
        return Optional.ofNullable(source).orElseGet(Collections::emptyList).stream()
                .map(extractor).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    public static <T, R> List<R> extract(List<T> source, Predicate<T> filter, Function<T, R> extractor) {
        return Optional.ofNullable(source).orElseGet(Collections::emptyList).stream().filter(filter)
                .map(extractor).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    public static <K, V> Map<K, V> toMap(List<V> list, Function<V, K> key) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList).stream()
                .collect(Collectors.toMap(key, Function.identity(), (v, v2) -> v2));
    }

    public static <K, V, T> Map<K, V> toMap(List<T> list, Function<T, K> key, Function<T, V> value) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList).stream()
                .collect(Collectors.toMap(key, value, (v, v2) -> v2));
    }

    public static <K, V> Map<K, List<V>> groupingBy(List<V> list, Function<V, K> key) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList).stream()
                .collect(Collectors.groupingBy(key));
    }

    public static <K, V, T> Map<K, List<V>> groupingBy(List<T> list, Function<T, K> key, Function<T, V> value) {
        return Optional.ofNullable(list).orElseGet(Collections::emptyList).stream()
                .collect(Collectors.groupingBy(key, Collectors.mapping(value, Collectors.toList())));
    }

    public static <T> List<T> toList(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        if (iterator == null) {
            return list;
        }
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <T, R extends Comparable> Optional<R> min(Collection<T> list, Function<T, R> function) {
        return list.stream().filter(Objects::nonNull).map(function).filter(Objects::nonNull)
                .min(Comparator.comparing(Function.identity()));
    }

    public static <T, R extends Comparable> Optional<R> max(Collection<T> list, Function<T, R> function) {
        return list.stream().filter(Objects::nonNull).map(function).filter(Objects::nonNull)
                .max(Comparator.comparing(Function.identity()));
    }

    public static <F, R> List<R> merge(List<F> fromList, Function<? super F, List<? extends R>> function) {
        return fromList.stream().flatMap(item -> function.apply(item).stream()).collect(Collectors.toList());
    }

}
