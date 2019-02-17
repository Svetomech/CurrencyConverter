package ru.svetomech.currencyconverter.ui.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IocContainer {
    private static final Map<String, List<Object>> mStaticContainer = new HashMap<>();
//    private final Map<String, List<Object>> mContainer = new HashMap<>();

//    public void addTransient(String interfaceName, Object implementation) {
//        if (!mContainer.containsKey(interfaceName)) {
//            ArrayList<Object> container = new ArrayList<>();
//            container.add(implementation);
//            mContainer.put(interfaceName, container);
//        } else {
//            mContainer.get(interfaceName).add(implementation);
//        }
//    }

    public static void addSingleton(String interfaceName, Object implementation) {
        if (!mStaticContainer.containsKey(interfaceName)) {
            ArrayList<Object> container = new ArrayList<>();
            container.add(implementation);
            mStaticContainer.put(interfaceName, container);
        } else {
            mStaticContainer.get(interfaceName).add(implementation);
        }
    }

    public static Object resolve(String interfaceName) {
//        if (mContainer.containsKey(interfaceName)) {
//            return mContainer.get(interfaceName).get(0);
//        }
        if (mStaticContainer.containsKey(interfaceName)) {
            return mStaticContainer.get(interfaceName).get(0);
        }
        return null;
    }
}
