package de.westnordost.streetcomplete.data.osm.mapdata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.lang.System.currentTimeMillis

@Serializable
sealed class Element {
    abstract val id: Long
    abstract val version: Int
    abstract val tags: Map<String, String>
    abstract val timestampEdited: Long
    abstract val type: ElementType

    var isModified: Boolean = false
    var isDeleted: Boolean = false
}

@Serializable
@SerialName("node")
data class Node(
    override val id: Long,
    val position: LatLon,
    override val tags: Map<String, String> = HashMap(0),
    override val version: Int = 1,
    override val timestampEdited: Long = currentTimeMillis()
) : Element() {
    override val type get() = ElementType.NODE
}

@Serializable
@SerialName("way")
data class Way(
    override val id: Long,
    val nodeIds: List<Long>,
    override val tags: Map<String, String> = HashMap(0),
    override val version: Int = 1,
    override val timestampEdited: Long = currentTimeMillis()
) : Element() {
    override val type = ElementType.WAY

    val isClosed get() = nodeIds.size >= 3 && nodeIds.first() == nodeIds.last()
}

@Serializable
@SerialName("relation")
data class Relation(
    override val id: Long,
    val members: List<RelationMember>,
    override val tags: Map<String, String> = HashMap(0),
    override val version: Int = 1,
    override val timestampEdited: Long = currentTimeMillis()
) : Element() {
    override val type = ElementType.RELATION
}

@Serializable
data class RelationMember(val type: ElementType, val ref: Long, val role: String)

enum class ElementType { NODE, WAY, RELATION }

@Serializable
data class LatLon(val latitude: Double, val longitude: Double) {
    init {
        checkValidity(latitude, longitude)
    }

    companion object {
        fun checkValidity(latitude: Double, longitude: Double) {
            require(
                latitude >= -90.0 && latitude <= +90 &&
                    longitude >= -180 && longitude <= +180
            ) { "Latitude $latitude, longitude $longitude is not a valid position"}
        }
    }
}
