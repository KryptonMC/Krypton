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

    public companion object {

        /**
         * Creates a new builder for building writable book metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun builder(): Builder = ItemMeta.FACTORY.builder(WritableBookMeta::class.java)
    }
}
