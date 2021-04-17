var a = {
    id : '#id',
    getId : function ( ){  __CheckPointProxy.run(this.id, 'delete', arguments); },
    getOwner : function ( ){ return  __CheckPointProxy.run(this.id, 'getOwner', arguments); },
    isStreamedForPlayer : function ( arg0 ){ return  __CheckPointProxy.run(this.id, 'isStreamedForPlayer', arguments); },
    setColour : function ( arg0, arg1, arg2, arg3 ){  __CheckPointProxy.run(this.id, 'setColour', arguments); },
    getColourHex : function ( ){ return  __CheckPointProxy.run(this.id, 'getColourHex', arguments); },
    getPosition : function ( ){ return  __CheckPointProxy.run(this.id, 'getPosition', arguments); },
    setWorld : function ( arg0 ){  __CheckPointProxy.run(this.id, 'setWorld', arguments); },
    setRadius : function ( arg0 ){  __CheckPointProxy.run(this.id, 'setRadius', arguments); },
    getColour : function ( ){ return  __CheckPointProxy.run(this.id, 'getColour', arguments); },
    getWorld : function ( ){ return  __CheckPointProxy.run(this.id, 'getWorld', arguments); },
    setPosition : function ( arg0, arg1, arg2 ){  __CheckPointProxy.run(this.id, 'setPosition', arguments); },
    getRadius : function ( ){ return  __CheckPointProxy.run(this.id, 'getRadius', arguments); },
    getId : function ( ){ return  __CheckPointProxy.run(this.id, 'getId', arguments); },
    isValid : function ( ){ return  __CheckPointProxy.run(this.id, 'isValid', arguments); },
    print : function() {    var result = [];     for (var id in this) {   try {  if (typeof(this[id]) == "function") {  result.push(id + ": " + this[id].toString().split(")")[0]+")" );  }  } catch (err) { result.push(id + ": inaccessible");   }    }  console.log(result);  }


 }