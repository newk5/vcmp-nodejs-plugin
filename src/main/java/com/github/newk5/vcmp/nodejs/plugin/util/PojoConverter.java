package com.github.newk5.vcmp.nodejs.plugin.util;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.utils.converters.JavetObjectConverter;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.reference.V8ValueObject;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@SuppressWarnings("unchecked")
public class PojoConverter extends JavetObjectConverter {

    public static final String METHOD_PREFIX_GET = "get";
    public static final String METHOD_PREFIX_IS = "is";
    protected static final Set<String> EXCLUDED_METHODS;

    static {
        EXCLUDED_METHODS = new HashSet<>();
        for (Method method : Object.class.getMethods()) {
            if (method.getParameterCount() == 0) {
                String methodName = method.getName();
                if (methodName.startsWith(METHOD_PREFIX_IS) || methodName.startsWith(METHOD_PREFIX_GET)) {
                    EXCLUDED_METHODS.add(methodName);
                }
            }
        }
    }

    @Override
    public V8Value toV8Value(V8Runtime v8Runtime, Object object) throws JavetException {
        V8Value v8Value = super.toV8Value(v8Runtime, object);
        if (v8Value != null && !(v8Value.isUndefined())) {
            return v8Value;
        }
        Class objectClass = object.getClass();
        V8ValueObject v8ValueObject = v8Runtime.createV8ValueObject();
        for (Method method : objectClass.getMethods()) {
            if (method.getParameterCount() == 0) {
                String methodName = method.getName();
                String propertyName = null;
                if (methodName.startsWith(METHOD_PREFIX_IS) && !EXCLUDED_METHODS.contains(methodName) && methodName.length() > METHOD_PREFIX_IS.length()) {
                    propertyName = methodName.substring(METHOD_PREFIX_IS.length(), METHOD_PREFIX_IS.length() + 1).toLowerCase(Locale.ROOT) + methodName.substring(METHOD_PREFIX_IS.length() + 1);
                } else if (methodName.startsWith(METHOD_PREFIX_GET) && !EXCLUDED_METHODS.contains(methodName) && methodName.length() > METHOD_PREFIX_GET.length()) {
                    propertyName = methodName.substring(METHOD_PREFIX_GET.length(), METHOD_PREFIX_GET.length() + 1).toLowerCase(Locale.ROOT) + methodName.substring(METHOD_PREFIX_GET.length() + 1);
                }
                if (propertyName != null) {
                    try (V8Value v8ValueTemp = toV8Value(v8Runtime, method.invoke(object))) {
                        v8ValueObject.set(propertyName, v8ValueTemp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        v8Value = v8ValueObject;
        return v8Runtime.decorateV8Value(v8Value);
    }
}
