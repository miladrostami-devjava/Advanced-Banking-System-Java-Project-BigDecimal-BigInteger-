package org.bank.service;

import org.bank.model.Account;

import java.io.*;
import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PersistenceService {

    private final File storageFile;


    public PersistenceService(File storageFile) {
        this.storageFile = storageFile;
    }


    public void save(Map<BigInteger, Account> accountMap) throws Exception {
        File parent = storageFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(storageFile)))) {
            oos.writeObject(accountMap);
            oos.flush();
        }

    }


    @SuppressWarnings("unckecked")
    public Map<BigInteger,Account> load() throws IOException,ClassNotFoundException{
        if (!storageFile.exists()){
            return new ConcurrentHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(storageFile)))){
            Object obj = ois.readObject();
            if (obj instanceof  Map){
                return (Map<BigInteger, Account>) obj;
            }else {
                throw  new IOException( "Saved data is not the expected format (Map<BigInteger, Account>) ");
            }
        }
    }



}
