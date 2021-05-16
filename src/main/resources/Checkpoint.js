var a = {
    id : '#id',
    delete : function ( ){  
        __CheckPointProxy.run(this.id, 'delete', arguments);
        VCMP.CheckpointData[this.id]  = {};
    },
    getOwner : function ( ){ const p=  __CheckPointProxy.run(this.id, 'getOwner', arguments);  if (p==null || p == undefined) return p; return JSON.parse(p); },
    isStreamedForPlayer : function ( arg0 ){ return  __PlayerProxy.run(this.id, 'isStreamedForPlayer', [arg0.id]); },
    setColour : function ( arg0, arg1, arg2, arg3 ){  __CheckPointProxy.run(this.id, 'setColour', arguments); },
    getPosition : function ( ){ return  __CheckPointProxy.run(this.id, 'getPosition', arguments);  },
    setWorld : function ( arg0 ){  __CheckPointProxy.run(this.id, 'setWorld', arguments); },
    setRadius : function ( arg0 ){  __CheckPointProxy.run(this.id, 'setRadius', arguments); },
    getColour : function ( ){ return  __CheckPointProxy.run(this.id, 'getColour', arguments); },
    getWorld : function ( ){ return  __CheckPointProxy.run(this.id, 'getWorld', arguments); },
    setPosition : function ( arg0, arg1, arg2 ){  __CheckPointProxy.run(this.id, 'setPosition', arguments); },
    getRadius : function ( ){ return  __CheckPointProxy.run(this.id, 'getRadius', arguments); },
    getId : function ( ){ return  __CheckPointProxy.run(this.id, 'getId', arguments); },
    isValid : function ( ){ return  __CheckPointProxy.run(this.id, 'isValid', arguments); },
    attachData : function(){
        let data = VCMP.CheckpointData[this.id];
        if (data==undefined){
            VCMP.CheckpointData[this.id]  = {};
            data  = VCMP.CheckpointData[this.id];
        }
        this["data"] = data;
        return this;
    },
    print : function() {    var result = [];     for (var id in this) {   try {  if (typeof(this[id]) == "function") {  result.push(id + ": " + this[id].toString().split(")")[0]+")" );  }  } catch (err) { result.push(id + ": inaccessible");   }    }  console.log(result);  }

 }