package dev.toast.toastsMCToolBox.lib.overrides

fun Int.isNegative(): Boolean {
    return this < 0
}

fun Int.isPositive(): Boolean {
    return this > 0
}