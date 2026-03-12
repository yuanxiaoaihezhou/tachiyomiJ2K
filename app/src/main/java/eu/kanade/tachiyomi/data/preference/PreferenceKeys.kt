package eu.kanade.tachiyomi.data.preference

/**
 * This class stores the keys for the preferences in the application.
 */
object PreferenceKeys {
    const val NIGHT_MODE = "night_mode"
    const val LIGHT_THEME = "light_theme"
    const val DARK_THEME = "dark_theme"
    const val THEME_DARK_AMOLED = "pref_theme_dark_amoled_key"

    const val STARTING_TAB = "starting_tab"

    const val BACK_TO_START = "back_to_start"

    const val DENIED_A11_FILE_PERMISSION = "denied_a11_file_permission"

    const val ENABLE_TRANSITIONS = "pref_enable_transitions_key"

    const val PAGER_CUTOUT_BEHAVIOR = "pager_cutout_behavior"

    const val DOUBLE_TAP_ANIMATION_SPEED = "pref_double_tap_anim_speed"

    const val SHOW_PAGE_NUMBER = "pref_show_page_number_key"

    const val TRUE_COLOR = "pref_true_color_key"

    const val FULLSCREEN = "fullscreen"

    const val KEEP_SCREEN_ON = "pref_keep_screen_on_key"

    const val CUSTOM_BRIGHTNESS = "pref_custom_brightness_key"

    const val CUSTOM_BRIGHTNESS_VALUE = "custom_brightness_value"

    const val COLOR_FILTER = "pref_color_filter_key"

    const val COLOR_FILTER_VALUE = "color_filter_value"

    const val COLOR_FILTER_MODE = "color_filter_mode"

    const val DEFAULT_READING_MODE = "pref_default_reading_mode_key"

    const val DEFAULT_ORIENTATION_TYPE = "pref_default_orientation_type_key"

    const val IMAGE_SCALE_TYPE = "pref_image_scale_type_key"

    const val ZOOM_START = "pref_zoom_start_key"

    const val READER_THEME = "pref_reader_theme_key"

    const val CROP_BORDERS = "crop_borders"

    const val CROP_BORDERS_WEBTOON = "crop_borders_webtoon"

    const val READ_WITH_LONG_TAP = "reader_long_tap"

    const val READ_WITH_VOLUME_KEYS = "reader_volume_keys"

    const val READ_WITH_VOLUME_KEYS_INVERTED = "reader_volume_keys_inverted"

    const val NAVIGATION_MODE_PAGER = "reader_navigation_mode_pager"

    const val NAVIGATION_MODE_WEBTOON = "reader_navigation_mode_webtoon"

    const val PAGER_NAV_INVERTED = "reader_tapping_inverted"

    const val WEBTOON_NAV_INVERTED = "reader_tapping_inverted_webtoon"

    const val PAGE_LAYOUT = "page_layout"

    const val AUTOMATIC_SPLITS_PAGE = "automatic_splits_page"

    const val INVERT_DOUBLE_PAGES = "invert_double_pages"

    const val WEBTOON_PAGE_LAYOUT = "webtoon_page_layout"

    const val WEBTOON_INVERT_DOUBLE_PAGES = "webtoon_invert_double_pages"

    const val READER_BOTTOM_BUTTONS = "reader_bottom_buttons"

    const val SHOW_NAVIGATION_OVERLAY_NEW_USER = "reader_navigation_overlay_new_user"
    const val SHOW_NAVIGATION_OVERLAY_NEW_USER_WEBTOON = "reader_navigation_overlay_new_user_webtoon"

    const val PRELOAD_SIZE = "preload_size"

    const val WEBTOON_SIDE_PADDING = "webtoon_side_padding"

    const val WEBTOON_ENABLE_ZOOM_OUT = "webtoon_enable_zoom_out"

    const val AUTO_UPDATE_TRACK = "pref_auto_update_manga_sync_key"

    const val TRACK_MARKED_AS_READ = "track_marked_as_read"

    const val TRACKINGS_TO_ADD_ONLINE = "pref_tracking_for_online"

    const val LAST_USED_CATALOGUE_SOURCE = "last_catalogue_source"

    const val LAST_USED_CATEGORY = "last_used_category"

    const val CATALOGUE_AS_LIST = "pref_display_catalogue_as_list"

    const val ENABLED_LANGUAGES = "source_languages"

    const val SOURCES_SORT = "sources_sort"

    const val BACKUP_DIRECTORY = "backup_directory"

    const val DOWNLOADS_DIRECTORY = "download_directory"

    const val DOWNLOAD_ONLY_OVER_WIFI = "pref_download_only_over_wifi_key"

    const val SHOW_LIBRARY_SEARCH_SUGGESTIONS = "show_library_search_suggestions"

    const val LIBRARY_SEARCH_SUGGESTION = "library_search_suggestion"

    const val NUMBER_OF_BACKUPS = "backup_slots"

    const val BACKUP_INTERVAL = "backup_interval"

    const val REMOVE_AFTER_READ_SLOTS = "remove_after_read_slots"

    const val DELETE_REMOVED_CHAPTERS = "delete_removed_chapters"

    const val REMOVE_AFTER_MARKED_AS_READ = "pref_remove_after_marked_as_read_key"

    const val LIBRARY_UPDATE_INTERVAL = "pref_library_update_interval_key"

    const val FILTER_DOWNLOADED = "pref_filter_downloaded_key"

    const val FILTER_UNREAD = "pref_filter_unread_key"

    const val FILTER_COMPLETED = "pref_filter_completed_key"

    const val FILTER_TRACKED = "pref_filter_tracked_key"

    const val FILTER_MANGA_TYPE = "pref_filter_manga_type_key"

    const val SHOW_EMPTY_CATEGORIES_FILTERING = "show_empty_categories_filtering"

    const val AUTOMATIC_EXT_UPDATES = "automatic_ext_updates"

    const val INSTALLED_EXTENSIONS_ORDER = "installed_extensions_order"

    const val AUTO_HIDE_HOPPER = "autohide_hopper"

    const val HOPPER_LONG_PRESS = "hopper_long_press"

    const val ONLY_SEARCH_PINNED = "only_search_pinned"

    const val DOWNLOAD_NEW = "download_new"

    const val LIBRARY_LAYOUT = "pref_display_library_layout"

    const val GRID_SIZE = "grid_size_float"

    const val UNIFORM_GRID = "uniform_grid"

    const val OUTLINE_ON_COVERS = "outline_on_covers"

    const val DATE_FORMAT = "app_date_format"

    const val DEFAULT_CATEGORY = "default_category"

    const val SKIP_READ = "skip_read"

    const val SKIP_FILTERED = "skip_filtered"

    const val DOWNLOAD_BADGE = "display_download_badge"

    const val LANGUAGE_BADGE = "display_language_badge"

    const val USE_BIOMETRICS = "use_biometrics"

    const val LOCK_AFTER = "lock_after"

    const val LAST_UNLOCK = "last_unlock"

    const val HIDE_NOTIFICATION_CONTENT = "hide_notification_content"

    const val REMOVE_ARTICLES = "remove_articles"

    const val SKIP_PRE_MIGRATION = "skip_pre_migration"

    const val REFRESH_COVERS_TOO = "refresh_covers_too"

    const val SHOW_DLS_IN_RECENTS = "show_dls_in_recents"
    const val SHOW_REM_HISTORY_IN_RECENTS = "show_rem_history_in_recents"
    const val SHOW_READ_IN_ALL_RECENTS = "show_read_in_all_recents"
    const val SHOW_TITLE_FIRST_IN_RECENTS = "show_title_first_in_recents"

    const val SHOW_UPDATED_TIME = "show_updated_time"

    const val CATEGORY_NUMBER_OF_ITEMS = "display_number_of_items"

    const val ALWAYS_SHOW_CHAPTER_TRANSITION = "always_show_chapter_transition"

    const val HIDE_BOTTOM_NAV_ON_SCROLL = "hide_bottom_nav_on_scroll"

    const val SIDE_NAV_ICON_ALIGNMENT = "pref_side_nav_icon_alignment"

    const val SHOW_SERIES_IN_SHORTCUTS = "show_series_shortcuts"
    const val SHOW_SOURCES_IN_SHORTCUTS = "show_sources_shortcuts"
    const val OPEN_CHAPTER_IN_SHORTCUTS = "open_chapter_shortcuts"

    const val DOH_PROVIDER = "doh_provider"

    const val USE_SHIZUKU = "use_shizuku"

    const val SHOW_NSFW_SOURCE = "show_nsfw_source"

    const val THEME_MANGA_DETAILS = "theme_manga_details"

    const val INCOGNITO_MODE = "incognito_mode"

    const val SIDE_NAV_MODE = "side_nav_mode"

    const val SHOULD_AUTO_UPDATE = "should_auto_update"

    const val AUTO_UPDATE_EXTENSIONS = "auto_update_extensions"

    const val DEFAULT_CHAPTER_FILTER_BY_READ = "default_chapter_filter_by_read"

    const val DEFAULT_CHAPTER_FILTER_BY_DOWNLOADED = "default_chapter_filter_by_downloaded"

    const val DEFAULT_CHAPTER_FILTER_BY_BOOKMARKED = "default_chapter_filter_by_bookmarked"

    const val DEFAULT_CHAPTER_SORT_BY_SOURCE_OR_NUMBER = "default_chapter_sort_by_source_or_number" // and upload date

    const val DEFAULT_CHAPTER_SORT_BY_ASCENDING_OR_DESCENDING = "default_chapter_sort_by_ascending_or_descending"

    const val COVER_RATIOS = "cover_ratio"

    const val COVER_COLORS = "cover_colors"

    const val HIDE_CHAPTER_TITLES = "hide_chapter_titles"

    const val CHAPTERS_DESC_AS_DEFAULT = "chapters_desc_as_default"

    const val EINK_MODE = "pref_eink_mode"

    const val EINK_REFRESH_MODE = "pref_eink_refresh_mode"
}
