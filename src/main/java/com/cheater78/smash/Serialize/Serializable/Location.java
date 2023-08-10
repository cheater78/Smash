package com.cheater78.smash.Serialize.Serializable;

import com.cheater78.smash.Utils.BukkitWorldLoader;

import java.util.Arrays;
import java.util.UUID;

public class Location implements Serializable {

    UUID uuid;

    String world;

    private double x;
    private double y;
    private double z;

    private float yaw = 0;
    private float pitch = 0;

    public Location(String world, double x, double y, double z){
        if(world == null) throw new NullPointerException("Serializable.Location: world is null");
        if(world.equals("")) throw new IllegalArgumentException("Serializable.Location: world is empty");
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.uuid = new UUID(toString().hashCode(), toString().hashCode());
    }

    public Location(String world, double x, double y, double z, float yaw, float pitch){
        if(world == null) throw new NullPointerException("Serializable.Location: world is null");
        if(world.equals("")) throw new IllegalArgumentException("Serializable.Location: world is empty");
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.uuid = new UUID(toString().hashCode(), toString().hashCode());
    }

    public Location(org.bukkit.Location loc){
        if(loc == null) throw new NullPointerException();
        if(loc.getWorld() == null) throw new NullPointerException();
        this.world = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.yaw = loc.getYaw();
        this.pitch = loc.getPitch();
        this.uuid = new UUID(toString().hashCode(), toString().hashCode());
    }

    @Override
    public UUID getUuid() { return uuid; }

    public String getWorld()               { return world;         }
    public double getX()                   { return x;             }
    public double getY()                   { return y;             }
    public double getZ()                   { return z;             }
    public float getPitch()                { return pitch;         }
    public float getYaw()                  { return yaw;           }

    public void setWorld(String world)     { this.world = world;   }
    public void setX(double x)             { this.x = x;           }
    public void setY(double y)             { this.y = y;           }
    public void setZ(double z)             { this.z = z;           }
    public void setPitch(float pitch)      { this.pitch = pitch;   }
    public void setYaw(float yaw)          { this.yaw = yaw;       }

    public int getBlockX()                 { return (int)x;        }
    public int getBlockY()                 { return (int)y;        }
    public int getBlockZ()                 { return (int)z;        }


    public org.bukkit.Location toBukkitLocation(){
        return new org.bukkit.Location(BukkitWorldLoader.getWorld(this.world), x, y, z, yaw, pitch);
    }

    public boolean isInRegion(Location loc1, Location loc2){
        double[] dim = new double[2];

        dim[0] = loc1.getX();
        dim[1] = loc2.getX();
        Arrays.sort(dim);
        if(this.getX() > dim[1] || this.getX() < dim[0])
            return false;

        dim[0] = loc1.getZ();
        dim[1] = loc2.getZ();
        Arrays.sort(dim);
        if(this.getZ() > dim[1] || this.getZ() < dim[0])
            return false;

        dim[0] = loc1.getY();
        dim[1] = loc2.getY();
        Arrays.sort(dim);
        if(this.getY() > dim[1] || this.getY() < dim[0])
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "Location{" +
                "world='" + world + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                '}';
    }
}
