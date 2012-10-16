package remotedebug;

import java.lang.reflect.Method;

public class JavaClassExecuter {
	public static String execute(byte[] classBytes) {
		HackSystem.clearBuffer();
		ClassModifier cm = new ClassModifier(classBytes);
		byte[] modibytes = cm.modifyUTF8Constant("java/lang/System", "remotedebug/HackSystem");
		HotSwapClassLoader loader = new HotSwapClassLoader();
		Class clazz = loader.loadByte(modibytes);
		try {
			Method method = clazz.getMethod("main", new Class[] {String[].class});
			method.invoke(null, new String[] {null});
		} catch (Throwable e) {
			e.printStackTrace(HackSystem.out);
		}
		return HackSystem.getBufferString();
	}
}
