package com.github.newk5.vcmp.nodejs.plugin;

import com.maxorator.vcmp.java.plugin.integration.player.Player;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerUpdateEvents {

    private Map<Integer, SavedPlayerData> lastPlayerData = new LinkedHashMap<>();
    private ServerEventHandler eventHandler;

    public PlayerUpdateEvents(ServerEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void connect(Player p) {
        lastPlayerData.put(p.getId(), new SavedPlayerData(0, 100, 0f, 0f, 0f, 0f));
    }

    public void update(Player player) {
        SavedPlayerData pd = lastPlayerData.get(player.getId());

    /*    if (pd.getLastWep() != player.getWeapon()) {
            eventHandler.onPlayerWeaponChange(player, pd.getLastWep(), player.getWeapon());
            pd.setLastWep(player.getWeapon());
        }

        if (pd.getLastHP() != player.getHealth()) {
            eventHandler.onPlayerHealthChange(player, pd.getLastHP(), player.getHealth());
            pd.setLastHP(player.getHealth());
        }

        if (pd.getLastArmour() != player.getArmour()) {
            eventHandler.onPlayerArmourChange(player, pd.getLastArmour(), player.getArmour());
            pd.setLastArmour(player.getArmour());
        }

        if (pd.getLastX() != player.getPosition().x || pd.getLastY() != player.getPosition().y || pd.getLastZ() != player.getPosition().z) {
            eventHandler.onPlayerMove(player, pd.getLastX(), pd.getLastY(), pd.getLastZ(), player.getPosition().x, player.getPosition().y, player.getPosition().z);
            pd.setLastX(player.getPosition().x);
            pd.setLastY(player.getPosition().y);
            pd.setLastZ(player.getPosition().z);
        }*/

    }

}

class SavedPlayerData {

    private int lastWep = -1;
    private float lastHP = -1;
    private float lastArmour = -1;
    private float lastX = -1, lastY = -1, lastZ = -1;

    public SavedPlayerData() {
    }

    public SavedPlayerData(int lastWep, int lastHP, float lastArmour, float lastX, float lastY, float lastZ) {
        this.lastWep = lastWep;
        this.lastHP = lastHP;
        this.lastArmour = lastArmour;

        this.lastX = lastX;
        this.lastY = lastY;
        this.lastZ = lastZ;
    }

    /**
     * @return the lastWep
     */
    public int getLastWep() {
        return lastWep;
    }

    /**
     * @param lastWep the lastWep to set
     */
    public void setLastWep(int lastWep) {
        this.lastWep = lastWep;
    }

    /**
     * @return the lastHP
     */
    public float getLastHP() {
        return lastHP;
    }

    /**
     * @param lastHP the lastHP to set
     */
    public void setLastHP(float lastHP) {
        this.lastHP = lastHP;
    }

    /**
     * @return the lastArmour
     */
    public float getLastArmour() {
        return lastArmour;
    }

    /**
     * @param lastArmour the lastArmour to set
     */
    public void setLastArmour(float lastArmour) {
        this.lastArmour = lastArmour;
    }

    /**
     * @return the lastX
     */
    public float getLastX() {
        return lastX;
    }

    /**
     * @param lastX the lastX to set
     */
    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    /**
     * @return the lastY
     */
    public float getLastY() {
        return lastY;
    }

    /**
     * @param lastY the lastY to set
     */
    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    /**
     * @return the lastZ
     */
    public float getLastZ() {
        return lastZ;
    }

    /**
     * @param lastZ the lastZ to set
     */
    public void setLastZ(float lastZ) {
        this.lastZ = lastZ;
    }

}
