package com.github.newk5.vcmp.nodejs.plugin.proxies;

import com.caoccao.javet.values.primitive.V8ValueBoolean;
import com.caoccao.javet.values.primitive.V8ValueDouble;
import com.caoccao.javet.values.primitive.V8ValueInteger;
import com.caoccao.javet.values.primitive.V8ValueLong;
import com.caoccao.javet.values.primitive.V8ValueNull;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueArray;
import com.caoccao.javet.values.reference.V8ValueObject;
import com.github.newk5.vcmp.nodejs.plugin.Context;
import com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler;
import static com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler.playerJs;
import com.maxorator.vcmp.java.plugin.integration.generic.Quaternion;
import com.maxorator.vcmp.java.plugin.integration.generic.Rotation2d;
import com.maxorator.vcmp.java.plugin.integration.generic.Vector;
import com.maxorator.vcmp.java.plugin.integration.player.Player;
import com.maxorator.vcmp.java.plugin.integration.vehicle.Vehicle;
import com.maxorator.vcmp.java.plugin.integration.vehicle.VehicleColours;
import com.maxorator.vcmp.java.plugin.integration.vehicle.VehicleDamage;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import vlsi.utils.CompactHashMap;

public class VehicleProxy {

    private static CompactHashMap<String, Method> cachedMethods = new CompactHashMap<>();
    private static Method[] methods = Vehicle.class.getMethods();

    public Object run(Integer id, String method, Object... args) {
        try {
            ServerProxy.syncThread();
            Vehicle p = ServerEventHandler.server.getVehicle(id);
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
            if (method.equalsIgnoreCase("getTurretRotation")) {
                Rotation2d ord = (Rotation2d) m.invoke(p, lst.toArray());
                ServerProxy.closeSyncBlock();
                if (ord != null) {
                    V8ValueObject obj = Context.v8.createV8ValueObject();
                    obj.setProperty("horizontal", ServerEventHandler.entityConverter.toV8Value(Context.v8, ord.horizontal));
                    obj.setProperty("vertical", ServerEventHandler.entityConverter.toV8Value(Context.v8, ord.vertical));
                    return obj;

                }
                return null;

            } else if (method.equalsIgnoreCase("getColours")) {
                VehicleColours vec = (VehicleColours) m.invoke(p, lst.toArray());
                ServerProxy.closeSyncBlock();
                if (vec != null) {
                    V8ValueObject obj = Context.v8.createV8ValueObject();
                    obj.setProperty("primary", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.primary));
                    obj.setProperty("secondary", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.secondary));
                    return obj;

                }
                return null;

            } else if (method.equals("isStreamedForPlayer")) {
                if (lst.get(0) == null) {
                    boolean b = p.isStreamedForPlayer(null);
                    ServerProxy.closeSyncBlock();
                    return b;
                }
                Player target = ServerEventHandler.server.getPlayer((int) lst.get(0));
                boolean b = p.isStreamedForPlayer(target);
                ServerProxy.closeSyncBlock();
                return b;
            } else if (method.equals("getDamage")) {

                int dmg = p.getDamageHex();
                ServerProxy.closeSyncBlock();
                return dmg;
            } else if (method.equals("getSyncController") || method.equals("getOccupant")) {
                Player target = (Player) m.invoke(p, lst.toArray());
                if (target == null) {
                    ServerProxy.closeSyncBlock();
                    return null;
                }

                String playerObj = playerJs.replaceFirst("'#id'", target.getId() + "");
                ServerProxy.closeSyncBlock();
                return playerObj;
            } else if (method.equals("getSyncReason")) {
                int ord = (int) m.invoke(p, lst.toArray());
                ServerProxy.closeSyncBlock();
                return ord;
            } else if (method.equals("getPosition") || method.equals("getRotationEuler") || method.equals("getSpeed") || method.equals("getTurnSpeed") || method.equals("getSpawnPosition") || method.equals("getSpawnRotationEuler")) {
                Vector vec = (Vector) m.invoke(p, lst.toArray());
                ServerProxy.closeSyncBlock();
                V8ValueObject obj = Context.v8.createV8ValueObject();
                if (vec == null) {
                    return null;
                }
                obj.setProperty("x", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.x));
                obj.setProperty("y", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.y));
                obj.setProperty("z", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.z));
                return obj;
            } else if (method.equals("getRotation") || method.equals("getSpawnRotation")) {
                Quaternion vec = (Quaternion) m.invoke(p, lst.toArray());
                ServerProxy.closeSyncBlock();
                V8ValueObject obj = Context.v8.createV8ValueObject();
                if (vec == null) {
                    return null;
                }
                obj.setProperty("x", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.x));
                obj.setProperty("y", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.y));
                obj.setProperty("z", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.z));
                obj.setProperty("w", ServerEventHandler.entityConverter.toV8Value(Context.v8, vec.w));
                return obj;
            }
            ServerProxy.closeSyncBlock();
            return m.invoke(p, lst.toArray());
        } catch (Exception ex) {
            ServerProxy.closeSyncBlock();
            ex.printStackTrace();
        }
        return null;
    }

    public int getTyreStatus(int id, int tyre) {
        ServerProxy.syncThread();
        Vehicle v = ServerEventHandler.server.getVehicle(id);
        if (v != null) {
            VehicleDamage vd = v.getDamage();
            ServerProxy.closeSyncBlock();
            return vd.getTyreStatus(tyre);
        }
        ServerProxy.closeSyncBlock();
        return -1;
    }

    public int getDoorStatus(int id, int tyre) {
        ServerProxy.syncThread();
        Vehicle v = ServerEventHandler.server.getVehicle(id);
        if (v != null) {
            VehicleDamage vd = v.getDamage();
            ServerProxy.closeSyncBlock();
            return vd.getDoorStatus(tyre).ordinal();
        }
        ServerProxy.closeSyncBlock();
        return -1;
    }

    public void setDoorStatus(int id, int door, int status) {
        ServerProxy.syncThread();
        Vehicle v = ServerEventHandler.server.getVehicle(id);
        if (v != null) {
            VehicleDamage vd = v.getDamage();
            vd.setDoorStatus(door, status);
            v.setDamage(vd.damage);
        }
        ServerProxy.closeSyncBlock();
    }

    public void setTyreStatus(int id, int trye, int status) {
        ServerProxy.syncThread();
        Vehicle v = ServerEventHandler.server.getVehicle(id);
        if (v != null) {
            VehicleDamage vd = v.getDamage();
            vd.setTyreStatus(trye, status);
            v.setDamage(vd.damage);
        }
        ServerProxy.closeSyncBlock();
    }

    public int getPanelStatus(int id, int panel) {
        ServerProxy.syncThread();
        Vehicle v = ServerEventHandler.server.getVehicle(id);
        if (v != null) {
            VehicleDamage vd = v.getDamage();
            ServerProxy.closeSyncBlock();
            return vd.getPanelStatus(panel);
        }
        ServerProxy.closeSyncBlock();
        return -1;
    }

    public void setPanelStatus(int id, int panel, int status) {
        ServerProxy.syncThread();
        Vehicle v = ServerEventHandler.server.getVehicle(id);
        if (v != null) {
            VehicleDamage vd = v.getDamage();
            vd.setPanelStatus(panel, status);
            v.setDamage(vd.damage);
        }
        ServerProxy.closeSyncBlock();
    }

}
