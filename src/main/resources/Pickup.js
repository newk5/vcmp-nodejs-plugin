var a = {
    id : '#id',
    isStreamedForPlayer : function ( arg0 ){ return  __PlayerProxy.run(this.id, 'isStreamedForPlayer', [arg0.id]); },
    setAutomaticTimer : function ( arg0 ){  __PickupProxy.run(this.id, 'setAutomaticTimer', arguments); },
    getAutomaticTimer : function ( ){ return  __PickupProxy.run(this.id, 'getAutomaticTimer', arguments); },
    setAlpha : function ( arg0 ){  __PickupProxy.run(this.id, 'setAlpha', arguments); },
    getWorld : function ( ){ return  __PickupProxy.run(this.id, 'getWorld', arguments); },
    getPosition : function ( ){ return  __PickupProxy.run(this.id, 'getPosition', arguments); },
    getAlpha : function ( ){ return  __PickupProxy.run(this.id, 'getAlpha', arguments); },
    setAutomatic : function ( arg0 ){  __PickupProxy.run(this.id, 'setAutomatic', arguments); },
    getQuantity : function ( ){ return  __PickupProxy.run(this.id, 'getQuantity', arguments); },
    setOption : function ( arg0, arg1 ){  __PickupProxy.run(this.id, 'setOption', arguments); },
    setWorld : function ( arg0 ){  __PickupProxy.run(this.id, 'setWorld', arguments); },
    refresh : function ( ){  __PickupProxy.run(this.id, 'refresh', arguments); },
    isAutomatic : function ( ){ return  __PickupProxy.run(this.id, 'isAutomatic', arguments); },
    setPosition : function ( arg0, arg1, arg2 ){  __PickupProxy.run(this.id, 'setPosition', arguments); },
    getModel : function ( ){ return  __PickupProxy.run(this.id, 'getModel', arguments); },
    delete : function ( ){  __PickupProxy.run(this.id, 'delete', arguments); },
    isValid : function ( ){ return  __PickupProxy.run(this.id, 'isValid', arguments); },
    print : function() {    var result = [];     for (var id in this) {   try {  if (typeof(this[id]) == "function") {  result.push(id + ": " + this[id].toString().split(")")[0]+")" );  }  } catch (err) { result.push(id + ": inaccessible");   }    }  console.log(result);  }

  
 }