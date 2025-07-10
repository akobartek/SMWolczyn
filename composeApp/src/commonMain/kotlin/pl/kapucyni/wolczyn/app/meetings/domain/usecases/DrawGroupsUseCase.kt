package pl.kapucyni.wolczyn.app.meetings.domain.usecases

import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.toGroupMembers

class DrawGroupsUseCase {

    operator fun invoke(
        participants: List<Participant>,
        animators: List<Participant>,
    ): List<Group> {
        val sortedParticipants = participants.sortedBy { it.getAge() }
        val shuffledAnimators = animators.shuffled()

        val baseGroupSize = participants.size / shuffledAnimators.size
        val groupsWithExtraPerson = participants.size % shuffledAnimators.size

        val groups = mutableListOf<Group>()
        var currentIndex = 0
        shuffledAnimators.forEachIndexed { index, animator ->
            val currentGroupSize =
                if (index < groupsWithExtraPerson) baseGroupSize + 1
                else baseGroupSize

            val endIndex = (currentIndex + currentGroupSize).coerceAtMost(participants.size)
            val members = if (currentIndex < participants.size) {
                sortedParticipants.subList(currentIndex, endIndex).toGroupMembers()
            } else {
                hashMapOf()
            }
            currentIndex = endIndex

            groups.add(
                Group(
                    number = index + 1,
                    members = members,
                    animatorMail = animator.email,
                    animatorName = "${animator.firstName} ${animator.lastName}",
                    animatorContact = "",
                )
            )
        }
        return groups
    }
}