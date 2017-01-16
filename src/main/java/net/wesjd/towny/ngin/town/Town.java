package net.wesjd.towny.ngin.town;

import net.wesjd.towny.ngin.storage.Data;
import net.wesjd.towny.ngin.storage.StorageFolder;
import net.wesjd.towny.ngin.town.ranks.DefaultRank;
import net.wesjd.towny.ngin.town.ranks.OwnerRank;
import net.wesjd.towny.ngin.util.Region;
import org.bukkit.Location;

import java.util.*;

/**
 * Represents a town
 */
public class Town {

    /**
     * An instance of the folder containing all towns
     */
    private final StorageFolder _storage;

    /**
     * The spawn location for the town
     */
    @Data
    private Location _spawnLocation;

    /**
     * The region specifying the perimeter of the town
     */
    @Data
    private Region _region;

    /**
     * The current balance of the town
     */
    @Data
    private double _money;

    /**
     * The warps the town has
     */
    @Data
    private Map<String, Location> _warps = new HashMap<>();

    /**
     * The map of player to rank
     */
    @Data
    private Map<UUID, String> _playerRanks = new HashMap<>();

    /**
     * The name of the town
     */
    @Data
    private String _townName;

    /**
     * The ranks that the town has
     */
    @Data
    private List<TownRank> _ranks = new ArrayList<>();

    /**
     * Creates a new town with the specified name and folder it's located in
     *
     * @param name The name of the town
     * @param storage The folder the town is stored in
     */
    Town(String name, StorageFolder storage) {
        _townName = name;
        _storage = storage;
    }

    public Location getSpawnLocation() {
        return _spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        _spawnLocation = spawnLocation;
    }

    public Region getRegion() {
        return _region;
    }

    public void setRegion(Region region) {
        _region = region;
    }

    public double getMoney() {
        return _money;
    }

    public void setMoney(double money) {
        _money = money;
    }

    public Map<String, Location> getWarps() {
        return _warps;
    }

    public String getTownName() {
        return _townName;
    }

    public void setTownName(String townName) {
        _townName = townName;
    }

    /**
     * Saves the town to the file
     */
    public void save() {
        _storage.packup(_townName, this);
    }

    /**
     * Loads the town from the file and sets the fields
     */
    public void load() {
        _storage.unbox(_townName, this);
    }

    /**
     * Generates the generic set of ranks all towns have
     */
    public void generateDefaultRanks() {
        _ranks.add(new OwnerRank("mayor", "Mayor"));
        _ranks.add(new DefaultRank("member", "Town Member", Collections.emptyList()));
    }

    /**
     *  Gets a rank by its name
     *
     * @param name The name of the rank
     * @return An {@link Optional<TownRank>} empty if no rank found
     */
    public Optional<TownRank> getRankByName(String name) {
        return _ranks.stream().filter(r -> r.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Town town = (Town) o;

        return _townName.equalsIgnoreCase(town._townName);
    }

    @Override
    public int hashCode() {
        return _townName.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Town{");
        sb.append("_spawnLocation=").append(_spawnLocation);
        sb.append(", _region=").append(_region);
        sb.append(", _money=").append(_money);
        sb.append(", _warps=").append(_warps);
        sb.append(", _townName='").append(_townName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
