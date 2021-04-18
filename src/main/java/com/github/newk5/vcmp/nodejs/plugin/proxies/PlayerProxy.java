package com.github.newk5.vcmp.nodejs.plugin.proxies;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.values.primitive.V8ValueBoolean;
import com.caoccao.javet.values.primitive.V8ValueDouble;
import com.caoccao.javet.values.primitive.V8ValueInteger;
import com.caoccao.javet.values.primitive.V8ValueLong;
import com.caoccao.javet.values.primitive.V8ValueNull;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueArray;
import com.caoccao.javet.values.reference.V8ValueObject;
import com.github.newk5.vcmp.nodejs.plugin.Context;
import static com.github.newk5.vcmp.nodejs.plugin.Context.v8;
import com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler;
import static com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler.objectJs;
import static com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler.playerJs;
import static com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler.vehicleJs;
import com.maxorator.vcmp.java.plugin.integration.generic.Vector;
import com.maxorator.vcmp.java.plugin.integration.placeable.GameObject;
import com.maxorator.vcmp.java.plugin.integration.player.Player;
import com.maxorator.vcmp.java.plugin.integration.player.PlayerImpl;
import com.maxorator.vcmp.java.plugin.integration.vehicle.Vehicle;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import vlsi.utils.CompactHashMap;

public class PlayerProxy {

    private static CompactHashMap<String, Method> cachedMethods = new CompactHashMap<>();
    private static Method[] methods = PlayerImpl.class.getMethods();

    public void sendStream(Integer id, byte[] b) throws JavetException {
        Player p = ServerEventHandler.server.getPlayer(id);

        ServerEventHandler.server.sendScriptData(p, b);
    }

    public Object run(Integer id, String method, Object... args) {
        try {
            Player p = ServerEventHandler.server.getPlayer(id);
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
                if (v instanceof V8ValueNull) {
                    lst.add(null);
                } else if (v instanceof V8ValueString) {
                    lst.add(((V8ValueString) v).toPrimitive());
                } else if (v instanceof V8ValueBoolean) {
                    lst.add(((V8ValueBoolean) v).toPrimitive());
                } else if (v instanceof V8ValueInteger) {
                    lst.add(((V8ValueInteger) v).toPrimitive());
                } else if (v instanceof V8ValueDouble) {
                    lst.add(Float.valueOf(((V8ValueDouble) v).toPrimitive() + ""));
                } else if (v instanceof V8ValueLong) {
                    lst.add(((V8ValueLong) v).toPrimitive());
                }
            });

            if (method.equalsIgnoreCase("setSpectateTarget")) {
                if (lst.get(0) == null) {
                    p.setSpectateTarget(null);
                    return null;
                }
                Player target = ServerEventHandler.server.getPlayer((int) lst.get(0));
                if (target != null) {
                    p.setSpectateTarget(target);
                }
                return null;

            } else if (method.equals("getSpectateTarget")) {
                Player target = p.getSpectateTarget();
                if (target == null) {
                    return null;
                }

                String playerObj = playerJs.replaceFirst("'#id'", target.getId() + "");
                return playerObj;

            } else if (method.equals("isStreamedForPlayer")) {
                if (lst.get(0) == null) {
                    return p.isStreamedForPlayer(null);
                }
                Player target = ServerEventHandler.server.getPlayer((int) lst.get(0));
                return p.isStreamedForPlayer(target);
            } else if (method.equals("putInVehicle")) {
                int vid = (int) lst.get(0);
                int slot = (int) lst.get(1);
                boolean b1 = (boolean) lst.get(2);
                boolean b2 = (boolean) lst.get(3);

                p.putInVehicle(ServerEventHandler.server.getVehicle(vid), slot, b1, b2);
                return null;
            } else if (method.equals("getColour")) {
                V8ValueInteger obj = Context.v8.createV8ValueInteger(p.getColourHex());
                return obj;

            } else if (method.equals("getVehicle") || method.equals("getStandingOnVehicle")) {
                Vehicle v = (Vehicle) m.invoke(p, lst.toArray());
                if (v == null) {
                    return null;
                }

                String vehicleObjId = vehicleJs.replaceFirst("'#id'", v.getId() + "");
                return vehicleObjId;

            } else if (method.equals("getStandingOnObject")) {
                GameObject v = (GameObject) m.invoke(p, lst.toArray());
                if (v == null) {
                    return null;
                }

                String gameObj = objectJs.replaceFirst("'#id'", v.getId() + "");
                return gameObj;

            } else if (method.equals("getPosition") || method.equals("getSpeed") || method.equals("getAimPosition") || method.equals("getAimDirection")) {
                V8ValueObject obj = Context.v8.createV8ValueObject();
                Vector vec = null;
                if (method.equals("getPosition")) {
                    vec = p.getPosition();
                } else if (method.equals("getSpeed")) {
                    vec = p.getSpeed();
                } else if (method.equals("getAimPosition")) {
                    vec = p.getAimPosition();
                } else if (method.equals("getAimDirection")) {
                    vec = p.getAimDirection();
                }
                obj.setProperty("x", ServerEventHandler.entityConverter.toV8Value(v8, vec.x));
                obj.setProperty("y", ServerEventHandler.entityConverter.toV8Value(v8, vec.y));
                obj.setProperty("z", ServerEventHandler.entityConverter.toV8Value(v8, vec.z));
                return obj;

            }

            return m.invoke(p, lst.toArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
