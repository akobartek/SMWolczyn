package pl.kapucyni.wolczyn.app.model

data class Guest(
    var name: String = "",
    var description: String = "",
    var photoUrl: String = "",
    var sites: Array<String> = arrayOf("", "", "", "")
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Guest

        if (name != other.name) return false
        if (description != other.description) return false
        if (photoUrl != other.photoUrl) return false
        if (!sites.contentEquals(other.sites)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + photoUrl.hashCode()
        result = 31 * result + sites.contentHashCode()
        return result
    }
}