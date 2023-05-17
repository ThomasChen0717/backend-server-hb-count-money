package guide.server.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Convertor extends Convert {
    public Convertor() {
    }

    public static <S, T> List<T> convert(List<S> from, Class<T> to) {
        return (List)from.stream().filter(Objects::nonNull).map((element) -> {
            return BeanUtil.copyProperties(element, to, new String[0]);
        }).collect(Collectors.toList());
    }

    public static <S, T> List<T> convert(List<S> from, Class<T> to, Predicate<S> predicate) {
        return (List)from.stream().filter(predicate).map((element) -> {
            return BeanUtil.copyProperties(element, to, new String[0]);
        }).collect(Collectors.toList());
    }

    public static <T, R> List<R> convert(List<T> from, Function<T, R> function) {
        return (List)from.stream().filter(Objects::nonNull).map(function).collect(Collectors.toList());
    }

    public static <T, R> List<R> convert(List<T> from, Function<T, R> function, Predicate<T> predicate) {
        return (List)from.stream().filter(predicate).map(function).collect(Collectors.toList());
    }

    public static <T, R> List<R> convertAndFilterNull(List<T> from, Function<T, R> function) {
        return (List)from.stream().filter(Objects::nonNull).map(function).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
