package eu.kanade.tachiyomi.util.system

import android.annotation.SuppressLint
import android.os.Build
import android.view.WindowManager
import timber.log.Timber

/**
 * Helper class for detecting and optimizing for E-Ink displays,
 * specifically targeting the Kaleido 3 (e.g., Bigme Ocean 5c) and similar devices.
 *
 * Device reference specs (Ocean 5c):
 * - Processor: G99
 * - Resolution: 1680x1264
 * - Battery: 3000mAh
 * - Pixel density: Color 150PPI / B&W 300PPI
 * - RAM: 4+64GB
 */
object EInkHelper {

    /**
     * Known e-ink device manufacturers (lowercase).
     */
    private val EINK_MANUFACTURERS = setOf(
        "bigme",
        "onyx",
        "boox",
        "hisense",
        "dasung",
        "remarkable",
        "kobo",
        "pocketbook",
        "hanvon",
        "boyue",
        "likebook",
        "supernote",
        "moan",
        "meebook",
    )

    /**
     * Known e-ink device model patterns (lowercase).
     */
    private val EINK_MODEL_PATTERNS = setOf(
        "ocean",
        "kaleido",
        "boox",
        "hisense",
        "a5pro",
        "a7cc",
        "a9",
        "dasung",
        "remarkable",
        "inkpad",
        "inkpalm",
    )

    /**
     * Detects if the current device is likely an e-ink device based on manufacturer
     * and model information.
     */
    val isEInkDevice: Boolean by lazy {
        val manufacturer = Build.MANUFACTURER?.lowercase() ?: ""
        val model = Build.MODEL?.lowercase() ?: ""
        val product = Build.PRODUCT?.lowercase() ?: ""
        val brand = Build.BRAND?.lowercase() ?: ""

        val isKnownManufacturer = EINK_MANUFACTURERS.any { manufacturer.contains(it) || brand.contains(it) }
        val isKnownModel = EINK_MODEL_PATTERNS.any { model.contains(it) || product.contains(it) }
        val hasEInkSystemProp = hasEInkSystemProperty()

        val result = isKnownManufacturer || isKnownModel || hasEInkSystemProp
        if (result) {
            Timber.i("E-Ink device detected: manufacturer=%s, model=%s, product=%s", manufacturer, model, product)
        }
        result
    }

    /**
     * Checks if the device is specifically an Ocean 5c (Kaleido 3) device.
     */
    val isOcean5c: Boolean by lazy {
        val model = Build.MODEL?.lowercase() ?: ""
        val product = Build.PRODUCT?.lowercase() ?: ""
        model.contains("ocean") && (model.contains("5c") || product.contains("5c"))
    }

    /**
     * Check for e-ink system properties that may indicate an e-ink display.
     */
    @SuppressLint("PrivateApi")
    private fun hasEInkSystemProperty(): Boolean =
        try {
            val systemProperties = Class.forName("android.os.SystemProperties")
            val get = systemProperties.getDeclaredMethod("get", String::class.java)
            val einkProp = get.invoke(null, "ro.hardware.eink") as? String
            val epdProp = get.invoke(null, "ro.hardware.epd") as? String
            !einkProp.isNullOrEmpty() || !epdProp.isNullOrEmpty()
        } catch (e: Exception) {
            false
        }

    /**
     * Returns the recommended animation duration for e-ink devices.
     * For e-ink: returns 1ms (near-instant, avoids 0 which may break some views).
     * For regular devices: returns the provided default value.
     */
    fun getRecommendedAnimDuration(einkModeEnabled: Boolean, default: Int = 500): Int =
        if (einkModeEnabled) 1 else default // 1ms = near-instant, 0 may break some views

    /**
     * Whether page transitions should be used.
     * E-ink displays have slow refresh rates, so transitions are jarring.
     */
    fun shouldUsePageTransitions(einkModeEnabled: Boolean): Boolean = !einkModeEnabled

    /**
     * Whether the keep-screen-on flag should be set.
     * E-ink displays consume no power to maintain an image, so keeping screen on is unnecessary.
     */
    fun shouldKeepScreenOn(einkModeEnabled: Boolean, userPreference: Boolean): Boolean =
        if (einkModeEnabled) false else userPreference

    /**
     * Gets a reduced preload size for e-ink devices to save memory and battery.
     * Kaleido 3 devices have limited RAM (4GB) and battery (3000mAh).
     * Returns null for non-e-ink mode (use default).
     */
    fun getRecommendedPreloadSize(einkModeEnabled: Boolean, userPreference: Int): Int =
        if (einkModeEnabled) userPreference.coerceAtMost(4) else userPreference

    /**
     * Apply e-ink optimized window flags.
     * Ensures system bar backgrounds are drawn by the window rather than the system,
     * which helps reduce visual artifacts on e-ink displays.
     */
    fun applyEInkWindowFlags(window: android.view.Window, einkModeEnabled: Boolean) {
        if (einkModeEnabled) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            )
        }
    }
}
