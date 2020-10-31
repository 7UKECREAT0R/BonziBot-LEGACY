package com.lukecreator.BonziBot;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class DynamicExecutor {

	public static Class<?> compile(String code, OutputStream out) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
		// This code will correctly wrap any raw code given.
		code = 
			   "import net.dv8tion.jda.api.*;\n"
			 + "import java.*;\n"
			 + "public class compiled {\n"
			 + "	public Object msg = \"Assign to the msg field to set this message.\\nYou can also reference the **e** field and the **app** field to use messagereceivedevent and the bonzibot app instance.\";\n"
			 + "    public static void main(String[] args) {}\n"
			 + "	public Object getMsg(net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent e, com.lukecreator.BonziBot.App app) {\n"
			 + 			code + "\n"
			 + "		return msg;\n"
			 + "	}\n"
			 + "}";
		Path p = saveSource(code);
		Path p2 = compileSource(p, out);
		return getClass(p2);
	}
	
	// Credit to Nicolas Frankel for the guide on dynamic compilation.
    private static Path compileSource(Path javaFile, OutputStream out) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if(compiler == null) {
        	// just spam this bc i know checken and giraffey are stupid enough not to see it lmao
        	for(int i = 0; i < 25; i++) {
            	System.out.println("The java toolset is not properly installed. Navigate to: " + System.getProperty("java.home") + ", go back one directory and head into the JRE folder. "
            	+ "Copy tools.jar and paste it into the directory stated before to fix this error.");
        	}
        }
        compiler.run(System.in, System.out, out, javaFile.toFile().getAbsolutePath());
        return javaFile.getParent().resolve("compiled.java");
    }
    private static Path saveSource(String source) throws IOException {
        String tmpProperty = System.getProperty("java.io.tmpdir");
        Path sourcePath = Paths.get(tmpProperty, "compiled.java");
        if(sourcePath == null) {
        	System.out.print("sourcePath == null\n");
        	return null;
        }
        Files.write(sourcePath, source.getBytes());
        return sourcePath;
    }
    private static Class<?> getClass(Path javaClass)
            throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL classUrl = javaClass.getParent().toFile().toURI().toURL();
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{classUrl});
        Class<?> clazz = Class.forName("compiled", true, classLoader);
        return clazz;
    }
}
