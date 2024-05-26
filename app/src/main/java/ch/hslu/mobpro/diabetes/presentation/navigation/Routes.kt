package ch.hslu.mobpro.diabetes.presentation.navigation

object Routes {
    const val home = "home"
    const val products = "products"
    const val uneditableProducts = "uneditableProducts"
    const val resultScreen = "result_screen/{glucoseLevel}"
    const val user = "user"
    const val composeMeal = "compose_meal"
    const val dashboard = "dashboard"
    const val profile = "profile"
    const val userFormCreate = "user_form"
    const val userFormEdit = "user_form/{userName}"
    fun userForm(userName: String) = "user_form/$userName"
    fun resultScreen(glucoseLevel: Float) = "result_screen/$glucoseLevel"
}
