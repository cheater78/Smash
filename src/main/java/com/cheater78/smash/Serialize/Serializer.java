package com.cheater78.smash.Serialize;

import com.cheater78.smash.Serialize.Serializable.Serializable;
import com.cheater78.smash.Smash;
import com.google.gson.Gson;
import org.bukkit.Bukkit;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Serializer <T extends Serializable> {

    private final Gson gson;
    private File file;

    class Container{
        public List<T> content = new ArrayList<>();
        public Container() {}
    }

    public Serializer(String filename){
        gson = new Gson();
        initFile(new File(Smash.plugin.getDataFolder().getAbsolutePath() + filename));
    }

    public boolean save(T obj){
        Container all = gson.fromJson(getFile(), Container.class);
        for (T saved : all.content){
            if(saved.getUuid() == obj.getUuid()){
                all.content.set(all.content.indexOf(saved),obj);
                String writeBack = gson.toJson(all);
                if(file.canWrite())
                    try{ Files.write(file.toPath(), writeBack.getBytes()); }
                    catch (Exception e) {
                        Bukkit.getLogger().severe("Serialization File could not be written(save " + file.getName() + ")[emplace]");
                        return false;
                    }
                return true;
            }
        }
        all.content.add(obj);
        String writeBack = gson.toJson(all);
        if(file.canWrite())
            try{ Files.write(file.toPath(), writeBack.getBytes()); }
            catch (Exception e) {
                Bukkit.getLogger().severe("Serialization File could not be written(save " + file.getName() + ")[add]");
                return false;
            }
        return true;
    }

    public T load(UUID id){
        Container all = gson.fromJson(getFile(), Container.class);
        for (T saved : all.content){
            if(saved.getUuid() == id){
                return saved;
            }
        }
        return null;
    }

    public List<T> loadAll(){
        String json= getFile();
        Container all = gson.fromJson(json, Container.class);
        if(all == null || all.content == null)
            return new ArrayList<>();
        return all.content;
    }

    public boolean remove(T obj){
        boolean contains = false;
        Container all = gson.fromJson(getFile(), Container.class);
        Container withoutObj = new Container();
        for (T saved : all.content){
            if(saved.getUuid() != obj.getUuid()){
                withoutObj.content.add(saved);
            }else contains = true;
        }
        String writeBack = gson.toJson(withoutObj);
        if(file.canWrite())
            try{ Files.write(file.toPath(), writeBack.getBytes()); }
            catch (Exception e) {
                Bukkit.getLogger().severe("Serialization File could not be written(save " + file.getName() + ")[add]");
                return false;
            }
        return contains;
    }

    private String getFile(){
        try {
            return Files.readString(file.toPath());
        } catch (Exception e){
            if(initFile(file)){
                try{
                    return Files.readString(file.toPath());
                }catch (Exception ee){
                    Bukkit.getLogger().severe("Serialization File could not be accessed(save " + file.getName() + ")");
                    return "";
                }
            }else{
                Bukkit.getLogger().severe("Serialization File could not be accessed(save " + file.getName() + ")");
                return "";
            }
        }
    }

    private boolean initFile(File file){
        this.file = new File(file.getAbsolutePath());
        this.file.mkdirs();
        if(!this.file.exists())
            try {
                this.file.createNewFile();
            }catch (Exception e){
                Bukkit.getLogger().severe("Serialization File could not be created!(" + file.getName() + ")");
                return false;
            }
        return true;
    }
}
