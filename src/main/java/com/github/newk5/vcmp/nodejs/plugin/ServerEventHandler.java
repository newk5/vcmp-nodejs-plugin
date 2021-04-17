package com.github.newk5.vcmp.nodejs.plugin;

import com.github.newk5.vcmp.nodejs.plugin.util.FileResourceUtils;
import com.github.newk5.vcmp.nodejs.plugin.util.EntityConverter;
import com.github.newk5.vcmp.nodejs.plugin.proxies.PlayerProxy;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.exceptions.JavetExecutionException;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueBoolean;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueObject;
import static com.github.newk5.vcmp.nodejs.plugin.Context.v8;
import com.github.newk5.vcmp.nodejs.plugin.proxies.CheckpointProxy;
import com.github.newk5.vcmp.nodejs.plugin.proxies.GameObjectProxy;
import com.github.newk5.vcmp.nodejs.plugin.proxies.PickupProxy;
import com.github.newk5.vcmp.nodejs.plugin.proxies.ServerProxy;
import com.github.newk5.vcmp.nodejs.plugin.proxies.VehicleProxy;

import com.maxorator.vcmp.java.plugin.integration.RootEventHandler;
import com.maxorator.vcmp.java.plugin.integration.placeable.CheckPoint;
import com.maxorator.vcmp.java.plugin.integration.placeable.GameObject;
import com.maxorator.vcmp.java.plugin.integration.placeable.Pickup;
import com.maxorator.vcmp.java.plugin.integration.player.Player;
import com.maxorator.vcmp.java.plugin.integration.server.Server;
import com.maxorator.vcmp.java.plugin.integration.vehicle.Vehicle;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerEventHandler extends RootEventHandler {

    public static EntityConverter entityConverter;
    public static Server server;
    public static String playerJs = "";
    public static String vehicleJs = "";
    public static String checkpointJs = "";
    public static String objectJs = "";
    public static String pickupJs = "";

    private String tempPlayerVar = "__tempPlayer";
    private boolean hotReload = false;

    private AtomicBoolean changed = new AtomicBoolean(false);
    private PlayerUpdateEvents playerUpdateEvents;

    public ServerEventHandler(Server server) throws IOException, InterruptedException, JavetException {
        super(server);
        this.server = server;
        v8 = V8Host.getNodeInstance().createV8Runtime();
        //v8.getNodeModule(NodeModuleProcess.class).setWorkingDirectory(new File("src"+File.separator+"script").toPath());

        entityConverter = new EntityConverter();
        if (hotReload) {
            new Thread(() -> {

                try {
                    WatchService watchService = FileSystems.getDefault().newWatchService();
                    Path path = new File("src").toPath();
                    path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);

                    WatchKey key;
                    while ((key = watchService.take()) != null) {
                        for (WatchEvent<?> event : key.pollEvents()) {

                            System.out.println("changes detected, reloading context...");
                            changed.set(true);

                        }
                        key.reset();
                    }
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(ServerEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(ServerEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
        }

    }

    @Override
    public boolean onServerInitialise() {
        Context.load(server);
        if (Context.playerUpdateFunctionsExist()) {
            playerUpdateEvents = new PlayerUpdateEvents(this);
        }

        try {
            FileResourceUtils utils = new FileResourceUtils();

            this.playerJs = utils.readResource("Player.js");
            this.checkpointJs = utils.readResource("Checkpoint.js");
            this.objectJs = utils.readResource("Object.js");
            this.pickupJs = utils.readResource("Pickup.js");
            this.vehicleJs = utils.readResource("Vehicle.js");
            init();

            System.out.println("");
            v8.getExecutor(" console.log('\\x1b[32m', 'Loaded plugin: Node.js v0.0.1 by NewK ');").executeVoid();
            v8.getExecutor(" console.log('\\x1b[32m','Node.js v14.16.0');").executeVoid();
            v8.getExecutor(" console.log('\\x1b[0m', '');").executeVoid();
            System.out.println("");
            org.pmw.tinylog.Logger.info("Node.js context initilized");
            if (Context.functionExists("onServerInitialise")) {

                v8.getGlobalObject().invoke("onServerInitialise", false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void init() {
        try {

            V8ValueString path = new V8ValueString(new File("").getAbsolutePath() + File.separator + "src");
            v8.getGlobalObject().setProperty("__dirname", path);
            v8.getExecutor(new File("src" + File.separator + "main.js")).executeVoid();

            V8Value playerProxy = entityConverter.toV8Value(v8, new PlayerProxy());
            V8Value vehicleProxy = entityConverter.toV8Value(v8, new VehicleProxy());
            V8Value pickupProxy = entityConverter.toV8Value(v8, new PickupProxy());
            V8Value objectProxy = entityConverter.toV8Value(v8, new GameObjectProxy());
            V8Value checkpointProxy = entityConverter.toV8Value(v8, new CheckpointProxy());

            ServerProxy sp = new ServerProxy();
            V8Value serverProxy = entityConverter.toV8Value(v8, sp);
            V8Value serverJs = entityConverter.toV8Value(v8, server);

            v8.getGlobalObject().set("__PlayerProxy", playerProxy);
            v8.getGlobalObject().set("__VehicleProxy", vehicleProxy);
            v8.getGlobalObject().set("__PickupProxy", pickupProxy);
            v8.getGlobalObject().set("__GameObjectProxy", objectProxy);
            v8.getGlobalObject().set("__CheckPointProxy", checkpointProxy);
            v8.getGlobalObject().set("__ServerProxy", serverProxy);

            v8.getGlobalObject().set("server", serverJs);
            sp.overrideObjectGetters();

            playerProxy.close();
            vehicleProxy.close();
            pickupProxy.close();
            objectProxy.close();
            checkpointProxy.close();
            serverJs.close();
            path.close();

            FileResourceUtils utils = new FileResourceUtils();
            v8.getExecutor(utils.readResource("byte-buffer.min.js")).executeVoid();
            v8.getExecutor(utils.readResource("VCMPStream.js")).executeVoid();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onServerFrame() {
        if (hotReload) {
            if (changed.get()) {
                try {
                    v8.resetContext();
                    init();
                } catch (JavetException ex) {
                    java.util.logging.Logger.getLogger(ServerEventHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                changed.set(false);
            }
        }
    }

    public static void exception(Exception e) {
        try {
            e.printStackTrace();
            org.pmw.tinylog.Logger.error(e);
            if (e instanceof JavetExecutionException) {
                JavetExecutionException jex = (JavetExecutionException) e;
                String msg = "\033[0;31m ERROR: main.js:" + jex.getError().getLineNumber() + " " + jex.getError().getSourceLine() + " " + jex.getError().getMessage() + "\033[0m";
                System.out.println(msg);
                org.pmw.tinylog.Logger.error(msg);
            }
        } catch (Exception javetException) {
        }

    }

    @Override
    public void onPlayerConnect(Player player) {

        if (Context.functionExists("onPlayerConnect")) {
            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                v8.getExecutor("onPlayerConnect(" + playerObj + "); ").execute();

            } catch (JavetException ex) {
                ex.printStackTrace();
                exception(ex);
            }

        }
    }

    @Override
    public void onServerLoadScripts() {
        try {

            if (Context.functionExists("onServerLoadScripts")) {

                v8.getGlobalObject().invoke("onServerLoadScripts", false);

            }

        } catch (Exception ex) {

            exception(ex);
        }

    }

    @Override
    public void onServerPerformanceReport(int entry, String[] descriptions, long[] times) {

        if (Context.functionExists("onServerPerformanceReport")) {

            try {

                /*              TODO: convert to literal approach
                V8Value o1 = entityConverter.toV8Value(v8, entry);
                V8Value o2 = entityConverter.toV8Value(v8, descriptions);
                V8Value o3 = entityConverter.toV8Value(v8, times);
                v8.getGlobalObject().invoke("onServerPerformanceReport", false, o1, o2, o3);

                o1.close();
                o2.close();
                o3.close();
                 */
            } catch (Exception e) {
                this.exception(e);

            }
        }
    }

    public void onPlayerWeaponChange(Player p, int oldWep, int newWep) {

        if (Context.functionExists("onPlayerWeaponChange")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", p.getId() + "");

                v8.getExecutor("onPlayerWeaponChange(" + playerObj + ", " + oldWep + ", " + newWep + "); ").execute();

                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    public void onPlayerMove(Player player, float lastX, float lastY, float lastZ, float newX, float newY, float newZ) {
        if (Context.functionExists("onPlayerMove")) {

            try {

                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                v8.getExecutor("onPlayerMove(" + playerObj + ", " + lastX + ", " + lastY + ", " + lastZ + ", " + newX + ", " + newY + ", " + newZ + " ); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    public void onPlayerHealthChange(Player player, float lastHP, float newHP) {
        if (Context.functionExists("onPlayerHealthChange")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                v8.getExecutor("onPlayerHealthChange(" + playerObj + ", " + lastHP + ", " + newHP + "); ").execute();
                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    public void onPlayerArmourChange(Player player, float lastArmour, float newArmour) {
        if (Context.functionExists("onPlayerArmourChange")) {
            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                v8.getExecutor("onPlayerArmourChange(" + playerObj + ", " + lastArmour + ", " + newArmour + "); ").execute();
                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerModuleList(Player player, String list) {

        if (Context.functionExists("onPlayerModuleList")) {

            try {
                try (V8ValueObject p = playerToV8Object(player);
                        V8ValueString o2 = new V8ValueString(list);) {

                    v8.getGlobalObject().invoke("onPlayerModuleList", false, p, o2);

                }
                v8.await();

                v8.getExecutor("delete global." + tempPlayerVar).executeVoid();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onServerUnloadScripts() {
        if (Context.functionExists("onServerUnloadScripts")) {
            try {
                v8.getGlobalObject().invoke("onServerUnloadScripts", false);
            } catch (Exception e) {
                this.exception(e);
            }
        }

    }

    @Override
    public String onIncomingConnection(String name, String password, String ip) {
        if (Context.functionExists("onIncomingConnection")) {

            try {

                try (V8ValueString o1 = new V8ValueString(name);
                        V8ValueString o2 = new V8ValueString(password);
                        V8ValueString o3 = new V8ValueString(ip);) {
                    Object o = v8.getGlobalObject().invoke("onIncomingConnection", true, o1, o2, o3);

                    v8.await();

                    if (o != null) {
                        if (o instanceof V8ValueString) {
                            V8ValueString v = (V8ValueString) o;
                            return v.toPrimitive();
                        }
                    }
                }

            } catch (Exception e) {
                this.exception(e);
            }

        }
        return name;
    }

    @Override
    public void onPlayerSpawn(Player player) {
        if (Context.functionExists("onPlayerSpawn")) {
            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                v8.getExecutor("onPlayerSpawn(" + playerObj + "); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerDisconnect(Player player, int reason) {
        if (Context.functionExists("onPlayerDisconnect")) {
            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                v8.getExecutor("onPlayerDisconnect(" + playerObj + ", " + reason + "); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerEnterVehicle(Player player, Vehicle vehicle, int slot) {

        if (Context.functionExists("onPlayerEnterVehicle")) {

            try {

                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String vehicleObj = vehicleJs.replaceFirst("'#id'", vehicle.getId() + "");
                v8.getExecutor("onPlayerEnterVehicle(" + playerObj + ", " + vehicleObj + "," + slot + "); ").execute();
                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerExitVehicle(Player player, Vehicle vehicle) {

        if (Context.functionExists("onPlayerExitVehicle")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String vehicleObj = vehicleJs.replaceFirst("'#id'", vehicle.getId() + "");
                v8.getExecutor("onPlayerExitVehicle(" + playerObj + ", " + vehicleObj + "); ").execute();
                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onVehicleExplode(Vehicle vehicle) {
        if (Context.functionExists("onVehicleExplode")) {

            try {
                String vehicleObj = vehicleJs.replaceFirst("'#id'", vehicle.getId() + "");
                v8.getExecutor("onVehicleExplode(" + vehicleObj + "); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public boolean onPlayerCommand(Player player, String message) {

        if (Context.functionExists("onPlayerCommand")) {

            try (V8ValueObject p = playerToV8Object(player);
                    V8ValueString o2 = new V8ValueString(message);) {

                Object o = v8.getGlobalObject().invoke("onPlayerCommand", true, p, o2);
                v8.getExecutor("delete global." + tempPlayerVar).executeVoid();
                v8.await();

                if (o != null && o instanceof V8ValueBoolean) {
                    V8ValueBoolean v = (V8ValueBoolean) o;
                    return v.toPrimitive();
                }
            } catch (Exception e) {
                this.exception(e);
            }

        }
        return true;

    }

    @Override
    public void onPlayerCrashReport(Player player, String crashLog) {
        if (Context.functionExists("onPlayerCrashReport")) {
            try {
                try (
                        V8ValueObject p = playerToV8Object(player);
                        V8ValueString o2 = new V8ValueString(crashLog);) {

                    v8.getGlobalObject().invoke("onPlayerCommand", false, p, o2);

                }

                v8.getExecutor("delete global." + tempPlayerVar).executeVoid();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onCheckPointExited(CheckPoint checkPoint, Player player) {

        if (Context.functionExists("onCheckPointExited")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String chObj = checkpointJs.replaceFirst("'#id'", checkPoint.getId() + "");
                v8.getExecutor("onCheckPointExited(" + playerObj + ", '" + chObj + "'); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onCheckPointEntered(CheckPoint checkPoint, Player player) {

        if (Context.functionExists("onCheckPointEntered")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String chObj = checkpointJs.replaceFirst("'#id'", checkPoint.getId() + "");
                v8.getExecutor("onCheckPointEntered(" + playerObj + ", '" + chObj + "'); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPickupRespawn(Pickup pickup) {
        if (Context.functionExists("onPickupRespawn")) {
            try {
                String pobj = pickupJs.replaceFirst("'#id'", pickup.getId() + "");
                v8.getExecutor("onPickupRespawn(" + pobj + "); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPickupPicked(Pickup pickup, Player player) {

        if (Context.functionExists("onPickupPicked")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String pobj = pickupJs.replaceFirst("'#id'", pickup.getId() + "");
                v8.getExecutor("onPickupPicked(" + pobj + ", " + playerObj + "); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);

            }
        }
    }

    @Override
    public boolean onPickupPickAttempt(Pickup pickup, Player player) {

        if (Context.functionExists("onPickupPickAttempt")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String pobj = pickupJs.replaceFirst("'#id'", pickup.getId() + "");
                Boolean o = v8.getExecutor("onPickupPickAttempt(" + pobj + ", " + playerObj + "); ").executeBoolean();
                v8.await();
                if (o != null) {
                    return o;
                }

            } catch (Exception e) {
                this.exception(e);
            }

        }
        return false;
    }

    @Override
    public void onObjectTouched(GameObject object, Player player) {
        if (Context.functionExists("onObjectTouched")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String pobj = objectJs.replaceFirst("'#id'", object.getId() + "");

                v8.getExecutor("onObjectTouched(" + pobj + ", " + playerObj + "); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onObjectShot(GameObject object, Player player, int weaponId) {
        if (Context.functionExists("onObjectShot")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String pobj = objectJs.replaceFirst("'#id'", object.getId() + "");

                v8.getExecutor("onObjectShot(" + pobj + ", " + playerObj + ", " + weaponId + "); ").execute();
                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onVehicleRespawn(Vehicle vehicle) {
        if (Context.functionExists("onVehicleRespawn")) {

            try {
                String vehicleObj = vehicleJs.replaceFirst("'#id'", vehicle.getId() + "");
                v8.getExecutor("onVehicleRespawn(" + vehicleObj + "); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onVehicleUpdate(Vehicle vehicle, int updateType) {

        if (Context.functionExists("onVehicleUpdate")) {

            try {
                String vehicleObj = playerJs.replaceFirst("'#id'", vehicle.getId() + "");
                v8.getExecutor("onVehicleUpdate(" + vehicleObj + ", " + updateType + "); ").execute();
                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerSpectate(Player player, Player spectated) {

        if (Context.functionExists("onPlayerSpectate")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String playerObj2 = playerJs.replaceFirst("'#id'", spectated.getId() + "");

                v8.getExecutor("onPlayerSpectate(" + playerObj + ", " + playerObj2 + "); ").execute();
                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerKeyBindUp(Player player, int keyBindIndex) {

        if (Context.functionExists("onPlayerKeyBindUp")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                v8.getExecutor("onPlayerKeyBindUp(" + playerObj + ", " + keyBindIndex + "); ").execute();
                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerKeyBindDown(Player player, int keyBindIndex) {
        if (Context.functionExists("onPlayerKeyBindDown")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                v8.getExecutor("onPlayerKeyBindDown(" + playerObj + ", " + keyBindIndex + "); ").execute();
                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public boolean onPlayerPrivateMessage(Player player, Player recipient, String message) {
        if (Context.functionExists("onPlayerPrivateMessage")) {

            try {
                try (V8ValueObject p = playerToV8Object(player);
                        V8ValueObject p2 = playerToV8Object(recipient);
                        V8ValueString s = new V8ValueString(message);) {
                    Object o = v8.getGlobalObject().invoke("onPlayerPrivateMessage", true, p, p2, s);

                    v8.getExecutor("delete global." + tempPlayerVar).executeVoid();
                    v8.await();

                    if (o != null && o instanceof V8ValueBoolean) {
                        V8ValueBoolean b = (V8ValueBoolean) o;
                        return b.toPrimitive();
                    }
                }

            } catch (Exception e) {
                this.exception(e);
            }
        }
        return true;
    }

    @Override
    public boolean onPlayerMessage(Player player, String message) {

        if (Context.functionExists("onPlayerMessage")) {

            try {
                try (V8ValueObject p = playerToV8Object(player);
                        V8ValueString s = new V8ValueString(message);) {

                    Object o = v8.getGlobalObject().invoke("onPlayerMessage", true, p, s);

                    v8.getExecutor("delete global." + tempPlayerVar).executeVoid();
                    v8.await();

                    if (o != null && o instanceof V8ValueBoolean) {
                        V8ValueBoolean b = (V8ValueBoolean) o;
                        return b.toPrimitive();
                    }
                }

            } catch (Exception e) {
                this.exception(e);
            }
        }
        return true;
    }

    @Override
    public void onPlayerAwayChange(Player player, boolean isAway) {
        if (Context.functionExists("onPlayerAwayChange")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                v8.getExecutor("onPlayerAwayChange(" + playerObj + ", " + isAway + "); ").execute();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerEndTyping(Player player) {

        if (Context.functionExists("onPlayerEndTyping")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                v8.getExecutor("onPlayerBeginTyping(" + playerObj + "); ").execute();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerBeginTyping(Player player) {
        if (Context.functionExists("onPlayerBeginTyping")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                v8.getExecutor("onPlayerBeginTyping(" + playerObj + "); ").execute();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerGameKeysChange(Player player, int oldKeys, int newKeys) {
        if (Context.functionExists("onPlayerGameKeysChange")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                v8.getExecutor("onPlayerGameKeysChange(" + playerObj + ", " + oldKeys + ", " + newKeys + "); ").execute();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerCrouchChange(Player player, boolean isCrouching) {
        if (Context.functionExists("onPlayerCrouchChange")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                v8.getExecutor("onPlayerCrouchChange(" + playerObj + ", " + isCrouching + "); ").execute();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerOnFireChange(Player player, boolean isOnFire) {
        if (Context.functionExists("onPlayerOnFireChange")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                v8.getExecutor("onPlayerOnFireChange(" + playerObj + ", " + isOnFire + "); ").execute();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerActionChange(Player player, int oldAction, int newAction) {
        if (Context.functionExists("onPlayerActionChange")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                v8.getExecutor("onPlayerActionChange(" + playerObj + ", " + oldAction + ", " + newAction + "); ").execute();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerStateChange(Player player, int oldState, int newState) {
        if (oldState == newState) {
            return;
        }

        if (Context.functionExists("onPlayerStateChange")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                v8.getExecutor("onPlayerStateChange(" + playerObj + ", " + oldState + ", " + newState + "); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }

    }

    @Override
    public void onPlayerNameChange(Player player, String oldName, String newName) {

        if (Context.functionExists("onPlayerNameChange")) {

            try {
                try (V8ValueObject p = playerToV8Object(player);
                        V8ValueString o2 = new V8ValueString(oldName);
                        V8ValueString o3 = new V8ValueString(newName);) {

                    v8.getGlobalObject().invoke("onPlayerNameChange", false, p, o2, o3);

                }
                v8.await();

                v8.getExecutor("delete global." + tempPlayerVar).executeVoid();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    private V8ValueObject playerToV8Object(Player player) {
        try {
            String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
            V8ValueObject p = v8.getExecutor("global." + this.tempPlayerVar + "=" + playerObj + "; global." + this.tempPlayerVar + ";").execute();
            return p;
        } catch (JavetException ex) {
            Logger.getLogger(ServerEventHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean onPlayerRequestEnterVehicle(Player player, Vehicle vehicle, int slot) {
        if (Context.functionExists("onPlayerRequestEnterVehicle")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String vehicleObj = vehicleJs.replaceFirst("'#id'", vehicle.getId() + "");

                Boolean o = v8.getExecutor("onPlayerRequestEnterVehicle(" + playerObj + ", " + vehicleObj + ", " + slot + "); ").executeBoolean();
                if (o != null) {
                    return o;
                }
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
        return true;
    }

    @Override
    public void onPlayerUpdate(Player player, int updateType) {
        if (Context.playerUpdateFunctionsExist()) {
            playerUpdateEvents.update(player);
        }
        if (Context.functionExists("onPlayerUpdate")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                v8.getExecutor("onPlayerUpdate(" + playerObj + ", " + updateType + "); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onPlayerDeath(Player player, Player killer, int reason, int bodyPart) {

        if (Context.functionExists("onPlayerDeath")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");
                String playerObj2 = playerJs.replaceFirst("'#id'", killer == null ? "-1" : killer.getId() + "");

                v8.getExecutor("onPlayerDeath(" + playerObj + ", " + (killer == null ? "null" : playerObj2) + "," + reason + ", " + bodyPart + " ); ").execute();
                v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public boolean onPlayerRequestSpawn(Player player) {
        if (Context.functionExists("onPlayerRequestSpawn")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                Boolean o = v8.getExecutor("onPlayerRequestSpawn(" + playerObj + "); ").executeBoolean();
                if (o != null) {
                    return o;
                }
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }
        }
        return true;
    }

    @Override
    public boolean onPlayerRequestClass(Player player, int classIndex) {
        if (Context.functionExists("onPlayerRequestClass")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                Boolean o = v8.getExecutor("onPlayerRequestClass(" + playerObj + "," + classIndex + " ); ").executeBoolean();
                if (o != null) {
                    return o;
                }

            } catch (Exception e) {
                this.exception(e);
            }

        }
        return true;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public void onClientScriptData(Player player, byte[] data) {
        if (Context.functionExists("onClientScriptData")) {

            try {
                String playerObj = playerJs.replaceFirst("'#id'", player.getId() + "");

                v8.getExecutor("onClientScriptData(" + playerObj + ", new VCMPStream(" + Arrays.toString(data) + ")); ").execute();
                v8.await();

            } catch (Exception e) {
                this.exception(e);
            }

        }
    }

    @Override
    public void onPluginCommand(int identifier, String message) {
        if (Context.functionExists("onPluginCommand")) {

            try {

                //v8.getExecutor("onPluginCommand(" + identifier + ", '" + message + "'); ").execute();
                // v8.await();
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

    @Override
    public void onServerShutdown() {
        if (Context.functionExists("onServerShutdown")) {
            try {
                v8.getGlobalObject().invoke("onServerShutdown", false);
            } catch (Exception e) {
                this.exception(e);
            }
        }
    }

}
