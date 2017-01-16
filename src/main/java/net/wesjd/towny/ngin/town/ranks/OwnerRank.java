package net.wesjd.towny.ngin.town.ranks;

import org.bukkit.permissions.Permission;

import java.util.Collections;

/**
 * An non-removable rank that has all permissions for towns
 */
public class OwnerRank extends DefaultRank {

    /**
     * Creates a new owner rank
     *
     * @param internalName        The internal name
     * @param displayName     The name players will see
     */
    public OwnerRank(String internalName, String displayName) {
        super(internalName, displayName, Collections.singletonList(new Permission("towny.owner")));
    }
}
