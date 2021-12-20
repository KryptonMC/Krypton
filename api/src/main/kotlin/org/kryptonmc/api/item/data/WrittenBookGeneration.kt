package org.kryptonmc.api.item.data

/**
 * A generation that a written book is in. This is used to describe how many
 * times the written book has been copied.
 */
public enum class WrittenBookGeneration {

    /**
     * An original copy. Books in this generation have not been copied from
     * other sources, and are from the source.
     */
    ORIGINAL,

    /**
     * The first copy of a book. Also known as "second-hand".
     */
    COPY_OF_ORIGINAL,

    /**
     * The second copy of a book. This is the last copy that can be made of a
     * book.
     */
    COPY_OF_COPY,

    /**
     * Unused in vanilla, and functions the same as [COPY_OF_COPY].
     */
    TATTERED
}
