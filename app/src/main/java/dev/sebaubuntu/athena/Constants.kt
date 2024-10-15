package dev.sebaubuntu.athena

const val EXTRA_CHARGING_STATUS = "android.os.extra.CHARGING_STATUS"
const val EXTRA_CYCLE_COUNT = "android.os.extra.CYCLE_COUNT"
const val BATTERY_PROPERTY_MANUFACTURING_DATE = 7
const val BATTERY_PROPERTY_FIRST_USAGE_DATE = 8
const val BATTERY_PROPERTY_CHARGING_POLICY = 9
const val BATTERY_PROPERTY_STATE_OF_HEALTH = 10
const val BATTERY_STATS_PERM = "android.permission.BATTERY_STATS"

const val BATTERY_USAGE_DATE_IN_EPOCH_MIN: Long = 1606780800 // 2020-12-01
const val BATTERY_USAGE_DATE_IN_EPOCH_MAX: Long = 2147472000 // 2038-01-19

/**
 * Default policy (e.g. normal).
 */
const val CHARGING_POLICY_DEFAULT: Int = 1

/**
 * Optimized for battery health using static thresholds (e.g stop at 80%).
 */
const val CHARGING_POLICY_ADAPTIVE_AON: Int = 2

/**
 * Optimized for battery health using adaptive thresholds.
 */
const val CHARGING_POLICY_ADAPTIVE_AC: Int = 3

/**
 * Optimized for battery health, devices always connected to power.
 */
const val CHARGING_POLICY_ADAPTIVE_LONGLIFE: Int = 4
