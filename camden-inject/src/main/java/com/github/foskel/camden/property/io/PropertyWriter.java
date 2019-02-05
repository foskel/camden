package com.github.foskel.camden.property.io;

import java.io.IOException;
import java.nio.file.Path;

public interface PropertyWriter {
    void write(Object container, Path file) throws IOException;

    String getFileExtension();
}
