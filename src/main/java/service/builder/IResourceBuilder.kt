package service.builder

interface IResourceBuilder {

    fun build(productName: String): List<Resource>
}