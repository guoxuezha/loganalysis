package com.gem.loganalysis.handler;
import java.io.*;

public class ConsoleFilterInterceptor extends OutputStream {
    private ByteArrayOutputStream buffer;
    private PrintStream originalStream;
    private final String encoding = "UTF-8"; // 将编码硬编码为 UTF-8

    public ConsoleFilterInterceptor() {
        buffer = new ByteArrayOutputStream();
        originalStream = System.out;
    }

    @Override
    public void write(int b) throws IOException {
        buffer.write(b);

        if (b == '\n') {
            String line = buffer.toString(encoding);
            if (!line.startsWith("renew_session response:")
                    && !line.startsWith("renewSession request:")
                    && !line.startsWith(">>>>>>>>>>>>>>>>>>> renewSession")
                    && !line.startsWith("<envelope>")) {
                originalStream.print(line);
            }
            buffer.reset();
        }
    }
}

