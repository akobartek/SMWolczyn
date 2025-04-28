package pl.kapucyni.wolczyn.app.auth.presentation.edit

data class EditProfileScreenState(
    val loading: Boolean = false,
    val firstName: String,
    val firstNameError: Boolean = false,
    val lastName: String,
    val lastNameError: Boolean = false,
    val city: String,
    val cityError: Boolean = false,
    val birthdayDate: Long,
    val noInternetDialogVisible: Boolean = false,
)