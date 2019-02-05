package com.github.foskel.camden.property.io;

import java.io.IOException;
import java.nio.file.Path;

public interface PropertyReader {
    void read(Object container, Path source) throws IOException;

    String getFileExtension();
}
