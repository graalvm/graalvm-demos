/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import com.example.preload.ModuleLocation;
import com.example.preload.PreLoadedFiles;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.nio.file.Path;
import java.util.*;

public final class JavaFileManagerImpl implements JavaFileManager {

    /**
     * Location handlers for selected standard locations as well as for all {@link ModuleLocation}.
     */
    private final Map<Location, LocationHandler> locationHandlers = new HashMap<>();

    /**
     * Stores all output files for use later.
     */
    public final Map<String, JavaFileObject> generatedClasses = new HashMap<>();

    public JavaFileManagerImpl(Path classPath, Path sourcePath, Path jdkModulePath) {
        setupLocationHandlersForJDK(jdkModulePath);

        registerLocationHandler(StandardLocation.CLASS_PATH, classPath);
        registerLocationHandler(StandardLocation.CLASS_OUTPUT, classPath);
        registerLocationHandler(StandardLocation.SOURCE_PATH, sourcePath);
    }

    private void registerLocationHandler(Location location, Path rootPath) {
        locationHandlers.put(location, new LocationHandler(location, rootPath));
    }

    private void setupLocationHandlersForJDK(Path jdkModulePath) {
        for (Set<Location> moduleLocations : listLocationsForModules(StandardLocation.SYSTEM_MODULES)) {
            for (Location location : moduleLocations) {
                ModuleLocation moduleLocation = (ModuleLocation) location;
                Path rootPath = jdkModulePath.resolve(moduleLocation.getModuleName());
                registerLocationHandler(moduleLocation, rootPath);
            }
        }
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return JavaFileManagerImpl.class.getClassLoader();
    }

    @Override
    public List<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) {
        if (locationHandlers.containsKey(location)) {
            return locationHandlers.get(location).list(location, packageName, kinds, recurse);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Extracts qualified class name from object file.
     * <p>
     * Example: From
     *
     * <pre>
     *    jfo:/SYSTEM_MODULES-java.base/java.lang.String.class
     * </pre>
     * <p>
     * we look at the the substring from the last "/" up to the extension of the JavaFileObject.Kind and extract
     *
     * <pre>
     * java.lang.String
     * </pre>
     *
     * @param location a location
     * @param file     a file object
     * @return qualified class name
     */
    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        String name = file.toUri().toString();
        return name.substring(name.lastIndexOf("/") + 1, name.length() - file.getKind().extension.length());
    }

    @Override
    public boolean isSameFile(FileObject a, FileObject b) {
        return Objects.equals(a, b);
    }

    @Override
    public boolean handleOption(String current, Iterator<String> remaining) {
        return false;
    }

    @Override
    public boolean hasLocation(Location location) {
        return locationHandlers.containsKey(location);
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) {
        if (locationHandlers.containsKey(location)) {
            return locationHandlers.get(location).getJavaFileForInput(location, className, kind);
        } else {
            return null;
        }
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        if (locationHandlers.containsKey(location)) {
            JavaFileObject javaFile = locationHandlers.get(location).getJavaFileForOutput(location, className, kind);
            generatedClasses.putIfAbsent(className, javaFile);
            return javaFile;
        } else {
            return null;
        }
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) {
        throw new UnsupportedOperationException("JavaFileManagerImpl.getFileForInput");
    }

    @Override
    public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) {
        throw new UnsupportedOperationException("JavaFileManagerImpl.getFileForOutput");
    }

    @Override
    public void flush() {
        // nothing to do
    }

    @Override
    public void close() {
        // nothing to do
    }

    @Override
    public int isSupportedOption(String option) {
        return -1;
    }

    @Override
    public Iterable<Set<Location>> listLocationsForModules(Location location) {
        return PreLoadedFiles.AVAILABLE_MODULES.getOrDefault(location, Collections.emptyList());
    }

    @Override
    public String inferModuleName(Location location) {
        return ((ModuleLocation) location).getModuleName();
    }

    @Override
    public Location getLocationForModule(Location location, JavaFileObject fo) {
        throw new UnsupportedOperationException("JavaFileManagerImpl.getLocationForModule");
    }

    @Override
    public Location getLocationForModule(Location location, String moduleName) {
        throw new UnsupportedOperationException("JavaFileManagerImpl.getLocationForModule");
    }

    public static String removeExtension(String fileName) {
        int suffixIdx = fileName.lastIndexOf('.');
        assert suffixIdx >= 0 : fileName;
        return fileName.substring(0, suffixIdx);
    }

}
