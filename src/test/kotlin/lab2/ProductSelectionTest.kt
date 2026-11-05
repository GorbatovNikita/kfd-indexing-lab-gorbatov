package lab2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductSelectionTest {
    @Test
    fun selectsAvailableProductsInRecommendationOrder() {
        val keyboard = Product(1, "Keyboard", true)
        val mouse = Product(2, "Mouse", false)
        val monitor = Product(3, "Monitor", true)
        val webcam = Product(4, "Webcam", true)
        val products = listOf(keyboard, mouse, monitor, webcam)

        val result = selectProducts(
            products,
            listOf(3L, 2L, 3L, 99L, 1L, 4L),
            listOf(1L)
        )

        assertEquals(listOf(monitor, webcam), result)
    }

    @Test
    fun returnsEmptyListWhenThereAreNoRecommendations() {
        val products = listOf(Product(1, "Keyboard", true))

        val result = selectProducts(products, emptyList(), emptyList())

        assertEquals(emptyList<Product>(), result)
    }

    @Test
    fun handlesRepeatedRecommendationsAndExclusionsAndUnknownIds() {
        val a = Product(10, "A", true)
        val b = Product(20, "B", true)
        val c = Product(30, "C", true)

        val result = selectProducts(
            listOf(c, b, a),
            listOf(20, 20, 999, 10, 30, 10, 30),
            listOf(999, 999, 30, 30)
        )

        assertEquals(listOf(b, a), result)
    }

    @Test
    fun returnsEmptyWhenEveryRecommendedProductIsUnavailableExcludedOrUnknown() {
        val unavailable = Product(1, "Unavailable", false)
        val excluded = Product(2, "Excluded", true)

        assertEquals(
            emptyList<Product>(),
            selectProducts(
                listOf(unavailable, excluded),
                listOf(1, 2, 404, 1, 2),
                listOf(2)
            )
        )
    }

    @Test
    fun handlesEmptyCatalogAndEmptyExclusions() {
        val available = Product(1, "Available", true)

        assertEquals(
            emptyList<Product>(),
            selectProducts(emptyList(), listOf(1), emptyList())
        )

        assertEquals(
            listOf(available),
            selectProducts(listOf(available), listOf(1), emptyList())
        )

        assertEquals(
            emptyList<Product>(),
            selectProducts(listOf(available), listOf(1), listOf(1))
        )
    }

    @Test
    fun doesNotModifyInputsAndRepeatedCallsAreIndependent() {
        val first = Product(1, "First", true)
        val second = Product(2, "Second", true)

        val products = mutableListOf(first, second)
        val recommendations = mutableListOf(1L, 2L, 1L)
        val exclusions = mutableListOf(2L)

        val productsBefore = products.toList()
        val recommendationsBefore = recommendations.toList()
        val exclusionsBefore = exclusions.toList()

        val firstResult = selectProducts(products, recommendations, exclusions)
        val secondResult = selectProducts(products, recommendations, emptyList())

        assertEquals(listOf(first), firstResult)
        assertEquals(listOf(first, second), secondResult)

        assertEquals(productsBefore, products)
        assertEquals(recommendationsBefore, recommendations)
        assertEquals(exclusionsBefore, exclusions)

        assertNotSame(firstResult, secondResult)
        assertSame(first, firstResult.single())
    }
}
