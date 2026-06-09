package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

/** Toma lecturas livianas de memoria del runtime sin acoplar UML Clases a APIs del sistema operativo. */
final class UmlClassRuntimeMemoryMonitor {

    UmlClassRuntimeMemorySnapshot snapshot() {
        return UmlClassRuntimeMemorySnapshot.fromRuntime(Runtime.getRuntime(), freeSystemMemoryBytes());
    }

    private long freeSystemMemoryBytes() {
        Object bean = ManagementFactory.getOperatingSystemMXBean();
        long current = invokeLong(bean, "getFreeMemorySize");
        if (current > 0L) {
            return current;
        }
        return invokeLong(bean, "getFreePhysicalMemorySize");
    }

    private static long invokeLong(Object target, String methodName) {
        if (target == null || methodName == null || methodName.isBlank()) {
            return 0L;
        }
        try {
            Method method = target.getClass().getMethod(methodName);
            method.setAccessible(true);
            Object value = method.invoke(target);
            return value instanceof Number number ? Math.max(0L, number.longValue()) : 0L;
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return 0L;
        }
    }
}
