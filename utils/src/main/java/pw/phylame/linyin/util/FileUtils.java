/*
 * This file is part of Linyin, Peng Wan's graduation project in Haut.
 * Copyright (C) 2016 Peng Wan <phylame@163.com>. All Rights Reserved.
 */

package pw.phylame.linyin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {
    /**
     * Get the MD5 value of file.
     *
     * @param file to compute
     * @return MD5 value
     */
    public static String md5OfFile(File file) throws IOException {
        try (FileInputStream fin = new FileInputStream(file)) {
            MappedByteBuffer byteBuffer = fin.getChannel().map(FileChannel.MapMode.READ_ONLY, 0,
                    file.length());
            return StringUtils.md5OfBuffer(byteBuffer);
        }
    }

}
