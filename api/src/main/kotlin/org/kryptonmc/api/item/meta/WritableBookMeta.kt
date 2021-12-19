package org.kryptonmc.api.item.meta

/**
 * Item metadata for a writable book (book and quill).
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface WritableBookMeta : BookMeta<WritableBookMeta> {

    /**
     * A builder for building writable book metadata.
     */
    public interface Builder : BookMeta.Builder<Builder, WritableBookMeta>
}
