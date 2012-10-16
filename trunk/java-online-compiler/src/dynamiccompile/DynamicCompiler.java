package dynamiccompile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import remotedebug.HackSystem;
import remotedebug.JavaClassExecuter;

public class DynamicCompiler {
	public static String run(String className, String code) {
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();  
        StandardJavaFileManager fileManager = jc.getStandardFileManager(null, null, null);  
        Location location = StandardLocation.CLASS_OUTPUT;
//        File outputDir = new File("build/classes/"); // in the common environment
        File outputDir = new File("./"); // in web environment
        File[] outputs = new File[]{outputDir};
        try {  
            fileManager.setLocation(location, Arrays.asList(outputs));  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        
        JavaFileObject jfo = new JavaSourceFromString(className, code);  
        JavaFileObject[] jfos = new JavaFileObject[]{jfo};
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(jfos);  
        boolean b = jc.getTask(new PrintWriter(HackSystem.err), fileManager, null, null, null, compilationUnits).call(); 
		if (b) {
			try {
				InputStream is = new FileInputStream(outputDir.getCanonicalPath() + File.separator + className + ".class"); // 读取文件字节码
				byte[] clazzBytes = new byte[is.available()];
				is.read(clazzBytes);
				is.close();
				
				// 删除临时的class文件
				File tempClassFile = new File(outputDir.getCanonicalPath() + File.separator + className + ".class");
				tempClassFile.delete();
				
				return JavaClassExecuter.execute(clazzBytes);
			} catch (Throwable e) {
				e.printStackTrace(HackSystem.out);
			}
		}
		String result = HackSystem.getBufferString();
		HackSystem.clearBuffer();
		return result;
	}
	
	public static void main(String[] args) {
		StringBuilder classStr = new StringBuilder("public class Foo{");  
        classStr.append("public static void main(String[] args){");  
        classStr.append("System.out.println(\"Foo2\");}}");
        
        System.out.println(run("Foo", classStr.toString()));
	}
}
