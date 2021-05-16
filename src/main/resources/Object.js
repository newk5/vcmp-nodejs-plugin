var a = {
    id : '#id',
    delete : function ( ){  
        __GameObjectProxy.run(this.id, 'delete', arguments);
        VCMP.ObjectData[this.id]  = {};
    },
    setShotReportEnabled : function ( arg0 ){  __GameObjectProxy.run(this.id, 'setShotReportEnabled', arguments); },
    isShotReportEnabled : function ( ){ return  __GameObjectProxy.run(this.id, 'isShotReportEnabled', arguments); },
    isTouchedReportEnabled : function ( ){ return  __GameObjectProxy.run(this.id, 'isTouchedReportEnabled', arguments); },
    isStreamedForPlayer : function ( arg0 ){ return  __PlayerProxy.run(this.id, 'isStreamedForPlayer', [arg0.id]); },
    setTouchedReportEnabled : function ( arg0 ){  __GameObjectProxy.run(this.id, 'setTouchedReportEnabled', arguments); },
    moveBy : function ( arg0, arg1, arg2, arg3 ){  __GameObjectProxy.run(this.id, 'moveBy', arguments); },
    setAlpha : function ( arg0, arg1 ){  __GameObjectProxy.run(this.id, 'setAlpha', arguments); },
    getAlpha : function ( ){ return  __GameObjectProxy.run(this.id, 'getAlpha', arguments); },
    getPosition : function ( ){ return  __GameObjectProxy.run(this.id, 'getPosition', arguments); },
    rotateByEuler : function ( arg0, arg1, arg2, arg3 ){  __GameObjectProxy.run(this.id, 'rotateByEuler', arguments); },
    getRotation : function ( ){ return  __GameObjectProxy.run(this.id, 'getRotation', arguments); },
    moveTo : function ( arg0, arg1, arg2, arg3 ){  __GameObjectProxy.run(this.id, 'moveTo', arguments); },
    setWorld : function ( arg0 ){  __GameObjectProxy.run(this.id, 'setWorld', arguments); },
    setPosition : function ( arg0, arg1, arg2 ){  __GameObjectProxy.run(this.id, 'setPosition', arguments); },
    getModel : function ( ){ return  __GameObjectProxy.run(this.id, 'getModel', arguments); },
    getWorld : function ( ){ return  __GameObjectProxy.run(this.id, 'getWorld', arguments); },
    rotateToEuler : function ( arg0, arg1, arg2, arg3 ){  __GameObjectProxy.run(this.id, 'rotateToEuler', arguments); },
    rotateBy : function ( arg0, arg1, arg2, arg3, arg4 ){  __GameObjectProxy.run(this.id, 'rotateBy', arguments); },
    rotateTo : function ( arg0, arg1, arg2, arg3, arg4 ){  __GameObjectProxy.run(this.id, 'rotateTo', arguments); },
    getRotationEuler : function ( ){ return  __GameObjectProxy.run(this.id, 'getRotationEuler', arguments); },
    getId : function ( ){ return  __GameObjectProxy.run(this.id, 'getId', arguments); },
    attachData : function(){
        let data = VCMP.ObjectData[this.id];
        if (data==undefined){
            VCMP.ObjectData[this.id]  = {};
            data  = VCMP.ObjectData[this.id];
        }
        this["data"] = data;
        return this;
    },
    print : function() {    var result = [];     for (var id in this) {   try {  if (typeof(this[id]) == "function") {  result.push(id + ": " + this[id].toString().split(")")[0]+")" );  }  } catch (err) { result.push(id + ": inaccessible");   }    }  console.log(result);  }

    
 }