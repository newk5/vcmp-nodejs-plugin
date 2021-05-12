package com.github.newk5.vcmp.nodejs.plugin.util;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.JavetObjectConverter;
import com.caoccao.javet.utils.JavetCallbackContext;
import com.caoccao.javet.values.V8Value;

import com.caoccao.javet.values.reference.V8ValueObject;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class EntityConverter extends JavetObjectConverter {

    public static final String METHOD_PREFIX_GET = "get";
    public static final String METHOD_PREFIX_IS = "is";
    protected static final Set<String> EXCLUDED_METHODS;

    static {
        EXCLUDED_METHODS = new HashSet<>();
        EXCLUDED_METHODS.add("equals");
     //  EXCLUDED_METHODS.add("sync");
        EXCLUDED_METHODS.add("startThreadSynced");
        EXCLUDED_METHODS.add("isUnloaded");
        EXCLUDED_METHODS.add("runSyncBlocks");
        EXCLUDED_METHODS.add("reloadScript");
        EXCLUDED_METHODS.add("sendScriptData");
        EXCLUDED_METHODS.add("endThreadSynced");
        for (Method method : Object.class.getMethods()) {
            if (method.getParameterCount() == 0) {
                String methodName = method.getName();

                EXCLUDED_METHODS.add(methodName);

            }
        }
    }

    @Override
    public V8Value toV8Value(V8Runtime v8, Object object) throws JavetException {
        V8Value v8Value = null;
        V8ValueObject v8ValueObject = null;
        try {
            v8Value = super.toV8Value(v8, object);
            if (v8Value != null && !(v8Value.isUndefined())) {
                return v8Value;
            }
            Class objectClass = object.getClass();
            v8ValueObject = v8.createV8ValueObject();
            for (Method method : objectClass.getMethods()) {

                if (!EXCLUDED_METHODS.contains(method.getName())) {
                    JavetCallbackContext callback = new JavetCallbackContext(object, method, false);
                    
                    v8ValueObject.setFunction(method.getName(), callback);
                }

            }
            v8Value = v8ValueObject;
            return v8.decorateV8Value(v8Value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v8.createV8ValueUndefined();
    }
}
