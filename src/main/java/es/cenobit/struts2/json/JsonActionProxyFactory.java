package es.cenobit.struts2.json;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.DefaultActionProxyFactory;

public class JsonActionProxyFactory extends DefaultActionProxyFactory implements ActionProxyFactory {

	public JsonActionProxyFactory() {
		super();
	}

	public ActionProxy createActionProxy(String namespace, String actionName, String methodName,
			Map<String, Object> extraContext, boolean executeResult, boolean cleanupContext) {

		ActionInvocation inv = new JsonActionInvocation(extraContext, true);
		container.inject(inv);
		return createActionProxy(inv, namespace, actionName, methodName, executeResult, cleanupContext);
	}

	public ActionProxy createActionProxy(ActionInvocation inv, String namespace, String actionName, String methodName,
			boolean executeResult, boolean cleanupContext) {

		JsonActionProxy proxy = new JsonActionProxy(inv, namespace, actionName, methodName, executeResult,
				cleanupContext);
		container.inject(proxy);
		proxy.prepare();
		return proxy;
	}

}
