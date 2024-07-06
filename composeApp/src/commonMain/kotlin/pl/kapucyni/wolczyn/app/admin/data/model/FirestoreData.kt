package pl.kapucyni.wolczyn.app.admin.data.model

import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem
import pl.kapucyni.wolczyn.app.quiz.data.model.FirestoreQuiz
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizResult
import pl.kapucyni.wolczyn.app.shop.data.model.FirestoreShopProduct

data class FirestoreData(
    val kitchenMenuItems: List<FirestoreMenuItem>,
    val kitchenPromotions: List<FirestorePromotion>,
    val kitchenQuiz: FirestoreQuiz?,
    val kitchenQuizResults: List<QuizResult>,
    val shopProducts: List<FirestoreShopProduct>,
    val shopPromotions: List<FirestorePromotion>,
)