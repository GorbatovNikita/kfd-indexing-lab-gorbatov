package lab2

class ProductCatalog(products: List<Product>) {
    private val productsById: Map<Long, Product> = products.associateBy { it.id }

    fun select(
        recommendedIds: List<Long>,
        excludedIds: List<Long>,
        limit: Int
    ): List<Product> = selectInternal(recommendedIds, excludedIds, limit, null)

    internal fun selectInternal(
        recommendedIds: List<Long>,
        excludedIds: List<Long>,
        limit: Int,
        onRecommendationVisited: (() -> Unit)?
    ): List<Product> {
        if (limit == 0 || recommendedIds.isEmpty() || productsById.isEmpty()) {
            return emptyList()
        }

        val excluded = excludedIds.toHashSet()
        val selectedIds = HashSet<Long>()
        val result = ArrayList<Product>()

        for (id in recommendedIds) {
            onRecommendationVisited?.invoke()

            if (id in excluded || id in selectedIds) continue

            val product = productsById[id] ?: continue

            if (product.available) {
                selectedIds.add(id)
                result.add(product)

                if (result.size == limit) break
            }
        }

        return result
    }
}

