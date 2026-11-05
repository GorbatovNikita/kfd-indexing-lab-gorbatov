package lab2

fun selectProducts(
    products: List<Product>,
    recommendedIds: List<Long>,
    excludedIds: List<Long>
): List<Product> {
    if (products.isEmpty() || recommendedIds.isEmpty()) return emptyList()

    val productsById = products.associateBy { it.id }
    val excluded = excludedIds.toHashSet()
    val selectedIds = HashSet<Long>()
    val result = ArrayList<Product>()

    for (id in recommendedIds) {
        if (id in excluded || id in selectedIds) continue

        val product = productsById[id] ?: continue

        if (product.available) {
            selectedIds.add(id)
            result.add(product)
        }
    }

    return result
}
