package service.builder.shpock

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import service.builder.IResourceBuilder
import service.builder.Resource
import service.builder.util.PropertyRetriever
import java.math.BigDecimal

class ShpockResourceBuilder : IResourceBuilder {

    val productPriceRegex = Regex("£(\\d+.\\d\\d)")

    private val shpockBaseRequestUrl = PropertyRetriever.getShpockBaseUrl()

    override fun build(productName: String): List<Resource> {

        val shpockRequestUrlWithProductName = generateShpockUrlWithProductName(productName)

        val shpockSearchResultPageBody = getShpockSearchResultsPageBodyElement(shpockRequestUrlWithProductName)

        val shpockSearchResultsCards = getShpockSearchResultCards(shpockSearchResultPageBody)

        return createResources(shpockSearchResultsCards)
    }

    private fun generateShpockUrlWithProductName(productName: String): String = shpockBaseRequestUrl + productName

    private fun getShpockSearchResultsPageBodyElement(shpockRequestUrlWithProductName: String): Element {
        return Jsoup.connect(shpockRequestUrlWithProductName).get().body()
    }

    private fun getShpockSearchResultCards(shpockSearchResultPageBody: Element): Elements? {
        return shpockSearchResultPageBody.select("[data-testid^=SearchResultItemCard]")
    }

    private fun createResources(shpockSearchResultsCards: Elements?): List<Resource> {

        return shpockSearchResultsCards!!.map {

            val productName = getProductNameFromShpockSearchResultCard(it)
            val price = getPriceFromShpockSearchResultCard(it)

            Resource(productName, price)
        }
    }

    private fun getProductNameFromShpockSearchResultCard(shpockSearchResultsCard: Element): String {
        return "Airpods"
    }

    private fun getPriceFromShpockSearchResultCard(shpockSearchResultsCard: Element): BigDecimal {
        val shpockSearchResultsCardText = shpockSearchResultsCard.text()

        val shpockProductPrice = productPriceRegex.find(shpockSearchResultsCardText)!!.groups.last()!!.value

        return shpockProductPrice.toBigDecimal()
    }
}