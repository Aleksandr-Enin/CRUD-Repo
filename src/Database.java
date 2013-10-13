import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database implements Serializable {
    private HashMap<String, Long> keyOffsetMap;
    private String keyOffsetMapFileName;
    private String currentBaseName;
    private String previousBaseName;
    private RandomAccessFile base;
    private long currentSize;

    Database(String currentBaseName, String previousBaseName, String keyOffsetMapFileName) throws Exception {
        this.currentBaseName = currentBaseName;
        this.previousBaseName = previousBaseName;
        this.keyOffsetMapFileName = keyOffsetMapFileName;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(keyOffsetMapFileName));
            keyOffsetMap = (HashMap<String, Long>) ois.readObject();
        } catch (Exception ex) {
            keyOffsetMap = new HashMap<>();
        }
        base = new RandomAccessFile(currentBaseName, "rws");
        base.seek(0);
    }

    String add(String key, String value) {
        if (keyOffsetMap.containsKey(key)) {
            return "Key already exists";
        }
        try {
            base.seek(base.length());
            keyOffsetMap.put(key, base.length());
            base.writeUTF(key + "\n" + value);
            return "Value added";
        } catch (Exception ex) {
            return "Exception " + ex.toString();
        }

    }

    String read(String key) {
        if (!keyOffsetMap.containsKey(key)) {
            return "Key doesn't exist";
        }
        try {
            base.seek(keyOffsetMap.get(key));
            return base.readUTF().split("\n")[1];
        } catch (Exception ex) {
            return "Exception " + ex.toString();
        }
    }

    String update(String key, String value) {
        if (delete(key).equals("Successfully deleted")) {
            return add(key, value);
        } else {
            return "Failure";
        }
    }

    String delete(String key) {
        if (keyOffsetMap.remove(key) == null) {
            return "Key doesn't exist";
        } else {
            return "Successfully deleted";
        }
    }

    /*    void exit() throws Exception {
            RandomAccessFile newBase = new RandomAccessFile(previousBaseName, "rws");
            HashMap<String, Pair<Long,Long>> newKeyOffsetMap = new HashMap<>();
            base.seek(0);
            newBase.writeLong(base.readLong());
            String key;
            String value;
            long length;
            Long offset = base.getFilePointer();
            Long newOffset = newBase.getFilePointer();
            while (offset < currentSize) {
                key = base.readLine();
                value = base.readLine();
                if (keyOffsetMap.containsKey(key) && keyOffsetMap.get(key).getKey() == offset) {
                    length = keyOffsetMap.get(key).getValue() - keyOffsetMap.get(key).getKey();
                    newBase.writeChars(key+System.getProperty("line.separator")+value);
                    newKeyOffsetMap.put(key, new Pair<Long, Long>(newOffset, newOffset + length));
                    newOffset += length;
                }
            }
            newBase.seek(0);
            newBase.writeLong(newOffset);
            base.close();
            newBase.close();
            ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(keyOffsetMapFileName));
            ois.writeObject(newKeyOffsetMap);
            ois.close();
        }
    */
    void exit() throws Exception {
        RandomAccessFile newBase = new RandomAccessFile(previousBaseName, "rws");
        HashMap<String, Long> newKeyOffsetMap = new HashMap<>();

        long offset;
        String data;
        String value;
        String key;

        newBase.seek(0);
        base.seek(0);

        while (base.getFilePointer() < base.length())
        {
            offset = base.getFilePointer();
            data = base.readUTF();
            key = data.split("\n")[0];
            if (keyOffsetMap.get(key) == offset) {
                newKeyOffsetMap.put(key, newBase.length());
                newBase.writeUTF(data);
            }
            //newBase.seek(newBase.length());
        }
        newBase.setLength(newBase.getFilePointer()+1);

        ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(keyOffsetMapFileName));
        ois.writeObject(newKeyOffsetMap);
    }
}

