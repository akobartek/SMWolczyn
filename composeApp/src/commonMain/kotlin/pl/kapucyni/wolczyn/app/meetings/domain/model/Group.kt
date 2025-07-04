package pl.kapucyni.wolczyn.app.meetings.domain.model

data class Group(
    val number: Int,
    val members: HashMap<String, String>, // email -> data
    val animatorMail: String,
    val animatorName: String,
    val animatorContact: String,
)