package com.github.newk5.vcmp.nodejs.plugin.proxies;

import com.caoccao.javet.values.primitive.V8ValueBoolean;
import com.caoccao.javet.values.primitive.V8ValueDouble;
import com.caoccao.javet.values.primitive.V8ValueInteger;
import com.caoccao.javet.values.primitive.V8ValueLong;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueArray;
import com.caoccao.javet.values.reference.V8ValueObject;
import com.github.newk5.vcmp.nodejs.plugin.Context;
import com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler;
import com.maxorator.vcmp.java.plugin.integration.generic.Vector;
import com.maxorator.vcmp.java.plugin.integration.placeable.Pickup;
import com.maxorator.vcmp.java.plugin.integration.player.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import vlsi.utils.CompactHashMap;

public class PickupProxy {

    private static CompactHashMap<String, Method> cachedMethods = new CompactHashMap<>();
    private static Method[] methods = Pickup.class.getMethods();

    public Object run(Integer id, String method, Object... args) {
        try {
            Pickup p = ServerEventHandler.server.getPickup(id);
            Method m = cachedMethods.get(method);
            if (m == null) {
                m = Arrays
                        .stream(methods)
                        .filter(me -> me.getName()
                        .equals(method))
                        .findAny().get();
                cachedMethods.put(method, m);

            }

            V8ValueArray arr = (V8ValueArray) args[0];
            List<Object> lst = new ArrayList<>();

            arr.forEach((k, v) -> {
                if (v instanceof V8ValueString) {
                    lst.add(((V8ValueString) v).toPrimitive());
                } else if (v instanceof V8ValueBoolean) {
                    lst.add(((V8ValueBoolean) v).toPrimitive());
                } else if (v instanceof V8ValueInteger) {
                    lst.add(((V8ValueInteger) v).toPrimitive());
                } else if (v instanceof V8ValueDouble) {
                    lst.add(((V8ValueDouble) v).toPrimitive());
                } else if (v instanceof V8ValueLong) {
                    lst.add(((V8ValueLong) v).toPrimitive());
                }
            });

            if (method.equals("isStreamedForPlayer")) {
                if (lst.get(0) == null) {
                    return p.isStreamedForPlayer(null);
                }
                Player target = ServerEventHandler.server.getPlayer((int) lst.get(0));
                return p.isStreamedForPlayer(target);
            } else if (method.equals("getPosition")) {
                Vector vec = (Vector) m.invoke(p, lst.toArray());
                V8ValueObject obj = Context.v8.createV8ValueObject();
                if (vec == null) {
                    return null;
                }
                obj.setProperty("x", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.x));
                obj.setProperty("y", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.y));
                obj.setProperty("z", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.z));
                return obj;
            }

            return m.invoke(p, lst.toArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
