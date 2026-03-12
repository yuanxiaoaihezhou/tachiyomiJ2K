package eu.kanade.tachiyomi.ui.reader.model

/**
 * A special blank (white) page inserted between content pages when using e-ink
 * "insert blank pages" refresh mode. This page is used to clear ghosting on
 * e-ink displays by displaying a fully white page between every two content pages.
 */
class EInkRefreshPage(
    parent: ReaderPage,
) : ReaderPage(
        parent.index,
        parent.url,
        parent.imageUrl,
    ) {
    override var chapter: ReaderChapter = parent.chapter

    init {
        fullPage = true
        firstHalf = null
        stream = null
        // Set status to READY immediately since this page has no image to load.
        // PagerPageHolder checks this status to skip the image loading pipeline
        // and simply displays a white background.
        status = State.READY
    }
}
