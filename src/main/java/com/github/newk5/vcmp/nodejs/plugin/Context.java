package com.github.newk5.vcmp.nodejs.plugin;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.values.primitive.V8ValueUndefined;

import com.maxorator.vcmp.java.plugin.integration.server.Server;
import vlsi.utils.CompactHashMap;

public class Context {

    static CompactHashMap<String, Boolean> functionMap = new CompactHashMap<>();

    public static NodeRuntime v8;
    static Server server;

    public static void load(Server s) {
        server = s;
    }

    public static boolean functionExists(String name) {
        try {
            Boolean val = functionMap.get(name);
            if (val == null) {
                val = v8.getGlobalObject().get(name) instanceof V8ValueUndefined;
                val = !val;
                functionMap.put(name, val);
                return val;
            } else {
                return val;
            }

        } catch (JavetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean playerUpdateFunctionsExist() {
        return false;
    }

  

}
