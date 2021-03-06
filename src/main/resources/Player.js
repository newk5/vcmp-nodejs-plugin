var a = {
    id : '#id',
    addImmunity: function(intValue) { __PlayerProxy.addImmunity(this.id,intValue); },
    removeImmunity: function(intValue) { __PlayerProxy.removeImmunity(this.id,intValue); },
    hasImmunity: function(intValue) { return __PlayerProxy.hasImmunity(this.id,intValue); },
    sendStream : function (stream) { stream.build();  __PlayerProxy.sendStream(this.id,stream.bb.raw); },
    getName : function ( ){ return  __PlayerProxy.run(this.id, 'getName', arguments); },
    setName : function ( arg0 ){ return  __PlayerProxy.run(this.id, 'setName', arguments); },
    getState : function ( ){ return  __PlayerProxy.run(this.id, 'getState', arguments); },
    getWeaponAtSlot : function ( arg0 ){ return  __PlayerProxy.run(this.id, 'getWeaponAtSlot', arguments); },
    isStreamedForPlayer : function ( arg0 ){ return  __PlayerProxy.run(this.id, 'isStreamedForPlayer', [arg0.id]); },
    setSecondaryWorld : function ( arg0 ){  __PlayerProxy.run(this.id, 'setSecondaryWorld', arguments); },
    setCameraPosition : function ( arg0, arg1, arg2, arg3, arg4, arg5 ){  __PlayerProxy.run(this.id, 'setCameraPosition', arguments); },
    getStandingOnObject : function ( ){ const o =  __PlayerProxy.run(this.id, 'getStandingOnObject', arguments); if (o == null || o == undefined) return o; return server.getObject(o); },
    removeFromVehicle : function ( ){ return  __PlayerProxy.run(this.id, 'removeFromVehicle', arguments); },
    setSpectateTarget : function ( arg0 ){  __PlayerProxy.run(this.id, 'setSpectateTarget', arguments); },
    isCompatibleWithWorld : function ( arg0 ){ return  __PlayerProxy.run(this.id, 'isCompatibleWithWorld', arguments); },
    getSpectateTarget : function ( ){ const id= __PlayerProxy.run(this.id, 'getSpectateTarget', arguments); if (id== null ) return null; return server.getPlayer(id); },
    requestModuleList : function ( ){  __PlayerProxy.run(this.id, 'requestModuleList', arguments); },
    getInVehicleStatus : function ( ){ return  __PlayerProxy.run(this.id, 'getInVehicleStatus', arguments); },
    getSecondaryWorld : function ( ){ return  __PlayerProxy.run(this.id, 'getSecondaryWorld', arguments); },
    getStandingOnVehicle : function ( ){ const v =  __PlayerProxy.run(this.id, 'getStandingOnVehicle', arguments); if (v == null || v== undefined) return v; return server.getVehicle(v); },
    isAdmin : function ( ){ return  __PlayerProxy.run(this.id, 'isAdmin', arguments); },
    getIP : function ( ){ return  __PlayerProxy.run(this.id, 'getIP', arguments); },
    setAdmin : function ( arg0 ){  __PlayerProxy.run(this.id, 'setAdmin', arguments); },
    kick : function ( ){  __PlayerProxy.run(this.id, 'kick', arguments); },
    ban : function ( ){  __PlayerProxy.run(this.id, 'ban', arguments); },
    isSpawned : function ( ){ return  __PlayerProxy.run(this.id, 'isSpawned', arguments); },
    getUniqueId : function ( ){ return  __PlayerProxy.run(this.id, 'getUniqueId', arguments); },
    setSkin : function ( arg0 ){  __PlayerProxy.run(this.id, 'setSkin', arguments); },
    setWorld : function ( arg0 ){  __PlayerProxy.run(this.id, 'setWorld', arguments); },
    setScore : function ( arg0 ){  __PlayerProxy.run(this.id, 'setScore', arguments); },
    isTyping : function ( ){ return  __PlayerProxy.run(this.id, 'isTyping', arguments); },
    setWeaponSlot : function ( arg0 ){  __PlayerProxy.run(this.id, 'setWeaponSlot', arguments); },
    setMoney : function ( arg0 ){  __PlayerProxy.run(this.id, 'setMoney', arguments); },
    getWeaponSlot : function ( ){ return  __PlayerProxy.run(this.id, 'getWeaponSlot', arguments); },
    getWorld : function ( ){ return  __PlayerProxy.run(this.id, 'getWorld', arguments); },
    forceSpawn : function ( ){  __PlayerProxy.run(this.id, 'forceSpawn', arguments); },
    forceSelect : function ( ){  __PlayerProxy.run(this.id, 'forceSelect', arguments); },
    setHealth : function ( arg0 ){  __PlayerProxy.run(this.id, 'setHealth', arguments); },
    getArmour : function ( ){ return  __PlayerProxy.run(this.id, 'getArmour', arguments); },
    getImmunities : function ( ){ return  __PlayerProxy.run(this.id, 'getImmunities', arguments); },
    getHeading : function ( ){ return  __PlayerProxy.run(this.id, 'getHeading', arguments); },
    getAlpha : function ( ){ return  __PlayerProxy.run(this.id, 'getAlpha', arguments); },
    setTeam : function ( arg0 ){  __PlayerProxy.run(this.id, 'setTeam', arguments); },
    getInVehicleSlot : function ( ){ return  __PlayerProxy.run(this.id, 'getInVehicleSlot', arguments); },
    setOption : function ( arg0, arg1 ){  __PlayerProxy.run(this.id, 'setOption', arguments); },
    getHealth : function ( ){ return  __PlayerProxy.run(this.id, 'getHealth', arguments); },
    setWeapon : function ( arg0, arg1 ){  __PlayerProxy.run(this.id, 'setWeapon', arguments); },
    giveMoney : function ( arg0 ){  __PlayerProxy.run(this.id, 'giveMoney', arguments); },
    getUniqueWorld : function ( ){ return  __PlayerProxy.run(this.id, 'getUniqueWorld', arguments); },
    getMoney : function ( ){ return  __PlayerProxy.run(this.id, 'getMoney', arguments); },
    setPosition : function ( arg0, arg1, arg2 ){  __PlayerProxy.run(this.id, 'setPosition', arguments); },
    putInVehicle : function ( arg0, arg1, arg2, arg3 ){ return  __PlayerProxy.run(this.id, 'putInVehicle', arguments); },
    getSkin : function ( ){ return  __PlayerProxy.run(this.id, 'getSkin', arguments); },
    setArmour : function ( arg0 ){  __PlayerProxy.run(this.id, 'setArmour', arguments); },
    addSpeed : function ( arg0, arg1, arg2 ){  __PlayerProxy.run(this.id, 'addSpeed', arguments); },
    setHeading : function ( arg0 ){  __PlayerProxy.run(this.id, 'setHeading', arguments); },
    getAimPosition : function ( ){ return  __PlayerProxy.run(this.id, 'getAimPosition', arguments); },
    getFPS : function ( ){ return  __PlayerProxy.run(this.id, 'getFPS', arguments); },
    getImmunityFlags : function ( ){ return  __PlayerProxy.run(this.id, 'getImmunityFlags', arguments); },
    getScore : function ( ){ return  __PlayerProxy.run(this.id, 'getScore', arguments); },
    getUID : function ( ){ return  __PlayerProxy.run(this.id, 'getUID', arguments); },
    setImmunityFlags : function ( arg0 ){  __PlayerProxy.run(this.id, 'setImmunityFlags', arguments); },
    getColour : function ( ){ 
        let v =  __PlayerProxy.run(this.id, 'getColour', []); 
        if (arguments.length >0){
            const type = arguments[0].toLowerCase();
            if (type == "rgb"){
                v = VCMP.Colors.toRGB(v);
            }
        }
        return v;
    },
    getPosition : function ( ){ return  __PlayerProxy.run(this.id, 'getPosition', arguments); },
    getGameKeys : function ( ){ return  __PlayerProxy.run(this.id, 'getGameKeys', arguments); },
    getOption : function ( arg0 ){ return  __PlayerProxy.run(this.id, 'getOption', arguments); },
    giveWeapon : function ( arg0, arg1 ){  __PlayerProxy.run(this.id, 'giveWeapon', arguments); },
    getWeapon : function ( ){ return  __PlayerProxy.run(this.id, 'getWeapon', arguments); },
    setImmunities : function ( arg0 ){  __PlayerProxy.run(this.id, 'setImmunities', arguments); },
    setAlpha : function ( arg0, arg1 ){  __PlayerProxy.run(this.id, 'setAlpha', arguments); },
    setSpeed : function ( arg0, arg1, arg2 ){  __PlayerProxy.run(this.id, 'setSpeed', arguments); },
    getWeaponAmmo : function ( ){ return  __PlayerProxy.run(this.id, 'getWeaponAmmo', arguments); },
    getAction : function ( ){ return  __PlayerProxy.run(this.id, 'getAction', arguments); },
    getAimDirection : function ( ){ return  __PlayerProxy.run(this.id, 'getAimDirection', arguments); },
    setColour : function ( arg0 ){  

        const isObj = typeof arg0 === 'object' && arg0 !== null;
        
        __PlayerProxy.run(this.id, 'setColour', isObj ? [VCMP.Colors.toHex(arg0)] : [ VCMP.Colors.hexToInteger(arg0) ]); 
    },
    getTeam : function ( ){ return  __PlayerProxy.run(this.id, 'getTeam', arguments); },
    getPing : function ( ){ return  __PlayerProxy.run(this.id, 'getPing', arguments); },
    getUID2 : function ( ){ return  __PlayerProxy.run(this.id, 'getUID2', arguments); },
    getSpeed : function ( ){ return  __PlayerProxy.run(this.id, 'getSpeed', arguments); },
    isOnFire : function ( ){ return  __PlayerProxy.run(this.id, 'isOnFire', arguments); },
    isCrouching : function ( ){ return  __PlayerProxy.run(this.id, 'isCrouching', arguments); },
    getVehicle : function ( ){   const v = __PlayerProxy.run(this.id, 'getVehicle', arguments);  if (v==null) return null; return server.getVehicle(v); },
    getDrunkHandling : function ( ){ return  __PlayerProxy.run(this.id, 'getDrunkHandling', arguments); },
    getWantedLevel : function ( ){ return  __PlayerProxy.run(this.id, 'getWantedLevel', arguments); },
    getPlayerClass : function ( ){ return  __PlayerProxy.run(this.id, 'getPlayerClass', arguments); },
    getAmmoAtSlot : function ( arg0 ){ return  __PlayerProxy.run(this.id, 'getAmmoAtSlot', arguments); },
    restoreCamera : function ( ){  __PlayerProxy.run(this.id, 'restoreCamera', arguments); },
    isAway : function ( ){ return  __PlayerProxy.run(this.id, 'isAway', arguments); },
    setDrunkHandling : function ( arg0 ){  __PlayerProxy.run(this.id, 'setDrunkHandling', arguments); },
    setAnimation : function ( arg0, arg1 ){  __PlayerProxy.run(this.id, 'setAnimation', arguments); },
    redirectToServer : function ( arg0, arg1, arg2, arg3, arg4 ){  __PlayerProxy.run(this.id, 'redirectToServer', arguments); },
    setDrunkVisuals : function ( arg0 ){  __PlayerProxy.run(this.id, 'setDrunkVisuals', arguments); },
    isCameraLocked : function ( ){ return  __PlayerProxy.run(this.id, 'isCameraLocked', arguments); },
    setWantedLevel : function ( arg0 ){  __PlayerProxy.run(this.id, 'setWantedLevel', arguments); },
    removeAllWeapons : function ( ){  __PlayerProxy.run(this.id, 'removeAllWeapons', arguments); },
    getDrunkVisuals : function ( ){ return  __PlayerProxy.run(this.id, 'getDrunkVisuals', arguments); },
    removeWeapon : function ( arg0 ){  __PlayerProxy.run(this.id, 'removeWeapon', arguments); },
    getId : function ( ){ return  __PlayerProxy.run(this.id, 'getId', arguments); },
    isValid : function ( ){ return  __PlayerProxy.run(this.id, 'isValid', arguments); },
    print : function() {    var result = [];     for (var id in this) {   try {  if (typeof(this[id]) == "function") {  result.push(id + ": " + this[id].toString().split(")")[0]+")" );  }  } catch (err) { result.push(id + ": inaccessible");   }    }  console.log(result);  },
    attachData : function(){
        let data = VCMP.PlayerData[this.id];
        if (data==undefined){
            VCMP.PlayerData[this.id]  = {};
            data  = VCMP.PlayerData[this.id];
        }
        this["data"] = data;
        return this;
    }

   
 }