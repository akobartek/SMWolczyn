package pl.kapucyni.wolczyn.app.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.TextNode
import pl.kapucyni.wolczyn.app.utils.PreferencesManager
import java.util.*

class BreviaryViewModel : ViewModel() {

    private val mBreviaryUrlTypes = arrayOf(
        "wezw", "godzczyt", "jutrznia", "modlitwa1",
        "modlitwa2", "modlitwa3", "nieszpory", "kompleta"
    )


    @Suppress("BlockingMethodInNonBlockingContext")
    fun checkIfThereAreMultipleOffices(
        handleOfficesResult: (offices: List<Pair<String, String>>?) -> Unit,
        onLoadFailed: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val document = withContext(Dispatchers.IO) {
                    Jsoup.connect(buildBaseBreviaryUrl(true) + "index.php3")
                        .timeout(15000).get()
                }
                if (!document.html().contains("WYBIERZ OFICJUM", true))
                    handleOfficesResult(null)
                else {
                    val offices = arrayListOf<Pair<String, String>>()
                    val officesDivs = document.select("div")
                        .last { it.html().contains("OFICJUM") }
                        .selectFirst("table")
                        ?.selectFirst("tbody")
                        ?.select("div")
                    officesDivs?.forEach {
                        offices.add(
                            Pair(
                                it.selectFirst("a")!!.attr("href")
                                    .split("/")[1].replace("\n", ""),
                                it.text()
                            )
                        )
                    }
                    handleOfficesResult(offices)
                }
            } catch (exc: Exception) {
                Log.e("BreviaryViewModel", exc.toString())
                onLoadFailed()
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun loadBreviaryHtml(
        office: String,
        type: Int,
        onLoadSuccess: (String) -> Unit,
        onLoadFailed: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val breviaryUrl = buildBaseBreviaryUrl(office == "") +
                        "${office}/${mBreviaryUrlTypes[type]}.php3"
                val document = withContext(Dispatchers.IO) {
                    Jsoup.connect(breviaryUrl).timeout(30000).get()
                }
                onLoadSuccess(checkBreviaryNightMode(getFinalBreviaryHtml(document, type == 1)))
            } catch (exc: Exception) {
                Log.e("BreviaryViewModel", exc.toString())
                onLoadFailed()
            }
        }
    }

    private fun buildBaseBreviaryUrl(withDays: Boolean): String {
        val romanMonths =
            arrayOf("i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x", "xi", "xii")
        val calendar = Calendar.getInstance()
        val dayInt = calendar.get(Calendar.DAY_OF_MONTH)
        val day = if (dayInt < 10) "0$dayInt" else dayInt.toString()
        val monthInt = calendar.get(Calendar.MONTH) + 1
        val month = if (monthInt < 10) "0$monthInt" else monthInt.toString()
        val year = calendar.get(Calendar.YEAR).toString().substring(2)

        return if (withDays) "https://brewiarz.pl/${romanMonths[monthInt - 1]}_$year/$day$month/"
        else "https://brewiarz.pl/${romanMonths[monthInt - 1]}_$year/"
    }

    private fun getFinalBreviaryHtml(document: Document, isOfficeOfReadings: Boolean): String {
        val element =
            document.select("table").last { it.outerHtml().contains("Psalm ") }

        if (isOfficeOfReadings) {
            element.select("i").first { it.html().contains("Te Deum") }.parentNode()?.remove()
            if (!element.html().contains("\"def1\"")) {
                val def1Elem = document.getElementById("def1")
                def1Elem?.let { elem ->
                    element.appendChild(elem.child(0))
                    element.append("<br><br>")
                    element.appendChild(
                        elem.parent()!!.select("table")
                            .first { it.html().contains("RESPONSORIUM") })
                }
            }
        }

        // Remove all images
        element.select("img").remove()
        // Remove Premium
        element.select("a")
            .filter { it.attr("href").contains("premium") || it.attr("href").contains("access") }
            .forEach { it.parentNode()?.remove() }
        // Remove different variants
        element.select("ul").forEach { ulElem ->
            val idsToRemove = ulElem.select("a").map { it.attr("rel") }
            idsToRemove.forEachIndexed { index, id ->
                if (index > 0) element.getElementById(id)?.remove()
            }
            ulElem.remove()
        }
        // Remove unnecessary links
        element.select("a")
            .filter { it.attr("href").contains("javascript") || it.hasAttr("onclick") }
            .forEach { it.remove() }
        element.select("a")
            .lastOrNull { it.attr("href").contains("jutrznia") }?.parentNode()?.remove()
        // Clear text links
        element.select("a").filter { it.attr("href").contains("appendix") }.forEach {
            it.replaceWith(TextNode(it.text()))
        }

        var breviary = element.html()
        for (i in 1..5)
            breviary = breviary.replaceFirst("class=\"c\"", "class=\"xD\"")
        return breviary
            .replace("<tr><td colspan=2 width=490 class=ww>\n", "")
            .replace("width=\"490\"", "")
            .replace("width:490px", "")
            .replace("color=\"red\">", "color=\"saddlebrown\">")
            .replace("color=\"Red\">", "color=\"saddlebrown\">")
            .replace("color:red", "color:saddlebrown")
            .replace("color:Red", "color:saddlebrown")
            .replace("</a> - ", "</a>")
            .replace("align=\"center\"", "")
            .replace("class=\"b\"", "style=\"text-indent:12pt\"")
            .replace("class=\"c\"", "style=\"text-indent:16pt\"")
            .replace("style=\"margin-left:15pt\"", "")
            .replace("font-size:10pt", "")
            .replace("font-size: 10pt", "")
            .replace("font-size:8pt", "")
            .replace("font-size: 8pt", "")
            .replaceFirst("<br>", "")
    }

    private fun checkBreviaryNightMode(breviaryHtml: String): String {
        return if (PreferencesManager.getNightMode()) {
            val result = "<html><head>" +
                    "<style type=\"text/css\">body{color: #fff; background-color: #160A01;}" +
                    "</style></head>" +
                    "<body>" +
                    breviaryHtml +
                    "</body></html>"
            result.replace("black", "white")
        } else breviaryHtml
    }
}