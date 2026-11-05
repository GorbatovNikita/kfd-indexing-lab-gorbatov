
package lab2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ProductCatalogTest {

    @Test
    fun zeroLimitReturnsEmptyList() {
        val catalog = ProductCatalog(listOf(Product(1, "A", true)))

        assertEquals(
            emptyList<Product>(),
            catalog.select(listOf(1), emptyList(), 0)
        )
    }

    @Test
    fun stopsAsSoonAsLimitIsReached() {
        val a = Product(1, "A", true)
        val b = Product(2, "B", true)
        val catalog = ProductCatalog(listOf(a, b))

        val recommendations = listOf(1L, 2L, 3L, 4L, 5L)
        var visited = 0

        val result = catalog.selectInternal(
            recommendations,
            emptyList(),
            1
        ) {
            visited++
        }

        assertEquals(listOf(a), result)
        assertEquals(1, visited)
    }

    @Test
    fun returnsAllMatchesWhenThereAreFewerThanLimit() {
        val a = Product(1, "A", true)
        val b = Product(2, "B", false)
        val catalog = ProductCatalog(listOf(a, b))

        assertEquals(
            listOf(a),
            catalog.select(listOf(2, 1, 3), emptyList(), 10)
        )
    }

    @Test
    fun requestsToSameCatalogDoNotShareRequestState() {
        val a = Product(1, "A", true)
        val b = Product(2, "B", true)
        val catalog = ProductCatalog(listOf(a, b))

        assertEquals(
            listOf(a),
            catalog.select(listOf(1, 2), listOf(2), 10)
        )

        assertEquals(
            listOf(a, b),
            catalog.select(listOf(1, 2), emptyList(), 10)
        )

        assertEquals(
            listOf(b),
            catalog.select(listOf(2, 1), listOf(1), 10)
        )
    }

    @Test
    fun newCatalogInstanceUsesNewCatalogData() {
        val oldCatalog = ProductCatalog(listOf(Product(1, "Old", true)))
        val newProduct = Product(2, "New", true)
        val newCatalog = ProductCatalog(listOf(newProduct))

        assertEquals(
            listOf(newProduct),
            newCatalog.select(listOf(2), emptyList(), 10)
        )

        assertEquals(
            emptyList<Product>(),
            oldCatalog.select(listOf(2), emptyList(), 10)
        )
    }
}
