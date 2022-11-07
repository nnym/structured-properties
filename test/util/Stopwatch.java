package util;

import java.lang.reflect.Method;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

public class Stopwatch implements InvocationInterceptor {
	@Override public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) {
		var start = System.currentTimeMillis();

		try {
			invocation.proceed();
		} finally {
			System.out.printf("%s::%s took %s ms.%n", extensionContext.getTestClass().get().getName(), extensionContext.getDisplayName(), System.currentTimeMillis() - start);
		}
	}
}
