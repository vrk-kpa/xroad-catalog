package fi.vrk.xroad.catalog.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Embeddable to model the status / timestamp fields that are repeated in all the tables
 */
@Embeddable
@Getter
@Setter
public class StatusInfo {

    private LocalDateTime created;
    /**
     * When data was changed somehow, whether it was updated, created or removed
     */
    private LocalDateTime changed;
    /**
     * When data was last time fetched from the source (regardless of whether there
     * were any changes). For removed entities, fetched it updated the first time
     * we receive "missing item" from data, but not after that
     */
    private LocalDateTime fetched;
    private LocalDateTime removed;

    /**
     * Set timestamps for item that is saved and is not removed.
     * The data can either be modified from previous values (isModified)
     * or identical. If items was previously marked removed, it no longer is.
     * Changed-timestamp is updated for modified and un-removed items.
     * @param timestamp
     * @param isModified
     */
    public void setTimestampsForSaved(LocalDateTime timestamp, boolean isModified) {
        if (removed != null) {
            changed = timestamp;
        }
        if (isModified) {
            changed = timestamp;
        }
        removed = null;
        fetched = timestamp;
    }

    public boolean isRemoved() {
        return removed != null;
    }

    /**
     * Sets timestamps to correct values when an item is fetched, but not changed
     */
    public void setTimestampsForFetched(LocalDateTime timestamp) {
        if (isRemoved()) {
            // resurrect this item
            removed = null;
            changed = timestamp;
            fetched = timestamp;
        } else {
            // already existing item, no fields to update except timestamp
            fetched = timestamp;
        }
    }

    /**
     * Sets timestamps to correct values for new instance
     */
    public void setTimestampsForNew(LocalDateTime timestamp) {
        created = timestamp;
        changed = timestamp;
        fetched = timestamp;
        removed = null;
    }

    /**
     * Sets timestamps to correct values for removed instance (which was not already removed)
     */
    public void setTimestampsForRemoved(LocalDateTime timestamp) {
        changed = timestamp;
        removed = timestamp;
        fetched = timestamp;
    }

    public StatusInfo(LocalDateTime created, LocalDateTime changed, LocalDateTime fetched, LocalDateTime removed) {
        this.created = created;
        this.changed = changed;
        this.fetched = fetched;
        this.removed = removed;
    }
    public StatusInfo() {
    }
}
