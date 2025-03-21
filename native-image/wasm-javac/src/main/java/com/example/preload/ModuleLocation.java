/*
 * Copyright (c) 2025, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example.preload;

import java.util.Objects;

import javax.tools.JavaFileManager;

public final class ModuleLocation implements JavaFileManager.Location {
    private final String moduleName;
    private final JavaFileManager.Location rootLocation;

    public ModuleLocation(JavaFileManager.Location location, String moduleName) {
        assert location.isModuleOrientedLocation();
        this.rootLocation = location;
        this.moduleName = moduleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    @Override
    public String getName() {
        return rootLocation + "-" + moduleName;
    }

    @Override
    public boolean isOutputLocation() {
        return rootLocation.isOutputLocation();
    }

    @Override
    public boolean isModuleOrientedLocation() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName, rootLocation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModuleLocation that = (ModuleLocation) o;
        return Objects.equals(moduleName, that.moduleName) && Objects.equals(rootLocation, that.rootLocation);
    }

    @Override
    public String toString() {
        return getName();
    }
}
