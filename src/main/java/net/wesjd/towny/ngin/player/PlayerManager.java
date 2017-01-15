package net.wesjd.towny.ngin.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.wesjd.towny.ngin.storage.StorageFolder;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Where {@link Player}s are mapped to their {@link TownyPlayer} wrapper
 */
public class PlayerManager {

    /**
     * The injected {@link StorageFolder} for players
     */
    @Inject @Named("players")
    private StorageFolder _storage;

    /**
     * {@link UUID} to {@link TownyPlayer} store
     */
    private final Map<UUID, TownyPlayer> store = new HashMap<>();

    /**
     * Creates a new {@link OfflineTownyPlayer}
     *
     * @param uuid The uuid to create the player from
     * @return The created {@link OfflineTownyPlayer}
     */
    public OfflineTownyPlayer createOfflineTownyPlayer(UUID uuid) {
        return new OfflineTownyPlayer(_storage, uuid);
    }

    /**
     * Creates a wrapper for a player
     *
     * @param player The {@link Player} to create a wrapper for
     */
    public TownyPlayer initializePlayer(Player player, OfflineTownyPlayer offline) {
        Validate.isTrue(!store.containsKey(offline.getUuid()));
        return store.put(offline.getUuid(), new TownyPlayer(player, _storage, offline));
    }

    /**
     * Gets an online player's {@link TownyPlayer} wrapper
     *
     * @param player The {@link Player} to get the wrapper for
     * @return The wrapper
     */
    public TownyPlayer getPlayer(Player player) {
        return store.get(player.getUniqueId());
    }

    /**
     * Removes a player from the server
     *
     * @param player The {@link Player} to remove
     */
    public void removePlayer(Player player) {
        store.remove(player.getUniqueId()).save();
    }

}