var a = {
    id : '#id',
    getTyreStatus : function(tyre) {  return __VehicleProxy.getTyreStatus(this.id, tyre);  },
    getDoorStatus : function(door) {  return __VehicleProxy.getDoorStatus(this.id, door);  },
    getPanelStatus : function(panel) {  return __VehicleProxy.getPanelStatus(this.id, panel);  },
    setDoorStatus : function(door,status) {  __VehicleProxy.setDoorStatus(this.id, door, status);  },
    setTyreStatus : function(tyre,status) {  return __VehicleProxy.setTyreStatus(this.id, tyre,status);  },
    setPanelStatus : function(pane,status) {  return __VehicleProxy.setPanelStatus(this.id, panel,status);  },
    delete : function ( ){  
        __VehicleProxy.run(this.id, 'delete', arguments);
        VCMP.VehicleData[this.id]  = {};
    },
    setSpawnRotationEuler : function ( arg0, arg1, arg2 ){  __VehicleProxy.run(this.id, 'setSpawnRotationEuler', arguments); },
    isStreamedForPlayer : function ( arg0 ){ return  __PlayerProxy.run(this.id, 'isStreamedForPlayer', [arg0.id]); },
    getSpawnRotationEuler : function ( ){ return  __VehicleProxy.run(this.id, 'getSpawnRotationEuler', arguments); },
    getTurrentRotation : function ( ){ return  __VehicleProxy.run(this.id, 'getTurrentRotation', arguments); },
    handlingRuleExists : function ( arg0 ){ return  __VehicleProxy.run(this.id, 'handlingRuleExists', arguments); },
    resetHandlingRule : function ( arg0 ){  __VehicleProxy.run(this.id, 'resetHandlingRule', arguments); },
    setIdleRespawnTimeout : function ( arg0 ){  __VehicleProxy.run(this.id, 'setIdleRespawnTimeout', arguments); },
    getIdleRespawnTimeout : function ( ){ return  __VehicleProxy.run(this.id, 'getIdleRespawnTimeout', arguments); },
    getSyncController : function ( ){ const p = __VehicleProxy.run(this.id, 'getSyncController', arguments); if (p==null || p == undefined) return p; return JSON.parse(p); },
    getSyncReason : function ( ){ return  __VehicleProxy.run(this.id, 'getSyncReason', arguments); },
    setWorld : function ( arg0 ){  __VehicleProxy.run(this.id, 'setWorld', arguments); },
    getWorld : function ( ){ return  __VehicleProxy.run(this.id, 'getWorld', arguments); },
    getModel : function ( ){ return  __VehicleProxy.run(this.id, 'getModel', arguments); },
    getOccupant : function ( arg0 ){   const p = __VehicleProxy.run(this.id, 'getOccupant', arguments); if (p==null || p == undefined) return p; return server.getPlayer(p); },
    respawn : function ( ){  __VehicleProxy.run(this.id, 'respawn', arguments); },
    setImmunities : function ( arg0 ){  __VehicleProxy.run(this.id, 'setImmunities', arguments); },
    setHealth : function ( arg0 ){  __VehicleProxy.run(this.id, 'setHealth', arguments); },
    getTurnSpeed : function ( arg0 ){ return  __VehicleProxy.run(this.id, 'getTurnSpeed', arguments); },
    setImmunityFlags : function ( arg0 ){  __VehicleProxy.run(this.id, 'setImmunityFlags', arguments); },
    getRotation : function ( ){ return  __VehicleProxy.run(this.id, 'getRotation', arguments); },
    getOption : function ( arg0 ){ return  __VehicleProxy.run(this.id, 'getOption', arguments); },
    setRotationEuler : function ( arg0, arg1, arg2 ){  __VehicleProxy.run(this.id, 'setRotationEuler', arguments); },
    setLightsData : function ( arg0 ){  __VehicleProxy.run(this.id, 'setLightsData', arguments); },
    setTaxiLight : function ( arg0 ){  __VehicleProxy.run(this.id, 'setTaxiLight', arguments); },
    setPosition : function ( arg0, arg1, arg2 ){  __VehicleProxy.run(this.id, 'setPosition', arguments); },
    getRotationEuler : function ( ){ return  __VehicleProxy.run(this.id, 'getRotationEuler', arguments); },
    getSpawnPosition : function ( ){ return  __VehicleProxy.run(this.id, 'getSpawnPosition', arguments); },
    getHealth : function ( ){ return  __VehicleProxy.run(this.id, 'getHealth', arguments); },
    getTaxiLight : function ( ){ return  __VehicleProxy.run(this.id, 'getTaxiLight', arguments); },
    setRadio : function ( arg0 ){  __VehicleProxy.run(this.id, 'setRadio', arguments); },
    setHandlingRule : function ( arg0, arg1 ){  __VehicleProxy.run(this.id, 'setHandlingRule', arguments); },
    resetHandling : function ( ){  __VehicleProxy.run(this.id, 'resetHandling', arguments); },
    getPosition : function ( ){ return  __VehicleProxy.run(this.id, 'getPosition', arguments); },
    getRadio : function ( ){ return  __VehicleProxy.run(this.id, 'getRadio', arguments); },
    getImmunityFlags : function ( ){ return  __VehicleProxy.run(this.id, 'getImmunityFlags', arguments); },
    setSpeed : function ( arg0, arg1, arg2, arg3, arg4 ){  __VehicleProxy.run(this.id, 'setSpeed', arguments); },
    getImmunities : function ( ){ return  __VehicleProxy.run(this.id, 'getImmunities', arguments); },
    isWrecked : function ( ){ return  __VehicleProxy.run(this.id, 'isWrecked', arguments); },
    detonate : function ( ){  __VehicleProxy.run(this.id, 'detonate', arguments); },
    setTurnSpeed : function ( arg0, arg1, arg2, arg3, arg4 ){  __VehicleProxy.run(this.id, 'setTurnSpeed', arguments); },
    setSpawnRotation : function ( arg0, arg1, arg2, arg3 ){  __VehicleProxy.run(this.id, 'setSpawnRotation', arguments); },
    getSpawnRotation : function ( ){ return  __VehicleProxy.run(this.id, 'getSpawnRotation', arguments); },
    setColours : function ( arg0 ){ 
        let primary = arg0.primary;
        if (typeof primary === 'object' && primary !== null){
            primary = VCMP.Colors.toHex(primary);
        }
        let secondary = arg0.secondary;
        if (typeof secondary === 'object' && secondary !== null){
            secondary = VCMP.Colors.toHex(secondary);
        }
        __VehicleProxy.run(this.id, 'setColours', [primary,secondary]);
    },
    getLightsData : function ( ){ return  __VehicleProxy.run(this.id, 'getLightsData', arguments); },
    getHandlingRule : function ( arg0 ){ return  __VehicleProxy.run(this.id, 'getHandlingRule', arguments); },
    setRotation : function ( arg0, arg1, arg2, arg3 ){  __VehicleProxy.run(this.id, 'setRotation', arguments); },
    getSpeed : function ( arg0 ){ return  __VehicleProxy.run(this.id, 'getSpeed', arguments); },
    setOption : function ( arg0, arg1 ){  __VehicleProxy.run(this.id, 'setOption', arguments); },
    setSpawnPosition : function ( arg0, arg1, arg2 ){  __VehicleProxy.run(this.id, 'setSpawnPosition', arguments); },
    getColours : function ( ){ return  __VehicleProxy.run(this.id, 'getColours', arguments); },
    getId : function ( ){ return  __VehicleProxy.run(this.id, 'getId', arguments); },
    isValid : function ( ){ return  __VehicleProxy.run(this.id, 'isValid', arguments); },
    attachData : function(){
        let data = VCMP.VehicleData[this.id];
        if (data==undefined){
            VCMP.VehicleData[this.id]  = {};
            data  = VCMP.VehicleData[this.id];
        }
        this["data"] = data;
        return this;
    },
    print : function() {    var result = [];     for (var id in this) {   try {  if (typeof(this[id]) == "function") {  result.push(id + ": " + this[id].toString().split(")")[0]+")" );  }  } catch (err) { result.push(id + ": inaccessible");   }    }  console.log(result);  }

   
 }