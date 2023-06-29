package admin.server.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class rolesUtil {
    public static List<String> convertRoles(String roles){
        String cleanString = roles.replaceAll("[\\[\\]\\s]", ""); // Remove square brackets
        List<String> list = new ArrayList<>(Arrays.asList(cleanString.split(",")));
        return list;
    }

    public static String convertListToString(List<String> list){
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));

            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }

}
