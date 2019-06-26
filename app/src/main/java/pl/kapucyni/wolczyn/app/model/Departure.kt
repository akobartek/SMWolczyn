package pl.kapucyni.wolczyn.app.model

data class Departure(
    val city: String?,
    val patron: String?,
    val from: String?,
    val contact_person: String?,
    val contact_phone: String?,
    val contact_email: String?,
    val signin_url: String?,
    val transport_type: String?,
    val direction: String?,
    val notes: String?
)