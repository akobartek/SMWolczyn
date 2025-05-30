package pl.kapucyni.wolczyn.app.auth.domain.model

enum class UserType {
    MEMBER,
    SIGNINGS_MANAGER,
    SCOUTS_MANAGER,
    ANIMATORS_MANAGER,
    ADMIN,
    ;

    fun canManageParticipants() =
        this == ADMIN || this == SIGNINGS_MANAGER
}