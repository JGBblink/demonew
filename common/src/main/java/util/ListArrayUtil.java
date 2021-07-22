package util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JinGuiBo
 * @date 7/22/21 2:50 PM
 **/
public class ListArrayUtil<T,V> {
    // 请把字符串数组转list
    public List<String> arrToListStr(String[] arr) {
        return Arrays.asList(arr);
    }

    // 请把字符串list转数组
    public String[] listToArrStr(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    // 请把list数组转成int[]
    public int[] arrToListInt (List<Integer> list) {
        // 不可以toArray，int[]和Integer[]是不同的
        //return list.toArray(new int[list.size()]);
        return list.stream()
                .mapToInt(Integer::valueOf)
                .toArray();
    }

    // 请把int[] 数组转成list
    public List<Integer> arrToListInt (int[] num) {
        // 不可以asList，因为int[]和Integer[]不同
        // return Arrays.<Integer>asList(num);
        return Arrays
                .stream(num)
                .boxed()
                .collect(Collectors.toList());
    }
}
