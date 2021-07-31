package de.pierreschwang.levelhead.api.data;

import com.google.gson.annotations.JsonAdapter;
import de.pierreschwang.levelhead.api.HypixelConstants;
import de.pierreschwang.levelhead.api.adapter.UuidJsonAdapter;

import java.util.UUID;

public class HypixelPlayer {

    @JsonAdapter(UuidJsonAdapter.class)
    private UUID uuid;
    private String displayname;
    private String rank;
    private String packageRank;
    private String newPackageRank;
    private String monthlyPackageRank;
    private long firstLogin;
    private long lastLogin;
    private long lastLogout;
    private long networkExp;

    public UUID getUuid() {
        return uuid;
    }

    public String getDisplayName() {
        return displayname;
    }

    public String getRank() {
        return rank;
    }

    public String getPackageRank() {
        return packageRank;
    }

    public String getNewPackageRank() {
        return newPackageRank;
    }

    public String getMonthlyPackageRank() {
        return monthlyPackageRank;
    }

    public long getFirstLogin() {
        return firstLogin;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public long getLastLogout() {
        return lastLogout;
    }

    public long getNetworkExp() {
        return networkExp;
    }

    public int getNetworkLevel() {
        return (int) HypixelConstants.getLevel(getNetworkExp());
    }

}
