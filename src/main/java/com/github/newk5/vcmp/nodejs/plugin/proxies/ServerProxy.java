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
import static com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler.checkpointJs;
import static com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler.objectJs;
import static com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler.pickupJs;
import static com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler.playerJs;
import static com.github.newk5.vcmp.nodejs.plugin.ServerEventHandler.vehicleJs;
import com.maxorator.vcmp.java.plugin.integration.placeable.CheckPoint;
import com.maxorator.vcmp.java.plugin.integration.placeable.GameObject;
import com.maxorator.vcmp.java.plugin.integration.placeable.Pickup;
import com.maxorator.vcmp.java.plugin.integration.player.Player;
import com.maxorator.vcmp.java.plugin.integration.server.CoordBlipInfo;
import com.maxorator.vcmp.java.plugin.integration.server.KeyBind;
import com.maxorator.vcmp.java.plugin.integration.server.MapBounds;
import com.maxorator.vcmp.java.plugin.integration.server.Server;
import com.maxorator.vcmp.java.plugin.integration.server.WastedSettings;
import com.maxorator.vcmp.java.plugin.integration.vehicle.Vehicle;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import vlsi.utils.CompactHashMap;

public class ServerProxy {

    private static CompactHashMap<String, Method> cachedMethods = new CompactHashMap<>();
    private static Method[] methods = Server.class.getMethods();

    private V8ValueObject tempObj;

    public void overrideObjectGetters() throws JavetException {
        String playerObj = playerJs.replaceFirst("'#id'", "arg0");
        String playerObjId = playerJs.replaceFirst("'#id'", "id");
        String playerObjGetAll = playerJs.replaceFirst("'#id'", "ids[i]");
        String vehicleObjGetAll = vehicleJs.replaceFirst("'#id'", "ids[i]");

        String gameObj = objectJs.replaceFirst("'#id'", "arg0");
        String pobj = pickupJs.replaceFirst("'#id'", "arg0");
        String vehicleObj = vehicleJs.replaceFirst("'#id'", "arg0");
        String chObj = checkpointJs.replaceFirst("'#id'", "arg0");

        String chObjId = checkpointJs.replaceFirst("'#id'", "id");
        String gameObjId = objectJs.replaceFirst("'#id'", "id");
        String pobjId = pickupJs.replaceFirst("'#id'", "id");
        String vehicleObjId = vehicleJs.replaceFirst("'#id'", "id");

        Context.v8.getExecutor(""
                + "server.print = function() {    var result = [];     for (var id in this) {   try {  if (typeof(this[id]) == \"function\") {  result.push(id + \": \" + this[id].toString().split(\")\")[0]+\")\" );  }  } catch (err) { result.push(id + \": inaccessible\");   }    }  console.log(result);  }\n"
                + "server.sendClientMessage = function (  recipient,  colourHex,  message ){  __ServerProxy.run('sendClientMessage', [recipient.id, colourHex, message]);  };\n"
                + "server.getObject = function ( arg0 ){ if (__ServerProxy.objectExists(arg0)) { return  " + gameObj + ";  } return null;   };\n"
                + "server.sendGameMessage = function ( arg0, arg1, arg2 ){ __ServerProxy.run('sendGameMessage', [arg0.id, arg1, arg2]); };\n"
                + "server.addPlayerClass = function ( arg0, arg1, arg2 ){ __ServerProxy.run('addPlayerClass', arguments); };\n"
                + "server.createExplosion = function ( arg0, arg1, arg2, arg3, arg4, arg5, arg6 ){  __ServerProxy.createExplosion( arg0, arg1, arg2, arg3, arg4, arg5==null ? null : arg5.id, arg6  ); };"
                + "server.createObject = function ( arg0, arg1, arg2, arg3, arg4, arg5 ){ const id =  __ServerProxy.run('createObject', arguments);  return  " + gameObjId + "; };\n"
                + "server.getCoordBlipInfo = function ( arg0 ){ return  __ServerProxy.run('getCoordBlipInfo', arguments); };\n"
                + "server.getPlayer = function ( arg0 ){ if (__ServerProxy.playerExists(arg0)) { return  " + playerObj + ";  } return null;   };\n"
                + "server.getAllPlayers = function ( ){ let players = []; const ids =  __ServerProxy.getJSPlayerIdsArray(); for (let i = 0; i < ids.length; i++){ players.push(" + playerObjGetAll + "); } return players; };\n"
                + "server.findPlayer = function ( arg0 ){ const id =  __ServerProxy.getPlayerIdByName(arg0); if (id != null) {  return  " + playerObjId + "; } return null; };\n"
                + "server.createVehicle = function ( arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7 ){ const id =  __ServerProxy.run('createVehicle', arguments); return  " + vehicleObjId + ";  };\n"
                + "server.getVehicle = function ( arg0 ){ if (__ServerProxy.vehicleExists(arg0)) { return  " + vehicleObj + ";  } return null;   };\n"
                + "server.getAllVehicles = function ( ){ let vehs = []; const ids = __ServerProxy.getJSVehIdsArray(); for (let i = 0; i < ids.length; i++){ vehs.push(" + vehicleObjGetAll + "); } return vehs; };\n"
                + "server.getPickup = function ( arg0 ){ if (__ServerProxy.pickupExists(arg0)) { return  " + pobj + ";  } return null;   };\n"
                + "server.createPickup = function ( arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7 ){ const id = __ServerProxy.run('createPickup', arguments); return  " + pobjId + ";   };\n"
                + "server.getCheckPoint = function ( arg0 ){ if (__ServerProxy.checkPointExists(arg0)) { return  " + chObj + ";  } return null;   };\n"
                + "server.createCheckPoint = function ( arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10 ){  const id =  __ServerProxy.run('createCheckPoint', [arg0== null ? null: arg0.id, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10] );  return  " + chObjId + ";  };")
                .executeVoid();
    }

    public void createExplosion(int worldId, int type, float x, float y, float z, Integer responsiblePlayer, boolean atGroundLevel) {
        ServerEventHandler.server.createExplosion(worldId, type, x, y, z, responsiblePlayer == null ? null : ServerEventHandler.server.getPlayer(responsiblePlayer), atGroundLevel);
    }

    public V8ValueArray getJSVehIdsArray() {

        try {
            V8ValueArray arr = v8.createV8ValueArray();
            Arrays.stream(ServerEventHandler.server.getAllVehicles()).
                    map(p -> p.getId()).
                    forEach(i -> {
                        try {
                            arr.push(i);
                        } catch (JavetException ex) {
                            ex.printStackTrace();
                        }
                    });

            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public V8ValueArray getJSPlayerIdsArray() {

        try {
            V8ValueArray arr = v8.createV8ValueArray();
            Arrays.
                    stream(ServerEventHandler.server.getAllPlayers()).
                    map(p -> p.getId()).
                    forEach(i -> {
                        try {
                            arr.push(i);
                        } catch (JavetException ex) {
                            ex.printStackTrace();
                        }
                    });

            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean checkPointExists(Integer id) {
        return ServerEventHandler.server.getCheckPoint(id) != null;
    }

    public boolean pickupExists(Integer id) {
        return ServerEventHandler.server.getPickup(id) != null;
    }

    public boolean objectExists(Integer id) {
        return ServerEventHandler.server.getObject(id) != null;
    }

    public boolean vehicleExists(Integer id) {
        return ServerEventHandler.server.getVehicle(id) != null;
    }

    public boolean playerExists(Integer id) {
        return ServerEventHandler.server.getPlayer(id) != null;
    }

    public Integer getPlayerIdByName(String name) {
        Player p = ServerEventHandler.server.findPlayer(name);
        if (p == null) {
            return null;
        }
        return p.getId();
    }

    public Object run(String methodName, Object... args) {
        try {
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

            Method m = cachedMethods.get(methodName);
            if (m == null) {
                m = Arrays
                        .stream(methods)
                        .filter(me -> me.getName().equals(methodName))
                        .filter(me -> me.getParameterCount() == lst.size()) //make sure method signature matches
                        .findAny().get();
                cachedMethods.put(methodName, m);

            }

            Object o = null;
            if (!methodName.equals("sendGameMessage") && !methodName.equals("sendClientMessage")) {
                if (methodName.equals("createCheckPoint") && lst.get(0) != null) {
                    int id = (int) lst.get(0);
                    lst.set(0, ServerEventHandler.server.getPlayer(id));
                }
                o = m.invoke(ServerEventHandler.server, lst.toArray());
                if (o == null) {
                    return null;
                }
            }

            if (methodName.equals("shutdownServer")) {
                System.exit(0);
            } else if (methodName.equals("createCheckPoint")) {
                CheckPoint ch = (CheckPoint) o;
                return ch.getId();

            } else if (methodName.equals("createVehicle")) {
                Vehicle ch = (Vehicle) o;
                return ch.getId();

            } else if (methodName.equals("createPickup")) {
                Pickup ch = (Pickup) o;
                return ch.getId();

            } else if (methodName.equals("getKeyBind")) {
                KeyBind kb = (KeyBind) o;
                V8ValueObject obj = v8.createV8ValueObject();

                obj.setProperty("keys", ServerEventHandler.entityConverter.toV8Value(v8, kb.keys));
                obj.setProperty("id", ServerEventHandler.entityConverter.toV8Value(v8, kb.id));
                obj.setProperty("boolean", ServerEventHandler.entityConverter.toV8Value(v8, kb.onRelease));
                return obj;

            } else if (methodName.equals("getWorldBounds")) {
                MapBounds mb = (MapBounds) o;
                V8ValueObject obj = v8.createV8ValueObject();
                obj.setProperty("maxX", ServerEventHandler.entityConverter.toV8Value(v8, mb.maxX));
                obj.setProperty("maxY", ServerEventHandler.entityConverter.toV8Value(v8, mb.maxY));
                obj.setProperty("minX", ServerEventHandler.entityConverter.toV8Value(v8, mb.minX));
                obj.setProperty("minY", ServerEventHandler.entityConverter.toV8Value(v8, mb.minY));
                return obj;

            } else if (methodName.equals("getWastedSettings")) {

                WastedSettings mb = (WastedSettings) o;
                V8ValueObject obj = v8.createV8ValueObject();
                obj.setProperty("corpseFadeDuration", ServerEventHandler.entityConverter.toV8Value(v8, mb.corpseFadeDuration));
                obj.setProperty("corpseFadeStart", ServerEventHandler.entityConverter.toV8Value(v8, mb.corpseFadeStart));
                obj.setProperty("deathTimeMillis", ServerEventHandler.entityConverter.toV8Value(v8, mb.deathTimeMillis));
                obj.setProperty("fadeColour", ServerEventHandler.entityConverter.toV8Value(v8, mb.fadeColour.getHex()));
                obj.setProperty("fadeInSpeed", ServerEventHandler.entityConverter.toV8Value(v8, mb.fadeInSpeed));
                obj.setProperty("fadeOutSpeed", ServerEventHandler.entityConverter.toV8Value(v8, mb.fadeOutSpeed));
                obj.setProperty("fadeTimeMillis", ServerEventHandler.entityConverter.toV8Value(v8, mb.fadeTimeMillis));

                return obj;
            } else if (methodName.equals("getCoordBlipInfo")) {
                CoordBlipInfo coord = (CoordBlipInfo) o;
                V8ValueObject obj = v8.createV8ValueObject();
                obj.setProperty("colour", ServerEventHandler.entityConverter.toV8Value(v8, coord.colour.getHex()));
                obj.setProperty("id", ServerEventHandler.entityConverter.toV8Value(v8, coord.index));

                V8ValueObject vec = v8.createV8ValueObject();

                vec.setProperty("x", ServerEventHandler.entityConverter.toV8Value(v8, coord.position.x));
                vec.setProperty("y", ServerEventHandler.entityConverter.toV8Value(v8, coord.position.y));
                vec.setProperty("z", ServerEventHandler.entityConverter.toV8Value(v8, coord.position.z));

                obj.setProperty("position", vec);
                obj.setProperty("scale", ServerEventHandler.entityConverter.toV8Value(v8, coord.scale));
                obj.setProperty("spriteId", ServerEventHandler.entityConverter.toV8Value(v8, coord.spriteId));
                obj.setProperty("worldId", ServerEventHandler.entityConverter.toV8Value(v8, coord.worldId));

                return obj;
            } else if (methodName.equals("createObject")) {
                GameObject ch = (GameObject) o;
                return ch.getId();
            } else if (methodName.equals("sendGameMessage")) {
                Player target = ServerEventHandler.server.getPlayer((int) lst.get(0));
                int type = (int) lst.get(1);
                String message = (String) lst.get(2);

                ServerEventHandler.server.sendGameMessage(target, type, message);
            } else if (methodName.equals("sendClientMessage")) {
                Player target = ServerEventHandler.server.getPlayer((int) lst.get(0));
                int colour = (int) lst.get(1);
                String message = (String) lst.get(2);

                ServerEventHandler.server.sendClientMessage(target, colour, message);
            }

            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
