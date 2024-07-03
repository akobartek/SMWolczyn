package pl.kapucyni.wolczyn.app.breviary.domain.model

import pl.kapucyni.wolczyn.app.breviary.domain.model.Breviary.BreviaryHtml

data class BreviaryDay(
    val id: Long = 0L,
    val date: String,
    val invitatory: BreviaryHtml? = null,
    val officeOfReadings: BreviaryHtml? = null,
    val lauds: BreviaryHtml? = null,
    val prayer1: BreviaryHtml? = null,
    val prayer2: BreviaryHtml? = null,
    val prayer3: BreviaryHtml? = null,
    val vespers: BreviaryHtml? = null,
    val compline: BreviaryHtml? = null,
)
