package eu.kanade.tachiyomi.data.preference

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
import com.google.android.material.color.DynamicColors
import eu.kanade.tachiyomi.BuildConfig
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.database.models.Manga
import eu.kanade.tachiyomi.data.updater.AppDownloadInstallJob
import eu.kanade.tachiyomi.extension.model.InstalledExtensionsOrder
import eu.kanade.tachiyomi.extension.util.ExtensionInstaller
import eu.kanade.tachiyomi.network.NetworkHelper
import eu.kanade.tachiyomi.ui.library.LibraryItem
import eu.kanade.tachiyomi.ui.library.filter.FilterBottomSheet
import eu.kanade.tachiyomi.ui.reader.settings.OrientationType
import eu.kanade.tachiyomi.ui.reader.settings.PageLayout
import eu.kanade.tachiyomi.ui.reader.settings.ReaderBottomButton
import eu.kanade.tachiyomi.ui.reader.settings.ReadingModeType
import eu.kanade.tachiyomi.ui.reader.viewer.ViewerNavigation
import eu.kanade.tachiyomi.ui.recents.RecentMangaAdapter
import eu.kanade.tachiyomi.ui.recents.RecentsPresenter
import eu.kanade.tachiyomi.util.system.Themes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import eu.kanade.tachiyomi.data.preference.PreferenceKeys as Keys
import eu.kanade.tachiyomi.data.preference.PreferenceValues as Values

fun <T> Preference<T>.asImmediateFlow(block: (value: T) -> Unit): Flow<T> {
    block(get())
    return asFlow()
        .onEach { block(it) }
}

fun <T> Preference<T>.asImmediateFlowIn(
    scope: CoroutineScope,
    block: (value: T) -> Unit,
): Job {
    block(get())
    return asFlow()
        .onEach { block(it) }
        .launchIn(scope)
}

fun Preference<Boolean>.toggle() = set(!get())

operator fun <T> Preference<Set<T>>.plusAssign(item: T) {
    set(get() + item)
}

operator fun <T> Preference<Set<T>>.minusAssign(item: T) {
    set(get() - item)
}

operator fun <T> Preference<Set<T>>.plusAssign(item: Collection<T>) {
    set(get() + item)
}

operator fun <T> Preference<Set<T>>.minusAssign(item: Collection<T>) {
    set(get() - item)
}

class PreferencesHelper(
    val context: Context,
) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val flowPrefs = FlowSharedPreferences(prefs)

    private val defaultDownloadsDir =
        Uri.fromFile(
            File(
                Environment.getExternalStorageDirectory().absolutePath + File.separator +
                    context.getString(R.string.app_name),
                "downloads",
            ),
        )

    private val defaultBackupDir =
        Uri.fromFile(
            File(
                Environment.getExternalStorageDirectory().absolutePath + File.separator +
                    context.getString(R.string.app_name),
                "backup",
            ),
        )

    fun getInt(
        key: String,
        default: Int,
    ) = flowPrefs.getInt(key, default)

    fun getStringPref(
        key: String,
        default: String = "",
    ) = flowPrefs.getString(key, default)

    fun getStringSet(
        key: String,
        default: Set<String>,
    ) = flowPrefs.getStringSet(key, default)

    fun startingTab() = flowPrefs.getInt(Keys.STARTING_TAB, 0)

    fun backReturnsToStart() = flowPrefs.getBoolean(Keys.BACK_TO_START, true)

    fun hasShownNotifPermission() = flowPrefs.getBoolean("has_shown_notification_permission", false)

    fun hasDeniedA11FilePermission() = flowPrefs.getBoolean(Keys.DENIED_A11_FILE_PERMISSION, false)

    fun clear() = prefs.edit().clear().apply()

    fun nightMode() = flowPrefs.getInt(Keys.NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    fun themeDarkAmoled() = flowPrefs.getBoolean(Keys.THEME_DARK_AMOLED, false)

    private val supportsDynamic = DynamicColors.isDynamicColorAvailable()

    fun lightTheme() = flowPrefs.getEnum(Keys.LIGHT_THEME, if (supportsDynamic) Themes.MONET else Themes.DEFAULT)

    fun darkTheme() = flowPrefs.getEnum(Keys.DARK_THEME, if (supportsDynamic) Themes.MONET else Themes.DEFAULT)

    fun pageTransitions() = flowPrefs.getBoolean(Keys.ENABLE_TRANSITIONS, true)

    fun pagerCutoutBehavior() = flowPrefs.getInt(Keys.PAGER_CUTOUT_BEHAVIOR, 0)

    fun landscapeCutoutBehavior() = flowPrefs.getInt("landscape_cutout_behavior", 0)

    fun doubleTapAnimSpeed() = flowPrefs.getInt(Keys.DOUBLE_TAP_ANIMATION_SPEED, 500)

    fun showPageNumber() = flowPrefs.getBoolean(Keys.SHOW_PAGE_NUMBER, true)

    fun trueColor() = flowPrefs.getBoolean(Keys.TRUE_COLOR, false)

    fun fullscreen() = flowPrefs.getBoolean(Keys.FULLSCREEN, true)

    fun keepScreenOn() = flowPrefs.getBoolean(Keys.KEEP_SCREEN_ON, true)

    fun customBrightness() = flowPrefs.getBoolean(Keys.CUSTOM_BRIGHTNESS, false)

    fun customBrightnessValue() = flowPrefs.getInt(Keys.CUSTOM_BRIGHTNESS_VALUE, 0)

    fun colorFilter() = flowPrefs.getBoolean(Keys.COLOR_FILTER, false)

    fun colorFilterValue() = flowPrefs.getInt(Keys.COLOR_FILTER_VALUE, 0)

    fun colorFilterMode() = flowPrefs.getInt(Keys.COLOR_FILTER_MODE, 0)

    fun defaultReadingMode() = prefs.getInt(Keys.DEFAULT_READING_MODE, ReadingModeType.RIGHT_TO_LEFT.flagValue)

    fun defaultOrientationType() = flowPrefs.getInt(Keys.DEFAULT_ORIENTATION_TYPE, OrientationType.FREE.flagValue)

    fun imageScaleType() = flowPrefs.getInt(Keys.IMAGE_SCALE_TYPE, 1)

    fun zoomStart() = flowPrefs.getInt(Keys.ZOOM_START, 1)

    fun readerTheme() = flowPrefs.getInt(Keys.READER_THEME, 2)

    fun cropBorders() = flowPrefs.getBoolean(Keys.CROP_BORDERS, false)

    fun cropBordersWebtoon() = flowPrefs.getBoolean(Keys.CROP_BORDERS_WEBTOON, false)

    fun navigateToPan() = flowPrefs.getBoolean("navigate_pan", true)

    fun landscapeZoom() = flowPrefs.getBoolean("landscape_zoom", false)

    fun grayscale() = flowPrefs.getBoolean("pref_grayscale", false)

    fun invertedColors() = flowPrefs.getBoolean("pref_inverted_colors", false)

    fun webtoonSidePadding() = flowPrefs.getInt(Keys.WEBTOON_SIDE_PADDING, 0)

    fun webtoonEnableZoomOut() = flowPrefs.getBoolean(Keys.WEBTOON_ENABLE_ZOOM_OUT, false)

    fun einkMode() = flowPrefs.getBoolean(Keys.EINK_MODE, false)

    fun einkRefreshMode() = flowPrefs.getInt(Keys.EINK_REFRESH_MODE, 0)

    fun readWithLongTap() = flowPrefs.getBoolean(Keys.READ_WITH_LONG_TAP, true)

    fun readWithVolumeKeys() = flowPrefs.getBoolean(Keys.READ_WITH_VOLUME_KEYS, false)

    fun readWithVolumeKeysInverted() = flowPrefs.getBoolean(Keys.READ_WITH_VOLUME_KEYS_INVERTED, false)

    fun navigationModePager() = flowPrefs.getInt(Keys.NAVIGATION_MODE_PAGER, 0)

    fun navigationModeWebtoon() = flowPrefs.getInt(Keys.NAVIGATION_MODE_WEBTOON, 0)

    fun pagerNavInverted() = flowPrefs.getEnum(Keys.PAGER_NAV_INVERTED, ViewerNavigation.TappingInvertMode.NONE)

    fun webtoonNavInverted() = flowPrefs.getEnum(Keys.WEBTOON_NAV_INVERTED, ViewerNavigation.TappingInvertMode.NONE)

    fun pageLayout() = flowPrefs.getInt(Keys.PAGE_LAYOUT, PageLayout.AUTOMATIC.value)

    fun automaticSplitsPage() = flowPrefs.getBoolean(Keys.AUTOMATIC_SPLITS_PAGE, false)

    fun invertDoublePages() = flowPrefs.getBoolean(Keys.INVERT_DOUBLE_PAGES, false)

    fun webtoonPageLayout() = flowPrefs.getInt(Keys.WEBTOON_PAGE_LAYOUT, PageLayout.SINGLE_PAGE.value)

    fun webtoonReaderHideThreshold() = flowPrefs.getEnum("reader_hide_threshold", Values.ReaderHideThreshold.LOW)

    fun webtoonInvertDoublePages() = flowPrefs.getBoolean(Keys.WEBTOON_INVERT_DOUBLE_PAGES, false)

    fun readerBottomButtons() =
        flowPrefs.getStringSet(
            Keys.READER_BOTTOM_BUTTONS,
            ReaderBottomButton.BUTTONS_DEFAULTS,
        )

    fun showNavigationOverlayNewUser() = flowPrefs.getBoolean(Keys.SHOW_NAVIGATION_OVERLAY_NEW_USER, true)

    fun showNavigationOverlayNewUserWebtoon() = flowPrefs.getBoolean(Keys.SHOW_NAVIGATION_OVERLAY_NEW_USER_WEBTOON, true)

    fun preloadSize() = flowPrefs.getInt(Keys.PRELOAD_SIZE, 6)

    fun autoUpdateTrack() = prefs.getBoolean(Keys.AUTO_UPDATE_TRACK, true)

    fun trackMarkedAsRead() = prefs.getBoolean(Keys.TRACK_MARKED_AS_READ, false)

    fun trackingsToAddOnline() = flowPrefs.getStringSet(Keys.TRACKINGS_TO_ADD_ONLINE, emptySet())

    fun lastUsedCatalogueSource() = flowPrefs.getLong(Keys.LAST_USED_CATALOGUE_SOURCE, -1)

    fun lastUsedCategory() = flowPrefs.getInt(Keys.LAST_USED_CATEGORY, 0)

    fun lastUsedSources() = flowPrefs.getStringSet("last_used_sources", emptySet())

    fun lastVersionCode() = flowPrefs.getInt("last_version_code", 0)

    fun browseAsList() = flowPrefs.getBoolean(Keys.CATALOGUE_AS_LIST, false)

    fun enabledLanguages() =
        flowPrefs.getStringSet(
            Keys.ENABLED_LANGUAGES,
            setOfNotNull("all", "en", Locale.getDefault().language.takeIf { !it.startsWith("en") }),
        )

    fun sourceSorting() = flowPrefs.getInt(Keys.SOURCES_SORT, 0)

    fun anilistScoreType() = flowPrefs.getString("anilist_score_type", "POINT_10")

    fun backupsDirectory() = flowPrefs.getString(Keys.BACKUP_DIRECTORY, defaultBackupDir.toString())

    fun dateFormat(format: String = flowPrefs.getString(Keys.DATE_FORMAT, "").get()): DateFormat =
        when (format) {
            "" -> DateFormat.getDateInstance(DateFormat.SHORT)
            else -> SimpleDateFormat(format, Locale.getDefault())
        }

    fun appLanguage() = flowPrefs.getString("app_language", "")

    fun downloadsDirectory() = flowPrefs.getString(Keys.DOWNLOADS_DIRECTORY, defaultDownloadsDir.toString())

    fun downloadOnlyOverWifi() = prefs.getBoolean(Keys.DOWNLOAD_ONLY_OVER_WIFI, true)

    fun folderPerManga() = flowPrefs.getBoolean("create_folder_per_manga", false)

    fun librarySearchSuggestion() = flowPrefs.getString(Keys.LIBRARY_SEARCH_SUGGESTION, "")

    fun showLibrarySearchSuggestions() = flowPrefs.getBoolean(Keys.SHOW_LIBRARY_SEARCH_SUGGESTIONS, false)

    fun lastLibrarySuggestion() = flowPrefs.getLong("last_library_suggestion", 0L)

    fun numberOfBackups() = flowPrefs.getInt(Keys.NUMBER_OF_BACKUPS, 2)

    fun backupInterval() = flowPrefs.getInt(Keys.BACKUP_INTERVAL, 0)

    fun removeAfterReadSlots() = flowPrefs.getInt(Keys.REMOVE_AFTER_READ_SLOTS, -1)

    fun removeAfterMarkedAsRead() = prefs.getBoolean(Keys.REMOVE_AFTER_MARKED_AS_READ, false)

    fun libraryUpdateInterval() = flowPrefs.getInt(Keys.LIBRARY_UPDATE_INTERVAL, 24)

    fun libraryUpdateLastTimestamp() = flowPrefs.getLong("library_update_last_timestamp", 0L)

    fun libraryUpdateDeviceRestriction() = flowPrefs.getStringSet("library_update_restriction", setOf(DEVICE_ONLY_ON_WIFI))

    fun libraryUpdateMangaRestriction() = flowPrefs.getStringSet("library_update_manga_restriction", setOf(MANGA_HAS_UNREAD, MANGA_NON_COMPLETED, MANGA_NON_READ))

    fun libraryUpdateCategories() = flowPrefs.getStringSet("library_update_categories", emptySet())

    fun libraryUpdateCategoriesExclude() = flowPrefs.getStringSet("library_update_categories_exclude", emptySet())

    fun libraryLayout() = flowPrefs.getInt(Keys.LIBRARY_LAYOUT, LibraryItem.LAYOUT_COMFORTABLE_GRID)

    fun gridSize() = flowPrefs.getFloat(Keys.GRID_SIZE, 1f)

    fun uniformGrid() = flowPrefs.getBoolean(Keys.UNIFORM_GRID, true)

    fun outlineOnCovers() = flowPrefs.getBoolean(Keys.OUTLINE_ON_COVERS, true)

    fun downloadBadge() = flowPrefs.getBoolean(Keys.DOWNLOAD_BADGE, false)

    fun languageBadge() = flowPrefs.getBoolean(Keys.LANGUAGE_BADGE, false)

    fun filterDownloaded() = flowPrefs.getInt(Keys.FILTER_DOWNLOADED, 0)

    fun filterUnread() = flowPrefs.getInt(Keys.FILTER_UNREAD, 0)

    fun filterCompleted() = flowPrefs.getInt(Keys.FILTER_COMPLETED, 0)

    fun filterBookmarked() = flowPrefs.getInt("pref_filter_bookmarked_key", 0)

    fun filterTracked() = flowPrefs.getInt(Keys.FILTER_TRACKED, 0)

    fun filterMangaType() = flowPrefs.getInt(Keys.FILTER_MANGA_TYPE, 0)

    fun showEmptyCategoriesWhileFiltering() = flowPrefs.getBoolean(Keys.SHOW_EMPTY_CATEGORIES_FILTERING, false)

    fun librarySortingMode() = flowPrefs.getInt("library_sorting_mode", 0)

    fun librarySortingAscending() = flowPrefs.getBoolean("library_sorting_ascending", true)

    fun automaticExtUpdates() = flowPrefs.getBoolean(Keys.AUTOMATIC_EXT_UPDATES, true)

    fun extensionRepos() = flowPrefs.getStringSet("extension_repos", emptySet())

    fun installedExtensionsOrder() = flowPrefs.getInt(Keys.INSTALLED_EXTENSIONS_ORDER, InstalledExtensionsOrder.Name.value)

    fun migrationSourceOrder() = flowPrefs.getInt("migration_source_order", Values.MigrationSourceOrder.Alphabetically.value)

    fun collapsedCategories() = flowPrefs.getStringSet("collapsed_categories", mutableSetOf())

    fun collapsedDynamicCategories() = flowPrefs.getStringSet("collapsed_dynamic_categories", mutableSetOf())

    fun collapsedDynamicAtBottom() = flowPrefs.getBoolean("collapsed_dynamic_at_bottom", false)

    fun hiddenSources() = flowPrefs.getStringSet("hidden_catalogues", mutableSetOf())

    fun pinnedCatalogues() = flowPrefs.getStringSet("pinned_catalogues", mutableSetOf())

    fun saveChaptersAsCBZ() = flowPrefs.getBoolean("save_chapter_as_cbz", true)

    fun splitTallImages() = flowPrefs.getBoolean("split_tall_images", false)

    fun downloadNewChapters() = flowPrefs.getBoolean(Keys.DOWNLOAD_NEW, false)

    fun downloadNewChaptersInCategories() = flowPrefs.getStringSet("download_new_categories", emptySet())

    fun excludeCategoriesInDownloadNew() = flowPrefs.getStringSet("download_new_categories_exclude", emptySet())

    fun autoDownloadWhileReading() = flowPrefs.getInt("auto_download_while_reading", 0)

    fun defaultCategory() = prefs.getInt(Keys.DEFAULT_CATEGORY, -2)

    fun skipRead() = prefs.getBoolean(Keys.SKIP_READ, false)

    fun skipFiltered() = prefs.getBoolean(Keys.SKIP_FILTERED, true)

    fun skipDupe() = flowPrefs.getBoolean("skip_dupe", false)

    fun useBiometrics() = flowPrefs.getBoolean(Keys.USE_BIOMETRICS, false)

    fun lockAfter() = flowPrefs.getInt(Keys.LOCK_AFTER, 0)

    fun lastUnlock() = flowPrefs.getLong(Keys.LAST_UNLOCK, 0)

    fun secureScreen() = flowPrefs.getEnum("secure_screen_v2", Values.SecureScreenMode.INCOGNITO)

    fun hideNotificationContent() = prefs.getBoolean(Keys.HIDE_NOTIFICATION_CONTENT, false)

    fun removeArticles() = flowPrefs.getBoolean(Keys.REMOVE_ARTICLES, false)

    fun migrateFlags() = flowPrefs.getInt("migrate_flags", Int.MAX_VALUE)

    fun trustedExtensions() = flowPrefs.getStringSet("trusted_extensions", emptySet())

    // using string instead of set so it is ordered
    fun migrationSources() = flowPrefs.getString("migrate_sources", "")

    fun useSourceWithMost() = flowPrefs.getBoolean("use_source_with_most", false)

    fun skipPreMigration() = flowPrefs.getBoolean(Keys.SKIP_PRE_MIGRATION, false)

    fun defaultMangaOrder() = flowPrefs.getString("default_manga_order", "")

    fun refreshCoversToo() = flowPrefs.getBoolean(Keys.REFRESH_COVERS_TOO, true)

    fun extensionUpdatesCount() = flowPrefs.getInt("ext_updates_count", 0)

    fun recentsViewType() = flowPrefs.getInt("recents_view_type", 0)

    fun showRecentsDownloads() = flowPrefs.getEnum(Keys.SHOW_DLS_IN_RECENTS, RecentMangaAdapter.ShowRecentsDLs.All)

    fun showRecentsRemHistory() = flowPrefs.getBoolean(Keys.SHOW_REM_HISTORY_IN_RECENTS, true)

    fun showReadInAllRecents() = flowPrefs.getBoolean(Keys.SHOW_READ_IN_ALL_RECENTS, false)

    fun showUpdatedTime() = flowPrefs.getBoolean(Keys.SHOW_UPDATED_TIME, false)

    fun sortFetchedTime() = flowPrefs.getBoolean("sort_fetched_time", false)

    fun collapseGroupedUpdates() = flowPrefs.getBoolean("group_chapters_updates", false)

    fun groupChaptersHistory() = flowPrefs.getEnum("group_chapters_history_type", RecentsPresenter.GroupType.ByWeek)

    fun collapseGroupedHistory() = flowPrefs.getBoolean("collapse_group_history", true)

    fun showTitleFirstInRecents() = flowPrefs.getBoolean(Keys.SHOW_TITLE_FIRST_IN_RECENTS, false)

    fun lastExtCheck() = flowPrefs.getLong("last_ext_check", 0)

    fun lastAppCheck() = flowPrefs.getLong("last_app_check", 0)

    fun checkForBetas() = flowPrefs.getBoolean("check_for_betas", BuildConfig.BETA)

    fun unreadBadgeType() = flowPrefs.getInt("unread_badge_type", 2)

    fun categoryNumberOfItems() = flowPrefs.getBoolean(Keys.CATEGORY_NUMBER_OF_ITEMS, false)

    fun hideStartReadingButton() = flowPrefs.getBoolean("hide_reading_button", false)

    fun alwaysShowChapterTransition() = flowPrefs.getBoolean(Keys.ALWAYS_SHOW_CHAPTER_TRANSITION, true)

    fun deleteRemovedChapters() = flowPrefs.getInt(Keys.DELETE_REMOVED_CHAPTERS, 0)

    fun removeBookmarkedChapters() = flowPrefs.getBoolean("pref_remove_bookmarked", false)

    fun removeExcludeCategories() = flowPrefs.getStringSet("remove_exclude_categories", emptySet())

    fun showAllCategories() = flowPrefs.getBoolean("show_all_categories", true)

    fun showAllCategoriesWhenSearchingSingleCategory() = flowPrefs.getBoolean("show_all_categories_when_searching_single_category", false)

    fun hopperGravity() = flowPrefs.getInt("hopper_gravity", 1)

    fun filterOrder() = flowPrefs.getString("filter_order", FilterBottomSheet.Filters.DEFAULT_ORDER)

    fun hopperLongPressAction() = flowPrefs.getInt(Keys.HOPPER_LONG_PRESS, 0)

    fun hideHopper() = flowPrefs.getBoolean("hide_hopper", false)

    fun autohideHopper() = flowPrefs.getBoolean(Keys.AUTO_HIDE_HOPPER, true)

    fun groupLibraryBy() = flowPrefs.getInt("group_library_by", 0)

    fun showCategoryInTitle() = flowPrefs.getBoolean("category_in_title", false)

    fun onlySearchPinned() = flowPrefs.getBoolean(Keys.ONLY_SEARCH_PINNED, false)

    fun hideInLibraryItems() = flowPrefs.getBoolean("browse_hide_in_library_items", false)

    // Tutorial preferences
    fun shownFilterTutorial() = flowPrefs.getBoolean("shown_filter_tutorial", false)

    fun shownChapterSwipeTutorial() = flowPrefs.getBoolean("shown_swipe_tutorial", false)

    fun shownDownloadQueueTutorial() = flowPrefs.getBoolean("shown_download_queue", false)

    fun shownLongPressCategoryTutorial() = flowPrefs.getBoolean("shown_long_press_category", false)

    fun shownHopperSwipeTutorial() = flowPrefs.getBoolean("shown_hopper_swipe", false)

    fun shownDownloadSwipeTutorial() = flowPrefs.getBoolean("shown_download_tutorial", false)

    fun hideBottomNavOnScroll() = flowPrefs.getBoolean(Keys.HIDE_BOTTOM_NAV_ON_SCROLL, true)

    fun sideNavIconAlignment() = flowPrefs.getInt(Keys.SIDE_NAV_ICON_ALIGNMENT, 1)

    fun showNsfwSources() = flowPrefs.getBoolean(Keys.SHOW_NSFW_SOURCE, true)

    fun themeMangaDetails() = prefs.getBoolean(Keys.THEME_MANGA_DETAILS, true)

    fun useLargeToolbar() = flowPrefs.getBoolean("use_large_toolbar", true)

    fun dohProvider() = prefs.getInt(Keys.DOH_PROVIDER, -1)

    fun defaultUserAgent() = flowPrefs.getString("default_user_agent", NetworkHelper.DEFAULT_USER_AGENT)

    fun showSeriesInShortcuts() = prefs.getBoolean(Keys.SHOW_SERIES_IN_SHORTCUTS, true)

    fun showSourcesInShortcuts() = prefs.getBoolean(Keys.SHOW_SOURCES_IN_SHORTCUTS, true)

    fun openChapterInShortcuts() = prefs.getBoolean(Keys.OPEN_CHAPTER_IN_SHORTCUTS, true)

    fun incognitoMode() = flowPrefs.getBoolean(Keys.INCOGNITO_MODE, false)

    fun hasPromptedBeforeUpdateAll() = flowPrefs.getBoolean("has_prompted_update_all", false)

    fun sideNavMode() = flowPrefs.getInt(Keys.SIDE_NAV_MODE, 0)

    fun appShouldAutoUpdate() = prefs.getInt(Keys.SHOULD_AUTO_UPDATE, AppDownloadInstallJob.ONLY_ON_UNMETERED)

    fun autoUpdateExtensions() = prefs.getInt(Keys.AUTO_UPDATE_EXTENSIONS, AppDownloadInstallJob.ONLY_ON_UNMETERED)

    fun extensionInstaller() = flowPrefs.getInt("extension_installer", ExtensionInstaller.PACKAGE_INSTALLER)

    fun filterChapterByRead() = flowPrefs.getInt(Keys.DEFAULT_CHAPTER_FILTER_BY_READ, Manga.SHOW_ALL)

    fun filterChapterByDownloaded() = flowPrefs.getInt(Keys.DEFAULT_CHAPTER_FILTER_BY_DOWNLOADED, Manga.SHOW_ALL)

    fun filterChapterByBookmarked() = flowPrefs.getInt(Keys.DEFAULT_CHAPTER_FILTER_BY_BOOKMARKED, Manga.SHOW_ALL)

    fun sortChapterOrder() = flowPrefs.getInt(Keys.DEFAULT_CHAPTER_SORT_BY_SOURCE_OR_NUMBER, Manga.CHAPTER_SORTING_SOURCE)

    fun hideChapterTitlesByDefault() = flowPrefs.getBoolean(Keys.HIDE_CHAPTER_TITLES, false)

    fun chaptersDescAsDefault() = flowPrefs.getBoolean(Keys.CHAPTERS_DESC_AS_DEFAULT, true)

    fun sortChapterByAscendingOrDescending() = prefs.getInt(Keys.DEFAULT_CHAPTER_SORT_BY_ASCENDING_OR_DESCENDING, Manga.CHAPTER_SORT_DESC)

    fun coverRatios() = flowPrefs.getStringSet(Keys.COVER_RATIOS, emptySet())

    fun coverColors() = flowPrefs.getStringSet(Keys.COVER_COLORS, emptySet())

    fun useStaggeredGrid() = flowPrefs.getBoolean("use_staggered_grid", false)
}
