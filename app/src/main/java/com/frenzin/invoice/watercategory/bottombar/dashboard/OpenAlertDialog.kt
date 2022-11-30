package com.frenzin.invoice.watercategory.bottombar.dashboard


interface OpenAlertDialog {
    fun openAlertDialog(
        id: String,
        fname: String?,
        teaItemMrng: Int,
        coffeeItemMrng: Int,
        teaItemEvng: Int,
        coffeeItemEvng: Int
    )
    fun showMessage()
}