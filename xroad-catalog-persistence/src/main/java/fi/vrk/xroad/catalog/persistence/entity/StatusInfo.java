/**
 * The MIT License
 * Copyright (c) 2022, Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vrk.xroad.catalog.persistence.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * Embeddable to model the status / timestamp fields that are repeated in all the tables
 */
@Embeddable
@Getter
@Setter
public class StatusInfo {

    @Column(nullable = false)
    private LocalDateTime created;

    /**
     * When data was changed somehow, whether it was updated, created or removed
     */
    @Column(nullable = false)
    private LocalDateTime changed;

    /**
     * When data was last time fetched from the source (regardless of whether there
     * were any changes). For removed entities, fetched it updated the first time
     * we receive "missing item" from data, but not after that
     */
    @Column(nullable = false)
    private LocalDateTime fetched;

    @Column(nullable = true)
    private LocalDateTime removed;

    /**
     * Constructor based on timestamps
     *
     */
    public StatusInfo(LocalDateTime created, LocalDateTime changed, LocalDateTime fetched, LocalDateTime removed) {
        this.created = created;
        this.changed = changed;
        this.fetched = fetched;
        this.removed = removed;
    }

    public StatusInfo() {
        // Empty contructor
    }

    /**
     * Set timestamps for item that is saved and is not removed.
     * The data can either be modified from previous values (isModified)
     * or identical. If items was previously marked removed, it no longer is.
     * Changed-timestamp is updated for modified and un-removed items.
     *
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


}
