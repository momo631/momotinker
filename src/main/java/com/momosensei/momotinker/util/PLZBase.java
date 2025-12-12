package com.momosensei.momotinker.util;

import cpw.mods.niofs.union.UnionFileSystem;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PLZBase {
	public static final Unsafe UNSAFE = getUnsafe();
	public static final MethodHandles.Lookup LOOKUP = getLookup();
	public static final MethodHandle ClassLoader_defineClass0;
	public static final MethodHandle ClassLoader_defineClass1;
	public static final Map<String, Class<?>> HIDDEN_CLASSES_MAP = new HashMap<>();
	@SuppressWarnings("resource")
	public static final Scanner scanner = new Scanner(System.in);
	static {
		try {
			ClassLoader_defineClass0 = LOOKUP.findStatic(ClassLoader.class, "defineClass0", MethodType.methodType(Class.class, ClassLoader.class, Class.class, String.class, byte[].class, int.class, int.class, ProtectionDomain.class, boolean.class, int.class, Object.class));
			ClassLoader_defineClass1 = LOOKUP.findStatic(ClassLoader.class, "defineClass1", MethodType.methodType(Class.class, ClassLoader.class, String.class, byte[].class, int.class, int.class, ProtectionDomain.class, String.class));
		} catch (Throwable e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static Unsafe getUnsafe() {
		if (UNSAFE != null)
			return UNSAFE;
		try {
			Constructor<Unsafe> c = Unsafe.class.getDeclaredConstructor();
			c.setAccessible(true);
			Unsafe instance = c.newInstance();
			return instance;
		} catch (Throwable var3) {
			throw new RuntimeException(var3);
		}
	}

	public static MethodHandles.Lookup getLookup() {
		try {
			Constructor<MethodHandles.Lookup> c = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Class.class, int.class);
			return (MethodHandles.Lookup) ((MethodHandles.Lookup) UNSAFE.getObjectVolatile(UNSAFE.staticFieldBase(MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP")), UNSAFE.staticFieldOffset(MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP")))).unreflectConstructor(c).invoke(Object.class, null, -1);
		} catch (Throwable var3) {
			throw new RuntimeException(var3);
		}
	}

	public static void klassPtr(Object o, Class<?> clazz) {
		if (o == null || clazz == null)
			return;
		if (o.getClass().equals(clazz))
			return;
		InternalUnsafe.ensureClassInitialized(clazz);
		try {
			InternalUnsafe.putIntVolatile(o, 8, InternalUnsafe.getIntVolatile(InternalUnsafe.allocateInstance(clazz), 8));
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public static void extractJar(String jarPath, String destDir) throws IOException {
		File destDirectory = new File(destDir);
		if (!destDirectory.exists()) {
			destDirectory.mkdirs();
		}
		try (JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarPath))) {
			JarEntry entry;
			while ((entry = jarInputStream.getNextJarEntry()) != null) {
				String entryName = entry.getName();
				File entryFile = new File(destDir, entryName);
				if (entry.isDirectory()) {
					entryFile.mkdirs();
					continue;
				}
				File parent = entryFile.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				try (OutputStream outputStream = new FileOutputStream(entryFile)) {
					byte[] buffer = readAllBytes(jarInputStream);
					outputStream.write(buffer);
				}
			}
		}
	}

	public static byte[] readAllBytes(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] data = new byte[4096];
		int bytesRead;
		while ((bytesRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, bytesRead);
		}
		buffer.flush();
		return buffer.toByteArray();
	}

	public static List<String> matchAllReplace(String s, String from, String to) {
		List<String> r = new ArrayList<>();
		if (from.isEmpty())
			return Collections.singletonList(s);
		int i = s.indexOf(from);
		if (i == -1)
			return Collections.singletonList(s);
		for (String t : matchAllReplace(s.substring(i + from.length()), from, to)) {
			r.add(s.substring(0, i) + from + t);
			r.add(s.substring(0, i) + to + t);
		}
		return r;
	}

	public static List<String> matchAllReplaceMin(String s, String from, String to) {
		return from.isEmpty() ? Collections.singletonList(s) : s.contains(from) ? Stream.concat(matchAllReplace(s.substring(s.indexOf(from) + from.length()), from, to).stream().map(t -> s.substring(0, s.indexOf(from)) + from + t), matchAllReplace(s.substring(s.indexOf(from) + from.length()), from, to).stream().map(t -> s.substring(0, s.indexOf(from)) + to + t)).collect(Collectors.toList()) : Collections.singletonList(s);
	}

	public static byte[] getClassBytes(String jarPath, String className) throws Exception {
		if (jarPath.isEmpty()) {
			throw new FileNotFoundException("Jar not found: " + className);
		}
		try (JarFile jarFile = new JarFile(jarPath)) {
			String classPath = className.replace('.', '/') + ".class";
			JarEntry entry = jarFile.getJarEntry(classPath);
			if (entry == null) {
				jarFile.close();
				throw new ClassNotFoundException("Class not found in jar: " + className);
			}
			try (InputStream is = jarFile.getInputStream(entry)) {
				return readAllBytes(is);
			}
		}
	}

	public static byte[] getClassBytes(Class<?> clazz, boolean fromSource) throws Exception {
		if (fromSource) {
			return getClassBytes(getJarPath(clazz), clazz.getName());
		} else {
			InputStream is = clazz.getResourceAsStream("/" + clazz.getName().replace('.', '/') + ".class");
			if (is == null) {
				throw new FileNotFoundException(clazz.getName());
			}
			return readAllBytes(is);
		}
	}

	public static String getJarPath() {
		return getJarPath(PLZBase.class);
	}

	public static String getJarPath(Class<?> clazz) {
		try {
			String s = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
			return new File(s.substring(0, s.lastIndexOf(".jar") + 4)).getAbsolutePath();
		} catch (Throwable e) {
		}
		try {
			return clazz.getModule().getLayer().configuration().findModule(clazz.getModule().getName()).flatMap(rm -> rm.reference().location()).map(Path::of).map(p -> p.getFileSystem() instanceof UnionFileSystem ufs ? ufs.getPrimaryPath() : p).map(Path::toAbsolutePath).map(Path::toString).orElse("none");
		} catch (Throwable t) {
		}
		try {
			String name = clazz.getName().replace('.', '/') + ".class";
			for (ClassLoader loader : new HashSet<>(List.of(clazz.getClassLoader(), Thread.currentThread().getContextClassLoader(), ClassLoader.getSystemClassLoader(), ClassLoader.getPlatformClassLoader()))) {
				String s = loader.getResource(name).getPath();
				return new File(s.substring(0, s.lastIndexOf(".jar") + 4)).getAbsolutePath();
			}
		} catch (Throwable e) {
		}
		return "none";
	}

	public static List<String> getAllClassNamesFromJar(String jarPath) {
		List<String> classNames = new ArrayList<>();
		for (String path : matchAllReplace(jarPath, "_", "!")) {
			try (JarFile jarFile = new JarFile(path)) {
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					String entryName = entries.nextElement().getName();
					if (entryName.endsWith(".class")) {
						classNames.add(entryName.replace('/', '.').substring(0, entryName.length() - 6));
					}
				}
				jarFile.close();
				break;
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}
		return classNames;
	}

	public static Map<String, byte[]> getAllClassBytesFromJar(String jarPath) {
		try (JarFile jarFile = new JarFile(new File(jarPath))) {
			return jarFile.stream().filter(e -> e.getName().endsWith(".class") && !e.isDirectory()).collect(Collectors.toMap(e -> e.getName(), e -> {
				try {
					return jarFile.getInputStream(jarFile.getEntry(e.getName())).readAllBytes();
				} catch (IOException e1) {
					throw new RuntimeException(e1);
				}
			}));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<Object, Object> getManifestAttributes(String jarPath) {
		try (JarFile jarFile = new JarFile(jarPath)) {
			Manifest manifest = jarFile.getManifest();
			return (manifest != null) ? manifest.getMainAttributes() : null;
		} catch (IOException e) {
			return new HashMap<>();
		}
	}

	public static List<String> filesInFolder(String folderPath, String extension) {
		List<String> filePaths = new ArrayList<>();
		File folder = new File(folderPath);
		if (!folder.exists() || !folder.isDirectory()) {
			return filePaths;
		}
		String suffix = extension.startsWith(".") ? extension : "." + extension;
		suffix = suffix.toLowerCase();
		File[] files = folder.listFiles();
		if (files == null) {
			return filePaths;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				filePaths.addAll(filesInFolder(file.getAbsolutePath(), extension));
			} else {
				String fileName = file.getName().toLowerCase();
				if (fileName.endsWith(suffix)) {
					filePaths.add(file.getAbsolutePath());
				}
			}
		}
		return filePaths;
	}

	public static String readFile(String filePath) throws Exception {
		Path path = Paths.get(filePath);
		File file = path.toFile();
		if (!file.exists()) {
			throw new RuntimeException("null");
		}
		byte[] encoded = Files.readAllBytes(path);
		return new String(encoded, StandardCharsets.UTF_8);
	}

	public static void writeFile(String filePath, String content) throws Throwable {
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(content);
		}
	}

	public static String getStackTrace() {
		StringBuilder builder = new StringBuilder();
		for (StackTraceElement stackTrace : Thread.currentThread().getStackTrace()) {
			builder.append(stackTrace);
			builder.append("\n");
		}
		return builder.toString();
	}

	public static void openAccess(Module targetModule, Class<?> myClass) {
		try {
			if (targetModule.canRead(myClass.getModule())) {
				return;
			}
			LOOKUP.unreflect(Module.class.getDeclaredMethod("implAddReads", Module.class)).bindTo(targetModule).invoke(myClass.getModule());
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public static String input(String s) {
		System.out.print(s);
		return scanner.nextLine();
	}

	public static void cls() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		} catch (Exception e) {
		}
	}

	public static String[] splitFirst(String input, String target) {
		int firstDotIndex = input.indexOf(target);
		if (firstDotIndex == -1) {
			return new String[] { input, "" };
		}
		return new String[] { input.substring(0, firstDotIndex), input.substring(firstDotIndex + 1) };
	}

	public static String[] splitLast(String input, String target) {
		int lastIndex = input.lastIndexOf(target);
		if (lastIndex == -1) {
			return new String[] { input, "" };
		}
		return new String[] { input.substring(0, lastIndex), input.substring(lastIndex + 1) };
	}

	public static List<String> dumpCmdline(String cmdline) {
		List<String> argv = new ArrayList<>();
		StringBuilder arg = new StringBuilder();
		boolean inQuotes = false;
		char quoteChar = 0;
		for (char c : cmdline.toCharArray()) {
			if (c == '"' || c == '\'') {
				if (!inQuotes) {
					inQuotes = true;
					quoteChar = c;
				} else if (quoteChar == c)
					inQuotes = false;
				else
					arg.append(c);
			} else if (Character.isWhitespace(c) && !inQuotes) {
				if (arg.length() > 0) {
					argv.add(arg.toString());
					arg.setLength(0);
				}
			} else
				arg.append(c);
		}
		if (arg.length() > 0)
			argv.add(arg.toString());
		return argv;
	}

	public static void selectCopyFile(String sourceDir, String targetDirForSpecified, String targetDirForOthers, List<String> specifiedSuffixes) throws IOException {
		Files.createDirectories(Paths.get(targetDirForSpecified));
		Files.createDirectories(Paths.get(targetDirForOthers));
		Files.walk(Paths.get(sourceDir)).filter(Files::isRegularFile).forEach(sourcePath -> {
			try {
				String fileName = sourcePath.getFileName().toString();
				int dotIndex = fileName.lastIndexOf('.');
				String suffix = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
				String targetDir = specifiedSuffixes.contains(suffix.toLowerCase()) ? targetDirForSpecified : targetDirForOthers;
				Path relativePath = Paths.get(sourceDir).relativize(sourcePath);
				Path targetPath = Paths.get(targetDir, relativePath.toString());
				Files.createDirectories(targetPath.getParent());
				Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static Map<Object, Object> getMainManifestAttributes(String jarPath) {
		try (JarFile jarFile = new JarFile(jarPath)) {
			Manifest manifest = jarFile.getManifest();
			return (manifest != null) ? manifest.getMainAttributes() : null;
		} catch (IOException e) {
			return new HashMap<>();
		}
	}

	public static List<String> getFilePaths(String folderPath, String extension) {
		List<String> filePaths = new ArrayList<>();
		File folder = new File(folderPath);
		if (!folder.exists() || !folder.isDirectory()) {
			return filePaths;
		}
		String suffix = extension.startsWith(".") ? extension : "." + extension;
		suffix = suffix.toLowerCase();
		File[] files = folder.listFiles();
		if (files == null) {
			return filePaths;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				filePaths.addAll(getFilePaths(file.getAbsolutePath(), extension));
			} else {
				String fileName = file.getName().toLowerCase();
				if (fileName.endsWith(suffix)) {
					filePaths.add(file.getAbsolutePath());
				}
			}
		}
		return filePaths;
	}

	public static void copyFields(Object old, Object next) {
		Map<String, Object> oldFieldMap = new HashMap<>();
		for (Field field : old.getClass().getDeclaredFields()) {
			try {
				if (!Modifier.isStatic(field.getModifiers())) {
					oldFieldMap.put(field.getName(), LOOKUP.unreflectGetter(field).invoke(old));
				}
			} catch (Throwable e) {
			}
		}
		for (Field field : next.getClass().getDeclaredFields()) {
			if (oldFieldMap.containsKey(field.getName()) && !Modifier.isStatic(field.getModifiers())) {
				Object obj = oldFieldMap.get(field.getName());
				try {
					LOOKUP.unreflectSetter(field).invoke(next, obj);
				} catch (Throwable e) {
				}
			}
		}
	}

	public static String toVMName(String type) {
		if (type == null || type.isEmpty()) {
			return type;
		}
		switch (type) {
			case "int":
				return "I";
			case "float":
				return "F";
			case "long":
				return "J";
			case "double":
				return "D";
			case "boolean":
				return "Z";
			case "byte":
				return "B";
			case "char":
				return "C";
			case "short":
				return "S";
			case "void":
				return "V";
		}
		if (type.endsWith("[]")) {
			int dimensions = 0;
			while (type.endsWith("[]")) {
				dimensions++;
				type = type.substring(0, type.length() - 2);
			}
			String baseType = toVMName(type);
			return "[".repeat(dimensions) + baseType;
		}
		if (type.contains(".")) {
			return "L" + type.replace('.', '/') + ";";
		}
		return type;
	}

	public static String fromVMName(String vmType) {
		if (vmType == null || vmType.isEmpty()) {
			return vmType;
		}
		switch (vmType) {
			case "I":
				return "int";
			case "F":
				return "float";
			case "J":
				return "long";
			case "D":
				return "double";
			case "Z":
				return "boolean";
			case "B":
				return "byte";
			case "C":
				return "char";
			case "S":
				return "short";
			case "V":
				return "void";
		}
		if (vmType.startsWith("[")) {
			int arrayDepth = 0;
			while (arrayDepth < vmType.length() && vmType.charAt(arrayDepth) == '[') {
				arrayDepth++;
			}
			String elementType = fromVMName(vmType.substring(arrayDepth));
			return elementType + "[]".repeat(arrayDepth);
		}
		if (vmType.startsWith("L") && vmType.endsWith(";")) {
			vmType = vmType.substring(1, vmType.length() - 1);
		}
		return vmType.replace('/', '.');
	}

	@SuppressWarnings("unchecked")
	public static <T> T copy(T original) throws Throwable {
		if (original == null) {
			return null;
		}
		if (original.getClass().isArray()) {
			int length = Array.getLength(original);
			Object newArray = Array.newInstance(original.getClass().getComponentType(), length);
			System.arraycopy(original, 0, newArray, 0, length);
			return (T) newArray;
		}
		T copy = (T) InternalUnsafe.allocateInstance(original.getClass());
		PLZBase.copyFields(original, copy);
		return copy;
	}

	public static String realClassName(Class<?> klass) {
		return realClassName(klass.getName());
	}

	public static String realClassName(String name) {
		return fromVMName(name).split("\\$\\$")[0].split("/")[0];
	}

	public static String lowCaseFlowString(String str, int size, double speed) {
		StringBuilder builder = new StringBuilder(str);
		long time = System.currentTimeMillis();
		for (int g = 0; g < size; g++) {
			int i = (int) (((long) time / (speed * 1000) + g) % builder.length());
			builder.setCharAt(i, Character.toLowerCase(builder.charAt(i)));
		}
		return builder.toString();
	}

	public static String waveString(String text, int waveHeight, double speed, double distance) {
		char[] chars = text.toCharArray();
		int length = chars.length;
		int totalRows = waveHeight + 2;
		char[][] matrix = new char[totalRows][length];
		for (char[] row : matrix) {
			Arrays.fill(row, ' ');
		}
		double time = System.currentTimeMillis() * -0.001 * speed;
		for (int i = 0; i < length; i++) {
			double wave = Math.sin(time + i * distance) * (waveHeight / 2 + 2);
			int row = (int) (waveHeight / 2 - wave + 1);
			row = Math.max(0, Math.min(row, totalRows - 1));
			matrix[row][i] = chars[i];
		}
		StringBuilder sb = new StringBuilder();
		for (char[] row : matrix) {
			sb.append(row).append("\n");
		}
		return sb.toString();
	}

	public static void unJar(String jarPath, String destDirPath) throws Throwable {
		File jarFile = new File(jarPath);
		if (!jarFile.exists()) {
			return;
		}
		File destDir = new File(destDirPath);
		if (!destDir.exists() && !destDir.mkdirs()) {
			return;
		}
		String targetCanonicalPath = destDir.getCanonicalPath();
		try (JarFile jar = new JarFile(jarFile)) {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName();
				File destFile = new File(destDir, entryName);
				String destCanonicalPath = destFile.getCanonicalPath();
				if (!destCanonicalPath.startsWith(targetCanonicalPath + File.separator)) {
					return;
				}
				if (entry.isDirectory()) {
					if (!destFile.exists() && !destFile.mkdirs()) {
						return;
					}
				} else {
					File parent = destFile.getParentFile();
					if (parent != null && !parent.exists() && !parent.mkdirs()) {
						return;
					}
					try (InputStream is = jar.getInputStream(entry); OutputStream os = new BufferedOutputStream(new FileOutputStream(destFile))) {
						byte[] buffer = new byte[4096];
						int bytesRead;
						while ((bytesRead = is.read(buffer)) != -1) {
							os.write(buffer, 0, bytesRead);
						}
					}
				}
			}
		}
	}

	public static boolean wasLoaded(String name, ClassLoader loader) {
		try {
			for (Class<?> c : new ArrayList<>((ArrayList<Class<?>>) PLZBase.LOOKUP.findGetter(ClassLoader.class, "classes", ArrayList.class).invoke(loader))) {
				if (c.getName().equals(name)) {
					return true;
				}
			}
		} catch (Throwable e) {
		}
		return false;
	}

	public static Class<?> defineClass(ClassLoader loader, String name, byte[] buf) {
		try {
			return (Class<?>) ClassLoader_defineClass1.invoke(loader, name, buf, 0, buf.length, null, null);
		} catch (Throwable e1) {
			try {
				return Class.forName(name);
			} catch (Exception e) {
				e1.addSuppressed(e);
				throw new RuntimeException(e1);
			}
		}
	}

	public static Class<?> defineClassInPackage(ClassLoader loader, Class<?> lookup, String name) {
		try {
			return defineClass(loader, name, getClassBytes(getJarPath(lookup), name));
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

//	public static Class<?> defineHiddenClass(ClassLoader loader, Class<?> lookup, String name, byte[] buf, MethodHandles.Lookup.ClassOption... options) {
//		if (HIDDEN_CLASSES_MAP.containsKey(name)) {
//			return HIDDEN_CLASSES_MAP.get(name);
//		}
//		int flags = 2 | MethodHandles.Lookup.ClassOption.optionsToFlag(Set.of(options));
//		if (loader == null || loader == ClassLoader.getPlatformClassLoader()) {
//			flags |= 8;
//		}
//		try {
//			Class<?> klass = (Class<?>) ClassLoader_defineClass0.invoke(loader, lookup, name, buf, 0, buf.length, null, true, flags, null);
//			HIDDEN_CLASSES_MAP.put(name, klass);
//			return klass;
//		} catch (Throwable e1) {
//			throw new RuntimeException(e1);
//		}
//	}

//	public static Class<?> defineHiddenClassInPackage(ClassLoader loader, Class<?> lookup, String name, MethodHandles.Lookup.ClassOption... options) {
//		try {
//			return defineHiddenClass(loader, lookup, name, getClassBytes(getJarPath(lookup), name), options);
//		} catch (Throwable e) {
//			throw new RuntimeException(e);
//		}
//	}

	public static Class<?> findClass(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public static Field findField(Class<?> klass, String name) {
		Class<?> current = klass;
		while (current != null) {
			try {
				return current.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				current = current.getSuperclass();
			}
		}
		throw new RuntimeException(new NoSuchFieldException(name));
	}

	public static Object getField(Object target, String name) {
		return getField(target, findField(target instanceof Class cls ? cls : target.getClass(), name));
	}

	public static Object getField(Object target, Field f) {
		boolean isStatic = target instanceof Class;
		try {
			MethodHandle getter = LOOKUP.unreflectGetter(f);
			return isStatic ? getter.invoke() : getter.invoke(target);
		} catch (Throwable e) {
			Object base = isStatic ? UNSAFE.staticFieldBase(f) : target;
			long offset = isStatic ? UNSAFE.staticFieldOffset(f) : UNSAFE.objectFieldOffset(f);
			switch (f.getType().getName()) {
				case "int":
					return UNSAFE.getIntVolatile(base, offset);
				case "long":
					return UNSAFE.getLongVolatile(base, offset);
				case "boolean":
					return UNSAFE.getBooleanVolatile(base, offset);
				case "byte":
					return UNSAFE.getByteVolatile(base, offset);
				case "char":
					return UNSAFE.getCharVolatile(base, offset);
				case "short":
					return UNSAFE.getShortVolatile(base, offset);
				case "float":
					return UNSAFE.getFloatVolatile(base, offset);
				case "double":
					return UNSAFE.getDoubleVolatile(base, offset);
				default:
					return UNSAFE.getObjectVolatile(base, offset);
			}
		}
	}

	public static Object setField(Object target, String name, Object value) {
		return setField(target, findField(target instanceof Class cls ? cls : target.getClass(), name), value);
	}

	public static Object setField(Object target, Field f, Object value) {
		boolean isStatic = target instanceof Class;
		Object old = getField(target, f);
		try {
			MethodHandle setter = LOOKUP.unreflectSetter(f);
			if (target instanceof Class) {
				setter.invoke(value);
			} else {
				setter.invoke(target, value);
			}
		} catch (Throwable e) {
			Object base = isStatic ? UNSAFE.staticFieldBase(f) : target;
			long offset = isStatic ? UNSAFE.staticFieldOffset(f) : UNSAFE.objectFieldOffset(f);
			switch (f.getType().getName()) {
				case "int":
					UNSAFE.putIntVolatile(base, offset, (int) value);
				case "long":
					UNSAFE.putLongVolatile(base, offset, (long) value);
				case "boolean":
					UNSAFE.putBooleanVolatile(base, offset, (boolean) value);
				case "byte":
					UNSAFE.putByteVolatile(base, offset, (byte) value);
				case "char":
					UNSAFE.putCharVolatile(base, offset, (char) value);
				case "short":
					UNSAFE.putShortVolatile(base, offset, (short) value);
				case "float":
					UNSAFE.putFloatVolatile(base, offset, (float) value);
				case "double":
					UNSAFE.putDoubleVolatile(base, offset, (double) value);
				default:
					UNSAFE.putObjectVolatile(base, offset, value);
			}
		}
		return old;
	}

	public static Method findMethod(Class<?> klass, String name, Class<?>... argTypes) {
		final int argLen = argTypes.length;
		Class<?> current = klass;
		while (current != null) {
			for (Method m : current.getDeclaredMethods()) {
				if (!m.getName().equals(name))
					continue;
				Class<?>[] declared = m.getParameterTypes();
				if (declared.length != argLen)
					continue;
				boolean match = true;
				for (int i = 0; i < argLen; i++) {
					Class<?> provided = argTypes[i];
					if (provided != null && provided != Object.class && !declared[i].isAssignableFrom(provided)) {
						match = false;
						break;
					}
				}
				if (match)
					return m;
			}
			current = current.getSuperclass();
		}
		throw new RuntimeException(new NoSuchMethodException(name));
	}

	public static Object invoke(Object target, String name, Object... args) {
		Class<?> klass = (target instanceof Class) ? (Class<?>) target : target.getClass();
		Class<?>[] argTypes = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		try {
			Method method = findMethod(klass, name, argTypes);
			MethodHandle methodHandle = LOOKUP.unreflect(method);
			if (target instanceof Class) {
				return methodHandle.invokeWithArguments(args);
			} else {
				Object[] combinedArgs = new Object[args.length + 1];
				combinedArgs[0] = target;
				System.arraycopy(args, 0, combinedArgs, 1, args.length);
				return methodHandle.invokeWithArguments(combinedArgs);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] readFileBytes(String fileName) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(fileName));
			out = new ByteArrayOutputStream();
			byte[] tempbytes = new byte[in.available()];
			for (int i = 0; (i = in.read(tempbytes)) != -1;) {
				out.write(tempbytes, 0, i);
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return out.toByteArray();
	}

	public static void writeFileBytes(String fileName, byte[] bytes, boolean append) throws IOException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(fileName, append));
			out.write(bytes);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static void writeFileBytes(String fileName, byte[] bytes) throws IOException {
		writeFileBytes(fileName, bytes, false);
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] reverse(T[] original) {
		if (original == null) {
			return null;
		}
		T[] reversed = (T[]) Array.newInstance(original.getClass().getComponentType(), original.length);
		for (int i = 0; i < original.length; i++) {
			reversed[i] = original[original.length - 1 - i];
		}
		return reversed;
	}

	public static int[] reverse(int[] original) {
		if (original == null) {
			return null;
		}
		int[] reversed = new int[original.length];
		for (int i = 0; i < original.length; i++) {
			reversed[i] = original[original.length - 1 - i];
		}
		return reversed;
	}

	public static String dumpClassName(byte[] bytes) {
		int int1 = 0, offset = 10;
		int[] cpInfoOffsets;
		int int2, cpInfoSize, currentCpInfoIndex;
		byte val;
		for (int2 = (bytes[8] & 0xFF) << 8 | bytes[9] & 0xFF, cpInfoOffsets = new int[int2], currentCpInfoIndex = 1; currentCpInfoIndex < int2; offset += cpInfoSize) {
			cpInfoOffsets[currentCpInfoIndex++] = offset + 1;
			val = bytes[offset];
			if (val == 9 || val == 10 || val == 11 || val == 3 || val == 4 || val == 12 || val == 17 || val == 18) {
				cpInfoSize = 5;
			} else if (val == 5 || val == 6) {
				cpInfoSize = 9;
				currentCpInfoIndex++;
			} else if (val == 1) {
				cpInfoSize = 3 + ((bytes[offset + 1] & 0xFF) << 8 | bytes[offset + 2] & 0xFF);
				if (cpInfoSize > int1) {
					int1 = cpInfoSize;
				}
			} else if (val == 15) {
				cpInfoSize = 4;
			} else if (val == 7 || val == 8 || val == 16 || val == 20 || val == 19) {
				cpInfoSize = 3;
			} else {
				throw new IllegalArgumentException();
			}
		}
		char[] charBuffer = new char[int1];
		int1 = (bytes[offset + 2] & 0xFF) << 8 | bytes[offset + 3] & 0xFF;
		offset = cpInfoOffsets[int1];
		if (bytes[offset - 1] == 7) {
			int1 = (bytes[offset] & 0xFF) << 8 | bytes[offset + 1] & 0xFF;
			if (int1 == 0) {
				throw new ClassFormatError();
			}
			offset = cpInfoOffsets[int1];
			int2 = offset + 2 + ((bytes[offset] & 0xFF) << 8 | bytes[offset + 1] & 0xFF);
			offset += 2;
			int1 = 0;
			while (offset < int2) {
				val = bytes[offset++];
				if ((val & 0x80) == 0) {
					charBuffer[int1++] = (char) (val & 0x7F);
				} else if ((val & 0xE0) == 0xC0) {
					charBuffer[int1++] = (char) (((val & 0x1F) << 6) + (bytes[offset++] & 0x3F));
				} else {
					charBuffer[int1++] = (char) (((val & 0xF) << 12) + ((bytes[offset++] & 0x3F) << 6) + (bytes[offset++] & 0x3F));
				}
			}
			return new String(charBuffer, 0, int1);
		}
		throw new ClassFormatError("this_class item: #" + int1 + " not a CONSTANT_Class_info");
	}

	public static String dumpSuperName(byte[] bytes) {
		int index = 1;
		int offset = 10;
		int maxStringLength = 0;
		int currentByte;
		int val = (bytes[8] & 0xFF) << 8 | bytes[9] & 0xFF;
		int[] cpInfoOffsets = new int[val];
		while (index < val) {
			cpInfoOffsets[index++] = offset + 1;
			int cpInfoSize;
			currentByte = bytes[offset];
			if (currentByte == 9 || currentByte == 10 || currentByte == 11 || currentByte == 12 || currentByte == 3 || currentByte == 4 || currentByte == 18 || currentByte == 17) {
				cpInfoSize = 5;
			} else if (currentByte == 5 || currentByte == 6) {
				cpInfoSize = 9;
				index++;
			} else if (currentByte == 1) {
				cpInfoSize = 3 + ((bytes[offset + 1] & 0xFF) << 8 | bytes[offset + 2] & 0xFF);
				if (cpInfoSize > maxStringLength) {
					maxStringLength = cpInfoSize;
				}
			} else if (currentByte == 15) {
				cpInfoSize = 4;
			} else if (currentByte == 7 || currentByte == 8 || currentByte == 16 || currentByte == 20 || currentByte == 19) {
				cpInfoSize = 3;
			} else {
				throw new IllegalArgumentException();
			}
			offset += cpInfoSize;
		}
		char[] charBuffer = new char[maxStringLength];
		offset = cpInfoOffsets[(bytes[offset + 4] & 0xFF) << 8 | bytes[offset + 5] & 0xFF];
		val = (bytes[offset] & 0xFF) << 8 | bytes[offset + 1] & 0xFF;
		if (offset == 0 || val == 0) {
			throw new ClassFormatError();
		}
		offset = cpInfoOffsets[val];
		val = offset + 2 + ((bytes[offset] & 0xFF) << 8 | bytes[offset + 1] & 0xFF);
		offset += 2;
		index = 0;
		while (offset < val) {
			currentByte = bytes[offset++];
			if ((currentByte & 0x80) == 0) {
				charBuffer[index++] = (char) (currentByte & 0x7F);
			} else if ((currentByte & 0xE0) == 0xC0) {
				charBuffer[index++] = (char) (((currentByte & 0x1F) << 6) + (bytes[offset++] & 0x3F));
			} else {
				charBuffer[index++] = (char) (((currentByte & 0xF) << 12) + ((bytes[offset++] & 0x3F) << 6) + (bytes[offset++] & 0x3F));
			}
		}
		return new String(charBuffer, 0, index);
	}

	public static String[] dumpInterfaceNames(byte[] bytes) {
		int cnt = (bytes[8] & 0xFF) << 8 | bytes[9] & 0xFF;
		int[] cpInfoOffsets;
		int index = 1;
		int offset = 10;
		int maxStringLength = 0;
		int currentByte;
		cpInfoOffsets = new int[cnt];
		while (index < cnt) {
			cpInfoOffsets[index++] = offset + 1;
			int cpInfoSize;
			currentByte = bytes[offset];
			if (currentByte == 9 || currentByte == 10 || currentByte == 11 || currentByte == 12 || currentByte == 3 || currentByte == 4 || currentByte == 18 || currentByte == 17) {
				cpInfoSize = 5;
			} else if (currentByte == 5 || currentByte == 6) {
				cpInfoSize = 9;
				index++;
			} else if (currentByte == 1) {
				cpInfoSize = 3 + ((bytes[offset + 1] & 0xFF) << 8 | bytes[offset + 2] & 0xFF);
				if (cpInfoSize > maxStringLength) {
					maxStringLength = cpInfoSize;
				}
			} else if (currentByte == 15) {
				cpInfoSize = 4;
			} else if (currentByte == 7 || currentByte == 8 || currentByte == 16 || currentByte == 20 || currentByte == 19) {
				cpInfoSize = 3;
			} else {
				throw new IllegalArgumentException();
			}
			offset += cpInfoSize;
		}
		index = offset + 6;
		cnt = (bytes[index] & 0xFF) << 8 | bytes[index + 1] & 0xFF;
		String[] interfaces = new String[cnt];
		if (cnt > 0) {
			char[] charBuffer = new char[maxStringLength];
			for (int i = 0; i < cnt; ++i) {
				index += 2;
				offset = cpInfoOffsets[(bytes[index] & 0xFF) << 8 | bytes[index + 1] & 0xFF];
				int constantPoolEntryIndex = (bytes[offset] & 0xFF) << 8 | bytes[offset + 1] & 0xFF;
				if (offset == 0 || constantPoolEntryIndex == 0) {
					throw new ClassFormatError();
				}
				offset = cpInfoOffsets[constantPoolEntryIndex];
				int endOffset = offset + 2 + ((bytes[offset] & 0xFF) << 8 | bytes[offset + 1] & 0xFF);
				int strLength = 0;
				offset += 2;
				while (offset < endOffset) {
					currentByte = bytes[offset++];
					if ((currentByte & 0x80) == 0) {
						charBuffer[strLength++] = (char) (currentByte & 0x7F);
					} else if ((currentByte & 0xE0) == 0xC0) {
						charBuffer[strLength++] = (char) (((currentByte & 0x1F) << 6) + (bytes[offset++] & 0x3F));
					} else {
						charBuffer[strLength++] = (char) (((currentByte & 0xF) << 12) + ((bytes[offset++] & 0x3F) << 6) + (bytes[offset++] & 0x3F));
					}
				}
				interfaces[i] = new String(charBuffer, 0, strLength);
			}
		}
		return interfaces;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> void throwEx(Throwable t) throws T {
		throw (T) t;
	}

	public static String readThrowableStack(Throwable t) {
		StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}
