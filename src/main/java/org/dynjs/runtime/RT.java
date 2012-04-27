/**
 *  Copyright 2012 Douglas Campos, and individual contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.runtime;

import org.dynalang.dynalink.support.Lookup;
import org.dynjs.api.Function;
import org.dynjs.runtime.linker.DynJSBootstrapper;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static java.lang.invoke.MethodType.methodType;
import static me.qmx.jitescript.util.CodegenUtils.p;

public class RT {

    public static final Handle BOOTSTRAP = new Handle(Opcodes.H_INVOKESTATIC,
            p(DynJSBootstrapper.class), "bootstrap", methodType(CallSite.class,
            MethodHandles.Lookup.class, String.class, MethodType.class).toMethodDescriptorString());
    public static final Object[] BOOTSTRAP_ARGS = new Object[0];

    public static final MethodHandle FUNCTION_CALL;

    static {
        MethodType functionMethodType = methodType(Object.class, DynThreadContext.class, Object[].class);
        FUNCTION_CALL = Lookup.PUBLIC.findVirtual(Function.class, "call", functionMethodType);
    }

    public static DynFunction paramPopulator(DynFunction function, Object[] args) {
        String[] parameters = function.getArguments();
        for (int i = 0; i < parameters.length; i++) {
            String parameter = parameters[i];
            if (i < args.length) {
                function.define(parameter, args[i]);
            }
        }
        // function.define("arguments", args); TODO
        return function;
    }

}
