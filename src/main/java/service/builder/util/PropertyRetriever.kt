package service.builder.util

import java.util.*

object PropertyRetriever {

    private const val configFileName = "configProperties.properties"

    private val configStreamResource = javaClass.classLoader.getResourceAsStream(configFileName)
    private val loadedConfigProperties = loadResource()

    fun getShpockBaseUrl(): String = loadedConfigProperties.getProperty("shpockRequestUrl")

    private fun loadResource(): Properties {
        val configProperties = Properties()
        configProperties.load(configStreamResource)

        return configProperties
    }
}