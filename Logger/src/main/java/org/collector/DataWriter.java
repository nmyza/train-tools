package org.collector;

import java.io.FileWriter;
import java.io.IOException;

class DataWriter {
    void writeToFile(String line) throws IOException{
        FileWriter sw = new FileWriter(Const.LOG_FILE_NAME, true);
        sw.write(line + Const.NEW_LINE);
        sw.close();
    }
}
